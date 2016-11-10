package com.cxqm.xiaoerke.modules.consult.entity;

/**
 * 运营推广
 * @author sunxiao
 * 2016-11-10
 */
public class OperationPromotionTemplateVo {

    private Integer id;

    private String type;

    private String info1;

    private String info2;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }
}