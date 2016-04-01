package com.cxqm.xiaoerke.modules.plan.entity;

import java.util.Date;

public class PlanInfoTaskConfirm {
    private Long id;

    private Long planInfoTaskId;

    private Date taskDate;

    private Date createTime;

    private String createBy;

    private String openId;

    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanInfoTaskId() {
        return planInfoTaskId;
    }

    public void setPlanInfoTaskId(Long planInfoTaskId) {
        this.planInfoTaskId = planInfoTaskId;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}