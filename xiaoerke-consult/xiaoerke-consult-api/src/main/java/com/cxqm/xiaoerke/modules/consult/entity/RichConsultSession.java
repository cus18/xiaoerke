package com.cxqm.xiaoerke.modules.consult.entity;

public class RichConsultSession extends ConsultSession {

	private static final long serialVersionUID = 1L;

	private String csUserName;
	
	private String userName;
	
	private String serverAddress;

	private String payStatus = "";

	private String userType;

	//jiangzzg add 2016-9-8 17:26:03 ，当前会话数
	private Integer consultNum ;

	public Integer getConsultNum() {
		return consultNum;
	}

	public void setConsultNum(Integer consultNum) {
		this.consultNum = consultNum;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public RichConsultSession(){
		this.setStatus(ConsultSession.STATUS_ONGOING);
	}

	public String getCsUserName() {
		return csUserName;
	}

	public void setCsUserName(String csUserName) {
		this.csUserName = csUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
}
