package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-6-21.
 * @version 1.0v
 * 语音聊天记录
 */
public class ConsultVoiceRecordMongoVo {

        private String id;

        private Integer sessionId;

        private String type;

        private Date createDate;

        private String csUserId;

        private String userId;

        private String csUserName;

        private String userName;

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

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        private String content;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public Integer getSessionId() {
                return sessionId;
        }

        public void setSessionId(Integer sessionId) {
                this.sessionId = sessionId;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public Date getCreateDate() {
                return createDate;
        }

        public void setCreateDate(Date createDate) {
                this.createDate = createDate;
        }

        public String getCsUserId() {
                return csUserId;
        }

        public void setCsUserId(String csUserId) {
                this.csUserId = csUserId;
        }

        public String getUserId() {
                return userId;
        }

        public void setUserId(String userId) {
                this.userId = userId;
        }
}
