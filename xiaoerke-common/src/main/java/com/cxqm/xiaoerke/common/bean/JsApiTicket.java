package com.cxqm.xiaoerke.common.bean;

/**
 * Created by baoweiw on 2015/7/28.
 */
/**
 * @author little轩轩
 */
public class JsApiTicket {
    /**
     * 获取到的jsApiTicket凭证
     */
    private String ticket;
    /**
     * 凭证有效时间，单位：秒
     */
    private long expires_in;


    public long getexpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getTicket() {
        return ticket;
    }

    public void setticket(String ticket) {
        this.ticket = ticket;
    }

}