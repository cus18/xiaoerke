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

    private Date inoculationTime;

    private Integer vaccineId;

    private String content;

    private Date startSearchDate;

    private Date endSearchDate;

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

    public Date getInoculationTime() {
        return inoculationTime;
    }

    public void setInoculationTime(Date inoculationTime) {
        this.inoculationTime = inoculationTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Integer vaccineId) {
        this.vaccineId = vaccineId;
    }

    public Date getStartSearchDate() {
        return startSearchDate;
    }

    public void setStartSearchDate(Date startSearchDate) {
        this.startSearchDate = startSearchDate;
    }

    public Date getEndSearchDate() {
        return endSearchDate;
    }

    public void setEndSearchDate(Date endSearchDate) {
        this.endSearchDate = endSearchDate;
    }
}