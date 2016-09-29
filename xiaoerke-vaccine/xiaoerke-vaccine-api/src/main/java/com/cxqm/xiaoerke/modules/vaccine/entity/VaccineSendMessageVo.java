package com.cxqm.xiaoerke.modules.vaccine.entity;

import java.util.Date;

public class VaccineSendMessageVo {
    private Integer id;

    private String sysUserId;

    private Date sendTime;

    private String searchTime;

    private String searchCreateTime;

    private String validFlag;

    private String createBy;

    private Date createTime;

    private String msgType;

    private Date updateTime;

    private String bak2;

    private Integer nextVaccineId;

    private String content;

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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public Integer getNextVaccineId() {
        return nextVaccineId;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public String getSearchCreateTime() {
        return searchCreateTime;
    }

    public void setSearchCreateTime(String searchCreateTime) {
        this.searchCreateTime = searchCreateTime;
    }

    public void setNextVaccineId(Integer nextVaccineId) {
        this.nextVaccineId = nextVaccineId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}