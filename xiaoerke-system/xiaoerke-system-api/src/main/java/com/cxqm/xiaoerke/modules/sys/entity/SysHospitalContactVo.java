package com.cxqm.xiaoerke.modules.sys.entity;

public class SysHospitalContactVo {
    private Integer id;

    private String contactName;

    private String contactPhone;

    private String sysHospitalId;

    private String sysHospitalName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getSysHospitalId() {
        return sysHospitalId;
    }

    public void setSysHospitalId(String sysHospitalId) {
        this.sysHospitalId = sysHospitalId;
    }

    public String getSysHospitalName() {
        return sysHospitalName;
    }

    public void setSysHospitalName(String sysHospitalName) {
        this.sysHospitalName = sysHospitalName;
    }
}