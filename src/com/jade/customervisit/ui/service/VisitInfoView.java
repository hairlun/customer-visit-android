package com.jade.customervisit.ui.service;

import java.util.ArrayList;
import java.util.List;

import com.jade.customervisit.R;
import com.jade.customervisit.adapter.VisitInfoAdapter;
import com.jade.customervisit.bean.GetVisitInfoResult;
import com.jade.customervisit.bean.VisitInfo;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout.OnRefreshListener;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayoutDirection;
import com.jade.customervisit.util.ToastUtil;
import com.lidroid.xutils.http.HttpHandler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class VisitInfoView extends LinearLayout implements OnRefreshListener, OnItemClickListener,
		IServiceView {

	private ListView lv;
	private SwipeRefreshLayout swipe;
	private ProgressBar progressBar;

    private VisitInfoAdapter adapter;
    public int page = 1;
    private int total = 0;
    public String keyword = "";
    
    private List<VisitInfo> dataInfo = new ArrayList<VisitInfo>();

    /** http请求处理器，用于取消请求 */
    HttpHandler<String> httpHandler;

	public VisitInfoView(Context context) {
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
        
        adapter = new VisitInfoAdapter(dataInfo, getContext());
        lv.setAdapter(adapter);
        getVisitInfoList();
    }

    public void getVisitInfoList() {
        if (page > 1 && (page - 1) * ServiceManager.LIMIT >= total) {
            ToastUtil.showShort(getContext(), "没有更多数据了");
            swipe.setRefreshing(false);
            return;
        }
        RequestListener<GetVisitInfoResult> callback = new RequestListener<GetVisitInfoResult>() {

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
            public void onSuccess(int statusCode, GetVisitInfoResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        dataInfo = result.getVisitInfoList();
                        total = result.getTotal();
                        setData(dataInfo);
                    } else if (result.getRetcode().equals("000004")) {
                        if (page == 1) {
                            ToastUtil.showShort(getContext(), "没有数据");
                        } else {
                            ToastUtil.showShort(getContext(), result.getRetinfo());
                        }
                    } else {
                        ToastUtil.showShort(getContext(), result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(getContext(), R.string.connect_exception);
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
        httpHandler = ServiceManager.getVisitInfo(0, page, keyword, callback);
    }
    
    private void setData(List<VisitInfo> data) {
        if (page == 1) {
            adapter.setData(data);
        } else {
            adapter.addData(data);
        }
        adapter.notifyDataSetChanged();
    }

    /* (non-Javadoc)
     * @see com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout.OnRefreshListener#onRefresh(com.jade.customervisit.ui.view.swipe.SwipeRefreshLayoutDirection)
     */
    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if (SwipeRefreshLayoutDirection.TOP == direction) {
            page = 1;
            keyword = "";
            getVisitInfoList();
        } else if (SwipeRefreshLayoutDirection.BOTTOM == direction) {
            page++;
            getVisitInfoList();
        }
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        
    }

    @Override
    public void onRefresh() {
    	page = 1;
        keyword = "";
        getVisitInfoList();
    }

}
