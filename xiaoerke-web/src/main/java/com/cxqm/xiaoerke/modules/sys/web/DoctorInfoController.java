/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorGroupVo;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生信息Controller
 *
 * @author sunxiao
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "")
public class DoctorInfoController extends BaseController {

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private DoctorLocationService doctorLocationService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private DoctorCaseService doctorCaseService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private HospitalInfoService hospitalInfoService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private DoctorGroupInfoService doctorGroupInfoService;

    /**
     * 获取某个医生的详细信息 @author zdl_01
     * params:{"doctorId":"324xdksg234","register_service_id":"241342353141354dsfawe"}
     * <p/>
     * response
     * {
     * "doctorId":"787xeieo234","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"departmentName":"儿内-重症医学科","hospital_type":"1"
     * "expertise":"小儿呼吸","position1":"主任医师","position2":"教授","career_time":"32","fans_number":"40",
     * "doctor_expert_desc":"擅长:小儿呼吸道感染，肺炎，反复喘息，儿童哮喘，慢性咳嗽，儿童睡眠呼吸问题",
     * "doctor_normal_desc":"简介:庄晓华，医学博士，首都医院呼吸科主任医师。2003年在美国Riley 儿童医院做访问学者一年。先后参与了国家“十一五”课题"，
     * "example":{"desc":"概括74名小朋友的疾病症状","examList":[{"急慢性咳嗽":"1"},{"呕吐":"2"},{"其他":"8"}]},
     * "appraise":[{"star":"4","date":"2015-04-03 17:55","phone":"138xxxxx551","desc":"非常有用，感觉医生人很好"},
     * {"star":"4","date":"2015-04-03 17:55","phone":"138xxxxx551","desc":"非常有用，感觉医生人很好"},
     * {"star":"4","date":"2015-04-03 17:55","phone":"138xxxxx551","desc":"非常有用，感觉医生人很好"}]
     * {"doctorCaseList":[{"doctor_case_number":"23","doctor_case_name":"咳嗽"},{},{}]}  -------案例
     * {"otherCase":"23"} ---案例其他
     * <p/>
     * }
     *///hospital_type  1：加号   2：公立特需服务   3：私立医院
    @RequestMapping(value = "/sys/user/doctorDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000073")
    //获取某个医生的详细信息
    Map<String, Object> DoctorDetail(@RequestBody Map<String, Object> params) {
        String doctorId = (String) params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("id",UserUtils.getUser().getId());
            doctorId = (String)doctorInfoService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        }

        Map<String, Object> response;
        PraiseVo praiseVo = new PraiseVo();
        HashMap<String, Object> doctorMap = new HashMap<String, Object>();
        doctorMap.put("doctorId", doctorId);
        String register_service_id = (String) params.get("register_service_id");
        //需返回hospital_type，需关联表查询
        if (!"".equals(register_service_id) && register_service_id != null) {
            doctorMap.put("register_service_id", register_service_id);
            response = doctorInfoService.findDoctorDetailInfoAndType(doctorMap);
        } else {//不需返回hospital_type，不需关联表查询
            response = doctorInfoService.findDoctorDetailInfo(doctorMap);
            DoctorGroupVo doctorGroupVo = doctorGroupInfoService.getDoctorGroupInfoByDoctor(doctorId);
            if(doctorGroupVo!=null) {
                response.put("doctorGroupId",doctorGroupVo.getDoctorGroupId());
                response.put("doctorGroupName",doctorGroupVo.getName());
            }
        }
        //获取科室
        response.put("departmentName", hospitalInfoService.getDepartmentFullName(doctorId,(String) response.get("hospitalId")));
        //获取从业擅长
        response.put("doctor_expert_desc", doctorInfoService.getDoctorCardExpertiseById(doctorId));
        praiseVo.setSys_doctor_id(doctorId);
        List<PraiseVo> mapPraise = patientRegisterPraiseService.findDoctorDetailPraiseInfo(praiseVo);

        HashMap<String, Object> doctorScore = doctorInfoService.findDoctorScoreInfo(doctorId);
        response.put("doctorScore", doctorScore);
        //获取医生的案例信息
        List<DoctorCaseVo> doctorCaseVos = doctorCaseService.findDoctorCase(doctorId);
        int sumcase = 0;
        int otherCase = 0;
        if (doctorCaseVos.size() > 0) {
            sumcase = doctorCaseService.findDoctorCaseNumber(doctorId);
            for (DoctorCaseVo doctorCaseVo : doctorCaseVos) {
                //"计算"其他案例
                otherCase = otherCase + doctorCaseVo.getDoctor_case_number();
            }
            otherCase = sumcase - otherCase;
            if (otherCase > 0) {
                DoctorCaseVo doctorCaseVo = new DoctorCaseVo();
                doctorCaseVo.setDoctor_case_name("其他");
                doctorCaseVo.setDoctor_case_number(otherCase);
                doctorCaseVos.add(doctorCaseVo);
            }
        }

        List newList = new ArrayList();
        if (mapPraise != null && !mapPraise.isEmpty()) {
            for (PraiseVo praVo : mapPraise) {
                HashMap<String, Object> newMap = new HashMap<String, Object>();
                newMap.put("star", praVo.getStar());
                newMap.put("date", praVo.getPraiseDate());
                newMap.put("phone", praVo.getPhone());
                newMap.put("desc", praVo.getPraise());
                newList.add(newMap);
            }
        }
        response.put("appraise", newList);
        response.put("otherCase", otherCase);//其他案例数
        response.put("sumcase", sumcase);//总案例数
        response.put("doctorCaseList", doctorCaseVos);//医生案例
        //获取医生的案例信息
        return response;


    }

    /**
     * 获取医生7天内的出诊位置信息 @author cjk
     * <p/>
     * params:{"doctorId":"fewi323odw"}
     * <p/>
     * response:
     * {
     * "doctorId":"fewi323odw",
     * "maxItem":6,
     * "visitInfo":[
     * {"sys_hospital_id":"fwefrwef","hospitalName":"儿童医院","position":"雅宝路56号","location":"专家楼5诊室","item":1},
     * {"sys_hospital_id":"fwefrwef","hospitalName":"儿童医院","position":"雅宝路56号","location":"专家楼5诊室","item":1}
     * ]
     * }
     * //status即是返回此号源的状态即可
     */
    @RequestMapping(value = "/sys/user/doctorVisitInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000074")
//搜索医生7天内的出诊位置信息
    Map<String, Object> GetDoctorVisitInfo(@RequestBody Map<String, Object> params) throws Exception {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String doctorId = (String) params.get("doctorId");
        List<HashMap<String, Object>> doctorInfo = registerService.getDoctorVisitInfo(doctorId);

        List<HashMap<String, Object>> visitInfo = new ArrayList();
        int infoItem = 0;
        for (Map doctorVisit : doctorInfo) {
            if ((Long) doctorVisit.get("available_time") >= 0 && (Long) doctorVisit.get("available_time") < 7) {
                infoItem++;
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("sys_hospital_id", doctorVisit.get("sys_hospital_id"));
                dataMap.put("hospitalName", doctorVisit.get("hospitalName"));
                dataMap.put("position", doctorVisit.get("position"));
                dataMap.put("location", doctorVisit.get("location"));
                dataMap.put("location_id", doctorVisit.get("location_id"));
                dataMap.put("infoItem", infoItem);
                dataMap.put("date", doctorVisit.get("date"));
                dataMap.put("available_time", doctorVisit.get("available_time"));
                visitInfo.add(dataMap);
            }
        }
        response.put("maxItem", infoItem);
        response.put("doctorId", doctorId);
        response.put("visitInfo", visitInfo);
        return response;

    }

    /**
     * 根据医生的出诊地点获取医生7天内的出诊时间
     * <p/>
     * params:{"doctorId":"fewi323odw"，"hospitalName":"儿童医院"，"location":"专家楼5诊室"}
     * <p/>
     * response:
     * {
     * "dateList":[
     * {"date":"2015-08-11"},{"date":"2015-08-13"}
     * ]
     * }
     * //status即是返回此号源的状态即可
     */
    @RequestMapping(value = "/sys/user/DoctorVisitInfoByLocation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> GetDoctorVisitInfoByLocation(@RequestBody Map<String, Object> params) throws Exception {
        return registerService.GetDoctorVisitInfoByLocation(params);
    }

    /**
     * 获取医生的就诊地址信息(sys_doctor_location)
     * <p/>
     * params:{"sys_doctor_id":"01qwerqwevfgtr3421sfqw"}
     * <p/>
     * response:
     * {
     * "DoctorLocationId":"fewi323odw",
     * "DoctorId":"123456789",
     * "HospitalName":"儿童医院",
     * "location"："门诊楼2层内科专家诊区",
     * "shotTime":"15min"
     * }
     * //status即是返回此号源的状态即可
     */
    @RequestMapping(value = "/sys/user/findDoctorLocationByDoctorId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findDoctorLocationByDoctorId(@RequestBody Map<String, Object> params) throws Exception {
        return doctorLocationService.findDoctorLocationByDoctorId(params);
    }

    /**
     * 入 ： doctorId 出 ：doctorCaseList 案例列表
     *
     * @param params
     */
    @RequestMapping(value = "/sys/user/findDoctorCaseEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findDoctorCaseEvaluation(@RequestBody Map<String, Object> params) {
        String doctorId = (String)params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            //获取医生的案例信息
            String patient_register_service_id = (String) params.get("patient_register_service_id");
            HashMap<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("id", patient_register_service_id);
            HashMap<String, Object> patientMap = patientRegisterService.findSysRegisterServiceByPRSIdExecute(searchMap);
            doctorId = (String) patientMap.get("sys_doctor_id");
        }

        Map<String, Object> respose = new HashMap<String, Object>();
        List<DoctorCaseVo> doctorCaseVos = doctorCaseService.findDoctorCase(doctorId);
        DoctorCaseVo docVo = new DoctorCaseVo();
        docVo.setDoctor_case_name("其他");
        doctorCaseVos.add(docVo);
        respose.put("doctorCaseList", doctorCaseVos);
        return respose;
    }

    /**
     * 获取医生的基本信息
     *
     * @return 返回信息
     */
    @RequestMapping(value = "/sys/doctor/doctorInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getDoctorInfo(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String userId = UserUtils.getUser().getId();
        if (userId != null && !"".equals(userId)) {
            HashMap<String, Object> perInfo = doctorInfoService.findDoctorDetailInfoByUserId(userId);
            response.put("phone", perInfo.get("phone"));
            response.put("doctorId", perInfo.get("doctor_id"));
            response.put("userId", userId);
            response.put("balance", perInfo.get("balance") == null ? 0.0 : (Float) perInfo.get("balance") / 100);
            response.put("doctorName", perInfo.get("name"));
            response.put("position1", perInfo.get("position1"));
            response.put("position2", perInfo.get("position2"));
            response.put("hospitalName", perInfo.get("hospitalName"));
            response.put("department", perInfo.get("department_level1"));
            response.put("extensionQrcode", perInfo.get("extensionQrcode"));
            response.put("currentTime", System.currentTimeMillis());
        }
        return response;
    }

    @RequestMapping(value = "/sys/doctor/doctorBaseInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000073")
        //获取某个医生的详细信息
    Map<String, Object> DoctorBaseInfo(@RequestBody Map<String, Object> params) {
        String doctorId = (String) params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("id",UserUtils.getUser().getId());
            doctorId = (String)doctorInfoService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        }

        HashMap<String, Object> doctorMap = new HashMap<String, Object>();
        doctorMap.put("doctorId", doctorId);

        Map<String, Object> response = doctorInfoService.findDoctorDetailInfo(doctorMap);

        //获取科室
        response.put("departmentName", hospitalInfoService.getDepartmentFullName(doctorId,(String) response.get("hospitalId")));

        HashMap<String, Object> doctorScore = doctorInfoService.findDoctorScoreInfo(doctorId);
        response.put("doctorScore", doctorScore);

        return response;

    }

}
