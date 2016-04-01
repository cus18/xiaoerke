package com.cxqm.xiaoerke.modules.task.entity;

import java.util.Date;

public class WechatRecord {
    private String id;

    private String openid;

    private String opercode;

    private Date infoTime;

    private String worker;

    private String text;

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

    public String getOpercode() {
        return opercode;
    }

    public void setOpercode(String opercode) {
        this.opercode = opercode;
    }

    public Date getinfoTime() {
        return infoTime;
    }

    public void setinfoTime(Date infotime) {
        this.infoTime = infotime;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}