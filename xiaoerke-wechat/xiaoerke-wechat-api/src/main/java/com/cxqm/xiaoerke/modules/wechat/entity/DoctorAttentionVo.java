package com.cxqm.xiaoerke.modules.wechat.entity;

import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

/**
 * 医生关注Vo
 *
 * @author 得良
 */
public class DoctorAttentionVo extends WechatAttention {

    private String doctorName;//医生名字
    private String department;//医生所在科室
    private String registerServiceCount;//号源总数
    private String appointNumberAlready;//已经预约号源数
    private String appointNumber;//可预约号源数
    private String hospitalName;
    private String dateDisplay;
    private String startDate;//查询开始时间
    private String endDate;//查询结束时间


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRegisterServiceCount() {
        return registerServiceCount;
    }

    public void setRegisterServiceCount(String registerServiceCount) {
        this.registerServiceCount = registerServiceCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    public String getAppointNumberAlready() {
        return appointNumberAlready;
    }

    public void setAppointNumberAlready(String appointNumberAlready) {
        this.appointNumberAlready = appointNumberAlready;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAppointNumber() {
        return appointNumber;
    }

    public void setAppointNumber(String appointNumber) {
        this.appointNumber = appointNumber;
    }
}