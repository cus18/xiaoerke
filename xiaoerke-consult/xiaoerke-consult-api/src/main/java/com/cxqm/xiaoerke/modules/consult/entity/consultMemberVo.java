package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class consultMemberVo {
    private Integer id;

    private String nickname;

    private Date createDate;

    private String payAcount;

    private Date endTime;

    private String memberType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPayAcount() {
        return payAcount;
    }

    public void setPayAcount(String payAcount) {
        this.payAcount = payAcount;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
}