package com.cxqm.xiaoerke.modules.vaccine.entity;

import java.util.Date;

public class VaccineBabyRecordVo {
    private Integer id;

    private String sysUserId;

    private String babySeedNumber;

    private String babyName;

    private Integer vaccineInfoId;

    private String vaccineInfoName;

    private String createBy;

    private Date createTime;

    private String updateTime;

    private String bak1;

    private String bak2;

    private String bak3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getBabySeedNumber() {
        return babySeedNumber;
    }

    public void setBabySeedNumber(String babySeedNumber) {
        this.babySeedNumber = babySeedNumber;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public Integer getVaccineInfoId() {
        return vaccineInfoId;
    }

    public void setVaccineInfoId(Integer vaccineInfoId) {
        this.vaccineInfoId = vaccineInfoId;
    }

    public String getVaccineInfoName() {
        return vaccineInfoName;
    }

    public void setVaccineInfoName(String vaccineInfoName) {
        this.vaccineInfoName = vaccineInfoName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }
}