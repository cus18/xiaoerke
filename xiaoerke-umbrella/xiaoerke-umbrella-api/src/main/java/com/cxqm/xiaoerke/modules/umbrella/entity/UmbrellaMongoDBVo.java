package com.cxqm.xiaoerke.modules.umbrella.entity;

import java.util.Date;

/**
 * Created by sunxiao on 2016/6/20.
 */
public class UmbrellaMongoDBVo {
    private  String openid;
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
