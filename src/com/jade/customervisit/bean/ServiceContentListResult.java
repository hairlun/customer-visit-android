/**
 * CustomerVisit
 * ServiceContentListResult
 * Administrator
 * 2016-4-25 下午5:55:31
 */
package com.jade.customervisit.bean;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class ServiceContentListResult extends RequestResult {

    /**
     * 
     */
    private static final long serialVersionUID = -9203756460185639246L;

    private List<ServiceContent> serviceContentList;

    public List<ServiceContent> getServiceContentList() {
        return serviceContentList;
    }

    public void setServiceContentList(List<ServiceContent> serviceContentList) {
        this.serviceContentList = serviceContentList;
    }

    @Override
    public String toString() {
        return "ServiceContentListResult [serviceContentList="
                + serviceContentList + ", isSuccesses()=" + isSuccesses()
                + ", getRetcode()=" + getRetcode() + ", getRetinfo()="
                + getRetinfo() + ", getResponse()=" + getResponse() + "]";
    }
    
}
