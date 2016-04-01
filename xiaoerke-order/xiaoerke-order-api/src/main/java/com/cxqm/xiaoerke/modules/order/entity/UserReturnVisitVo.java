/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.entity;

import java.util.Date;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

/**
 * 工作流Entity
 *
 * @author sunxiao
 * @version 2015-09-17
 */
public class UserReturnVisitVo extends BaseEntity<UserReturnVisitVo> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String patientRegisterId;
    private String waitTime;
    private String overallSatisfy;
    private String userFeedback;
    private String userFeedbackRemarks;
    private String isUser;
    private String falseUserReason;
    private String falseUserReasonRemarks;
    private String cancelReason;
    private String remarks;
    private Date createDate;
    private Date updateDate;

    public UserReturnVisitVo() {
        super();
        this.overallSatisfy = "0";
        this.userFeedback = "0";
        this.isUser = "0";
        this.falseUserReason = "0";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getPatientRegisterId() {
        return patientRegisterId;
    }

    public void setPatientRegisterId(String patientRegisterId) {
        this.patientRegisterId = patientRegisterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getOverallSatisfy() {
        return overallSatisfy;
    }

    public void setOverallSatisfy(String overallSatisfy) {
        this.overallSatisfy = overallSatisfy;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getUserFeedbackRemarks() {
        return userFeedbackRemarks;
    }

    public void setUserFeedbackRemarks(String userFeedbackRemarks) {
        this.userFeedbackRemarks = userFeedbackRemarks;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    public String getFalseUserReason() {
        return falseUserReason;
    }

    public void setFalseUserReason(String falseUserReason) {
        this.falseUserReason = falseUserReason;
    }

    public String getFalseUserReasonRemarks() {
        return falseUserReasonRemarks;
    }

    public void setFalseUserReasonRemarks(String falseUserReasonRemarks) {
        this.falseUserReasonRemarks = falseUserReasonRemarks;
    }


    public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public void preInsert() {
        // TODO Auto-generated method stub

    }

    @Override
    public void preUpdate() {
        // TODO Auto-generated method stub
    }

}


