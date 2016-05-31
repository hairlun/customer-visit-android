package com.jade.customervisit.bean;

/**
 * 获取系统信息
 * 
 */
public class GetSysInfoResult extends RequestResult {

    /**
     *
     */
    private static final long serialVersionUID = 4959705432294296043L;

    /**
     * 系统信息
     */
    private SysInfo sysInfo = new SysInfo();

    /**
     * 获取系统信息
     */
    public GetSysInfoResult() {
        super();
    }

    /**
     * 获取系统信息
     * 
     * @param resultCode
     *            应用结果代码
     * @param resultMsg
     *            应用结果描述
     */
    public GetSysInfoResult(String retcode, String retinfo) {
        super(retcode, retinfo);
    }

    /**
     * 系统信息
     * 
     * @return
     */
    public SysInfo getSysInfo() {
        return sysInfo;
    }

}
