/**
 * CustomerVisit
 * WorkflowDealActivity
 * zhoushujie
 * 2016年12月21日 上午11:00:34
 */
package com.jade.customervisit.ui.workflow;

import java.util.HashSet;

import org.xutils.common.Callback.Cancelable;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.Workflow;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.TitleBarView;

import android.os.Bundle;
import android.widget.TextView;

/**
 * @author zhoushujie
 *
 */
public class WorkflowDealActivity extends BaseActivity {

    private TitleBarView titlebar;

    private TextView customerNameTv;

    private TextView addressTv;

    private TextView sellNumberTv;

    private TextView receiveTimeTv;

    private TextView problemFinderTv;

    private Workflow workflow;

    /** http请求处理器，用于取消请求 */
    Cancelable httpHandler;
    private HashSet<String> urlSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_deal);
        workflow = (Workflow) getIntent().getSerializableExtra("workflow");
        initView();
        initData();
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

    }

    private void initData() {

    }

}
