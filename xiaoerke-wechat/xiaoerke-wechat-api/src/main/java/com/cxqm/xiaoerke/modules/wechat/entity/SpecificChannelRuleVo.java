package com.cxqm.xiaoerke.modules.wechat.entity;


import java.util.Date;

/**
 * Created by wangbaowei on 16/9/8.
 */
public class SpecificChannelRuleVo {

    private String id;
    private String qrcode;
    private Long babyCoin;
    private String documents;
    private String repeatdocuments;
    private Date endTime;

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

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getRepeatdocuments() {
        return repeatdocuments;
    }

    public void setRepeatdocuments(String repeatdocuments) {
        this.repeatdocuments = repeatdocuments;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
