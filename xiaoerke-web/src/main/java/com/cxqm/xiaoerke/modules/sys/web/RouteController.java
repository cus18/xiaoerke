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
@RequestMapping(value = "${xiaoerkePath}/firstPage")
public class RouteController extends BaseController {

    @Autowired
    private BabyEmrService babyEmrService;

    @Autowired
    private PlanInfoService planInfoService;

    @Autowired
    private OperationHandleService operationHandleService;

    /**
     * appoint 预约 原生首页
     */
    @RequestMapping(value ="/appoint",method = {RequestMethod.POST, RequestMethod.GET})
    public String appointmentFirstPage() {
        return "appointmentFirstPage";
    }
    /**
     * phoneConsult 电话咨询 原生首页
     */
    @RequestMapping(value ="/phoneConsult",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultFirstPage() {
        return "phoneConsultFirstPage";
    }
    /**
     * phoneConsult 电话咨询 原生首页 试验
     */
    @RequestMapping(value ="/phoneConsult1",method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultFirstPage1() {
        return "phoneConsultFirstPage1";
    }
    /**
     * 应用首页
     */
    @RequestMapping(value ="/knowledge",method = {RequestMethod.POST, RequestMethod.GET})
    public String knowledgeFirstPage(HttpServletRequest request,HttpSession session) {
        LogUtils.saveLog(Servlets.getRequest(), "00000079");//进入预约医生主页
        String openId = WechatUtil.getOpenId(session, request);
        List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(list.size()!=0){
            return "knowledgeFirstPage";
        }else{
            return "knowledgeLoginPage";
        }
    }

    @RequestMapping(value ="/knowledgeLogin",method = {RequestMethod.POST, RequestMethod.GET})
    public String knowledgeLoginPage() {
        return "knowledgeLoginPage";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/healthPlan",method = {RequestMethod.POST, RequestMethod.GET})
    public String healthPlanIndex() {
        return "healthPlanFirstPage";
//        Map<String, Object> planInfoMap = new HashMap<String, Object>();
//        if(UserUtils.getUser().getId()!=null){
//            List<String> list = new ArrayList<String>();
//            list.add("ongoing");
//            planInfoMap.put("userId", UserUtils.getUser().getId());
//            planInfoMap.put("statusList",list);
//            List<Map<String,Object>> manageList = planInfoService.getPlanInfoByUserId(planInfoMap);
//
//            if(manageList.size()==0){
//                return "healthPlanFirstPage";
//            }else if(manageList.size()==1){
//                if(manageList.get(0).get("name").equals("便秘计划")){
////                    HashMap<String,Object> param = new HashMap<String,Object>();
////                    param.put("logContent","healthPlanFirstPageAccess");
////                    param.put("userId", UserUtils.getUser().getId());
////                    Map result = operationHandleService.getLogInfoByLogContent(param);
////                    if(result.size()!=0){
////                        return "redirect:/ap/ctp#/constipationIndex";
////                    }else{
////                        return "healthPlanFirstPage";
////                    }
//                    return "redirect:/ap/ctp#/constipationIndex";
//                }else if(manageList.get(0).get("name").equals("营养管理")){
////                    HashMap<String,Object> param = new HashMap<String,Object>();
////                    param.put("logContent","healthPlanFirstPageAccess");
////                    param.put("userId", UserUtils.getUser().getId());
////                    Map result = operationHandleService.getLogInfoByLogContent(param);
////                    if(result.size()==0){
////                        return "redirect:/ap/ntr#/nutritionIndex";
////                    }else{
////                        return "healthPlanFirstPage";
////                    }
//                    return "redirect:/ap/ntr#/nutritionIndex";
//                }else{
//                    return "healthPlanFirstPage";
//                }
//            }else{
//                return "healthPlanFirstPage";
//            }
//        }else{
//            return "healthPlanFirstPage";
//        }
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/doctor",method = {RequestMethod.POST, RequestMethod.GET})
    public String doctorIndex() {
        return "doctorIndex";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/momNutritionTest",method = {RequestMethod.POST, RequestMethod.GET})
    public String momNutritionTestFirst() {
        return "momNutritionTestFirst";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/antiDogFirst",method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogFirst() {
        return "antiDogFirstPage";
    }

    /**
     * 应用首页
     */
    @RequestMapping(value ="/antiDogLead",method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogLead() {
        return "antiDogFirstLead";
    }

    /**
     * 咨询打赏
     */
    @RequestMapping(value ="/playtour",method = {RequestMethod.POST, RequestMethod.GET})
    public String playTour() {
        return "playtourIndex";
    }

}
