package com.cxqm.xiaoerke.modules.activity.entity;

import java.util.Date;

public class PunchCardInfoVo {

    private String id;

    private String openId;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    private Integer totalDays;

    private Integer marketer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getMarketer() {
        return marketer;
    }

    public void setMarketer(Integer marketer) {
        this.marketer = marketer;
    }
}