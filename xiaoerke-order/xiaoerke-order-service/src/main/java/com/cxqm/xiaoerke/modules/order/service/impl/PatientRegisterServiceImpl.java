package com.cxqm.xiaoerke.modules.order.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.ServiceException;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.RegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.UserReturnVisitServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.UserReturnVisitVo;
import com.cxqm.xiaoerke.modules.order.service.OrderPropertyService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = false)
public class PatientRegisterServiceImpl implements PatientRegisterService {

    @Autowired
    private UtilService utilService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PatientRegisterServiceDao patientRegisterServiceDao;

    @Autowired
    private RegisterServiceDao registerServiceDao;

    @Autowired
    public UserDao userDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserReturnVisitServiceDao userReturnVisitServiceDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderPropertyService orderPropertyService;

    @Autowired
    private OrderMessageService orderMessageService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private PayRecordService payRecordService;

    //对预约的状态进行操作（预约确认） @author 14_zdl
    @Override
    public Map<String, Object> orderAppointOperation(Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        params.put("urlPath", "s68.baodf.com/titan");
        HashMap<String, Object> action = (HashMap<String, Object>) params.get("action");
        String openId = WechatUtil.getOpenId(session, request);
        params.put("openId", openId);
        String patientRegisterServiceId = (String) params.get("patient_register_service_id");
        if (patientRegisterServiceId == null || "".equals(patientRegisterServiceId)) {
            patientRegisterServiceId = IdGen.uuid();
        }
        HashMap<String, Object> response = new HashMap<String, Object>();
        HashMap<String, Object> excuteMap = new HashMap<String, Object>();

        //根据手机号查询或创建用户  有则查，没有则创建
        PatientVo PatientVo = utilService.CreateUser(String.valueOf(action.get("userPhone")), openId, "patient");
        excuteMap.put("openId", openId);
        //预约确认，往patient_register_service表中插入数据 ,并且更新sys_register_service表status和sys_patient_id字段action
        String status = (String) action.get("status");
        excuteMap.put("status", status);
        excuteMap.put("userId", PatientVo.getSysUserId());
        excuteMap.put("patient_register_service_id", patientRegisterServiceId);
        excuteMap.put("babyName", action.get("babyName"));
        excuteMap.put("userPhone", action.get("userPhone"));
        excuteMap.put("illnessDesc", action.get("illnessDesc"));
        excuteMap.put("create_date", new Date());
        excuteMap.put("babyinfo_id", action.get("babyId"));
        if (action.get("birthday") != null && !"".equals(action.get("birthday"))) {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = dateFormat1.parse((String) action.get("birthday"));
                String a1 = dateFormat1.format(d);
                excuteMap.put("birthday", a1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String register_no = ChangzhuoMessageUtil.createRandom(true, 10);
        excuteMap.put("register_no", register_no);//随机数
        excuteMap.put("sys_register_service_id", params.get("sys_register_service_id"));
        excuteMap.put("sys_patient_id", PatientVo.getId());

        // 记录日志
        LogUtils.saveLog(Servlets.getRequest(), "00000019" ,"订单表主键"+ patientRegisterServiceId);//用户预约医生:

        int flagConfirm = appointmentConfirm(excuteMap);
        if (flagConfirm > 0) {
            response.put("appointmentNo", register_no);
            //获取路线
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("patientRegisterServiceId", patientRegisterServiceId);
            HashMap<String, Object> resultMap = findAppointMessage(hashMap);
            response.put("begin_time", resultMap.get("begin_time"));
            response.put("patientRegisterServiceId", patientRegisterServiceId);
            response.put("end_time", resultMap.get("end_time"));
            response.put("root", resultMap.get("root"));
            response.put("location", resultMap.get("location"));
            insertMonitor(register_no, "0", "0");//插入监听器
            insertMonitor(register_no, "1", "7");//提示医生就诊相关内容
        } else {//当前号源已被预约
            response.put("status", '0');
            return response;
        }
        response.put("patient_register_service_id", patientRegisterServiceId);
        response.put("status", '1');
        return response;
    }

    @Override
    public void orderFreePayOperation(String openId, String patient_register_service_id) {
        HashMap<String, Object> executeMap = new HashMap<String, Object>();

        executeMap.put("patientRegisterServiceId", patient_register_service_id);
        patientRegisterServiceDao.UpdatePatientRegisterServiceIsPay(executeMap);


        //启动线程 保存订单属性
        OrderPropertyVo orderPropertyVo = new OrderPropertyVo();
        orderPropertyVo.setPatientRegisterServiceId(patient_register_service_id);
        orderPropertyVo.setOpenid(openId);
        orderPropertyVo.setOrderSource("weixin");
        orderPropertyService.saveOrderProperty(orderPropertyVo);

        //插入消息
        HashMap<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("patient_register_service_id", patient_register_service_id);
        messageMap.put("openId", openId);
        messageMap.put("urlPath", "s68.baodf.com/titan");
        orderMessageService.sendMessage(messageMap, true);
    }
    @SystemServiceLog(description = "判断用户与订单的关系")
    @Override
    public Boolean judgeUserOrderRealtion(HttpSession session, HttpServletRequest request) {

        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("phone", UserUtils.getUser().getLoginName());

        List<HashMap<Integer, Object>> orderList = patientRegisterServiceDao.getUnBindUserNum(searchMap);
        if (orderList != null && orderList.size() > 0)
            return true;

        String openId = WechatUtil.getOpenId(session, request);
        //判断黄牛
        Map map = new HashMap();
        map.put("openid", openId);
        map.put("firstOrder", "no");
        List<PatientRegisterServiceVo> yellowCattleVo = patientRegisterServiceDao.findPageRegisterServiceByPatient(map);
        if (yellowCattleVo != null && yellowCattleVo.size() > 3) {
            return true;
        }
        return false;
    }

    //获取一个医生某天的预约列表
    @Override
    public List<HashMap<String, Object>> findAllAppointmentInfoByDoctor(HashMap<String, Object> dataInfo) {

        return registerServiceDao.findAllAppointmentInfoByDoctor(dataInfo);
    }

    @Override
    public Page<HashMap<String, Object>> findPersonRegisterNoBypatientId(HashMap<String, Object> saveHash, Page<HashMap<String, Object>> page) {

        Page<HashMap<String, Object>> pageVo = patientRegisterServiceDao.findPersonRegisterNoByPatientIdExecute(saveHash, page);

        return pageVo;
    }

    @Override
    public Date getOrderCreateDate(String patientRegistId) {
        Map<String, Object> executeMap = new HashMap<String, Object>();
        executeMap.put("patientRegisterService", patientRegistId);
        List<PatientRegisterServiceVo> patientList = patientRegisterServiceDao.findPageRegisterServiceByPatient(executeMap);
        if (patientList.size() > 0) {
            Date orderCreateDate = patientList.get(0).getCreateDate();
            return orderCreateDate;
        }
        return null;
    }

    //根据手机号查询该用户的未支付订单
    @Override
    public Page<PatientRegisterServiceVo> appointmentByPhone(Page<PatientRegisterServiceVo> page, HashMap<String, Object> hashMap) {

        return patientRegisterServiceDao.findPersonAppointDetailInfoListByPhone(page, hashMap);

    }

    @Override
    public String getUserIdByPatientRegisterId(String patientRegistId) {
        return userInfoService.getUserIdByPatient(patientRegistId);
    }

    ;

    //获取个人的预约信息详情 @author 0_zdl
    @Override
    public Map fidPersonAppointDetailInfo(PerAppDetInfoVo PerAppDetInfoVo) {
        Map map = patientRegisterServiceDao.findPersonAppointDetailInfo(PerAppDetInfoVo);
        if (map != null && !map.isEmpty()) {
            return map;
        }
        return null;
    }

    //获取个人中的预约信息列表  @author 03_zdl
    @Override
    public Page<PatientRegisterServiceVo> findPersonAppointDetailInfoList(Page<PatientRegisterServiceVo> page, HashMap<String, Object> hashMap) {
        Page<PatientRegisterServiceVo> page1 = patientRegisterServiceDao.findPersonAppointDetailInfoList(page, hashMap);
        return page1;

    }

    //获取个人中心主页的信息 @ author 13_zdl
    @Override
    public HashMap getPerCenterPageInfo(HashMap<String, Object> hashMap) {
        //status为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档,6表示已取消，显示不同状态的预订
        String count = "0";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        hashMap.put("status", "0");//待支付
        String waitPay = Integer.toString(patientRegisterServiceDao.getPerCenterPageInfo(hashMap));

        hashMap.put("status", "1");//待就诊
        String waitTreat = Integer.toString(patientRegisterServiceDao.getPerCenterPageInfo(hashMap));

        hashMap.put("status", "2");//待评价
        String waitAppraise = Integer.toString(patientRegisterServiceDao.getPerCenterPageInfo(hashMap));

        hashMap.put("status", "3");//待分享
        String waitShare = Integer.toString(patientRegisterServiceDao.getPerCenterPageInfo(hashMap));

        hashMap.put("status", "4");//待建档
        String waitEmr = Integer.toString(patientRegisterServiceDao.getPerCenterPageInfo(hashMap));

        //我的资料信息
        HashMap<String, Object> re = userInfoService.findPersonInfoExecute(hashMap);
        resultMap.put("userPhoneNum", re.get("login_name"));

        resultMap.put("waitPay", waitPay);
        resultMap.put("waitTreat", waitTreat);
        resultMap.put("waitAppraise", waitAppraise);
        resultMap.put("waitShare", waitShare);
        resultMap.put("waitEmr", waitEmr);
        resultMap.put("count", count);

        if (resultMap != null && !resultMap.isEmpty()) {
            return resultMap;
        }
        return null;
    }

    @Override
    public List<HashMap<Integer, Object>> getUnBindUserOrder(String unBindUserPhoneNum) {
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("unBindUserPhoneNum", unBindUserPhoneNum);
        return patientRegisterServiceDao.getUnBindUserNum(searchMap);
    }


    @Override
    public HashMap<String, Object> checkOrder(String phone, String babyName,
                                              String reid) throws ParseException {
        HashMap<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat stfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //每天同一个手机号不能超过2个号
        Integer dayPhoneNum = patientRegisterServiceDao.getOrderCheckEverydayForPhone(phone);
        if (dayPhoneNum >= 2) {
            result.put("status", "1");
            result.put("doctorname", "");
            return result;
        }

        //每月同一个手机号不能超过5个号
        Integer monthPhoneNum = patientRegisterServiceDao.getOrderCheckEverymonthForPhone(phone);
        if (monthPhoneNum >= 5) {
            result.put("status", "2");
            result.put("doctorname", "");
            return result;
        }
        //每天同一个手机号，同一个儿童不能预约同一医生的两个号
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("babyname", babyName);
        map.put("reid", reid);
        List<HashMap<String, Object>> list = patientRegisterServiceDao.getOrderCheckEverydayForDoctor(map);
        if (list.size() > 1) {
            if (list.get(0) != null || !list.get(0).get("sys_doctor_id").equals("")) {
                if (list.get(0).get("sys_doctor_id").equals(list.get(1).get("sys_doctor_id"))) {
                    result.put("status", "3");
                    result.put("doctorname", patientRegisterServiceDao.getDoctorName(list.get(1).get("sys_doctor_id").toString()));
                    return result;
                }
            }
        }
        //每天同一个手机号，同一个儿童不能预约同时间段的两个号。（就诊时间，间隔1小时）
        HashMap<String, String> ma = new HashMap<String, String>();
        ma.put("phone", phone);
        ma.put("babyname", babyName);
        List<HashMap<String, String>> lastreid = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterID(ma);
        if (lastreid == null) {
            result.put("status", "0");
            result.put("doctorname", "");
            return result;
        } else {
            for (HashMap<String, String> hashMap : lastreid) {
                String id = hashMap.get("sys_register_service_id").toString();
                HashMap<String, String> lastTime = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterTime(id);
                HashMap<String, String> nowTime = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterTime(reid);
                String lastDates = sdf.format(sdf.parse(lastTime.get("redate"))) + " " + stf.format(stf.parse(lastTime.get("retime")));
                Date lasttime = stfs.parse(lastDates);

                String nowDates = sdf.format(sdf.parse(nowTime.get("redate"))) + " " + stf.format(stf.parse(nowTime.get("retime")));
                Date nowtime = stfs.parse(nowDates);
                long diffTime = (nowtime.getTime() - lasttime.getTime());
                //1000*3600 一小时的毫秒数
                if (diffTime < 0 && diffTime > -(1000 * 3600)) {
                    result.put("status", "4");
                    result.put("doctorname", "");
                    return result;
                }
                if (diffTime > 0 && diffTime < (1000 * 3600)) {
                    result.put("status", "4");
                    result.put("doctorname", "");
                    return result;
                }
                if (diffTime == 0) {
                    result.put("status", "4");
                    result.put("doctorname", "");
                    return result;
                }
            }
        }
        result.put("status", "0");
        result.put("doctorname", "");
        return result;
    }

    //获取医生某个出诊日期的预约信息
    @Override
    public void findDoctorAppointmentInfoByDate(String doctorId, String date, HashMap<String, Object> response) {
        List<HashMap<String, Object>> valueData = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> doctorHospitalInfo = doctorInfoService.getDoctorHospitalInfo(doctorId);

        List<HashMap<String, Object>> valuList = new ArrayList<HashMap<String, Object>>();
        for (HashMap<String, Object> doctorHospital : doctorHospitalInfo) {
            HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
            hospitalInfo.put("hospitalName", doctorHospital.get("hospitalName"));
            hospitalInfo.put("hospitalId", doctorHospital.get("hospitalId"));
            hospitalInfo.put("relationType", doctorHospital.get("relationType"));
            hospitalInfo.put("price", doctorHospital.get("price"));
            valuList.add(hospitalInfo);
        }

        removeDuplicateWithOrder(valuList);

        for (HashMap<String, Object> hospitalInfo : valuList) {
            HashMap<String, Object> valueInfo = new HashMap<String, Object>();
            valueInfo.put("hospitalName", hospitalInfo.get("hospitalName"));
            valueInfo.put("hospitalId", hospitalInfo.get("hospitalId"));
            valueInfo.put("relationType", hospitalInfo.get("relationType"));
            valueInfo.put("price", hospitalInfo.get("price"));
            List<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
            for (HashMap<String, Object> doctorHospital : doctorHospitalInfo) {
                if (doctorHospital.get("hospitalId").equals(hospitalInfo.get("hospitalId"))) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("location", doctorHospital.get("location"));
                    map.put("locationId", doctorHospital.get("locationId"));
                    map.put("price", doctorHospital.get("price"));
                    map.put("serviceType", doctorHospital.get("serviceType"));
                    map.put("date", date);
                    List<String> statusList = new ArrayList<String>();
                    statusList.add("0");
                    statusList.add("1");
                    map.put("statusList", statusList);
                    List<HashMap<String, Object>> doctorAppointmentTimes = doctorInfoService.getDoctorAppointmentTime(map);
                    List<HashMap<String, Object>> times = new ArrayList<HashMap<String, Object>>();
                    for (HashMap<String, Object> doctorAppointmentTime : doctorAppointmentTimes) {
                        HashMap<String, Object> timeMap = new HashMap<String, Object>();
                        timeMap.put("begin_time", doctorAppointmentTime.get("begin_time"));
                        timeMap.put("end_time", doctorAppointmentTime.get("end_time"));
                        times.add(timeMap);
                    }
                    map.put("times", times);
                    listData.add(map);
                }
            }
            valueInfo.put("availableInfo", listData);
            valueData.add(valueInfo);
        }
        response.put("dataValue", valueData);
    }


    @Override
    @SystemServiceLog(description = "00000023")//用户预约支付
    public Map<String, Object> orderPayOperation(Map<String, Object> params, OrderPropertyVo orderPropertyVo, HttpSession session, HttpServletRequest request) {
        String path = request.getLocalAddr() + request.getContextPath();
        params.put("urlPath", "s68.baodf.com/titan");
        String openId = WechatUtil.getOpenId(session, request);
        params.put("openId", openId);
        String patientRegisterServiceId = (String) params.get("patient_register_service_id");
        if (patientRegisterServiceId == null || "".equals(patientRegisterServiceId)) {
            patientRegisterServiceId = IdGen.uuid();
        }
        HashMap<String, Object> response = new HashMap<String, Object>();
        HashMap<String, Object> excuteMap = new HashMap<String, Object>();

        if (!StringUtils.isNotNull(openId)) {
            openId = CookieUtils.getCookie(request, "openId");
        }
        excuteMap.put("openId", openId);

        //----------------------支付处理-----------------------------------
        excuteMap.put("patientRegisterServiceId", patientRegisterServiceId);
        excuteMap.put("payWay", params.get("payWay"));
        patientRegisterServiceDao.UpdatePatientRegisterService(excuteMap);
        //如果用本地账户资金支付则扣除相应资金
        if (null != params.get("accountPay") && 0 != Integer.parseInt((String) params.get("accountPay"))) {
            LogUtils.saveLog(Servlets.getRequest(), "00000024" ,"金额"+ Integer.parseInt((String) params.get("accountPay")) * 100);

            PatientRegisterServiceVo patientRegisterServiceVo = patientRegisterServiceDao.selectByPrimaryKey(patientRegisterServiceId);
            Map param = new HashMap();
            param.put("id", patientRegisterServiceVo.getSysRegisterServiceId());
            RegisterServiceVo registerServiceVo = registerServiceDao.getRegisterById(param);

            if (registerServiceVo == null)
                throw new RuntimeException("There is no associated register with this order, order id: " + patientRegisterServiceId);

            accountService.payByRemainder(Float.valueOf(Integer.parseInt((String) params.get("accountPay")) * 100),
                    (String) params.get("openId"), patientRegisterServiceId, registerServiceVo.getSysDoctorId());

//            PerAppDetInfoVo Info = new PerAppDetInfoVo();
//            Info.setId(patientRegisterServiceId);
//            Map map = messageService.fidPersonAppointDetailInfoExcut(Info);
//            String content = map.get("babyName") + "小朋友家长，已付费预约" + map.get("doctorName") + "医生，就诊时间为" + map.get("date") + " " + "订单号：" + map.get("appointmentNo") + "预约手机号为：" + map.get("phone") + " ";
//            Map<String, Object> parameter = systemService.getWechatParameter();
//            String token = (String) parameter.get("token");
//            ChangzhuoMessageUtil.sendMsgToManagerForAccount(content, token);

        }

        //启动线程 保存订单属性
        orderPropertyVo.setPatientRegisterServiceId(patientRegisterServiceId);
        orderPropertyVo.setOpenid((String) excuteMap.get("openId"));
        orderPropertyVo.setOrderSource("weixin");
        orderPropertyService.saveOrderProperty(orderPropertyVo);
        //插入消息
        orderMessageService.sendMessage(params, true);
        //----------------------支付处理结束-----------------------------------
        response.put("patient_register_service_id", patientRegisterServiceId);
        response.put("status", '1');
        return response;
    }

    @Override
    public Map<String, Object> orderCancelOperation(Map<String, Object> params, HashMap<String, Object> excuteMap, HttpSession session) {
        params.put("urlPath", "s68.baodf.com/titan");
        String openId = (String) session.getAttribute("openId");
        params.put("openId", openId);
        String patientRegisterServiceId = (String) params.get("patient_register_service_id");
        if (patientRegisterServiceId == null || "".equals(patientRegisterServiceId)) {
            patientRegisterServiceId = IdGen.uuid();
        }
        HashMap<String, Object> response = new HashMap<String, Object>();

        //根据patientRegisterServiceId查询此订单是会员 ，还是老用户
        PatientRegisterServiceVo pvo = patientRegisterServiceDao.selectByPrimaryKey(patientRegisterServiceId);
        String memberServiceId = "";
        if (pvo != null) {
            if (pvo.getMemberServiceId() != null) {
                memberServiceId = pvo.getMemberServiceId().toString();
            }
            if (StringUtils.isNotNull(memberServiceId)) { //是会员服务订单
                // 记录日志
                LogUtils.saveLog(Servlets.getRequest(), "00000020" ,"订单表主键"+ patientRegisterServiceId);//会员服务订单用户取消预约

                //支付订单的用户，就诊前两小时不允许退款
                if (twoHourRefund(patientRegisterServiceId, response)) return response;

                //取消预约
                HashMap<String, Object> action = (HashMap<String, Object>) params.get("action");
                excuteMap.put("patientRegisterServiceId", patientRegisterServiceId);
                if (action != null) {
                    excuteMap.put("register_no", action.get("appointmentNo"));
                } else {
                    excuteMap.put("register_no", params.get("appointmentNo"));
                }

                excuteMap.put("", "");

                String flag = (String) params.get("flag");
                registerServiceDao.CancelSysRegisterService(excuteMap);//更新sys_register_service表中的status为0
                patientRegisterServiceDao.CancelPatientRegisterService(excuteMap);
                //根据订单表主键查询预约服务表id
                PatientRegisterServiceVo patientRegisterServiceVo = patientRegisterServiceDao.selectByPrimaryKey(patientRegisterServiceId);
                String relationType = this.getRegisterAttributeOfHospital(patientRegisterServiceId);
                if (!relationType.equals("2")) {
                    memberService.updateMemberLeftTimes(patientRegisterServiceVo.getMemberServiceId().toString(), "1");
                }
                excuteMap.put("sys_user_id", UserUtils.getUser().getId());
                //flag为0代表取消，为1代表删除支付，如果为取消则需插入消息
                if ("0".equals(flag)) {
                    orderMessageService.sendMessage(params, false);
                }
                response.put("relationType", relationType);
                response.put("patient_register_service_id", patientRegisterServiceId);
                response.put("status", '1');
                response.put("memberServiceId", patientRegisterServiceVo.getMemberServiceId());
                return response;
            } else {
                //不是会员服务订单
                // 记录日志
                LogUtils.saveLog(Servlets.getRequest(), "00000021" ,"订单表主键"+ patientRegisterServiceId);//不是会员服务订单用户取消预约:

                //支付订单的用户，就诊前两小时不允许退款
                if (twoHourRefund(patientRegisterServiceId, response)) return response;

                Map<String, Object> executeMap = new HashMap<String, Object>();
                executeMap.put("patientRegisterService", patientRegisterServiceId);
                List<PatientRegisterServiceVo> patientVo = patientRegisterServiceDao.findPageRegisterServiceByPatient(executeMap);
                HashMap<String, Object> patientmap = new HashMap<String, Object>();
                patientmap.put("patientId", patientVo.get(0).getSysPatientId());
                patientmap = userInfoService.findPersonInfoExecute(patientmap);
                if (patientVo.size() > 0) {
                    if (!"6".equals(patientVo.get(0).getStatus())) {
                        //将保证金退给用户  先判断用户是否交保障金  如果交了则再账户表增加金额  需判断时间是否超出规定
                        LogUtils.saveLog(Servlets.getRequest(), "00000022" ,"订单表主键"+patientRegisterServiceId);//用户取消退回保证金-订单号:
                        accountService.updateAccount(0f, patientRegisterServiceId, response, false, (String) patientmap.get("id"),"");
                    }
                }

                //取消预约
                HashMap<String, Object> action = (HashMap<String, Object>) params.get("action");
                excuteMap.put("patientRegisterServiceId", patientRegisterServiceId);
                if (action != null) {
                    excuteMap.put("register_no", action.get("appointmentNo"));
                } else {
                    excuteMap.put("register_no", params.get("appointmentNo"));
                }

                excuteMap.put("", "");

                String flag = (String) params.get("flag");
                registerServiceDao.CancelSysRegisterService(excuteMap);//更新sys_register_service表中的status为0
                patientRegisterServiceDao.CancelPatientRegisterService(excuteMap);
                //根据订单表主键查询预约服务表id
                excuteMap.put("sys_user_id", UserUtils.getUser().getId());
                //flag为0代表取消，为1代表删除支付，如果为取消则需插入消息
                if ("0".equals(flag)) {
                    orderMessageService.sendMessage(params, false);
                }
                response.put("patient_register_service_id", patientRegisterServiceId);
                response.put("status", '1');
                return response;
            }
        }
        return null;
    }

    private boolean twoHourRefund(String patientRegisterServiceId, HashMap<String, Object> response) {
        PerAppDetInfoVo Info = new PerAppDetInfoVo();
        Info.setId(patientRegisterServiceId);
        Map map = patientRegisterServiceDao.findPersonAppointDetailInfo(Info);
        if (map != null) {
            if((String.valueOf(map.get("hospitalType"))).equals("2")){
                response.put("relationType", String.valueOf(map.get("hospitalType")));
                return false;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowDate = df.format(new Date());
            String appDate = map.get("date")+" "+map.get("beginTime");
            long diff = 0;
            try {
                diff = df.parse(appDate).getTime() - df.parse(nowDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double hours = (Math.round(diff * 100 / (1000 * 60 * 60)) / 100.0);
            if (hours < 3) {
                response.put("status", "20");
                return true;
            }

        }
        return false;
    }

    //一鍵預約
    @Override
    public Map<String, Object> checkUserAppointment(Map<String, Object> params, HttpSession session) {
        Map<String, Object> respose = new HashMap<String, Object>();
        String openId = session.getAttribute("openId") == null ? "" : (String) session.getAttribute("openId");
        int appointmentNum = userDao.checkUserAppointment(openId);
        if (appointmentNum > 0) {
            respose.put("flag", false);
        } else {
            respose.put("flag", true);
        }
        return respose;
    }

    @Override
    public void insertUserAppointmentNum(Map<String, Object> params, HttpSession session) {
        HashMap<String, String> parameter = new HashMap<String, String>();
        String openId = session.getAttribute("openId") == null ? "" : (String) session.getAttribute("openId");
        parameter.put("id", IdGen.uuid());
        parameter.put("openid", openId);
        userDao.insertUserAppointmentNum(parameter);
    }

    @Override
    public Map<String, Object> getUserOrderDetail(Map<String, Object> params) {
        //判断用户是否绑定了手机号，如果没有绑定，将让用户跳转到手机号的绑定页面
        PerAppDetInfoVo Info = new PerAppDetInfoVo();
        String prsi = (String) params.get("patient_register_service_id");//用户进行加号的业务表 主键
        Info.setId(prsi);
        Map<String, Object> responseMap = patientRegisterServiceDao.findPersonAppointDetailInfo(Info);

        // 记录日志
        LogUtils.saveLog(Servlets.getRequest(), "00000017","patient_register_service_id:"+prsi);//获取个人的预约信息详情:

        if (responseMap != null && !responseMap.isEmpty()) {
            responseMap.put("patient_register_service_id", prsi == null ? "" : prsi);
            if (((String) responseMap.get("department_level2")).equals("")) {
                responseMap.put("departmentName", (responseMap.get("department_level1")));
            } else {
                responseMap.put("departmentName", (responseMap.get("department_level1") + "-" + responseMap.get("department_level2")));
            }

            Date createTime = (Date) responseMap.get("date");
            String week = DateUtils.getWeekOfDate(createTime);
            String year = DateUtils.formatDateToStr(createTime, "yyyy/MM/dd");
            String hour = (String) responseMap.get("beginTime");
            responseMap.put("date", year + " " + week + " " + hour);
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> getUserOrderList(Map<String, Object> params) {
        //声明集合变量，并取值
        HashMap<String, Object> response = new HashMap<String, Object>();
        User user = UserUtils.getUser();
        String userId = user.getId();

        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String status = (String) params.get("status");//
        String patientId = userInfoService.getPatientIdByUserId(userId);
        //设值
        Page<PatientRegisterServiceVo> page = FrontUtils.generatorPage(pageNo, pageSize);//暂设设固定值
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        if (status == null) {
            searchMap.put("status", null);
        } else {
            searchMap.put("status", status);
        }
        searchMap.put("patientId", patientId);
        //取数据
        Page<PatientRegisterServiceVo> resultPage = this.findPersonAppointDetailInfoList(page, searchMap);

        // 记录日志
        String statusFlag = "";
//		s为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档，6表示已经取消,显示不同状态的预订，
        if ("0".equals(statusFlag)) {
            statusFlag = "待支付";
        } else if ("1".equals(statusFlag)) {
            statusFlag = "待就诊";
        } else if ("2".equals(statusFlag)) {
            statusFlag = "待评价";
        } else if ("3".equals(statusFlag)) {
            statusFlag = "待分享";
        } else if ("4".equals(statusFlag)) {
            statusFlag = "待建档";
        }
        LogUtils.saveLog(Servlets.getRequest(), "00000018",statusFlag);//进入我的版块

        //组装json数据
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<PatientRegisterServiceVo> list = resultPage.getList();
        List<HashMap<String, Object>> patientRegisterServiceVoList = new ArrayList<HashMap<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (PatientRegisterServiceVo patientRegisterServiceVo : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("patient_register_service_id", patientRegisterServiceVo.getId());
                dataMap.put("doctorName", doctorInfoService.getDoctorNameByDoctorId(patientRegisterServiceVo.getDoctorId()));
                dataMap.put("doctorId", patientRegisterServiceVo.getDoctorId());
                dataMap.put("position", patientRegisterServiceVo.getPosition());

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newDate = dateformat.format(patientRegisterServiceVo.getDate());
                String beginTime = dateformat.format(patientRegisterServiceVo.getBeginTime());

                dataMap.put("startTime", beginTime.substring(11, 16));
                dataMap.put("register_service_id", patientRegisterServiceVo.getSysRegisterServiceId());
                dataMap.put("appointmentNo", patientRegisterServiceVo.getRegisterNo());
                dataMap.put("hospitalName", patientRegisterServiceVo.getHospitalName());
                dataMap.put("date", newDate.substring(5, 7) + "/" + newDate.substring(8, 10));
                dataMap.put("hospitalName", patientRegisterServiceVo.getHospitalName());
                if (patientRegisterServiceVo.getEndTime() != null) {
                    String endTime = dateformat.format(patientRegisterServiceVo.getEndTime());
                    dataMap.put("endTime", endTime.substring(11, 16));
                    dataMap.put("time", beginTime.substring(11, 16) + "-" + endTime.substring(11, 16));
                } else {
                    dataMap.put("time", beginTime.substring(11, 16));
                }

                if ("0".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "预约成功（待支付）");
                } else if ("1".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待就诊");
                } else if ("2".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待评价");
                } else if ("3".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待分享");
                } else if ("4".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "已完成");
                } else if ("6".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "已取消");
                }
                patientRegisterServiceVoList.add(dataMap);
            }
        }


        response.put("appointmentData", patientRegisterServiceVoList);

        //返回json数据
        return response;
    }

    @Override
    public Map<String, Object> getOrderListByUserPhone(Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        HashMap<String, Object> response = new HashMap<String, Object>();

        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        //设值
        Page<PatientRegisterServiceVo> page = FrontUtils.generatorPage(pageNo, pageSize);//暂设设固定值

        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        String unBindUserPhoneNum = (String) params.get("unBindUserPhoneNum");
        if (unBindUserPhoneNum != "") {
            CookieUtils.setCookie(httpResponse, "unBindUserPhoneNum", unBindUserPhoneNum);
        } else {
            unBindUserPhoneNum = CookieUtils.getCookie(request, "unBindUserPhoneNum");
        }

        hashMap.put("phone", unBindUserPhoneNum);


        //取数据
        Page<PatientRegisterServiceVo> resultPage = this.appointmentByPhone(page, hashMap);

        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<PatientRegisterServiceVo> list = resultPage.getList();
        List<HashMap<String, Object>> patientRegisterServiceVoList = new ArrayList<HashMap<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (PatientRegisterServiceVo patientRegisterServiceVo : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("patient_register_service_id", patientRegisterServiceVo.getId());
                dataMap.put("doctorName", doctorInfoService.getDoctorNameByDoctorId(patientRegisterServiceVo.getDoctorId()));
                dataMap.put("doctorId", patientRegisterServiceVo.getDoctorId());
                dataMap.put("position", patientRegisterServiceVo.getPosition());

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newDate = dateformat.format(patientRegisterServiceVo.getDate());
                String beginTime = dateformat.format(patientRegisterServiceVo.getBeginTime());

                dataMap.put("startTime", beginTime.substring(11, 16));
                dataMap.put("register_service_id", patientRegisterServiceVo.getSysRegisterServiceId());
                dataMap.put("appointmentNo", patientRegisterServiceVo.getRegisterNo());
                dataMap.put("hospitalName", patientRegisterServiceVo.getHospitalName());
                dataMap.put("date", newDate.substring(5, 7) + "/" + newDate.substring(8, 10));
                dataMap.put("hospitalName", patientRegisterServiceVo.getHospitalName());
                if (patientRegisterServiceVo.getEndTime() != null) {
                    String endTime = dateformat.format(patientRegisterServiceVo.getEndTime());
                    dataMap.put("endTime", endTime.substring(11, 16));
                    dataMap.put("time", beginTime.substring(11, 16) + "-" + endTime.substring(11, 16));
                } else {
                    dataMap.put("time", beginTime.substring(11, 16));
                }

                if ("0".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "预约成功（待支付）");
                } else if ("1".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待就诊");
                } else if ("2".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待评价");
                } else if ("3".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "待分享");
                } else if ("4".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "已完成");
                } else if ("6".equals(patientRegisterServiceVo.getStatus())) {
                    dataMap.put("status", "已取消");
                }
                patientRegisterServiceVoList.add(dataMap);
            }
        }
        response.put("appointmentData", patientRegisterServiceVoList);
        //返回json数据
        return response;
    }

    @Override
    public Map<String, Object> checkOrderInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("haveOrder", false);
        String pageNo = "1";
        String pageSize = "10";
        //设值
        Page<PatientRegisterServiceVo> page = FrontUtils.generatorPage(pageNo, pageSize);//暂设设固定值

        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        String loginName = UserUtils.getUser().getLoginName();
        hashMap.put("phone", loginName);
        if (null == loginName) {
          String  unBindUserPhoneNum = CookieUtils.getCookie(request, "unBindUserPhoneNum");
          if (null == unBindUserPhoneNum) {
            return response;
          }
          hashMap.put("phone", unBindUserPhoneNum);
        }else{
            CookieUtils.setCookie(httpResponse, "unBindUserPhoneNum", loginName);
        }
        //取数据
        Page<PatientRegisterServiceVo> resultPage = patientRegisterServiceDao.getUnBindUserPhoneOrder(page, hashMap);
        if (resultPage.getList().size() > 0) {
            response.put("haveOrder", true);
        }
        ;
        return response;
    }

    @Override
    public Map<String, Object> getReminderOrder(Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>(4);

        PatientRegisterServiceVo patientRegisterServiceVo = new PatientRegisterServiceVo();
        patientRegisterServiceVo.setDoctorId((String) params.get("doctorId"));
        patientRegisterServiceVo.setStatus((String) params.get("status"));

        Date theDate = DateUtils.StrToDate((String) params.get("date"), "date");

        patientRegisterServiceVo.setDate(theDate);
        patientRegisterServiceVo.setLocationId((String) params.get("locationId"));
        patientRegisterServiceVo.setHospitalId((String) params.get("hospitalId"));

        List<PatientRegisterServiceVo> orders = patientRegisterServiceDao.findOrders(patientRegisterServiceVo);
        HashMap<String, Object> ordersMap = new HashMap<String, Object>(4);
        for (PatientRegisterServiceVo order : orders) {
            Object theMapObj = ordersMap.get(order.getLocationId());
            List<PatientRegisterServiceVo> ordersList = null;
            if (theMapObj == null) {
                HashMap<String, Object> theMap = new HashMap<String, Object>(8);
                ordersList = new ArrayList<PatientRegisterServiceVo>(8);
                theMap.put("orders", ordersList);
                theMap.put("hospitalName", order.getHospitalName());
                theMap.put("location", order.getLocation());
                theMap.put("locationId", order.getLocationId());
                ordersMap.put(order.getLocationId(), theMap);
            } else {
                HashMap<String, Object> theMap = (HashMap<String, Object>) theMapObj;
                ordersList = (List<PatientRegisterServiceVo>) theMap.get("orders");
            }

            order.setHospitalName(null);
            order.setLocation(null);
            order.setLocationId(null);
            order.setHospitalId(null);
            ordersList.add(order);
        }

        response.put("orders", ordersMap.values());
        return response;
    }

    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList<HashMap<String, Object>>();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    //获取医生某个出诊日期的结算信息
    @Override
    public void findDoctorSettlementAppointmentInfoByDate(String doctorId, String userId, String date, HashMap<String, Object> response) {
        HashMap<String, Object> paramData = new HashMap<String, Object>();
        paramData.put("date", date);
        paramData.put("doctorId", doctorId);
        List<HashMap<String, Object>> doctorSettlementAppointment = patientRegisterServiceDao.findDoctorSettlementAppointmentInfoByDate(paramData);
        List<HashMap<String, Object>> doctorSettlementList = new ArrayList();
        int totalAppPrice = 0;
        for (HashMap<String, Object> doctorSettlement : doctorSettlementAppointment) {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("beginTime", doctorSettlement.get("begin_time"));
            data.put("endTime", doctorSettlement.get("end_time"));
            String position = (String) doctorSettlement.get("position");
            //Object subsidyObj = doctorSettlement.get("subsidy");
            Object allowanceObj = doctorSettlement.get("allowance");
            //int subsidy = subsidyObj == null ? 0 : (Integer) subsidyObj;
            int allowance = allowanceObj == null ? 0 : (Integer) allowanceObj;

            data.put("price", allowance);
            totalAppPrice = totalAppPrice + allowance;

            if (position.indexOf("副主任") == 1) {
                position = "2";
                data.put("position", position);
            } else if (position.indexOf("主治") == 1) {
                position = "3";
                data.put("position", position);
            } else {
                position = "1";
                data.put("position", position);
            }
            data.put("illness", doctorSettlement.get("illness"));
            data.put("babyName", doctorSettlement.get("babyName"));
            doctorSettlementList.add(data);
        }
        response.put("appointment", doctorSettlementList);
        response.put("totalAppPrice", totalAppPrice);
        response.put("appointmentTotal", doctorSettlementList.size());

        AccountInfo accountInfo = accountService.findAccountInfoByUserId(userId);
        if (accountInfo == null) {
            accountInfo = new AccountInfo();
            accountInfo.setId(IdGen.uuid());
            accountInfo.setUserId(userId);
            accountInfo.setOpenId("");
            accountInfo.setBalance(Float.parseFloat("0"));
            accountInfo.setCreatedBy(userId);
            accountInfo.setCreateTime(new Date());
            accountInfo.setStatus("normal");
            accountInfo.setType("1");
            accountInfo.setUpdatedTime(new Date());
            accountService.saveOrUpdateAccountInfo(accountInfo);
            response.put("balance", Float.parseFloat("0"));
        } else {
            response.put("balance", accountInfo.getBalance() / 100);
        }
    }

    @Override
    public int saveDoctorAppointmentInfo(Map<String, Object> map) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("id", IdGen.uuid());
        param.put("doctorId", map.get("doctorId"));
        param.put("sys_hospital_id", map.get("hospitalId"));
        param.put("date", map.get("date"));
        param.put("price", map.get("price"));
        param.put("begin_time", map.get("begin_time"));
        param.put("end_time", map.get("end_time"));
        param.put("location", map.get("location"));
        param.put("desc", "");
        param.put("status", "0");
        param.put("deposit", 50.00);
        param.put("service_type", map.get("serviceType"));
        param.put("root", "");
        param.put("create_date", df.format(new Date()));
        param.put("locationId", map.get("locationId"));

        registerServiceDao.insertSysRegisterServiceExecute(param);
        return 1;
    }

    @Override
    public Map<String, Object> getUnNormalCheckOrder(Map<String, Object> params) {
        String phone = params.get("phone").toString();
        String babyName = params.get("babyName").toString();
        String reid = params.get("reid").toString();

        HashMap<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat stfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //每天同一个手机号不能超过2个号
        Integer dayPhoneNum = patientRegisterServiceDao.getOrderCheckEverydayForPhone(phone);
        if (dayPhoneNum >= 2) {
            result.put("status", "1");
            result.put("doctorname", "");
            return result;
        }
        //每月同一个手机号不能超过5个号
        Integer monthPhoneNum = patientRegisterServiceDao.getOrderCheckEverymonthForPhone(phone);
        if (monthPhoneNum >= 5) {
            result.put("status", "2");
            result.put("doctorname", "");
            return result;
        }
        //每天同一个手机号，同一个儿童不能预约同一医生的两个号
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("babyname", babyName);
        map.put("reid", reid);
        List<HashMap<String, Object>> list = patientRegisterServiceDao.getOrderCheckEverydayForDoctor(map);
        if (list.size() > 1) {
            if (list.get(0) != null || !list.get(0).get("sys_doctor_id").equals("")) {
                if (list.get(0).get("sys_doctor_id").equals(list.get(1).get("sys_doctor_id"))) {
                    result.put("status", "3");
                    result.put("doctorname", patientRegisterServiceDao.getDoctorName(list.get(1).get("sys_doctor_id").toString()));
                    return result;
                }
            }
        }
        //每天同一个手机号，同一个儿童不能预约同时间段的两个号。（就诊时间，间隔1小时）
        HashMap<String, String> ma = new HashMap<String, String>();
        ma.put("phone", phone);
        ma.put("babyname", babyName);
        List<HashMap<String, String>> lastreid = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterID(ma);
        if (lastreid == null) {
            result.put("status", "0");
            result.put("doctorname", "");
            return result;
        } else {
            for (HashMap<String, String> hashMap : lastreid) {
                try {
                    String id = hashMap.get("sys_register_service_id").toString();
                    HashMap<String, String> lastTime = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterTime(id);
                    HashMap<String, String> nowTime = patientRegisterServiceDao.getOrderCheckEverydaySpaceOneHoursOfRegisterTime(reid);
//				String lastDates=sdf.format(lastTime.get("redate"))+""+stf.format(lastTime.get("retime"));
                    String lastDates = sdf.format(sdf.parse(lastTime.get("redate"))) + " " + stf.format(stf.parse(lastTime.get("retime")));
                    Date lasttime = stfs.parse(lastDates);
//				String last=stfs.format(lastd);
//				String lastDates=stfs.format(last);
//				Date lasttime=stfs.parse(lastDates);

                    String nowDates = sdf.format(sdf.parse(nowTime.get("redate"))) + " " + stf.format(stf.parse(nowTime.get("retime")));
                    Date nowtime = stfs.parse(nowDates);
//				String now=stfs.format(nowd);
//				String nowDates=stfs.format(now);
//				Date nowtime=stfs.parse(nowDates);
                    long diffTime = (nowtime.getTime() - lasttime.getTime());
                    //1000*3600 一小时的毫秒数
                    if (diffTime < 0 && diffTime > -(1000 * 3600)) {
                        result.put("status", "4");
                        result.put("doctorname", "");
                        return result;
                    }
                    if (diffTime > 0 && diffTime < (1000 * 3600)) {
                        result.put("status", "4");
                        result.put("doctorname", "");
                        return result;
                    }
                    if (diffTime == 0) {
                        result.put("status", "4");
                        result.put("doctorname", "");
                        return result;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        result.put("status", "0");
        result.put("doctorname", "");
        return result;
    }

    //对预约的状态进行操作（预约确认） @author 14_zdl
    private int appointmentConfirm(HashMap<String, Object> excuteMap) {
        HashMap<String, Object> judgeMap = registerServiceDao.findSysRegisterServiceStatusExecute(excuteMap);
        String judgeStatus = (String) judgeMap.get("status");
        //如果updateRow为0 ，则表示该号刚被约走,处理并发（两个号同时被预约，放到同一个事物里）

        if ("1".equals(judgeStatus)) {
            return 0;
        }

        //by frank, 2015-12-29
        //added by frank on 2015-11-06
        Object registerId = excuteMap.get("sys_register_service_id");
        if(registerId != null) {
        	DoctorVo doctor = doctorInfoService.findDoctorByRegisterId((String)registerId);
        	excuteMap.put("allowance", doctor.getSubsidy());
        }
        //added by frank on 2015-11-06
        registerServiceDao.UpdateSysRegisterService(excuteMap);//更新sys_register_service数据
        patientRegisterServiceDao.InsertOrUpdatePatientRegisterService(excuteMap);//往patient_register_service表插入数据
        return 1;
    }

    private HashMap<String, Object> findAppointMessage(HashMap<String, Object> hashMap) {
        HashMap<String, Object> resultMap = messageService.findAppointMessageExecute(hashMap);
        return resultMap;
    }

    //插入监听器
    private void insertMonitor(String register_no, String type, String status) {
        HashMap<String, Object> monitorMap = new HashMap<String, Object>();
        monitorMap.put("id", IdGen.uuid());
        monitorMap.put("register_no", register_no);
        monitorMap.put("status", status);
        monitorMap.put("types", type);
        messageService.insertMonitorExecute(monitorMap);
    }


    @Override
    public Page<PatientRegisterServiceVo> findRegisterPatientList(
            Page<PatientRegisterServiceVo> page, PatientRegisterServiceVo pvo,
            String warn) {
        pvo.setPage(page);

        if ((pvo.getVisitDateFrom() != null && !"".equals(pvo
                .getVisitDateFrom()))
                || (pvo.getVisitDateTo() != null && !"".equals(pvo
                .getVisitDateTo()))) {
            pvo.setOrder("desc");
        } else {
            if ((pvo.getOrderCreateDateFrom() != null && !"".equals(pvo
                    .getOrderCreateDateTo()))
                    || (pvo.getOrderCreateDateTo() != null && !"".equals(pvo
                    .getOrderCreateDateTo()))) {
                pvo.setOrder("CreateDate");
            } else {
                pvo.setOrder("asc");
            }
        }

        Page<PatientRegisterServiceVo> pageList = patientRegisterServiceDao
                .findRegisterPatientList(page, pvo);
        List<PatientRegisterServiceVo> warnList = new ArrayList<PatientRegisterServiceVo>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("0", "待支付");
        map.put("1", "待就诊");
        map.put("2", "待评价");
        map.put("3", "待分享");
        map.put("4", "待建档");
        map.put("6", "已取消");
        Map<String, Object> reasonMap = new HashMap<String, Object>();
        reasonMap.put("1", "未接电话");
        reasonMap.put("2", "黄牛");
        reasonMap.put("3", "怀疑占号");
        for (PatientRegisterServiceVo vo : pageList.getList()) {
            String showTime = "";
            if (vo.getDate() != null && vo.getBeginTime() != null) {
                showTime = DateUtils.DateToStr(vo.getDate(), "date") + " "
                        + DateUtils.DateToStr(vo.getBeginTime(), "time")
                        + ":00";
                Date temp = DateUtils.StrToDate(showTime, "datetime");
                long time = temp.getTime() - 7200000;
                if (time < System.currentTimeMillis()
                        && temp.getTime() > System.currentTimeMillis()) {
                    vo.setWarnFlag("1");
                    if ("1".equals(warn)) {
                        warnList.add(vo);
                    }
                }
            }

            if ("0".equals(vo.getStatus())) {
                vo.setPayStatus("待支付");
            } else if ("6".equals(vo.getStatus())) {
                PayRecord payRecord = payRecordService.findPayRecordByOrder(vo.getId());
                if (payRecord == null) {
                    vo.setPayStatus("未支付");
                } else {
                    vo.setPayStatus("已退款");
                }
            } else {
                PayRecord payRecord = payRecordService.findPayRecordByOrder(vo.getId());
                if (payRecord == null) {
                    vo.setPayStatus("未支付");
                } else {
                    vo.setPayStatus("已支付");
                }
            }

            if ("0".equals(vo.getIsUser())) {
                vo.setFalseUserReason("真");
            } else if ("1".equals(vo.getIsUser())) {
                if (null == vo.getFalseUserReason()
                        || "".equals(vo.getFalseUserReason())) {
                    vo.setFalseUserReason("假");
                } else if ("4".equals(vo.getFalseUserReason())) {
                    vo.setFalseUserReason((vo.getFalseUserReasonRemarks() == null || ""
                            .equals(vo.getFalseUserReasonRemarks())) ? "假" : vo
                            .getFalseUserReasonRemarks());
                } else {
                    vo.setFalseUserReason((String) reasonMap.get(vo
                            .getFalseUserReason()));
                }
            }
            vo.setShowUpdateDate(vo.getUpdateDate() == null ? "" : DateUtils
                    .DateToStr(vo.getUpdateDate(), "datetime"));
            vo.setShowTime(showTime);
            vo.setShowCreateDate(DateUtils.DateToStr(vo.getCreateDate(),
                    "datetime"));
            vo.setStatus((String) map.get(vo.getStatus()));
            
            if("0".equals(vo.getRelationType())){
            	vo.setRelationType("公立医院");
			}else if("1".equals(vo.getRelationType())){
				vo.setRelationType("公立医院");
			}else if("2".equals(vo.getRelationType())){
				vo.setRelationType("合作机构");
			}
        }
        // 执行分页查询
        pageList.setList("1".equals(warn) ? warnList : pageList.getList());
        return pageList;
    }

    @Override
    public List<PatientRegisterServiceVo> getPatientRegisterList(
            PatientRegisterServiceVo vo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("0", "待支付");
        map.put("1", "待就诊");
        map.put("2", "待评价");
        map.put("3", "待分享");
        map.put("4", "待建档");
        map.put("6", "已取消");
        Map<String, Object> reasonMap = new HashMap<String, Object>();
        reasonMap.put("1", "未接电话");
        reasonMap.put("2", "黄牛");
        reasonMap.put("3", "怀疑占号");
        Map<String, Object> serviceMap = new HashMap<String, Object>();
        serviceMap.put("1", "专家门诊");
        serviceMap.put("2", "专家门诊需等待");
        serviceMap.put("3", "特需门诊");
        serviceMap.put("4", "特需门诊需等待");
        serviceMap.put("5", "私立医院");
        List<PatientRegisterServiceVo> list = patientRegisterServiceDao.getPatientRegisterList(vo);
        for (PatientRegisterServiceVo pvo : list) {
            String showTime = "";
            if (pvo.getDate() != null && pvo.getBeginTime() != null) {
                showTime = DateUtils.DateToStr(pvo.getDate(), "date") + " "
                        + DateUtils.DateToStr(pvo.getBeginTime(), "time")
                        + ":00";
            }
            if (null == pvo.getFalseUserReason()
                    || "".equals(pvo.getFalseUserReason())) {
                pvo.setFalseUserReason("");
            } else if ("4".equals(pvo.getFalseUserReason())) {
                pvo.setFalseUserReason(pvo.getFalseUserReasonRemarks());
            } else {
                pvo.setFalseUserReason((String) reasonMap.get(pvo
                        .getFalseUserReason()));
            }
            if (null == pvo.getOverallSatisfy()
                    || "".equals(pvo.getOverallSatisfy())) {
                pvo.setOverallSatisfy("");
            } else if ("1".equals(pvo.getOverallSatisfy())) {
                pvo.setOverallSatisfy("否");
            } else {
                pvo.setOverallSatisfy("是");
            }
            if ("0".equals(pvo.getUserFeedBack())) {
                pvo.setUserFeedBack(pvo.getUserFeedBackRemarks());
                pvo.setUserFeedBackRemarks("");
            } else {
                pvo.setUserFeedBack("");
            }

            if ("0".equals(pvo.getStatus())) {
                pvo.setPayStatus("待支付");
            } else if ("6".equals(pvo.getStatus())) {
                PayRecord payRecord = payRecordService.findPayRecordByOrder(pvo.getId());
                if (payRecord == null) {
                    pvo.setPayStatus("未支付");
                } else {
                    pvo.setPayStatus("已退款");
                }
            } else {
                PayRecord payRecord = payRecordService.findPayRecordByOrder(pvo.getId());
                if (payRecord == null) {
                    pvo.setPayStatus("未支付");
                } else {
                    pvo.setPayStatus("已支付");
                }
            }

            pvo.setServiceType((String) serviceMap.get(pvo.getServiceType()));
            pvo.setShowUpdateDate(pvo.getUpdateDate() == null ? "" : DateUtils
                    .DateToStr(pvo.getUpdateDate(), "date"));
            pvo.setShowTime(showTime);
            pvo.setShowCreateDate(DateUtils.DateToStr(pvo.getCreateDate(),
                    "datetime"));
            pvo.setStatus((String) map.get(pvo.getStatus()));
            
            if("0".equals(pvo.getRelationType())){
            	pvo.setRelationType("公立医院");
			}else if("1".equals(pvo.getRelationType())){
				pvo.setRelationType("公立医院");
			}else if("2".equals(pvo.getRelationType())){
				pvo.setRelationType("合作机构");
			}
        }
        return list;
    }

    private void insertNotification(String patientRegisterId) {
        Notification notification = new Notification();
        notification.setMessage("取消号源导致订单被取消");
        User user = UserUtils.getUser();
        notification.setCreatedBy(user == null ? null : user.getId());
        notification.setCreatedTime(new Date());
        notification.setRelatedId(patientRegisterId);
        notification.setStatus(Notification.STATUS_INITIAL);
        notification.setType(Notification.TYPE_ORDER_REMOVED);
        notification.setUpdatedTime(new Date());
        notificationService.saveNotification(notification);
    }

    @Override
    public void cancelAppointment(String registerId, String patientRegisterId, String memberServiceId, String keep, String reason, String deleteBy) {
        this.insertNotification(patientRegisterId);
        registerServiceDao.updateSysRegisterServiceStatusCancel(registerId);
        Map<String, Object> execute = new HashMap<String, Object>();
        execute.put("id", patientRegisterId);
        execute.put("keep", keep);
        execute.put("reason", reason);
        execute.put("deleteBy", deleteBy);
        patientRegisterServiceDao.updatePatientRegisterStatusCancelById(execute);

        if ("1".equals(keep)) {
            String relationType = this.getRegisterAttributeOfHospital(patientRegisterId);
            if (!relationType.equals("2")) {
                memberService.updateMemberLeftTimes(memberServiceId, "1");
            }
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("patient_register_service_id", patientRegisterId);
        LogUtils.saveLog(Servlets.getRequest(), "00000014" ,"patientRegisterId:"+ patientRegisterId);//用户取消预约
        try {
            orderMessageService.sendMessage(map, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param patientRegisterServiceVo
     */
    @Override
    public void savePatientRegisterService(
            PatientRegisterServiceVo patientRegisterServiceVo, OrderPropertyVo orderPropertyVo) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        String phone = patientRegisterServiceVo.getPhone();
        hashMap.put("phone", phone);
        hashMap = userDao.findPatientIdByPhoneExecute(hashMap);
        String spid = "";
        String uid = "";
        if (hashMap == null) {
            // 如果没有查询到患者信息，则新建一个用户
            uid = IdGen.uuid();
            User user = new User();
            user.setId(uid);
            user.setLoginName(patientRegisterServiceVo.getPhone());
            user.setPhone(phone);
            user.setCreateDate(new Date());
            userDao.insert(user);
            PatientVo patientVo = new PatientVo();
            spid = IdGen.uuid();
            patientVo.setId(spid);
            patientVo.setSysUserId(uid);
            patientVo.setStatus("2");
            patientVo.setAccountNumber(0);
            userInfoService.savePatient(patientVo);
        } else {
            spid = (String) hashMap.get("spid");
            uid = (String) hashMap.get("uid");
        }
        
        int memberServiceId = memberService.produceChargeMemberByMemberType("month",phone,uid,"bankend","后台预约赠送");
        
        HashMap<String, Object> excuteMap = new HashMap<String, Object>();
        String register_no = ChangzhuoMessageUtil.createRandom(true, 10);
        excuteMap.put("registerNo", register_no);
        excuteMap.put("id", IdGen.uuid());
        excuteMap.put("sysRegisterServiceId",
                patientRegisterServiceVo.getSysRegisterServiceId());
        excuteMap.put("babyName", patientRegisterServiceVo.getBabyName());
        excuteMap.put("birthday", patientRegisterServiceVo.getBirthday());
        excuteMap.put("phone", phone);
        excuteMap.put("illnessDesc", patientRegisterServiceVo.getIllness());
        excuteMap.put("createDate", new Date());
        excuteMap.put("status", "1");
        excuteMap.put("sysPatientId", spid);
        patientRegisterServiceDao.savePatientRegisterService(excuteMap);
        registerServiceDao
                .updateSysRegisterServiceStatusUsed(patientRegisterServiceVo
                        .getSysRegisterServiceId());
        insertMonitor(register_no, "0", "0");// 插入监听器
        insertMonitor(register_no, "1", "7");// 提示医生就诊相关内容
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("patient_register_service_id", excuteMap.get("id"));

        memberService.updateMemberLeftTimes(memberServiceId+"", "-1");

        //启动线程 保存订单属性
        orderPropertyVo.setPatientRegisterServiceId((String) excuteMap.get("id"));
        orderPropertyVo.setOrderSource("kefu");
        orderPropertyService.saveOrderProperty(orderPropertyVo);


        try {
            orderMessageService.sendMessage(map, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * // * @param getPatientRegisterInfo
     */
    @Override
    public HashMap<String, Object> getPatientRegisterInfo(
            HashMap<String, Object> paramMap) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = patientRegisterServiceDao.getPatientRegisterInfo(paramMap);
        return map;
    }

    @Override
    public HashMap<String, Object> getOrderInfoById(
            HashMap<String, Object> paramMap) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = patientRegisterServiceDao.getOrderInfoByPatientRegistId(paramMap);
        return map;
    }

    @Override
    public List<Map<String, Object>> getHealthRecordsAppointmentInfo(String userPhone, String babyId) {
      return patientRegisterServiceDao.getHealthRecordsAppointmentInfo(userPhone, babyId);
    }

    @Override
    public HashMap<String, Object> patientReturnVisitDetail(String patientId,
                                                            String sysRegisterId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", patientId);
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("patientMap", getPatientRegisterInfo(map));
        retMap.put("registerMap", registerService.getRegisterServiceInfo(sysRegisterId));
        HashMap<String, Object> excute = new HashMap<String, Object>();
        excute.put("patientRegisterId", patientId);
        UserReturnVisitVo vo = userReturnVisitServiceDao
                .getUserReturnVisitByInfo(excute);
        retMap.put("UserReturnVisitVo", vo == null ? new UserReturnVisitVo()
                : vo);
        return retMap;
    }

    @Override
    public void saveUserReturnVisit(UserReturnVisitVo userReturnVisitVo) {
        HashMap<String, Object> excute = new HashMap<String, Object>();
        excute.put("patientRegisterId", userReturnVisitVo.getPatientRegisterId());
        UserReturnVisitVo vo = userReturnVisitServiceDao.getUserReturnVisitByInfo(excute);
        if ((null == userReturnVisitVo.getId() || "".equals(userReturnVisitVo.getId())) && vo == null) {
            userReturnVisitVo.setCreateDate(new Date());
            userReturnVisitVo.setUpdateDate(new Date());
            userReturnVisitServiceDao.saveUserReturnVisitInfo(userReturnVisitVo);
        } else {
            userReturnVisitVo.setUpdateDate(new Date());
            userReturnVisitVo.setId(vo.getId());
            userReturnVisitServiceDao.updateUserReturnVisitInfo(userReturnVisitVo);
        }
    }


    @Override
    public int getNeedPayStatusByRegisterService(String register_service_id, String openid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("register_service_id", register_service_id);
        map.put("openid", openid);
        int chargePrice;
        System.out.println("=================进入收费判断======================register_service_id:" + register_service_id + "===========openid:" + openid);
        //医生给用户扫码，此用户一个小时内预约不收费 @add deliang
        int scanCodeNoChange = registerServiceDao.judgeUserScanCode(map);
        if (scanCodeNoChange > 0) {//不收费
            chargePrice = 0;
        } else {
            chargePrice = getChargePrice(register_service_id, openid, map);
        }

        return chargePrice;
    }


    private int getChargePrice(String register_service_id, String openid, HashMap<String, Object> map) {
        int chargePrice;//查询 是否需要收费 收费金额是多少
        HashMap<String, Object> chargeMap = patientRegisterServiceDao.getNeedPayStatusByRegisterServiceId(register_service_id);

        if (chargeMap == null) {
            chargePrice = 5000;
            System.out.println("查询该用户收费金额为空：" + chargePrice);
        } else {
            chargePrice = (Integer) chargeMap.get("appointment_price");
            System.out.println("该用户收费金额为：" + chargePrice);
        }
        System.out.println("是否收费判断标志" + chargePrice);
        return chargePrice;
    }

    @Override
    public PatientRegisterServiceVo selectByPrimaryKey(String id) {
        return patientRegisterServiceDao.selectByPrimaryKey(id);
    }

    ;

    @Override
    public Map findPersonAppointDetailInfo(PerAppDetInfoVo PerAppDetInfoVo) {
        return patientRegisterServiceDao.findPersonAppointDetailInfo(PerAppDetInfoVo);
    }

    @Override
    public HashMap<String, Object> findSysRegisterServiceByPRSIdExecute(HashMap<String, Object> hashMap) {
        return patientRegisterServiceDao.findSysRegisterServiceByPRSIdExecute(hashMap);
    }

    @Override
    public void completeShareExecute(HashMap<String, Object> executeMap) {
        patientRegisterServiceDao.completeShareExecute(executeMap);
    }

    @Override
    public List<PatientRegisterServiceVo> findPageRegisterServiceByPatient(
            Map map) {
        return patientRegisterServiceDao.findPageRegisterServiceByPatient(map);
    }

    @Override
    public void PatientRegisterServiceIsService(
            HashMap<String, Object> executeMap) {
        patientRegisterServiceDao.PatientRegisterServiceIsService(executeMap);
    }

    @Override
    public List<PatientRegisterServiceVo> findAllPatientRegisterService(HashMap seachMap) {
        return patientRegisterServiceDao.findAllPatientRegisterService(seachMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleWxMemberServiceNotifyInfo(String accountPay, PayRecord payRecord, String token) {
        //推送付费用户信息到管理员
        PayRecord pay = payRecordService.findById(payRecord.getId());
        if (!pay.getOrderId().equals("noData")) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("patient_register_service_id", pay.getOrderId());
            params.put("payWay", "0");
            params.put("accountPay", accountPay);
            params.put("openId", pay.getOpenId());

            HashMap<String, Object> registerMap = new HashMap<String, Object>();
            registerMap.put("id", pay.getOrderId());
            HashMap<String, Object> result = patientRegisterServiceDao.getPatientRegisterInfo(registerMap);
            if ("0".equals(result.get("status"))) {
                payRecord.getId();//修改pay_record表状态
                payRecord.setStatus("success");
                payRecord.setReceiveDate(new Date());
                payRecordService.updatePayInfoByPrimaryKeySelective(payRecord, "");
                //生成会员
                memberService.produceChargeMember("month", pay.getOpenId(), pay.getUserId(), "appoint", "");
                List<MemberservicerelItemservicerelRelationVo> memberServiceInfoList = memberService.findMemberPropertyAppAvailable(pay.getUserId());
                this.updateMemberOrderStatus(pay.getOrderId(), memberServiceInfoList.get(0).getId().toString());
            }

        } else {
            Integer memberservicerel_itemservicerel_relation_id = memberService.produceChargeMemberByMemberType("month", pay.getOpenId(), pay.getUserId(), "appoint", "");
            //更改账户记录memSrsItemSrsRelId值
            pay.setStatus("success");
            pay.setMemberservicerel_itemservicerel_relation_id(memberservicerel_itemservicerel_relation_id);
            pay.setReceiveDate(new Date());
            payRecordService.updatePayInfoByPrimaryKeySelective(pay, "");
        }
    }

    @Override
    public PatientRegisterServiceVo selectOrderInfoByPatientId(PatientRegisterServiceVo patientRegisterServiceVo) {
        return patientRegisterServiceDao.selectOrderInfoById(patientRegisterServiceVo);
    }


    /**
     * 更改会员订单状态
     *
     * @param patientRegisterServiceId 订单表主键
     * @param memSrsItemSrsRelId       会员关系表主键
     * @return 成功 "1"  失败  “0”
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberOrderStatusForCharge(String patientRegisterServiceId, String memSrsItemSrsRelId) throws ServiceException {

        //根据patientRegisterServiceId查询openid
        LogUtils.saveLog(Servlets.getRequest(), "00000026" ,"订单表主键"+ patientRegisterServiceId);//用户预约支付
        //更改订单状态
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("patientRegisterServiceId", patientRegisterServiceId);
        patientRegisterServiceDao.UpdatePatientRegisterService(hashMap);

        //服务次数-1
        String relationType = this.getRegisterAttributeOfHospital(patientRegisterServiceId);
        if (!relationType.equals("2")) {
            memberService.updateMemberLeftTimes(memSrsItemSrsRelId, "1");
        }

        PerAppDetInfoVo Info = new PerAppDetInfoVo();
        Info.setId(patientRegisterServiceId);
        Map map = patientRegisterServiceDao.findPersonAppointDetailInfo(Info);
        String content = map.get("babyName") + "小朋友家长，已付费预约" + map.get("doctorName") +
                "医生，就诊时间为" + map.get("date") + " " + "订单号：" + map.get("appointmentNo") + "预约手机号为：" + map.get("phone") + " ";
        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        ChangzhuoMessageUtil.sendMsgToManagerForAccount(content, token);
    }

    /**
     * 更改会员订单状态
     *
     * @param patientRegisterServiceId 订单表主键
     * @param memSrsItemSrsRelId       会员关系表主键
     * @return 成功 "1"  失败  “0”
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> updateMemberOrderStatus(String patientRegisterServiceId, String memSrsItemSrsRelId) throws ServiceException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("urlPath", "s68.baodf.com/titan");
        //根据patientRegisterServiceId查询openid
        PatientRegisterServiceVo patientRegisterServiceVo = new PatientRegisterServiceVo();
        patientRegisterServiceVo.setId(patientRegisterServiceId);
        patientRegisterServiceVo = patientRegisterServiceDao.selectOrderInfoById(patientRegisterServiceVo);
        response.put("openId", patientRegisterServiceVo.getOpenId());

        LogUtils.saveLog(Servlets.getRequest(), "00000025" ,"订单表主键"+ patientRegisterServiceId);
        //更改订单状态
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("patientRegisterServiceId", patientRegisterServiceId);
        hashMap.put("memSrsItemSrsRelId", memSrsItemSrsRelId);
        patientRegisterServiceDao.UpdatePatientRegisterService(hashMap);
        //更改账户记录memSrsItemSrsRelId值
        PayRecord payRecord = new PayRecord();
        payRecord.setOrderId(patientRegisterServiceId);
        payRecord.setMemberservicerel_itemservicerel_relation_id(Integer.parseInt(memSrsItemSrsRelId));
        payRecordService.updateByOrderId(payRecord);
        //服务次数-1，在此判断，用户下的订单，所属医院，是否是合作医院
        String relationType = this.getRegisterAttributeOfHospital(patientRegisterServiceId);
        if (!relationType.equals("2")) {
            memberService.updateMemberLeftTimes(memSrsItemSrsRelId, "-1");
        }

        PerAppDetInfoVo Info = new PerAppDetInfoVo();
        Info.setId(patientRegisterServiceId);
        response.put("patient_register_service_id", patientRegisterServiceId);
        response.put("status", "1");

        //启动线程 保存订单属性 controller层处理
        OrderPropertyVo orderPropertyVo = new OrderPropertyVo();
        orderPropertyVo.setPatientRegisterServiceId(patientRegisterServiceId);
        orderPropertyVo.setOpenid(patientRegisterServiceVo.getOpenId());
        orderPropertyVo.setOrderSource("weixin");
        orderPropertyService.saveOrderProperty(orderPropertyVo);

        //插入消息
        HashMap<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("patient_register_service_id", patientRegisterServiceId);
        messageMap.put("openId", patientRegisterServiceVo.getOpenId());
        messageMap.put("urlPath", "s68.baodf.com/titan");
        orderMessageService.sendMessage(messageMap, true);
        return response;
    }

    /**
     * 根据订单主键查询该订单下的医生与医院的关系
     *
     * @param patientRegisterServiceId
     * @return
     */
    @Override
    public String getRegisterAttributeOfHospital(String patientRegisterServiceId) {
        PatientRegisterServiceVo pVo = new PatientRegisterServiceVo();
        pVo.setId(patientRegisterServiceId);
        DoctorHospitalRelationVo dhVo = patientRegisterServiceDao.getRegisterAttributeOfHospital(pVo);
        if (dhVo != null) {
            return dhVo.getRelationType();
        }
        return "";
    }

    public HashMap<String, Object> getlastPatientRegisterInfo(String userPhone){
        return patientRegisterServiceDao.getlastPatientRegisterInfo(userPhone);
    }

}
