package com.cxqm.xiaoerke.modules.sys.entity;

/**
 * 开关配置Entity
 * @author sunxiao
 * @version 2016-06-13
 */
public class SwitchConfigure {

	private int id;
	private String type;	// 类型
	private String flag;// 描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}