package com.jade.customervisit.util.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.QueryServiceContentResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bean.ServiceContent;
import com.jade.customervisit.bll.ServiceManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

public class QueryServiceContentParser extends
		AbsBaseParser<QueryServiceContentResult> {

	public QueryServiceContentParser(
			IAsyncListener<QueryServiceContentResult> listener) {
		super(listener);
	}

	@Override
	public QueryServiceContentResult parse(String response) {
		QueryServiceContentResult result = null;
		try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new QueryServiceContentResult(retcode, retinfo);
                int total = json.optInt(ResponseKey.TOTAL);
                result.setTotal(total);
                if (!json.isNull(ResponseKey.DATA_INFO)) {
                	JSONArray array = json.optJSONArray(ResponseKey.DATA_INFO);
                	int size = array.length();
                	for (int i = 0; i < size; i++) {
                		JSONObject obj = array.getJSONObject(i);
                		String serviceId = obj.optString(ResponseKey.SERVICE_ID);
                		String title = obj.optString(ResponseKey.TITLE);
                		String name = obj.optString(ResponseKey.NAME);
                		String createTime = obj.optString(ResponseKey.CREATE_TIME);
                		int type = obj.optInt(ResponseKey.TYPE);
                		ServiceContent serviceContent = new ServiceContent();
                		serviceContent.setServiceId(serviceId);
                		serviceContent.setTitle(title);
                		serviceContent.setCustomerName(name);
                		serviceContent.setCreateTime(createTime);
                		serviceContent.setType(type);
                		result.getServiceContentList().add(serviceContent);
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
