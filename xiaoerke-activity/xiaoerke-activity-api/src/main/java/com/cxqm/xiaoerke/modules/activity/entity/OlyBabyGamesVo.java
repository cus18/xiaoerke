package com.cxqm.xiaoerke.modules.activity.entity;

import java.util.Date;

public class OlyBabyGamesVo {
    private Integer id;

    private String openId;

    private Integer gameLevel;

    private Float gameScore;

    private Integer level1CurrentTimes;

    private Integer level2CurrentTimes;

    private Integer level3CurrentTimes;

    private Integer level4CurrentTimes;

    private Integer level5CurrentTimes;

    private Integer level6CurrentTimes;

    private String marketer;

    private Integer inviteFriendNumber;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String prize;

    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public String getPrize() {
        return prize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrize(String prize) {
        this.prize = prize;
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

    public Integer getLevel1CurrentTimes() {
        return level1CurrentTimes;
    }

    public void setLevel1CurrentTimes(Integer level1CurrentTimes) {
        this.level1CurrentTimes = level1CurrentTimes;
    }

    public Integer getLevel2CurrentTimes() {
        return level2CurrentTimes;
    }

    public void setLevel2CurrentTimes(Integer level2CurrentTimes) {
        this.level2CurrentTimes = level2CurrentTimes;
    }

    public Integer getLevel3CurrentTimes() {
        return level3CurrentTimes;
    }

    public void setLevel3CurrentTimes(Integer level3CurrentTimes) {
        this.level3CurrentTimes = level3CurrentTimes;
    }

    public Integer getLevel4CurrentTimes() {
        return level4CurrentTimes;
    }

    public void setLevel4CurrentTimes(Integer level4CurrentTimes) {
        this.level4CurrentTimes = level4CurrentTimes;
    }

    public Integer getLevel5CurrentTimes() {
        return level5CurrentTimes;
    }

    public void setLevel5CurrentTimes(Integer level5CurrentTimes) {
        this.level5CurrentTimes = level5CurrentTimes;
    }

    public Integer getLevel6CurrentTimes() {
        return level6CurrentTimes;
    }

    public void setLevel6CurrentTimes(Integer level6CurrentTimes) {
        this.level6CurrentTimes = level6CurrentTimes;
    }

    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }

    public Integer getInviteFriendNumber() {
        return inviteFriendNumber;
    }

    public void setInviteFriendNumber(Integer inviteFriendNumber) {
        this.inviteFriendNumber = inviteFriendNumber;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
}