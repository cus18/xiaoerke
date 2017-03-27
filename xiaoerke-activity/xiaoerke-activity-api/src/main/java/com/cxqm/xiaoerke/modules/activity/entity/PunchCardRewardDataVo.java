package com.cxqm.xiaoerke.modules.activity.entity;

import java.util.Date;

/**
 * ¼ÙÊý¾Ý
 */
public class PunchCardRewardDataVo {
    private String id;

    private String openId;

    private String nickName;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    private Float cashAmount;

    private Integer dayNum;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Float cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }
}