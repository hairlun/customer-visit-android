package com.jade.customervisit.util.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.QueryServiceStatusResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bll.ServiceManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

public class QueryServiceStatusParser extends
		AbsBaseParser<QueryServiceStatusResult> {

	public QueryServiceStatusParser(
			IAsyncListener<QueryServiceStatusResult> listener) {
		super(listener);
	}

	@Override
	public QueryServiceStatusResult parse(String response) {
		QueryServiceStatusResult result = null;
		try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new QueryServiceStatusResult(retcode, retinfo);
                int codeSignFlag = json.optInt(ResponseKey.CODE_SIGN_FLAG, 1);
                int codeExitFlag = json.optInt(ResponseKey.CODE_EXIT_FLAG, 1);
                int contentFlag = json.optInt(ResponseKey.CONTENT_FLAG, 1);
                int takePhotoFlag = json.optInt(ResponseKey.TAKE_PHOTO_FLAG, 1);
                int praiseFlag = json.optInt(ResponseKey.PRAISE_FLAG, 1);
                result.setCodeSignFlag(codeSignFlag);
                result.setCodeExitFlag(codeExitFlag);
                result.setContentFlag(contentFlag);
                result.setTakePhotoFlag(takePhotoFlag);
                result.setPraiseFlag(praiseFlag);
                result.setResponse(response);
            }
		} catch (JSONException e) {
            e.printStackTrace();
            onFailure(e);
        }
		return result;
	}

}
