package com.cxqm.xiaoerke.modules.activity.entity;

import java.util.Date;

public class OlyBabyGameDetailVo {
    private Integer id;

    private String openId;

    private Integer gameLevel;

    private Float gameScore;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(Integer gameLevel) {
        this.gameLevel = gameLevel;
    }

    public Float getGameScore() {
        return gameScore;
    }

    public void setGameScore(Float gameScore) {
        this.gameScore = gameScore;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}