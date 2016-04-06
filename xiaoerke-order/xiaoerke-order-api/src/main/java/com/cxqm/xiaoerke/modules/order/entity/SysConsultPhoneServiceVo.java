package com.cxqm.xiaoerke.modules.order.entity;

import java.util.Date;

public class SysConsultPhoneServiceVo {
    private Integer id;

    private String sysDoctorId;

    private String price;

    private Date date;

    private Date begintime;

    private Date endtime;

    private String state;//0未预约，1已预约

    private Date createdate;

    private Date updatedate;

    private String servicetype;

    private String repeatFlag;

    public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysDoctorId() {
        return sysDoctorId;
    }

    public void setSysDoctorId(String sysDoctorId) {
        this.sysDoctorId = sysDoctorId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }
}