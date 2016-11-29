/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
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
import java.util.*;

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

    /*
        titan 项目
    */

    /**
     * appoint 预约挂号
     */
    @RequestMapping(value ="/appoint",method = {RequestMethod.POST, RequestMethod.GET})
    public String appointmentFirstPage() {
        return "native/appointmentFirstPage";
    }

    /**
     * phoneConsult 电话咨询
     */
    @RequestMapping(value ="/phoneConsult",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultFirstPage() {
        return "native/phoneConsultFirstPage";
    }

    /**
     * antiDog 防犬宝
     */
    @RequestMapping(value ="/antiDogFirst",method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogFirst() {
        return "native/antiDogFirstPage";
    }

    /**
     * insurance 类保险
     */
    @RequestMapping(value ="/insurance",method = {RequestMethod.POST, RequestMethod.GET})
    public String insuranceFirst() {
        return "native/insuranceFirstPage";
    }
    /**
     * olympicBaby 奥运宝贝
     */
    @RequestMapping(value ="/olympicBaby",method = {RequestMethod.POST, RequestMethod.GET})
    public String olympicBabyFirst() {
        return "native/olympicBabyFirstPage";
    }

     /*
        wisdom 项目
     */

    /**
     * 知识库
     */
    @RequestMapping(value ="/knowledge",method = {RequestMethod.POST, RequestMethod.GET})
    public String knowledgeFirstPage(HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

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
     * 健康管理
     */
    @RequestMapping(value ="/healthPlan",method = {RequestMethod.POST, RequestMethod.GET})
    public String healthPlanIndex() {
        return "native/healthPlanFirstPage";
    }

    /**
     * 宝护伞 宣传页
     */
    @RequestMapping(value ="/umbrellaSwipe",method = {RequestMethod.POST, RequestMethod.GET})
    public String umbrellaSwipe() {
        return "swipe/umbrellaSwipe";
    }

    /**
     * 宝护伞
     */
    @RequestMapping(value ="/umbrella",method = {RequestMethod.POST, RequestMethod.GET})
    public String umbrellaIndex() {
        return "native/umbrellaFirstPage";
    }

    /*
        market 项目
    */

    /**
     * market 妈妈营养测试
     */
    @RequestMapping(value ="/momNutritionTest",method = {RequestMethod.POST, RequestMethod.GET})
    public String momNutritionTestFirst() {
        return "native/momNutritionTestFirst";
    }

    /**
     * market 宝妈爱心接力
     */
    @RequestMapping(value ="/lovePlan",method = {RequestMethod.POST, RequestMethod.GET})
    public String lovePlanFirst() {
        return "native/lovePlanFirst";
    }

    /**
     * wisdom 身高预测
     */
    @RequestMapping(value ="/heightForecast",method = {RequestMethod.POST, RequestMethod.GET})
    public String heightForecastFirst() {
        return "native/heightForecastFirst";
    }


     /*
        keeper 项目
    */

   /* *//**
     * 咨询打赏
     *//*
    @RequestMapping(value ="/playtour",method = {RequestMethod.POST, RequestMethod.GET})
    public String playTour() {
        return "native/playtourIndex";
    }*/

    /**
     * 咨询打赏评价完成
     *//*
    @RequestMapping(value ="/playtourEvaluate",method = {RequestMethod.POST, RequestMethod.GET})
    public String playTourEval() {
        return "native/playtourEvaluate";
    }*/
}
