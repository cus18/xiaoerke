/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.member.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员Controller
 *
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "member/user")
public class MemberController extends BaseController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private RegisterService registerService;
    @RequestMapping(value = "/extendMemberService", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> extendMemberService(@RequestParam(required = true) String memberType,
                                                HttpSession session,
                                                HttpServletRequest request) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String, Object> response = new HashMap<String, Object>();
        LogUtils.saveLog(Servlets.getRequest(), "00000063","用户通过推广码来获取" + memberType + "会员服务");

        Boolean orderRelation = patientRegisterService.judgeUserOrderRealtion(session, request);
        if (orderRelation) {
            //该用户已有订单记录
            response.put("result", "false");
        } else {
            String openId = WechatUtil.getOpenId(session, request);
            User user = UserUtils.getUser();
            Boolean status = memberService.produceExtendMember(memberType, openId, user.getId(), memberType, "");
            if (status == true) {
                List<MemberservicerelItemservicerelRelationVo> memberservicerelItemservicerelRelationVos =
                        memberService.findMemberPropertyAppAvailable();
                if (memberservicerelItemservicerelRelationVos != null && memberservicerelItemservicerelRelationVos.size() > 0) {
                    response.put("result", "true");
                    response.put("startDate", DateUtils.formatDateToStr(memberservicerelItemservicerelRelationVos.get(0).getActivateDate(), "yyyy/MM/dd"));
                    response.put("endDate", DateUtils.formatDateToStr(memberservicerelItemservicerelRelationVos.get(0).getEndDate(), "yyyy/MM/dd"));
                    response.put("leftTimes", memberservicerelItemservicerelRelationVos.get(0).getLeftTimes());
                } else {
                    response.put("result", "false");
                }
            } else if (status == false) {
                response.put("result", "false");
            }
        }
        return response;
    }

    @SystemServiceLog(description = "00000062")
    @RequestMapping(value = "/checkIfAppScanDoctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkIfAppScanDoctor(@RequestBody Map<String, Object> params,
                                             HttpSession session,
                                             HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        String openId = WechatUtil.getOpenId(session, request);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "1");
        if (!StringUtils.isNotNull(openId)) {
            openId = CookieUtils.getCookie(request, "openId");
            if (!StringUtils.isNotNull(openId)) {
                response.put("status", "5");
                return response;
            }
        }

        String register_service_id = (String) params.get("register_service_id");
        int chargePrice = patientRegisterService.getNeedPayStatusByRegisterService(register_service_id, openId);
        if (chargePrice == 0) {
            response.put("needPay", "false");
        } else {
            HashMap<String, Object> map = registerService.getCooperationHospitalTypeBySrsId(register_service_id);
            if(map==null) {
                response.put("needPay", "true");
            }
            else {
                if(map.get("hospitalType").equals("2"))
                {
                    response.put("needPay", "tehui");
                }
                else{
                    response.put("needPay", "true");
                }
            }
        }
        return response;
    }


    @RequestMapping(value = "/orderFreePayOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderFreePayOperation(@RequestParam(required = true) String patient_register_service_id,
                                              HttpSession session,
                                              HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String, Object> response = new HashMap<String, Object>();

        String openId = WechatUtil.getOpenId(session, request);
        LogUtils.saveLog(Servlets.getRequest(),"00000064" ,"用户预约扫码医生" + patient_register_service_id);
        patientRegisterService.orderFreePayOperation(openId, patient_register_service_id);
        response.put("patient_register_service_id", patient_register_service_id);
        response.put("status", '1');
        return response;
    }

    /****
     * 状态1，会员已过期，免费卷已过期，免费卷没用完
     * 状态2，会员已过期，免费卷已过期，免费卷已用完
     * 状态3，会员已过期，免费卷没过期，免费卷已用完
     * 状态4，会员已过期，免费卷没过期，免费卷没用完
     * 状态5，会员没过期，免费卷已过期，免费卷没用完
     * 状态6，会员没过期，免费卷已过期，免费卷已用完
     * 状态7，会员没过期，免费卷没过期，免费卷已用完
     * 状态8，会员没过期，免费卷没过期，免费卷没用完
     * 状态9，会员没过期，免费卷已过期，免费卷没用完，且用户从没有过订单
     * 状态10，会员已过期，免费卷已过期，免费卷没用完，且用户从没有过订单
     * 状态11，用户没有任何会员服务，且用户从没有过订单
     ***/
    @RequestMapping(value = "/getMemberServiceStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> GetMemberServiceStatus(HttpSession session, HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        String status = memberService.getMemberServiceStatus("");
        if (status.equals("")) {
            Boolean orderRelation = patientRegisterService.judgeUserOrderRealtion(session, request);
            if (!orderRelation)//该用户没有订单记录 9  10
            {
                status = "11";
                String openId = WechatUtil.getOpenId(session, request);
                User user = UserUtils.getUser();
                memberService.produceExtendMember("week", openId, user.getId(), "week", "");
            } else {
                status = "7";
            }
        } else if (status.equals("5") || status.equals("1")) {
            Boolean orderRelation = patientRegisterService.judgeUserOrderRealtion(session, request);
            if (!orderRelation)//该用户没有订单记录 9  10
            {
                String openId = (String) session.getAttribute("openId");
                if (!StringUtils.isNotNull(openId)) {
                    openId = CookieUtils.getCookie(request, "openId");
                }
                User user = UserUtils.getUser();
                memberService.produceExtendMember("week", openId, user.getId(), "week", "");
                status = "9";
            }
        }

        response.put("status", status);
        return response;
    }

    @RequestMapping(value = "/getUserMemberService", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserMemberService() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        List<MemberservicerelItemservicerelRelationVo> list = memberService.findMemberProperty();// .findMemberPropertyAppAvailable();
        List<HashMap<String, Object>> memberServiceList = new ArrayList<HashMap<String, Object>>();
        if (null != list) {
            for (MemberservicerelItemservicerelRelationVo map : list) {
                HashMap<String, Object> member = new HashMap<String, Object>();
                member.put("leftTimes", map.getLeftTimes());
                member.put("startDate", DateUtils.formatDateToStr(map.getActivateDate(), "yyyy/MM/dd"));
                member.put("endDate", DateUtils.formatDateToStr(map.getEndDate(), "yyyy/MM/dd"));
                member.put("period", String.valueOf(map.getPeriod()));
                member.put("memSrsItemSrsRelId", map.getId());
                memberServiceList.add(member);
            }
        }
        response.put("memberServiceList", memberServiceList);
        return response;
    }

    @RequestMapping(value = "/orderPayMemberServiceOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> OrderPayMemberServiceOperation(@RequestParam(required = true) String patient_register_service_id,
                                                       @RequestParam(required = true) String memSrsItemSrsRelId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String, Object> response = new HashMap<String, Object>();
        //判断订单状态
        PatientRegisterServiceVo patientRegisterServiceVo = patientRegisterService.selectByPrimaryKey(patient_register_service_id);
        if (patientRegisterServiceVo != null && patientRegisterServiceVo.getStatus().equals("0")) {
            //扣除会员预约次数
            HashMap<String, Object> map = patientRegisterService.updateMemberOrderStatus(patient_register_service_id, memSrsItemSrsRelId);
            if (map != null && map.size() > 0) {
                response.put("status", map.get("status"));
            }
        } else {
            response.put("status", "2");
        }
        return response;

    }

    @RequestMapping(value = "/checkUserFirstOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkUserFirstOrder(HttpSession session, HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();

        Boolean orderRelation = patientRegisterService.judgeUserOrderRealtion(session, request);
        if (orderRelation) {
            //该用户已有订单记录
            response.put("result", "false");
        } else {
            response.put("result", "true");
        }
        return response;

    }

    @RequestMapping(value = "/memberServiceDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> memberServiceDetail(@RequestParam(required = true) String memberServiceId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        MemberservicerelItemservicerelRelationVo memberVo = memberService.findMemberProperty(memberServiceId);
        response.put("memberVo", memberVo);
        return response;
    }

}
