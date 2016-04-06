/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.interaction.service.ShareService;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "")
public class OrderUserController extends BaseController {

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    ConsultPhoneOrderService consultPhoneOrderService;

    //用户进行预约操作，并等待支付
    @RequestMapping(value = "/order/user/orderAppointOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderAppointOperation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
        return patientRegisterService.orderAppointOperation(params, session,request);
    }

    //用户进行预约支付操作
    @RequestMapping(value = "/order/user/orderPayOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderPayOperation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {

        //判断此订单是不是扫码预约用户（根据openid查marketer）
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        OrderPropertyVo orderPropertyVo = new OrderPropertyVo();
        sysWechatAppintInfoVo.setOpen_id(orderPropertyVo.getOpenid());
        sysWechatAppintInfoVo.setPatient_register_service_id(orderPropertyVo.getPatientRegisterServiceId());
        List<SysWechatAppintInfoVo> attentVos = wechatAttentionService.findAttentionInfoByOpenIdLists(sysWechatAppintInfoVo);
        if (attentVos.size() > 0) {
            orderPropertyVo.setScanCode("yes");//是扫码关注的
        } else {
            orderPropertyVo.setScanCode("no");
        }

        return patientRegisterService.orderPayOperation(params, orderPropertyVo, session, request);
    }

    //status:为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档,6表示已取消，显示不同状态的预订
    @RequestMapping(value = "/order/user/orderPraiseOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderPraiseOperation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
        return patientRegisterPraiseService.orderPraiseOperation(params, session, request);
    }

    //status:为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档,6表示已取消，显示不同状态的预订
    @RequestMapping(value = "/order/user/info/orderShareOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderShareOperation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
        return shareService.orderShareOperation(params, session, request);
    }

    //status:为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档,6表示已取消，显示不同状态的预订,用户取消操作
    @RequestMapping(value = "/order/user/info/orderCancelOperation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> orderCancelOperation(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
        HashMap<String, Object> excuteMap = new HashMap<String, Object>();
        Map<String, Object> result = patientRegisterService.orderCancelOperation(params, excuteMap,session);

        //handle cancel reason like this temporarily
        String flag = String.valueOf(result.get("status"));//(String) params.get("flag");
        if ("0".equals(flag)) {
            String reason = (String) params.get("reason");
            excuteMap.put("reason", reason);
            excuteMap.put("praiseId", IdGen.uuid());
            patientRegisterPraiseService.insertCancelReason(excuteMap);
        }

        return result;
    }


    @RequestMapping(value = "/order/user/msgAppointment", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> msgAppointment(@RequestBody Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int result = messageService.addMsgAppointment(params);
        resultMap.put("status",result);
        return resultMap;
    }

    @RequestMapping(value = "/order/user/msgAppointmentStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String msgAppointmentStatus(@RequestBody Map<String, Object> params) {
        return messageService.getMsgAppointmentStatus(params);
    }

    @RequestMapping(value = "/order/user/checkUserAppointment", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkUserAppointment(@RequestBody Map<String, Object> params,HttpSession session) {
        return patientRegisterService.checkUserAppointment(params, session);
    }

    @RequestMapping(value = "/order/user/insertUserAppointmentNum", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void insertUserAppointmentNum(@RequestBody Map<String, Object> params, HttpSession session) {
        patientRegisterService.insertUserAppointmentNum(params,session);
    }

    /**
     * 获取个人的预约信息详情，对预约的状态进行操作 @author 0_zdl
     * <p/>
     * params:{"patient_register_service_id":"fewi323odw"}
     * <p/>
     * response:
     * {
     * "doctorId":"4214cdsifn","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"position1":"主任医师",
     * "position2":"教授","departmentName":"儿内-重症医学科",
     * "time":"2015/05/18 周一","location":"北京市朝阳区首都儿科研究所 2楼北侧专家诊区11诊室",
     * "appointmentNo":"201505130001","status":"0","beginTime":"12:10","endTime":"12:20"
     * }
     * //status为0表示进行了预约，等待支付，1表示完成支付，等待就诊，2表示取消了预约，
     * 3表示完成了就诊，等待评价， 3表示完成了评价等待分享， 4表示完成了分享等待建档
     */
    @RequestMapping(value = "/order/user/orderDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> myselfInfoAppointmentDetail(@RequestBody Map<String, Object> params) {
        return patientRegisterService.getUserOrderDetail(params);
    }

    /**
     * 进入待评价，待支付，待分享列表信息  @author 03_zdl
     * <p/>
     * params:{"patientId":"few3fe3232","pageNo":"2","pageSize":"10","status":"0"}
     * //status为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档，6表示已经取消,显示不同状态的预订，
     * //所status为空，即""，表示显示所有状态的预订
     * <p/>
     * response:
     * {
     * "pageNo":"2","pageSize":"10","pageTotal":"20",
     * "appointmentData":[
     * {"patient_register_service_id":"fewi323odw","doctorName":"常丽","time":"05/13 15:00-15:15","status":"预约成功（待支付）"},
     * {"patient_register_service_id":"fewi323odw","doctorName":"常丽","time":"05/13 15:00-15:15","status":"预约成功（待就诊）"},
     * {"patient_register_service_id":"fewi323odw","doctorName":"常丽","time":"05/13 15:00-15:15","status":"预约成功（待评价）"},
     * ],
     * "failure":"0"
     * }
     */
    @RequestMapping(value = "/order/user/orderList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> myselfInfoAppointment(@RequestBody Map<String, Object> params) {
        return patientRegisterService.getUserOrderList(params);
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
        return consultPhoneOrderService.getOrderList(params);
    }

    /**
     *判断用户的订单信息
     */
    @RequestMapping(value = "/order/user/checkOrderInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkOrderInfo(@RequestBody Map<String, Object> params,HttpServletRequest request,
                                       HttpServletResponse httpResponse) {
        return patientRegisterService.checkOrderInfo(params, request, httpResponse);
    }

    /**
     *用户的账户信息
     */
    @RequestMapping(value = "/order/user/appointmentByPhone", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> appointmentByPhone(@RequestBody Map<String, Object> params,HttpServletRequest request,
                                           HttpServletResponse httpResponse) {
        return patientRegisterService.getOrderListByUserPhone(params, request, httpResponse);
    }

    @RequestMapping(value = "/order/user/getUnNormalCheckOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUnNormalCheckOrder(@RequestBody Map<String, Object> params) throws Exception {
        return patientRegisterService.getUnNormalCheckOrder(params);
    }

    @RequestMapping(value = "/order/user/checkIfAppointmentNeedPay", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000062")//扫码来预约
    Map<String, Object> checkIfAppointmentNeedPay(@RequestBody Map<String, Object> params, HttpSession session,
                                                  HttpServletRequest request) {
        String openId = WechatUtil.getOpenId(session, request);

        HashMap<String, Object> response = new HashMap<String, Object>();
        String register_service_id = (String) params.get("register_service_id");
        int chargePrice = patientRegisterService.getNeedPayStatusByRegisterService(register_service_id,openId);
        if(chargePrice==0)
        {
            response.put("needPay","false");
        }
        else
        {
            response.put("needPay","true");
            response.put("chargePrice",chargePrice);
        }
        return response;
    }


    @RequestMapping(value = "/order/getCheckOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCheckOrder(@RequestBody Map<String, Object> params,HttpSession session,
                                      HttpServletRequest request) throws Exception {
        String openId = WechatUtil.getOpenId(session,request);
        HashMap<String, Object> m = new HashMap<String, Object>();
        if(!StringUtils.isNotNull(openId)){
            openId = CookieUtils.getCookie(request,"openId");
            if(!StringUtils.isNotNull(openId)) {
                m.put("status", "5");
                return m;
            }
        }
        String phone=params.get("phone").toString();
        String babyName=params.get("babyName").toString();
        String reid=params.get("reid").toString();
        m = patientRegisterService.checkOrder(phone, babyName, reid);
        return m;
    }

    @RequestMapping(value = "/order/user/getOrderPayInfo", method = {RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> getOrderPayInfo(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request) {
        Map<String,Object> response = new HashMap<String, Object>();
        String patient_register_service_id  = (String)params.get("patient_register_service_id");

        PerAppDetInfoVo perVo = new PerAppDetInfoVo();
        perVo.setId(patient_register_service_id);
        Map<String, Object> responseMap = patientRegisterService.findPersonAppointDetailInfo(perVo);

        String userId = UserUtils.getUser().getId();
        Float accountFund = accountService.accountFund(userId);
        response.put("accountFund",accountFund);
        response.put("userBind",userId==null?false:true);
        Calendar rightNow = Calendar.getInstance();
        if(null != patient_register_service_id){
            Date createDate = (Date)responseMap.get("create_date");
            rightNow.setTime(createDate);
            rightNow.add(Calendar.MINUTE, +30);//日期减1年
            createDate = rightNow.getTime();
            response.put("createDate",createDate.getTime());
        }

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id", patient_register_service_id);
        String visitTimeStr = registerService.getVisitTimeById(map);
        Date visitTime = DateUtils.StrToDate(visitTimeStr, "datetime");
        rightNow = Calendar.getInstance();
        rightNow.setTime(visitTime);
        rightNow.add(Calendar.HOUR_OF_DAY, -5);//日期减1年
        visitTime = rightNow.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        response.put("endTime",format.format(visitTime));

        response.put("doctorName",responseMap.get("doctorName"));
        response.put("hospitalName", responseMap.get("hospitalName"));

        Date createTime = DateUtils.parseDate(responseMap.get("date"));
        String week = DateUtils.getWeekOfDate(createTime);
        String year = DateUtils.formatDateToStr(createTime, "yyyy/MM/dd");
        String hour = (String) responseMap.get("beginTime");//DateUtils.formatDateToStr(createTime,"HH:mm");
        response.put("date", year+" "+week+" "+hour);
        return response;
    }

    /**
     * 获取某个医生的某个号的详细信息
     * <p/>
     * params:{"doctorId":"324xdksg234","patient_register_id":"3dsf3xfe","date":"2015-06-08","begin_time":"8:00","end_time":"8:10"}
     * <p/>
     * response
     * {
     * "location":"北京市朝阳区首都医院2楼北侧专家诊所11诊室",
     * "price":"20",
     * "deposit":"50",
     * "service_type":"0"
     * "phone":"18266185538"
     * <p/>
     * }
     * //service_type的0表示公立特需，1表示私立单独问诊
     */
    @RequestMapping(value = "/order/user/doctor/time/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> DoctorAppointmentInfoDetail(@RequestBody Map<String, Object> params) {
        return registerService.doctorAppointmentInfoDetailOfDay(params);
    }
}
