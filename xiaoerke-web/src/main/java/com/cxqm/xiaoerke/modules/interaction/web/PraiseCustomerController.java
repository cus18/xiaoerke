/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "interaction")
public class PraiseCustomerController extends BaseController {

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @RequestMapping(value = "/user/customerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String customerEvaluation(@RequestBody Map<String, Object> params, HttpSession session) {
        String openId = UserUtils.getUser().getOpenid();
        return patientRegisterPraiseService.customerEvaluation(params, StringUtils.isNotNull(openId)?openId:"");
    }
    
    /**
     * 新版评价
     * @author 张博
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/updateCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String updateCustomerEvaluation(@RequestBody Map<String, Object> params) {
        return patientRegisterPraiseService.updateCustomerEvaluation(params)+"";
    }

    /**
     * 新版评价
     * @author 张博
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/findCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findCustomerEvaluation(@RequestBody Map<String, Object> params, HttpSession session) {
    	String id=params.get("id").toString();
    	Map<String, Object> result =new HashMap<String, Object>();
    	Map<String, Object> map=patientRegisterPraiseService.selectCustomerEvaluation(id);
        session.setAttribute("openid",map.get("openid"));
    	String doctorId=map.get("doctorId").toString();
    	result.put("evaluation", map);
    	result.put("starInfo", patientRegisterPraiseService.getCustomerStarInfoById(doctorId));
        result.put("doctorHeadImage",patientRegisterPraiseService.getDoctorHeadImageURIById(doctorId));
        return result;
    }
    
}
