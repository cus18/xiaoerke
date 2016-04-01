/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.entity;

import com.cxqm.xiaoerke.common.persistence.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流Entity
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class IllnessVo<T> extends BaseEntity<IllnessVo> {

    private static final long serialVersionUID = 1L;
    private String id;        // 疾病的主键ID
    private String level_1;    // 疾病的一级类名
    private String level_2;    // 疾病的二级类名
    private String details;    // 疾病的详细描述
    private String doctorNum;
    private Integer sort;  //二级疾病显示顺序
    private String sort_level_1;//一级疾病显示顺序
    public IllnessVo() {
        super();
    }

    private List<T> list = new ArrayList<T>();//当前Vo数据对象

    @Override
    public void preInsert() {

    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDoctorNum() {
        return doctorNum;
    }

    public void setDoctorNum(String doctorNum) {
        this.doctorNum = doctorNum;
    }

    public String getSort_level_1() {
        return sort_level_1;
    }

    public void setSort_level_1(String sort_level_1) {
        this.sort_level_1 = sort_level_1;
    }

    @Override
    public void preUpdate() {

    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLevel_1() {
        return level_1;
    }

    public void setLevel_1(String level_1) {
        this.level_1 = level_1;
    }

    public String getLevel_2() {
        return level_2;
    }

    public void setLevel_2(String level_2) {
        this.level_2 = level_2;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}


