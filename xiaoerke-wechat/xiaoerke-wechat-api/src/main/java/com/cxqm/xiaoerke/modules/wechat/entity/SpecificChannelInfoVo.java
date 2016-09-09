package com.cxqm.xiaoerke.modules.wechat.entity;


import java.util.Date;

/**
 * Created by wangbaowei on 16/9/8.
 */
public class SpecificChannelInfoVo {

    private String id;
    private String qrcode;
    private Long babyCoin;
    private String openid;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Long getBabyCoin() {
        return babyCoin;
    }

    public void setBabyCoin(Long babyCoin) {
        this.babyCoin = babyCoin;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
