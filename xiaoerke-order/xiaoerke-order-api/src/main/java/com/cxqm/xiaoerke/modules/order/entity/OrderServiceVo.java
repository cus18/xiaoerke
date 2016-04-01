package com.cxqm.xiaoerke.modules.order.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by cxq on 2016/3/29.
 * 用于当前订单页（包含预约就诊和电话咨询订单）
 */
public class OrderServiceVo implements Comparable<OrderServiceVo>{
    private String doctorName; //医生姓名
    private String position; //医生职务
    private String hospital; //医院名称
    private String date; //就诊或咨询日期
    private String beginTime; //就诊或咨询开始时间
    private String endTime; //就诊或咨询结束时间
    private String status; //状态
    private String classify; //类别：预约就诊或电话咨询

    private String doctorId;
    private String orderId;

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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    @Override
    public int compareTo(OrderServiceVo o) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String dateString = this.getDate() + " " + this.getBeginTime();
        String dateString_o = o.getDate() + " " + o.getBeginTime();

        boolean flag = false;
        try {
            flag = format.parse(dateString).getTime() - format.parse(dateString_o).getTime() > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(flag){
            return -1;
        }
        return 1;
    }
}
