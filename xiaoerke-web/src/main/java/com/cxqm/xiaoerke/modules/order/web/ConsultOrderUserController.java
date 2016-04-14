package com.cxqm.xiaoerke.modules.order.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.utils.DateUtil;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.exception.CreateOrderException;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/18.
 * 电话咨询订单
 */
@Controller
@RequestMapping(value = "consultPhone/")
public class ConsultOrderUserController {

    @Autowired
    private ConsultPhonePatientService consultPhonePatientService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private ConsultPhoneOrderService consultPhoneOrderService;

    @Autowired
    private SystemService systemService;

    /**
     * 根据订单号查询订单详情
     * @param phoneConsultaServiceId
     *
     * @return map{date:"2016-03-31",beginTime:"15:54:19",endTime:"15:54:21",phone:"131.."}
     * */
    @RequestMapping(value = "consultOrder/getOrderInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> getConsultPhoneRegisterInfoById(@RequestParam Integer phoneConsultaServiceId){
        return consultPhonePatientService.getPatientRegisterInfo(phoneConsultaServiceId);
    }


    /**
     * 电话咨询订单列表
     * @return Map
     * */
    @RequestMapping(value = "consultOrder/getOrderList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getConsultPhoneList(){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String userId = UserUtils.getUser().getId();
        List<HashMap<String,Object>> orderList = consultPhonePatientService.getOrderList(userId);
        resultMap.put("orderList",orderList);
        return resultMap;
    }

    /**
     * 订单列表，当前订单
     * @param
     * @return
     */
    @RequestMapping(value = "/order/user/orderListAll", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> myselfInfoOrderListAll(Map<String,Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        return consultPhoneOrderService.getOrderListAll(params);
    }

    /**
     * 电话咨询订单列表
     * @return Map
     * @author chenxiaoqiong
     * */
    @RequestMapping(value = "order/user/orderList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getConsultPhonePageList(Map<String,Object> params){
        return consultPhoneOrderService.getUserOrderPhoneConsultList(params);
    }


    /**
     * 生成订单
     * */
    @RequestMapping(value = "consultOrder/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> createOrder(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session){
        String babyId =(String)params.get("babyId");
        String  babyName =(String)params.get("babyName");
        String  phoneNum =(String)params.get("phoneNum");
        String  illnessDesc =(String)params.get("illnessDesc");
        Integer  sysConsultPhoneId =Integer.parseInt((String)params.get("sysConsultPhoneId"));
        Date  birthDay = DateUtils.StrToDate((String)params.get("birthDay"),"yyyy/MM/dd");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String openid = UserUtils.getUser().getOpenid();
        int reultState = 0;
        try {
            reultState = consultPhonePatientService.PatientRegister(openid, babyId, babyName, birthDay, phoneNum, illnessDesc, sysConsultPhoneId);
            Map<String,Object> consultOrder = consultPhonePatientService.getPatientRegisterInfo(reultState);
            String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String)consultOrder.get("date"),"yyyy/MM/dd"));
            PatientMsgTemplate.consultPhoneSuccess2Msg((String) consultOrder.get("babyName"), (String) consultOrder.get("doctorName"), (String) consultOrder.get("date"), week, (String) consultOrder.get("beginTime"), (String) consultOrder.get("phone"), (String) consultOrder.get("orderNo"));
            String openId = WechatUtil.getOpenId(session, request);
            Map<String,Object> parameter = systemService.getWechatParameter();
            String token = (String)parameter.get("token");
            PatientMsgTemplate.consultPhoneSuccess2Wechat((String)consultOrder.get("doctorName"),(String)consultOrder.get("date"),week,(String)consultOrder.get("beginTime"),(String)consultOrder.get("endTime"),(String)consultOrder.get("phone"),(String)consultOrder.get("orderNo"),openId,token,"url");
        } catch (CreateOrderException e) {
            e.printStackTrace();
            resultMap.put("state","false");
        }
        resultMap.put("reultState",reultState);
        return  resultMap;
    }

    /**
     * 评价订单
     * evaluate
     * */
    @RequestMapping(value="evaluateOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    int evaluateOrder(){
        return 0;
    }


    /**
     * 取消订单
     * @param params
     * @return map
     * */
    @RequestMapping(value = "cancelOrder",method = {RequestMethod.POST,RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String,Object> cancelOrder(@RequestBody Map<String, Object> params){
        Float resultState = 0f;
        Integer phoneConsultaServiceId = Integer.parseInt((String) params.get("phoneConsultaServiceId"));
        String cancelReason = (String)params.get("cancelReason");
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
        ConsultPhoneRegisterServiceVo vo = consultPhonePatientService.selectByPrimaryKey(phoneConsultaServiceId);
        if("1".equals(vo.getState())){
            try {
                 resultState = consultPhonePatientService.cancelOrder(phoneConsultaServiceId,cancelReason);
            }catch (Exception e){
                e.printStackTrace();
            }

            resultMap.put("reultState",resultState);
            //插入取消原因
            resultMap.put("reason", cancelReason);
            resultMap.put("praiseId", IdGen.uuid());
            resultMap.put("patientRegisterServiceId", phoneConsultaServiceId);
            resultMap.put("praise_date", new Date());
            if(resultState>0){
                patientRegisterPraiseService.insertCancelReason(resultMap);
            }
        }
        return  resultMap;
    }

}
