package com.cxqm.xiaoerke.modules.consult.entity;

/**
 * Created by wangbaowei on 16/11/15.
 */
public enum  MessageContentVo {

    SESSION_END("咨询关闭评价"),
    MIND_PAY("送心意成功");

    private final String variable;

    MessageContentVo(String variable) {
        this.variable = variable;
    }

    public String getVariable(){
        return variable;
    }

}
