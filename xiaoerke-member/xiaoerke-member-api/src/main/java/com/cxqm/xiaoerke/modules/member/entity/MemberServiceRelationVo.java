package com.cxqm.xiaoerke.modules.member.entity;

import java.util.Date;

public class MemberServiceRelationVo {
    private Integer id;

    private String sysUserId;

    private Integer sysActivityId;

    private String phone;

    private String openid;

    private Integer memberServiceId;

    private Date createDate;

    private Date updateDate;

    private String createBy;

    private Date activateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getMemberServiceId() {
        return memberServiceId;
    }

    public Integer getSysActivityId() {
        return sysActivityId;
    }

    public void setSysActivityId(Integer sysActivityId) {
        this.sysActivityId = sysActivityId;
    }

    public void setMemberServiceId(Integer memberServiceId) {
        this.memberServiceId = memberServiceId;
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

    public Date getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(Date activateDate) {
        this.activateDate = activateDate;
    }
}