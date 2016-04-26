package com.cxqm.xiaoerke.modules.task.service;

import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.common.bean.WechatRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ScheduleTaskService {

	List<HashMap<String, Object>> evaluaReminder();

	void updateSrSAndPrsStatus();

	void updateSrSerStatus();

	List<HashMap<String, Object>> AppointNoPay();

	void CancelAppointNoPay();

	void repeatSettingRegister();
	
	void repeatSettingConsultPhoneRegister();

	void getCustomerOnlineTime(ArrayList<CustomBean> list);

	List<HashMap<String, Object>> getSettlementDocToday(String date);

	void saveWechatRecoder(List<WechatRecord> li);

	void updateWechatParameter(HashMap<String, Object> Map);

	List<HashMap<String, Object>> getOrderInfoAtMornings();

	List<HashMap<String, Object>> getOrderDoctorAtMornings();

	List<HashMap<String, Object>> getOrderInfoToday();

	List<HashMap<String,Object>> getOrderDoctorToday();

	void updateOrderInfoToDoc(List<String> classList);

	List<HashMap<String, Object>> getOrderInfoToDoc();

	List<HashMap<String, Object>> LetsGoReminder();

	void setMonitorStatus(HashMap<String, Object> hashMap);

	List<HashMap<String, Object>> getUserListYesterday(String date);

	HashMap<String, Object> getUserOperationStatistic(
			HashMap<String, Object> data);

	List<HashMap<String,Object>> getUserOrderNextWeek();

    /**
     * 5min之前预约成功的电话咨询订单
     * @return
     */
    List<HashMap<String,Object>> getOrderInfoToDoc5minAfterSuccess();

    List<HashMap<String,Object>> getDoctorInfoByDate(String date);

    List<HashMap<String,Object>> getOrderInfoByDate(String tomorrow);
}
