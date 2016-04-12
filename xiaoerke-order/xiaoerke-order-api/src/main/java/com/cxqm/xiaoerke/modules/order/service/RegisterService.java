package com.cxqm.xiaoerke.modules.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterTemplateServiceVo;

import net.sf.json.JSONObject;

import com.cxqm.xiaoerke.common.persistence.Page;
/**
 * Created by sunxiao on 2015/7/30.
 */
public interface RegisterService {

    Map<String, Object> listAppointmentTimeHospital(Map<String, Object> params);

    Map<String, Object> listAppointmentTimeDoctor(Map<String, Object> params);

    Map<String, Object> listAppointmentTimeHospitalDoctor(Map<String, Object> params);

    Map<String, Object> doctorAppointmentInfoOfDay(Map<String, Object> params);

    Map<String, Object> doctorAppointmentInfoDetailOfDay(Map<String, Object> params);

    Map<String, Object> orderSourceRoute(Map<String, Object> params);

    Map<String, Object> returnRegisterInfo(String doctorId,String pageFlag);
    
    JSONObject getRegisterTime(String doctorId,String date,String locationId);

    String judgeRepeatEffect(String date,String timeParam,String locationId,String doctorId,String operRepeat);

    Map findRegisterList(Page<RegisterServiceVo> page, RegisterServiceVo registerServiceVo);
    
    Page<RegisterServiceVo> findWillNoRegisterList(Page<RegisterServiceVo> page);
    

    RegisterServiceVo getRegisterByInfo(String register);
    
    
    Map<String, String> addRegister(RegisterServiceVo registerServiceVo,List<String> timeList,
										   String date,String operInterval);
    
    void updateRegister(RegisterServiceVo registerServiceVo,String time ,String date,String operRepeat);

    int deleteRegisters(RegisterServiceVo registerServiceVo,List<String> timeList,String date,String operRepeat,String deleteBy);

    void insertRegister(RegisterServiceVo registerServiceVo,String time,String date);
    
    void deleteRegister(String id);
    
    List<Date> getDatesWithRegisters(String doctorId, String locationId, Date from, Integer days);
    
    List<RegisterServiceVo> getRegisterListByInfo(Map<String, Object> params);

    Map<String, Object> getDoctorAppointments4Doctor(Map<String, Object> params) throws Exception;

    /*
     *  作者: chenjiake
     *  功能：获取专业医生列表的数据
     *  参数：入口参数为list，包括了XXXX，出口参数，doctorDataVoList包括了
    */
    void generateDoctorDataVoList(List<HashMap<String, Object>> list, List<HashMap<String, Object>> doctorDataVoList);

	HashMap<String, Object> findRecentlyDateAppDate(String doctorId);

	Map<String, Object> GetDoctorVisitInfoByLocation(Map<String, Object> params);

	HashMap<String, Object> getRegisterServiceInfo(String registerId);

    Date getOrderCreateDate(String patientRegistId);
    
  //查询订单的就诊时间
    String getVisitTimeById(Map map);
    
    List<HashMap<String, Object>> getDoctorVisitInfoById(String id);

    List<HashMap<String, Object>> getDoctorVisitInfoByIdAndDate(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getDoctorVisitInfo(String id);

    List<HashMap<String, Object>> getDoctorVisitInfoByLocation(Map data);

    RegisterServiceVo getRegisterById(Map map);

    void batchInsertRegister(List<HashMap<String, Object>> excuteList);

    List<RegisterTemplateServiceVo> getRegisterTemplateList(Map<String, Object> executeMap);

	void updateRegisterTemplateByInfo(Map<String, Object> executeMap);

    int findDoctorRegisterServiceByData(HashMap<String, Object> hashMap);

    HashMap<String, Object> getCooperationHospitalTypeBySrsId(String register_service_id);

    Map<String,Object> getEarliestVisiteInfo(String doctorId);
}
