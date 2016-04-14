package com.cxqm.xiaoerke.modules.order.exception;

/**
 * Created by wangbaowei on 16/4/14.
 */
public class CancelOrderException extends Exception{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CancelOrderException() {
    }

    public CancelOrderException(String message) {
        super(message);
    }
}
