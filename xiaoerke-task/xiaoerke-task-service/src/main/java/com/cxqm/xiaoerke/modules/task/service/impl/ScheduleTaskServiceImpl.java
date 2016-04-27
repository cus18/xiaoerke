package com.cxqm.xiaoerke.modules.task.service.impl;

import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.operation.service.SysLogService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterTemplateServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.task.dao.ScheduledTaskDao;
import com.cxqm.xiaoerke.modules.task.service.ScheduleTaskService;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = false)
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

	@Autowired
	private ScheduledTaskDao scheduledTaskDao;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private MongoDBService<WechatRecord> mongoDBService;

	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private SysConsultPhoneService sysConsultPhoneService;

	@Autowired
	private WeChatInfoService weChatInfoService;
	
	private String mongoEnabled = Global.getConfig("mongo.enabled");
	
	 //查询插入评价提醒消息，所需要的信息，就诊后
	@Override
    public List<HashMap<String, Object>> evaluaReminder() {
        return messageService.evaluateReminderExecute();
    }
	
	// 监听就诊结束时间，就诊时间结束的话更新sys_register_service表status字段和patient_register_service表status字段
	@Override
	public void updateSrSAndPrsStatus() {
		scheduledTaskDao.updatePatRegSerStatusExecute();
	}

	// 每天晚上11：00更新sys_register_service中的status状态
	@Override
	public void updateSrSerStatus() {
		scheduledTaskDao.updateSrSAndPrsStatusExecute();
	}
	
	//订单生成5分钟内未支付 @author zdl
	@Override
    public List<HashMap<String, Object>> AppointNoPay() {
        return messageService.AppointNoPayExecute();
    }

    //更新sys_monitor表状态
	@Override
    public void setMonitorStatus(HashMap<String, Object> hashMap) {
		messageService.setMonitorStatusExecute(hashMap);
    }

	@Override
	public List<HashMap<String, Object>> getUserListYesterday(String date) {
		return sysLogService.getUserListYesterday(date);
	}

	//查询插入出发提醒消息，所需要的信息
	@Override
    public List<HashMap<String, Object>> LetsGoReminder() {
        return messageService.LetsGoReminderExecute();
    }
    
  //订单生成30分钟内未支付,则取消订单(更新patient_register_service中的status，并且更新sys_register_service中的状态)
  	@Override
  	public void CancelAppointNoPay() {
  		scheduledTaskDao.CancelAppointNoPayExecute_1();
  		scheduledTaskDao.CancelAppointNoPayExecute_2();
      }

    /**
     * 患者预约30分钟后发送订单消息给医生
     */
  	@Override
    public List<HashMap<String, Object>> getOrderInfoToDoc() {
        return scheduledTaskDao.getOrderInfoToDoc();
    }

    /**
     * 患者预约30分钟后发送订单消息给医生后更新短
  	@Override信状态
	*/
    public void updateOrderInfoToDoc(List<String> classList) {
    	scheduledTaskDao.updateOrderInfoToDoc(classList);
    }
    
    /**
     * 晚八点提示医生明天所预约的详情
     */
  	@Override
    public List<HashMap<String, Object>> getOrderInfoToday() {
        return scheduledTaskDao.getOrderInfoToday();
    }

	@Override
	public List<HashMap<String, Object>> getOrderDoctorToday() {
		return scheduledTaskDao.getOrderDoctorToday();
	}

    /**
     * 早七点提示医生明天所预约的详情
     */
  	@Override
    public List<HashMap<String, Object>> getOrderInfoAtMornings() {
        return scheduledTaskDao.getOrderInfoAtMorning();
    }

	@Override
	public List<HashMap<String, Object>> getOrderDoctorAtMornings() {
		return scheduledTaskDao.getOrderDoctorAtMorning();
	}

  	@Override
    public void updateWechatParameter(HashMap<String, Object> Map) {
    	scheduledTaskDao.updateWechatParameter(Map);
    }
  	
  	@Override
    public void saveWechatRecoder(List<WechatRecord> li) {
    	scheduledTaskDao.saveWechatRecoder(li);
        if("true".equalsIgnoreCase(mongoEnabled)) {
        	mongoDBService.insertByBatch(li);
        }
    }

  	@Override
    public List<HashMap<String,Object>> getSettlementDocToday(String date) {
        return scheduledTaskDao.getSettlementDocToday(date);
    }

	@Override
	public void getCustomerOnlineTime(ArrayList<CustomBean> list) {
		weChatInfoService.getCustomerOnlineTime(list);
	}
    
  	/**
  	 * 重复设置号源  sunxiao
  	 */
  	@Override
  	public void repeatSettingRegister() {
    	Calendar c = new GregorianCalendar(); 
		c.setTime(new Date());
		String weekday = c.get(Calendar.DAY_OF_WEEK)+"";
		c.add(Calendar.DAY_OF_YEAR, 28);  
		Date date = c.getTime();  
		String repeatDate = DateUtils.DateToStr(date, "date");
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("status", 1);
    	map.put("weekday", weekday);
    	List<RegisterTemplateServiceVo> list = registerService.getRegisterTemplateList(map);
    	List<HashMap<String, Object>> excuteList = new ArrayList<HashMap<String,Object>>();
    	for(RegisterTemplateServiceVo vo : list){
    		String time = vo.getTime();
    		RegisterServiceVo registerServiceVo = new RegisterServiceVo();
    		registerServiceVo.setSysDoctorId(vo.getDoctorId());
    		registerServiceVo.setSysHospitalId(vo.getHospitalId());
    		registerServiceVo.setPrice(vo.getPrice());
    		registerServiceVo.setServiceType(vo.getServerType());
    		registerServiceVo.setLocationId(vo.getLocationId());
    		registerServiceVo.setRepeatFlag(vo.getRepeatInterval());
    		if("1".equals(vo.getRepeatInterval())){//闅斿懆閲嶅
    			Date ud = vo.getUpdateDate();
    	    	Date bdate=new Date(); 
    	    	Calendar cal = Calendar.getInstance(); 
    	    	cal.setTime(ud); 
    	    	long time1 = cal.getTimeInMillis(); 
    	    	cal.setTime(bdate); 
    	    	long time2 = cal.getTimeInMillis(); 
    	    	long between_days=(time2-time1)/(1000*3600*24); 
    	    	if(between_days==14||between_days==0){
    	    		HashMap<String, Object> retMap = prepareRegisterBatch(registerServiceVo, time, repeatDate);
    	    		if(retMap!=null){
    	    			excuteList.add(retMap);
    	    		}
    	    		Map<String, Object> excuteMap = new HashMap<String, Object>();
    	    		excuteMap.put("doctorId", vo.getDoctorId());
    	    		excuteMap.put("locationId", vo.getLocationId());
    	    		excuteMap.put("weekday", weekday);
    	    		excuteMap.put("price", vo.getPrice());
    	    		excuteMap.put("serverType", vo.getServerType());
    	    		excuteMap.put("time", time);
    	    		c.setTime(new Date());
    	    		c.add(Calendar.DAY_OF_YEAR, -14);  
					Date updateDate = c.getTime();  
    	    		List<String> updateList = new ArrayList<String>();
    	    		updateList.add(DateUtils.DateToStr(updateDate,"date"));
    	    		updateList.add(DateUtils.DateToStr(new Date(),"date"));
    	    		excuteMap.put("updateDateList", updateList);
    	    		excuteMap.put("updateDate", new Date());
					registerService.updateRegisterTemplateByInfo(excuteMap);
    	    	}
    		} else {
    			HashMap<String, Object> retMap = prepareRegisterBatch(registerServiceVo, time, repeatDate);
	    		if(retMap!=null){
	    			excuteList.add(retMap);
	    		}
    		}
    	}
    	if(excuteList.size()!=0){
			registerService.batchInsertRegister(excuteList);
    	}
    }

	/**
  	 * 准备批量插入号源数据
  	 * sunxiao
  	 */
  	private HashMap<String, Object> prepareRegisterBatch(RegisterServiceVo registerServiceVo,String time,String date){
		HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
		Date begin_time= DateUtils.StrToDate(time,"time");

		Date end_time = new Date(begin_time.getTime() + 900000);
		doctorInfo.put("id", IdGen.uuid());
		doctorInfo.put("doctorId", registerServiceVo.getSysDoctorId());
		doctorInfo.put("sys_hospital_id", registerServiceVo.getSysHospitalId());
		doctorInfo.put("date", date);
		doctorInfo.put("begin_time", begin_time);
		doctorInfo.put("end_time", end_time);
		doctorInfo.put("price", registerServiceVo.getPrice());
		doctorInfo.put("deposit", "50");//鎵�渶鎶奸噾
		doctorInfo.put("service_type", registerServiceVo.getServiceType());//鏈嶅姟绫诲瀷
		doctorInfo.put("create_date", new Date());
		doctorInfo.put("locationId", registerServiceVo.getLocationId());
		doctorInfo.put("status", "0");
		doctorInfo.put("repeatFlag", registerServiceVo.getRepeatFlag());
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("date", date);
		queryMap.put("time", begin_time);
		queryMap.put("locationId", registerServiceVo.getLocationId());
		queryMap.put("sysDoctorId", registerServiceVo.getSysDoctorId());
		List<RegisterServiceVo> rlist = registerService.getRegisterListByInfo(queryMap);
		if(rlist.size()!=0){
			return null;
		}
		return doctorInfo;
    }

  	/**
  	 * 重复设置电话咨询号源  sunxiao
  	 */
  	@Override
  	public void repeatSettingConsultPhoneRegister() {
    	Calendar c = new GregorianCalendar(); 
		c.setTime(new Date());
		String weekday = c.get(Calendar.DAY_OF_WEEK)+"";
		c.add(Calendar.DAY_OF_YEAR, 28);  
		Date date = c.getTime();  
		String repeatDate = DateUtils.DateToStr(date, "date");
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("status", 1);
    	map.put("weekday", weekday);
    	List<ConsultPhoneRegisterTemplateVo> list = sysConsultPhoneService.getRegisterTemplateList(map);
    	List<HashMap<String, Object>> excuteList = new ArrayList<HashMap<String,Object>>();
    	for(ConsultPhoneRegisterTemplateVo vo : list){
    		String time = vo.getTime();
    		SysConsultPhoneServiceVo registerServiceVo = new SysConsultPhoneServiceVo();
    		registerServiceVo.setSysDoctorId(vo.getDoctorId());
    		registerServiceVo.setPrice(vo.getPrice()+"");
    		registerServiceVo.setServicetype(vo.getServerType());
    		registerServiceVo.setRepeatFlag(vo.getRepeatInterval());
			HashMap<String, Object> retMap = prepareConsultPhoneRegisterBatch(registerServiceVo, time, repeatDate);
    		if(retMap!=null){
    			excuteList.add(retMap);
    		}
    	}
    	if(excuteList.size()!=0){
			registerService.batchInsertRegister(excuteList);
    	}
    }
  	
  	/**
  	 * 准备批量插入电话咨询号源数据
  	 * sunxiao
  	 */
  	private HashMap<String, Object> prepareConsultPhoneRegisterBatch(SysConsultPhoneServiceVo registerServiceVo,String time,String date){
		HashMap<String, Object> registerInfo = new HashMap<String, Object>();
		Date begin_time= DateUtils.StrToDate(time,"time");

		Date end_time = new Date(begin_time.getTime() + 900000);
		registerInfo.put("id", IdGen.uuid());
		registerInfo.put("doctorId", registerServiceVo.getSysDoctorId());
		registerInfo.put("date", date);
		registerInfo.put("begin_time", begin_time);
		registerInfo.put("end_time", end_time);
		registerInfo.put("price", registerServiceVo.getPrice());
		registerInfo.put("deposit", "50");//鎵�渶鎶奸噾
		registerInfo.put("service_type", registerServiceVo.getServicetype());//鏈嶅姟绫诲瀷
		registerInfo.put("create_date", new Date());
		registerInfo.put("status", "0");
		registerInfo.put("repeatFlag", registerServiceVo.getRepeatFlag());
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("date", date);
		queryMap.put("time", begin_time);
		queryMap.put("sysDoctorId", registerServiceVo.getSysDoctorId());
		List<SysConsultPhoneServiceVo> rlist = sysConsultPhoneService.findSysConsultPhoneByInfo(queryMap);
		if(rlist.size()!=0){
			return null;
		}
		return registerInfo;
    }
  	
	@Override
	public HashMap<String,Object> getUserOperationStatistic(HashMap<String,Object> data) {
		return sysLogService.getUserOperationStatistic(data);
	}

	@Override
	public List<HashMap<String,Object>> getUserOrderNextWeek() {
		return scheduledTaskDao.getUserOrderNestWeek();
	}

    @Override
    public List<HashMap<String, Object>> getOrderInfoToDoc5minAfterSuccess() {
        return scheduledTaskDao.getOrderInfoToDoc5minAfterSuccess();
    }

    @Override
    public List<HashMap<String, Object>> getOrderInfoToDoc5minBeforeConnect() {
        return scheduledTaskDao.getOrderInfoToDoc5minBeforeConnect();
    }

    @Override
    public List<HashMap<String, Object>> getDoctorInfoByDate(String date) {
        return scheduledTaskDao.getDoctorInfoByDate(date);
    }

    @Override
    public List<HashMap<String, Object>> getOrderInfoByDate(String date) {
        return scheduledTaskDao.getOrderInfoByDate(date);
    }

}
