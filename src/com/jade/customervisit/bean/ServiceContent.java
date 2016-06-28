/**
 * CustomerVisit
 * ServiceContent
 * Administrator
 * 2016-4-25 下午4:29:24
 */
package com.jade.customervisit.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class ServiceContent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6453699292599890716L;

    private String serviceId;

    private String title;

    private String createTime;

    private String customerName;

    private int type;

    private int codeSignFlag;

    private int contentFlag;

    private int praiseFlag;

    private List<ContentItem> contents;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCodeSignFlag() {
        return codeSignFlag;
    }

    public void setCodeSignFlag(int codeSignFlag) {
        this.codeSignFlag = codeSignFlag;
    }

    public int getContentFlag() {
        return contentFlag;
    }

    public void setContentFlag(int contentFlag) {
        this.contentFlag = contentFlag;
    }

    public int getPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(int praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    public List<ContentItem> getContents() {
        return contents;
    }

    public void setContents(List<ContentItem> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ServiceContent [serviceId=" + serviceId + ", title=" + title
                + ", createTime=" + createTime + ", customerName="
                + customerName + ", type=" + type + ", codeSignFlag="
                + codeSignFlag + ", contentFlag=" + contentFlag
                + ", praiseFlag=" + praiseFlag + ", contents=" + contents + "]";
    }
}
