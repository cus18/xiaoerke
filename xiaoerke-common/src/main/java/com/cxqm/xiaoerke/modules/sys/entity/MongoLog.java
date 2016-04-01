package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.Date;

public class MongoLog {
	private String id;
	private String type; 		// 日志类型（1：接入日志；2：错误日志）
	private String title;		// 日志标题
	private String create_by;	// 创建者
	private Date create_date;	// 创建日期
	private String remote_addr; 	// 操作用户的IP地址
	private String request_uri; 	// 操作的URI
	private String method; 		// 操作的方式
	private String params; 		// 操作提交的数据
	private String user_agent;	// 操作用户代理信息
	private String exception; 	// 异常信息
	private String open_id; //用户的微信公众号ID

	public String marketer;//渠道
	public String status;//0关注 1取消关注
	public String nickname;//昵称
	public String isPay;//用户是否第一次关注
	public String parameters;//参数
	public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMarketer() {
		return marketer;
	}

	public void setMarketer(String marketer) {
		this.marketer = marketer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getRemote_addr() {
		return remote_addr;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public String getRequest_uri() {
		return request_uri;
	}

	public void setRequest_uri(String request_uri) {
		this.request_uri = request_uri;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
}
