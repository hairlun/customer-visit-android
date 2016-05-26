package com.jade.customervisit.ui;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.ui.view.dialog.LoadingDialog;
import com.jade.customervisit.util.L;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {

    protected String tag;
    protected Context context;

    /** 等待框 */
    public LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CVApplication.cvApplication.addActivity(this);
        context = this;
        tag = getClass().getName();
        L.i(tag + ".onCreate()");

        if (loadingDialog == null) {
            // 创建退出等待框
            loadingDialog = new LoadingDialog(this, getString(R.string.exiting));
        }
    }

    @Override
    protected void onStart() {
        L.i(tag + ".onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        L.i(tag + ".onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        L.i(tag + ".onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        L.i(tag + ".onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        L.i(tag + ".onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        L.i(tag + ".onDestroy()");
        CVApplication.cvApplication.deleteActivity(this);
        super.onDestroy();
    }

}
