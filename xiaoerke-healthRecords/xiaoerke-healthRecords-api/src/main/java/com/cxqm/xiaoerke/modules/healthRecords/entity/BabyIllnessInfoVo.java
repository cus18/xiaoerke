package com.cxqm.xiaoerke.modules.healthRecords.entity;

import java.util.Date;

public class BabyIllnessInfoVo {
    private Integer id;

    private String babyinfoId;

    private String desc;

    private Date createTime;

    private String conversationId;

    private String status;

    private String caseimg;

    private String resultimg;

    private String otherimg;

    private String positionimg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBabyinfoId() {
        return babyinfoId;
    }

    public void setBabyinfoId(String babyinfoId) {
        this.babyinfoId = babyinfoId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaseimg() {
        return caseimg;
    }

    public void setCaseimg(String caseimg) {
        this.caseimg = caseimg;
    }

    public String getResultimg() {
        return resultimg;
    }

    public void setResultimg(String resultimg) {
        this.resultimg = resultimg;
    }

    public String getOtherimg() {
        return otherimg;
    }

    public void setOtherimg(String otherimg) {
        this.otherimg = otherimg;
    }

    public String getPositionimg() {
        return positionimg;
    }

    public void setPositionimg(String positionimg) {
        this.positionimg = positionimg;
    }
}