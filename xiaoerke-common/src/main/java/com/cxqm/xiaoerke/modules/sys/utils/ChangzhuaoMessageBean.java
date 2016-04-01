package com.cxqm.xiaoerke.modules.sys.utils;

public class ChangzhuaoMessageBean {
	//  返回状态值：成功返回Success 失败返回：Faild
	private String returnstatus;
	//	返回信息
	private String message;
	//	返回余额
	private String remainpoint;
	//	返回本次任务的序列ID
	private String taskID;
	//	成功短信数
	private String successCounts;
	//	支付方式 ：后付费，预付费
	private String payinfo;
	//	余额
	private String overage;
	//	总充值点数
	private String sendTotal;


	public String getReturnstatus() {
		return returnstatus;
	}
	public void setReturnstatus(String returnstatus) {
		this.returnstatus = returnstatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRemainpoint() {
		return remainpoint;
	}
	public void setRemainpoint(String remainpoint) {
		this.remainpoint = remainpoint;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getSuccessCounts() {
		return successCounts;
	}
	public void setSuccessCounts(String successCounts) {
		this.successCounts = successCounts;
	}
	public String getPayinfo() {
		return payinfo;
	}
	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
	}
	public String getOverage() {
		return overage;
	}
	public void setOverage(String overage) {
		this.overage = overage;
	}
	public String getSendTotal() {
		return sendTotal;
	}
	public void setSendTotal(String sendTotal) {
		this.sendTotal = sendTotal;
	}

	
}
