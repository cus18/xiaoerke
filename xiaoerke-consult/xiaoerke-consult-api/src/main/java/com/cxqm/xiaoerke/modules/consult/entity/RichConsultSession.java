package com.cxqm.xiaoerke.modules.consult.entity;

public class RichConsultSession extends ConsultSession {

	private static final long serialVersionUID = 1L;

	private String csUserName;
	
	private String userName;
	
	private String serverAddress;
	
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
