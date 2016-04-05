package com.cxqm.xiaoerke.modules.order.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterTemplateDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterTemplateVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;

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
	private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

    public Map<String, Object> getDoctorConsultDate(HashMap<String,Object> dataMap) {

        Map<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> resultList =  sysConsultPhoneServiceDao.getConsultDateList(dataMap);

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

        // 记录日志 TODO：
//		LogUtils.saveLog(Servlets.getRequest(), "00000030" ,"医生主键："+ doctorId
//				+ "date:" + date);
        DateFormat formart = new SimpleDateFormat("hh:mm");
        List<HashMap<String, Object>> consultPhoneTimeList = new LinkedList<HashMap<String, Object>>();
        if(resultList != null && !resultList.isEmpty()){
            for(HashMap<String, Object> map:resultList){
                HashMap<String, Object> consultPhoneTime = new HashMap<String, Object>();
                consultPhoneTime.put("price", (String) map.get("price"));
                consultPhoneTime.put("id",(Integer)map.get("id"));
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
		
		returnMap.put("consulPhonetDoctorRelationVo", list.size()==0?cvo:list.get(0));
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
		paramMap.put("date", new Date());
		List<SysConsultPhoneServiceVo> spsvList = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(paramMap);
		List<String> beginTimeList = new ArrayList<String>();
		String time = new String();
		String serverType = "";
		for (SysConsultPhoneServiceVo rsv : spsvList) {// 1无信息2约3重4约 重
			time = DateUtils.DateToStr(rsv.getBegintime(), "time");
			if("0".equals(rsv.getState())){//没有被预约
				if("0".equals(rsv.getRepeatFlag())){//不重复
					beginTimeList.add("1S|X" + time);
				}else if("1".equals(rsv.getRepeatFlag())){//每周重复
					beginTimeList.add("3S|X" + time);
				}
			}
			if("1".equals(rsv.getState())){//已被预约
				if("0".equals(rsv.getRepeatFlag())){
					beginTimeList.add("2S|X" + time);
				}else if("1".equals(rsv.getRepeatFlag())){
					beginTimeList.add("4S|X" + time);
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
	public Map<String, String> addRegister(SysConsultPhoneServiceVo vo,
			List<String> timeList, String date, String operInterval) {
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
		// 结束时间自动加15分钟
		Date end_time = new Date(begin_time.getTime() + 900000);
		registerVo.setDate(DateUtils.StrToDate(date, "date"));
		registerVo.setBegintime(begin_time);
		registerVo.setEndtime(end_time);
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
     * 获取电话咨询模板列表
     * sunxiao
     * @param executeMap
     * @return
     */
	@Override
	public List<ConsultPhoneRegisterTemplateVo> getRegisterTemplateList(
			Map<String, Object> executeMap) {
		// TODO Auto-generated method stub
		return consultPhoneRegisterTemplateDao.getRegisterTemplateList(executeMap);
	}

	/**
	 * 根据条件查询电话咨询信息
	 * sunxiao
	 */
	@Override
	public List<SysConsultPhoneServiceVo> findSysConsultPhoneByInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<SysConsultPhoneServiceVo> list = sysConsultPhoneServiceDao.findSysConsultPhoneByInfo(map);
		return list;
	}
}
