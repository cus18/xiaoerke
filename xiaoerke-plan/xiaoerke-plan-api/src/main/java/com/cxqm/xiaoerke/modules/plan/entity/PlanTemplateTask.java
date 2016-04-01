package com.cxqm.xiaoerke.modules.plan.entity;

import java.util.Date;

public class PlanTemplateTask {
    private Integer id;

    private Short planTemplateId;

    private Date timeHappen;

    private String type;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getPlanTemplateId() {
        return planTemplateId;
    }

    public void setPlanTemplateId(Short planTemplateId) {
        this.planTemplateId = planTemplateId;
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