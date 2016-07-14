package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-7-13.
 */
public class ConsultCountTotal {
        private String id ;
        private Date createDate;
        private String userId;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public Date getCreateDate() {
                return createDate;
        }

        public void setCreateDate(Date createDate) {
                this.createDate = createDate;
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

        private String csUserId;

}
