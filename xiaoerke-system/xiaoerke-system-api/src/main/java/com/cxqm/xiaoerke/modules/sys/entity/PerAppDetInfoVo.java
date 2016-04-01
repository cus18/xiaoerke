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
public class PerAppDetInfoVo extends DoctorVo {
    private String doctorName;//医生名称
    private String departmentName;//医生所在科室
    private String position1;//职称
    private String position2;//职称
    private String hospitalName;//医院名称
    private String location;//所处地址
    private String appointmentNo;//预约编号
    private String status;//状态

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String getPosition1() {
        return position1;
    }

    @Override
    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    @Override
    public String getPosition2() {
        return position2;
    }

    @Override
    public void setPosition2(String position2) {
        this.position2 = position2;
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

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


