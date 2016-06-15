package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.RegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.RegisterTemplateServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterTemplateServiceVo;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sunxiao
 * 2015/7/30.
 */
@Service
@Transactional(readOnly = false)
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private IllnessInfoService illnessInfoService;

	@Autowired
	private RegisterServiceDao registerServiceDao;

	@Autowired
	private RegisterTemplateServiceDao registerTemplateServiceDao;

	@Autowired
	private PatientRegisterServiceDao patientRegisterServiceDao;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private HospitalInfoService hospitalInfoService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private DoctorInfoService doctorInfoService;
	
	@Autowired
	private DoctorLocationService doctorLocationService;
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	@Autowired
	private MemberService memberService;

//	@Autowired
//	private PatientRegisterService patientRegisterService;

   //根据sys_doctor_id查询它的最近可约时间
	@Override
    public HashMap<String, Object> findRecentlyDateAppDate(String doctorId) {
        HashMap<String, Object> doctorMap = new HashMap<String, Object>();
        doctorMap.put("doctorId", doctorId);
        HashMap<String, Object> resultMap = registerServiceDao.findrecentlyDateAppDateExcute(doctorMap);
        return resultMap;
    }

    public List<HashMap<String, Object>> getDoctorVisitInfoByLocation(Map data) {
        return registerServiceDao.getDoctorVisitInfoByLocation(data);
    }


	@Override
	public Map<String, Object> listAppointmentTimeHospital(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Date date = DateUtils.formatDate(params);
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");
		String consultPhone = (String) params.get("consultPhone");

		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(
				currentPage, pageSize);
		HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
		hospitalInfo.put("orderBy", orderBy);
		hospitalInfo.put("date", date);
		hospitalInfo.put("consultPhone", consultPhone);

		if(StringUtils.isNotNull(consultPhone)){
			List<HashMap<String, Object>> list = hospitalInfoService.findConsultHospitalByTime(date);
			response.put("hospitalData", list);
		}else{
			Page<HashMap<String, Object>> resultPage = hospitalInfoService
					.findPageHospitalByTime(hospitalInfo, page);
			// 记录日志
			LogUtils.saveLog(Servlets.getRequest(), "00000034" ,"date:"+ date);//获取某个预约日期下的可预约的医院
			response.put("pageNo", resultPage.getPageNo());
			response.put("pageSize", resultPage.getPageSize());
			long tmp = FrontUtils.generatorTotalPage(resultPage);
			response.put("pageTotal", tmp + "");
			List<HashMap<String, Object>> list = resultPage.getList();
			List<HashMap<String, Object>> hospitalVoList = new ArrayList<HashMap<String, Object>>();
			if (list != null && !list.isEmpty()) {
				for (Map hospitalVoMap : list) {
					HashMap<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("hospitalId", hospitalVoMap.get("id"));
					dataMap.put("hospitalName", hospitalVoMap.get("name"));
					dataMap.put("hospitalLocation", hospitalVoMap.get("position"));
					hospitalVoList.add(dataMap);
				}
			}
			response.put("hospitalData", hospitalVoList);
		}

		return response;
	}

	@Override
	public Map<String, Object> listAppointmentTimeDoctor(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Date date = DateUtils.formatDate(params);
		String currentPage = (String) params.get("pageNo");
		String pageSize = (String) params.get("pageSize");
		String orderBy = (String) params.get("orderBy");
		String hospitalId = (String) params.get("hospitalId");
		String department_level1 = (String) params.get("department_level1");
		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(
				currentPage, pageSize);
		HashMap<String, Object> dateInfo = new HashMap<String, Object>();
		dateInfo.put("orderBy", orderBy);
		dateInfo.put("date", date);
		dateInfo.put("hospitalId", hospitalId);
		dateInfo.put("department_level1", department_level1);
		Page<HashMap<String, Object>> resultPage = doctorInfoService.findPageDoctorByTime(dateInfo, page);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000032","date:" + date
				+ "医院" + hospitalId);//获取某个预约日期下的可预约医生
		String sort = "";
		if (orderBy == "0") {
			sort = "时间最近排序";
		} else if (orderBy == "1") {
			sort = "粉丝最多排序";
		} else if (orderBy == "1") {
			sort = "从业时间排序";
		}
		LogUtils.saveLog(Servlets.getRequest(), "00000033","根据" + sort + "获取某个预约日期下的可预约医生");//排序获取某个预约日期下的可预约医生

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
		generateDoctorDataVoListByDate(list, doctorDataVoList, date);
		response.put("doctorDataVo", doctorDataVoList);

		return response;
	}

	@Override
	public Map<String, Object> listAppointmentTimeHospitalDoctor(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Date date = DateUtils.formatDate(params);
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");
		String hospitalId = (String) params.get("hospitalId");

		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(
				currentPage, pageSize);
		HashMap<String, Object> dateInfo = new HashMap<String, Object>();
		dateInfo.put("orderBy", orderBy);
		dateInfo.put("date", date);
		dateInfo.put("hospitalId", hospitalId);

		Page<HashMap<String, Object>> resultPage = doctorInfoService
				.findPageDoctorByTimeAndHospital(dateInfo, page);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000035"
				,"医院id:"+ hospitalId);//获取某个预约日期下的可预约的医院下的医生

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
		generateDoctorDataVoList(list, doctorDataVoList);
		response.put("doctorDataVo", doctorDataVoList);

		return response;
	}

	@Override
	public Map<String, Object> doctorAppointmentInfoOfDay(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();

		String doctorId = (String) params.get("doctorId");
		// 根据日期得到周几
		Date date = DateUtils.formatDate(params);
		String DateToStr = DateUtils.DateToStr(date);
		int week = 0;
		try {
			week = DateUtils.dayForWeek(DateToStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String dayWeek = DateUtils.getDayWeek(week);

		HashMap<String, Object> dataInfo = new HashMap<String, Object>();
		dataInfo.put("doctorId", doctorId);
		dataInfo.put("date", date);
		dataInfo.put("location_id", params.get("location_id"));
		StringBuffer bf = new StringBuffer();
		bf.append(DateToStr.substring(0, 10));
		bf.append("  " + dayWeek).toString();
		response.put("date", bf);

		dataInfo.put("user","user");
		List<HashMap<String, Object>> appointmentVoList = registerServiceDao.findAllAppointmentInfoByDoctor(dataInfo);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000030" ,"医生主键："+ doctorId
				+ "date:" + date);

		HashMap<String, Object> dataMapInfo = new HashMap<String, Object>();
		List<HashMap<String, Object>> dataMapList = new ArrayList<HashMap<String, Object>>();
		for (Map appointmentMap : appointmentVoList) {
			String hospitalId = (String) appointmentMap.get("sys_hospital_id");
			dataMapInfo.put(hospitalId, hospitalId);
		}
		for (int i = 0; i < dataMapInfo.size(); i++) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> available_time = new ArrayList<HashMap<String, Object>>();
			for (Map appointmentMap : appointmentVoList) {
				HashMap<String, Object> timeMap = new HashMap<String, Object>();
				dataMap.put("hospitalId", appointmentMap.get("sys_hospital_id"));
				dataMap.put("location", appointmentMap.get("location"));
				dataMap.put("price", appointmentMap.get("price"));
				dataMap.put("service_type", appointmentMap.get("service_type"));
				timeMap.put("register_service_id", appointmentMap.get("id"));
				timeMap.put("begin_time", appointmentMap.get("begin_time"));
				timeMap.put("end_time", appointmentMap.get("end_time"));
				timeMap.put("status", appointmentMap.get("status"));
				available_time.add(timeMap);
				dataMap.put("available_time", available_time);
			}
			dataMapList.add(dataMap);
		}
		response.put("appointmentList", dataMapList);
		return response;
	}

	@Override
	public Map<String, Object> doctorAppointmentInfoDetailOfDay(Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> perInfo = null;
		String doctorId = (String) params.get("doctorId");
		String patient_register_service_id = (String) params
				.get("patient_register_service_id");
		String sys_register_service_id = (String) params
				.get("register_service_id");
		String date = (String) params.get("date");
		String begin_time = (String) params.get("begin_time");
		String end_time = (String) params.get("end_time");
		HashMap<String, Object> appointmentInfo = new HashMap<String, Object>();
		appointmentInfo.put("doctorId", doctorId);
		appointmentInfo.put("patient_register_service_id",
				patient_register_service_id);
		appointmentInfo.put("date", date);
		appointmentInfo.put("begin_time", begin_time);
		appointmentInfo.put("end_time", end_time);
		appointmentInfo.put("sys_register_service_id", sys_register_service_id);

		// 根据用户id获取当前用户的所有信息
		String userId = UserUtils.getUser().getId();
		if (userId != null && !"".equals(userId)) {
			perInfo = userInfoService.findPersonDetailInfoByUserId(userId);
			response.put("phone", perInfo.get("phone"));
		}

		List<HashMap<String, Object>> list = doctorInfoService
				.findAppointmentInfoDetail(appointmentInfo);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000029" ,"doctorId:"+ doctorId
				+ "sys_register_service_id:" + sys_register_service_id);

		for (Map appointmentData : list) {
			response.put("location", appointmentData.get("location"));
			response.put("price", appointmentData.get("price"));
			response.put("deposit", appointmentData.get("deposit"));
			response.put("service_type", appointmentData.get("service_type"));
			response.put("location", appointmentData.get("location"));
			response.put("root", appointmentData.get("root"));
			response.put("begin_time", appointmentData.get("begin_time"));
			response.put("end_time", appointmentData.get("end_time"));
		}
		return response;
	}

	@Override
	public Map<String, Object> orderSourceRoute(Map<String, Object> params) {
		HashMap<String, Object> sourceRouteMap = new HashMap<String, Object>();

		String register_service_id = (String) params.get("register_service_id");
		if(register_service_id == null)
			throw new RuntimeException("Parameter register_service_id cannot be null.");
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("registerId", register_service_id);
		// 查询所需要的信息
		
			
		hashMap = registerServiceDao.getSysRegisterServiceInfo(hashMap);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(),"00000036", "号源id:"
				+ register_service_id);//搜索号源的交通信息

		// 组合数据
		HashMap<String, Object> response = new HashMap<String, Object>();
		String location = hashMap.get("position") + "  "
				+ hashMap.get("hospitalName") + "  " + hashMap.get("location");
		response.put("location", location);
		String[] strings = hashMap.get("root").toString().split("；");
		String traffic = strings[0];
		response.put("traffic", traffic);
		response.put("internal_route", strings[1]);
		response.put("shot_message", strings[2]);
		String car = traffic.substring(traffic.indexOf("停车") + 2,
				traffic.indexOf("、地铁") - 1);
		String subway = traffic.substring(traffic.indexOf("、地铁") + 3,
				traffic.indexOf("、公交") - 1);
		String bus = traffic.substring(traffic.indexOf("、公交") + 3);
		response.put("car", car);
		response.put("subway", subway);
		response.put("bus", bus);
		sourceRouteMap.put("root", response);
		return sourceRouteMap;
	}

	/**
	 * 初始化添加号源页面查询数据
	 * sunxiao
	 */
	public Map<String, Object> returnRegisterInfo(String doctorId, String pageFlag) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<DoctorLocationVo> dlvlist = doctorLocationService.getDoctorLocationInfo(doctorId);
		if (dlvlist != null && dlvlist.size() != 0) {
			DoctorLocationVo doctorLocationVo = dlvlist.get(0);
			RegisterServiceVo vo = new RegisterServiceVo();
			vo.setSysDoctorId(doctorLocationVo.getSysDoctorId());
			vo.setSysHospitalId(doctorLocationVo.getSysHospitalId());
			vo.setDoctorName(doctorLocationVo.getDoctorName());
			vo.setHospitalName(doctorLocationVo.getHospital());
			vo.setLocation(doctorLocationVo.getLocation());
			vo.setLocationId(doctorLocationVo.getId());
			List<String> dateList = new ArrayList<String>();
			String weekday = "";
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_YEAR, 7 * Integer.parseInt(pageFlag));
			Date firstDay = c.getTime();
			for (int i = 0; i < 7; i++) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(firstDay);
				if (i == 0) {
					weekday = calendar.get(Calendar.DAY_OF_WEEK) + "";
				}
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
			Map<String, Object> retmap = compareRegisterTime(doctorId,
					DateUtils.DateToStr(firstDay, "date"), vo.getLocationId());
			vo.setPrice((Float) retmap.get("price"));
			vo.setServiceType((String) retmap.get("serverType"));
			retMap.put("dateList", dateList);
			retMap.put("locationList", dlvlist);
			retMap.put("registerServiceVo", vo);
			retMap.put("repeatFlag", retmap.get("repeatFlag"));
			retMap.put("intervalFlag", retmap.get("intervalFlag"));
			retMap.put("timeList", retmap.get("timeList"));
			retMap.put("distimeList", retmap.get("distimeList"));
			return retMap;
		}
		return null;
	}

	/**
	 * 添加号源页面切换日期和地点时查询号源
	 * sunxiao
	 */
	public JSONObject getRegisterTime(String doctorId, String date,
			String locationId) {
		JSONObject result = new JSONObject();
		Map<String, Object> retmap = compareRegisterTime(doctorId, date,
				locationId);
		result.put("price", retmap.get("price"));
		result.put("serverType", retmap.get("serverType"));
		result.put("repeatFlag", retmap.get("repeatFlag"));
		result.put("intervalFlag", retmap.get("intervalFlag"));
		result.put("success", "success");
		result.put("timeList", retmap.get("timeList"));
		result.put("distimeList", retmap.get("distimeList"));
		return result;
	}

	/**
	 * 删除号源时判断受影响的号源
	 * sunxiao
	 */
	public String judgeRepeatEffect(String date, String timeParam,
			String locationId, String doctorId, String operRepeat) {
		String[] timeArray = timeParam.split(";");
		StringBuffer sb = new StringBuffer("");
		for (String time : timeArray) {
			RegisterServiceVo vo = new RegisterServiceVo();
			vo.setLocationId(locationId);
			vo.setSysDoctorId(doctorId);
			RegisterServiceVo rvo = new RegisterServiceVo();
			rvo.setSysDoctorId(doctorId);
			rvo.setLocationId(locationId);

			List<String> dateList = new ArrayList<String>();
			Map<String, Object> retMap = getDateList(rvo, date, time,
					operRepeat);
			dateList = (List<String>) retMap.get("dateList");
			String repeat = (String) retMap.get("repeat");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sysDoctorId", doctorId);
			map.put("locationId", locationId);
			map.put("dateList", dateList);
			map.put("status", "1");
			map.put("time", time);
			List<RegisterServiceVo> rsList = registerServiceDao
					.getRegisterListByInfo(map);
			RegisterServiceVo isRepeatvo = null;
			for (RegisterServiceVo rsvo : rsList) {
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
						+ DateUtils.DateToStr(isRepeatvo.getBeginTime(), "time")
						+ "的号源已被预约");
			} else {
				for (RegisterServiceVo rsvo : rsList) {
					if ("0".equals(rsvo.getRepeatFlag())
							|| "1".equals(rvo.getRepeatFlag())) {
						sb.append(DateUtils.DateToStr(rsvo.getDate(), "date")
								+ DateUtils.DateToStr(rsvo.getBeginTime(),
										"time") + "的号源已被预约");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 号源列表
	 * sunxiao
	 */
	public Map findRegisterList(Page<RegisterServiceVo> page,
			RegisterServiceVo registerServiceVo) {
		// 设置分页参数
		registerServiceVo.setStatus("0");
		Page<RegisterServiceVo> pageList = registerServiceDao.findRegisterList(
				page, registerServiceVo);
		Map map = new HashMap<String, Object>();
		for (RegisterServiceVo temp : pageList.getList()) {
			if("0".equals(temp.getRelationType())){
				temp.setRelationType("公立医院");
			}else if("1".equals(temp.getRelationType())){
				temp.setRelationType("公立医院");
			}else if("2".equals(temp.getRelationType())){
				temp.setRelationType("合作机构");
			}
		}
		List<String> illLevel1List = new ArrayList<String>();
		List<IllnessVo> p = illnessInfoService.getFirstIllnessList();
		if (p != null) {
			for (IllnessVo vo : p) {
				illLevel1List.add(vo.getLevel_1());
			}
		}
		Page<HashMap<String, Object>> hpage = hospitalInfoService
				.findPageAllHospital(new Page<HashMap<String, Object>>());
		List<HashMap<String, Object>> tempList = hpage.getList();
		List<HospitalVo> hospitalList = new ArrayList<HospitalVo>();
		for (HashMap<String, Object> tempMap : tempList) {
			HospitalVo vo = new HospitalVo();
			vo.setId((String) tempMap.get("id"));
			vo.setName((String) tempMap.get("name"));
			hospitalList.add(vo);
		}
		map.put("illLevel1List", illLevel1List);
		map.put("hospital", hospitalList);
		map.put("page", pageList);
		return map;
	}

	/**
	 * 即将没有的号源列表
	 * sunxiao
	 */
	public Page<RegisterServiceVo> findWillNoRegisterList(
			Page<RegisterServiceVo> page) {
		Page<RegisterServiceVo> pageList = registerServiceDao
				.findWillNoRegisterList(page);
		return pageList;
	}

	/**
	 * 根据一些信息查询号源
	 * sunxiao
	 */
	public RegisterServiceVo getRegisterByInfo(String register) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sysRegisterId", register);
		RegisterServiceVo vo = registerServiceDao.getRegisterByInfo(map);
		return vo;
	}

	/**
	 * 添加号源
	 * sunxiao
	 */
	public Map<String, String> addRegister(RegisterServiceVo registerServiceVo,
			List<String> timeList, String date, String operInterval) {
		synchronized (RegisterServiceImpl.class) {
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, String> retmap = new HashMap<String, String>();
			List<String> insertDateList = new ArrayList<String>();
			List<String> statusList = new ArrayList<String>();
			statusList.add("0");
			statusList.add("1");
			map.put("sysDoctorId", registerServiceVo.getSysDoctorId());
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
				List<RegisterServiceVo> insertFlagList = new ArrayList<RegisterServiceVo>();
				List<RegisterServiceVo> updateList = new ArrayList<RegisterServiceVo>();
				List<String> updateDateList = new ArrayList<String>();
				map.put("time", time);
				List<RegisterServiceVo> rsvlist = registerServiceDao
						.getRegisterListByInfo(map);
				for (RegisterServiceVo vo : rsvlist) {
					insertFlagList.add(vo);
					if (vo.getLocationId().equals(
							registerServiceVo.getLocationId())) {
						if ("0".equals(vo.getStatus())) {
							updateDateList.add(DateUtils.DateToStr(
									vo.getDate(), "date"));
							updateList.add(vo);
						}
					}
				}
				if (insertFlagList.size() == 0) {
					if ("no".equals(operInterval)) {
						registerServiceVo.setRepeatFlag("2");
						insertRegister(registerServiceVo, time, date);
					} else {
						registerServiceVo.setRepeatFlag(operInterval);
						RegisterTemplateServiceVo rtsv = operTemplate("get",
								registerServiceVo, weekday, date, time,
								operInterval);
						if (rtsv != null) {
							if ("1".equals(rtsv.getRepeatInterval())
									&& "1".equals(operInterval)) {
								for (String dt : insertDateList) {
									if (!updateDateList.contains(dt)) {
										insertRegister(registerServiceVo, time,
												dt);
									}
								}
								operTemplate("update", registerServiceVo,
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
									insertRegister(registerServiceVo, time, dt);
								}
							}
							operTemplate("add", registerServiceVo, weekday,
									date, time, operInterval);
						}
					}
				} else {
					for (RegisterServiceVo vo : insertFlagList) {
						if ("1".equals(vo.getStatus())) {
							countRegister++;
							sb.append(DateUtils.DateToStr(vo.getDate(), "date")
									+ "("
									+ DateUtils.DateToStr(vo.getBeginTime(),
											"time") + ")的号源已被预约\n");
						} else {
							countUsed++;
							sb.append(DateUtils.DateToStr(vo.getDate(), "date")
									+ "("
									+ DateUtils.DateToStr(vo.getBeginTime(),
											"time") + ")的号源已被占用\n");
						}
					}
				}
				if ("".equals(sb.toString())) {
					for (RegisterServiceVo rsv : updateList) {
						update(rsv.getId(), registerServiceVo.getPrice(),
								registerServiceVo.getServiceType());
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
	 * 更新号源
	 * sunxiao
	 */
	public void updateRegister(RegisterServiceVo registerServiceVo,
			String time, String date, String operRepeat) {
		Map<String, Object> retMap = getDateList(registerServiceVo, date, time,
				operRepeat);
		List<String> insertDateList = (List<String>) retMap.get("dateList");
		String repeat = (String) retMap.get("repeat");
		if ("yes".equals(operRepeat) && "yes".equals(repeat)) {
			Date temp = DateUtils.StrToDate(date, "date");
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<String> statusList = new ArrayList<String>();
			statusList.add("0");
			statusList.add("1");
			map.put("sysDoctorId", registerServiceVo.getSysDoctorId());
			map.put("statusList", statusList);
			map.put("time", time);
			map.put("dateList", insertDateList);
			List<RegisterServiceVo> rsvlist = registerServiceDao
					.getRegisterListByInfo(map);
			for (RegisterServiceVo vo : rsvlist) {
				update(vo.getId(), registerServiceVo.getPrice(),
						registerServiceVo.getServiceType());
			}
			operTemplate("update", registerServiceVo, weekday, date, time, null);
		} else {
			update(registerServiceVo.getId(), registerServiceVo.getPrice(),
					registerServiceVo.getServiceType());
		}
	}

	/**
	 * 删除号源
	 * sunxiao
	 */
	public int deleteRegisters(RegisterServiceVo registerServiceVo,
			List<String> timeList, String date, String operRepeat,String deleteBy) {
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
			map.put("sysDoctorId", registerServiceVo.getSysDoctorId());
			map.put("locationId", registerServiceVo.getLocationId());
			List<String> statusList = new ArrayList<String>();
			statusList.add("0");
			statusList.add("1");
			map.put("statusList", statusList);
			map.put("time", time);
			List<RegisterServiceVo> rsList = registerServiceDao
					.getRegisterListByInfo(map);
			RegisterServiceVo isRepeatvo = null;
			for (RegisterServiceVo rvo : rsList) {
				if (date.equals(DateUtils.DateToStr(rvo.getDate(), "date"))) {
					if (!("0".equals(rvo.getRepeatFlag()) || "1".equals(rvo
							.getRepeatFlag()))) {
						isRepeatvo = rvo;
					} else {
						if (!"yes".equals(operRepeat)) {
							isRepeatvo = rvo;
						}
					}
				}
			}
			if (isRepeatvo != null) {
				Map<String, Object> executeMap = new HashMap<String, Object>();
				if ("1".equals(isRepeatvo.getStatus())) {
					HashMap<String, Object> executeMap1 = new HashMap<String, Object>();
					executeMap1.put("registerId", isRepeatvo.getId());
					HashMap<String, Object> patientRegister = patientRegisterServiceDao
							.getPatientRegisterInfo(executeMap1);
					String patientRegisterId = (String) patientRegister
							.get("id");
					executeMap.put("id", patientRegisterId);
					executeMap.put("keep", "1");
					executeMap.put("reason", "医生停诊");
					executeMap.put("deleteBy", deleteBy);
					patientRegisterServiceDao.updatePatientRegisterStatusCancelById(executeMap);
					PatientRegisterServiceVo pVo = new PatientRegisterServiceVo();
					DoctorHospitalRelationVo dhVo = new DoctorHospitalRelationVo();
					pVo.setId(patientRegisterId);
					dhVo = patientRegisterServiceDao.getRegisterAttributeOfHospital(pVo);
					String relationType = pVo.getRelationType();
					if(!relationType.equals("2")) {
						memberService.updateMemberLeftTimes(patientRegister.get("member_service_id")+"","1");
					}
					LogUtils.saveLog(Servlets.getRequest(), "00000027" ,"订单主键"+ patientRegisterId
							+ ":" + date);//删除号源时删除订单，订单id:
					try {
						executeMap.put("patient_register_service_id",
								patientRegisterId);
						orderMessageService.sendMessage(executeMap, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					insertNotification(patientRegisterId);
				}
				Calendar calendar = Calendar.getInstance();    
			    calendar.setTime(isRepeatvo.getBeginTime());    
			    calendar.add(Calendar.SECOND, r.nextInt(58)+1); 
				executeMap.put("registerId", isRepeatvo.getId());
				executeMap.put("updateDate", new Date());
				executeMap.put("beginTime", calendar.getTime());
				count += registerServiceDao
						.updateSysRegisterStatusCancel(executeMap);
				LogUtils.saveLog(Servlets.getRequest(), "00000028" ,"订单主键"+ isRepeatvo.getId()
						+ ":" + date);
			} else {
				for (RegisterServiceVo rvo : rsList) {
					if ("0".equals(rvo.getRepeatFlag())
							|| "1".equals(rvo.getRepeatFlag())) {
						Map<String, Object> executeMap = new HashMap<String, Object>();
						if ("1".equals(rvo.getStatus())) {
							HashMap<String, Object> executeMap1 = new HashMap<String, Object>();
							executeMap1.put("registerId", rvo.getId());
							HashMap<String, Object> patientRegister = patientRegisterServiceDao
									.getPatientRegisterInfo(executeMap1);
							String patientRegisterId = (String) patientRegister
									.get("id");
							executeMap.put("id", patientRegisterId);
							executeMap.put("keep", "1");
							executeMap.put("reason", "医生停诊");
							executeMap.put("deleteBy", deleteBy);
							patientRegisterServiceDao.updatePatientRegisterStatusCancelById(executeMap);
							PatientRegisterServiceVo pVo = new PatientRegisterServiceVo();
							DoctorHospitalRelationVo dhVo = new DoctorHospitalRelationVo();
							pVo.setId(patientRegisterId);
							dhVo = patientRegisterServiceDao.getRegisterAttributeOfHospital(pVo);
							String relationType = dhVo.getRelationType();
							if(!relationType.equals("2")) {
								memberService.updateMemberLeftTimes((String) patientRegister.get("member_service_id"), "1");
							}
							LogUtils.saveLog(Servlets.getRequest(), "00000027" ,"订单主键"+ patientRegisterId
									+ ":" + date);
							try {
								executeMap.put("patient_register_service_id",
										patientRegisterId);
								orderMessageService.sendMessage(executeMap, false);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							insertNotification(patientRegisterId);
						}
						Calendar calendar = Calendar.getInstance();    
        			    calendar.setTime(rvo.getBeginTime());    
        			    calendar.add(Calendar.SECOND, r.nextInt(58)+1);
						executeMap.put("registerId", rvo.getId());
						executeMap.put("updateDate", new Date());
						executeMap.put("beginTime", calendar.getTime());
						count += registerServiceDao
								.updateSysRegisterStatusCancel(executeMap);
						LogUtils.saveLog(Servlets.getRequest(), "00000028","订单表主键" + rvo.getId()
								+ ":" + date);//删除号源，号源
					}
				}
			}
			if ("yes".equals(operRepeat) && "yes".equals(repeat)
					&& isRepeatvo == null) {
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

	/**
	 * 保存号源
	 * sunxiao
	 */
	@Override
	public void insertRegister(RegisterServiceVo registerServiceVo,
			String time, String date) {
		HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
		Date begin_time = DateUtils.StrToDate(time, "time");
		// 结束时间自动加15分钟
		Date end_time = new Date(begin_time.getTime() + 900000);
		doctorInfo.put("id", IdGen.uuid());
		doctorInfo.put("doctorId", registerServiceVo.getSysDoctorId());
		doctorInfo.put("sys_hospital_id", registerServiceVo.getSysHospitalId());
		doctorInfo.put("date", date);
		doctorInfo.put("begin_time", begin_time);
		doctorInfo.put("end_time", end_time);
		doctorInfo.put("price", registerServiceVo.getPrice());
		doctorInfo.put("deposit", "50");// 所需押金
		doctorInfo.put("service_type", registerServiceVo.getServiceType());// 服务类型
		doctorInfo.put("create_date", new Date());
		doctorInfo.put("locationId", registerServiceVo.getLocationId());
		doctorInfo.put("status", "0");
		doctorInfo.put("repeatFlag", registerServiceVo.getRepeatFlag());
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		List<String> statusList = new ArrayList<String>();
    	statusList.add("0");
    	statusList.add("1");
		queryMap.put("statusList", statusList);
		queryMap.put("date", date);
		queryMap.put("time", begin_time);
		queryMap.put("sysDoctorId", registerServiceVo.getSysDoctorId());
		List<RegisterServiceVo> rlist = registerServiceDao.getRegisterListByInfo(queryMap);
		if(rlist.size()==0){
			registerServiceDao.insertSysRegisterServiceExecute(doctorInfo);
		}
	}

	/**
	 * 获取号源信息，用于回访信息
	 * sunxiao
	 */
	@Override
	public HashMap<String, Object> getRegisterServiceInfo(String registerId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("registerId", registerId);
		map = registerServiceDao.getSysRegisterServiceInfo(map);
		return map;
	}

	@Override
	public  Date getOrderCreateDate(String patientRegistId){
		Map<String,Object> executeMap = new HashMap<String, Object>();
		executeMap.put("patientRegisterService", patientRegistId);
		List<PatientRegisterServiceVo> patientList = patientRegisterServiceDao.findPageRegisterServiceByPatient(executeMap);
		if(patientList.size()>0){
			Date orderCreateDate =   patientList.get(0).getCreateDate();
			return orderCreateDate;
		}
		return null;
	}

	/**
	 * 删除号源
	 * sunxiao
	 */
	public void deleteRegister(String id) {
		Random r = new Random();
		Map<String,Object> map = new HashMap<String, Object>();
        map.put("id", id);
        RegisterServiceVo vo = registerServiceDao.getRegisterById(map);
    	Map<String, Object> excute = new HashMap<String, Object>();
    	Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(vo.getBeginTime());    
	    calendar.add(Calendar.SECOND, r.nextInt(58)+1);
		excute.put("registerId", id);
		excute.put("updateDate", new Date());
		excute.put("beginTime", calendar.getTime());
		registerServiceDao.updateSysRegisterStatusCancel(excute);
	}

	/**
	 * 获取已经设置重复的号源信息
	 * sunxiao
	 */
	public List<RegisterTemplateServiceVo> getRegisterTemplate(
			RegisterTemplateServiceVo registerTemplate) {
		return registerTemplateServiceDao.getRegisterTemplate(registerTemplate);
	}

	public List<Date> getDatesWithRegisters(String doctorId, String locationId,
			Date from, Integer days) {
		Map<String, Object> params = new HashMap<String, Object>(8);
		params.put("doctorId", doctorId);
		params.put("locationId", locationId);
		params.put("from", DateUtils.DateToStr(from, "date"));
		Calendar c = Calendar.getInstance();
		c.setTime(from);
		c.add(Calendar.DATE, 7);
		params.put("to", DateUtils.DateToStr(c.getTime(), "date"));
		List<Date> dates = registerServiceDao.findDatesWithRegisters(params);
		return dates;
	}

	/**
	 * 根据一些信息获取号源列表
	 * sunxiao
	 */
	@Override
	public List<RegisterServiceVo> getRegisterListByInfo(Map<String, Object> params) {
		return registerServiceDao.getRegisterListByInfo(params);
	}

	public void generateDoctorDataVoList(List<HashMap<String, Object>> list,
			List<HashMap<String, Object>> doctorDataVoList) {
		if (list != null && !list.isEmpty()) {
			for (Map doctorDataVoMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("doctorId", doctorDataVoMap.get("id"));
				dataMap.put("doctorName", doctorDataVoMap.get("doctorName"));
				dataMap.put("hospitalName", StringUtils.isNotNull((String)doctorDataVoMap.get("ascriptionHospitalName"))?doctorDataVoMap.get("ascriptionHospitalName"):doctorDataVoMap.get("hospital"));
				dataMap.put("begin_time", doctorDataVoMap.get("begin_time"));
				dataMap.put("position1", doctorDataVoMap.get("position1"));
				dataMap.put("position2", doctorDataVoMap.get("position2"));

				// 获取该医生的所有出诊科室以及最早的可预约时间
				List<HashMap<String, Object>> doctorInfo = registerServiceDao.getDoctorVisitInfoById((String) doctorDataVoMap.get("id"));
				List<Map> formatInfo = new ArrayList<Map>();
				for (Map m : doctorInfo) {
					HashMap<String,Object> info = new HashMap<String,Object>();
 					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String latest_time = sdf.format((Date) m.get("latest_time"));
					String pat1 = "yyyy-MM-dd HH:mm";
					String pat2 = "HH:mm";
					try {
						SimpleDateFormat sdf1 = new SimpleDateFormat(pat1); // 实例化模板对象
						SimpleDateFormat sdf2 = new SimpleDateFormat(pat2); // 实例化模板对象
						Date d = sdf1.parse(latest_time);
						// 将给定的字符串中的日期提取出来
						info.put("latest_time",sdf2.format(d));
						info.put("availableDate",m.get("availableDate"));
						info.put("begin_time",m.get("begin_time"));
						info.put("name",m.get("name"));
						info.put("position",m.get("position"));
						info.put("date",m.get("date"));
						info.put("location",m.get("location"));
						info.put("location_id",m.get("location_id"));
						info.put("available_time",m.get("available_time"));
						info.put("service_type",m.get("service_type"));
						info.put("shot_time",m.get("shot_time"));
						formatInfo.add(info);
					} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
						e.printStackTrace(); // 打印异常信息
					}
				}

				dataMap.put("visitInfo", formatInfo);

				// 根据医生的ID，可获取医生的专长
				String doctorId = (String) doctorDataVoMap.get("id");
				String hospitalId = (String) doctorDataVoMap
						.get("sysHospitalId");
				
				Object departmentLevel1Obj = doctorDataVoMap.get("department_level1");
				
				dataMap.put("expertise", doctorInfoService
						.getDoctorExpertiseById(doctorId, hospitalId, departmentLevel1Obj == null ? null : (String) departmentLevel1Obj));

				Object departmentLevel2Obj = doctorDataVoMap.get("department_level2");
				boolean departmentLevel2IsEmpty = StringUtils.isEmpty( departmentLevel2Obj == null ? "" : (String) departmentLevel2Obj);
				String departmentFullName = departmentLevel1Obj == null ? null : departmentLevel1Obj + (departmentLevel2IsEmpty ? "" : "  " + departmentLevel2Obj);
				
				// 根据医生ID和医院ID，获取医生的所处科室
				dataMap.put("departmentFullName", departmentFullName);
				
				dataMap.put("career_time", String.valueOf((((new Date())
						.getTime() - ((Date) doctorDataVoMap.get("careerTime"))
						.getTime()) / (24 * 60 * 60 * 1000)) / 365 + 1));
				dataMap.put("fans_number", doctorDataVoMap.get("fansNumber"));

				// 根据sys_doctor_id查询它的最近可约时间 @author 得良添加
				/*HashMap<String, Object> recentlyDateMap = this
						.findRecentlyDateAppDate((String) doctorDataVoMap
								.get("id"));*/

				if (doctorDataVoMap.get("availableDate") != null) {
					Date date = new Date();
					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// DateFormat.getDateInstance();
					String value = df1.format(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = null;
					Date date2 = (Date) doctorDataVoMap.get("availableDate");
					try {
						date1 = df.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long time1 = date1.getTime();
					long time2 = date2.getTime();
					long newValue = time2 - time1;
					long day = (newValue) / (24 * 60 * 60 * 1000);
					if (day >= 0) {
						dataMap.put("available_time", day);// String.valueOf((newValue)
															// / (24 * 60 * 60 *
															// 1000))
					} else {
						dataMap.put("available_time", "-1");
					}
				} else {
					dataMap.put("available_time", "-1");
				}
				doctorDataVoList.add(dataMap);
			}
		}
	}

	/**
	 * 查询添加号源页面需要的数据
	 * sunxiao
	 */
	private Map<String, Object> compareRegisterTime(String doctorId,
			String date, String locationId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sysDoctorId", doctorId);
		map.put("date", date);
		List<String> statusList = new ArrayList<String>();
		statusList.add("0");
		statusList.add("1");
		map.put("statusList", statusList);
		List<RegisterServiceVo> temp = registerServiceDao
				.getRegisterListByInfo(map);
		List<String> distimeList = new ArrayList<String>();
		List<String> beginTimeList = new ArrayList<String>();
		List<String> comList = new ArrayList<String>();
		String time = new String();
		Float price = null;
		String serverType = "";
		for (RegisterServiceVo rsv : temp) {// 1添加：重复2添加3设置：重复4设置：约5设置：约重复6设置7置灰8置灰：重复
			time = DateUtils.DateToStr(rsv.getBeginTime(), "time");
			if (rsv.getLocationId().equals(locationId)) {
				if ("0".equals(rsv.getStatus())) {
					if ("0".equals(rsv.getRepeatFlag())
							|| "1".equals(rsv.getRepeatFlag())) {
						beginTimeList.add("3S|X" + time + "S|X"
								+ rsv.getRepeatFlag());
					} else {
						beginTimeList.add("6S|X" + time);
					}
				}
				if ("1".equals(rsv.getStatus())) {
					if ("0".equals(rsv.getRepeatFlag())
							|| "1".equals(rsv.getRepeatFlag())) {
						beginTimeList.add("5S|X" + time + "S|X"
								+ rsv.getRepeatFlag());
					} else {
						beginTimeList.add("4S|X" + time);
					}
				}
				if ("0".equals(rsv.getRepeatFlag())
						|| "1".equals(rsv.getRepeatFlag())) {
					comList.add(time);
				}
			} else {
				distimeList.add(time);
			}
		}
		map.put("price", price);
		map.put("serverType", serverType);
		map.put("success", "success");
		map.put("timeList", beginTimeList);
		map.put("distimeList", distimeList);
		return map;
	}

	/**
	 * 获取设置重复号源的日期
	 * sunxiao
	 *//*
	private Map<String, Object> getDateList(RegisterServiceVo vo, String date,
			String time, String repeat, String pageFlag) {
		List<String> dateList = new ArrayList<String>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		dateList.add(date);
		if (!"no".equals(repeat) && repeat != null && !"".equals(repeat)) {
			Date temp = DateUtils.StrToDate(date, "date");
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
			RegisterTemplateServiceVo rvo = operTemplate("get", vo, weekday,
					date, time, null);
			if (rvo != null) {
				retmap.put("repeat", "yes");
				if ("0".equals(rvo.getRepeatInterval())) {// 每周重复只能查出一个
					if ("0".equals(pageFlag)) {
						c.setTime(temp);
						for (int i = 0; i < 3; i++) {
							c.add(Calendar.DAY_OF_YEAR, 7);
							Date today = c.getTime();
							dateList.add(DateUtils.DateToStr(today, "date"));
						}
					}
					if ("1".equals(pageFlag)) {
						c.setTime(temp);
						c.add(Calendar.DAY_OF_YEAR, -7);
						Date lasttoday = c.getTime();
						dateList.add(DateUtils.DateToStr(lasttoday, "date"));
						c.setTime(temp);
						for (int i = 0; i < 2; i++) {
							c.add(Calendar.DAY_OF_YEAR, 7);
							Date today = c.getTime();
							dateList.add(DateUtils.DateToStr(today, "date"));
						}
					}
					if ("2".equals(pageFlag)) {
						c.setTime(temp);
						for (int i = 0; i < 2; i++) {
							c.add(Calendar.DAY_OF_YEAR, -7);
							Date today = c.getTime();
							dateList.add(DateUtils.DateToStr(today, "date"));
						}
						c.setTime(temp);
						c.add(Calendar.DAY_OF_YEAR, 7);
						Date today = c.getTime();
						dateList.add(DateUtils.DateToStr(today, "date"));
					}
					if ("3".equals(pageFlag)) {
						c.setTime(temp);
						for (int i = 0; i < 3; i++) {
							c.add(Calendar.DAY_OF_YEAR, -7);
							Date today = c.getTime();
							dateList.add(DateUtils.DateToStr(today, "date"));
						}
					}
				}
				if ("1".equals(rvo.getRepeatInterval())) {
					if ("0".equals(pageFlag) || "1".equals(pageFlag)) {
						c.setTime(temp);
						c.add(Calendar.DAY_OF_YEAR, 14);
						Date today = c.getTime();
						dateList.add(DateUtils.DateToStr(today, "date"));
					}
					if ("2".equals(pageFlag) || "3".equals(pageFlag)) {
						c.setTime(temp);
						c.add(Calendar.DAY_OF_YEAR, -14);
						Date today = c.getTime();
						dateList.add(DateUtils.DateToStr(today, "date"));
					}
				}
			}
		}
		retmap.put("dateList", dateList);
		return retmap;
	}*/
	
	/**
	 * 获取设置重复号源的日期
	 * sunxiao
	 */
	private Map<String, Object> getDateList(RegisterServiceVo vo, String date,
			String time, String repeat) {
		List<String> dateList = new ArrayList<String>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		dateList.add(date);
		if (!"no".equals(repeat) && repeat != null && !"".equals(repeat)) {
			Date temp = DateUtils.StrToDate(date, "date");
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			String weekday = c.get(Calendar.DAY_OF_WEEK) + "";
			RegisterTemplateServiceVo rvo = operTemplate("get", vo, weekday,
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

	@Override
	public Map<String, Object> getDoctorAppointments4Doctor(Map<String, Object> params) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();

		String doctorId = (String) params.get("doctorId");
		// 根据日期得到周几
		String dateStr = (String) params.get("date");
		Date date = DateUtils.StrToDate(dateStr, "date");
		String DateToStr = DateUtils.DateToStr(date);
		int week = DateUtils.dayForWeek(DateToStr);
		String dayWeek = DateUtils.getDayWeek(week);

		HashMap<String, Object> dataInfo = new HashMap<String, Object>();
		dataInfo.put("doctorId", doctorId);
		dataInfo.put("date", date);
		String locationId = (String) params.get("locationId");
		dataInfo.put("location_id", locationId);
		StringBuffer bf = new StringBuffer();
		bf.append(DateToStr.substring(0, 10));
		bf.append("  " + dayWeek).toString();
		response.put("date", bf);

		dataInfo.put("user","doctor");
		List<HashMap<String, Object>> appointmentVoList = registerServiceDao
				.findAllAppointmentInfoByDoctor(dataInfo);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000030" ,"doctorId:"+ doctorId
				+ "date:" + date);//00000030获取某个医生的某天的加号信息

		List<String> locationIdList = new ArrayList<String>();
		List<HashMap<String, Object>> dataMapList = new ArrayList<HashMap<String, Object>>();
		for (Map appointmentMap : appointmentVoList) {
			String theLocationId = (String) appointmentMap.get("location_id");
			if (!locationIdList.contains(theLocationId)) {
				locationIdList.add(theLocationId);
			}
		}

		for (String theLocationId : locationIdList) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> available_time = new ArrayList();
			for (Map appointmentMap : appointmentVoList) {
				HashMap<String, Object> timeMap = new HashMap<String, Object>();
				String location_id = (String) appointmentMap.get("location_id");
				if (theLocationId.equals(location_id)) {
					dataMap.put("hospitalId",
							appointmentMap.get("sys_hospital_id"));
					dataMap.put("hospitalName",
							appointmentMap.get("hospitalName"));
					dataMap.put("location", appointmentMap.get("location"));
					dataMap.put("locationId", appointmentMap.get("location_id"));
					dataMap.put("price", appointmentMap.get("price"));
					dataMap.put("service_type",appointmentMap.get("service_type"));

					timeMap.put("register_service_id", appointmentMap.get("id"));
					Object beginTime = appointmentMap.get("begin_time");
					timeMap.put("begin_time", beginTime);
					if ("0".equals(appointmentMap.get("repeatFlag"))
							|| "1".equals(appointmentMap.get("repeatFlag")))
						timeMap.put("repeat", true);
					else
						timeMap.put("repeat", false);

					timeMap.put("end_time", appointmentMap.get("end_time"));
					timeMap.put("status", appointmentMap.get("status"));
					
					if("1".equals(appointmentMap.get("status"))){
						HashMap<String, Object> ordermap = new HashMap<String, Object>();
						ordermap.put("registerServiceId", appointmentMap.get("id"));
						HashMap<String,Object> map = patientRegisterServiceDao.getPatientRegisterInfo(ordermap);
						if(map!=null){
							timeMap.put("illness", map.get("illness"));
							timeMap.put("babyName", map.get("babyName"));
							timeMap.put("phone", map.get("phone"));
							if(map.get("birthday")!=null){
								timeMap.put("birthday", DateUtils.DateToStr((Date)map.get("birthday"), "date"));
							}
						}
					}
					
					available_time.add(timeMap);
				}
				dataMap.put("available_time", available_time);
			}
			dataMapList.add(dataMap);
		}
		response.put("appointmentList", dataMapList);
		return response;
	}

	/**
	 * 操作号源模板表，用于号源的重复设置
	 * sunxiao
	 */
	private RegisterTemplateServiceVo operTemplate(String operType,
			RegisterServiceVo vo, String weekday, String date, String time,
			String operInterval) {
		Map<String, Object> map = new HashMap<String, Object>();
		if ("get".equals(operType)) {
			map.put("doctorId", vo.getSysDoctorId());
			map.put("weekday", weekday);
			map.put("time", time);
			List<RegisterTemplateServiceVo> list = registerTemplateServiceDao
					.getRegisterTemplateList(map);
			for (RegisterTemplateServiceVo rtvo : list) {
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
				map.put("serverType", vo.getServiceType());
				map.put("doctorId", vo.getSysDoctorId());
				map.put("locationId", vo.getLocationId());
				map.put("weekday", weekday);
				map.put("time", time);
				List<RegisterTemplateServiceVo> list = registerTemplateServiceDao
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
				registerTemplateServiceDao.updateRegisterTemplateByInfo(map);
			}
		}
		if ("add".equals(operType)) {
			map.put("doctorId", vo.getSysDoctorId());
			map.put("locationId", vo.getLocationId());
			map.put("weekday", weekday);
			map.put("price", vo.getPrice());
			map.put("serverType", vo.getServiceType());
			map.put("status", 1);
			map.put("hospitalId", vo.getSysHospitalId());
			map.put("createDate", new Date());
			map.put("updateDate", date);
			map.put("interval", operInterval);
			map.put("time", time);
			registerTemplateServiceDao.saveRegisterTemplate(map);
		}
		if ("del".equals(operType)) {
			if (time != null && !"".equals(time)) {
				map.put("doctorId", vo.getSysDoctorId());
				map.put("locationId", vo.getLocationId());
				map.put("weekday", weekday);
				map.put("time", time);
				List<RegisterTemplateServiceVo> list = registerTemplateServiceDao
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
				registerTemplateServiceDao.deleteRegisterTemplateByinfo(map);
			}
		}
		return null;
	}

	/**
	 * 修改号源
	 * sunxiao
	 */
	private void update(String id, float price, String serviceType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("price", price);
		map.put("serverType", serviceType);
		map.put("updateDate", new Date());
		registerServiceDao.updateRegisterService(map);
	}

	private void insertNotification(String patientRegisterId) {
		Notification notification = new Notification();
		notification.setMessage("取消号源导致订单被取消");
		User user = UserUtils.getUser();
		notification.setCreatedBy(user == null ? null : user.getId());
		notification.setCreatedTime(new Date());
		notification.setRelatedId(patientRegisterId);
		notification.setStatus(Notification.STATUS_INITIAL);
		notification.setType(Notification.TYPE_ORDER_REMOVED);
		notification.setUpdatedTime(new Date());
		notificationService.saveNotification(notification);
	}

	/**
	 * 修改号源模板
	 * sunxiao
	 */
	private void modifyRegisterTemplate(RegisterTemplateServiceVo vo,
			String date, List<String> timeList) {
		Calendar c = new GregorianCalendar();
		c.setTime(DateUtils.StrToDate(date, "date"));
		String weekday = c.get(Calendar.DAY_OF_WEEK) + "";

		JSONArray ja = new JSONArray();
		ja.addAll(timeList);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doctorId", vo.getDoctorId());
		map.put("locationId", vo.getLocationId());
		map.put("weekday", weekday);
		List<RegisterTemplateServiceVo> list = registerTemplateServiceDao
				.getRegisterTemplateList(map);
		if (list.size() == 0) {
			if (timeList.size() != 0) {
				map.put("price", vo.getPrice());
				map.put("serverType", vo.getServerType());
				map.put("time", ja.toString());
				map.put("status", 1);
				map.put("hospitalId", vo.getHospitalId());
				map.put("createDate", new Date());
				map.put("updateDate", date);
				map.put("interval", vo.getRepeatInterval());
				registerTemplateServiceDao.saveRegisterTemplate(map);
			}
		} else {
			if (timeList.size() == 0) {
				registerTemplateServiceDao.deleteRegisterTemplateByinfo(map);
			} else {
				map.put("price", vo.getPrice());
				map.put("serverType", vo.getServerType());
				map.put("time", ja.toString());
				map.put("updateDate", date);
				map.put("interval", vo.getRepeatInterval());
				registerTemplateServiceDao.updateRegisterTemplateByInfo(map);
			}
		}
	}

	private void insertMonitor(String register_no, String type, String status) {
		HashMap<String, Object> monitorMap = new HashMap<String, Object>();
		monitorMap.put("id", IdGen.uuid());
		monitorMap.put("register_no", register_no);
		monitorMap.put("status", status);
		monitorMap.put("types", type);
		messageService.insertMonitorExecute(monitorMap);
	} 

	private void generateDoctorDataVoListByDate(
			List<HashMap<String, Object>> list,
			List<HashMap<String, Object>> doctorDataVoList, Date dateInfo) {
		if (list != null && !list.isEmpty()) {  
			for (Map doctorDataVoMap : list) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("doctorId", doctorDataVoMap.get("id"));
				dataMap.put("doctorName", doctorDataVoMap.get("doctorName"));
				dataMap.put("hospitalName", doctorDataVoMap.get("hospital"));
				dataMap.put("begin_time", doctorDataVoMap.get("begin_time"));
				dataMap.put("position1", doctorDataVoMap.get("position1"));
				dataMap.put("position2", doctorDataVoMap.get("position2"));

				// 获取该医生的所有出诊科室以及最早的可预约时间
				HashMap<String, Object> dataInputMap = new HashMap<String, Object>();
				dataInputMap.put("doctorId", doctorDataVoMap.get("id"));
				dataInputMap.put("date", dateInfo);
				List<HashMap<String, Object>> doctorInfo = registerServiceDao
						.getDoctorVisitInfoByIdAndDate(dataInputMap);
				List<Map> formatInfo = new ArrayList<Map>();
				for (Map m : doctorInfo) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String latest_time = sdf
							.format((Date) m.get("latest_time"));
					String pat1 = "yyyy-MM-dd HH:mm";
					String pat2 = "HH:mm";
					try {
						SimpleDateFormat sdf1 = new SimpleDateFormat(pat1); // 实例化模板对象
						SimpleDateFormat sdf2 = new SimpleDateFormat(pat2); // 实例化模板对象
						Date d = null;
						d = sdf1.parse(latest_time);
						m.put("latest_time", sdf2.format(d));
						formatInfo.add(m);
					} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
						e.printStackTrace(); // 打印异常信息
					}
				}

				dataMap.put("visitInfo", formatInfo);
				// 根据医生的ID，可获取医生的专长
				String doctorId = (String) doctorDataVoMap.get("id");
				String hospitalId = (String) doctorDataVoMap
						.get("sysHospitalId");
				dataMap.put("expertise", doctorInfoService
						.getDoctorExpertiseById(doctorId, hospitalId, null));

				// 根据医生ID和医院ID，获取医生的所处科室
				dataMap.put("departmentFullName", hospitalInfoService
						.getDepartmentFullName(doctorId, hospitalId));

				dataMap.put("career_time", String.valueOf((((new Date())
						.getTime() - ((Date) doctorDataVoMap.get("careerTime"))
						.getTime()) / (24 * 60 * 60 * 1000)) / 365 + 1));
				dataMap.put("fans_number", doctorDataVoMap.get("fansNumber"));

				// 根据sys_doctor_id查询它的最近可约时间 @author 得良添加
				HashMap<String, Object> recentlyDateMap = this
						.findRecentlyDateAppDate((String) doctorDataVoMap
								.get("id"));

				if (recentlyDateMap != null
						&& recentlyDateMap.get("availableDate") != null) {
					Date date = new Date();
					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// DateFormat.getDateInstance();
					String value = df1.format(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = null;
					Date date2 = (Date) recentlyDateMap.get("availableDate");
					try {
						date1 = df.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long time1 = date1.getTime();
					long time2 = date2.getTime();
					long newValue = time2 - time1;
					long day = (newValue) / (24 * 60 * 60 * 1000);
					if (day >= 0) {
						dataMap.put("available_time", day);// String.valueOf((newValue)
															// / (24 * 60 * 60 *
															// 1000))
					} else {
						dataMap.put("available_time", "-1");
					}
				} else {
					dataMap.put("available_time", "-1");
				}
				doctorDataVoList.add(dataMap);
			}
		}
	}

	@Override
	public Map<String,Object> GetDoctorVisitInfoByLocation(Map<String, Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		String doctorId = (String) params.get("doctorId");
		String hospitalName = (String) params.get("hospitalName");
		String location_id = (String) params.get("location_id");
		String status = (String) params.get("status");
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("doctorId", doctorId);
		data.put("hospitalName", hospitalName);
		data.put("location_id", location_id);
		data.put("status", status);
		List<HashMap<String, Object>> doctorVisitInfo = getDoctorVisitInfoByLocation(data);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000031","doctorId:"+ doctorId + "location_id:" + location_id);//根据医生的出诊地点获取医生7天内的出诊信息:

		List<HashMap<String, Object>> visitInfo = new ArrayList();
		for (Map doctorVisit : doctorVisitInfo) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("date", doctorVisit.get("date"));
			visitInfo.add(dataMap);
		}
		response.put("dateList", visitInfo);
		if (doctorVisitInfo != null && doctorVisitInfo.size() > 0) {
			response.put("kindlyReminder", doctorVisitInfo.get(0).get("kindlyReminder"));
		}
		return response;

	}

	@Override
	public String getVisitTimeById(Map map) {
		return registerServiceDao.getVisitTimeById(map);
	}

	@Override
	public List<HashMap<String, Object>> getDoctorVisitInfoById(String id) {
		return registerServiceDao.getDoctorVisitInfoById(id);
	}

	@Override
	public List<HashMap<String, Object>> getDoctorVisitInfoByIdAndDate(
			HashMap<String, Object> map) {
		return registerServiceDao.getDoctorVisitInfoByIdAndDate(map);
	}

	@Override
	public List<HashMap<String, Object>> getDoctorVisitInfo(String id) {
		return registerServiceDao.getDoctorVisitInfo(id);
	}

	@Override
	public RegisterServiceVo getRegisterById(Map map){
	  return registerServiceDao.getRegisterById(map);
	};

	@Override
	public void batchInsertRegister(List<HashMap<String, Object>> excuteList){registerServiceDao.batchInsertRegister(excuteList);};

	@Override
	public List<RegisterTemplateServiceVo> getRegisterTemplateList(Map<String, Object> executeMap){return registerTemplateServiceDao
			.getRegisterTemplateList(executeMap); };

	@Override
	public void updateRegisterTemplateByInfo(Map<String, Object> executeMap){
		registerTemplateServiceDao.updateRegisterTemplateByInfo(executeMap);
	}

	@Override
	public int findDoctorRegisterServiceByData(HashMap<String, Object> hashMap) {
		return registerServiceDao.findDoctorRegisterServiceByData(hashMap);
	}

	@Override
	public HashMap<String, Object> getCooperationHospitalTypeBySrsId(String register_service_id){
		return registerServiceDao.getCooperationHospitalTypeBySrsId(register_service_id);
	}

	@Override
	public Map<String, Object> getEarliestVisiteInfo(String doctorId) {
		HashMap<String,Object> dataMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> doctorInfo = registerServiceDao.getDoctorVisitInfoById(doctorId);
		List<Map> formatInfo = new ArrayList<Map>();
		for (Map m : doctorInfo) {
			HashMap<String,Object> info = new HashMap<String,Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String latest_time = sdf.format((Date) m.get("latest_time"));
			String pat1 = "yyyy-MM-dd HH:mm";
			String pat2 = "HH:mm";
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat(pat1); // 实例化模板对象
				SimpleDateFormat sdf2 = new SimpleDateFormat(pat2); // 实例化模板对象
				Date d = sdf1.parse(latest_time);
				// 将给定的字符串中的日期提取出来
				info.put("latest_time",sdf2.format(d));
				info.put("availableDate",m.get("availableDate"));
				info.put("begin_time",m.get("begin_time"));
				info.put("name",m.get("name"));
				info.put("position",m.get("position"));
				info.put("date",m.get("date"));
				info.put("location",m.get("location"));
				info.put("location_id",m.get("location_id"));
				info.put("available_time",m.get("available_time"));
				info.put("service_type",m.get("service_type"));
				info.put("shot_time",m.get("shot_time"));
				formatInfo.add(info);
			} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
				e.printStackTrace(); // 打印异常信息
			}
		}

		dataMap.put("visitInfo", formatInfo);
		return dataMap;
	}

}
