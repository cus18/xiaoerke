package com.cxqm.xiaoerke.modules.consult.entity;


/**
 * 运营推广vo
 * Created by sunxiao on 2016-9-19.
 */
public class OperationPromotionVo {

        private Integer id ;

        private String roleId;

        private String keyword ;

        private String roleName ;

        private String messageType;

        private String replyText;

        private String replyPicId;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getKeyword() {
                return keyword;
        }

        public void setKeyword(String keyword) {
                this.keyword = keyword;
        }

        public String getRoleName() {
                return roleName;
        }

        public String getRoleId() {
                return roleId;
        }

        public void setRoleId(String roleId) {
                this.roleId = roleId;
        }

        public String getMessageType() {
                return messageType;
        }

        public void setMessageType(String messageType) {
                this.messageType = messageType;
        }

        public String getReplyText() {
                return replyText;
        }

        public void setReplyText(String replyText) {
                this.replyText = replyText;
        }

        public String getReplyPicId() {
                return replyPicId;
        }

        public void setReplyPicId(String replyPicId) {
                this.replyPicId = replyPicId;
        }

        public void setRoleName(String roleName) {
                this.roleName = roleName;
        }
}
