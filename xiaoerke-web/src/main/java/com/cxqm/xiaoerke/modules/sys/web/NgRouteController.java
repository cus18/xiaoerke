/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 工具 Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "")
public class NgRouteController extends BaseController {

    @RequestMapping(value = "patient/{routeURL}/{routeParam}", method = { RequestMethod.GET })
    public String angualrjsRoute(@PathVariable("routeURL") String routeURL,@PathVariable("routeParam") String routeParam) {
        String redirectParam = routeURL + "/" + routeParam;
        return "redirect:"+"/ap#/"+redirectParam;
    }

    /*
        angel 独立咨询系统 项目
     */

    /**
     * 独立咨询系统 医生端
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="doctor/consult",method = {RequestMethod.POST, RequestMethod.GET})
    public String doctorConsult() {
        return "doctorConsultIndex";
    }

    /**
     * 独立咨询系统 用户端
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="patient/consult",method = {RequestMethod.POST, RequestMethod.GET})
    public String patientConsult() {
        return "patientConsultIndex";
    }

    /*
        titan 项目
    */

    /**
     *  appoint 客户端预约
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/appoint",method = {RequestMethod.POST, RequestMethod.GET})
    public String appointIndex() {
        return "angular/appointIndex";
    }

    /**
     *  phoneConsult 电话咨询
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/phoneConsult",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultIndex() {
        return "angular/phoneConsultIndex";
    }

    /**
     *  baoFansCamp 宝粉营
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/baoFansCamp",method = {RequestMethod.POST, RequestMethod.GET})
    public String baoFansIndex() {
        return "angular/baoFansCampIndex";
    }

    /**
     * insurance 类保险
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/insurance",method = {RequestMethod.POST, RequestMethod.GET})
    public String insuranceIndex() {
        return "angular/insuranceIndex";
    }
    /**
     * olympicBaby 奥运宝贝
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/olympicBaby",method = {RequestMethod.POST, RequestMethod.GET})
    public String olympicBabyIndex() {
        return "angular/olympicBabyIndex";
    }

    /*
        wisdom 项目
    */

    /**
     *  PC端官网
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/baodaifuFirst",method = {RequestMethod.POST, RequestMethod.GET})
    public String baodaifuIndex() {
        return "angular/baodaifuFirstPage";
    }

    /**
     *  PC端官网2
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/baodaifu",method = {RequestMethod.POST, RequestMethod.GET})
    public String baodaifuIndex2() {
        return "angular/baodaifuFirstPage2";
    }

    /**
     *   knowledge 知识库
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/knowledge",method = {RequestMethod.POST, RequestMethod.GET})
    public String knowledgeIndex() {
        return "angular/knowledgeIndex";
    }


    /**
     * constipation 便秘管理
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/ctp",method = {RequestMethod.POST, RequestMethod.GET})
    public String constipationPlanIndex() {
        return "angular/constipationIndex";
    }

    /**
     *  nutrition 营养管理
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/ntr",method = {RequestMethod.POST, RequestMethod.GET})
    public String nutritionPlanIndex() {
        return "angular/nutritionIndex";
    }

    /**
     *  lovePlan
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/lovePlan",method = {RequestMethod.POST, RequestMethod.GET})
    public String lovePlanIndex() {
        return "angular/lovePlanIndex";
    }
    /**
     *  heightForecast
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/heightForecast",method = {RequestMethod.POST, RequestMethod.GET})
    public String heightForecastIndex() {
        return "angular/heightForecastIndex";
    }

    /**
     *  umbrella 宝护伞
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/umbrella",method = {RequestMethod.POST, RequestMethod.GET})
    public String umbrellaIndex() {
        return "angular/umbrellaIndex";
    }

    /**
     *  umbrella 外接app宝护伞
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/appUmbrella",method = {RequestMethod.POST, RequestMethod.GET})
    public String appUmbrellaIndex() {
        return "angular/appUmbrellaIndex";
    }

     /*
        doctor 项目
    */

    /**
     * doctor 医生个人中心
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/doctor",method = {RequestMethod.POST, RequestMethod.GET})
    public String doctorIndex() {
        return "angular/doctorIndex";
    }

    /**
     *  phoneConsultDoctor 医生端电话咨询
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/phoneConsultDoctor",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultDoctorIndex() {
        return "angular/phoneConsultIndex";
    }


      /*
        market 项目
     */

    /**
     *  market 运营活动
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/market",method = {RequestMethod.POST, RequestMethod.GET})
    public String marketIndex() {
        return "angular/marketIndex";
    }


     /*
        keeper 项目
    */

    /**
     * healthManage 微信多客服 健康档案
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/health",method = {RequestMethod.POST, RequestMethod.GET})
    public String healthManageIndex() {
        return "angular/healthManage";
    }

    /**
     * 咨询打赏
     */
    @SystemControllerLog(description = "00000079")
    @RequestMapping(value ="/playtour",method = {RequestMethod.POST, RequestMethod.GET})
    public String playtourIndex() {
        return "angular/playtourManage";
    }
}

