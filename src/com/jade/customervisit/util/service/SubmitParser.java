package com.jade.customervisit.util.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.LoginResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bean.SubmitResult;
import com.jade.customervisit.bll.LoginManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

/**
 * 登录结果解析器
 */
public class SubmitParser extends AbsBaseParser<SubmitResult> {

    public SubmitParser(IAsyncListener<SubmitResult> listener) {
        super(listener);
    }

    @Override
    public SubmitResult parse(String response) {
    	SubmitResult result = null;
        try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new SubmitResult(retcode, retinfo);
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
