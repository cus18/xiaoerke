package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.List;


public class MessageReqState {
  private String result; 
  private String name;
  private List<MessageReqInfo> messageInfo;
  
  public String getResult() {
	return result;
  }
  public void setResult(String result) {
	this.result = result;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public List<MessageReqInfo> getMessageInfo() {
	return messageInfo;
  }
  public void setMessageInfo(List<MessageReqInfo> messageInfo) {
	this.messageInfo = messageInfo;
  }
  
}
