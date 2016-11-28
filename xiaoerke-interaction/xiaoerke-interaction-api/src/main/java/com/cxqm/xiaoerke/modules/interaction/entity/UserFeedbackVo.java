package com.cxqm.xiaoerke.modules.interaction.entity;

import java.util.Date;

/**
 * 用户反馈
 *
 * @author sunxiao
 * @version 2016-11-15
 */
public class UserFeedbackVo {

    private Integer id;//主键

    private String openid;

    private String content;

    private Date createTime;

    private String type;

    private String solve;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSolve() {
        return solve;
    }

    public void setSolve(String solve) {
        this.solve = solve;
    }
}