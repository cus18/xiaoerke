/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class AccountVo extends BaseEntity<AccountVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 账户表的主键ID
    private String sysPatientId;    // sys_patient表的ID
    private float accountNumber;    // 用户的账户余额
    private float depositNumber;    // 用户的存入金额
    private float takeNumber;    // 用户的取出金额
    private Date createTime;    // 记录创建时间
    private Date updateTime;    // 记录删除时间
    private String remark;

    public AccountVo() {
        super();
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSysPatientId() {
        return sysPatientId;
    }

    public void setSysPatientId(String sysPatientId) {
        this.sysPatientId = sysPatientId;
    }

    public float getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(float accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(float depositNumber) {
        this.depositNumber = depositNumber;
    }

    public float getTakeNumber() {
        return takeNumber;
    }

    public void setTakeNumber(float takeNumber) {
        this.takeNumber = takeNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


