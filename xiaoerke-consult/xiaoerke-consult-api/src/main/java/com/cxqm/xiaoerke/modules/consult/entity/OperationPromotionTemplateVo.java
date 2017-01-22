package com.cxqm.xiaoerke.modules.consult.entity;

import java.sql.Time;
import java.util.Date;

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

    private Date info3;

    private Date info4;

    private Date info5;

    private Date info6;

    private String info3s;

    private String info4s;

    private String info5s;

    private String info6s;

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

    public String getInfo3s() {
        return info3s;
    }

    public void setInfo3s(String info3s) {
        this.info3s = info3s;
    }

    public String getInfo4s() {
        return info4s;
    }

    public void setInfo4s(String info4s) {
        this.info4s = info4s;
    }

    public String getInfo5s() {
        return info5s;
    }

    public void setInfo5s(String info5s) {
        this.info5s = info5s;
    }

    public String getInfo6s() {
        return info6s;
    }

    public void setInfo6s(String info6s) {
        this.info6s = info6s;
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

    public Date getInfo3() {
        return info3;
    }

    public void setInfo3(Date info3) {
        this.info3 = info3;
    }

    public Date getInfo4() {
        return info4;
    }

    public void setInfo4(Date info4) {
        this.info4 = info4;
    }

    public Date getInfo6() {
        return info6;
    }

    public void setInfo6(Date info6) {
        this.info6 = info6;
    }

    public Date getInfo5() {
        return info5;
    }

    public void setInfo5(Date info5) {
        this.info5 = info5;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }
}