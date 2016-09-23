/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.register.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;

/**
 * 号源Controller
 *
 * @author sunxiao
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "register/doctor")
public class RegisterDoctorController extends BaseController {

	@Autowired
    private RegisterService registerService;

	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private SysPropertyServiceImpl sysPropertyService;

    @RequestMapping(value = "/date", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getDatesHasRegisters(@RequestParam String doctorId, String locationId,
                                             @RequestParam Integer days, @RequestParam String date) throws Exception {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        HashMap<String, Object> response = new HashMap<String, Object>(2);
        Date currentDate = DateUtils.StrToDate(date, "date");
        List<Date> dates = registerService.getDatesWithRegisters(doctorId, locationId, currentDate, days);
        response.put("dates", dates);
        return response;
    }

	@RequestMapping(value = "/arrange", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> arrangeRegister(@RequestBody Map<String, Object> params) throws Exception {
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
    	String doctorId = (String) params.get("doctorId");
    	String hospitalId = (String) params.get("hospitalId");
    	String locationId = (String) params.get("locationId");
    	String serviceType = (String) params.get("serviceType");
    	Float price = Float.parseFloat(String.valueOf(params.get("price")));
    	String date = (String) params.get("date");
    	String fromTime = (String) params.get("fromTime");
    	String toTime = (String) params.get("toTime");
    	String repeatType = (String) params.get("repeatType");//三种值不重复no,每周重复0,隔周重复1
    	List<String> timeList = new ArrayList<String>();
		Date from = DateUtils.StrToDate(fromTime, "time");
		Date to = DateUtils.StrToDate(toTime, "time");
		if(from.getTime() < to.getTime()){
			if(!fromTime.equals(toTime)){
				Date temp = new Date();
				for(int i=0;i < Integer.parseInt(sysPropertyVoWithBLOBsVo.getDayQuarterNumber());i++){
					temp.setTime(from.getTime() + Integer.parseInt(sysPropertyVoWithBLOBsVo.getVisitInterval())*60*1000*i);
					String toTimeStr =DateUtils.DateToStr(temp,"time");
					if(!toTime.equals(toTimeStr)){
						timeList.add(toTimeStr);
					}else{
						break;
					}
				}
			}
		}
		RegisterServiceVo vo = new RegisterServiceVo();
        vo.setSysDoctorId(doctorId);
        vo.setSysHospitalId(hospitalId);
		vo.setPrice(price);
        vo.setServiceType(serviceType);
		vo.setLocationId(locationId);
		Map<String, String> ret = registerService.addRegister(vo,timeList,date,repeatType);
		HashMap<String, Object> response = new HashMap<String, Object>(4);
		response.put("reason", ret.get("doctor"));
        return response;
    }

	@RequestMapping(value = "/remove", method = {RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> removeRegister(@RequestBody List<Map<String, Object>> params) throws Exception {
    	int count = 0;
    	for(Map<String, Object> param : params) {
			DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
	    	String doctorId = (String) param.get("doctorId");
	    	String hospitalId = (String) param.get("hospitalId");
	    	String locationId = (String) param.get("locationId");
	    	List<String> times = (List<String>) param.get("times");
	    	String date = (String) param.get("date");
	    	boolean repeated = param.get("repeated") == null ? false : (Boolean) param.get("repeated");
	    	String operRepeat = repeated?"yes":"no";
	    	RegisterServiceVo vo = new RegisterServiceVo();
			vo.setSysDoctorId(doctorId);
			vo.setSysHospitalId(hospitalId);
			vo.setLocationId(locationId);
			count += registerService.deleteRegisters(vo,times,date,operRepeat,"doctor");
	    	//TODO should be in the same transaction, but now using
    	}
    	HashMap<String, Object> response = new HashMap<String, Object>(4);
    	response.put("status", "OK");
    	response.put("count", count);

        return response;
    }

    /**
     * 医生端获取某个医生的某天的加号信息
     * <p/>
     * params:{"doctorId":"324xdksg234","date":"2015-06-08"}
     * <p/>
     * {
     * "appointmentList":[
     * {
     * "hospitalId":"432kfjdsoijfe",
     * "location":"北京市朝阳区首都医院2楼北侧专家诊所11诊室",
     * "price":"14",
     * "service_type":"1"
     * "available_time":[
     * {"register_service_id":"3dsf3xfe","begin_time":"8:00","end_time":"8:10","status":"0"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:10","end_time":"8:20","status":"1"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:20","end_time":"8:30","status":"1"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:30","end_time":"8:40","status":"0"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:40","end_time":"8:50","status":"0"}
     * ]
     * },
     * {
     * "hospitalId":"432kfjdsoijfe",
     * "location":"北京市儿童医院2楼北侧专家诊所11诊室",
     * "price":"14",
     * "service_type":"1"
     * "available_time":[
     * {"register_service_id":"3dsf3xfe","begin_time":"8:00","end_time":"8:10","status":"0"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:10","end_time":"8:20","status":"1"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:20","end_time":"8:30","status":"1"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:30","end_time":"8:40","status":"0"},
     * {"register_service_id":"3dsf3xfe","begin_time":"8:40","end_time":"8:50","status":"0"}
     * ]
     * }
     * <p/>
     * ]
     * }
     * //status的0表示可约，1表示已被人约走
     */
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getDoctorAppointments4Doctor(@RequestBody Map<String, Object> params) throws Exception {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        return registerService.getDoctorAppointments4Doctor(params);
    }

    @RequestMapping(value = "/saveDoctorAppointmentInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> saveDoctorAppointmentInfo(@RequestBody Map<String, Object> params) {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        HashMap<String, Object> response = new HashMap<String, Object>();
        int status = patientRegisterService.saveDoctorAppointmentInfo(params);
        if(status==1) {
            response.put("result","true");
        } else {
            response.put("result", "false");
        }
        return response;
    }

	@RequestMapping(value = "/bookedNum", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> getBookedRegisters(@RequestBody List<Map<String, Object>> params) throws Exception {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
		int count = 0;
		for(Map<String, Object> param : params) {
			String doctorId = (String) param.get("doctorId");
			String locationId = (String) param.get("locationId");
			List<String> timeList = (List<String>) param.get("times");
			StringBuffer timesb = new StringBuffer("");
			String date = (String) param.get("date");
			for(String time : timeList){
				timesb.append(time).append(";");
			}
			boolean repeated = param.get("repeated") == null ? false : (Boolean) param.get("repeated");
			String operRepeat = repeated?"yes":"no";
			String re = registerService.judgeRepeatEffect(date, timesb.toString(), locationId, doctorId,operRepeat);
			if(!"".equals(re)){
				count += re.split("的号源已被预约").length;
			}
		}
		HashMap<String, Object> response = new HashMap<String, Object>(2);
		response.put("count", count);
		return response;
	}

	/**
	 * 获取医生某天的出诊信息
	 * params:{"doctorId":"fjewiofnwe","date":"2015-09-25"}
	 *
	 * response:
	 * [
	 *   {"hospitalId":"fwejoijfoiwe","hospitalName":"北京儿童医院"，"relationType":"1","availableInfo":
	 *      [{"location":"儿童保健科一楼8号诊室","locationId":"fwieonfiowe","times":[{"time":"08:00"},{"time":"08:15"},{"time":"08:30"}]},
	 *      {"location":"儿童保健科一楼8号诊室","locationId":"fwieonfiowe","times":[{"time":"08:00"},{"time":"08:15"},{"time":"08:30"}]}]},
	 *  {"hospitalId":"fwejoijfoiwe","hospitalName":"儿童研究所"，"relationType":"0","availableInfo":
	 *      [{"location":"儿童保健科一楼8号诊室","locationId":"fwieonfiowe","times":[{"time":"08:00"},{"time":"08:15"},{"time":"08:30"}]},
	 *      {"location":"儿童保健科一楼8号诊室","locationId":"fwieonfiowe","times":[{"time":"08:00"},{"time":"08:15"},{"time":"08:30"}]}]}
	 * ]
	 *
	 * @return 返回信息
	 */
	@RequestMapping(value = "/appointmentInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> getDoctorTimeInfo(@RequestBody Map<String, Object> params) {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
		HashMap<String, Object> response = new HashMap<String, Object>();
		patientRegisterService.findDoctorAppointmentInfoByDate((String) params.get("doctorId"),
				(String) params.get("date"), response);
		return response;
	}
}
