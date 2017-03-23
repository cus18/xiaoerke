/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.wechat.web;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping
public class PayController {

    /**
     *
     * 处理来自微信服务器的请求
     *
     */
    @RequestMapping(value = "/pay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String patientPay(@RequestParam(required=false) String patient_register_service_id,
                            @RequestParam(required=false) String chargePrice,
                            HttpServletResponse response) {
        if(!StringUtils.isNotNull(patient_register_service_id)){
            patient_register_service_id = "noData";
        }
        CookieUtils.setCookie(response,"patient_register_service_id", patient_register_service_id);
        CookieUtils.setCookie(response, "chargePrice", chargePrice);
        return "pay/pay";
    }
    /**
     *
     * 处理来自微信服务器的请求
     * 宝护伞
     *
     */
    @RequestMapping(value = "/umbrellaPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String umbrellaPay() {
        return "umbrella";
    }
    /**
     *
     * 处理来自微信服务器的请求
     * 爱心传递计划
     *
     */
    @RequestMapping(value = "/lovePlanPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String lovePlanPay() {
        return "lovePlan";
    }
    /**
     *
     * 处理来自微信服务器的请求
     * 防犬宝
     *
     */
    @RequestMapping(value = "/antiDogPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogPay() {
        return "antiDogPay";
    }
    /**
     *
     * 处理来自微信服务器的请求
     * 电话咨询
     *
     */
    @RequestMapping(value = "/phoneConsultPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String phoneConsultPay(@RequestParam(required=false) String phoneConDoctorDetail,
                             HttpServletResponse response) {
        CookieUtils.setCookie(response,"phoneConDoctorDetail", phoneConDoctorDetail);
        return "phoneConsultPay";
    }
    /**
     *
     * 处理来自微信服务器的请求
     * 电话咨询 订单详情页 去支付
     *
     */
    @RequestMapping(value = "/orderDetailPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String orderDetailPay(@RequestParam(required=false) String orderDetailPay,
                                  HttpServletResponse response) {
        CookieUtils.setCookie(response,"orderDetailPay", orderDetailPay);
        return "orderDetailPay";
    }
    
    /**
    *
    * 处理来自微信服务器的请求
    *
    */
   @RequestMapping(value = "/customerPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
   public String customerPay() {
       return "customerPay";
   }

    /**
     *
     * 处理来自微信服务器的请求
     *
     */
    @RequestMapping(value = "/wxPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String wxPay(@RequestParam(required=true) String serviceType,
                        @RequestParam(required=false) String phoneConDoctorDetail,
                        @RequestParam(required=false) String doctorId,
                        Model model) {

        if(serviceType.equals("antiDogPay")){
            model.addAttribute("payPrice", 19.8);
            model.addAttribute("intervalFlag", "1");
            return "insurance/antiDogPay";
        }
        else if(serviceType.equals("phoneConsult")){
            model.addAttribute("ceshi", "");
            model.addAttribute("phoneConDoctorDetail", phoneConDoctorDetail);
            model.addAttribute("doctorId", doctorId);
            return "pay/phoneConsultPay";
        }
        else if(serviceType.equals("appointment")){
            model.addAttribute("payPrice", 200);
            model.addAttribute("intervalFlag", "1");
            return "appointmentPay";
        }
        else if(serviceType.equals("orderDetail")){
           return  "pay/orderDetailPay";
        }
        else if(serviceType.equals("customerPay")){
            return  "native/evaluateSendHeart";
        }
        else if(serviceType.equals("playtourPay")){
            return  "native/evaluateFinish";
        }
        else if("handfootmouth".equals(serviceType)){
            model.addAttribute("payPrice", 26.8);
            model.addAttribute("intervalFlag", "1");
            return "insurance/handfootmouthPay";
        }
        else if("pneumonia".equals(serviceType)){
            model.addAttribute("payPrice", 68);
            model.addAttribute("intervalFlag", "1");
            return "insurance/pneumoniaPay";
        }
        else if("umbrellaPay".equals(serviceType)){
            model.addAttribute("payPrice", 5.0);
            model.addAttribute("intervalFlag", "1");
            return "pay/umbrellaPay";
        }else if("lovePlanPay".equals(serviceType)){
            return "pay/lovePlanPay";
        }else if("doctorConsultPay".equals(serviceType)){
            return "pay/doctorConsultPay";
        }else if("nonRealTime".equals(serviceType)){
            model.addAttribute("payPrice",8);
            return "pay/nonRealTimeConsultPay";
        }else if("payPunchCardCash".equals(serviceType)){
            return "pay/signPay";
        }
        else{
            return null;
        }
    }
    
}
