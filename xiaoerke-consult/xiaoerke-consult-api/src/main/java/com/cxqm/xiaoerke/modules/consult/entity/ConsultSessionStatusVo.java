package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 根据此vo判断redis中的会话是否过期
 * @author deliang
 */
public class ConsultSessionStatusVo {
    private String sessionId;

    private Date lastMessageTime;

    private String userId;

    private String createDate;

    private String status;

    private String userName;

    private String csUserName;

    private String csUserId;

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
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

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}