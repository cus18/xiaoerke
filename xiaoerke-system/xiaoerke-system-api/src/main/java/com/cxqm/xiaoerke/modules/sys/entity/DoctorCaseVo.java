/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author deliang
 * @version 2015-10-16
 */
public class DoctorCaseVo extends BaseEntity<DoctorCaseVo> {
	private static final long serialVersionUID = 1L;
	
    private String id;        //案例表主键
    private String sys_doctor_id;        //医生主键
    private String doctor_case_name;        //案例名称
    private String patient_register_service_id;        //订单表主键
    private Integer doctor_case_number;        //案例数
    private Integer sort;        //排序
	private String display_status;//是否显示，显示为 0 ，不显示为 1
	private String sys_register_service_id; // 对应的号源主键
	private String sys_patient_id;//用户表主键
	private String doctorName;//医生姓名
	private String registerNo;//订单编号
	private Date create_date;//创建时间
	private Date update_date;//修改时间


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getSys_register_service_id() {
		return sys_register_service_id;
	}

	public String getSys_patient_id() {
		return sys_patient_id;
	}

	public void setSys_patient_id(String sys_patient_id) {
		this.sys_patient_id = sys_patient_id;
	}

	public void setSys_register_service_id(String sys_register_service_id) {
		this.sys_register_service_id = sys_register_service_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSys_doctor_id() {
		return sys_doctor_id;
	}

	public void setSys_doctor_id(String sys_doctor_id) {
		this.sys_doctor_id = sys_doctor_id;
	}

	public String getDoctor_case_name() {
		return doctor_case_name;
	}

	public void setDoctor_case_name(String doctor_case_name) {
		this.doctor_case_name = doctor_case_name;
	}

	public String getPatient_register_service_id() {
		return patient_register_service_id;
	}

	public void setPatient_register_service_id(String patient_register_service_id) {
		this.patient_register_service_id = patient_register_service_id;
	}

	public Integer getDoctor_case_number() {
		return doctor_case_number;
	}

	public void setDoctor_case_number(Integer doctor_case_number) {
		this.doctor_case_number = doctor_case_number;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDisplay_status() {
		return display_status;
	}

	public void setDisplay_status(String display_status) {
		this.display_status = display_status;
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


