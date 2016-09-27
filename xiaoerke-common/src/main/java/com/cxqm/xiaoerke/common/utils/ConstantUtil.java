package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.common.config.Global;

/**
 * modify 得良
 */
public enum ConstantUtil {

    SERVER_ADDRESS(Global.getConfig("SERVER_ADDRESS")),
    NO_PAY("noPay"),
    NOT_INSTANT_CONSULTATION("notInstantConsultation"),
    PAY_SUCCESS("paySuccess"),
    USE_TIMES("useTimes"),
    WITHIN_24HOURS("within24Hours"),
    DISTRIBUTOR("distributor"),
    CONSULTDOCTOR("consultDoctor"),
    VACCINEVALID("valid"),
    VACCINEINVALID("inValid");

    private final String variable;

    ConstantUtil(String variable) {
        this.variable = variable;
    }

    public String getVariable(){
        return variable;
    }


}
