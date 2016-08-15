package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 第三方合作宝宝信息
 * Created by jiangzhongge on 2016-8-15.
 *
 */
public class CoopThirdBabyInfoVo {

        private Integer id ;
        private String name ;
        private String gender ;      //1：女 2：男
        private Date birthday ;
        private String source ;
        private String sysUserId ; //宝宝父母的ID
        private String status ;    //
        private String delFlag;   // 0：未删除 1: 删除
        private Date createDate ;
        private Date updateTime;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getGender() {
                return gender;
        }

        public void setGender(String gender) {
                this.gender = gender;
        }

        public Date getBirthday() {
                return birthday;
        }

        public void setBirthday(Date birthday) {
                this.birthday = birthday;
        }

        public String getSource() {
                return source;
        }

        public void setSource(String source) {
                this.source = source;
        }

        public String getSysUserId() {
                return sysUserId;
        }

        public void setSysUserId(String sysUserId) {
                this.sysUserId = sysUserId;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getDelFlag() {
                return delFlag;
        }

        public void setDelFlag(String delFlag) {
                this.delFlag = delFlag;
        }

        public Date getCreateDate() {
                return createDate;
        }

        public void setCreateDate(Date createDate) {
                this.createDate = createDate;
        }

        public Date getUpdateTime() {
                return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
                this.updateTime = updateTime;
        }
}
