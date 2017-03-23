package com.jade.customervisit.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.xutils.common.Callback.Cancelable;

import com.jade.customervisit.R;
import com.jade.customervisit.adapter.ServiceContentAdapter;
import com.jade.customervisit.bean.ServiceContent;
import com.jade.customervisit.bean.QueryServiceContentResult;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout.OnRefreshListener;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayoutDirection;
import com.jade.customervisit.util.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ServiceContentView extends LinearLayout implements
        OnRefreshListener, OnItemClickListener, IServiceView {

    private ListView lv;
    private SwipeRefreshLayout swipe;
    private ProgressBar progressBar;

    private ServiceContentAdapter adapter;
    public int page = 1;
    private int total = 0;
    public String keyword = "";

    /**
     * 服务列表
     */
    private List<ServiceContent> dataInfo = new ArrayList<ServiceContent>();

    /** http请求处理器，用于取消请求 */
    Cancelable httpHandler;

    public ServiceContentView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_service_list,
                this);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        lv = (ListView) findViewById(R.id.service_listview);
        lv.setOnItemClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        adapter = new ServiceContentAdapter(dataInfo, getContext());
        lv.setAdapter(adapter);
        queryServiceContentList();
    }

    public void queryServiceContentList() {
        if (page > 1 && (page - 1) * ServiceManager.LIMIT >= total) {
            ToastUtil.showShort(getContext(), "没有更多数据了");
            swipe.setRefreshing(false);
            return;
        }
        RequestListener<QueryServiceContentResult> callback = new RequestListener<QueryServiceContentResult>() {

            @Override
            public void onStart() {
                swipe.post(new Runnable() {

                    @Override
                    public void run() {
                        swipe.setRefreshing(true);
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode,
                    QueryServiceContentResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        dataInfo = result.getServiceContentList();
                        total = result.getTotal();
                        setData(dataInfo);
                    } else if (result.getRetcode().equals("000004")) {
                        if (page == 1) {
                            ToastUtil.showShort(getContext(), "没有数据");
                        } else {
                            ToastUtil.showShort(getContext(),
                                    result.getRetinfo());
                        }
                    } else {
                        ToastUtil.showShort(getContext(), result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(getContext(),
                            R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
                ToastUtil.showShort(getContext(), R.string.connect_exception);
            }

            @Override
            public void onStopped() {
                swipe.setRefreshing(false);
            }
        };

        // 开始请求列表数据
        httpHandler = ServiceManager.queryServiceContent(0, page, keyword,
                callback);
    }

    private void setData(List<ServiceContent> data) {
        if (page == 1) {
            adapter.setData(data);
        } else {
            adapter.addData(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if (SwipeRefreshLayoutDirection.TOP == direction) {
            page = 1;
            keyword = "";
            queryServiceContentList();
        } else if (SwipeRefreshLayoutDirection.BOTTOM == direction) {
            page++;
            queryServiceContentList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent(getContext(), ServiceMainActivity.class);
        intent.putExtra("serviceContent", dataInfo.get(position));
        getContext().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 1;
        keyword = "";
        queryServiceContentList();
    }
}
