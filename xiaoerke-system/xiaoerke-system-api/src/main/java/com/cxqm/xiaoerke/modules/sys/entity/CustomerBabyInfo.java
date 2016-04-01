package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.Date;
/**
 * 客服咨询用户登记表(多客服)
 * @author Cnto
 *
 */
public class CustomerBabyInfo {

	private String babyName;
	private Date babyBirthday;
	private String phone;
	private String sex;
	private Integer id;
	private String openid;
	private String nickname;
	private String remark;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBabyName() {
		return babyName;
	}
	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}
	public Date getBabyBirthday() {
		return babyBirthday;
	}
	public void setBabyBirthday(Date babyBirthday) {
		this.babyBirthday = babyBirthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
