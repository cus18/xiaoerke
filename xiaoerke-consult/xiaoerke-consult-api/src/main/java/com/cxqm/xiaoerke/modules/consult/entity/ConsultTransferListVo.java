package com.cxqm.xiaoerke.modules.consult.entity;


import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * Created by jiangzhongge on 2016-5-13.
 * 转接咨询会话Entity
 */
public class ConsultTransferListVo {

    private Integer id;        //主键ID
    private Integer sessionId;  //会话ID
    private Date create_date ;  //转诊列表创建时间
    private String create_by;   //转诊列表创建者
    private Date update_date ;  //转诊列表更新时间
    private String sys_user_id ;    //用户userID
    private String sys_user_name ;    //用户userName
    private String sys_user_id_cs;  //咨询医生userID
    private String sys_user_name_cs;  //咨询医生userName
    private String del_flag ;   //是否删除标识
    private String status ;     //转诊状态
    private String department ; //转诊科室名称
    private String department_id;   //转诊科室ID
    @Transient
    private String order;       //是否排序

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getSys_user_id() {
        return sys_user_id;
    }

    public void setSys_user_id(String sys_user_id) {
        this.sys_user_id = sys_user_id;
    }

    public String getSys_user_id_cs() {
        return sys_user_id_cs;
    }

    public void setSys_user_id_cs(String sys_user_id_cs) {
        this.sys_user_id_cs = sys_user_id_cs;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getSys_user_name() {
        return sys_user_name;
    }

    public void setSys_user_name(String sys_user_name) {
        this.sys_user_name = sys_user_name;
    }

    public String getSys_user_name_cs() {
        return sys_user_name_cs;
    }

    public void setSys_user_name_cs(String sys_user_name_cs) {
        this.sys_user_name_cs = sys_user_name_cs;
    }

}
