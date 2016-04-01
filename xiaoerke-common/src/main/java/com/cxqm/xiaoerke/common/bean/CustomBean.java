package com.cxqm.xiaoerke.common.bean;

/**
 * Created by wangbaowei on 15/9/7.
 *
 * 微信多客服实体类
 */


public class CustomBean {
    private String id;
//    完整客服账号
    private String kf_account;
//    客服在线状态 1：pc在线，2：手机在线。若pc和手机同时在线则为 1+2=3
    private String status;
//    客服工号
    private String kf_id;
//    客服设置的最大自动接入数
    private String auto_accept;
//    客服当前正在接待的会话数
    private String accepted_case;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKf_account() {
        return kf_account;
    }

    public void setKf_account(String kf_account) {
        this.kf_account = kf_account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKf_id() {
        return kf_id;
    }

    public void setKf_id(String kf_id) {
        this.kf_id = kf_id;
    }

    public String getAuto_accept() {
        return auto_accept;
    }

    public void setAuto_accept(String auto_accept) {
        this.auto_accept = auto_accept;
    }

    public String getAccepted_case() {
        return accepted_case;
    }

    public void setAccepted_case(String accepted_case) {
        this.accepted_case = accepted_case;
    }
}
