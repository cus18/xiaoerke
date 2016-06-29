package com.cxqm.xiaoerke.modules.order.entity;

import com.cxqm.xiaoerke.common.persistence.DataEntity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class RegisterServiceVo extends DataEntity<RegisterServiceVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 医生注册加号业务的主键ID
    private String sysDoctorId;    // doctor表ID
    private String sysPatientId;    // patient表ID
    private String sysHospitalId;    // hospital表ID
    private Date date;  //医生出诊日期
    private Float price;  //医生出诊服务价格
    private Date beginTime;  //开始时间
	private Date endTime;  //结束时间
	private String location;  //医生出诊的医院具体位置
    private String desc; //业务相关描述
    private String status; //加号业务的状态
    private float deposit;  //押金数额

	private String serviceType; //业务类型
    private String doctorName;//加号管理显示用
    private String phone;//加号管理显示用
    private String hospitalName;//加号管理显示用
    private String locationId;  //医生出诊的医院具体位置id
    private String department;  //医生所在科室
    private String warnFlag;
    private String fromDate;
    private String toDate;
    private String endDate;
    private String startDate;
    private String priceRange;
    private String repeatFlag;//0是每周重复，1是隔周重复，2是不重复
    private String relationType;
    private String channel;
    
    public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public String getFromDate() {
		return fromDate;
	}

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

    

	public String getWarnFlag() {
    	return warnFlag;
    }
    
    public void setWarnFlag(String warnFlag) {
    	this.warnFlag = warnFlag;
    }
    
    public String getDepartment() {
    	return department;
    }
    
    public void setDepartment(String department) {
    	this.department = department;
    }
    public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSysDoctorId() {
        return sysDoctorId;
    }

    public void setSysDoctorId(String sysDoctorId) {
        this.sysDoctorId = sysDoctorId;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    public String getSysHospitalId() {
        return sysHospitalId;
    }

    public void setSysHospitalId(String sysHospitalId) {
        this.sysHospitalId = sysHospitalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}


