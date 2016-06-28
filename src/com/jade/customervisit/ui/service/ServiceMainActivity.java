package com.jade.customervisit.ui.service;

import java.util.HashSet;

import org.xutils.common.Callback.Cancelable;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.QueryContentResult;
import com.jade.customervisit.bean.QueryServiceStatusResult;
import com.jade.customervisit.bean.ServiceContent;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.scancode.MipcaActivityCapture;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.ui.view.dialog.LoadingDialog;
import com.jade.customervisit.util.Constants;
import com.jade.customervisit.util.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ServiceMainActivity extends BaseActivity implements
        OnClickListener {

    private TitleBarView titlebar;

    private TextView customerNameTv;
    /**
     * 二维码签到
     */
    private Button two_dimensional_code_sign;

    /**
     * 二维码签退
     */
    private Button two_dimensional_code_exit;

    /**
     * 服务内容表
     */
    private Button service_contents;

    /**
     * 服务结果痕迹
     */
    private Button trace_service;

    /**
     * 服务评价
     */
    private Button service_evaluation;

    /**
     * 服务内容id
     */
    private ServiceContent serviceContent;

    /**
     * 网络请求框
     */
    private LoadingDialog loadingDialog;

    /** http请求处理器，用于取消请求 */
    Cancelable httpHandler;
    private HashSet<String> urlSet = new HashSet<String>();

    private int codeSignFlag;
    private int codeExitFlag;
    private int contentFlag;
    private int takePhotoFlag;
    private int praiseFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);
        serviceContent = (ServiceContent) getIntent().getSerializableExtra(
                "serviceContent");

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryServiceStatus();
    }

    @Override
    protected void onDestroy() {
        if (httpHandler != null) {
            httpHandler.cancel();
        }
        super.onDestroy();
    }

    private void initView() {
        titlebar = (TitleBarView) findViewById(R.id.service_main_titlebar);
        titlebar.hideSearchBtn();
        customerNameTv = (TextView) findViewById(R.id.customer_name);
        two_dimensional_code_sign = (Button) findViewById(R.id.two_dimensional_code_sign);
        two_dimensional_code_exit = (Button) findViewById(R.id.two_dimensional_code_exit);
        service_contents = (Button) findViewById(R.id.service_contents);
        trace_service = (Button) findViewById(R.id.trace_service);
        service_evaluation = (Button) findViewById(R.id.service_evaluation);
        loadingDialog = new LoadingDialog(context);

        /**
         * 添加按钮点击事件
         */
        two_dimensional_code_sign.setOnClickListener(this);
        two_dimensional_code_exit.setOnClickListener(this);
        service_contents.setOnClickListener(this);
        trace_service.setOnClickListener(this);
        service_evaluation.setOnClickListener(this);
    }

    private void initData() {
        customerNameTv.setText("客户名称：" + serviceContent.getCustomerName());
        codeSignFlag = 3;
        contentFlag = 0;
        praiseFlag = 0;

        // 获取服务内容项
        queryContent();
    }

    private void queryContent() {
        RequestListener<QueryContentResult> callback = new RequestListener<QueryContentResult>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, QueryContentResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        serviceContent.setContents(result.getContentList());
                    } else {
                        ToastUtil.showShort(context, result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(context, R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
            }

            @Override
            public void onStopped() {
            }
        };
        if (urlSet.contains(ServiceManager.Url.QUERY_CONTENT)) {
            return;
        }
        urlSet.add(ServiceManager.Url.QUERY_CONTENT);
        // 开始请求列表数据
        httpHandler = ServiceManager.queryContent(
                serviceContent.getServiceId(), callback);
    }

    private void queryServiceStatus() {
        RequestListener<QueryServiceStatusResult> callback = new RequestListener<QueryServiceStatusResult>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode,
                    QueryServiceStatusResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        codeSignFlag = result.getCodeSignFlag();
                        codeExitFlag = result.getCodeExitFlag();
                        contentFlag = result.getContentFlag();
                        takePhotoFlag = result.getTakePhotoFlag();
                        praiseFlag = result.getPraiseFlag();
                        refresh();
                    } else {
                        ToastUtil.showShort(context, result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(context, R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
            }

            @Override
            public void onStopped() {
                urlSet.remove(ServiceManager.Url.QUERY_SERVICE_STATUS);
            }
        };

        if (urlSet.contains(ServiceManager.Url.QUERY_SERVICE_STATUS)) {
            return;
        }
        urlSet.add(ServiceManager.Url.QUERY_SERVICE_STATUS);
        // 开始请求服务状态信息
        httpHandler = ServiceManager.queryServiceStatus(
                serviceContent.getServiceId(), callback);
    }

    private void refresh() {
        two_dimensional_code_sign.setEnabled(codeSignFlag == 0 ? false : true);
        two_dimensional_code_exit.setEnabled(codeExitFlag == 0 ? false : true);
        service_contents.setEnabled(contentFlag == 0 ? false : true);
        trace_service.setEnabled(takePhotoFlag == 0 ? false : true);
        service_evaluation.setEnabled(praiseFlag == 0 ? false : true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /**
         * 响应二维码签到按钮
         */
        case R.id.two_dimensional_code_sign:
            Intent codeIntent = new Intent(this, MipcaActivityCapture.class);
            codeIntent.putExtra("type", "0");
            codeIntent.putExtra("flag", "0");
            codeIntent.putExtra("serviceId", serviceContent.getServiceId());
            startActivity(codeIntent);
            break;
        /**
         * 响应二维码签退按钮
         */
        case R.id.two_dimensional_code_exit:
            Intent exitIntent = new Intent(this, MipcaActivityCapture.class);
            exitIntent.putExtra("type", "0");
            exitIntent.putExtra("flag", "1");
            exitIntent.putExtra("serviceId", serviceContent.getServiceId());
            startActivity(exitIntent);
            break;
        /**
         * 响应服务内容表按钮
         */
        case R.id.service_contents:
            Intent contentsIntent = new Intent(this,
                    ServiceContentActivity.class);
            contentsIntent.putExtra("serviceContent", serviceContent);
            startActivity(contentsIntent);
            break;
        /**
         * 响应服务结果痕迹按钮
         */
        case R.id.trace_service:
            Intent traceIntent = new Intent(this, TraceServiceActivity.class);
            traceIntent.putExtra("serviceId", serviceContent.getServiceId());
            startActivity(traceIntent);
            break;
        /**
         * 响应服务评价按钮
         */
        case R.id.service_evaluation:
            Intent evaluationIntent = new Intent(this,
                    MipcaActivityCapture.class);
            evaluationIntent.putExtra("type", Constants.PRAISE);
            evaluationIntent.putExtra("serviceId",
                    serviceContent.getServiceId());
            startActivity(evaluationIntent);
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
