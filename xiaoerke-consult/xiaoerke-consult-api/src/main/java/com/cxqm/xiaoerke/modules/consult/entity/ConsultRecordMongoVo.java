package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class ConsultRecordMongoVo {
    private String id;

    private String sessionId;

    private String consultType;

    private String message;

    private String worker;

    private Date createDate;

    private String infoDate;

    private Date updateDate;

    private String opercode;

    private String userId;

    private String attentionNickname;

    private String csUserId;

    private String senderId;

    private String type;

    private String doctorName;

    private String senderName;

    private String doctorcs;

    private String nickName;

    private String source ;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAttentionNickname() {
        return attentionNickname;
    }

    public void setAttentionNickname(String attentionNickname) {
        this.attentionNickname = attentionNickname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDoctorcs() {
        return doctorcs;
    }

    public void setDoctorcs(String doctorcs) {
        this.doctorcs = doctorcs;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getType() {
        return type;
    }

    public String getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(String infoDate) {
        this.infoDate = infoDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
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

    public String getOpercode() {
        return opercode;
    }

    public void setOpercode(String opercode) {
        this.opercode = opercode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public String getConsultType() {
        return consultType;
    }

    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }
}