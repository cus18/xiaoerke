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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        return "pay";
    }

    /**
     *
     * 处理来自微信服务器的请求
     *
     */
    @RequestMapping(value = "/antiDogPay/patientPay.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String antiDogPay(@RequestParam(required=false) String patient_register_service_id,
                             @RequestParam(required=false) String chargePrice,
                             HttpServletResponse response) {
//        if(!StringUtils.isNotNull(patient_register_service_id)){
//            patient_register_service_id = "noData";
//        }
//        CookieUtils.setCookie(response,"patient_register_service_id", patient_register_service_id);
//        CookieUtils.setCookie(response, "chargePrice", chargePrice);
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
   public String customerPay(@RequestParam(required=false) String patient_register_service_id,
                            @RequestParam(required=false) String chargePrice,
                            HttpServletResponse response) {
       return "customerPay";
   }
    
}
