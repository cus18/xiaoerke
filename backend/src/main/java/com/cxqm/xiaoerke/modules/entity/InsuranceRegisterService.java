package com.cxqm.xiaoerke.modules.entity;

import java.util.Date;

import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;

public class InsuranceRegisterService {

    private String id;

    private String parentType;

    private String insuranceType;

    private String parentName;

    private String parentId;

    private String state;

    private String parentPhone;

    private String idCard;

    private Date createTime;

    private Date updateTime;

    private String babyId;

    private String babyName;

    private Date startTime;

    private Date endTime;

    private String fromOrderDate;

    private String toOrderDate;

    private String fromStartDate;

    private String toStartDate;

    private String nickName;

    private String updateBy;

    private Float price;

    private String auditReason;

    private String source;

    private String gender;

    private Date birthday;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @ExcelField(title="来源", align=2, sort=13)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    private String name;

    @ExcelField(title="宝宝姓名", align=2, sort=6)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title="操作人", align=2, sort=11)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @ExcelField(title="微信名", align=2, sort=2)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }


    public String getFromOrderDate() {
        return fromOrderDate;
    }

    public void setFromOrderDate(String fromOrderDate) {
        this.fromOrderDate = fromOrderDate;
    }

    public String getToOrderDate() {
        return toOrderDate;
    }

    public void setToOrderDate(String toOrderDate) {
        this.toOrderDate = toOrderDate;
    }

    public String getFromStartDate() {
        return fromStartDate;
    }

    public void setFromStartDate(String fromStartDate) {
        this.fromStartDate = fromStartDate;
    }

    public String getToStartDate() {
        return toStartDate;
    }

    public void setToStartDate(String toStartDate) {
        this.toStartDate = toStartDate;
    }

    @ExcelField(title="开始时间", align=2, sort=8)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @ExcelField(title="结束时间", align=2, sort=9)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @ExcelField(title="订单号", align=2, sort=1)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    @ExcelField(title="购买人", align=2, sort=4)
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @ExcelField(title="状态", align=2, sort=12)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @ExcelField(title="手机号", align=2, sort=3)
    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    @ExcelField(title="身份证号", align=2, sort=5)
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @ExcelField(title="下单时间", align=2, sort=7)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ExcelField(title="最后操作时间", align=2, sort=10)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

}
