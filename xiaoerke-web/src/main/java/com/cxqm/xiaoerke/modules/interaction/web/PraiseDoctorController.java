/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 医生评价Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "interaction")
public class PraiseDoctorController extends BaseController {

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    /**
     * appointment所有，未分页
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/getUserEvaluate", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserEvaluate(@RequestBody Map<String, Object> params) {

        HashMap<String, Object> response = new HashMap<String, Object>();
        patientRegisterPraiseService.getUserEvaluate(params,response);
        LogUtils.saveLog(Servlets.getRequest(), "00000049","获取医生的评价信息:" + params.get("doctorId"));
        return response;
    }

    /**
     * 通用，带分页
     * @params(pageNo,pageSize,evaluateType,doctorId)
     *
     * @return Map
     * @author chenxiaoqiong
     * */
    @RequestMapping(value = "/user/evaluateDoctor",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> evaluateDoctor(@RequestBody HashMap<String, Object> params){
        HashMap<String, Object> response = new HashMap<String, Object>();
        response = patientRegisterPraiseService.getConsultEvaluate(params);
        LogUtils.saveLog(Servlets.getRequest(), "00000049","获取医生的评价信息:" + params.get("doctorId"));
        return response;
    }
}
