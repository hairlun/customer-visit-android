/**
 * CustomerVisit
 * QueryWorkflowResult
 * zhoushujie
 * 2016年12月13日 上午6:45:49
 */
package com.jade.customervisit.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoushujie
 *
 */
public class QueryWorkflowResult extends RequestResult {

    private List<Workflow> wofklowList = new ArrayList<Workflow>();

    private int total;

    /**
     * @param retcode
     * @param retinfo
     */
    public QueryWorkflowResult(String retcode, String retinfo) {
        super(retcode, retinfo);
    }

    public List<Workflow> getWofklowList() {
        return wofklowList;
    }

    public void setWofklowList(List<Workflow> wofklowList) {
        this.wofklowList = wofklowList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "QueryWorkflowResult [wofklowList=" + wofklowList + ", total="
                + total + ", isSuccesses()=" + isSuccesses() + ", getRetcode()="
                + getRetcode() + ", getRetinfo()=" + getRetinfo()
                + ", getResponse()=" + getResponse() + "]";
    }

}
