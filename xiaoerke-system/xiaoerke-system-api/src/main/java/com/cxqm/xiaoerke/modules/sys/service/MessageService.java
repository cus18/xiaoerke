package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.modules.sys.entity.MessageVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface MessageService {

	HashMap<String, Object> findMessageNeedToInsert(
			HashMap<String, Object> hashMap);

	int addMsgAppointment(Map<String, Object> executeMap);

	void sendMsg2Doctor(String patientRegisterServiceId,String doctorName,String babyName,String userPhone,String date,String week,String beginTime,String hostpitalName);

	String getMsgAppointmentStatus(Map<String, Object> params);

	void sendDoctorWithDrawMessage(String userId, Float money, String token);

	void insertMessage(HashMap<String, Object> executeMap);

	List<MessageVo> findMessageByRegisterNo(String appointmentNo);

	void insertMonitorExecute(HashMap<String, Object> monitorMap);

	void insertMonitorConsultPhone(HashMap<String, Object> monitorMap);
	
	//获取个人的预约信息详情 @author 得良
    Map fidPersonAppointDetailInfoExcut(PerAppDetInfoVo perAppDetInfoVO);
    
    HashMap<String,Object> findAppointMessageExecute(HashMap<String,Object> hashMap);
    
  //获取分享信息详情 @author zdl
    HashMap<String,Object> findShareDetailInfoExecute(HashMap<String,Object> hashMap);
    HashMap<String,Object> findPhoneConsultShareDetailInfoExecute(HashMap<String, Object> hashMap);
    
    void saveAdvice(Map<String, Object>  hashMap);

	List<HashMap<String,Object>> evaluateReminderExecute();

	List<HashMap<String,Object>> AppointNoPayExecute();

	void setMonitorStatusExecute(HashMap<String,Object> hashMap);

	List<HashMap<String,Object>> LetsGoReminderExecute();

	List<HashMap<String, Object>> getTrackOrder();

	Map consultPhoneMsgRemind(String id);
}
