package com.cxqm.xiaoerke.modules.vaccine.entity;

/**
 * Created by sunxiao on 2016-12-12.
 */
public class VaccineScanCodeVo {

    private Integer id;

    private String name;

    private String age;

    private String isfree;

    private String diseasePrevention;

    private String attention;

    private String informedForm;

    private String informedFormTitle;

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

    public String getInformedFormTitle() {
        return informedFormTitle;
    }

    public void setInformedFormTitle(String informedFormTitle) {
        this.informedFormTitle = informedFormTitle;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIsfree() {
        return isfree;
    }

    public void setIsfree(String isfree) {
        this.isfree = isfree;
    }

    public String getDiseasePrevention() {
        return diseasePrevention;
    }

    public void setDiseasePrevention(String diseasePrevention) {
        this.diseasePrevention = diseasePrevention;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getInformedForm() {
        return informedForm;
    }

    public void setInformedForm(String informedForm) {
        this.informedForm = informedForm;
    }
}
