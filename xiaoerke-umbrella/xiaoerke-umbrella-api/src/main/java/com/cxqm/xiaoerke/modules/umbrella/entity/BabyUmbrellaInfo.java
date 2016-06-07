package com.cxqm.xiaoerke.modules.umbrella.entity;

import java.util.Date;

/**
 * Created by feibendechayedan on 16/5/20.
 */
public class BabyUmbrellaInfo {

    private Integer id;
    private String openid;
    private String money;
    private Integer umbrellaMoney;
    private Date createTime;
    private String babyId;
    private String parentIdCard;
    private String parentPhone;
    private String parentName;
    private Integer parentType;
    private String truePayMoneys;
    private String payResult;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTruePayMoneys() {
        return truePayMoneys;
    }

    public void setTruePayMoneys(String truePayMoneys) {
        this.truePayMoneys = truePayMoneys;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    private Date activationTime;

    public Integer getUmbrellaMoney() {
        return umbrellaMoney;
    }

    public void setUmbrellaMoney(Integer umbrellaMoney) {
        this.umbrellaMoney = umbrellaMoney;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getUmberllaMoney() {
        return umbrellaMoney;
    }

    public void setUmberllaMoney(Integer umbrellaMoney) {
        this.umbrellaMoney = umbrellaMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getParentIdCard() {
        return parentIdCard;
    }

    public void setParentIdCard(String parentIdCard) {
        this.parentIdCard = parentIdCard;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
