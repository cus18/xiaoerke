package com.cxqm.xiaoerke.modules.plan.entity;

import java.util.Date;

public class PlanInfoTask {
    private Long id;

    private Long planInfoId;

    private Date timeHappen;

    private String type;

    private Boolean remindMe;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanInfoId() {
        return planInfoId;
    }

    public void setPlanInfoId(Long planInfoId) {
        this.planInfoId = planInfoId;
    }

    public Date getTimeHappen() {
        return timeHappen;
    }

    public void setTimeHappen(Date timeHappen) {
        this.timeHappen = timeHappen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRemindMe() {
        return remindMe;
    }

    public void setRemindMe(Boolean remindMe) {
        this.remindMe = remindMe;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}