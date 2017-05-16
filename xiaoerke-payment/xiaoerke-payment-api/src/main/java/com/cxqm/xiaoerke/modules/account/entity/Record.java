package com.cxqm.xiaoerke.modules.account.entity;

import java.util.Date;

/**
 * Created by wangbaowei on 15/9/29.
 */
public abstract class Record implements Comparable<Record>{

    private String id;

    private String userId;

    private Float amount;

    private Date receiveDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public int compareTo(Record o) {
      boolean flag =   this.getReceiveDate().getTime() -( o.getReceiveDate()==null?new Date().getTime():o.getReceiveDate().getTime())>0;
      if(flag){
        return -1;
      }
        return 1;
    }
}
