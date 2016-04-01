package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.Date;

public class CustomerLog {

	private Integer id;
	private String customerID;
	private Date createDate;
	private String sections;
	private String illnessID;
	private String babyInfoID;
	private String remark;
	private String show;
	private String result;
	private String openid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getIllnessID() {
		return illnessID;
	}
	public void setIllnessID(String illnessID) {
		this.illnessID = illnessID;
	}
	public String getBabyInfoID() {
		return babyInfoID;
	}
	public void setBabyInfoID(String babyInfoID) {
		this.babyInfoID = babyInfoID;
	}
	public String getSections() {
		return sections;
	}
	public void setSections(String sections) {
		this.sections = sections;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
