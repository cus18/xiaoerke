package com.cxqm.xiaoerke.modules.consult.entity;

/**
 * 根据此vo判断redis中的会话是否过期
 * @author deliang
 */
public class ConsultSessionStatusVo {
    private String sessionId;

    private String lastMessageTime;

    private String userId;


    private String status;

    private String userName;

    private String csUserName;

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

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}