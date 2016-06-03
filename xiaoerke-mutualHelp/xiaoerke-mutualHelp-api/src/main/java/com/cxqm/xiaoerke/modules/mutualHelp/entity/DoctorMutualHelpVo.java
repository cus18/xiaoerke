package com.cxqm.xiaoerke.modules.mutualHelp.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class DoctorMutualHelpVo {
    private Integer id;
    private String openid;
    private String name;//姓名
    private String sex;//性别
    private String phone;//电话
    private String idCard;//身份证号
    private String hospitalName;//医院名称
    private String department;//所在科室
    private String position;//职位
    private String practisingCertificate;//执业证书编号
    private String status;//账户状态
    private double balance;//账户余额
    private Date createTime;//创建时间
    private Date markTime;//余额低于20的时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPractisingCertificate() {
        return practisingCertificate;
    }

    public void setPractisingCertificate(String practisingCertificate) {
        this.practisingCertificate = practisingCertificate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Date markTime) {
        this.markTime = markTime;
    }
}
