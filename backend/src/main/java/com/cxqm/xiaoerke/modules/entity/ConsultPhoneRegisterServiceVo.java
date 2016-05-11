package com.cxqm.xiaoerke.modules.entity;

import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 电话咨询订单vo
 * sunxiao 2016-5-11
 */
 
public class ConsultPhoneRegisterServiceVo {

    private Integer id;

    private Integer sysPhoneconsultServiceId;

    private String sysPatientId;

    private String state;//订单状态 0-待支付  1-待接通  2-待评价  3-待分享 4-已取消

    private String payState;//订单支付状态

    private Date createTime;

    private Date updateTime;

    private String registerNo;

    private Integer illnessDescribeId;

    private String phoneNum;

    private String loginPhone;

    private String babyName;

    private String creat_by;

    private String type;

    private String nickName;//微信号

    private String deleteBy;//删除订单人

    private String doctorName;//预约医生姓名

    private String doctorId;

    private String orderTimeFromStr;//下单时间从

    private String orderTimeToStr;//下单时间到

    private String consultPhoneTimeFromStr;//咨询时间从

    private String consultPhoneTimeToStr;//咨询时间到

    private Date date;//医生加号表中的时间

    private Date beginTime;//开始时间

    private Date endTime;//开始时间

    private long surplusTime; //通话剩余时长

    private Date surplusDate; //通话剩余时长

    private String callSid; //通话的唯一标示

    private String price;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    @ExcelField(title="登录手机号", align=2, sort=4)
    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @ExcelField(title="下单时间", align=2, sort=3)
    public String getOrderTimeFromStr() {
        return orderTimeFromStr;
    }

    @ExcelField(title="宝宝姓名", align=2, sort=6)
    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    @ExcelField(title="微信号", align=2, sort=2)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @ExcelField(title="删除人", align=2, sort=12)
    public String getDeleteBy() {
        return deleteBy;
    }

    public Date getSurplusDate() {
        return surplusDate;
    }

    public void setSurplusDate(Date surplusDate) {
        this.surplusDate = surplusDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public void setOrderTimeFromStr(String orderTimeFromStr) {
        this.orderTimeFromStr = orderTimeFromStr;
    }

    @ExcelField(title="最后操作时间", align=2, sort=11)
    public String getOrderTimeToStr() {
        return orderTimeToStr;
    }

    public void setOrderTimeToStr(String orderTimeToStr) {
        this.orderTimeToStr = orderTimeToStr;
    }

    @ExcelField(title="咨询时刻", align=2, sort=8)
    public String getConsultPhoneTimeFromStr() {
        return consultPhoneTimeFromStr;
    }

    public void setConsultPhoneTimeFromStr(String consultPhoneTimeFromStr) {
        this.consultPhoneTimeFromStr = consultPhoneTimeFromStr;
    }

    @ExcelField(title="剩余时长", align=2, sort=10)
    public String getConsultPhoneTimeToStr() {
        return consultPhoneTimeToStr;
    }

    public void setConsultPhoneTimeToStr(String consultPhoneTimeToStr) {
        this.consultPhoneTimeToStr = consultPhoneTimeToStr;
    }

    @ExcelField(title="预约医生", align=2, sort=7)
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysPhoneconsultServiceId() {
        return sysPhoneconsultServiceId;
    }

    public void setSysPhoneconsultServiceId(Integer sysPhoneconsultServiceId) {
        this.sysPhoneconsultServiceId = sysPhoneconsultServiceId;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    @ExcelField(title="订单状态", align=2, sort=11)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @ExcelField(title="订单号", align=2, sort=1)
    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public Integer getIllnessDescribeId() {
        return illnessDescribeId;
    }

    public void setIllnessDescribeId(Integer illnessDescribeId) {
        this.illnessDescribeId = illnessDescribeId;
    }

    @ExcelField(title="接听手机号", align=2, sort=5)
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCreat_by() {
        return creat_by;
    }

    public void setCreat_by(String creat_by) {
        this.creat_by = creat_by;
    }

    @ExcelField(title="预约类型", align=2, sort=9)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCallSid() {
        return callSid;
    }

    public void setCallSid(String callSid) {
        this.callSid = callSid;
    }

    public long getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(long surplusTime) {
        this.surplusTime = surplusTime;
    }
}
