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

    private String isShow;

    private String image;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
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