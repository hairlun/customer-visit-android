/**
 * CustomerVisit
 * Workflow
 * zhoushujie
 * 2016年12月8日 下午10:09:49
 */
package com.jade.customervisit.bean;

import java.io.Serializable;

/**
 * @author zhoushujie
 *
 */
public class Workflow implements Serializable {

    private static final long serialVersionUID = -2703515366288072573L;

    private String id;
    
    private Customer customer;

    private String address;

    private String receiveTime;

    private CustomerManager problemFinder;

    private String handleTime;
    
    private CustomerManager currentHandler;

    private CustomerManager handler;

    private String solvedTime;

    private String description;

    private String remark;
    
    private String brief;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public CustomerManager getProblemFinder() {
        return problemFinder;
    }

    public void setProblemFinder(CustomerManager problemFinder) {
        this.problemFinder = problemFinder;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public CustomerManager getCurrentHandler() {
        return currentHandler;
    }

    public void setCurrentHandler(CustomerManager currentHandler) {
        this.currentHandler = currentHandler;
    }

    public CustomerManager getHandler() {
        return handler;
    }

    public void setHandler(CustomerManager handler) {
        this.handler = handler;
    }

    public String getSolvedTime() {
        return solvedTime;
    }

    public void setSolvedTime(String solvedTime) {
        this.solvedTime = solvedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
