/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cxqm.xiaoerke.modules.consult.entity;
/**
 * <p>
 * Title: CallEstablish
 * </p>
 * <p>
 * Description: 鉴权响应实体
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: hisunsray
 * </p>
 * <p>
 * Date: 2013-07-09
 * </p>
 * 
 * @version 1.0
 */
public class CallEstablish {
	private String type;
	private String orderId;
	private String subId;
	private String caller;
	private String called;
	private String callSid;
	public String getCallSid() {
		return callSid;
	}
	public void setCallSid(String callSid) {
		this.callSid = callSid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getCalled() {
		return called;
	}
	public void setCalled(String called) {
		this.called = called;
	}
	
	@Override
	public String toString(){
		return "CallEstablish [type = "+type+", orderId = "+orderId+", subId = "+subId+", caller = "+caller+", called = "+called+", callSid = "+callSid+"]";
	}
}
