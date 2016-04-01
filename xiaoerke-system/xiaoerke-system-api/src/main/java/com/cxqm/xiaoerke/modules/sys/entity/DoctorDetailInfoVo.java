/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

/**
 * 工作流Entity
 *
 * @author 得良
 * @version 2013-11-03
 */

public class DoctorDetailInfoVo extends DoctorVo {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;//主要擅长的疾病名称
    private String doctorName;//医生名称
    private String department_level1;//医生所在科室
    private String details;//医生擅长的详细描述

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment_level1() {
        return department_level1;
    }

    public void setDepartment_level1(String department_level1) {
        this.department_level1 = department_level1;
    }
}


