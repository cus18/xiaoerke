package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

public class ConsultSessionForwardRecordsVo {
	
	public static final String REACT_TRANSFER_OPERATION_ACCEPT = "accept";
	
	public static final String REACT_TRANSFER_OPERATION_REJECT = "reject";
	
	public static final String REACT_TRANSFER_OPERATION_CANCEL = "cancel";
	
	public static final String REACT_TRANSFER_STATUS_ACCEPT = "accepted";
	
	public static final String REACT_TRANSFER_STATUS_REJECT = "rejected";
	
	public static final String REACT_TRANSFER_STATUS_WAITING = "waiting";
	
	public static final String REACT_TRANSFER_STATUS_CANCELLED = "cancelled";
	
    private Long id;

    private Long conversationId;

    private String fromUserId;

    private String toUserId;

    private Date createTime;

    private String createBy;

    private String remark;
    
    private String status;

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}