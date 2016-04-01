package com.cxqm.xiaoerke.modules.order.web;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/18.
 * 电话咨询订单
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}/consultPhone/")
public class ConsultOrderUserController {

    @Autowired
    private ConsultPhonePatientService consultPhonePatientService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;



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
    @RequestMapping(value = "getOrderList",method = {RequestMethod.GET,RequestMethod.POST})
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
     * 生成订单
     * */
    @RequestMapping(value = "createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> createOrder(@RequestBody Map<String, Object> params){
            //@RequestParam String babyId,@RequestParam String babyName,Date birthDay,String phoneNum,String illnessDesc,Integer sysConsultPhoneId

      String babyId =(String)params.get("babyId");
      String  babyName =(String)params.get("babyName");
      String  phoneNum =(String)params.get("phoneNum");
      String  illnessDesc =(String)params.get("illnessDesc");
      Integer  sysConsultPhoneId =(Integer)params.get("sysConsultPhoneId");
      Date  birthDay =(Date)params.get("birthDay");
      Map<String,Object> resultMap = new HashMap<String, Object>();
      String openid = UserUtils.getUser().getOpenid();
      int reultState = consultPhonePatientService.PatientRegister(openid, babyId, babyName, birthDay, phoneNum, illnessDesc, sysConsultPhoneId);
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
     * @param phoneConsultaServiceId
     * @return map
     * */
    @RequestMapping(value = "cancelOrder",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> cancelOrder(@RequestParam Integer phoneConsultaServiceId,@RequestParam String cancelReason){
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
      int resultState = consultPhonePatientService.cancelOrder(phoneConsultaServiceId,cancelReason);
      resultMap.put("reultState",resultState);
      //插入取消原因
      resultMap.put("reason", cancelReason);
      resultMap.put("praiseId", IdGen.uuid());
      resultMap.put("patientRegisterServiceId", phoneConsultaServiceId);
      resultMap.put("praise_date", new Date());
      if(resultState>0){
        patientRegisterPraiseService.insertCancelReason(resultMap);
      }
      return  resultMap;
    }

}
