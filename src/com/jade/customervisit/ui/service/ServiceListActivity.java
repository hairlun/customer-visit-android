package com.jade.customervisit.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.xutils.common.util.LogUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.adapter.ServiceListPagerAdapter;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.ActionItem;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.util.Constants;

public class ServiceListActivity extends BaseActivity implements
		OnItemClickListener, OnPageChangeListener, OnClickListener {

	public static final String TAG = ServiceListActivity.class.getName();

	/** 标题栏 */
	TitleBarView mTitleBar;

	/** 界面切换 */
	ViewPager mPager;

	/** 界面切换适配器 */
	ServiceListPagerAdapter mPagerAdapter;

	/** 遮罩层 */
	TextView mShadeTv;

	/** View集合 */
	List<LinearLayout> mViews = new ArrayList<LinearLayout>();
	
	ServiceContentView serviceContentView = null;
	
	VisitInfoView visitInfoView = null;

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
		mTitleBar
				.setTitle(R.string.service_list)
				.setOnSearchClickListener(this)
				.setOnBackClickListener(this)
				.setBackText("退出");
		if (!CVApplication.cvApplication.getUsername().equals("admin")) {
			// 非admin用户才有服务列表界面
			mTitleBar
					.showMoreBtn()
					.addMenuItem(new ActionItem(mContext, 0, R.string.service_list))
					.addMenuItem(
							new ActionItem(mContext, 1,
									R.string.service_record_list))
					.setOnMenuItemClickListener(this).select(0);
			// 服务列表界面
			serviceContentView = new ServiceContentView(this);
			mViews.add(serviceContentView);
		}
		// 服务记录界面
		visitInfoView = new VisitInfoView(this);
		mViews.add(visitInfoView);

		mPagerAdapter = new ServiceListPagerAdapter(this, mViews);
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
			((IServiceView)ll).onRefresh();
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

	public static void start(Context context) {
	    Intent intent = new Intent(context, ServiceListActivity.class);
	    context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	    switch (v.getId()) {
	    case R.id.titlebar_back_btn:
	        showDialog();
	        break;
	        
	    case R.id.titlebar_search_btn:
	        if (mPager.getCurrentItem() == 0) {
	            // 服务列表
	            Intent intent = new Intent(this, ServiceSearchActivity.class);
	            intent.putExtra(Constants.KEYWORD, serviceContentView.keyword);
	            startActivityForResult(intent, Constants.SEARCH_SERVICE);
	        } else {
	            // 服务记录
	            Intent intent = new Intent(this, ServiceSearchActivity.class);
	            intent.putExtra(Constants.KEYWORD, visitInfoView.keyword);
	            startActivityForResult(intent, Constants.SEARCH_SERVICE);
	        }
	        break;
	    }
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.SEARCH_SERVICE && resultCode == RESULT_OK) {
			if (mPager.getCurrentItem() == 0) {
				// 服务列表
				serviceContentView.keyword = data.getStringExtra(Constants.KEYWORD);
				serviceContentView.page = 1;
				serviceContentView.queryServiceContentList();
			} else {
				// 服务记录
			    visitInfoView.keyword = data.getStringExtra(Constants.KEYWORD);
			    visitInfoView.page = 1;
			    visitInfoView.getVisitInfoList();
			}
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    /**
     * 
     * <退出框>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showDialog()
    {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("提示").setMessage("是否退出程序？").setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
                System.exit(1);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                
            }
        }).create().show();
    }
}
