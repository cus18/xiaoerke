package com.cxqm.xiaoerke.modules.entity;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class PatientRegisterServiceVo{

    private String payWay;
    private Integer allowance;
    private static final long serialVersionUID = 1L;
    private String id;        // 用户订购加号的主键ID
    private String sysRegisterServiceId;    // 关联的register_service的ID
    private String patientRegisterServiceId;
    private String sysPatientId;    // 关联的patient表ID
    private String status;    // 加号业务的用户订购状态
    private Date createDate;  //创建日期
    private Date updateDate;  //修改日期
    private String registerNo;  //加号订购编号
    private String phone;  //用户手机号
    private String babyName;  //用户加号此业务的孩子名称
    private String illness; //疾病相关描述
    private Date birthday; //孩子的生日
    private String praise;  //用户对于此次加号看病的评价
    private String star; //用户对于此次加号看病的打分
    private String sys_user_id;//用户主键
    private Date date;//医生加号表中的时间
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
    private String showTime;
    private String order;
    private String showCreateDate;
    private String nickName;
    private String marketer;
    private String falseUserReason;
    private String doctorId;
    private String hospitalName;//医院姓名
    private String warnFlag;
    private String doctorName;//医生姓名
    private String departmentLevel1;//医生所在的科室名称

    private String OrderCreateDate;  //下单日期日期
    private String locationId;

    private String location;

    protected Page page;

    private String showUpdateDate;
    private String visitDate;//就诊日期

    private String OrderCreateDateFrom;
    private String OrderCreateDateTo;
    private String visitDateFrom;
    private String visitDateTo;
    private String doctorPhone;//医生手机号

    private String hospitalId;
    private String falseUserReasonRemarks;
    private String position;


    private String openId;
    private String serviceType;
    private String overallSatisfy;
    private String userFeedBack;
    private String userFeedBackRemarks;
    private String isUser;
    private String cancelReason;
    private String remarks;
    private String waitTime;
    private String payStatus;
    private Integer memberServiceId;
    private String deleteBy;


    private String relationType;//订单所属医生与医院的关系

    @ExcelField(title="订单类型", align=2, sort=21)
    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public String getPatientRegisterServiceId() {
        return patientRegisterServiceId;
    }

    public void setPatientRegisterServiceId(String patientRegisterServiceId) {
        this.patientRegisterServiceId = patientRegisterServiceId;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Integer getMemberServiceId() {
        return memberServiceId;
    }

    public void setMemberServiceId(Integer memberServiceId) {
        this.memberServiceId = memberServiceId;
    }

    @ExcelField(title="来源", align=2, sort=0)
    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }

    @ExcelField(title="成功（候诊时长）", align=2, sort=5)
    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    @ExcelField(title="openID", align=2, sort=2)
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @ExcelField(title="出诊类别 专家门诊/特需门诊", align=2, sort=9)
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @ExcelField(title="整体满意度 是/否", align=2, sort=11)
    public String getOverallSatisfy() {
        return overallSatisfy;
    }

    public void setOverallSatisfy(String overallSatisfy) {
        this.overallSatisfy = overallSatisfy;
    }

    @ExcelField(title="用户反馈（满意的地方）", align=2, sort=12)
    public String getUserFeedBack() {
        return userFeedBack;
    }

    public void setUserFeedBack(String userFeedBack) {
        this.userFeedBack = userFeedBack;
    }

    @ExcelField(title="用户反馈（不满意和改进建议）", align=2, sort=13)
    public String getUserFeedBackRemarks() {
        return userFeedBackRemarks;
    }

    public void setUserFeedBackRemarks(String userFeedBackRemarks) {
        this.userFeedBackRemarks = userFeedBackRemarks;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    @ExcelField(title="取消（原因及方式）", align=2, sort=16)
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @ExcelField(title="备注", align=2, sort=18)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFalseUserReasonRemarks() {
        return falseUserReasonRemarks;
    }

    public void setFalseUserReasonRemarks(String falseUserReasonRemarks) {
        this.falseUserReasonRemarks = falseUserReasonRemarks;
    }

    public String getOrderCreateDateFrom() {
        return OrderCreateDateFrom;
    }

    public void setOrderCreateDateFrom(String orderCreateDateFrom) {
        OrderCreateDateFrom = orderCreateDateFrom;
    }

    public String getOrderCreateDateTo() {
        return OrderCreateDateTo;
    }

    public void setOrderCreateDateTo(String orderCreateDateTo) {
        OrderCreateDateTo = orderCreateDateTo;
    }

    public String getVisitDateFrom() {
        return visitDateFrom;
    }

    public void setVisitDateFrom(String visitDateFrom) {
        this.visitDateFrom = visitDateFrom;
    }

    public String getVisitDateTo() {
        return visitDateTo;
    }

    public void setVisitDateTo(String visitDateTo) {
        this.visitDateTo = visitDateTo;
    }

    @ExcelField(title="微信用户名", align=2, sort=1)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @ExcelField(title="用户真假", align=1, sort=14)
    public String getFalseUserReason() {
        return falseUserReason;
    }

    public void setFalseUserReason(String falseUserReason) {
        this.falseUserReason = falseUserReason;
    }

    @ExcelField(title="最后操作时间", align=1, sort=17)
    public String getShowUpdateDate() {
        return showUpdateDate;
    }

    public void setShowUpdateDate(String showUpdateDate) {
        this.showUpdateDate = showUpdateDate;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getOrderCreateDate() {
        return OrderCreateDate;
    }

    public void setOrderCreateDate(String orderCreateDate) {
        OrderCreateDate = orderCreateDate;
    }

    @ExcelField(title="科室", align=1, sort=8)
    public String getDepartmentLevel1() {
        return departmentLevel1;
    }

    public void setDepartmentLevel1(String departmentLevel1) {
        this.departmentLevel1 = departmentLevel1;
    }

    @ExcelField(title="预约医生", align=1, sort=6)
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @ExcelField(title="预约日期", align=1, sort=9)
    public String getShowCreateDate() {
        return showCreateDate;
    }

    public void setShowCreateDate(String showCreateDate) {
        this.showCreateDate = showCreateDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @ExcelField(title="就诊时间", align=2, sort=10)
    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getWarnFlag() {
        return warnFlag;
    }

    public void setWarnFlag(String warnFlag) {
        this.warnFlag = warnFlag;
    }



    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSysRegisterServiceId() {
        return sysRegisterServiceId;
    }

    public void setSysRegisterServiceId(String sysRegisterServiceId) {
        this.sysRegisterServiceId = sysRegisterServiceId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getSys_user_id() {
        return sys_user_id;
    }

    public void setSys_user_id(String sys_user_id) {
        this.sys_user_id = sys_user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    @ExcelField(title="订单状态", align=2, sort=7)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @ExcelField(title="预约序号", align=2, sort=2)
    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    @ExcelField(title="联系电话", align=2, sort=4)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title="宝宝姓名", align=2, sort=3)
    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    @ExcelField(title="症状", align=1, sort=15)
    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @ExcelField(title="评价", align=1, sort=19)
    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @ExcelField(title="医院", align=1, sort=7)
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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @JsonIgnore
    @XmlTransient
    public Page getPage() {
        if (page == null){
            page = new Page();
        }
        return page;
    }

    public Page setPage(Page page) {
        this.page = page;
        return page;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBeginTimeStr() {
        return beginTime == null ? "" : DateUtils.DateToStr(beginTime, "time");
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Integer getAllowance() {
        return allowance;
    }

    public void setAllowance(Integer allowance) {
        this.allowance = allowance;
    }

    @ExcelField(title="支付状态", align=1, sort=20)
    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

}


