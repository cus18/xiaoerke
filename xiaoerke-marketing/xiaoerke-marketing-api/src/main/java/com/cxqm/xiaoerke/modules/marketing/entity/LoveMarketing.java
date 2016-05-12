package com.cxqm.xiaoerke.modules.marketing.entity;

import java.util.Date;

/**
 * Created by feibendechayedan on 16/5/11.
 */
public class LoveMarketing {
    private  String id;
    private String openid;
    private  Date   createTime;
    private  double loveMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public double getLoveMoney() {
        return loveMoney;
    }

    public void setLoveMoney(double loveMoney) {
        this.loveMoney = loveMoney;
    }

}
