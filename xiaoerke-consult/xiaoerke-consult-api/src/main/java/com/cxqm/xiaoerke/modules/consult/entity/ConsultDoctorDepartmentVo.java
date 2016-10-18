package com.cxqm.xiaoerke.modules.consult.entity;

import java.util.Date;

/**
 * 咨询医生部门
 * @author sunxiao
 * 2016-10-18
 */
public class ConsultDoctorDepartmentVo {

    private String id;

    private String name;

    private Short sorting;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Short getSorting() {
        return sorting;
    }

    public void setSorting(Short sorting) {
        this.sorting = sorting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}