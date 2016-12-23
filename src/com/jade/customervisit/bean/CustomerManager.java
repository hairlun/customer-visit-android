/**
 * CustomerVisit
 * CustomerManager
 * zhoushujie
 * 2016年12月11日 下午12:34:49
 */
package com.jade.customervisit.bean;

import java.io.Serializable;

/**
 * @author zhoushujie
 *
 */
public class CustomerManager implements Serializable {

    private static final long serialVersionUID = 5343900258576417518L;
    private String id;
    private String username;
    private String name;
    private String department;
    private String area;

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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
