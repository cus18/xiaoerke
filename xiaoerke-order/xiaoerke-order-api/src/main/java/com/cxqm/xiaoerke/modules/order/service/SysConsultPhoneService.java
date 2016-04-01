package com.cxqm.xiaoerke.modules.order.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * Created by cxq on 2016/3/21.
 */
public interface SysConsultPhoneService {
    //获取可以预约的日期dateList
    Map<String, Object> getDoctorConsultDate(HashMap<String,Object> dataMap);

    Integer getCount();

    //获取可预约的时间available_time
    Map<String, Object> doctorConsultPhoneOfDay(Map<String, Object> params);
    
    JSONObject getRegisterTime(String doctorId , String date);
}
