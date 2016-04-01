package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class ConsultPhoneRecordVo {

    private Integer id;

    private String userdata;

    private String action;

    private String type;

    private String subtype;

    private String orderid;

    private String subid;

    private String caller;

    private String called;

    private String callsid;

    private Date starttime;

    private String byetype;

    private Date endtime;

    private String talkduration;

    private String alertingduration;

    private String billdata;

    private String noAnswerEndtime;

    private String recordurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserdata() {
        return userdata;
    }

    public void setUserdata(String userdata) {
        this.userdata = userdata;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getCallsid() {
        return callsid;
    }

    public void setCallsid(String callsid) {
        this.callsid = callsid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getByetype() {
        return byetype;
    }

    public void setByetype(String byetype) {
        this.byetype = byetype;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getTalkduration() {
        return talkduration;
    }

    public void setTalkduration(String talkduration) {
        this.talkduration = talkduration;
    }

    public String getAlertingduration() {
        return alertingduration;
    }

    public void setAlertingduration(String alertingduration) {
        this.alertingduration = alertingduration;
    }

    public String getBilldata() {
        return billdata;
    }

    public void setBilldata(String billdata) {
        this.billdata = billdata;
    }

    public String getNoAnswerEndtime() {
        return noAnswerEndtime;
    }

    public void setNoAnswerEndtime(String noAnswerEndtime) {
        this.noAnswerEndtime = noAnswerEndtime;
    }

    public String getRecordurl() {
        return recordurl;
    }

    public void setRecordurl(String recordurl) {
        this.recordurl = recordurl;
    }
}