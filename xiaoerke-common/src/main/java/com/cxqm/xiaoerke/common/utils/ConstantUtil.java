package com.cxqm.xiaoerke.common.utils;

/**
 * modify 得良
 */
public enum ConstantUtil {

    NO_PAY("noPay"),
    NOT_INSTANT_CONSULTATION("notInstantConsultation"),
    PAY_SUCCESS("paySuccess"),
    USE_TIMES("useTimes"),
    USE_MONTH_TIMES("monthTimes"),
    USE_PER_TIMES("perTimes"),
    WITHIN_24HOURS("within24Hours"),
    DISTRIBUTOR("distributor"),
    CONSULTDOCTOR("consultDoctor"),
    VACCINEVALID("valid"),
    VACCINEINVALID("inValid"),
    ALL_VACCINE_INTERVAL("30");//所有疫苗接种>=30天

    private final String variable;

    ConstantUtil(String variable) {
        this.variable = variable;
    }

    public String getVariable(){
        return variable;
    }


}
