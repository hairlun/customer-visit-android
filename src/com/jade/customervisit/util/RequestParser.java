package com.jade.customervisit.util;

import org.json.JSONObject;

import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.network.IAsyncListener;

/**
 * 请求结果解析器
 * 
 * @author huangzhongwen 2014-9-17 下午1:27:14
 */
public class RequestParser extends AbsBaseParser<RequestResult> {

    /**
     * 请求结果解析器
     */
    public RequestParser() {
        super();
    }

    public RequestParser(IAsyncListener<RequestResult> listener) {
        super(listener);
    }

    @Override
    public RequestResult parse(String response) {
        RequestResult result = null;
        try {
            JSONObject json = new JSONObject(response);
            String retcode = json.optString(RequestResult.RET_CODE_KEY);
            String retinfo = json.optString(RequestResult.RET_INFO_KEY);
            result = new RequestResult(retcode, retinfo);
            result.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                onFailure(e);
            }
        }
        return result;
    }
}
