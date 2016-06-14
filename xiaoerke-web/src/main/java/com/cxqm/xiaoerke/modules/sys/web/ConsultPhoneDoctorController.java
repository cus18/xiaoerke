package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorCaseService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by wangbaowei on 16/3/16.
 */

@Controller
@RequestMapping(value = "consultPhone/consultPhoneDoctor/")
public class ConsultPhoneDoctorController {

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private SysConsultPhoneService sysConsultPhoneService;

    @Autowired
    private HospitalInfoService hospitalInfoService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private DoctorCaseService doctorCaseService;

    /**
     * 电话咨询首页-获取所有医院列表
     * @param pageSize
     * @param pageNo
     * @param order
     * @return page
     * */
    @RequestMapping(value = "getAllDoctorList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Page<HashMap<String, Object>> getAllDoctorList(@RequestParam String pageSize,@RequestParam String pageNo,@RequestParam String order){
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo, pageSize);
        Page<HashMap<String, Object>> hospitalPage = hospitalInfoService.getHospitalListByConsulta(page);
        return hospitalPage;
    }


    /**
     * 医生列表-获取医生详细信息
     * 基本信息
     * 评价信息
     * 关注
     * 服务态度评价
     * @return Map
     * */
    @RequestMapping(value = "getDoctorInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getDoctorInfo(@RequestParam String doctorId){
        Map map = new HashMap();
        map.put("doctorId",doctorId);
        return doctorInfoService.findDoctorDetailInfo(map);
    }

    /**
     * 医生列表-获取医生评价信息
     * 基本信息
     * 评价信息
     * @return Map
     * */
    @RequestMapping(value = "getDoctorEvaluateInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getDoctorEvaluateInfo(@RequestParam String doctorId){
        Map map = new HashMap();
        map.put("doctorId", doctorId);
        return doctorInfoService.findDoctorDetailInfo(map);
    }

    /**
     * 医生列表-获取医生可约时间  按时间先后倒叙
     * <p/>
     * params:{"doctorId":"fewi323odw"}
     * <p/>
     * response:
     * {
     * "dateList":[
     * {"date":"2015-08-11"},{"date":"2015-08-13"}
     * ]
     * }
     * @return Map
     */
    @RequestMapping(value = "getConsultInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> getConsultInfo(@RequestBody Map<String, Object> params){
        String doctorId = (String) params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("id",UserUtils.getUser().getId());
            doctorId = (String)doctorInfoService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        }
        String state = (String) params.get("state");
        String date = (String) params.get("date");

        HashMap<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("doctorId", doctorId);
        dataMap.put("state", state);
        dataMap.put("date", date);

        Map<String, Object> response = sysConsultPhoneService.getDoctorConsultDate(dataMap);
        response.put("doctorId",doctorId);

        return response;
    }

    @RequestMapping(value = "getConsultDateInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> getConsultDateInfo(@RequestBody Map<String, Object> params){
        String doctorId = (String) params.get("doctorId");
        if(doctorId == null || "".equals(doctorId)){
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("id",UserUtils.getUser().getId());
            doctorId = (String)doctorInfoService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        }
        String date = (String) params.get("date");

        HashMap<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("doctorId", doctorId);
        dataMap.put("date", date);

        Map<String, Object> response = sysConsultPhoneService.getDoctorConsultDateInfo(dataMap);
        response.put("doctorId",doctorId);

        return response;
    }

    /**
     * 根据医院查询医生
     * @return Map
     * */
    @RequestMapping(value = "getDoctorListByHospital",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object>  getDoctorListByHospital(@RequestBody Map<String, Object> params){
        HashMap<String, Object> response = new HashMap<String, Object>();
        HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
        String hospitalId = (String) params.get("hospitalId");
        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String orderBy = (String) params.get("orderBy");
        String departmentLevel1Name = (String) params.get("departmentLevel1Name");


        hospitalInfo.put("hospitalId", hospitalId);
        hospitalInfo.put("departmentName", departmentLevel1Name);
        hospitalInfo.put("orderBy", orderBy);
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo,pageSize);
        Page<HashMap<String, Object>> resultPage = doctorInfoService.findPageConsultaDoctorByDepartment(hospitalInfo, page);

        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<HashMap<String, Object>> list = resultPage.getList();
        List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
        List<Map> departmentList = new ArrayList<Map>();
        if (list != null && !list.isEmpty()) {
            for (Map doctorDataVoMap : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("doctorId", doctorDataVoMap.get("id"));
                dataMap.put("doctorName", doctorDataVoMap.get("NAME"));
                dataMap.put("hospitalName",doctorDataVoMap.get("hospital"));
                dataMap.put("position2", doctorDataVoMap.get("position2"));
                dataMap.put("latestTime", doctorDataVoMap.get("latest_time"));
                dataMap.put("available_time",doctorDataVoMap.get("available_time"));
                dataMap.put("star",doctorDataVoMap.get("star"));
                dataMap.put("career_time", String.valueOf((((new Date())
                        .getTime() - ((Date) doctorDataVoMap.get("careerTime"))
                        .getTime()) / (24 * 60 * 60 * 1000)) / 365 + 1));

                String doctorId = (String) doctorDataVoMap.get("id");

                Map<String,Object> expertiseMap = doctorInfoService.getPhoneExpertiseById(doctorId, hospitalId,  null);
                dataMap.put("expertise", expertiseMap.get("expertise"));
                Object departmentLevel2Obj = doctorDataVoMap.get("department_level2");
                boolean departmentLevel2IsEmpty = StringUtils.isEmpty( departmentLevel2Obj == null ? "" : (String) departmentLevel2Obj);
                String departmentFullName = departmentLevel1Name == null ? null : departmentLevel1Name + (departmentLevel2IsEmpty ? "" : "  " + departmentLevel2Obj);
                // 根据医生ID和医院ID，获取医生的所处科室
                dataMap.put("departmentFullName", departmentFullName);
                doctorDataVoList.add(dataMap);

                List<IllnessVo> iVoList =(List<IllnessVo>) expertiseMap.get("iVoList");
                for (IllnessVo vo : iVoList) {
                    Map map = new HashMap();
                    map.put("departmentLevel1Name",vo.getLevel_1());
                    departmentList.add(map);
                }
            }
        }
        response.put("departmentList",departmentList);
        response.put("doctorDataVo", doctorDataVoList);
        return response;
    }

    /**
     * 根据时间查询医生
     * @return Map
     * */
    @RequestMapping(value = "getDoctorListByTime",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String, Object> getDoctorListByTime(@RequestBody Map<String, Object> params){
        HashMap<String, Object> response = new HashMap<String, Object>();
        Date date = DateUtils.formatDate(params);
        String currentPage = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String orderBy = (String) params.get("orderBy");
        String hospitalId = (String) params.get("hospitalId");
        String departmentLevel1Name = (String) params.get("department_level1");
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(
                currentPage, pageSize);
        HashMap<String, Object> dateInfo = new HashMap<String, Object>();
        dateInfo.put("orderBy", orderBy);
        dateInfo.put("date", date);
        dateInfo.put("hospitalId", hospitalId);
        dateInfo.put("department_level1", departmentLevel1Name);

        Page<HashMap<String, Object>> resultPage = doctorInfoService.findPageDoctorByTime4Consult(dateInfo, page);

        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<HashMap<String, Object>> list = resultPage.getList();
        List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (Map doctorDataVoMap : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("doctorId", doctorDataVoMap.get("id"));
                dataMap.put("doctorName", doctorDataVoMap.get("name"));
                dataMap.put("hospitalName",doctorDataVoMap.get("hospital"));
                dataMap.put("position2", doctorDataVoMap.get("position2"));
                dataMap.put("latestTime", doctorDataVoMap.get("latest_time"));
                dataMap.put("available_time",doctorDataVoMap.get("available_time"));
                dataMap.put("star",doctorDataVoMap.get("star"));
                dataMap.put("career_time", String.valueOf((((new Date())
                        .getTime() - ((Date) doctorDataVoMap.get("careerTime"))
                        .getTime()) / (24 * 60 * 60 * 1000)) / 365 + 1));

                String doctorId = (String) doctorDataVoMap.get("id");
//                String hospitalId = (String) doctorDataVoMap.get("sysHospitalId");

                dataMap.put("expertise", doctorInfoService
                        .getDoctorExpertiseById(doctorId, hospitalId, departmentLevel1Name == null ? null : (String) departmentLevel1Name));
                Object departmentLevel2Obj = doctorDataVoMap.get("department_level2");
                boolean departmentLevel2IsEmpty = StringUtils.isEmpty( departmentLevel2Obj == null ? "" : (String) departmentLevel2Obj);
                String departmentFullName = departmentLevel1Name == null ? null : departmentLevel1Name + (departmentLevel2IsEmpty ? "" : "  " + departmentLevel2Obj);
                // 根据医生ID和医院ID，获取医生的所处科室
                dataMap.put("departmentFullName", departmentFullName);
                doctorDataVoList.add(dataMap);
            }
        }
        response.put("doctorDataVo", doctorDataVoList);
        return response;
    }

    /**
     * 根据疾病查询医生
     * @return Map
     * */
    @RequestMapping(value = "getDoctorListByIllness",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String, Object> getDoctorListByIllness(@RequestBody Map<String, Object> params){
        HashMap<String, Object> response = new HashMap<String, Object>();
        String illnessSecondId = (String) params.get("illnessSecondId");
        String hospitalId = (String) params.get("hospitalId");
        String currentPage = ((String) params.get("pageNo"));
        String pageSize = ((String) params.get("pageSize"));
        String orderBy = (String) params.get("orderBy");
        String departmentLevel1Name = (String) params.get("departmentLevel1Name");
//        String department_level1 = (String) params.get("department_level1");
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
                pageSize);

        HashMap<String, Object> secondIllnessInfo = new HashMap<String, Object>();
        secondIllnessInfo.put("orderBy", orderBy);
        secondIllnessInfo.put("illnessSecondId", illnessSecondId);
        secondIllnessInfo.put("hospitalId", hospitalId);
//        secondIllnessInfo.put("department_level1", department_level1);
        Page<HashMap<String, Object>> resultPage = doctorInfoService.listSecondIllnessDoctor4Consult(secondIllnessInfo, page);

        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<HashMap<String, Object>> list = resultPage.getList();
        List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (Map doctorDataVoMap : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("doctorId", doctorDataVoMap.get("id"));
                dataMap.put("doctorName", doctorDataVoMap.get("name"));
                dataMap.put("hospitalName",doctorDataVoMap.get("hospital"));
                dataMap.put("position2", doctorDataVoMap.get("position2"));
                dataMap.put("latestTime", doctorDataVoMap.get("latest_time"));
                dataMap.put("available_time",doctorDataVoMap.get("available_time"));
                dataMap.put("star",doctorDataVoMap.get("star"));
                dataMap.put("career_time", String.valueOf((((new Date())
                        .getTime() - ((Date) doctorDataVoMap.get("careerTime"))
                        .getTime()) / (24 * 60 * 60 * 1000)) / 365 + 1));

                String doctorId = (String) doctorDataVoMap.get("id");
                dataMap.put("expertise", doctorInfoService
                        .getDoctorExpertiseById(doctorId, hospitalId, departmentLevel1Name == null ? null : (String) departmentLevel1Name));
                Object departmentLevel2Obj = doctorDataVoMap.get("department_level2");
                boolean departmentLevel2IsEmpty = StringUtils.isEmpty( departmentLevel2Obj == null ? "" : (String) departmentLevel2Obj);
                String departmentFullName = departmentLevel1Name == null ? null : departmentLevel1Name + (departmentLevel2IsEmpty ? "" : "  " + departmentLevel2Obj);
                // 根据医生ID和医院ID，获取医生的所处科室
                dataMap.put("departmentFullName", departmentFullName);
                doctorDataVoList.add(dataMap);
            }
        }
        response.put("doctorDataVo", doctorDataVoList);

        return response;
    }

    /**
     * 根据专家团查询医生
     * @return Map
     * */
    @RequestMapping(value = "getDoctorListByDoctorGroup",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Page<HashMap<String, Object>> getDoctorListByDoctorGroup(@RequestParam String doctorId){
        Map<String, Object> params = new HashMap<String, Object>();

        return null;
    }

    /**
     * 获取医生信息
     * 基本信息
     * 评价信息
     * 案例
     * @return Map
     * */
    @RequestMapping(value = "doctorDetail",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> doctorDetail(@RequestParam String doctorId){

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("doctorId",doctorId);
        Map<String, Object> response = phoneConsultDoctorRelationService.findDoctorDetailInfo(doctorId);
        ConsulPhonetDoctorRelationVo relationVo = phoneConsultDoctorRelationService.getPhoneConsultRigister(doctorId);
        if(relationVo != null) {
            float price = relationVo.getPrice();
            response.put("price",price);
            response.put("ServerLength", relationVo.getServerLength());
        }
        //获取科室
        response.put("departmentName", hospitalInfoService.getDepartmentFullName(doctorId,
                (String) response.get("hospitalId")));
        //获取从业擅长
        response.put("doctor_expert_desc", doctorInfoService.getDoctorCardExpertiseById(doctorId));

        //评价
        params.put("evaluateType", "1");
        params.put("limit", 1);//默认获取一条评论
        HashMap<String,Object> evaluaMap = patientRegisterPraiseService.getDoctorEvaluateTop(params);
        HashMap<String, Object> doctorScore = doctorInfoService.findDoctorScoreInfo(doctorId);
        response.put("doctorScore", doctorScore);
        response.put("evaluaMap",evaluaMap);
        response.put("evaluateTotal", patientRegisterPraiseService.getTotalCount(params));//评论总数

        //获取医生的案例信息
        List<DoctorCaseVo> doctorCaseVos = doctorCaseService.findDoctorCase((String) params.get("doctorId"));
        int sumcase = 0;
        int otherCase = 0;
        if (doctorCaseVos.size() > 0) {
            sumcase = doctorCaseService.findDoctorCaseNumber((String) params.get("doctorId"));
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

        response.put("otherCase", otherCase);//其他案例数
        response.put("sumcase", sumcase);//总案例数
        response.put("doctorCaseList", doctorCaseVos);//医生案例
        return response;
    }

    @RequestMapping(value = "earliestVisiteInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> earliestVisiteInfo(@RequestParam String doctorId){

        Map<String,Object> resultMap = registerService.getEarliestVisiteInfo(doctorId);
        resultMap.size();
        return resultMap;
    }

}
