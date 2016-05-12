package com.cxqm.xiaoerke.modules.marketing.entity;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-5-11.
 */
public class LoveActivityComment {
    private String id;
    private String userId;
    private String content;
    private Date createDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
