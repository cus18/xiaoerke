package com.cxqm.xiaoerke.modules.operation.entity;

import java.util.Date;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

public class StatisticsTitle extends BaseEntity<StatisticsTitle>{

	private  String id;
	private  Integer addOrder;
	private  Integer cancelOrder;
	private  Integer totalAddOrder;
	private	Integer thatDayOrder;
	private  Integer netOrder;
	private	Integer totalVictoryOrder;
	private	Date createDate;
	private	Integer victoryNums;
	private	Integer totalVictoryNums;
	private	Integer addReadNums;
	private	Integer totalReadNums;
	private	Integer addSecReadNums;
	private	Integer totalSecReadNums;
	private	Integer shareNums;
	private	Integer addUserNum;
	private	Integer cancelNum;
	private	Integer totalUserNum;
	private	Integer netUserNum;
	private	Integer addDoctorNum;
	private	Integer netDoctorNum;
	private	String updateDate;
	private String  createD;
	private Integer cookieTotal;
	private Integer truthOrder;
	private Integer payOrder;
	private Integer addZYQPbe;
	private Integer visiteNum;
	private Integer addVIP;
	private Integer diffVIP;
	private Integer netVIP;
	private Integer totalVIP;

	public Integer getAddZYQPbe() {
		return addZYQPbe;
	}
	public void setAddZYQPbe(Integer addZYQPbe) {
		this.addZYQPbe = addZYQPbe;
	}
	public Integer getTotalZYQPbe() {
		return totalZYQPbe;
	}
	public void setTotalZYQPbe(Integer totalZYQPbe) {
		this.totalZYQPbe = totalZYQPbe;
	}
	private Integer totalZYQPbe;
	
	public Integer getTruthOrder() {
		return truthOrder;
	}
	public void setTruthOrder(Integer truthOrder) {
		this.truthOrder = truthOrder;
	}
	public Integer getPayOrder() {
		return payOrder;
	}
	public void setPayOrder(Integer payOrder) {
		this.payOrder = payOrder;
	}
	public Integer getCookieTotal() {
		return cookieTotal;
	}
	public void setCookieTotal(Integer cookieTotal) {
		this.cookieTotal = cookieTotal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getAddOrder() {
		return addOrder;
	}
	public void setAddOrder(Integer addOrder) {
		this.addOrder = addOrder;
	}
	public Integer getCancelOrder() {
		return cancelOrder;
	}
	public void setCancelOrder(Integer cancelOrder) {
		this.cancelOrder = cancelOrder;
	}
	public Integer getTotalAddOrder() {
		return totalAddOrder;
	}
	public void setTotalAddOrder(Integer totalAddOrder) {
		this.totalAddOrder = totalAddOrder;
	}
	public Integer getThatDayOrder() {
		return thatDayOrder;
	}
	public void setThatDayOrder(Integer thatDayOrder) {
		this.thatDayOrder = thatDayOrder;
	}
	public Integer getTotalVictoryOrder() {
		return totalVictoryOrder;
	}
	public void setTotalVictoryOrder(Integer totalVictoryOrder) {
		this.totalVictoryOrder = totalVictoryOrder;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getVictoryNums() {
		return victoryNums;
	}
	public void setVictoryNums(Integer victoryNums) {
		this.victoryNums = victoryNums;
	}
	public Integer getTotalVictoryNums() {
		return totalVictoryNums;
	}
	public void setTotalVictoryNums(Integer totalVictoryNums) {
		this.totalVictoryNums = totalVictoryNums;
	}
	public Integer getAddReadNums() {
		return addReadNums;
	}
	public void setAddReadNums(Integer addReadNums) {
		this.addReadNums = addReadNums;
	}
	public Integer getTotalReadNums() {
		return totalReadNums;
	}
	public void setTotalReadNums(Integer totalReadNums) {
		this.totalReadNums = totalReadNums;
	}
	public Integer getAddSecReadNums() {
		return addSecReadNums;
	}
	public void setAddSecReadNums(Integer addSecReadNums) {
		this.addSecReadNums = addSecReadNums;
	}
	public Integer getTotalSecReadNums() {
		return totalSecReadNums;
	}
	public void setTotalSecReadNums(Integer totalSecReadNums) {
		this.totalSecReadNums = totalSecReadNums;
	}
	public Integer getShareNums() {
		return shareNums;
	}
	public void setShareNums(Integer shareNums) {
		this.shareNums = shareNums;
	}
	public Integer getAddUserNum() {
		return addUserNum;
	}
	public void setAddUserNum(Integer addUserNum) {
		this.addUserNum = addUserNum;
	}
	public Integer getCancelNum() {
		return cancelNum;
	}
	public void setCancelNum(Integer cancelNum) {
		this.cancelNum = cancelNum;
	}
	public Integer getTotalUserNum() {
		return totalUserNum;
	}
	public void setTotalUserNum(Integer totalUserNum) {
		this.totalUserNum = totalUserNum;
	}
	public Integer getNetUserNum() {
		return netUserNum;
	}
	public void setNetUserNum(Integer netUserNum) {
		this.netUserNum = netUserNum;
	}
	public Integer getAddDoctorNum() {
		return addDoctorNum;
	}
	public void setAddDoctorNum(Integer addDoctorNum) {
		this.addDoctorNum = addDoctorNum;
	}
	public Integer getNetDoctorNum() {
		return netDoctorNum;
	}
	public void setNetDoctorNum(Integer netDoctorNum) {
		this.netDoctorNum = netDoctorNum;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getNetOrder() {
		return netOrder;
	}
	public void setNetOrder(Integer netOrder) {
		this.netOrder = netOrder;
	}
	public String getCreateD() {
		return createD;
	}
	public void setCreateD(String createD) {
		this.createD = createD;
	}
	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

	public Integer getVisiteNum() {
		return visiteNum;
	}

	public void setVisiteNum(Integer visiteNum) {
		this.visiteNum = visiteNum;
	}
	public Integer getAddVIP() {
		return addVIP;
	}
	public void setAddVIP(Integer addVIP) {
		this.addVIP = addVIP;
	}
	public Integer getDiffVIP() {
		return diffVIP;
	}
	public void setDiffVIP(Integer diffVIP) {
		this.diffVIP = diffVIP;
	}
	public Integer getNetVIP() {
		return netVIP;
	}
	public void setNetVIP(Integer netVIP) {
		this.netVIP = netVIP;
	}
	public Integer getTotalVIP() {
		return totalVIP;
	}
	public void setTotalVIP(Integer totalVIP) {
		this.totalVIP = totalVIP;
	}
}
