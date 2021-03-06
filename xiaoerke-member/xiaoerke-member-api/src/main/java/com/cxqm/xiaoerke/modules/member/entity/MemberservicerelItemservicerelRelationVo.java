package com.cxqm.xiaoerke.modules.member.entity;

import java.util.Date;


public class MemberservicerelItemservicerelRelationVo {
    private Integer id;

    private Integer memberServiceRelationId;

    private Integer memberItemserviceRelationId;

    private Integer leftTimes;

    private Date createDate;

    private Date updateDate;

    private String createBy;

    private Date activateDate;

    private Date endDate;

    private String sysUserId;

    private String name;

    private String qrCode;

    private String type;

    private String source;

    private Integer times;//服务次数

    private Integer period;//服务周期

    private String periodUnit;//服务周期单位

    private String openid;
    
    private String nickName;
    
    private String phone;
    
    private long serviceValidityPeriod;//服务有效期，会员列表显示
    
    private long reservationPeriod;//预约有效期，会员列表显示

    private String activateDateStr;//购买时间，会员列表显示

    private String status;//服务状态

    private String remark;//描述

    private String isPay;//是否收费
    
    public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
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

    public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getServiceValidityPeriod() {
		return serviceValidityPeriod;
	}

	public void setServiceValidityPeriod(long serviceValidityPeriod) {
		this.serviceValidityPeriod = serviceValidityPeriod;
	}

	public long getReservationPeriod() {
		return reservationPeriod;
	}

	public void setReservationPeriod(long reservationPeriod) {
		this.reservationPeriod = reservationPeriod;
	}

	public String getActivateDateStr() {
		return activateDateStr;
	}

	public void setActivateDateStr(String activateDateStr) {
		this.activateDateStr = activateDateStr;
	}

	public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getId() {
        return id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberServiceRelationId() {
        return memberServiceRelationId;
    }

    public void setMemberServiceRelationId(Integer memberServiceRelationId) {
        this.memberServiceRelationId = memberServiceRelationId;
    }

    public Integer getMemberItemserviceRelationId() {
        return memberItemserviceRelationId;
    }

    public void setMemberItemserviceRelationId(Integer memberItemserviceRelationId) {
        this.memberItemserviceRelationId = memberItemserviceRelationId;
    }

    public Integer getLeftTimes() {
        return leftTimes;
    }

    public void setLeftTimes(Integer leftTimes) {
        this.leftTimes = leftTimes;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(Date activateDate) {
        this.activateDate = activateDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}