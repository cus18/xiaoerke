package com.cxqm.xiaoerke.modules.entity;

import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;

/**
 * Created by sunxiao on 2016/5/18.
 */
public class DissatisfiedVo {

    private String nickname;

    private String openid;

    private String doctorName;

    private String phone;

    private String content;

    private String createtime;

    private String dissatisfied;

    private String attitude;

    private String ability;

    private String response;

    private String other;

    @ExcelField(title="用户微信号", align=2, sort=1)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ExcelField(title="openId", align=2, sort=2)
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @ExcelField(title="评价医生", align=2, sort=3)
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @ExcelField(title="医生电话", align=2, sort=4)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title="留言内容", align=2, sort=5)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDissatisfied() {
        return dissatisfied;
    }

    public void setDissatisfied(String dissatisfied) {
        this.dissatisfied = dissatisfied;
    }

    @ExcelField(title="服务态度", align=2, sort=6)
    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    @ExcelField(title="专业水平", align=2, sort=7)
    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @ExcelField(title="响应速度", align=2, sort=8)
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @ExcelField(title="其他", align=2, sort=9)
    public String getOther() {
        return other;
    }

    @ExcelField(title="评价时间", align=2, sort=3)
    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
