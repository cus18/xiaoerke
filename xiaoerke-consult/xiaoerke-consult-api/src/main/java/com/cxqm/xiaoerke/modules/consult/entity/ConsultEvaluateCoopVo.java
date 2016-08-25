package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 合作方评价信息
 * Created by jiangzhongge on 2016-8-24.
 */
public class ConsultEvaluateCoopVo {
        private Integer id ;
        private Integer sessionId ;
        private String userId ;
        private String csUserId ;
        private Integer evaluateLevel ;
        private String suggestMessage ;
        private Date evaluateDate ;
        private Date createDate ;
        private String source ;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getSessionId() {
                return sessionId;
        }

        public void setSessionId(Integer sessionId) {
                this.sessionId = sessionId;
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

        public Integer getEvaluateLevel() {
                return evaluateLevel;
        }

        public void setEvaluateLevel(Integer evaluateLevel) {
                this.evaluateLevel = evaluateLevel;
        }

        public String getSuggestMessage() {
                return suggestMessage;
        }

        public void setSuggestMessage(String suggestMessage) {
                this.suggestMessage = suggestMessage;
        }

        public Date getEvaluateDate() {
                return evaluateDate;
        }

        public void setEvaluateDate(Date evaluateDate) {
                this.evaluateDate = evaluateDate;
        }

        public Date getCreateDate() {
                return createDate;
        }

        public void setCreateDate(Date createDate) {
                this.createDate = createDate;
        }

        public String getSource() {
                return source;
        }

        public void setSource(String source) {
                this.source = source;
        }
}
