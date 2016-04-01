/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

/**
 * 工作流Entity
 *
 * @author sunxiao @edit deliang
 * @version 2015-8-4
 */
public class DoctorLocationVo extends BaseEntity<DoctorLocationVo> {
    private static final long serialVersionUID = 1L;

    private String id;        // 医生具体出诊地点id

    private String sysDoctorId;    // doctor表ID

    private String sysHospitalId;    // hospital表ID

    private String location;  //医生出诊的医院具体位置

    private String doctorName;//医生姓名

    private String hospitalName;//医院名称

    private String hospital;//所属医院名称

    private String route;

    private Float price;  //医生出诊服务价格

    private String kindlyReminder;

    public String getKindlyReminder() {
        return kindlyReminder;
    }

    public void setKindlyReminder(String kindlyReminder) {
        this.kindlyReminder = kindlyReminder;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        // TODO Auto-generated method stub

    }

    @Override
    public void preUpdate() {
        // TODO Auto-generated method stub

    }
}


