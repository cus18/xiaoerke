package com.cxqm.xiaoerke.modules.umbrella.entity;

import java.util.Date;

public class UmbrellaFamilyInfo {
    private Integer id;

    private String name;

    private Integer sex;

    private Date birthday;

    private Integer umbrellaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getUmbrellaId() {
        return umbrellaId;
    }

    public void setUmbrellaId(Integer umbrellaId) {
        this.umbrellaId = umbrellaId;
    }
}