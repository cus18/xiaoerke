package com.cxqm.xiaoerke.modules.marketing.entity;

import java.util.Date;

public class MarketingActivities {

	private Integer id;
	private String openid;
	private Date createDate;
	private String result;
	private String ifShare;
	private String score;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getIfShare() {
		return ifShare;
	}
	public void setIfShare(String ifShare) {
		this.ifShare = ifShare;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
}
