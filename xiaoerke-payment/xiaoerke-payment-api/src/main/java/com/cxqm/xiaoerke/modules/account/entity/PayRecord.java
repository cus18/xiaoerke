package com.cxqm.xiaoerke.modules.account.entity;

import java.util.Date;

public class PayRecord extends Record{

    private String orderId;

    private String payType;

    private String feeType;

    private String status;

    private Date payDate;

    private String operateType;

    private String createdBy;

    private String doctorId;

    private String openId;

    private String phone;//用户手机号

    private String name;

    private String reason;//退款原因

    private Float amount;//金额

    private String leaveNote;

    public String getReason() {
        return reason;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Float getAmount() {
        return amount;
    }

    @Override
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    private Integer  memberservicerel_itemservicerel_relation_id;

    public Integer getMemberservicerel_itemservicerel_relation_id() {
        return memberservicerel_itemservicerel_relation_id;
    }

    public void setMemberservicerel_itemservicerel_relation_id(Integer memberservicerel_itemservicerel_relation_id) {
        this.memberservicerel_itemservicerel_relation_id = memberservicerel_itemservicerel_relation_id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaveNote() {
        return leaveNote;
    }

    public void setLeaveNote(String leaveNote) {
        this.leaveNote = leaveNote;
    }
}