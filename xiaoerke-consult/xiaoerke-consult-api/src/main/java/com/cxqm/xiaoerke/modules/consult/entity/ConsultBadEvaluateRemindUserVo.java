package com.cxqm.xiaoerke.modules.consult.entity;

import com.cxqm.xiaoerke.common.persistence.DataEntity;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-6-22.
 */
public class ConsultBadEvaluateRemindUserVo{

        private Integer id;
        private String openId;
        private String csUserName;
        private Date createDate;
        private Date updateDate;
        private String delFlag;   //'0'未删除；'1'已删除

        public String getOpenId() {
                return openId;
        }

        public void setOpenId(String openId) {
                this.openId = openId;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getCsUserName() {
                return csUserName;
        }

        public void setCsUserName(String csUserName) {
                this.csUserName = csUserName;
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

        public String getDelFlag() {
                return delFlag;
        }

        public void setDelFlag(String delFlag) {
                this.delFlag = delFlag;
        }
}
