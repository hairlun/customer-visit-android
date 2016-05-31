package com.jade.customervisit.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.GetSysInfoResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bll.UpgradeManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;

/**
 * 获取系统信息结果解析器
 * 
 * @author huangzhongwen 2014-8-11 下午5:24:21
 */
public class GetSysInfoParser extends AbsBaseParser<GetSysInfoResult> {

    /**
     * 获取系统信息结果解析器
     * 
     * @param listener
     */
    public GetSysInfoParser(IAsyncListener<GetSysInfoResult> listener) {
        this.listener = listener;
    }

    /**
     * 获取系统信息结果解析器
     */
    public GetSysInfoParser() {
        super();
    }

    @Override
    public GetSysInfoResult parse(String response) {
        GetSysInfoResult result = null;
        try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new GetSysInfoResult(retcode, retinfo);

                if (result.isSuccesses()) {
                    String versionName = json.optString(ResponseKey.VERSION_NAME);
                    int versionCode = json.optInt(ResponseKey.VERSION_CODE);
                    String apkLink = json.optString(ResponseKey.APK_LINK);
                    result.getSysInfo().setVersionName(versionName);
                    result.getSysInfo().setVersionCode(versionCode);
                    result.getSysInfo().setApkLink(apkLink);
                }
                result.setResponse(response);
            }
        } catch (final JSONException e) {
            e.printStackTrace();
            if (listener != null) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        listener.onFailure(e, "解析异常");
                    }
                });
            }
        }
        return result;
    }

}
