package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterTemplateDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbaowei on 16/3/21.
 */
@Service
public class SysConsultPhoneServiceImpl implements SysConsultPhoneService {

	@Autowired
	SysConsultPhoneServiceDao sysConsultPhoneServiceDao;

	@Autowired
	ConsultPhoneRegisterTemplateDao consultPhoneRegisterTemplateDao;

	@Autowired
	ConsultPhonePatientService consultPhonePatientService;

	@Autowired
	private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

    @Override
	public Map<String, Object> getDoctorConsultDate(HashMap<String,Object> dataMap) {
		List<HashMap<String, Object>> resultList =  sysConsultPhoneServiceDao.getConsultDateList(dataMap);

        Map<String, Object> response = new HashMap<String, Object>();
		List<HashMap<String, Object>> consultDateList = new LinkedList<HashMap<String, Object>>();
		if(resultList != null && !resultList.isEmpty()){
			for(HashMap<String,Object> map:resultList){
				HashMap<String,Object> consultDate = new HashMap<String, Object>();
				consultDate.put("date",(String)map.get("date"));
				consultDateList.add(consultDate);
			}
		}
		response.put("dateList",consultDateList);

		return response;
	}

    @Override
    public Map<String, Object> getDoctorConsultDateInfo(HashMap<String,Object> dataMap) {
        List<HashMap<String, Object>> resultList =  sysConsultPhoneServiceDao.getConsultDateInfoList(dataMap);

        Map<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> consultDateList = new LinkedList<HashMap<String, Object>>();
        if(resultList != null && !resultList.isEmpty()){
            for(HashMap<String,Object> map:resultList){
                HashMap<String,Object> consultDate = new HashMap<String, Object>();
                consultDate.put("date",(String)map.get("date"));
                consultDateList.add(consultDate);
            }
        }
        response.put("dateList",consultDateList);

        return response;
    }

	@Override
	public Integer getCount() {
		return sysConsultPhoneServiceDao.finCountOfService();
	}

	@Override
	public Map<String, Object> doctorConsultPhoneOfDay(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();

		String doctorId = (String) params.get("doctorId");
		Date date = DateUtils.formatDate(params);

		// 根据日期得到周几
		String DateToStr = DateUtils.DateToStr(date);
		int week = 0;
		try {
			week = DateUtils.dayForWeek(DateToStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String dayWeek = DateUtils.getDayWeek(week);

		StringBuffer bf = new StringBuffer();
		bf.append(DateToStr.substring(0, 10));
		bf.append("  " + dayWeek).toString();
		response.put("date", bf.toString());

		HashMap<String, Object> dataInfo = new HashMap<String, Object>();
		dataInfo.put("doctorId", doctorId);
		dataInfo.put("date", date);
		List<HashMap<String, Object>> resultList = sysConsultPhoneServiceDao.findConsultPhoneTimeListByDoctorAndDate(dataInfo);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000030" ,"医生主键："+ doctorId
				+ "date:" + date);

		DateFormat formart = new SimpleDateFormat("HH:mm");
		List<HashMap<String, Object>> consultPhoneTimeList = new LinkedList<HashMap<String, Object>>();
		if(resultList != null && !resultList.isEmpty()){
			for(HashMap<String, Object> map:resultList){
				HashMap<String, Object> consultPhoneTime = new HashMap<String, Object>();
				consultPhoneTime.put("price", (String) map.get("price"));
				consultPhoneTime.put("id",(Integer)map.get("id"));
				consultPhoneTime.put("data", DateToStr);
				consultPhoneTime.put("begin_time",(String)map.get("begin_time"));
				try {
					Date start = formart.parse((String)map.get("begin_time"));
					Date now = formart.parse(formart.format(new Date()));

					if(date.getTime()>=new Date().getTime()){
						consultPhoneTime.put("state",(String)map.get("state"));
					}else{
						if(start.getTime()>now.getTime()){
							consultPhoneTime.put("state",(String)map.get("state"));
						}else{
							consultPhoneTime.put("state","1");
						}

					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				consultPhoneTime.put("end_time", (String) map.get("end_time"));

				consultPhoneTime.put("serviceType",(String)map.get("serviceType"));
                consultPhoneTime.put("repeatFlag",(String)map.get("repeatFlag"));
				consultPhoneTimeList.add(consultPhoneTime);
			}
		}

		response.put("consultPhoneTimeList", consultPhoneTimeList);

		return response;
	}

	/**
	 * 返回一个医生指定日期的号源
	 */
	@Override
	public JSONObject getRegisterTime(String doctorId , String date) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		Map<String, Object> retmap = getSysConsultPhoneServiceListInfo(doctorId, date);
		result.put("price", retmap.get("price"));
		result.put("serverType", retmap.get("serverType"));
		result.put("repeatFlag", retmap.get("repeatFlag"));
		result.put("intervalFlag", retmap.get("intervalFlag"));
		result.put("success", "success");
		result.put("beginTimeList", retmap.get("beginTimeList"));
		return result;
	}

	/**
	 * 电话咨询添加号源页面初始数据
	 */
	@Override
	public Map<String, Object> getRegisterInfo(String doctorId,String pageFlag) {
		// TODO Auto-generated method stub
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, 7 * Integer.parseInt(pageFlag));
		Date firstDay = c.getTime();
		for (int i = 0; i < 7; i++) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(firstDay);
			calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
			Date date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			int dayForWeek = 0;
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
				dayForWeek = 7;
			} else {
				dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			}
			String[] weekDays = { "一", "二", "三", "四", "五", "六", "日" };
			String date_ = DateUtils.DateToStr(date, "date");
			dateList.add(date_ + "(" + weekDays[dayForWeek - 1] + ")");
		}
		ConsulPhonetDoctorRelationVo paramVo = new ConsulPhonetDoctorRelationVo();
		paramVo.setDoctorId(doctorId);
		List<ConsulPhonetDoctorRelationVo> list = phoneConsultDoctorRelationService.getPhoneConsultDoctorRelationByInfo(paramVo);
		String phoneConsultFlag = "";
		ConsulPhonetDoctorRelationVo cvo = new ConsulPhonetDoctorRelationVo();
		if(list.size()==0){
			phoneConsultFlag = "no";
			cvo.setDoctorId(doctorId);
		}else{
			phoneConsultFlag = "yes";
		}

		Map<String, Object> retmap = getSysConsultPhoneServiceListInfo(doctorId,DateUtils.DateToStr(firstDay, "date"));

		if(list.size()==0){
			returnMap.put("consulPhonetDoctorRelationVo", cvo);
		}else{
			returnMap.put("consulPhonetDoctorRelationVo", list.get(0));
			returnMap.put("serverLength", list.get(0).getServerLength());
		}
		returnMap.put("dateList", dateList);
		returnMap.put("phoneConsultFlag", phoneConsultFlag);
		returnMap.put("serverType", retmap.get("serverType"));
		returnMap.put("success", "success");
		returnMap.put("beginTimeList", retmap.get("beginTimeList"));
		return returnMap;
	}

	/**
	 * 获取医生指定日期的号源信息
	 */
	private Map<String, Object> getSysConsultPhoneServiceListInfo(String doctorId,String date){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("doctorId", doctorId);
		paramMap.put("date", date);
		List<SysConsultPhoneServiceVo> spsvList = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(paramMap);
		List<String> beginTimeList = new ArrayList<String>();
		String time = new String();
		String serverType = "";
		for (SysConsultPhoneServiceVo rsv : spsvList) {// 1无信息2约3重4约 重
			time = DateUtils.DateToStr(rsv.getBegintime(), "time");
			if("0".equals(rsv.getState())){//没有被预约
				if("0".equals(rsv.getRepeatFlag())){//每周重复
					beginTimeList.add("3S|X" + time);
				}/*else if("1".equals(rsv.getRepeatFlag())){//隔周重复
					beginTimeList.add("S|X" + time);
				}*/else if("2".equals(rsv.getRepeatFlag())){//不重复
					beginTimeList.add("1S|X" + time);
				}
			}
			if("1".equals(rsv.getState())){//已被预约
				if("0".equals(rsv.getRepeatFlag())){
					beginTimeList.add("4S|X" + time);
				}/*else if("1".equals(rsv.getRepeatFlag())){
					beginTimeList.add("S|X" + time);
				}*/else if("2".equals(rsv.getRepeatFlag())){
					beginTimeList.add("2S|X" + time);
				}
			}
		}
		map.put("price", null);
		map.put("serverType", serverType);
		map.put("success", "success");
		map.put("beginTimeList", beginTimeList);
		return map;
	}

	/**
	 * 添加电话咨询号源
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> addRegisters(SysConsultPhoneServiceVo vo,
											List<String> timeList, String date, String operInterval) throws Exception{
		// TODO Auto-generated method stub
		synchronized (SysConsultPhoneServiceImpl.class) {
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, String> retmap = new HashMap<String, String>();
			List<String> insertDateList = new ArrayList<String>();
			List<String> statusList = new ArrayList<String>();
			statusList.add("0");
			statusList.add("1");
			map.put("sysDoctorId", vo.getSysDoctorId());
			map.put("statusList", statusList);
			Date temp = DateUtils.StrToDate(date, "date");
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
			if ("no".equals(operInterval)) {
				map.put("date", date);
			} else if ("1".equals(operInterval) || "0".equals(operInterval)) {
				insertDateList.add(date);
				for (int i = 0; i < 3; i++) {
					c.add(Calendar.DAY_OF_YEAR, 7);
					if ("1".equals(operInterval) && (i == 0 || i == 2)) {
						continue;
					}
					Date today = c.getTime();
					insertDateList.add(DateUtils.DateToStr(today, "date"));
				}
				map.put("dateList", insertDateList);
			}
			StringBuffer sb = new StringBuffer("");
			int countRepeat = 0;
			int countUsed = 0;
			int countRegister = 0;
			for (String time : timeList) {
				List<SysConsultPhoneServiceVo> insertFlagList = new ArrayList<SysConsultPhoneServiceVo>();
				List<SysConsultPhoneServiceVo> updateList = new ArrayList<SysConsultPhoneServiceVo>();
				List<String> updateDateList = new ArrayList<String>();
				map.put("time", time);
				List<SysConsultPhoneServiceVo> rsvlist = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(map);
				for (SysConsultPhoneServiceVo tvo : rsvlist) {
					insertFlagList.add(tvo);
					if ("0".equals(tvo.getState())) {
						updateDateList.add(DateUtils.DateToStr(
								tvo.getDate(), "date"));
						updateList.add(tvo);
					}
				}
				if (insertFlagList.size() == 0) {
					if ("no".equals(operInterval)) {
						vo.setRepeatFlag("2");//不重复
						insertRegister(vo, time, date);
					} else {
						vo.setRepeatFlag(operInterval);
						ConsultPhoneRegisterTemplateVo rtsv = operTemplate("get",vo, weekday, date, time,operInterval);
						if (rtsv != null) {
							if ("1".equals(rtsv.getRepeatInterval()) && "1".equals(operInterval)) {
								for (String dt : insertDateList) {
									if (!updateDateList.contains(dt)) {
										insertRegister(vo, time,
												dt);
									}
								}
								operTemplate("update", vo,
										weekday, date, time, operInterval);
							} else {
								countRepeat++;
								if ("1".equals(rtsv.getRepeatInterval())) {
									sb.append(time + "的号源已被设置隔周重复！\n");
								} else {
									sb.append(time + "的号源已被设置每周重复！\n");
								}
							}
						} else {
							for (String dt : insertDateList) {
								if (!updateDateList.contains(dt)) {
									insertRegister(vo, time, dt);
								}
							}
							operTemplate("add", vo, weekday,date, time, operInterval);
						}
					}
				} else {
					for (SysConsultPhoneServiceVo tvo : insertFlagList) {
						if ("1".equals(tvo.getState())) {
							countRegister++;
							sb.append(DateUtils.DateToStr(tvo.getDate(), "date")
									+ "("
									+ DateUtils.DateToStr(tvo.getBegintime(),
									"time") + ")的号源已被预约\n");
						} else {
							countUsed++;
							sb.append(DateUtils.DateToStr(tvo.getDate(), "date")
									+ "("
									+ DateUtils.DateToStr(tvo.getBegintime(),
									"time") + ")的号源已被占用\n");
						}
					}
				}
				if ("".equals(sb.toString())) {
					for (SysConsultPhoneServiceVo rsv : updateList) {
						update(rsv.getId(), vo.getPrice(),vo.getServicetype());
					}
				}
			}
			StringBuffer sbf = new StringBuffer("");
			if (countRepeat != 0) {
				sbf.append(countRepeat + "个号源已被设置重复<br/>");
			}
			if (countRegister != 0) {
				sbf.append(countRegister + "个号源已被预约<br/>");
			}
			if (countUsed != 0) {
				sbf.append(countUsed + "个号源已被占用<br/>");
			}
			retmap.put("doctor", sbf.toString());
			retmap.put("backend", sb.toString());
			return retmap;
		}
	}

	/**
	 * 保存电话咨询号源
	 * sunxiao
	 */
	private void insertRegister(SysConsultPhoneServiceVo registerVo,
								String time, String date) {
		Date begin_time = DateUtils.StrToDate(time, "time");
		ConsulPhonetDoctorRelationVo rvo = new ConsulPhonetDoctorRelationVo();
		rvo.setDoctorId(registerVo.getSysDoctorId());
		List<ConsulPhonetDoctorRelationVo> relationList = phoneConsultDoctorRelationService.getPhoneConsultDoctorRelationByInfo(rvo);
		if(relationList!=null){
			if(relationList.size()!=0){
				Date end_time = new Date(begin_time.getTime() + relationList.get(0).getServerLength()*60*1000);
				registerVo.setEndtime(end_time);
			}
		}
		registerVo.setDate(DateUtils.StrToDate(date, "date"));
		registerVo.setBegintime(begin_time);
		registerVo.setCreatedate(new Date());
		registerVo.setState("0");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		List<String> statusList = new ArrayList<String>();
		statusList.add("0");
		statusList.add("1");
		queryMap.put("statusList", statusList);
		queryMap.put("date", date);
		queryMap.put("time", begin_time);
		queryMap.put("sysDoctorId", registerVo.getSysDoctorId());
		List<SysConsultPhoneServiceVo> rlist = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(queryMap);
		if(rlist.size()==0){
			sysConsultPhoneServiceDao.insertSelective(registerVo);
		}
	}

	/**
	 * 修改电话咨询号源
	 * sunxiao
	 */
	private void update(Integer id, String price, String serviceType) {
		SysConsultPhoneServiceVo svo = new SysConsultPhoneServiceVo();
		svo.setId(id);
		svo.setPrice(price);
		svo.setServicetype(serviceType);
		svo.setUpdatedate(new Date());
		sysConsultPhoneServiceDao.updateByPrimaryKeySelective(svo);
	}

	/**
	 * 操作电话咨询号源模板表，用于号源的重复设置
	 * sunxiao
	 */
	private ConsultPhoneRegisterTemplateVo operTemplate(String operType,
														SysConsultPhoneServiceVo vo, String weekday, String date, String time,
														String operInterval) {
		Map<String, Object> map = new HashMap<String, Object>();
		if ("get".equals(operType)) {
			map.put("doctorId", vo.getSysDoctorId());
			map.put("weekday", weekday);
			map.put("time", time);
			List<ConsultPhoneRegisterTemplateVo> list = consultPhoneRegisterTemplateDao.getRegisterTemplateList(map);
			for (ConsultPhoneRegisterTemplateVo rtvo : list) {
				if ("1".equals(rtvo.getRepeatInterval())
						&& "1".equals(operInterval)) {
					List<String> dateList = new ArrayList<String>();
					dateList.add(date);
					Date temp = DateUtils.StrToDate(date, "date");
					Calendar c = Calendar.getInstance();
					c.setTime(temp);
					c.add(Calendar.DAY_OF_YEAR, -14);
					Date lasttoday = c.getTime();
					dateList.add(DateUtils.DateToStr(lasttoday, "date"));
					if (dateList.contains(DateUtils.DateToStr(
							rtvo.getUpdateDate(), "date"))) {
						return rtvo;
					}
				} else {
					return rtvo;
				}
			}
			return null;
		}
		if ("update".equals(operType)) {
			if (time != null && !"".equals(time)) {
				map.put("price", vo.getPrice());
				map.put("serverType", vo.getServicetype());
				map.put("doctorId", vo.getSysDoctorId());
				map.put("weekday", weekday);
				map.put("time", time);
				List<ConsultPhoneRegisterTemplateVo> list = consultPhoneRegisterTemplateDao.getRegisterTemplateList(map);
				if (list != null) {
					if (list.size() != 0) {
						if ("1".equals(list.get(0).getRepeatInterval())) {
							List<String> dateList = new ArrayList<String>();
							dateList.add(date);
							Date temp = DateUtils.StrToDate(date, "date");
							Calendar c = Calendar.getInstance();
							c.setTime(temp);
							c.add(Calendar.DAY_OF_YEAR, -14);
							Date lasttoday = c.getTime();
							dateList.add(DateUtils.DateToStr(lasttoday, "date"));
							map.put("updateDateList", dateList);
						}
					}
				}
				consultPhoneRegisterTemplateDao.updateRegisterTemplateByInfo(map);
			}
		}
		if ("add".equals(operType)) {
			map.put("doctorId", vo.getSysDoctorId());
			map.put("weekday", weekday);
			map.put("price", vo.getPrice());
			map.put("serverType", vo.getServicetype());
			map.put("status", 1);
			map.put("createDate", new Date());
			map.put("updateDate", date);
			map.put("interval", operInterval);
			map.put("time", time);
			consultPhoneRegisterTemplateDao.saveRegisterTemplate(map);
		}
		if ("del".equals(operType)) {
			if (time != null && !"".equals(time)) {
				map.put("doctorId", vo.getSysDoctorId());
				map.put("weekday", weekday);
				map.put("time", time);
				List<ConsultPhoneRegisterTemplateVo> list = consultPhoneRegisterTemplateDao
						.getRegisterTemplateList(map);
				if (list != null) {
					if (list.size() != 0) {
						if ("1".equals(list.get(0).getRepeatInterval())) {
							List<String> dateList = new ArrayList<String>();
							dateList.add(date);
							Date temp = DateUtils.StrToDate(date, "date");
							Calendar c = Calendar.getInstance();
							c.setTime(temp);
							c.add(Calendar.DAY_OF_YEAR, -14);
							Date lasttoday = c.getTime();
							dateList.add(DateUtils.DateToStr(lasttoday, "date"));
							map.put("updateDateList", dateList);
						}
					}
				}
				consultPhoneRegisterTemplateDao.deleteRegisterTemplateByinfo(map);
			}
		}
		return null;
	}

	/**
	 * 获取设置重复号源的日期
	 * sunxiao
	 */
	private Map<String, Object> getDateList(SysConsultPhoneServiceVo vo, String date,
											String time, String repeat) {
		List<String> dateList = new ArrayList<String>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		dateList.add(date);
		if ("yes".equals(repeat) && repeat != null && !"".equals(repeat)) {
			Date temp = DateUtils.StrToDate(date, "date");
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
			ConsultPhoneRegisterTemplateVo rvo = operTemplate("get", vo, weekday,
					date, time, null);
			if (rvo != null) {
				retmap.put("repeat", "yes");
				if ("0".equals(rvo.getRepeatInterval())) {// 每周重复只能查出一个
					c.setTime(temp);
					for (int i = 0; i < 6; i++) {
						c.add(Calendar.DAY_OF_YEAR, 7);
						Date today = c.getTime();
						dateList.add(DateUtils.DateToStr(today, "date"));
					}
				}
				if ("1".equals(rvo.getRepeatInterval())) {
					c.setTime(temp);
					for (int i = 0; i < 3; i++) {
						c.add(Calendar.DAY_OF_YEAR, 14);
						Date today = c.getTime();
						dateList.add(DateUtils.DateToStr(today, "date"));
					}
				}
			}else{
				c.setTime(temp);
				for (int i = 0; i < 6; i++) {
					c.add(Calendar.DAY_OF_YEAR, 7);
					Date today = c.getTime();
					dateList.add(DateUtils.DateToStr(today, "date"));
				}
			}
		}
		retmap.put("dateList", dateList);
		return retmap;
	}

	/**
	 * 删除号源时判断受影响的号源
	 * sunxiao
	 */
	@Override
	public String judgeRepeatEffect(String date, String timeParam,String doctorId, String operRepeat) {
		String[] timeArray = timeParam.split(";");
		StringBuffer sb = new StringBuffer("");
		for (String time : timeArray) {
			SysConsultPhoneServiceVo vo = new SysConsultPhoneServiceVo();
			vo.setSysDoctorId(doctorId);
			SysConsultPhoneServiceVo rvo = new SysConsultPhoneServiceVo();
			rvo.setSysDoctorId(doctorId);

			List<String> dateList = new ArrayList<String>();
			Map<String, Object> retMap = getDateList(rvo, date, time,
					operRepeat);
			dateList = (List<String>) retMap.get("dateList");
			String repeat = (String) retMap.get("repeat");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doctorId", doctorId);
			map.put("dateList", dateList);
			map.put("status", "1");
			map.put("time", time);
			List<SysConsultPhoneServiceVo> rsList = sysConsultPhoneServiceDao
					.findSysConsultPhoneByInfo(map);
			SysConsultPhoneServiceVo isRepeatvo = null;
			for (SysConsultPhoneServiceVo rsvo : rsList) {
				if (date.equals(DateUtils.DateToStr(rsvo.getDate(), "date"))) {
					if (!("0".equals(rsvo.getRepeatFlag()) || "1".equals(rsvo
							.getRepeatFlag()))) {
						isRepeatvo = rsvo;
					} else {
						if (!"yes".equals(operRepeat)) {
							isRepeatvo = rsvo;
						}
					}
				}
			}
			if (isRepeatvo != null) {
				sb.append(DateUtils.DateToStr(isRepeatvo.getDate(), "date")
						+ DateUtils.DateToStr(isRepeatvo.getBegintime(), "time")
						+ "的号源已被预约");
			} else {
				for (SysConsultPhoneServiceVo rsvo : rsList) {
					if ("0".equals(rsvo.getRepeatFlag())
							|| "1".equals(rvo.getRepeatFlag())) {
						sb.append(DateUtils.DateToStr(rsvo.getDate(), "date")
								+ DateUtils.DateToStr(rsvo.getBegintime(),
								"time") + "的号源已被预约");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 删除号源,被预约的退款
	 * sunxiao
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int deleteRegisters(SysConsultPhoneServiceVo registerServiceVo,
							   List<String> timeList, String date, String operRepeat) throws Exception{
		int count = 0;
		Random r = new Random();
		for (String time : timeList) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> dateList = new ArrayList<String>();
			Map<String, Object> retMap = getDateList(registerServiceVo, date,
					time, operRepeat);
			dateList = (List<String>) retMap.get("dateList");
			String repeat = (String) retMap.get("repeat");
			map.put("dateList", dateList);
			map.put("doctorId", registerServiceVo.getSysDoctorId());
			List<String> statusList = new ArrayList<String>();
			statusList.add("0");
			statusList.add("1");
			map.put("statusList", statusList);
			map.put("time", time);
			List<SysConsultPhoneServiceVo> rsList = sysConsultPhoneServiceDao
					.findSysConsultPhoneByInfo(map);
			SysConsultPhoneServiceVo svo = null;
			for (SysConsultPhoneServiceVo rvo : rsList) {
				if (date.equals(DateUtils.DateToStr(rvo.getDate(), "date"))) {//当前号源不重复或不重复删除时只删除当前号源
					if ("2".equals(rvo.getRepeatFlag())) {
						svo = rvo;
					} else {
						if ("no".equals(operRepeat)) {
							svo = rvo;
						}
					}
				}
			}
			if (svo != null) {
				delRegister(svo);
			} else {
				for (SysConsultPhoneServiceVo rvo : rsList) {
					if ("0".equals(rvo.getRepeatFlag())	|| "1".equals(rvo.getRepeatFlag())) {
						delRegister(rvo);
					}
				}
			}
			if ("yes".equals(operRepeat) && "yes".equals(repeat)
					&& svo == null) {//删除模板
				Date temp = DateUtils.StrToDate(date, "date");
				Calendar c = Calendar.getInstance();
				c.setTime(temp);
				String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
				operTemplate("del", registerServiceVo, weekday, date, time,
						null);
			}
		}
		return count;
	}

	private Float delRegister(SysConsultPhoneServiceVo vo) throws Exception {
		Float price = 0f;
		try {
			if ("1".equals(vo.getState())) {//被预约了删除号源，删除订单，退费
				HashMap<String, Object> executeMap1 = new HashMap<String, Object>();
				executeMap1.put("registerId", vo.getId());
				List<Map<String, Object>> orderList = consultPhonePatientService.getConsultPhoneRegisterListByInfo(executeMap1);
				Integer orderId = (Integer) orderList.get(0).get("id");
				price = consultPhonePatientService.cancelOrder(orderId, "运维" + UserUtils.getUser().getName() + "删除", "2");//删除订单，修改号源状态，退款
			}else if("0".equals(vo.getState())){//没被预约只删除号源
				sysConsultPhoneServiceDao.cancelOrder(vo.getId(),"2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		return price;
	}
	/**
	 * 获取电话咨询模板列表
	 * sunxiao
	 * @param executeMap
	 * @return
	 */
	@Override
	public List<ConsultPhoneRegisterTemplateVo> getRegisterTemplateList(Map<String, Object> executeMap) {
		// TODO Auto-generated method stub
		return consultPhoneRegisterTemplateDao.getRegisterTemplateList(executeMap);
	}

	/**
	 * 根据条件查询电话咨询信息
	 * sunxiao
	 */
	@Override
	public List<SysConsultPhoneServiceVo> findSysConsultPhoneByInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<SysConsultPhoneServiceVo> list = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(map);
		return list;
	}

    @Override
    public HashMap<String, Object> findSysConsultPhoneServiceByCRSIdExecute(HashMap<String, Object> hashMap) {
        return sysConsultPhoneServiceDao.findSysConsultPhoneServiceByCRSIdExecute(hashMap);
    }

    @Override
    public Map<String, Object> getSysPhoneConsultInfo(Integer id) {
        SysConsultPhoneServiceVo vo = sysConsultPhoneServiceDao.selectByPrimaryKey(id);
        Map<String, Object> response = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        if(vo != null){
            response.put("consultDate", format.format(vo.getDate())+format1.format(vo.getBegintime())+
                    format1.format(vo.getEndtime()));
        }

        return response;
    }



}
