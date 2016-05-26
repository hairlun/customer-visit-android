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
public class QueryServiceContentResult extends RequestResult {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9062033815658620459L;

	private List<ServiceContent> serviceContentList = new ArrayList<ServiceContent>();
    
    private int total;

    public QueryServiceContentResult() {
		super();
	}

	public QueryServiceContentResult(String retcode, String retinfo) {
		super(retcode, retinfo);
	}

	public List<ServiceContent> getServiceContentList() {
        return serviceContentList;
    }

    public void setServiceContentList(List<ServiceContent> serviceContentList) {
        this.serviceContentList = serviceContentList;
    }

    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
    public String toString() {
        return "ServiceContentListResult [serviceContentList="
                + serviceContentList + ", total=" + total + ", isSuccesses()=" + isSuccesses()
                + ", getRetcode()=" + getRetcode() + ", getRetinfo()="
                + getRetinfo() + ", getResponse()=" + getResponse() + "]";
    }
    
}
