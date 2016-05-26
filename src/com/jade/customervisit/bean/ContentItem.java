/**
 * CustomerVisit
 * ServiceContentItem
 * Administrator
 * 2016-4-25 下午5:22:57
 */
package com.jade.customervisit.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class ContentItem implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 10849534604745132L;

	private String id;
    
    private String name;
    
    /**
     * 是否已经点击
     * 0：未点击。1：已点击
     */
    private String isClick = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getIsClick()
    {
        return isClick;
    }
    
    public void setIsClick(String isClick)
    {
        this.isClick = isClick;
    }

}
