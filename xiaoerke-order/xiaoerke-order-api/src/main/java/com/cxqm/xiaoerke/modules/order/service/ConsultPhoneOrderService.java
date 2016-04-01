package com.cxqm.xiaoerke.modules.order.service;

import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/18.
 */
public interface ConsultPhoneOrderService {

    //根据号源id获取号源的基本详情
    SysConsultPhoneServiceVo getConsultServiceInfo(Integer consultServiceId);

    //订单列表（电话咨询和预约就诊）
    List<HashMap<String,Object>> getOrderList(String type);
}
