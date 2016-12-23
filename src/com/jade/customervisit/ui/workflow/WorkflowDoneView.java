/**
 * CustomerVisit
 * WorkflowDoneView
 * zhoushujie
 * 2016年12月21日 上午10:27:37
 */
package com.jade.customervisit.ui.workflow;

import java.util.ArrayList;
import java.util.List;

import org.xutils.common.Callback.Cancelable;

import com.jade.customervisit.R;
import com.jade.customervisit.adapter.WorkflowDoneAdapter;
import com.jade.customervisit.bean.QueryWorkflowResult;
import com.jade.customervisit.bean.Workflow;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.ui.view.IListView;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayoutDirection;
import com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout.OnRefreshListener;
import com.jade.customervisit.util.ToastUtil;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author zhoushujie
 *
 */
public class WorkflowDoneView extends LinearLayout
        implements OnRefreshListener, OnItemClickListener, IListView {

    private ListView lv;
    private SwipeRefreshLayout swipe;
    private WorkflowDoneAdapter adapter;
    public int page = 1;
    private int total = 0;
    public String keyword = "";
    private List<Workflow> dataInfo = new ArrayList<Workflow>();
    /** http请求处理器，用于取消请求 */
    Cancelable httpHandler;

    /**
     * @param context
     */
    public WorkflowDoneView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workflow, this);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        lv = (ListView) findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        adapter = new WorkflowDoneAdapter(dataInfo, getContext());
        lv.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        if (page > 1 && (page - 1) * ServiceManager.LIMIT >= total) {
            ToastUtil.showShort(getContext(), "没有更多数据了");
            swipe.setRefreshing(false);
            return;
        }
        RequestListener<QueryWorkflowResult> callback = new RequestListener<QueryWorkflowResult>() {

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
            public void onSuccess(int stateCode, QueryWorkflowResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        dataInfo = result.getWofklowList();
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

        httpHandler = ServiceManager.viewWorkflowList(page, keyword, callback);
    }

    private void setData(List<Workflow> data) {
        if (page == 1) {
            adapter.setData(data);
        } else {
            adapter.addData(data);
        }
        adapter.notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jade.customervisit.ui.view.IListView#onRefresh()
     */
    @Override
    public void onRefresh() {
        page = 1;
        keyword = "";
        loadData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // Intent intent = new Intent(getContext(), ServiceMainActivity.class);
        // intent.putExtra("serviceContent", dataInfo.get(position));
        // getContext().startActivity(intent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jade.customervisit.ui.view.swipe.SwipeRefreshLayout.OnRefreshListener
     * #onRefresh(com.jade.customervisit.ui.view.swipe.
     * SwipeRefreshLayoutDirection)
     */
    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if (SwipeRefreshLayoutDirection.TOP == direction) {
            page = 1;
            keyword = "";
            loadData();
        } else if (SwipeRefreshLayoutDirection.BOTTOM == direction) {
            page++;
            loadData();
        }
    }

}
