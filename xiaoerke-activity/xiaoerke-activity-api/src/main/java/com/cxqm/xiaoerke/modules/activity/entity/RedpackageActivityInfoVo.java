package com.cxqm.xiaoerke.modules.activity.entity;

import java.util.Date;

public class RedpackageActivityInfoVo {
    private String id;

    private String openId;

    private Date createTime;

    private Integer cardRuyi;

    private Integer cardYoushan;

    private Integer cardHealth;

    private Integer cardHappy;

    private Integer cardLove;

    private Integer cardBig;

    private Integer totalInvitation;

    private Integer delState;

    private Date updateTime;

    private Integer market ;

    public Integer getMarket() {
        return market;
    }

    public void setMarket(Integer market) {
        this.market = market;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCardRuyi() {
        return cardRuyi;
    }

    public void setCardRuyi(Integer cardRuyi) {
        this.cardRuyi = cardRuyi;
    }

    public Integer getCardYoushan() {
        return cardYoushan;
    }

    public void setCardYoushan(Integer cardYoushan) {
        this.cardYoushan = cardYoushan;
    }

    public Integer getCardHealth() {
        return cardHealth;
    }

    public void setCardHealth(Integer cardHealth) {
        this.cardHealth = cardHealth;
    }

    public Integer getCardHappy() {
        return cardHappy;
    }

    public void setCardHappy(Integer cardHappy) {
        this.cardHappy = cardHappy;
    }

    public Integer getCardLove() {
        return cardLove;
    }

    public void setCardLove(Integer cardLove) {
        this.cardLove = cardLove;
    }

    public Integer getCardBig() {
        return cardBig;
    }

    public void setCardBig(Integer cardBig) {
        this.cardBig = cardBig;
    }

    public Integer getTotalInvitation() {
        return totalInvitation;
    }

    public void setTotalInvitation(Integer totalInvitation) {
        this.totalInvitation = totalInvitation;
    }

    public Integer getDelState() {
        return delState;
    }

    public void setDelState(Integer delState) {
        this.delState = delState;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}