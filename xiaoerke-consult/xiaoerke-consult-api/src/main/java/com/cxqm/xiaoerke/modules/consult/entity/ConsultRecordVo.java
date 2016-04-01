package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class ConsultRecordVo {
    private Long id;

    private Integer sessionId;

    private String openid;

    private String message;

    private String worker;

    private Date attentionDate;

    private String attentionMarketer;

    private String attentionNickname;

    private Date createDate;

    private Date updateDate;

    private String messageType;

    private String opercode;

    private String fromUserId;

    private String toUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public Date getAttentionDate() {
        return attentionDate;
    }

    public void setAttentionDate(Date attentionDate) {
        this.attentionDate = attentionDate;
    }

    public String getAttentionMarketer() {
        return attentionMarketer;
    }

    public void setAttentionMarketer(String attentionMarketer) {
        this.attentionMarketer = attentionMarketer;
    }

    public String getAttentionNickname() {
        return attentionNickname;
    }

    public void setAttentionNickname(String attentionNickname) {
        this.attentionNickname = attentionNickname;
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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOpercode() {
        return opercode;
    }

    public void setOpercode(String opercode) {
        this.opercode = opercode;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}