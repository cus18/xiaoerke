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

    /**
     * 订单列表（电话咨询和预约就诊）
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    Map<String, Object> getOrderList(Map<String,Object> params);

    /**
     * 电话咨询（含分页）
     * @param params
     * @return
     * @author chenxiaoqiong
     */
    Map<String, Object> getUserOrderPhoneConsultList(Map<String, Object> params);


    /**
     * 定时任务
     * 查询当前时间需要创建通话链接的订单数据
     * */
    List<HashMap<String, Object>> getOrderPhoneConsultListByTime(String state);
}
