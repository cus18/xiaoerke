package com.cxqm.xiaoerke.modules.operation.entity;

/**
 * Created by guozengguang on 2016/6/22.
 */
public class ChannelInfo {
    private Integer id; //渠道添加主键
    private String operater;   //渠道添加人
    private String department;  //部门
    private String marketer;      //二维码
    private String channel;     //渠道

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMarketer() {
        return marketer;
    }

    public void setMarketer(String marketer) {
        this.marketer = marketer;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}