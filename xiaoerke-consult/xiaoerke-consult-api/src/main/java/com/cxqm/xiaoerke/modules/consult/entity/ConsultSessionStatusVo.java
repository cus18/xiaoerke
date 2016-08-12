package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 根据此vo判断redis中的会话是否过期
 * @author deliang
 */
public class ConsultSessionStatusVo {

    private  String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String sessionId;

    private Date lastMessageTime;

    private String userId;

    private Date createDate;

    private Date firstTransTime;

    private String status;

    private String userName;

    private String csUserName;

    private String csUserId;

    private String source;

    private String flag;

    private String payStatus;

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCsUserName() {
        return csUserName;
    }

    public void setCsUserName(String csUserName) {
        this.csUserName = csUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
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

    public String getSessionId() {
        return sessionId;
    }

    public Date getFirstTransTime() {
        return firstTransTime;
    }

    public void setFirstTransTime(Date firstTransTime) {
        this.firstTransTime = firstTransTime;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}