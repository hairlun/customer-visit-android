/**
 * CustomerVisit
 * WorkflowActivity
 * zhoushujie
 * 2016年12月7日 下午5:05:43
 */
package com.jade.customervisit.ui.workflow;

import java.util.ArrayList;
import java.util.List;

import org.xutils.common.util.LogUtil;

import com.jade.customervisit.R;
import com.jade.customervisit.adapter.ListPagerAdapter;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.service.ServiceSearchActivity;
import com.jade.customervisit.ui.view.ActionItem;
import com.jade.customervisit.ui.view.IListView;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.util.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author zhoushujie
 *
 */
public class WorkflowActivity extends BaseActivity
        implements OnClickListener, OnPageChangeListener, OnItemClickListener {

    /** 标题栏 */
    TitleBarView mTitleBar;

    /** 界面切换 */
    ViewPager mPager;

    /** 界面切换适配器 */
    ListPagerAdapter mPagerAdapter;

    /** 遮罩层 */
    TextView mShadeTv;

    /** View集合 */
    List<LinearLayout> mViews = new ArrayList<LinearLayout>();

    WorkflowTodoView workflowTodoView = null;

    WorkflowDoneView workflowDoneView = null;

    Context mContext;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.i("action = " + action);
            if (Constants.ACTION_REFRESH_LIST.equals(action)) {
                doRefresh();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        mTitleBar = (TitleBarView) findViewById(R.id.service_list_titlebar);
        mPager = (ViewPager) findViewById(R.id.service_list_pager);
        mShadeTv = (TextView) findViewById(R.id.service_list_shade_tv);
        mContext = this;

        // 注册广播接收器
        registerReceiver();

        initView();
        initListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mTitleBar.setTitle(R.string.workflow_todo)
                .setOnSearchClickListener(this).showMoreBtn()
                .addMenuItem(new ActionItem(mContext, 0, R.string.service_list))
                .addMenuItem(new ActionItem(mContext, 1,
                        R.string.service_record_list))
                .setOnMenuItemClickListener(this).select(0);
        workflowTodoView = new WorkflowTodoView(mContext);
        mViews.add(workflowTodoView);
        workflowDoneView = new WorkflowDoneView(mContext);
        mViews.add(workflowDoneView);
        mPagerAdapter = new ListPagerAdapter(this, mViews);
        mPager.setAdapter(mPagerAdapter);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mPager.setOnPageChangeListener(this);
    }

    private void doRefresh() {
        for (LinearLayout ll : mViews) {
            ((IListView) ll).onRefresh();
        }
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_REFRESH_LIST);
        registerReceiver(receiver, filter);
    }

    /**
     * 注销广播接收器
     */
    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        ActionItem item = (ActionItem) parent.getItemAtPosition(position);
        mTitleBar.select(item, true);
        mPager.setCurrentItem(item.getId());
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        ActionItem item = mTitleBar.getMenuItem(position);
        mTitleBar.select(item == null ? 0 : position);
        switch (position) {
        case 0:
            mTitleBar.setTitle(R.string.service_list);
            break;
        case 1:
            mTitleBar.setTitle(R.string.service_record_list);
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_search_btn:
            if (mPager.getCurrentItem() == 0) {
                // 服务列表
                Intent intent = new Intent(this, ServiceSearchActivity.class);
                intent.putExtra(Constants.KEYWORD, workflowTodoView.keyword);
                startActivityForResult(intent, Constants.SEARCH_SERVICE);
            } else {
                // 服务记录
                Intent intent = new Intent(this, ServiceSearchActivity.class);
                intent.putExtra(Constants.KEYWORD, workflowDoneView.keyword);
                startActivityForResult(intent, Constants.SEARCH_SERVICE);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SEARCH_SERVICE
                && resultCode == RESULT_OK) {
            if (mPager.getCurrentItem() == 0) {
                // 服务列表
                workflowTodoView.keyword = data
                        .getStringExtra(Constants.KEYWORD);
                workflowTodoView.page = 1;
                workflowTodoView.loadData();
            } else {
                // 服务记录
                workflowDoneView.keyword = data
                        .getStringExtra(Constants.KEYWORD);
                workflowDoneView.page = 1;
                workflowDoneView.loadData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

}
