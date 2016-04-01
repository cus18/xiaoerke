/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.entity;

/**
 * OrderPropertyVo Entity
 *
 * @author deliang
 * @version 2015-12-16
 */
public class OrderPropertyVo {

    private String id;

    private String patientRegisterServiceId;

    private String yellowCattle;

    private String firstOrder;

    private String scanCode;

    private String charge;

    private String createDate;

    private String updateTime;

    private String openid;

    private String orderSource;

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientRegisterServiceId() {
        return patientRegisterServiceId;
    }

    public void setPatientRegisterServiceId(String patientRegisterServiceId) {
        this.patientRegisterServiceId = patientRegisterServiceId;
    }

    public String getYellowCattle() {
        return yellowCattle;
    }

    public void setYellowCattle(String yellowCattle) {
        this.yellowCattle = yellowCattle;
    }

    public String getFirstOrder() {
        return firstOrder;
    }

    public void setFirstOrder(String firstOrder) {
        this.firstOrder = firstOrder;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}


