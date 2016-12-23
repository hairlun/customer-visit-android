/**
 * CustomerVisit
 * Customer
 * zhoushujie
 * 2016年12月11日 下午11:03:23
 */
package com.jade.customervisit.bean;

import java.io.Serializable;

/**
 * @author zhoushujie
 *
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = -7723830326201256291L;
    private String id;
    private String number;
    private String name;
    private String sellNumber;
    private String storeName;
    private String phoneNumber;
    private String backupNumber;
    private String address;
    private CustomerManager customerManager;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellNumber() {
        return this.sellNumber;
    }

    public void setSellNumber(String sellNumber) {
        this.sellNumber = sellNumber;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBackupNumber() {
        return this.backupNumber;
    }

    public void setBackupNumber(String backupNumber) {
        this.backupNumber = backupNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerManager getCustomerManager() {
        return this.customerManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
