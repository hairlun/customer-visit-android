package com.jade.customervisit.bll;

import java.io.File;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;

import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.util.LogUtil;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.GetSysInfoResult;
import com.jade.customervisit.bean.SysInfo;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.network.WebService;
import com.jade.customervisit.ui.view.dialog.LoadingDialog;
import com.jade.customervisit.ui.view.dialog.MsgDialog;
import com.jade.customervisit.util.CommonUtils;
import com.jade.customervisit.util.GetSysInfoParser;
import com.jade.customervisit.util.ToastUtil;

/**
 * 应用升级管理器
 * 
 * @author huangzhongwen
 * 
 */
public class UpgradeManager {

    public static final String TAG = UpgradeManager.class.getName();

    public static final String NEW_VERSION_CODE_KEY = "NEW_VERSION_CODE_KEY";

    private static final String DOWNLOAD_ID = "MobileOA_Apk_Download_Id";

    // private static final String DOWNLOAD_DIR = Environment
    // .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    // .getAbsolutePath();

    public static String APK_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/HLMobileOA/file/apk/%s.apk";

    /** 版本名称 */
    public static String versionName = "";

    /** 版本代码 */
    public static int versionCode = 0;

    private static DownloadManager downloadManager;

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 取出下载的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (id == PrefUtil.getLong(context, DOWNLOAD_ID)) {
                queryDownloadStatus(context);
            }
        }
    };

    /**
     * 升级相关的URL
     * 
     * @author huangzhongwen
     * 
     */
    public interface Url {
        
        /** 更新地址 */
        String GET_VERSION_INFO = "/upgrade.do?platform=android";
    	
    }

    /**
     * 响应参数Key
     */
    public interface ResponseKey {
        String VERSION_CODE = "androidversioncode";
        String VERSION_NAME = "androidversionname";
        String APK_LINK = "androidlink";
    }

    /**
     * 获取应用本地版本代码
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            LogUtil.e(e.getMessage());
        }
        return versionCode;
    }

    /**
     * 获取应用本地版本名称
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtil.e(e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取版本信息
     * 
     * @param asyncHandler
     * @param context
     */
    public static Cancelable getVerionInfoFromServer(
            RequestListener<GetSysInfoResult> listener) {
        String[] keys = {};
        String[] values = {};
        return WebService.post(Url.GET_VERSION_INFO,
                keys, values, listener,
                new GetSysInfoParser(listener));
    }

    /**
     * 显示更新对话框
     * 
     * @param context
     * @param sysInfo
     */
    public static void showUpgradeDialog(final Context context,
            final SysInfo sysInfo) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("更新").setMessage("应用有更新，是否下载？").setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                downloadApk(context, sysInfo);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                
            }
        }).create().show();
        
    }

    /**
     * 下载APK
     * 
     * @param context
     * @param sysInfo
     */
    public static void downloadApk(final Context context, final SysInfo sysInfo) {
        if (!SDCardUtils.isSDCardEnable()) {
            ToastUtil.showShort(context, "未找到可用的存储设备！");
            return;
        }
        final LoadingDialog dialog = new LoadingDialog(context);
        final File target = new File(String.format(APK_DIR,
                sysInfo.getVersionName()));
        RequestParams params = CommonUtils.createParams(null, null, sysInfo.getApkLink());
        WebService.download(params,
                target.getAbsolutePath(), false, new RequestListener<File>() {
                    @Override
                    public void onStart() {
                        dialog.show("正在下载");
                    }

                    @Override
                    public void onSuccess(File result) {
                        if (result != null && result.exists()) {
                            installApk(context, result);
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String content) {
                        HttpException e = (HttpException) error;
                        if (e.getCode() == 416) {
                            MsgDialog.show(context, "确认", "安装包已存在，请选择您的操作。",
                                    "重新下载", new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            target.delete();
                                            downloadApk(context, sysInfo);
                                        }
                                    }, "直接安装", new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            installApk(context, target);
                                        }
                                    });
                        } else {
                            ToastUtil.showShort(context,
                                    R.string.connect_exception);
                        }
                    }

                    @Override
                    public void onLoading(Object... values) {
                        long t = (Long) values[0];
                        long c = (Long) values[1];
                        dialog.setMessage(String.format("正在下载%s/%s",
                                Formatter.formatFileSize(context, c),
                                Formatter.formatFileSize(context, t)));
                    }

                    @Override
                    public void onStopped() {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 检查下载状态
     * 
     * @param context
     * @param downloadManager
     */
    private static void queryDownloadStatus(Context context) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(PrefUtil.getLong(context, DOWNLOAD_ID));
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c
                    .getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
            case DownloadManager.STATUS_PAUSED:
                LogUtil.v("STATUS_PAUSED");
            case DownloadManager.STATUS_PENDING:
                LogUtil.v("STATUS_PENDING");
            case DownloadManager.STATUS_RUNNING:
                // 正在下载，不做任何事情
                LogUtil.v("STATUS_RUNNING");
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                // 完成
                LogUtil.v("下载完成");
                // 打开APK进行安装
                String uriString = Environment
                        .getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath()
                        + "/"
                        + c.getString(c
                                .getColumnIndex(DownloadManager.COLUMN_TITLE));
                LogUtil.i(uriString);
                installApk(context, uriString);
                PrefUtil.remove(context, DOWNLOAD_ID);
                break;
            case DownloadManager.STATUS_FAILED:
                // 清除已下载的内容，重新下载
                LogUtil.v("STATUS_FAILED");
                downloadManager.remove(PrefUtil.getLong(context, DOWNLOAD_ID));
                PrefUtil.remove(context, DOWNLOAD_ID);
                break;
            }
        }
        context.unregisterReceiver(receiver);
    }

    /**
     * 安装APK文件
     * 
     * a. 设置Action : Intent.ACTION_VIEW. b. 设置数据和类型 : 设置apk文件的uri 和 MIME类型 c.
     * 开启安装文件的Activity.
     * 
     * @param context
     *            上下文对象
     * @param apkFile
     *            APK文件
     */
    private static void installApk(Context context, String uriString) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(uriString)),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PrefUtil.remove(context, DOWNLOAD_ID);
        }
    }

    /**
     * 
     * @param context
     * @param file
     */
    private static void installApk(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(context, "更新失败！");
            file.delete();
        }
    }

    /**
     * 检查更新
     * 
     * @param context
     */
    public static Cancelable checkUpdate(final Context context,
            final RequestListener<GetSysInfoResult> listener) {
        // 当前版本代码
        versionCode = getVersionCode(context);
        versionName = getVersionName(context);
        return getVerionInfoFromServer(new RequestListener<GetSysInfoResult>() {

            @Override
            public void onSuccess(int stateCode, GetSysInfoResult result) {
                listener.onSuccess(result);
                if (result != null) {
                    if (result.isSuccesses()) {
                        SysInfo sysInfo = result.getSysInfo();
                        int serverVerCode = sysInfo.getVersionCode();
                        if (serverVerCode > versionCode) {
                            // 判断服务器上的版本代码与当前版本代码是否一致
                            int verCode = PrefUtil.getInt(context,
                                    NEW_VERSION_CODE_KEY);
                            if (verCode < serverVerCode) {
                                // 如果没有提醒过，则提醒更新
                                showUpgradeDialog(context, sysInfo);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception error, String content) {
                listener.onFailure(error, content);
            }

        });
    }
}
