/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class HospitalVo<T> extends BaseEntity<HospitalVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 医院的主键ID
    private String name;    // 医院名称
    private String position;    // 医院的位置
    private String details;    // 医院的详细描述
    private String cityName;   //城市名称
    private String medicalProcess;//就诊流程
    private String sort;//排序
    private List<T> hospitalList;
    private String contactName;//BD联系人
    private String contactPhone;//BD联系人电话
    private String medicalExamination;//开药及检查
    private String chargeStandard;//收费标准
    private String specialDiscount;//特别优惠
    private String hospitalType;//医院类型
    private Date createDate;//创建时间
    private Date updateDate;//更新时间
    private String createBy;//创建人
    private String businessContactName;//合作机构联系人
    private String businessContactPhone;//合作机构联系人
    private String isDisplay;//是否在客户端显示

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getBusinessContactPhone() {
        return businessContactPhone;
    }

    public void setBusinessContactPhone(String businessContactPhone) {
        this.businessContactPhone = businessContactPhone;
    }

    public String getBusinessContactName() {
        return businessContactName;
    }

    public void setBusinessContactName(String businessContactName) {
        this.businessContactName = businessContactName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public HospitalVo() {
        super();
    }

    public String getHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(String hospitalType) {
        this.hospitalType = hospitalType;
    }

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public String getMedicalExamination() {
        return medicalExamination;
    }

    public void setMedicalExamination(String medicalExamination) {
        this.medicalExamination = medicalExamination;
    }

    public String getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(String specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<T> gethospitalList() {
        return hospitalList;
    }

    public void sethospitalList(List<T> list) {
        this.hospitalList = list;
    }

    public String getMedicalProcess() {
        return medicalProcess;
    }

    public void setMedicalProcess(String medicalProcess) {
        this.medicalProcess = medicalProcess;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}


