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

    private String willVaccineName;

    private String nextVaccineMiniumAge;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    public String getNextVaccineMiniumAge() {
        return nextVaccineMiniumAge;
    }

    public void setNextVaccineMiniumAge(String nextVaccineMiniumAge) {
        this.nextVaccineMiniumAge = nextVaccineMiniumAge;
    }

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

    public String getWillVaccineName() {
        return willVaccineName;
    }

    public void setWillVaccineName(String willVaccineName) {
        this.willVaccineName = willVaccineName;
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

}