package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class ConsultSessionPropertyVo {
    private Integer id;

    private String sysUserId;

    private Integer monthTimes;

    private Integer permTimes;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

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

    public Integer getMonthTimes() {
        return monthTimes;
    }

    public void setMonthTimes(Integer monthTimes) {
        this.monthTimes = monthTimes;
    }

    public Integer getPermTimes() {
        return permTimes;
    }

    public void setPermTimes(Integer permTimes) {
        this.permTimes = permTimes;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
}