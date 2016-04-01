/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.entity;

import java.util.Date;
import java.util.List;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

/**
 * 工作流Entity
 *
 * @author sunxiao
 * @version 2015-09-01
 */
public class RegisterTemplateServiceVo extends BaseEntity<RegisterTemplateServiceVo> {

	private static final long serialVersionUID = 1L;
	
	public static String STATUS_WORKING = "1";
	public static String STATUS_STOPPED = "0";
    private String id;        // 医生注册加号业务的主键ID
    private String doctorId;    // doctor表ID
    private String locationId;  //医生出诊的医院具体位置
    private String hospitalId;
    private Float price;  //医生出诊服务价格
    private String weekDay;
	private String time;
    private String status; //加号业务的状态
    private String serverType; //业务类型
	private Date createDate;
    private Date updateDate;
    private String repeatInterval;//是否隔周重复
	private List<String> times;
    
    public String getRepeatInterval() {
    	return repeatInterval;
    }
    public void setRepeatInterval(String repeatInterval) {
    	this.repeatInterval = repeatInterval;
    }
    public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getHospitalId() {
    	return hospitalId;
    }
    public void setHospitalId(String hospitalId) {
    	this.hospitalId = hospitalId;
    }
    
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServiceType(String serverType) {
		this.serverType = serverType;
	}

	public List<String> getTimes() {
		return times;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}
    
}


