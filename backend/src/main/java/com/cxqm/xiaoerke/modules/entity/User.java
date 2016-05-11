package com.cxqm.xiaoerke.modules.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.DataEntity;
import com.cxqm.xiaoerke.common.supcan.annotation.treelist.cols.SupCol;
import com.cxqm.xiaoerke.common.utils.Collections3;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User {

    private String id;
    private static final long serialVersionUID = 1L;
    public static final String USER_TYPE_USER = "user";
    public static final String USER_TYPE_DOCTOR = "doctor";
    public static final String USER_TYPE_DISTRIBUTOR = "distributor";
    public static final String USER_TYPE_CONSULTDOCTOR = "consultDoctor";

    private String loginName;// 登录名
    private String password;// 密码
    private String no;		// 工号
    private String name;	// 姓名
    private String email;	// 邮箱
    private String phone;	// 电话
    private String mobile;	// 手机
    private String userType;// 用户类型
    private String loginIp;	// 最后登陆IP
    private Date loginDate;	// 最后登陆日期
    private String loginFlag;	// 是否允许登陆
    private String photo;	// 头像

    private String oldLoginName;// 原登录名
    private String newPassword;	// 新密码

    private String oldLoginIp;	// 上次登陆IP

    private Date oldLoginDate;	// 上次登陆日期

    private String openid;//openid

    private  String remarks;

    private Date createDate;

    private String marketer;

    public User() {
        super();
        this.loginFlag = Global.YES;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @SupCol(isUnique="true", isHide="true")
    @ExcelField(title="ID", type=1, align=2, sort=1)
    public String getId() {
        return id;
    }

    @Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
    @ExcelField(title="登录名", align=2, sort=30)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JsonIgnore
    @Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
    public String getPassword() {
        return password;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
    @ExcelField(title="姓名", align=2, sort=40)
    public String getName() {
        return name;
    }

    @Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
    @ExcelField(title="工号", align=2, sort=45)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email(message="邮箱格式不正确")
    @Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
    @ExcelField(title="邮箱", align=1, sort=50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
    @ExcelField(title="电话", align=2, sort=60)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
    @ExcelField(title="手机", align=2, sort=70)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ExcelField(title="备注", align=1, sort=900)
    public String getRemarks() {
        return remarks;
    }

    @Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
    @ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @ExcelField(title="创建时间", type=0, align=1, sort=90)
    public Date getCreateDate() {
        return createDate;
    }

    @ExcelField(title="最后登录IP", type=1, align=1, sort=100)
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="最后登录日期", type=1, align=1, sort=110)
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null){
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null){
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public boolean isAdmin(){
        return isAdmin(this.id);
    }

    public static boolean isAdmin(String id){
        return id != null && "1".equals(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @JsonIgnore
    @Length(min=1, max=500, message="openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }
}