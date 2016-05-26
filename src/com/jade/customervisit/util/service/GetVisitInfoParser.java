package com.jade.customervisit.util.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.GetVisitInfoResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bean.VisitInfo;
import com.jade.customervisit.bll.ServiceManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

public class GetVisitInfoParser extends
		AbsBaseParser<GetVisitInfoResult> {

	public GetVisitInfoParser(
			IAsyncListener<GetVisitInfoResult> listener) {
		super(listener);
	}

	@Override
	public GetVisitInfoResult parse(String response) {
	    GetVisitInfoResult result = null;
		try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new GetVisitInfoResult(retcode, retinfo);
                int total = json.optInt(ResponseKey.TOTAL);
                result.setTotal(total);
                if (!json.isNull(ResponseKey.DATA_INFO)) {
                	JSONArray array = json.optJSONArray(ResponseKey.DATA_INFO);
                	int size = array.length();
                	for (int i = 0; i < size; i++) {
                		JSONObject obj = array.getJSONObject(i);
                		String userId = obj.optString(ResponseKey.CUSTOMER_ID);
                		String username = obj.optString(ResponseKey.CUSTOMER_NAME);
                		String arriveTime = obj.optString(ResponseKey.ARRIVE_TIME);
                		String leaveTime = obj.optString(ResponseKey.LEAVE_TIME);
                		String city = obj.optString(ResponseKey.CITY);
                		String praise = obj.optString(ResponseKey.PRAISE);
                		int type = obj.optInt(ResponseKey.TYPE);
                		List<String> imageList = new ArrayList<String>();
                		if (!obj.isNull(ResponseKey.IMAGE_LIST)) {
                		    JSONArray arr = obj.optJSONArray(ResponseKey.IMAGE_LIST);
                		    int ssize = arr.length();
                		    for (int j = 0; j < ssize; j++) {
                		        JSONObject imgObj = arr.getJSONObject(j);
                		        String imageUrl = imgObj.optString(ResponseKey.IMAGE_URL);
                		        imageList.add(imageUrl);
                		    }
                		}
                		VisitInfo visitInfo = new VisitInfo();
                		visitInfo.setUserId(userId);
                		visitInfo.setUsername(username);
                		visitInfo.setArriveTime(arriveTime);
                		visitInfo.setLeaveTime(leaveTime);
                		visitInfo.setCity(city);
                		visitInfo.setPraise(praise);
                		visitInfo.setType(type);
                		visitInfo.setImageList(imageList);
                		result.getVisitInfoList().add(visitInfo);
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
