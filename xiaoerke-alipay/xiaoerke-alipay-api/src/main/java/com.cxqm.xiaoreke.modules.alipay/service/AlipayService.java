package com.cxqm.xiaoreke.modules.alipay.service;

import java.util.Map;

/**
 * Created by wangbaowei on 16/7/6.
 */
public interface AlipayService {

    //调用支付宝支付
    String alipayment(String totleFee,String body,String describe,String showUrl);

    //异步接收支付信息
    String notification(Map requestParams, String out_trade_no, String tradeStatus);

    void returnAlipay(String detailData,String batchNum,String desc);
}
