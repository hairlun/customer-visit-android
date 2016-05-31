package com.jade.customervisit.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.xutils.common.Callback.Cancelable;

import com.jade.customervisit.R;
import com.jade.customervisit.adapter.ServiceContentSubmitAdapter;
import com.jade.customervisit.bean.ContentItem;
import com.jade.customervisit.bean.QueryContentResult;
import com.jade.customervisit.bean.ServiceContent;
import com.jade.customervisit.bean.SubmitResult;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.ui.view.dialog.LoadingDialog;
import com.jade.customervisit.util.ToastUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * <服务内容界面 >
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ServiceContentActivity extends BaseActivity implements OnClickListener
{

	private TitleBarView titlebar;

	/**
	 * 网络请求框
	 */
	private LoadingDialog loadingDialog;
    
    /**
     * listview
     */
    private ListView listView;
    
    /**
     * 动态适配器
     */
    private ServiceContentSubmitAdapter adapter;
    
    /**
     * 评论列表
     */
    private List<ContentItem> contentItems = new ArrayList<ContentItem>();
    
    /**
     * 上传服务器
     */
    private Button upload;
    
    private ServiceContent serviceContent;

    /** 列表http请求处理器，用于取消请求 */
    Cancelable contentHttpHandler;
    
    Cancelable submitHttpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        serviceContent = (ServiceContent)getIntent().getSerializableExtra("serviceContent");
        
        initView();
        initData();
    }

	@Override
	protected void onDestroy() {
		if (contentHttpHandler != null) {
			contentHttpHandler.cancel();
		}
		if (submitHttpHandler != null) {
			submitHttpHandler.cancel();
		}
		super.onDestroy();
	}

    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initView()
    {
        titlebar = (TitleBarView)findViewById(R.id.content_titlebar);
        titlebar.hideSearchBtn();
        listView = (ListView)findViewById(R.id.content_listview);
        upload = (Button)findViewById(R.id.upload);
        loadingDialog = new LoadingDialog(context);
        
        /**
         * 添加按钮点击事件
         */
        upload.setOnClickListener(this);
        
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                if ("0".equals(contentItems.get(position).getIsClick()))
                {
                    contentItems.get(position).setIsClick("1");
                }
                else
                {
                    contentItems.get(position).setIsClick("0");
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
    	titlebar.setTitle(serviceContent.getTitle());
        serviceContent = (ServiceContent) getIntent().getSerializableExtra("serviceContent");
        adapter = new ServiceContentSubmitAdapter(contentItems, ServiceContentActivity.this);
        listView.setAdapter(adapter);
        contentItems = serviceContent.getContents();
        if (contentItems != null && contentItems.size() > 0) {
        	setData(contentItems);
        } else {
        	queryContent();
        }
    }

	private void queryContent() {
		RequestListener<QueryContentResult> callback = new RequestListener<QueryContentResult>() {

            @Override
            public void onStart() {
                loadingDialogShow();
            }

            @Override
            public void onSuccess(int statusCode, QueryContentResult result) {
            	if (result != null) {
                    if (result.isSuccesses()) {
                        contentItems = result.getContentList();
                        setData(contentItems);
                    } else if (result.getRetcode().equals("000004")) {
                        ToastUtil.showShort(context, "没有数据");
                    } else {
                        ToastUtil.showShort(context, result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(context, R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
                ToastUtil.showShort(context, R.string.connect_exception);
            }

            @Override
            public void onStopped() {
                loadingDialogDismiss();
            }
        };
        // 开始请求列表数据
        contentHttpHandler = ServiceManager.queryContent(serviceContent.getServiceId(), callback);
	}
	
	private void setData(List<ContentItem> data) {
	    adapter.setData(data);
	    adapter.notifyDataSetChanged();
	}

    /**
     * 
     * <服务内容提交>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void submitContent(String ids)
    {
    	RequestListener<SubmitResult> callback = new RequestListener<SubmitResult>() {

            @Override
            public void onStart() {
                loadingDialogShow();
            }

            @Override
            public void onSuccess(int statusCode, SubmitResult result) {
            	if (result != null) {
                    if (result.isSuccesses()) {
                    	ToastUtil.showShort(context, "提交成功");
                    } else {
                        ToastUtil.showShort(context, result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(context, R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
                ToastUtil.showShort(context, R.string.connect_exception);
            }

            @Override
            public void onStopped() {
                loadingDialogDismiss();
            }
        };
        // 开始请求列表数据
        submitHttpHandler = ServiceManager.submitContent(ids, serviceContent.getServiceId(), callback);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            /**
             * 上传
             */
            case R.id.upload:
                String ids = "";
                for (int i = 0; i < contentItems.size(); i++)
                {
                    if ("1".equals(contentItems.get(i).getIsClick()))
                    {
                        ids = ids.concat(contentItems.get(i).getId());
                        ids += ",";
                    }
                }
                submitContent(ids.replaceFirst(",$", ""));
                break;
            default:
                break;
        }
    }

    /**
     * 释放登录等待框
     */
    private void loadingDialogDismiss() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 显示登录等待框
     */
    private void loadingDialogShow() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show("正在加载…");
        }
    }

}
