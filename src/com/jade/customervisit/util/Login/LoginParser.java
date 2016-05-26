package com.jade.customervisit.util.Login;

import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.LoginResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bll.LoginManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

/**
 * 登录结果解析器
 */
public class LoginParser extends AbsBaseParser<LoginResult> {

    public LoginParser(IAsyncListener<LoginResult> listener) {
        super(listener);
    }

    @Override
    public LoginResult parse(String response) {
        LoginResult result = null;
        try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new LoginResult(retcode, retinfo);
                String userid = json.optString(ResponseKey.USER_ID);
                result.setUserid(userid);
                if (result.isSuccesses()) {
                    result.setResponse(response);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(e);
        }
        return result;
    }

}
