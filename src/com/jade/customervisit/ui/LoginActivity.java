package com.jade.customervisit.ui;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.bean.LoginResult;
import com.jade.customervisit.bll.LoginManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.ui.service.ServiceListActivity;
import com.jade.customervisit.ui.view.dialog.AbsCustomDialog;
import com.jade.customervisit.util.CommonUtils;
import com.jade.customervisit.util.NetUtils;
import com.jade.customervisit.util.ToastUtil;
import com.lidroid.xutils.http.HttpHandler;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * <登陆界面> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-11-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LoginActivity extends BaseActivity implements OnClickListener
{
    /**
     * 用户名
     */
    private EditText login_user_edit;
    
    /**
     * 密码
     */
    private EditText login_password_edit;
    
    /**
     * 登陆按钮
     */
    private Button login;
    
    /**
     * 登录名（邮箱/手机号）
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;

    /** http请求处理器，用于取消请求 */
    HttpHandler<String> httpHandler;

    AbsCustomDialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        init();
    }
    
    /**
     * 
     * <初始化布局组件> <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        login_user_edit = (EditText)findViewById(R.id.login_user_edit);
        login_password_edit = (EditText)findViewById(R.id.login_password_edit);
        login = (Button)findViewById(R.id.login);
        
        /**
         * 添加按钮点击事件
         */
        login.setOnClickListener(this);
    }

    /**
     * 
     * <登录接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void login(final String account, final String pwd)
    {
        // 判断是否有网络
        if (NetUtils.isNetConnected(context)) {
            accountLogin(account, pwd);
        } else {
            loadingDialogDismiss();
            dialog = NetUtils.showDisconnectDialog(context);
        }
    }

    private void accountLogin(final String account, final String pwd) {
        // 创建回调对象
        RequestListener<LoginResult> callback = new RequestListener<LoginResult>() {

            @Override
            public void onStart() {
                loadingDialogShow();
            }

            @Override
            public void onSuccess(int statusCode, LoginResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        CVApplication.cvApplication.setUserid(result.getUserid());
                        CVApplication.cvApplication.setUsername(username);
                        ServiceListActivity.start(context);
                        finish();
                    } else {
                        ToastUtil.showLong(context, result.getRetinfo());
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

        // 开始登录
        httpHandler = LoginManager.accountLogin(account, pwd, callback);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login:
                username = login_user_edit.getText().toString().trim();
                password = login_password_edit.getText().toString().trim();
                if (CommonUtils.isNullOrZeroLenght(username))
                {
                    Toast.makeText(LoginActivity.this, "用户名不能为空，请重新输入!", 1).show();
                    break;
                }
                else if (CommonUtils.isNullOrZeroLenght(password))
                {
                    Toast.makeText(LoginActivity.this, "密码不能为空，请重新输入!", 1).show();
                    break;
                }
                else
                {
                    login(username, password);
                }
                break;
            
            default:
                break;
        }
    }
    
//    public void netBack(Object ob)
//    {
//        dailog.dismissDialog();
//        if (ob instanceof LoginResponse)
//        {
//            LoginResponse loginResponse = (LoginResponse)ob;
//            if (CommonUtils.isNotNullOrZeroLenght(loginResponse.getRetcode()))
//            {
//                if (Constants.SUCESS_CODE.equals(loginResponse.getRetcode()))
//                {
//                    Global.setUserId(loginResponse.getUserId());
//                    SharePref.saveString(SharePref.USERID, loginResponse.getUserId());
//                    SharePref.saveString(SharePref.USER_NAME, username);
//                    Intent intent;
//                    if ("admin".equals(SharePref.getString(SharePref.USER_NAME, "")))
//                    {
//                        intent = new Intent(LoginActivity.this, VisitInfoListActivity.class);
//                    }
//                    else
//                    {
//                        intent = new Intent(LoginActivity.this, ServiceContentListActivity.class);
//                    }
//                    startActivity(intent);
//                    finish();
//                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this, loginResponse.getRetinfo(), 1).show();
//                    
//                }
//            }
//            else
//            {
//                Toast.makeText(LoginActivity.this, "请求失败，请稍后再试", 1).show();
//            }
//        }
//    }

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
            loadingDialog.show("正在核实…");
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
        	finish();
            System.exit(1);
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
