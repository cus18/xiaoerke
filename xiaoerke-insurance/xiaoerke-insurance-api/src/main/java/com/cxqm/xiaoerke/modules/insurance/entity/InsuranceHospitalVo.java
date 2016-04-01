package com.cxqm.xiaoerke.modules.insurance.entity;


/**
 * 保险关联医院实体类
 * @author sunxiao
 *
 */
public class InsuranceHospitalVo {
	
	private Integer id;
	
	private String hospitalName;
	
	private String location;
	
	private String district;
	
	private String phone;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
