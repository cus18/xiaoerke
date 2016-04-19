package com.cxqm.xiaoerke.modules.consult.entity;

/**
 * 根据此vo判断redis中的会话是否过期
 * @author deliang
 */
public class ConsultSessionStatusVo {
    private String sessionId;

    private String lastMessageTime;

    private String UserId;

    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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