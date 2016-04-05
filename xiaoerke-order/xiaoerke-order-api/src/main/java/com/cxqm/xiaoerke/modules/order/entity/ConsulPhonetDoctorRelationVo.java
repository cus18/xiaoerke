package com.cxqm.xiaoerke.modules.order.entity;

import java.util.Date;

public class ConsulPhonetDoctorRelationVo {
    private Integer id;

    private String state;

    private String doctorId;

    private Date createTime;

    private Date updateTime;

    private Float price;

    private int serverLength;

    private String doctorAnswerPhone;

    public String getDoctorAnswerPhone() {
        return doctorAnswerPhone;
    }

    public void setDoctorAnswerPhone(String doctorAnswerPhone) {
        this.doctorAnswerPhone = doctorAnswerPhone;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getServerLength() {
        return serverLength;
    }

    public void setServerLength(int serverLength) {
        this.serverLength = serverLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
}