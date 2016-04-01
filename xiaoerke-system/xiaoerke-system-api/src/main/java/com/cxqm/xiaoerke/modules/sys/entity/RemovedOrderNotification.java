/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class RemovedOrderNotification extends Notification {
	private String sysRegisterServiceId; // 关联的register_service的ID
	private String orderId; // 关联的patient表ID
	private String orderStatus; // 加号业务的用户订购状态
	private String registerNo; // 加号订购编号
	private String phone; // 用户手机号
	private String babyName; // 用户加号此业务的孩子名称
	private String illness; // 疾病相关描述
	private Date birthday; // 孩子的生日
	private Date date;// 医生加号表中的时间
	private Date beginTime;// 开始时间

	private Date endTime;// 结束时间
	private String doctorId;
	private String doctorName;// 医生姓名
	private String serviceType;
	private Date createdTime;
	private String isPayDeposit;
	private String deleteBy;
	
	public String getDeleteBy() {
		return deleteBy;
	}

	public void setDeleteBy(String deleteBy) {
		this.deleteBy = deleteBy;
	}

	public String getIsPayDeposit() {
		return isPayDeposit;
	}

	public void setIsPayDeposit(String isPayDeposit) {
		this.isPayDeposit = isPayDeposit;
	}


	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getSysRegisterServiceId() {
		return sysRegisterServiceId;
	}

	public void setSysRegisterServiceId(String sysRegisterServiceId) {
		this.sysRegisterServiceId = sysRegisterServiceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBabyName() {
		return babyName;
	}

	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}

	public String getIllness() {
		return illness;
	}

	public void setIllness(String illness) {
		this.illness = illness;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

}
