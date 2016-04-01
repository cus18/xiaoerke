package com.cxqm.xiaoerke.common.utils;

public class Cookiebean {
    private String idurlname;//cookie的名称，组成的格式为：商品id+商品图片地址+商品名称
    private Integer xuhao;//用来记录浏览排序

    public String getIdurlname() {
        return idurlname;
    }

    public void setIdurlname(String idurlname) {
this.idurlname = idurlname;
}

    public Integer getXuhao() {
return xuhao;
}

    public void setXuhao(Integer xuhao) {
this.xuhao = xuhao;
}
}