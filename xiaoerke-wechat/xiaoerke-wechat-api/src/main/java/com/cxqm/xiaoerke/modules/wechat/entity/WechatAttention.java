package com.cxqm.xiaoerke.modules.wechat.entity;

import java.util.Date;

public class WechatAttention {
    private String id;

    private String openid;

    private String marketer;

    private Date date;

    private String status;

    private String nickname;

    private String phone;
    
    private Byte isPay;

    private Date updateTime;

    private String doctor_marketer;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }

    public Date getDate() {
        return date;
    }

    public Byte getIsPay() {
        return isPay;
    }

    public void setIsPay(Byte isPay) {
        this.isPay = isPay;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDoctor_marketer() {
        return doctor_marketer;
    }

    public void setDoctor_marketer(String doctor_marketer) {
        this.doctor_marketer = doctor_marketer;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}