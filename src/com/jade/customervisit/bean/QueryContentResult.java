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
public class QueryContentResult extends RequestResult {

    /**
	 * 
	 */
	private static final long serialVersionUID = -950000388811198971L;

	private List<ContentItem> contentList = new ArrayList<ContentItem>();
    
    private int total;

    public QueryContentResult() {
		super();
	}

	public QueryContentResult(String retcode, String retinfo) {
		super(retcode, retinfo);
	}

	public List<ContentItem> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentItem> contentList) {
        this.contentList = contentList;
    }

    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
    public String toString() {
        return "ServiceContentListResult [contentList="
                + contentList + ", total=" + total + ", isSuccesses()=" + isSuccesses()
                + ", getRetcode()=" + getRetcode() + ", getRetinfo()="
                + getRetinfo() + ", getResponse()=" + getResponse() + "]";
    }
    
}
