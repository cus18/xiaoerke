package com.cxqm.xiaoerke.modules.consult.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by wangbaowei on 16/3/29.
 * 云容联响应实体
 */
@XStreamAlias("Response")
public class CallResponse {
//    认证授权结果(必填)
    private String statuscode;

//    认证结果描述(可选)
    private String statusmsg;

//    是否录音(可选)
    private String record;

//    此次通话时长单位为秒(可选)
    private String recordPoint;

//    此次通话时长单位为秒(必填)
    private String  sessiontime;

//    呼叫的计费私有数据(必选)
    private String billdata;

    //本次通话消费(可选)
    private String totalfee;


    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatusmsg() {
        return statusmsg;
    }

    public void setStatusmsg(String statusmsg) {
        this.statusmsg = statusmsg;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRecordPoint() {
        return recordPoint;
    }

    public void setRecordPoint(String recordPoint) {
        this.recordPoint = recordPoint;
    }

    public String getSessiontime() {
        return sessiontime;
    }

    public void setSessiontime(String sessiontime) {
        this.sessiontime = sessiontime;
    }

    public String getBilldata() {
        return billdata;
    }

    public void setBilldata(String billdata) {
        this.billdata = billdata;
    }

    public String getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(String totalfee) {
        this.totalfee = totalfee;
    }
}
