package com.cxqm.xiaoerke.modules.order.entity;

import com.cxqm.xiaoerke.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 电话咨询手动接通电话vo
 * sunxiao
 */
public class ConsultPhoneManuallyConnectVo {

    private Integer id;

    private Integer orderId;

    private Date createTime;

    private Date updateTime;

    private String userPhone;

    private String doctorPhone;

    private String operBy;

    private Date dialDate;//开始时间

    private String dialType;

    private long surplusTime; //通话剩余时长
    private Date surplusDate; //通话剩余时长
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSurplusDate() {
        return surplusDate;
    }

    public void setSurplusDate(Date surplusDate) {
        this.surplusDate = surplusDate;
    }

    public long getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(long surplusTime) {
        this.surplusTime = surplusTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getOperBy() {
        return operBy;
    }

    public void setOperBy(String operBy) {
        this.operBy = operBy;
    }

    public Date getDialDate() {
        return dialDate;
    }

    public void setDialDate(Date dialDate) {
        this.dialDate = dialDate;
    }


    public String getDialType() {
        return dialType;
    }

    public void setDialType(String dialType) {
        this.dialType = dialType;
    }

}