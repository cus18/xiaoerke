package com.cxqm.xiaoerke.common.bean;

/**
 * Created by 王保伟 on 2015/8/20.
 */
public class WxQrcode {

    private String ticket;

    private String expire_seconds;

    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(String expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
