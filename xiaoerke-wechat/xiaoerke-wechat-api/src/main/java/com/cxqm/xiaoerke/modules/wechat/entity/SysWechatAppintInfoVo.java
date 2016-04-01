/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.wechat.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class SysWechatAppintInfoVo extends BaseEntity<SysWechatAppintInfoVo> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String patient_register_service_id;
    private String sys_register_service_id;
    private String open_id;
    private String wechat_name;
    private String phone;
    private String marketer;
    private String attention_time;
    private Date create_time;
    private Date update_time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    public String getPatient_register_service_id() {
        return patient_register_service_id;
    }

    public void setPatient_register_service_id(String patient_register_service_id) {
        this.patient_register_service_id = patient_register_service_id;
    }

    public String getSys_register_service_id() {
        return sys_register_service_id;
    }

    public void setSys_register_service_id(String sys_register_service_id) {
        this.sys_register_service_id = sys_register_service_id;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getWechat_name() {
        return wechat_name;
    }

    public void setWechat_name(String wechat_name) {
        this.wechat_name = wechat_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }

    public String getAttention_time() {
        return attention_time;
    }

    public void setAttention_time(String attention_time) {
        this.attention_time = attention_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}


