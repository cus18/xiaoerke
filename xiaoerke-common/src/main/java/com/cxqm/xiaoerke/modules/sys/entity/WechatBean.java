package com.cxqm.xiaoerke.modules.sys.entity;

/**
 * Created by Administrator on 2015/7/17.
 */
public class WechatBean {
    private String openid;
    private String access_token;
    private String refresh_token;
    private Long expires_in;
    private String scope;
    private String nickname;

    private String headimgurl;

    public String getOpenid() {
        return openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }



    public String getScope() {
        return scope;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }


    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getNickname() {
        return nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
