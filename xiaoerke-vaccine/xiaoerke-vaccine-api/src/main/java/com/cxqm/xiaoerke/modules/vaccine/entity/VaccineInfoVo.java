package com.cxqm.xiaoerke.modules.vaccine.entity;

import java.util.Date;

public class VaccineInfoVo {
    private Integer id;

    private String name;

    private String number;

    private String qrCode;

    private String QRCodeUrl;

    private Integer miniumAge;

    private Integer lastTimeVaccine;

    private Integer lastTimeInterval;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String bak1;

    private String bak2;

    private String bak3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getMiniumAge() {
        return miniumAge;
    }

    public void setMiniumAge(Integer miniumAge) {
        this.miniumAge = miniumAge;
    }

    public Integer getLastTimeVaccine() {
        return lastTimeVaccine;
    }

    public void setLastTimeVaccine(Integer lastTimeVaccine) {
        this.lastTimeVaccine = lastTimeVaccine;
    }

    public Integer getLastTimeInterval() {
        return lastTimeInterval;
    }

    public void setLastTimeInterval(Integer lastTimeInterval) {
        this.lastTimeInterval = lastTimeInterval;
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

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getQRCodeUrl() {
        return QRCodeUrl;
    }

    public void setQRCodeUrl(String QRCodeUrl) {
        this.QRCodeUrl = QRCodeUrl;
    }
}