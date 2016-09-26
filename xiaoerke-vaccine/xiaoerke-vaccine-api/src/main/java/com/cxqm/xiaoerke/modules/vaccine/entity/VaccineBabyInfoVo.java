package com.cxqm.xiaoerke.modules.vaccine.entity;
import java.util.Date;

public class VaccineBabyInfoVo {
    private Integer id;

    private String sysUserId;

    private String babyName;

    private String babySex;

    private Date birthday;

    private String babySeedNumber;

    private Integer vaccineStationId;

    private String vaccineStationName;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String bak1;

    private String bak2;

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

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabySex() {
        return babySex;
    }

    public void setBabySex(String babySex) {
        this.babySex = babySex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBabySeedNumber() {
        return babySeedNumber;
    }

    public void setBabySeedNumber(String babySeedNumber) {
        this.babySeedNumber = babySeedNumber;
    }

    public Integer getVaccineStationId() {
        return vaccineStationId;
    }

    public void setVaccineStationId(Integer vaccineStationId) {
        this.vaccineStationId = vaccineStationId;
    }

    public String getVaccineStationName() {
        return vaccineStationName;
    }

    public void setVaccineStationName(String vaccineStationName) {
        this.vaccineStationName = vaccineStationName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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

}