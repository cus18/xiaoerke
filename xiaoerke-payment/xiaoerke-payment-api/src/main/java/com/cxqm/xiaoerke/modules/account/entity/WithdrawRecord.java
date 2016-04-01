package com.cxqm.xiaoerke.modules.account.entity;

import java.util.Date;

public class WithdrawRecord extends Record{

	private String accountBindingId;

	private String status;

	private Date createdDate;

	private String createdBy;

	private String operateType = "提现";

	private String partnerTradeNo;

	public String getAccountBindingId() {
		return accountBindingId;
	}

	public void setAccountBindingId(String accountBindingId) {
		this.accountBindingId = accountBindingId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
}