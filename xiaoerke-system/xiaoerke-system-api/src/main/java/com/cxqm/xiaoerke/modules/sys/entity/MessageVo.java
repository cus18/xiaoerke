/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class MessageVo extends BaseEntity<MessageVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 账户表的主键ID
    private String sysPatientId;    // sys_patient表的ID
    private String messageType;    // 消息类型
    private String messageTitle;    // 消息标题
    private String messageContent;    // 消息内容
    private Date createTime;    // 记录创建时间
    private Date updateTime;    // 记录删除时间
    private String sysUserId; //用户id
    public MessageVo() {
        super();
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}


