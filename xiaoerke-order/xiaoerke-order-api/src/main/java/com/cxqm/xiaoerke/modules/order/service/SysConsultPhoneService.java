package com.cxqm.xiaoerke.modules.order.service;

import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 电话咨询添加号源页面初始数据
     * sunxiao
     */
    Map<String, Object> getRegisterInfo(String doctorId,String pageFlag);

    /**
     * 添加电话咨询号源
     * sunxiao
     */
    Map<String, String> addRegisters(SysConsultPhoneServiceVo Vo,List<String> timeList,
                                     String date,String operInterval) throws Exception;

    /**
     * 删除电话咨询号源时判断受影响的号源
     * sunxiao
     */
    String judgeRepeatEffect(String date,String timeParam,String doctorId,String operRepeat);

    /**
     * 删除电话咨询号源
     * sunxiao
     */
    int deleteRegisters(SysConsultPhoneServiceVo registerServiceVo,List<String> timeList,String date,String operRepeat) throws Exception;

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

    /**
     * 根据consultPhoneRegisterServiceId查询sys_consultPhone_service表信息
     * @param hashMap
     * @return
     * @author chenxiaoqiong
     */
    HashMap<String, Object> findSysConsultPhoneServiceByCRSIdExecute(HashMap<String, Object> hashMap);

    /**
     * 根据主键获取详细信息
     * @param id
     * @return
     * @author chenxiaoqiong
     */
    Map<String,Object> getSysPhoneConsultInfo(Integer id);
}
