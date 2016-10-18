package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-9-21.
 */
public class ConsultCoopUserInfoVo {

        private Integer id ;
        private String userId ;
        private String userName ;
        private String headImg ;
        private String source ;
        private Date createDate ;
        private Date updateDate ;
        private String userPhone ;
        private String sex ;
        private Integer delFlag ;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUserId() {
                return userId;
        }

        public void setUserId(String userId) {
                this.userId = userId;
        }

        public String getUserName() {
                return userName;
        }

        public void setUserName(String userName) {
                this.userName = userName;
        }

        public String getHeadImg() {
                return headImg;
        }

        public void setHeadImg(String headImg) {
                this.headImg = headImg;
        }

        public String getSource() {
                return source;
        }

        public void setSource(String source) {
                this.source = source;
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

        public String getUserPhone() {
                return userPhone;
        }

        public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
        }

        public String getSex() {
                return sex;
        }

        public void setSex(String sex) {
                this.sex = sex;
        }

        public Integer getDelFlag() {
                return delFlag;
        }

        public void setDelFlag(Integer delFlag) {
                this.delFlag = delFlag;
        }
}
