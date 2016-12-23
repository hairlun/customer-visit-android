/**
 * CustomerVisit
 * MainActivity
 * zhoushujie
 * 2016年12月7日 上午9:29:36
 */
package com.jade.customervisit.ui;

import com.jade.customervisit.R;
import com.jade.customervisit.ui.service.ServiceListActivity;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.ui.workflow.WorkflowActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author zhoushujie
 *
 */
public class MainActivity extends Activity implements OnClickListener {

    private TitleBarView titlebar;

    private Button customerVisitBtn;

    private Button workflowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);
        initView();
    }

    private void initView() {
        titlebar = (TitleBarView) findViewById(R.id.main_titlebar);
        titlebar.hideSearchBtn().setOnBackClickListener(this).setBackText("退出");
        customerVisitBtn = (Button) findViewById(R.id.customer_visit_btn);
        workflowBtn = (Button) findViewById(R.id.workflow_btn);
        customerVisitBtn.setOnClickListener(this);
        workflowBtn.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_back_btn:
            showDialog();
            break;

        case R.id.customer_visit_btn:
            startActivity(new Intent(this, ServiceListActivity.class));
            break;

        case R.id.workflow_btn:
            // startActivity(new Intent(this, WorkflowActivity.class));
            break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 
     * <退出框> <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    private void showDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("提示").setMessage("是否退出程序？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(1);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

}
