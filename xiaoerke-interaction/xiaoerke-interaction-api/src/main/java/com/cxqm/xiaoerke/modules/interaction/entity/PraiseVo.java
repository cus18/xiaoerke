/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author 得良
 * @version 2013-11-03
 */
public class PraiseVo extends BaseEntity<PraiseVo> {

    private static final long serialVersionUID = 1L;
    private String id;//主键
    private String patientRegisterServiceId;//加号业务表主键
    private String sysPatientId;//患者表
    private String phone;   //用户的手机号码
    private String praise;    //评论
    private String star;    //评价等级
    private Date praiseDate;//评价时间
    private String sys_doctor_id;//医生主键
    private String symptom;//症状
    private String impression;//印象
    private String zan;//赞
    private String reason;//原因
    private String visit_endTime;//最晚就诊时间（界面显示）
    private Integer wait_time;//wait_time = visit_endTime - begin_time(sys_register_service中);
    private String location_id;//当前评价的就诊地址

    public PraiseVo() {
        super();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    public Integer getWait_time() {
        return wait_time;
    }

    public void setWait_time(Integer wait_time) {
        this.wait_time = wait_time;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVisit_endTime() {
        return visit_endTime;
    }

    public void setVisit_endTime(String visit_endTime) {
        this.visit_endTime = visit_endTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getPatientRegisterServiceId() {
        return patientRegisterServiceId;
    }

    public void setPatientRegisterServiceId(String patientRegisterServiceId) {
        this.patientRegisterServiceId = patientRegisterServiceId;
    }

    public String getSys_doctor_id() {
        return sys_doctor_id;
    }

    public void setSys_doctor_id(String sys_doctor_id) {
        this.sys_doctor_id = sys_doctor_id;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public Date getPraiseDate() {
        return praiseDate;
    }

    public void setPraiseDate(Date praiseDate) {
        this.praiseDate = praiseDate;
    }
}


