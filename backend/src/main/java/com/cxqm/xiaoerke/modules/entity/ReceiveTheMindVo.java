package com.cxqm.xiaoerke.modules.entity;

import com.cxqm.xiaoerke.modules.utils.excel.annotation.ExcelField;

/**
 * Created by sunxiao on 2016/5/18.
 */
public class ReceiveTheMindVo {
    
    private String serviceAttitude;

    private String openid;

    private String nickname;

    private String doctorName;

    private String phone;

    private String createtime;

    private String content;

    private String redPacket;

    @ExcelField(title="评价结果", align=2, sort=1)
    public String getServiceAttitude() {
        return serviceAttitude;
    }

    public void setServiceAttitude(String serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
    }

    @ExcelField(title="openId", align=2, sort=2)
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @ExcelField(title="微信号", align=2, sort=3)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ExcelField(title="评价医生", align=2, sort=4)
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @ExcelField(title="医生电话", align=2, sort=5)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title="评价时间", align=2, sort=6)
    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @ExcelField(title="留言内容", align=2, sort=7)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ExcelField(title="金额", align=2, sort=8)
    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }
}
