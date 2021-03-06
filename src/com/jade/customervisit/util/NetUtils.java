package com.jade.customervisit.util;

import com.jade.customervisit.ui.view.dialog.MsgDialog;
import com.jade.customervisit.ui.view.dialog.MsgDialog.BtnType;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 网络工具类
 * 
 * @author zhoushujie
 * 
 */
public class NetUtils {

    /**
     * 网络类型
     * 
     * @author zhoushujie
     * 
     */
    public enum NetType {
        /**
         * 无网络
         */
        NONE,

        /**
         * WIFI网络
         */
        WIFI,

        /**
         * 其他网络
         */
        OTHER;
    }

    private NetUtils() {
    }

    /**
     * WIFI是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 网络是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        if (context != null) {
            return getConnectedType(context) != NetType.NONE;
        }
        return false;
    }

    /**
     * 获取网络类型
     * 
     * @param context
     * @return
     */
    public static NetType getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                switch (mNetworkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                default:
                    return NetType.OTHER;
                }
            }
        }
        return NetType.NONE;
    }

    /**
     * 打开网络设置
     * 
     * @param context
     */
    public static void openNetSetting(Context context) {
        Intent intent = null;
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(
                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    /**
     * 显示无网络提示对话框
     * 
     * @param context
     */
    public static MsgDialog showDisconnectDialog(final Context context) {
        return MsgDialog.show(context, "提示", "当前网络不可用，请检查网络设置！", "打开设置",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        openNetSetting(context);
                    }
                }, BtnType.TWO);
    }

    /**
     * 显示重试对话框
     * 
     * @param context
     * @param listener
     */
    public static MsgDialog showRetrytDialog(final Context context,
            OnClickListener listener) {
        return MsgDialog.show(context, "提示", "服务器繁忙，请稍候再试！", "重试", listener,
                BtnType.TWO);
    }

    /**
     * 显示重试对话框
     * 
     * @param context
     * @param msg
     * @param listener
     */
    public static MsgDialog showRetrytDialog(final Context context, String msg,
            OnClickListener listener) {
        return MsgDialog.show(context, "提示", msg, "重试", listener, BtnType.TWO);
    }
}
