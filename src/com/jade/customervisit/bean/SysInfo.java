package com.jade.customervisit.bean;

import java.io.Serializable;

/**
 * 系统信息
 * 
 * @author zhoushujie 2014-8-11 下午5:17:01
 */
public class SysInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1161282661977628957L;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本代码
     */
    private int versionCode;

    /**
     * APK下载链接
     */
    private String apkLink;

    /**
     * 版本名称
     * 
     * @return
     */
    public String getVersionName() {
        return versionName == null ? "" : versionName;
    }

    /**
     * 版本代码
     * 
     * @return
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     * APK下载链接
     * 
     * @return
     */
    public String getApkLink() {
        return apkLink == null ? "" : apkLink;
    }

    /**
     * 版本名称
     * 
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? "" : versionName;
    }

    /**
     * 版本代码
     * 
     * @param versionCode
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * APK下载链接
     * 
     * @param apkLink
     */
    public void setApkLink(String apkLink) {
        this.apkLink = apkLink == null ? "" : apkLink;
    }

}
