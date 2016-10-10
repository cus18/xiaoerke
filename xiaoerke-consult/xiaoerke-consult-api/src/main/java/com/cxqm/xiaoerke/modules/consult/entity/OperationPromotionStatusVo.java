package com.cxqm.xiaoerke.modules.consult.entity;

import com.cxqm.xiaoerke.common.config.Global;

/**
 * Created by wangbaowei on 16/9/28.
 */
public enum  OperationPromotionStatusVo {

    KEY_WORD("keyword"),
    QCODE("qcode");
    private final String variable;
    OperationPromotionStatusVo(String variable) {
        this.variable = variable;
    }

    public String getVariable(){
        return variable;
    }
}
