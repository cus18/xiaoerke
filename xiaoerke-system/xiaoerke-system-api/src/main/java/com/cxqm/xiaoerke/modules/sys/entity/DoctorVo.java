/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class DoctorVo<T> extends BaseEntity<DoctorVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 医生表的主键ID
    private String sysUserId;    // sys_user表的ID
    private String sysHospitalId;    // sys_hospital表的ID
    private String hospital;    // 医生所归属医院的名称
    private Date careerTime;    // 医生工作的时间
    private String personDetails;    // 医生的详细描述
    private String position1;    // 医生的一级职务
    private String position2;    // 医生的二级职务
    private int commentNumber; //医生的评论数
    private int fansNumber; //医生的粉丝数
    private String experience; //医生专业擅长描述
    private String createTime; //创建时间
    private String careerTimeForDisplay;
    private String doctorName;
    //医生姓名
    private String name;
    //医生的手机号
    private String phone;

    private String hospitalName;
    private String cardExperience;
    private String imgUrl;
    private Date latestTime;
    private String location;
    private String create_date;
    private int subsidy;


    private String placeDetail;//就诊地址
    private String relationType;//与医院所属关系
    private String departmentLevel1;//一级科室
    private String departmentLevel2;//二级科室
    private String contactPerson;//联系人
    private String contactPersonPhone;//联系人电话
    private List<T> doctorList;
    private String phoneConsultFlag;

    public String getPhoneConsultFlag() {
		return phoneConsultFlag;
	}

	public void setPhoneConsultFlag(String phoneConsultFlag) {
		this.phoneConsultFlag = phoneConsultFlag;
	}

	private String isDisplay;//控制该医生在客户端是否显示

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(int subsidy) {
        this.subsidy = subsidy;
    }

    public List<T> getdoctorList() {
        return doctorList;
    }

    public void setdoctorList(List<T> list) {
        this.doctorList = list;
    }

    private List<String> hospitalList = new ArrayList<String>();//医生所属医院列表

    public String getCareerTimeForDisplay() {
        return careerTimeForDisplay;
    }

    public void setCareerTimeForDisplay(String careerTimeForDisplay) {
        this.careerTimeForDisplay = careerTimeForDisplay;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public List<String> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<String> hospitalList) {
        this.hospitalList = hospitalList;
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

    public String getCardExperience() {
        return cardExperience;
    }

    public void setCardExperience(String cardExperience) {
        this.cardExperience = cardExperience;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DoctorVo() {
        super();
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    @Override
    public void preInsert() {

    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysHospitalId() {
        return sysHospitalId;
    }

    public void setSysHospitalId(String sysHospitalId) {
        this.sysHospitalId = sysHospitalId;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public Date getCareerTime() {
        return careerTime;
    }

    public void setCareerTime(Date careerTime) {
        this.careerTime = careerTime;
    }

    public String getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(String personDetails) {
        this.personDetails = personDetails;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(int fansNumber) {
        this.fansNumber = fansNumber;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}


