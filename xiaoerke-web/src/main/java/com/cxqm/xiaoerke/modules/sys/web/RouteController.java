/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;
import com.cxqm.xiaoerke.modules.cms.service.BabyEmrService;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具 Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "firstPage")
public class RouteController extends BaseController {

    @Autowired
    private BabyEmrService babyEmrService;

    /**
     * appoint 预约 原生首页
     */
    @RequestMapping(value ="/appoint",method = {RequestMethod.POST, RequestMethod.GET})
    public String appointmentFirstPage() {
        return "native/appointmentFirstPage";
    }
    /**
     * phoneConsult 电话咨询 原生首页
     */
    @RequestMapping(value ="/phoneConsult",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultFirstPage() {
        return "native/phoneConsultFirstPage";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/knowledge",method = {RequestMethod.POST, RequestMethod.GET})
    public String knowledgeFirstPage(HttpServletRequest request,HttpSession session) {
        String openId = WechatUtil.getOpenId(session, request);
        List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(list.size()!=0){
            return "native/knowledgeFirstPage";
        }else{
            return "native/knowledgeLoginPage";
        }
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/healthPlan",method = {RequestMethod.POST, RequestMethod.GET})
    public String healthPlanIndex() {
        return "native/healthPlanFirstPage";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/momNutritionTest",method = {RequestMethod.POST, RequestMethod.GET})
    public String momNutritionTestFirst() {
        return "native/momNutritionTestFirst";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/antiDogFirst",method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogFirst() {
        return "native/antiDogFirstPage";
    }

    /**
     * 咨询打赏
     */
    @RequestMapping(value ="/playtour",method = {RequestMethod.POST, RequestMethod.GET})
    public String playTour() {
        return "playtourIndex";
    }

}
