package com.cxqm.xiaoerke.modules.vaccine.entity;

import java.util.Date;

public class VaccineStationRelVo {
    private Integer id;

    private Integer vaccineId;

    private Integer vaccineStationId;

    private Integer nextVaccineId;

    private String vaccineName;

    private Integer miniumAge;

    private Integer lastTimeInterval;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String bak1;

    private String bak2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Integer vaccineId) {
        this.vaccineId = vaccineId;
    }

    public Integer getVaccineStationId() {
        return vaccineStationId;
    }

    public void setVaccineStationId(Integer vaccineStationId) {
        this.vaccineStationId = vaccineStationId;
    }

    public Integer getNextVaccineId() {
        return nextVaccineId;
    }

    public void setNextVaccineId(Integer nextVaccineId) {
        this.nextVaccineId = nextVaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Integer getMiniumAge() {
        return miniumAge;
    }

    public void setMiniumAge(Integer miniumAge) {
        this.miniumAge = miniumAge;
    }

    public Integer getLastTimeInterval() {
        return lastTimeInterval;
    }

    public void setLastTimeInterval(Integer lastTimeInterval) {
        this.lastTimeInterval = lastTimeInterval;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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