package com.cxqm.xiaoerke.modules.member.entity;

import java.util.Date;

public class MemberItemserviceRelationVo {
    private Integer id;

    private Integer memberServiceId;

    private Integer memberServiceItemId;

    private Integer times;

    private Integer period;

    private String periodUnit;

    private Date createDate;

    private Date updateDate;

    private String createBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberServiceId() {
        return memberServiceId;
    }

    public void setMemberServiceId(Integer memberServiceId) {
        this.memberServiceId = memberServiceId;
    }

    public Integer getMemberServiceItemId() {
        return memberServiceItemId;
    }

    public void setMemberServiceItemId(Integer memberServiceItemId) {
        this.memberServiceItemId = memberServiceItemId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}