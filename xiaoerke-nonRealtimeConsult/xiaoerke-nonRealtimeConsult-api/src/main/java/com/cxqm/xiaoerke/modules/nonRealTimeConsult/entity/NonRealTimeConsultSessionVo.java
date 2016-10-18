package com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity;

import java.util.Date;

public class NonRealTimeConsultSessionVo {
    private Integer id;

    private String status;

    private String userId;

    private String userName;

    private String csUserId;

    private String csUserName;

    private String source;

    private Date lastMessageTime;

    private String dispalyTimes;

    private String order;

    private String lastMessageType;

    private Integer consultNumber;

    private String doctorDepartmentName;

    private String doctorProfessor;

    private Date createTime;

    private Date updateTime;

    private String bak1;

    private String bak2;

    private String bak3;

    private String lastMessageContent;

    private String babyInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getBabyInfo() {
        return babyInfo;
    }

    public void setBabyInfo(String babyInfo) {
        this.babyInfo = babyInfo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDispalyTimes() {
        return dispalyTimes;
    }

    public void setDispalyTimes(String dispalyTimes) {
        this.dispalyTimes = dispalyTimes;
    }

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public String getCsUserName() {
        return csUserName;
    }

    public void setCsUserName(String csUserName) {
        this.csUserName = csUserName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public Integer getConsultNumber() {
        return consultNumber;
    }

    public void setConsultNumber(Integer consultNumber) {
        this.consultNumber = consultNumber;
    }

    public String getDoctorDepartmentName() {
        return doctorDepartmentName;
    }

    public void setDoctorDepartmentName(String doctorDepartmentName) {
        this.doctorDepartmentName = doctorDepartmentName;
    }

    public String getDoctorProfessor() {
        return doctorProfessor;
    }

    public void setDoctorProfessor(String doctorProfessor) {
        this.doctorProfessor = doctorProfessor;
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

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }
}