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
public class DoctorIllnessRelationVo extends BaseEntity<DoctorIllnessRelationVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 主键
    private String sys_doctor_id;    // 医生表主键
    private String sys_illness_id;    //疾病表主键
    private String phone;       //医生手机号
    private String level_1;     //一级疾病
    private String level_2;     //二级疾病
    private String doctorName;  //医生名称
    public DoctorIllnessRelationVo() {
        super();
    }

    @Override
    public void preInsert() {

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

    public String getSys_doctor_id() {
        return sys_doctor_id;
    }

    public void setSys_doctor_id(String sys_doctor_id) {
        this.sys_doctor_id = sys_doctor_id;
    }

    public String getSys_illness_id() {
        return sys_illness_id;
    }

    public void setSys_illness_id(String sys_illness_id) {
        this.sys_illness_id = sys_illness_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel_1() {
        return level_1;
    }

    public void setLevel_1(String level_1) {
        this.level_1 = level_1;
    }

    public String getLevel_2() {
        return level_2;
    }

    public void setLevel_2(String level_2) {
        this.level_2 = level_2;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}


