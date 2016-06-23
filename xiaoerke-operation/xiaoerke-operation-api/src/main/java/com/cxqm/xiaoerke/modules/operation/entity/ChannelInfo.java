package com.cxqm.xiaoerke.modules.operation.entity;

/**
 * Created by guozengguang on 2016/6/22.
 */
public class ChannelInfo {
    private Integer id; //渠道添加主键
    private String create_by;   //渠道添加人
    private String department;  //部门
    private String marker;      //二维码
    private String channel;     //渠道

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
