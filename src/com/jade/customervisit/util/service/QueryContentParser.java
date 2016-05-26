package com.jade.customervisit.util.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.ContentItem;
import com.jade.customervisit.bean.QueryContentResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bll.ServiceManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

public class QueryContentParser extends
		AbsBaseParser<QueryContentResult> {

	public QueryContentParser(
			IAsyncListener<QueryContentResult> listener) {
		super(listener);
	}

	@Override
	public QueryContentResult parse(String response) {
		QueryContentResult result = null;
		try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new QueryContentResult(retcode, retinfo);
                if (!json.isNull(ResponseKey.DATA_INFO)) {
                	JSONArray array = json.optJSONArray(ResponseKey.DATA_INFO);
                	int size = array.length();
                	for (int i = 0; i < size; i++) {
                		JSONObject obj = array.getJSONObject(i);
                		String id = obj.optString(ResponseKey.CONTENT_ID);
                		String name = obj.optString(ResponseKey.CONTENT_NAME);
                		ContentItem contentItem = new ContentItem();
                		contentItem.setId(id);
                		contentItem.setName(name);
                		result.getContentList().add(contentItem);
                	}
                }
                result.setResponse(response);
            }
		} catch (JSONException e) {
            e.printStackTrace();
            onFailure(e);
        }
		return result;
	}

}
