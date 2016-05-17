package com.cxqm.xiaoerke.modules.consult.entity;


import java.util.Date;

/**
 * Created by jiangzhongge on 2016-5-13.
 * 转接咨询会话Entity
 */
public class ConsultTransferListVo {

    private Integer id;        //主键ID
    private Integer sessionId;  //会话ID
    private Date createDate ;  //转诊列表创建时间
    private String createBy;   //转诊列表创建者
    private Date updateDate ;  //转诊列表更新时间
    private String sysUserId ;    //用户userID
    private String sysUserName ;    //用户userName
    private String sysUserIdCs;  //咨询医生userID
    private String sysUserNameCs;  //咨询医生userName
    private String delFlag ;   //是否删除标识
    private String status ;     //转诊状态
    private String department ; //转诊科室名称
    private String orderBy;       //是否排序

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysUserIdCs() {
        return sysUserIdCs;
    }

    public void setSysUserIdCs(String sysUserIdCs) {
        this.sysUserIdCs = sysUserIdCs;
    }

    public String getSysUserNameCs() {
        return sysUserNameCs;
    }

    public void setSysUserNameCs(String sysUserNameCs) {
        this.sysUserNameCs = sysUserNameCs;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
