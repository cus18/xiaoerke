/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.register.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "")
public class RegisterUserController extends BaseController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    SysConsultPhoneService sysConsultPhoneService;

    /**
     * 获取预约时间
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy","1"}
     * //1表示此预约时间下医生最多的排序
     * 此分页查询暂时没有必要，目前可按照最近7天的自然排序来
     * <p/>
     * response:
     * {
     * "timeData":[
     * {"date":"2015-06-14","week":"周六"，"desc":"今天"},
     * {"date":"2015-06-15","week":"周日"，"desc":"明天"}
     * {"date":"2015-06-16","week":"周一"，"desc":"后天"}
     * {"date":"2015-06-16","week":"周二"，"desc":""}
     * ]
     * }
     * //desc的描述，只能描述为今天，明天和后天，其他时间置空
     */
    @RequestMapping(value = "/register/user/time", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000071")//获取往后七天日期数据
    Map<String, Object> listAppointmentTime(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> timeDataList = new ArrayList<HashMap<String, Object>>();
        String pageSize = ((String) params.get("pageSize"));
        DateFormat format = new SimpleDateFormat("MM/dd");
        for (int i = 0; i < Integer.parseInt(pageSize); i++) {
            HashMap<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Date d = format.parse(format.format(new Date()));
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(c.DATE, i);
                Date temp_date = c.getTime();
                dataMap.put("date", format.format(temp_date));
                Calendar w = Calendar.getInstance();
                w.add(w.DATE, i-1);
                dataMap.put("week", DateUtils.getDayWeek(w.get(Calendar.DAY_OF_WEEK)));
                if (i == 0) {
                    dataMap.put("desc", "今天");
                } else if (i == 1) {
                    dataMap.put("desc", "明天");
                } else if (i == 2) {
                    dataMap.put("desc", "后天");
                } else {
                    dataMap.put("desc", "");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            timeDataList.add(dataMap);
        }
        response.put("timeData", timeDataList);
        return response;
    }

    /**
     * 获取某个预约日期下的可预约的医院
     * params:{"pageNo":"2","pageSize":"10","orderBy":"1","date":"2015-06-15"}
     * //1按照此时间下，医院可预约的医生人数最多的排序
     * <p/>
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "hospitalData":[
     * {"hospitalId":"787xeieo234","hospitalName":"北京儿童医院"，"hospitalLocation":"北京市西便门内大街53号"},
     * {"hospitalId":"7xdwerd234","hospitalName":"首都儿科研究所"，"hospitalLocation":"北京市西便门内大街53号"}
     * ]
     * }
     */
    @RequestMapping(value = "/register/user/time/hospital", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listAppointmentTimeHospital(@RequestBody Map<String, Object> params) {
        return registerService.listAppointmentTimeHospital(params);
    }


    /**
     * 获取某个预约日期下的可预约医生
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"2","date":"2015-06-15","department_level1":"内科"}
     * //2按照粉丝最多排序
     * <p/>
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "doctorDataVo":[
     * {"doctorId":"787xeieo234","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"departmentName":"儿内-重症医学科",
     * "expertise":"小儿呼吸","career_time":"32","fans_number":"40","available_time":"0"},
     * {"doctorId":"7xdwerd234","doctorName":"董庆华","hospitalName":"首都儿科研究所"，"departmentName":"儿内-呼吸内科",
     * "expertise":"小儿呼吸","career_time":"20"，"fans_number":"40","available_time":"0"}
     * ]
     * }
     * //available_time为0表示今日可约，为1代表明日可约，为2，代表2天后可约，依次类推7天以内的即可
     */
    @RequestMapping(value = "/register/user/time/doctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listAppointmentTimeDoctor(@RequestBody Map<String, Object> params) {
        return registerService.listAppointmentTimeDoctor(params);
    }


    /**
     * 获取某个预约日期下的可预约的医院下的医生
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"2","date":"2015-06-15","hospitalId":"324xdksg234"}
     * //2按照粉丝最多排序
     * <p/>
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "doctorDataVo":[
     * {"doctorId":"787xeieo234","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"departmentName":"儿内-重症医学科",
     * "expertise":"小儿呼吸","career_time":"32","fans_number":"40","available_time":"0"},
     * {"doctorId":"7xdwerd234","doctorName":"董庆华","hospitalName":"首都儿科研究所"，"departmentName":"儿内-呼吸内科",
     * "expertise":"小儿呼吸","career_time":"20"，"fans_number":"40","available_time":"0"}
     * ]
     * }
     * //available_time为0表示今日可约，为1代表明日可约，为2，代表2天后可约，依次类推7天以内的即可
     */
    @RequestMapping(value = "/appointment/time/hospital/doctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listAppointmentTimeHospitalDoctor(@RequestBody Map<String, Object> params) {
        return registerService.listAppointmentTimeHospitalDoctor(params);
    }

    /**
     * 获取某个医生的某天的加号信息
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
    @RequestMapping(value = "/register/user/doctor/time", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> doctorAppointmentInfo(@RequestBody Map<String, Object> params) throws Exception {
        return registerService.doctorAppointmentInfoOfDay(params);
    }

    /**
     * 获取某个医生的某天的(电话咨询)加号信息
     * <p/>
     * params:{"doctorId":"324xdksg234","date":"2015-06-08"}
     * <p/>
     * {
     * "consultPhoneList":[
     * {
     * "price":"14",
     * "service_type":"1"
     * "available_time":[
     * {"consultPhone_service_id":"3dsf3xfe","begin_time":"8:00","end_time":"8:10","state":"0"},
     * {"consultPhone_service_id":"3dsf3xfe","begin_time":"8:10","end_time":"8:20","state":"1"},
     * {"consultPhone_service_id":"3dsf3xfe","begin_time":"8:20","end_time":"8:30","state":"1"},
     * {"consultPhone_service_id":"3dsf3xfe","begin_time":"8:30","end_time":"8:40","state":"0"},
     * {"consultPhone_service_id":"3dsf3xfe","begin_time":"8:40","end_time":"8:50","state":"0"}
     * ]
     * }
     * ]
     * }
     * //state的0表示可约，1表示已被人约走
     */
    @RequestMapping(value = "/consultPhone/user/doctor/time", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> doctorConsultPhoneInfo(@RequestBody Map<String, Object> params) throws Exception {
        return sysConsultPhoneService.doctorConsultPhoneOfDay(params);
    }

    /**
     * 获取号源详细路线
     * <p/>
     * params:{"register_service_id":"fewi323odw"}
     * <p/>
     * response:
     * {
     * "register_service_id":"fewi323odw",
     * "status":"1"
     * }
     * //status即是返回此号源的状态即可
     */
    @RequestMapping(value = "/register/user/sourceRoute", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> appointmentSourceRoute(@RequestBody Map<String, Object> params) throws Exception {
        return registerService.orderSourceRoute(params);
    }

}
