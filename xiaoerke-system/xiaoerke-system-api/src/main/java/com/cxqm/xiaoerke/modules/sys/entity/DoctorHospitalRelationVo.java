/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class DoctorHospitalRelationVo extends BaseEntity<DoctorHospitalRelationVo> {

    private static final long serialVersionUID = 1L;
    private String id; //主键
    private String sysDoctorId;//医生表主键
    private String sysHospitalId;//医院表主键
    private String doctorName;//医生名称
    private String hospitalName;//医院名称
    private String placeDetail;//医生在此医院执业的位置

	private String relationType;//医生和此医院的关系
    private String departmentLevel1;//一级科室名称
    private String departmentLevel2;//二级科室名称

	private String contactPerson;//联系人

	private String contactPersonPhone;//联系人电话
    private String phone;//医生电话号码
    private String location;
    private String route;

    private String kindlyReminder;//温馨提示

    public String getKindlyReminder() {
        return kindlyReminder;
    }

    public void setKindlyReminder(String kindlyReminder) {
        this.kindlyReminder = kindlyReminder;
    }

    public String getRoute() {
    	return route;
    }
    
    public void setRoute(String route) {
    	this.route = route;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
	public DoctorHospitalRelationVo() {
        super();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSysDoctorId() {
        return sysDoctorId;
    }

    public void setSysDoctorId(String sysDoctorId) {
        this.sysDoctorId = sysDoctorId;
    }

    public String getSysHospitalId() {
        return sysHospitalId;
    }

    public void setSysHospitalId(String sysHospitalId) {
        this.sysHospitalId = sysHospitalId;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getDepartmentLevel1() {
        return departmentLevel1;
    }

    public void setDepartmentLevel1(String departmentLevel1) {
        this.departmentLevel1 = departmentLevel1;
    }

    public String getDepartmentLevel2() {
        return departmentLevel2;
    }

    public void setDepartmentLevel2(String departmentLevel2) {
        this.departmentLevel2 = departmentLevel2;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


}


