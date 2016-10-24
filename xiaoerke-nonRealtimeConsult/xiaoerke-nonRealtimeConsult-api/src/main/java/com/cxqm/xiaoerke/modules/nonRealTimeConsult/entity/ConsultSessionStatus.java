package com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity;

import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Created by wangbaowei on 16/10/24.
 */
public enum  ConsultSessionStatus {
    CREATE_SESSION("create_session"),
    TEXT("text"),
    IMG("img");

    private final String variable;

    ConsultSessionStatus(String variable) {
        this.variable = variable;
    }

    public String getVariable(){
        return variable;
    }
}
