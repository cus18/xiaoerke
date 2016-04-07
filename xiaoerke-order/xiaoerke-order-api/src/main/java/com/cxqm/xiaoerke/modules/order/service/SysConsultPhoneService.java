package com.cxqm.xiaoerke.modules.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;

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

    Map<String, Object> getRegisterInfo(String doctorId,String pageFlag);

    Map<String, String> addRegisters(SysConsultPhoneServiceVo Vo,List<String> timeList,
                                     String date,String operInterval);

    String judgeRepeatEffect(String date,String timeParam,String doctorId,String operRepeat);

    int deleteRegisters(SysConsultPhoneServiceVo registerServiceVo,List<String> timeList,String date,String operRepeat,String deleteBy);

    /**
     * 获取电话咨询模板列表
     * sunxiao
     * @param executeMap
     * @return
     */
    public List<ConsultPhoneRegisterTemplateVo> getRegisterTemplateList(Map<String, Object> executeMap);

    /**
     * 根据条件查询电话咨询信息
     * sunxiao
     */
    List<SysConsultPhoneServiceVo> findSysConsultPhoneByInfo(Map<String, Object> map);
}
