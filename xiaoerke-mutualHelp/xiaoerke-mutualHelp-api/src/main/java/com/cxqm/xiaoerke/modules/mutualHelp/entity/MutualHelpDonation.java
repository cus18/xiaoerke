package com.cxqm.xiaoerke.modules.mutualHelp.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class MutualHelpDonation {
    private Integer id;

    private String openId;

    private String userId;

    private Integer money;

    private String leaveNote;

    private Date createTime;

    private Integer donationType;//预留，以后可能加入其它的捐款

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getLeaveNote() {
        return leaveNote;
    }

    public void setLeaveNote(String leaveNote) {
        this.leaveNote = leaveNote;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDonationType() {
        return donationType;
    }

    public void setDonationType(Integer donationType) {
        this.donationType = donationType;
    }
}
