/**
 * CustomerVisit
 * LoginManager
 * Administrator
 * 2016-4-25 上午11:30:41
 */
package com.jade.customervisit.bll;

import com.jade.customervisit.bean.LoginResult;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.network.WebService;
import com.jade.customervisit.util.CommonUtils;
import com.jade.customervisit.util.Login.LoginParser;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

/**
 * @author Administrator
 *
 */
public class LoginManager {

    /**  */
    public static final String TAG = LoginManager.class.getName();

    /**
     * 登录参数Key
     * 
     * @author huangzhongwen
     * 
     */
    public interface RequestKey {

        /** 用户密码KEY */
        String PWD = "password";

        /** 用户账号KEY */
        String ACCOUNT = "username";
    }

    /**
     * 登录响应Key
     * 
     * @author huangzhongwen
     * 
     */
    public interface ResponseKey {

        /** 用户idKEY */
        String USER_ID = "userId";
    }

    /**
     * 登录地址
     */
    public interface Url {
        /** 账号登录地址 */
        String ACCOUNT_LOGIN = "/login.do?";
    }

    /**
     * 用户账号登录
     * 
     * @param account
     * @param pwd
     * @param listener
     * @return
     */
    public static HttpHandler<String> accountLogin(String account, String pwd,
            final RequestListener<LoginResult> listener) {
        String[] keys = { RequestKey.ACCOUNT, RequestKey.PWD };
        String[] values = { account, pwd };
        RequestParams params = CommonUtils.createParams(keys, values);
        return WebService.post(Url.ACCOUNT_LOGIN, params, listener,
                new LoginParser(listener));
    }

}
