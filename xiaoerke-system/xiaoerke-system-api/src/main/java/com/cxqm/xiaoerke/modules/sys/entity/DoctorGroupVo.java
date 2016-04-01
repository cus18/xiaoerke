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
public class DoctorGroupVo<T> extends BaseEntity<DoctorGroupVo> {

    private static final long serialVersionUID = 1L;
    private Integer doctorGroupId;                //专家团的主键ID
    private String name;             //专家团的名称
    private String description;    //专家团的描述
    private String expertise;      //专家团的擅长
    private String hospitalId;    //专家团所属医院
    private String doctorId;      //专家团首席医生

    public Integer getDoctorGroupId() {
        return doctorGroupId;
    }

    public void setDoctorGroupId(Integer doctorGroupId) {
        this.doctorGroupId = doctorGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public DoctorGroupVo() {
        super();
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

}


