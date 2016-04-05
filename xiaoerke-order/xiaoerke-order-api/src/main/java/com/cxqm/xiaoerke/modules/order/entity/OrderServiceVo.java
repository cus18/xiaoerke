package com.cxqm.xiaoerke.modules.order.entity;

import java.util.Date;

/**
 * Created by cxq on 2016/3/29.
 * 订单-用于订单列表页（包含预约就诊和电话咨询订单）
 */
public class OrderServiceVo implements Comparable<OrderServiceVo>{

    private String registerNo;//订单号

    private String doctorName; //医生姓名
    private String position; //医生职务
    private String hospitalName; //医院名称

    private Date date; //就诊或咨询日期

    private Date beginTime; //就诊或咨询开始时间
    private Date endTime; //就诊或咨询结束时间

    private Date createTime; //下单时间

    private String status; //状态
    private String classify; //类别：预约就诊或电话咨询

    private String doctorId;//医生id
    private String orderId;//就诊或咨询 订单id
    private String illnessDescribeId;

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIllnessDescribeId() {
        return illnessDescribeId;
    }

    public void setIllnessDescribeId(String illnessDescribeId) {
        this.illnessDescribeId = illnessDescribeId;
    }

    //按订单服务时间倒序
    @Override
    public int compareTo(OrderServiceVo o) {
        boolean flag = false;

        flag = this.getDate().getTime() + this.getBeginTime().getTime() -
                o.getDate().getTime()+o.getBeginTime().getTime() > 0;
        if(flag){
            return -1;
        }
        return 1;
    }
}
