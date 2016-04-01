package com.cxqm.xiaoerke.modules.account.exception;

/**
 * Created by wangbaowei on 15/11/9.
 * 调用企业支付接口异常
 */
public class BusinessPaymentExceeption extends Exception{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BusinessPaymentExceeption() {
    }

    public BusinessPaymentExceeption(String message) {
        super(message);
    }
}
