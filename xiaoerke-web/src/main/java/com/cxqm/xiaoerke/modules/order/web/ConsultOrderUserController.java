package com.cxqm.xiaoerke.modules.order.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.sdk.CCPRestSDK;
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
import java.math.BigDecimal;
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
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
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
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
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
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
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
    Map<String,Object> getConsultPhonePageList(@RequestBody Map<String,Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        return consultPhoneOrderService.getUserOrderPhoneConsultList(params);
    }


    /**
     * 生成订单
     * */
    @RequestMapping(value = "consultOrder/createOrder",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> createOrder(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String babyId =(String)params.get("babyId");
        String  babyName =(String)params.get("babyName");
        String  phoneNum =(String)params.get("phoneNum");
        String  illnessDesc =(String)params.get("illnessDesc");
        Integer  sysConsultPhoneId =Integer.parseInt((String)params.get("sysConsultPhoneId"));
        Date  birthDay = DateUtils.StrToDate((String)params.get("birthDay"),"yyyy/MM/dd");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String openid = UserUtils.getUser().getOpenid();
        int resultState = 0;
        try {
            resultState = consultPhonePatientService.PatientRegister(openid, babyId, babyName, birthDay, phoneNum, illnessDesc, sysConsultPhoneId);

        } catch (CreateOrderException e) {
            e.printStackTrace();
            resultMap.put("state","false");
        }
        resultMap.put("resultState",resultState);
        return  resultMap;
    }

    /**
     * 生成订单
     * */
    @RequestMapping(value = "consultOrder/getConnect",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getConnect(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String  babyName =(String)params.get("babyName");
        String  phoneNum =(String)params.get("phoneNum");
        String  illnessDesc =(String)params.get("illnessDesc");
        String  doctorId = (String)params.get("doctorId");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String resultState ="";
        try {
            resultState = consultPhonePatientService.createConsultOrder(babyName,phoneNum, illnessDesc, doctorId);
//

        } catch (CreateOrderException e) {
            e.printStackTrace();
            resultMap.put("state","false");
        }
        resultMap.put("orderid",resultState);
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
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        Float resultState =0f;
        Integer phoneConsultaServiceId = Integer.parseInt((String) params.get("phoneConsultaServiceId"));
        String cancelReason = (String)params.get("cancelReason");
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> orderVo = consultPhonePatientService.getPatientRegisterInfo(phoneConsultaServiceId);
        Date orderDate = DateUtils.StrToDate(orderVo.get("date") + " " + orderVo.get("beginTime"), "yyyy/MM/dd HH:mm");

        if(orderDate.getTime()<new Date().getTime()+5*60*1000){
            resultMap.put("status","20");
            return resultMap;
        }
        if("待接听".equals(orderVo.get("state"))){
            try {
                 resultState = consultPhonePatientService.cancelOrder(phoneConsultaServiceId,cancelReason,"0");
            }catch (Exception e){
                e.printStackTrace();
            }

            resultMap.put("resultState",resultState);
            //插入取消原因
            resultMap.put("reason", cancelReason);
            resultMap.put("praiseId", IdGen.uuid());
            resultMap.put("patientRegisterServiceId", phoneConsultaServiceId);
            resultMap.put("praise_date", new Date());
            int i = resultState.compareTo(0f);
            if(i>0){
                patientRegisterPraiseService.insertCancelReason(resultMap);
            }
        }
        return  resultMap;
    }

}
