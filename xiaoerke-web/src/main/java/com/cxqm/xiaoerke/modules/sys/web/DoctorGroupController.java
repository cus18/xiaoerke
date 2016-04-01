/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生集团Controller
 *
 * @author chenjiake
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class DoctorGroupController extends BaseController {

    @Autowired
    private DoctorGroupInfoService doctorGroupInfoService;

    @Autowired
    private RegisterService registerService;

    /**
     * 获取系统中的医生集团列表
     * [
     *     pageNo: 1,
     *     pageSize: 20,
     *     doctorGroupList:[
     *    {doctorGroupId:1,doctorGroupName:北大口腔医院医生集团,description:fajklfewf,expertise:fewfioifew,doctorIdList:rw3r3;rewrw;r3232r}
     *    {doctorGroupId:2,doctorGroupName:北大口腔医院医生集团,description:fajklfewf,expertise:fewfioifew,doctorIdList:rw3r3;rewrw;r3232r}
     *    {doctorGroupId:3,doctorGroupName:北大口腔医院医生集团,description:fajklfewf,expertise:fewfioifew,doctorIdList:rw3r3;rewrw;r3232r}
     *    {doctorGroupId:4,doctorGroupName:北大口腔医院医生集团,description:fajklfewf,expertise:fewfioifew,doctorIdList:rw3r3;rewrw;r3232r}
     *    ]
     * ]
     * */
    @RequestMapping(value = "/sys/doctor/group", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000073")
    Map<String, Object> getDoctorGroupList(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String,Object>();
        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String orderBy = (String) params.get("orderBy");
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo, pageSize);
        HashMap<String, Object> param = new HashMap<String, Object>();
        HashMap<String, Object> resultPage = doctorGroupInfoService.findPageAllDoctorGroup(page,param);
        response.put("pageNo",pageNo);
        response.put("pageSize",pageSize);
        response.put("doctorGroupList",resultPage.get("doctorGroupList"));
        return response;
    }

    /*
     *  [doctorGroupId:1,doctorGroupName:北大口腔医院医生集团,description:fajklfewf,expertise:fewfioifew]
     *
     * */
    @RequestMapping(value = "/sys/doctor/group/info", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000073")
    Map<String, Object> getDoctorGroupInfo(@RequestParam(required=true) String doctorGroupId) {
        HashMap<String, Object> response = new HashMap<String,Object>();
        response = doctorGroupInfoService.getDoctorGroupInfo(doctorGroupId);
        return response;
    }

    @RequestMapping(value = "/sys/doctor/group/doctorList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000073")
    Map<String, Object> getDoctorListInDoctorGroup(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();

        String doctorGroupId = (String) params.get("doctorGroupId");
        String currentPage = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String orderBy = (String) params.get("orderBy");

        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
                pageSize);

        HashMap<String, Object> doctorMap = new HashMap<String, Object>();
        doctorMap.put("doctorGroupId", doctorGroupId);
        doctorMap.put("orderBy", orderBy);

        Page<HashMap<String, Object>> resultPage = doctorGroupInfoService.findDoctorByDoctorGroup(doctorMap, page);

        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<HashMap<String, Object>> list = resultPage.getList();
        List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
        registerService.generateDoctorDataVoList(list, doctorDataVoList);
        response.put("doctorDataVo", doctorDataVoList);

        return response;
    }
}