package com.cxqm.xiaoerke.modules.sys.entity;

public class DoctorLocationRef {
    private String id;

    private String sysDoctorId;

    private String doctorname;

    private String sysHospitalId;

    private String hospitalname;

    private String location;

    private String shotVisitTime;

    private String waittime;

    private String route;

    private Float price;

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

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getSysHospitalId() {
        return sysHospitalId;
    }

    public void setSysHospitalId(String sysHospitalId) {
        this.sysHospitalId = sysHospitalId;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShotVisitTime() {
        return shotVisitTime;
    }

    public void setShotVisitTime(String shotVisitTime) {
        this.shotVisitTime = shotVisitTime;
    }

    public String getWaittime() {
        return waittime;
    }

    public void setWaittime(String waittime) {
        this.waittime = waittime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}