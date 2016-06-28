package com.jade.customervisit.bean;

/**
 * 登录结果包装
 */
public class SubmitResult extends RequestResult {

    /**
	 * 
	 */
    private static final long serialVersionUID = 682702008721444566L;

    /**
     * 无参构造
     */
    public SubmitResult() {

    }

    /**
     * 全参构造
     * 
     * @param resultCode
     * @param resultMsg
     */
    public SubmitResult(String retcode, String retinfo) {
        super(retcode, retinfo);
    }

    @Override
    public String toString() {
        return "SubmitContentResult [isSuccesses()=" + isSuccesses()
                + ", getRetcode()=" + getRetcode() + ", getRetinfo()="
                + getRetinfo() + ", getResponse()=" + getResponse() + "]";
    }

}
