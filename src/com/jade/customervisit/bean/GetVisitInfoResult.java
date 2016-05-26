/**
 * CustomerVisit
 * ServiceContentListResult
 * Administrator
 * 2016-4-25 下午5:55:31
 */
package com.jade.customervisit.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class GetVisitInfoResult extends RequestResult {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6795077017134201811L;

	private List<VisitInfo> visitInfoList = new ArrayList<VisitInfo>();
    
    private int total;

    /**
     * @param retcode
     * @param retinfo
     */
    public GetVisitInfoResult(String retcode, String retinfo) {
        super(retcode, retinfo);
    }

    public List<VisitInfo> getVisitInfoList() {
        return visitInfoList;
    }

    public void setVisitInfoList(List<VisitInfo> visitInfoList) {
        this.visitInfoList = visitInfoList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
