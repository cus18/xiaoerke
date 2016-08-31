package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 咨询医生信息
 * @author sunxiao
 * 2016-04-26
 */
public class ConsultDoctorInfoVo {

    private Integer id;

    private String userId;

    private String name;

    private String gender;

    private String title;

    private String hospital;

    private String department;

    private String skill;

    private String description;

    private String grabSession;

    private String sendMessage;

    private String receiveDifferentialNotification;

    private Date createDate;

    private Date updateDate;

    private String type;//类型，全职0，兼职1

    private String fromDate;

    private String toDate;

    private String practitionerCertificateNo;

    private String topics;

    private String link;

    private String lectureTime;

    public String getFromDate() {
        return fromDate;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(String lectureTime) {
        this.lectureTime = lectureTime;
    }

    public String getPractitionerCertificateNo() {
        return practitionerCertificateNo;
    }

    public void setPractitionerCertificateNo(String practitionerCertificateNo) {
        this.practitionerCertificateNo = practitionerCertificateNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrabSession() {
        return grabSession;
    }

    public void setGrabSession(String grabSession) {
        this.grabSession = grabSession;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getReceiveDifferentialNotification() {
        return receiveDifferentialNotification;
    }

    public void setReceiveDifferentialNotification(String receiveDifferentialNotification) {
        this.receiveDifferentialNotification = receiveDifferentialNotification;
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
}