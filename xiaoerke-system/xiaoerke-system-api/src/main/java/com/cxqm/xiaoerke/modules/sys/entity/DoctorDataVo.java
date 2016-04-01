package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.Date;

/**
 * Created by Administrator on 2015/6/16.
 */
public class DoctorDataVo extends DoctorVo {
    private Date availableDate;
    private String doctorName;

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date available_date) {
        this.availableDate = available_date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

}
