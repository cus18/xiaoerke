package com.cxqm.xiaoerke.modules.task.service.impl;

import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.sdk.CCPRestSDK;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.operation.service.BaseDataService;
import com.cxqm.xiaoerke.modules.operation.service.DataStatisticService;
import com.cxqm.xiaoerke.modules.operation.service.OperationsComprehensiveService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.plan.service.PlanMessageService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.CustomerService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.task.service.ScheduleTaskService;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by 得良 on 2015/6/17.
 */
public class ScheduledTask {

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private SystemService systemService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private DataStatisticService dataStatisticsService;

    @Autowired
    private BaseDataService baseDataService;

    @Autowired
    private OperationsComprehensiveService operationComprehensiveService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @Autowired
    private PlanMessageService planMessageService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InsuranceRegisterServiceService insuranceService;

    @Autowired
    private ConsultPhoneOrderService consultPhoneOrderService;

    @Autowired
    private ConsultPhonePatientService consultPhonePatientService;

    @Autowired
    private ConsultPhoneService consultPhoneService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoService;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    //将所有任务放到一个定时器里，减少并发
    //@Scheduled(cron = "0 */1 * * * ?")
    public void letsGoReminder() {
        /**
         * 以下状态代表sys_monitor中status状态，根据此状态发送短信或插入消息（sys_monitor表用于监控订单的状态）
         * 1、订单生成5分钟内未支付：  status为0；
         * 2、出发前三小时提醒：  status为1或0；
         * 3、就诊前超过预约时间3分钟评价提醒：status为2；
         * 4、就诊时间结束：  status为3；
         * 5、就诊后评价提醒：  status为4；
         */
        //一：订单生成30分钟内未支付
        System.out.println(new Date() + " package.controller scheduled test --> AppointNoPay");
        List<HashMap<String, Object>> listPay = scheduleTaskService.AppointNoPay();
        if (listPay != null && !listPay.isEmpty()) {
            produceMessage(listPay, "2");
            String status = "1";
            updateMonitorStatus(listPay, status);
        }

        // 二：出发提醒，出发前三小时发送提醒
        System.out.println(new Date() + " package.controller scheduled test --> LetsGoReminder");
        List<HashMap<String, Object>> listReminder = scheduleTaskService.LetsGoReminder();
        if (listReminder != null && !listReminder.isEmpty()) {
            //判断当前时间是否大于出发前3个小时评价
            produceMessage(listReminder, "1");
            String status = "2";
            updateMonitorStatus(listReminder, status);
        }

        //三：就诊后3分钟时，发送短信/微信至用户，评价提醒
        System.out.println(new Date() + " package.controller scheduled test --> evaluaReminder");
        List<HashMap<String, Object>> listPrase = scheduleTaskService.evaluaReminder();
        if (listPrase != null && !listPrase.isEmpty()) {
            produceMessage(listPrase, "0");
            String status = "3";
            updateMonitorStatus(listPrase, status);
        }

        //四、订单生成30分钟内未支付,则取消订单(更新patient_register_service中的status，并且更新sys_register_service中的状态)
        System.out.println(new Date() + " package.controller scheduled test --> CancelAppointNoPay");
        scheduleTaskService.CancelAppointNoPay();

        //五、监听就诊结束时间，就诊时间结束的话更新patient_register_service表status字段
        System.out.println(new Date() + " package.controller scheduled test --> changeStatus");
        scheduleTaskService.updateSrSerStatus();

        //患者预约5分钟后发送订单消息给医生
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH");
        int time = Integer.parseInt(format.format(date));
        if (22 > time && time > 7) {
            System.out.println(new Date() + " package.controller scheduled test --> sendMsgToDoc");
            List<HashMap<String, Object>> doctorMsg = scheduleTaskService.getOrderInfoToDoc();
            List<String> classList = new ArrayList<String>();
            for (HashMap<String, Object> map : doctorMsg) {
                if (!(map.get("hospitalContactPhone").equals(""))) {
                    String content = map.get("babyName") +
                            "宝宝及家长预约了" + map.get("doctorName") + "在" + map.get("date") + " " + map.get("begin_time") +
                            "的门诊。若医生因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
                    ChangzhuoMessageUtil.sendMsg((String) map.get("hospitalContactPhone"), content, ChangzhuoMessageUtil.RECEIVER_TYPE_DOCTOR);
                    //消息修改
                    //DoctorMsgTemplate.doctorAppointmentRemindAt5minLater2Sms((String) map.get("doctorName"), (String) map.get("babyName"),
                    // (String) map.get("hospitalContactPhone"), (String)map.get("date"),"", (String)map.get("begin_time"),(String)map.get("hospitalName"));
                    classList.add((String) map.get("id"));
                } else {
                    String content = "  尊敬的" + map.get("doctorName") + "医生，" + map.get("babyName") +
                            "宝宝及家长预约了您在" + map.get("date") + " " + map.get("begin_time") +
                            "的门诊。若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利！";
                    //消息修改
                    String visiteDate = DateUtils.DateToStr((Date) map.get("date"), "date");
                    String dateToStr = DateUtils.DateToStr((Date) map.get("begin_time"), "time");
                    String week = DateUtils.getWeekOfDate((Date) map.get("date"));
                    DoctorMsgTemplate.doctorAppointmentRemindAt5minLater2Sms((String) map.get("doctorName"), (String) map.get("babyName"), (String) map.get("phone"), visiteDate, week, dateToStr, (String) map.get("hospitalName"), (String) map.get("userPhone"));

                    classList.add((String) map.get("id"));
                    //微信推送
                    Map tokenMap = systemService.getDoctorWechatParameter();
                    String token = (String) tokenMap.get("token");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                    String nowTime = simpleDateFormat.format(new Date());
                    //消息修改
                    DoctorMsgTemplate.doctorAppointmentRemindAt5minLater2Wechat((String) map.get("doctorName"), (String) content, nowTime, token, "", (String) map.get("openid"));
                }
            }
            if (classList.size() > 0) {
                scheduleTaskService.updateOrderInfoToDoc(classList);
            }
        }

        //就诊24小时后向用户发送短息
        List<HashMap<String, Object>> trackList = messageService.getTrackOrder();
        //微信推送
        Map tokenMap = systemService.getWechatParameter();
        String token = (String) tokenMap.get("token");
        if (trackList.size() > 0) {
            String patientRegisterId = "";
            for (HashMap<String, Object> map : trackList) {
                patientRegisterId += map.get("phone") + ",";
                String content = "亲爱的宝妈宝爸，目前宝宝已就诊24小时了，病情好些了吗？如果您对诊后病况、" +
                        "用药方法等还有疑问，请及时到宝大夫平台在线咨询，专业儿科医生将为您提供服务，祝宝宝早日康复。点击可查看如何咨询。";
                PatientMsgTemplate.afterDiagnosisRemind2Wechat((String) map.get("openid"), token, (String) map.get("doctorName"), content,
                        "http://baodf.com/titan/appoint#/guideConsult");
            }
            String status = "4";
            updateMonitorStatus(trackList, status);
        }

    }

    public void phoneConsultReminder2Doc() {

        //电话咨询预约成功5min后发消息给医生
        sendMsgToDoc5minAfterSuccess();

        //电话咨询接通前5min发消息提醒医生
        sendMsgToDoc5minBeforeConnect();
    }

    //每天晚上11：00更新sys_register_service中的status状态
    //@Scheduled(cron = "0 0 23 * * ?")
    public void updateSrSerStatus() {
        scheduleTaskService.updateSrSAndPrsStatus();
    }

    //每天晚上八点提示医生次天的就诊人员
    //@Scheduled(cron = "0 0 20 * * ?")
    public void sendMsgToDocAtNight() {

        Map tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");
        List<HashMap<String, Object>> list = scheduleTaskService.getOrderInfoToday();
        List<HashMap<String, Object>> doctorList = scheduleTaskService.getOrderDoctorToday();
        List<String> classList = new ArrayList<String>();

        List<HashMap<String, Object>> doctorOrderInfoList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < doctorList.size(); i++) {
            HashMap<String, Object> listMap = new HashMap<String, Object>();
            HashMap<String, Object> mapi = doctorList.get(i);
            listMap.put("doctorName", mapi.get("doctorName"));
            listMap.put("hospitalContactPhone", mapi.get("hospitalContactPhone"));
            listMap.put("hospitalName", mapi.get("hospitalName"));
            listMap.put("doctorPhone", mapi.get("phone"));
            List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
            for (int j = 0; j < list.size(); j++) {
                HashMap<String, Object> newMap = new HashMap<String, Object>();
                HashMap<String, Object> mapj = list.get(j);
                if ((mapi.get("doctorName").equals(mapj.get("doctorName"))) &&
                        (mapi.get("phone").equals(mapj.get("phone"))) &&
                        (mapi.get("hospitalContactPhone").equals(mapj.get("hospitalContactPhone"))) &&
                        (mapi.get("hospitalName").equals(mapj.get("hospitalName")))) {
                    newMap.put("babyName", mapj.get("babyName"));
                    newMap.put("begin_time", mapj.get("begin_time"));
                    newMap.put("openid", mapj.get("openid"));
                    newList.add(newMap);
                    classList.add((String) mapj.get("id"));
                }
                listMap.put("patientList", newList);
            }
            doctorOrderInfoList.add(listMap);
        }

        for (int i = 0; i < doctorOrderInfoList.size(); i++) {
            HashMap<String, Object> orderMap = doctorOrderInfoList.get(i);
            if (!(orderMap.get("hospitalContactPhone").equals(""))) {
                String doctorName = (String) orderMap.get("doctorName");
                String hospitalName = (String) orderMap.get("hospitalName");
                int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
                String content = "";
                for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                    String babyName = (String) map.get("babyName");
                    String begin_time = (String) map.get("begin_time");
                    content = content + babyName + begin_time + ",";
                }
                String contentSMS = "【接诊一览：" + doctorName + "】" + hospitalName + "，明天已有" + num + "名宝宝预约了"
                        + doctorName + "医生的门诊：" + content.substring(0, content.length() - 1) +
                        "。若医生因紧急情况不能按时出诊，请您联系客服：400-623-7120。宝大夫祝您工作顺利！";
                //发送短信给医生
                ChangzhuoMessageUtil.sendMsg((String) orderMap.get("hospitalContactPhone"),
                        contentSMS, ChangzhuoMessageUtil.RECEIVER_TYPE_DOCTOR);
                System.out.println(contentSMS);
            } else {
                String doctorName = (String) orderMap.get("doctorName");
                int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
                String content = "";
                for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                    String babyName = (String) map.get("babyName");
                    String begin_time = (String) map.get("begin_time");
                    content = content + babyName + begin_time + ",";
                }
                DoctorMsgTemplate.doctorAppointmentRemindAtNight2Sms((String) orderMap.get("doctorPhone"), doctorName, String.valueOf(num), content);

                //如果医生有微信ID，则推送微信消息
                if (orderMap.containsKey("openid")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                    String nowTime = simpleDateFormat.format(new Date());
                    String openid = (String) orderMap.get("openid");
                    DoctorMsgTemplate.doctorAppointmentRemindAtNight2Wechat(nowTime, String.valueOf(num), content, token, "", openid);
                }
            }
        }

        //微信推送当日就诊的结算金额
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        Calendar now = Calendar.getInstance();
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = now.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        //获取今天有预约的意思
        List<HashMap<String, Object>> listSettlementDoc = scheduleTaskService.getSettlementDocToday(dateNowStr);

        for (Map<String, Object> settlementDoc : listSettlementDoc) {
            HashMap<String, Object> settlementInfo = new HashMap<String, Object>();
            patientRegisterService.findDoctorSettlementAppointmentInfoByDate((String) settlementDoc.get("id"),
                    (String) settlementDoc.get("sys_user_id"),
                    dateNowStr, settlementInfo);
            String st = "今日账单提醒\n\n" + dateNowStr + "\n帐单日期:" + now.get(Calendar.YEAR)
                    + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH)
                    + "日" + "(" + weekDays[w] + ")" + "\n 今日收入:" + settlementInfo.get("totalAppPrice") + "元\n" +
                    "【详情请点击“每日清单”查看】\n";
            DoctorMsgTemplate.dotorAppointmentBill2Wechat(now.get(Calendar.YEAR)
                            + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH)
                            + "日" + "(" + weekDays[w] + ")"
                    , (String) settlementInfo.get("totalAppPrice"), token,
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb6b6ad2a55af0567&redirect_uri=" +
                            "http%3a%2f%2fs11.baodf.com%2fxiaoerke-doctor%2fap%2fwechatInfo%2fgetDoctorWechatMenId%3furl%3d1" +
                            "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect",
                    (String) settlementDoc.get("openid"));
        }

        if (classList.size() > 0) {
            scheduleTaskService.updateOrderInfoToDoc(classList);
        }
    }

    //早晨七点提示医生今天就诊人员
    //@Scheduled(cron = "0 0 7 * * ?")
    public void sendMsgToDocAtMorning() {

        Map tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");
        List<HashMap<String, Object>> list = scheduleTaskService.getOrderInfoAtMornings();
        List<HashMap<String, Object>> doctorList = scheduleTaskService.getOrderDoctorAtMornings();
        List<String> classList = new ArrayList<String>();

        List<HashMap<String, Object>> doctorOrderInfoList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < doctorList.size(); i++) {
            HashMap<String, Object> listMap = new HashMap<String, Object>();
            HashMap<String, Object> mapi = doctorList.get(i);
            listMap.put("doctorName", mapi.get("doctorName"));
            listMap.put("hospitalContactPhone", mapi.get("hospitalContactPhone"));
            listMap.put("hospitalName", mapi.get("hospitalName"));
            listMap.put("doctorPhone", mapi.get("phone"));
            List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
            for (int j = 0; j < list.size(); j++) {
                HashMap<String, Object> newMap = new HashMap<String, Object>();
                HashMap<String, Object> mapj = list.get(j);
                if ((mapi.get("doctorName").equals(mapj.get("doctorName"))) &&
                        (mapi.get("phone").equals(mapj.get("phone"))) &&
                        (mapi.get("hospitalContactPhone").equals(mapj.get("hospitalContactPhone"))) &&
                        (mapi.get("hospitalName").equals(mapj.get("hospitalName")))) {
                    newMap.put("babyName", mapj.get("babyName"));
                    newMap.put("begin_time", mapj.get("begin_time"));
                    newMap.put("openid", mapj.get("openid"));
                    newList.add(newMap);
                    classList.add((String) mapj.get("id"));
                }
                listMap.put("patientList", newList);
            }
            doctorOrderInfoList.add(listMap);
        }

        for (int i = 0; i < doctorOrderInfoList.size(); i++) {
            HashMap<String, Object> orderMap = doctorOrderInfoList.get(i);
            if (!(orderMap.get("hospitalContactPhone").equals(""))) {
                String doctorName = (String) orderMap.get("doctorName");
                String hospitalName = (String) orderMap.get("hospitalName");
                int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
                String content = "";
                for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                    String babyName = (String) map.get("babyName");
                    String begin_time = (String) map.get("begin_time");
                    content = content + babyName + begin_time + ",";
                }
                String contentSMS = "【接诊一览：" + doctorName + "】" + hospitalName + "，今天新增有" + num + "名宝宝预约了"
                        + doctorName + "医生的门诊：" + content.substring(0, content.length() - 1) +
                        "。若医生因紧急情况不能按时出诊，请您联系客服：400-623-7120。宝大夫祝您工作顺利！";
                //发送短信给医生
                ChangzhuoMessageUtil.sendMsg((String) orderMap.get("hospitalContactPhone"),
                        contentSMS, ChangzhuoMessageUtil.RECEIVER_TYPE_DOCTOR);
                System.out.println(contentSMS);
            } else {
                String doctorName = (String) orderMap.get("doctorName");
                int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
                String content = "";
                for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                    String babyName = (String) map.get("babyName");
                    String begin_time = (String) map.get("begin_time");
                    content = content + babyName + begin_time + ",";
                }
                DoctorMsgTemplate.doctorAppointmentRemindAtMoning2Sms((String) orderMap.get("doctorPhone"), doctorName, String.valueOf(num), content);

                //如果医生有微信ID，则推送微信消息
                if (orderMap.containsKey("openid")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
                    String nowTime = simpleDateFormat.format(new Date());
                    String openid = (String) orderMap.get("openid");
                    DoctorMsgTemplate.doctorAppointmentRemindAtMoning2Wechat(nowTime, String.valueOf(num), content, token, "", openid);
                }
            }
        }
        if (classList.size() > 0) {
            scheduleTaskService.updateOrderInfoToDoc(classList);
        }
    }

    /**
     * 每天凌晨两点统计
     */
    public void statistics() {
        //用户分析
        String new_add_users = "0";//新增用户数
        String cancel_new_users = "0";//取消用户数
        String actual_new_user = "0";//净增用户数
        String sum_user = "0";//累计用户数
        //医生分析
        String new_add_doctor = "0";//新增医生数
        String total_doctor = "0";//累计医生数
        //预约分析
        String new_order_number = "0";//新增订单数
        String unsuccessful_order = "0";//不成功订单数（取消+异常）
        String successful_order = "0";//成功订单数
        String account_success_order = "0";//累计成功订单数

        Date newDate = new Date();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
        try {
            newDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("statisticsData", newDate);

        //获取数据
        List<HashMap<String, Object>> resultList = dataStatisticsService.findStatistics(hashMap);
        if (resultList != null && resultList.size() > 0) {
            for (HashMap newMap : resultList) {
                String number = String.valueOf(StringUtils.ObjectToLong(newMap.get("countNumber")));
                if (number == null || "".equals(number)) {//防止前一天关注了，后一天取消
                    number = "0";
                }
                if ("微信公众平台关注".equals(newMap.get("title"))) {
                    new_add_users = number;//新增用户数
                } else if ("微信公众平台取消关注".equals(newMap.get("title"))) {
                    cancel_new_users = number;//取消用户数
                } else if ("微信公众平台取消关注".equals(newMap.get("title"))) {
                    cancel_new_users = number;
                }
            }

            //--------------------获取用户分析数据-------------------------
            actual_new_user = Integer.parseInt(new_add_users) - Integer.parseInt(cancel_new_users) + "";//净增用户数
            sum_user = dataStatisticsService.findStatisticsConcern();//累计关注数

            //--------------------获取医生分析数据-------------------------
            new_add_doctor = dataStatisticsService.findCountDoctorNumber(hashMap);//新增医生数
            total_doctor = dataStatisticsService.findCountDoctorCountNmuber();//系统内总的医生数
            //--------------------获取预约分析数据-------------------------
            new_order_number = dataStatisticsService.findNewAddOrderNmuber(hashMap);//新增订单数
            unsuccessful_order = dataStatisticsService.findUnSuccessOrderNumber(hashMap);//不成功订单数

            successful_order = Integer.parseInt(new_order_number) - Integer.parseInt(unsuccessful_order) + "";//成功订单数

            account_success_order = dataStatisticsService.findCountOrderNumber();//累计成功订单数

            //-------------------------------------激活分析数据-----------------------------------------
            //获取所有当天所有预约信息（包括openId）
            List<HashMap<String, Object>> appointInfo = dataStatisticsService.findAllAppointByDate(hashMap);

            //获取所有当天所有咨询信息(包括openId)
            List<HashMap<String, Object>> ConsultInfo = dataStatisticsService.findAllConsultByDate(hashMap);

            //获取访问郑玉巧说的所有用户的openid/create_date
            List<HashMap<String, Object>> zhengYuQiaoList = dataStatisticsService.findAllReaderZhengYuQiao(hashMap);

            //访问郑玉巧说主页
            int newZhengYuQiao = 0;
            int oldZhengYuQiao = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);//前一天 00：00：00
            Long visitTime = calendar.getTimeInMillis();
            for (HashMap map : zhengYuQiaoList) {
                Long attentionDate = ((Date) map.get("date")).getTime();//关注时间
                if (visitTime < attentionDate) {//访问时间 > 关注时间 = 新关注用户+1
                    ++newZhengYuQiao;
                } else {
                    ++oldZhengYuQiao;
                }
            }

            // 分类:新关注用户 or 老用户 预约信息
            int newConcernNumber = 0;
            int oldConcernNumber = 0;
            if (appointInfo != null && appointInfo.size() > 0) {
                for (HashMap newMap : appointInfo) {
                    newMap.put("date", newDate);//new Date()new Date()
                    //此预约如果是老用户，返回0，新用户返回1
                    Boolean appFlag = dataStatisticsService.judgeAppIsNew(newMap);
                    if (appFlag)
                        newConcernNumber++;
                    else
                        oldConcernNumber++;
                }
            }
            // 分类:新关注用户 或者是 老用户 咨询信息
            int newConcernNumber_Con = 0;
            int oldConcernNumber_Con = 0;
            if (ConsultInfo != null && ConsultInfo.size() > 0) {
                for (HashMap newMap : ConsultInfo) {
                    newMap.put("date", newDate);
                    //此咨询如果是老用户，返回true，新用户返回false
                    Boolean conFlag = dataStatisticsService.judgeNewConIsExistWeChat(newMap);
                    if (conFlag) {//咨询用户为新用户
                        newConcernNumber_Con++;
                    } else {
                        oldConcernNumber_Con++;
                    }
                }
            }
            //查询总活跃用户数
            int sumAccount = dataStatisticsService.findSumActive(hashMap);
            //查询当天活跃数
            hashMap.put("date", newDate);
            int sumForOneDay = dataStatisticsService.findSumActive(hashMap);

            //---------------------------------获取咨询分析数据----------------------------
            //需求：获取新增咨询人数，其中已知 ConsultInfo 为今天所有的咨询信息，所以只要根据ConsultInfo中的openId和date（不是今天）进行过滤，查询sys_wechatattention表有无记录即可
            int conAnalysis = 0;//新增咨询数
            int alreadyCon = 0;//已咨询人数
            if (ConsultInfo != null && ConsultInfo.size() > 0) {
                for (HashMap newMap : ConsultInfo) {
                    newMap.put("date", newDate);
                    //为true表示此用户之前咨询过
                    Boolean addFlag = dataStatisticsService.judgeNewConIsExistWeChat(newMap);
                    if (addFlag)
                        conAnalysis++;
                }
            }

            //获取已咨询人数
            alreadyCon = dataStatisticsService.alreadyConNumber();

            //--------------------------------------------保存数据----------------------------------------
            HashMap<String, Object> saveMap = new HashMap<String, Object>();
            saveMap.put("id", IdGen.uuid());
            saveMap.put("new_add_users", new_add_users);//新关注数
            saveMap.put("cancel_new_users", cancel_new_users);//取消关注数
            saveMap.put("actual_new_user", actual_new_user);//净关注数
            saveMap.put("sum_user", sum_user);//累计关注数

            saveMap.put("new_add_doctor", new_add_doctor);//新增医生数
            saveMap.put("total_doctor", total_doctor);//医生总数

            saveMap.put("new_order_number", new_order_number);//新增订单数
            saveMap.put("unsuccessful_order", unsuccessful_order);//不成功订单数
            saveMap.put("successful_order", successful_order);//成功订单数
            saveMap.put("account_success_order", account_success_order);//累计成功订单数

            saveMap.put("new_add_consultation_number", conAnalysis);//新增咨询数
            saveMap.put("count_consultation_number", alreadyCon);//已咨询人数

            saveMap.put("activate_new_user", newConcernNumber_Con);//新关注咨询
            saveMap.put("activate_order", newConcernNumber);// 新关注预约
            saveMap.put("activate_order_consultation_number", oldConcernNumber);  //老用户预约人数
            saveMap.put("consultation", oldConcernNumber_Con);//老用户咨询人数
            saveMap.put("appoint", null);//新用户活跃数
            saveMap.put("sumcon", null);//老用户活跃数
            saveMap.put("alreadyCon", sumForOneDay);//当天总活跃用户数
            saveMap.put("create_date", newDate);
            //新增
            saveMap.put("sumForOneDay", sumForOneDay);//查询当天活跃数

            saveMap.put("sumAccount", sumAccount);//累计活跃用户数
            saveMap.put("newZhengYuQiao", newZhengYuQiao);//郑玉巧说（新）
            saveMap.put("oldZhengYuQiao", oldZhengYuQiao);//郑玉巧说（老）
            dataStatisticsService.saveDataStatistics(saveMap);//保存数据

        }
    }

    /**
     * 微信状态。启动时执行一次，之后每隔100分钟执行一次
     */
    public void persistRecord() {
        try {
            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            System.out.print("用户端微信参数更新");
            String token = WechatUtil.getToken(sysPropertyVoWithBLOBsVo.getUserCorpid(), sysPropertyVoWithBLOBsVo.getUserSectet());
            String ticket = WechatUtil.getJsapiTicket(token);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("ticket", ticket);
            map.put("id", "1");
            scheduleTaskService.updateWechatParameter(map);
            sessionRedisCache.putWeChatParamToRedis(map);

            System.out.print("医生端微信参数更新");
            token = WechatUtil.getToken(sysPropertyVoWithBLOBsVo.getDoctorCorpid(), sysPropertyVoWithBLOBsVo.getDoctorSectet());
            ticket = WechatUtil.getJsapiTicket(token);
            map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("ticket", ticket);
            map.put("id", "2");
            scheduleTaskService.updateWechatParameter(map);
            sessionRedisCache.putWeChatParamToRedis(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每天凌晨一点将前天的多客服聊天记录保存到数据库中
     */
    public void getWechatRecoder() {
        Map<String, Object> info = systemService.getWechatParameter();
        String accessToken = (String) info.get("token");
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = new Date();
            Calendar date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
            Date endDate = dft.parse(dft.format(date.getTime()));
            String dateTime = dft.format(endDate);
            List<WechatRecord> li = new ArrayList<WechatRecord>();
            WechatUtil.setWechatInfoToDb(dateTime, accessToken, 1, li);
            scheduleTaskService.saveWechatRecoder(li);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新Mointor状态
     *
     * @param listPay
     * @param status
     */
    private void updateMonitorStatus(List<HashMap<String, Object>> listPay, String status) {
        for (Map paymap : listPay) {
            HashMap<String, Object> newhash = new HashMap<String, Object>();
            newhash.put("patient_register_service_id", paymap.get("patient_register_service_id"));
            newhash.put("status", status);
            scheduleTaskService.setMonitorStatus(newhash);
        }
    }

    /**
     * 生成第五周号源，用于重复设置号源
     * sunxiao
     */
    public void repeatSettingRegister() {
        scheduleTaskService.repeatSettingRegister();
    }

    /**
     * 生成第五周号源，用于重复设置电话咨询号源
     * sunxiao
     */
    public void repeatSettingConsultPhoneRegister() {
        scheduleTaskService.repeatSettingConsultPhoneRegister();
    }

    /**
     * 插入信息 @author zdl
     *
     * @param list 信息
     * @param flag 标志
     */
    private void produceMessage(List<HashMap<String, Object>> list, String flag) {
        Map<String, Object> parameter = systemService.getWechatParameter();
        for (Map hashMap : list) {
            HashMap<String, Object> insertMap = new HashMap<String, Object>();
            String messageId1 = IdGen.uuid();
            Date create_date = new Date();
            insertMap.put("id", messageId1);
            //暂时这样写，防止hashMap.get("patientid")为空,数据问题
            if (hashMap.get("patientid") != null && !"".equals(hashMap.get("patientid"))) {
                insertMap.put("sys_user_id", hashMap.get("patientid"));
            }
            insertMap.put("create_date", create_date);
            insertMap.put("appointmentNo", hashMap.get("register_no"));
            insertMap.put("message_type", "0");//0表示与加号有关的信息
            if ("0".equals(flag)) {
                insertMap.put("message_title", "评价提醒");
                //（评价提醒）@小朋友家长您好，感谢您使用宝大夫预约@医生见面咨询，登录宝大夫完成评价，并将评价分享至朋友圈还可再拿10元现金！（@）
                insertMap.put("message_content", "（评价提醒）" + hashMap.get("babyName") + "小朋友家长您好，感谢您使用宝大夫预约" + hashMap.get("doctorName") + "医生见面咨询，请登录宝大夫完成评价。");

                //消息修改
                PatientMsgTemplate.evaluationRemind2Sms((String) hashMap.get("phone"), (String) hashMap.get("babyName"), (String) hashMap.get("doctorName"));
                messageService.insertMessage(insertMap);
                if (hashMap.containsKey("openid") && hashMap.get("openid") != null) {
                    //消息修改
                    PatientMsgTemplate.evaluationRemind2Wechat((String) hashMap.get("openid"),
                            (String) parameter.get("token"), "baodf.com/titan/appoint#/evaluateList/" +
                                    hashMap.get("patient_register_service_id"), "您的订单可以评价了哦!",
                            (String) hashMap.get("register_no"), (String) hashMap.get("create_date"), "");
                }
            } else if ("1".equals(flag)) {
                //将时间处理一下
                Date TOdate = dateToStrToDate(hashMap);
                long hour = DateUtils.pastHour(TOdate);//取整肯定为2
                if (hour == -2L) {//小于出发时间三小时则发送消息
                    insertMap.put("message_title", "出发提醒");
                    insertMap.put("message_content", "（出发提醒）您预约的" + hashMap.get("doctorName") +
                            "医生将在" + hashMap.get("begin_time") + "-" + hashMap.get("end_time") +
                            "为您提供服务，请准备好病历、诊疗卡，注意出发时间，迟到医生将无法接待；" +
                            "同时，特别提醒您遵守医院就诊规则，在医院吵闹和不尊重医生者可能被列入宝大夫黑名单。" +
                            "如需帮助或投诉请致电400-623-7120。");
                    ChangzhuoMessageUtil.sendMsg((String) hashMap.get("phone"), (String) insertMap.get("message_content"));
                    messageService.insertMessage(insertMap);
                }

            } else if ("2".equals(flag)) {
                insertMap.put("message_title", "订单未支付");
                insertMap.put("message_content", "您预约的" + hashMap.get("doctorName") + "医生订单已生成，" +
                        "请于30分钟内输入邀请码，以免订单超时被取消。宝大夫客服400-623-7120。");
                messageService.insertMessage(insertMap);
            }
        }
    }

    /**
     * 定时任务,用于监测多客服在线时长-没五分扫描一次
     */
    public void getCustomerOnlineTime() {
        Map<String, Object> info = systemService.getWechatParameter();
        String accessToken = (String) info.get("token");
        ArrayList<CustomBean> list = WechatUtil.getcustomInfo(accessToken);
        //将当前在线的客服存入数据库
        scheduleTaskService.getCustomerOnlineTime(list);

    }

    public void prepareOperationStatisticData() {
        //通过接口，获取所有关注平台的openid
        WechatUtil wxUtil = new WechatUtil();
        Map token = systemService.getWechatParameter();

        //获取昨天的日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());

        //在日志表中，获取昨天使用平台的用户ID
        List<HashMap<String, Object>> userListYesterday = scheduleTaskService.getUserListYesterday(yesterday);
        List<HashMap<String, Object>> userLog = new ArrayList<HashMap<String, Object>>();

        for (Map userListInfo : userListYesterday) {
            HashMap<String, Object> userStatisticInfo = new HashMap<String, Object>();
            //根据用户的openid，获取用户的名称，来源，关注时间
            if (userListInfo.get("open_id") != null) {
                WechatBean userinfo = wxUtil.getWechatName((String) token.get("token"), (String) userListInfo.get("open_id"));
                HashMap<String, Object> userAttention = wechatAttentionService.getAttention((String) userListInfo.get("open_id"));
                userStatisticInfo.put("id", IdGen.uuid());
                userStatisticInfo.put("openid", (String) userListInfo.get("open_id"));
                userStatisticInfo.put("date", yesterday);
                if (StringUtils.isNotNull(userinfo.getNickname())) {
                    userStatisticInfo.put("name", EmojiFilter.coverEmoji(userinfo.getNickname().trim()));
                } else {
                    userStatisticInfo.put("name", null);
                }
                if (userAttention != null) {
                    userStatisticInfo.put("marketid", userAttention.get("marketer"));
                    userStatisticInfo.put("attentiondate", userAttention.get("date"));
                } else {
                    userStatisticInfo.put("marketid", null);
                    userStatisticInfo.put("attentiondate", null);
                }
                HashMap<String, Object> dataParam = new HashMap<String, Object>();
                dataParam.put("open_id", userListInfo.get("open_id"));
                dataParam.put("date", yesterday);
                dataParam.put("alhos", "获取系统内所有医院 ");
                dataParam.put("ahosaldoc", "获取医院医生");
                dataParam.put("ahosaldep", "获取医院所有科室");
                dataParam.put("ahosadepaldoc", "获取医院的某个科室的医生");
                dataParam.put("alfirstill", "获取疾病一级分类");
                dataParam.put("afirstalsecill", "获取一级疾病下的二级分类");
                dataParam.put("asecillaldoc", "获取二级分类疾病下的专业医生");
                dataParam.put("asecillahosaldoc", "获取二级分类疾病下的某个医院的专业医生");
                dataParam.put("asecillalhos", "获取二级分类疾病下的所有关联医院");
                dataParam.put("adatealhos", "获取某个预约日期下的可预约的医院");
                dataParam.put("adatealdoc", "获取某个预约日期下的可预约医生");
                dataParam.put("adateahosaldoc", "获取某个预约日期下的可预约的医院下的医生");
                dataParam.put("adocdetail", "获取某个医生的详细信息");
                dataParam.put("adocadateserv", "获取某个医生的某天的加号信息");
                dataParam.put("adocaservdetail", "获取某个医生的某个号的详细信息");
                dataParam.put("apdoc", "用户预约医生");
                dataParam.put("canceldoc", "用户取消预约");
                dataParam.put("appraisedoc", "用户评价医生");
                dataParam.put("sharedoc", "用户分享");
                dataParam.put("servattention", "用户预约挂号提醒");
                dataParam.put("attentiondoc", "查询我的关注医生信息");
                dataParam.put("checkservroute", "搜索号源的交通信息");
                dataParam.put("checkservstatus", "搜索号源的状态");
                dataParam.put("checkdocaplocation", "搜索医生的就诊地址信息");
                dataParam.put("checkdoc7daylocation", "搜索医生7天内的出诊位置信息");
                dataParam.put("checkdoc7dayapbylocation", "根据医生的出诊地点获取医生7天内的出诊信息");
                dataParam.put("myfirstpage", "获取个人中心主页的信息");
                dataParam.put("myappointment", "进入我的预约版块");
                dataParam.put("myapdetail", "获取个人的预约信息详情");
                dataParam.put("myselfinfo", "获取个人信息");
                dataParam.put("attdoc", "关注医生");
                HashMap<String, Object> userOperationStatistic = scheduleTaskService.getUserOperationStatistic(dataParam);

                userStatisticInfo.put("alhos", Integer.valueOf(userOperationStatistic.get("alhos").toString()));
                userStatisticInfo.put("ahosaldoc", Integer.valueOf(userOperationStatistic.get("ahosaldoc").toString()));
                userStatisticInfo.put("ahosaldep", Integer.valueOf(userOperationStatistic.get("ahosaldep").toString()));
                userStatisticInfo.put("ahosadepaldoc", Integer.valueOf(userOperationStatistic.get("ahosadepaldoc").toString()));
                userStatisticInfo.put("alfirstill", Integer.valueOf(userOperationStatistic.get("alfirstill").toString()));
                userStatisticInfo.put("afirstalsecill", Integer.valueOf(userOperationStatistic.get("afirstalsecill").toString()));
                userStatisticInfo.put("asecillaldoc", Integer.valueOf(userOperationStatistic.get("asecillaldoc").toString()));
                userStatisticInfo.put("asecillahosaldoc", Integer.valueOf(userOperationStatistic.get("asecillahosaldoc").toString()));
                userStatisticInfo.put("asecillalhos", Integer.valueOf(userOperationStatistic.get("asecillalhos").toString()));
                userStatisticInfo.put("adatealhos", Integer.valueOf(userOperationStatistic.get("adatealhos").toString()));
                userStatisticInfo.put("adatealdoc", Integer.valueOf(userOperationStatistic.get("adatealdoc").toString()));
                userStatisticInfo.put("adateahosaldoc", Integer.valueOf(userOperationStatistic.get("adateahosaldoc").toString()));
                userStatisticInfo.put("adocdetail", Integer.valueOf(userOperationStatistic.get("adocdetail").toString()));
                userStatisticInfo.put("adocadateserv", Integer.valueOf(userOperationStatistic.get("adocadateserv").toString()));
                userStatisticInfo.put("adocaservdetail", Integer.valueOf(userOperationStatistic.get("adocaservdetail").toString()));
                userStatisticInfo.put("apdoc", Integer.valueOf(userOperationStatistic.get("apdoc").toString()));
                userStatisticInfo.put("canceldoc", Integer.valueOf(userOperationStatistic.get("canceldoc").toString()));
                userStatisticInfo.put("appraisedoc", Integer.valueOf(userOperationStatistic.get("appraisedoc").toString()));
                userStatisticInfo.put("sharedoc", Integer.valueOf(userOperationStatistic.get("sharedoc").toString()));
                userStatisticInfo.put("servattention", Integer.valueOf(userOperationStatistic.get("servattention").toString()));
                userStatisticInfo.put("attentiondoc", Integer.valueOf(userOperationStatistic.get("attentiondoc").toString()));
                userStatisticInfo.put("checkservroute", Integer.valueOf(userOperationStatistic.get("checkservroute").toString()));
                userStatisticInfo.put("checkservstatus", Integer.valueOf(userOperationStatistic.get("checkservstatus").toString()));
                userStatisticInfo.put("checkdocaplocation", Integer.valueOf(userOperationStatistic.get("checkdocaplocation").toString()));
                userStatisticInfo.put("checkdoc7daylocation", Integer.valueOf(userOperationStatistic.get("checkdoc7daylocation").toString()));
                userStatisticInfo.put("checkdoc7dayapbylocation", Integer.valueOf(userOperationStatistic.get("checkdoc7dayapbylocation").toString()));
                userStatisticInfo.put("myfirstpage", Integer.valueOf(userOperationStatistic.get("myfirstpage").toString()));
                userStatisticInfo.put("myappointment", Integer.valueOf(userOperationStatistic.get("myappointment").toString()));
                userStatisticInfo.put("myapdetail", Integer.valueOf(userOperationStatistic.get("myapdetail").toString()));
                userStatisticInfo.put("myselfinfo", Integer.valueOf(userOperationStatistic.get("myselfinfo").toString()));
                userStatisticInfo.put("attdoc", Integer.valueOf(userOperationStatistic.get("attdoc").toString()));
                userLog.add(userStatisticInfo);
            }
        }

        int k = operationComprehensiveService.insertBatchUserStatistic(userLog);

    }

    //时间处理函数
    private Date dateToStrToDate(Map hashMap) {
        Date visitdate = (Date) hashMap.get("date");//就诊日期
        Date visittime = (Date) hashMap.get("begin_time");//就诊时间
        String dateToStr = DateToStr(visitdate).substring(0, 10);//转换为字符串
        String timeToStr = DateToStr(visittime).substring(11, 19);
        String nowDateStr = dateToStr + " " + timeToStr;//组合后的时间
        HashMap<String, Object> newhash = new HashMap<String, Object>();
        newhash.put("date", nowDateStr);
        return formatDate(newhash);
    }

    private Date formatDate(HashMap<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse((String) params.get("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static void main(String[] args) {
        ChangzhuoMessageUtil.sendMsg("13811315356", "123", ChangzhuoMessageUtil.RECEIVER_TYPE_DOCTOR);
    }

    //每天晚上3：00更新sys_statistics_title表
    //@Scheduled(cron = "0 0 3 * * ?")
    public void insertStatisticsTitle() {
        baseDataService.insertStatisticsTitle();
    }


    //没周的晚八点给医生发送下周的预约详情信息
    public void sendMessageToDocEveryWeek() {
        List<HashMap<String, Object>> orderList = scheduleTaskService.getUserOrderNextWeek();
        Map tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");
        Map<String, String> messageMap = new HashMap<String, String>();
        Map<String, String> wechatMap = new HashMap<String, String>();
        for (HashMap<String, Object> map : orderList) {
            String week = DateUtils.getWeekOfDate((Date) map.get("visiteDate"));
            String beginTime = (String) map.get("beginTime");
            String doctorName = (String) map.get("doctorName");
            String phone = (String) map.get("phone");
            String openid = (String) map.get("openid");
            String msgContetn = messageMap.get(doctorName + ":" + phone);
            messageMap.put(doctorName + ":" + phone, StringUtils.isNotNull(msgContetn) ? (msgContetn + week + "、") : (week + "、"));
            String wechatContetn = wechatMap.get(openid);
            if (StringUtils.isNotNull(openid)) {
                wechatMap.put(openid, StringUtils.isNotNull(wechatContetn) ? (wechatContetn + week + "-" + beginTime + "、") : (week + "-" + beginTime + "、"));
            }
        }

        for (Map.Entry<String, String> entry : messageMap.entrySet()) {
            String content = "尊敬的" + entry.getKey().substring(0, entry.getKey().indexOf(":")) + "医生，您下周" + entry.getValue().substring(0, entry.getValue().length() - 1) + "有出诊时刻安排。可登录微信公众号宝大夫专家版查看详细安排。如有变化，请联系客服：400-623-7120。";
            DoctorMsgTemplate.doctorAppointmentNestWeek2Sms(entry.getKey().substring(entry.getKey().indexOf(":"), entry.getKey().length()), entry.getKey().substring(entry.getKey().indexOf(":")), entry.getValue());
            System.out.print(content);
        }
    }

    //慢病管理定时发送消息
    //2016年1月14日
    //张博


    //每天7点 综合提醒
    //@Scheduled(cron = "0 0 7 * * ?")
    public void everyMorningSendMessage() {
        planMessageService.everyMorningSendSMS();
        planMessageService.TimingSendWechatMessageByPunchTicket();
    }

    //每一分钟遍历一次发送慢病管理消息
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void everyMinuteSendWechatMessage() {
        planMessageService.TimingSendWechatMessage();
    }

    //每一分钟遍历一次发送慢病管理消息
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void sendMessageForCustomerReturn() {
        customerService.SendCustomerReturn();
    }

    //营养管理消息发送 sunxiao
    //@Scheduled(cron = "0 0 7:00,12:30,19:00 * * ?")
    public void nutritionManagementSendWechatMessage() {
        planMessageService.nutritionManagementSendWechatMessage();
    }

    //更新保险订单
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void insuranceUpdate() {
        System.out.print("进入定时器：");
        insuranceService.updateInsuranceRegisterServiceByState();
    }

    //建立患者与医生之间的通讯
    public synchronized void getConnection4doctorAndPatient() {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        //再建立通讯的五分钟前发消息给用户
        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");

        Date date = new Date();
        date.setTime(date.getTime() + 5 * 60 * 1000);
        String dateStr = DateUtils.DateToStr(date, "datetime");
        List<HashMap<String, Object>> orderMsgList = consultPhoneOrderService.getOrderPhoneConsultListByTime("1", date);
        for (HashMap<String, Object> map : orderMsgList) {
            List<Map> messageMap = messageService.consultPhoneMsgRemind((Integer) map.get("id") + "");
            if (null == messageMap || messageMap.size() == 0) {
                String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String) map.get("date"), "yyyy/MM/dd"));
                String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl()  + "/titan/phoneConsult#/orderDetail" + (String) map.get("doctorId") + "," + (Integer) map.get("id") + ",phone";
                PatientMsgTemplate.consultPhoneWaring2Wechat((String) map.get("doctorName"), (String) map.get("date"), week, (String) map.get("beginTime"), (String) map.get("endTime"), (String) map.get("userPhone"), (String) map.get("orderNo"), (String) map.get("openid"), token, url);
                PatientMsgTemplate.consultPhoneWaring2Msg((String) map.get("babyName"), (String) map.get("doctorName"), (String) map.get("date"), week, (String) map.get("beginTime"), (String) map.get("userPhone"), (String) map.get("orderNo"));
                insertMonitor((Integer) map.get("id") + "", "2", "7");
            }
        }

//建立通讯
        List<HashMap<String, Object>> consultOrderList = consultPhoneOrderService.getOrderPhoneConsultListByTime("1", new Date());
        for (HashMap map : consultOrderList) {
            String doctorPhone = (String) map.get("doctorPhone");
            String doctorName = (String) map.get("doctorName");
            String babyName = (String) map.get("babyName");
            String userPhone = (String) map.get("userPhone");
            Integer orderId = (Integer) map.get("id");
            List<ConsultPhoneRecordVo> list = consultPhoneService.getConsultRecordInfo(orderId + "", "CallAuth");
            if (list.size() < 1) {

                Integer conversationLength = (Integer) map.get("conversationLength") * 60;
                HashMap<String, Object> result = CCPRestSDK.callback(userPhone, doctorPhone,
                        "01057115120", "01057115120", null,
                        "true", null, orderId + "",
                        conversationLength + "", null, "0",
                        "1", "60", null);
                System.out.println("发起电话咨询:" + result);
                if ("000000".equals((String) result.get("statusCode"))) {
                    HashMap<String, Object> dataMap = (HashMap) result.get("data");
                    HashMap<String, Object> callBackMap = (HashMap) dataMap.get("CallBack");
                    String callSid = (String) callBackMap.get("callSid");

                    System.out.println(result);
                    ConsultPhoneRegisterServiceVo vo = new ConsultPhoneRegisterServiceVo();
                    vo.setId(orderId);
                    vo.setUpdateTime(new Date());
                    vo.setCallSid(callSid);
                    consultPhonePatientService.updateOrderInfoBySelect(vo);
                }
            }
        }

        //将钱退还给用户
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> returnPayList = consultPhoneOrderService.getReturnPayConsultList();
        for (HashMap<String, Object> map : returnPayList) {
            accountService.updateAccount(0F, (Integer) map.get("id") + "", response, false, (String) map.get("userId"), "电话咨询超时取消退款");
            Map<String, Object> consultOrder = consultPhonePatientService.getPatientRegisterInfo((Integer) map.get("id"));
            String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/phoneConsult#/orderDetail" + (String) consultOrder.get("doctorId") + "," + (Integer) consultOrder.get("orderId") + ",phone";
            PatientMsgTemplate.returnPayPhoneRefund2Msg((String) consultOrder.get("babyName"), (Float) consultOrder.get("price") + "", (String) consultOrder.get("phone"));
            String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String) consultOrder.get("date"), "yyyy/MM/dd"));
            String dateTime = (String) consultOrder.get("date") + " " + week + " " + (String) consultOrder.get("beginTime");
            PatientMsgTemplate.returnPayPhoneRefund2Wechat((String) consultOrder.get("babyName"), (String) consultOrder.get("doctorName"), dateTime, (String) consultOrder.get("phone"), (String) consultOrder.get("orderNo"), (Float) consultOrder.get("price") + "", (String) consultOrder.get("openid"), token, url);
//          LogUtils.saveLog(Servlets.getRequest(), "00000108", "电话咨询-退费" + map);//用户发起微信支付
        }

        //将半小时钱的未支付的订单释放
        consultPhonePatientService.CancelAppointNoPay();
    }

    /**
     * 删除mongo中的,redis中的,内存中的consultSession
     */
    public void consultManagement4Session() {
        //获取用户与平台最后交流时间
        Query query = new Query(where("status").is("ongoing"));
        List<ConsultSessionStatusVo> consultSessionStatusVos = consultRecordService.querySessionStatusList(query);
        if (consultSessionStatusVos != null && consultSessionStatusVos.size() > 0) {
            for (ConsultSessionStatusVo consultSessionStatusVo : consultSessionStatusVos) {
                if (consultSessionStatusVo != null && consultSessionStatusVo.getLastMessageTime() != null) {
                    if (DateUtils.pastMinutes(consultSessionStatusVo.getLastMessageTime()) > 120L) {
                        if (consultSessionStatusVo != null && StringUtils.isNotNull(consultSessionStatusVo.getSessionId()) && consultSessionStatusVo.getSessionId() != null) {
                            //根据sessionId查询consult_conversation_forward_records表，状态为waiting不执行
                            ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
                            consultSessionForwardRecordsVo.setConversationId(Long.parseLong(consultSessionStatusVo.getSessionId()));
                            consultSessionForwardRecordsVo.setStatus("waiting");
                            List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
                            if (consultSessionForwardRecordsVos.size() == 0) {
                                consultSessionService.clearSession(consultSessionStatusVo.getSessionId(),
                                        consultSessionStatusVo.getUserId());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 扫描转接表转接状态超过4分钟，将状态置为退回状态 123
     */
    public void modifyConsultConversationForwardRecords() {
        ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
        consultSessionForwardRecordsVo.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING);
        List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
        for (ConsultSessionForwardRecordsVo forwardRecordsVo : consultSessionForwardRecordsVos) {
            if (DateUtils.pastMinutes(forwardRecordsVo.getCreateTime()) > 4L) {
                ConsultSessionForwardRecordsVo recordsVo = new ConsultSessionForwardRecordsVo();
                recordsVo.setId(forwardRecordsVo.getId());
                recordsVo.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_REJECT);
                consultSessionForwardRecordsService.updateRejectedTransfer(recordsVo);
            }
        }
        //监控distributorList
        List<String> distributorList = ConsultSessionManager.INSTANCE.distributorsList;
        User user = new User();
        user.setUserType("distributor");
        List<User> users = systemService.findUserByUserType(user);
        if (null != distributorList && null != users && distributorList.size() > 0 && users.size() > 0 || users.size() != distributorList.size()) {
            ConsultSessionManager.INSTANCE.distributorsList = new ArrayList<String>();
            for (User u : users) {
                ConsultSessionManager.INSTANCE.distributorsList.add(u.getId());
            }
        }

    }


    /**
     * 每天晚上1点，将前一天的咨询数据备份到数据库中
     */
    public void consultRecordMongoTransMySql() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date newDate = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date oldDate = calendar.getTime();

        Query query = new Query().addCriteria(Criteria.where("createDate").gte(oldDate).andOperator(Criteria.where("createDate").lte(newDate)));
        List<ConsultRecordVo> consultRecordVoList = new ArrayList<ConsultRecordVo>();
        List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.getCurrentUserHistoryRecord(query);
        Iterator<ConsultRecordMongoVo> iterator = consultRecordMongoVos.iterator();
        while (iterator.hasNext()) {
            ConsultRecordMongoVo consultRecordMongoVo = iterator.next();
            ConsultRecordVo consultRecordVo = new ConsultRecordVo();
            consultRecordVo.setId(IdGen.uuid());
            consultRecordVo.setSessionId(consultRecordMongoVo.getSessionId());
            consultRecordVo.setType(consultRecordMongoVo.getType());
            consultRecordVo.setUserId(consultRecordMongoVo.getUserId());
            consultRecordVo.setCreateDate(consultRecordMongoVo.getCreateDate());
            consultRecordVo.setDoctorName(consultRecordMongoVo.getDoctorName());
            consultRecordVo.setSenderName(consultRecordMongoVo.getSenderName());
            consultRecordVo.setMessage(EmojiFilter.coverEmoji(consultRecordMongoVo.getMessage()));
            consultRecordVo.setCsuserId(consultRecordMongoVo.getCsUserId());
            consultRecordVo.setSenderId(consultRecordMongoVo.getSenderId());
            consultRecordVoList.add(consultRecordVo);
        }
        consultRecordService.insertConsultRecordBatch(consultRecordVoList);
        //conuslt_session恢复数据
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 4);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.add(Calendar.DATE, -2);
//        Date newDate = calendar.getTime();
//        calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.add(Calendar.DATE, -10);
//        Date oldDate = calendar.getTime();
//
//        Query query = new Query().addCriteria(Criteria.where("lastMessageTime").gte(oldDate).andOperator(Criteria.where("lastMessageTime").lte(newDate)));
//        List<ConsultSession> consultSessions = new ArrayList<ConsultSession>();
//        List<ConsultSessionStatusVo> consultRecordVoList = consultRecordService.getConsultSessionStatusVo(query);
//        Iterator<ConsultSessionStatusVo> iterator = consultRecordVoList.iterator();
//        while (iterator.hasNext()) {
//            ConsultSessionStatusVo consultSessionStatusVo = iterator.next();
//            ConsultSession consultSession = new ConsultSession();
//            consultSession.setId(Integer.parseInt(consultSessionStatusVo.getSessionId()));
//            consultSession.setCreateTime(new Date(consultSessionStatusVo.getLastMessageTime().getTime() - 2 * 60 * 60 * 1000));
//            consultSession.setUserId(consultSessionStatusVo.getUserId());
//            consultSession.setStatus(consultSessionStatusVo.getStatus());
//            consultSession.setSource(consultSessionStatusVo.getSource());
//            consultSession.setCsUserId(consultSessionStatusVo.getCsUserId().split(" ")[0]);
//            consultSessions.add(consultSession);
//        }
//        consultSessionService.insertConsultSessionBatch(consultSessions);
    }


    public void consultManagementDayTask() {
        //删除会话排名中的临时数据
        consultRecordService.removeConsultRankRecord(new Query());
    }

    void test() {
        List<Object> objectList = sessionRedisCache.getSessionIdByKey();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        for (Object o : objectList) {
            Integer sessionId = (Integer) o;
            ConsultSession consultSession = consultSessionService.selectByPrimaryKey(sessionId);
            if (consultSession != null && consultSession.getCreateTime().getTime() < (calendar.getTimeInMillis())) {
                sessionRedisCache.removeConsultSessionBySessionId(consultSession.getId());
                sessionRedisCache.removeUserIdSessionIdPair(consultSession.getUserId());
            }
        }

    }

    public void testMappingTask() {
        //删除会话排名中的临时数据
        System.out.println("userChannelMapping的大小为：" + ConsultSessionManager.INSTANCE.userChannelMapping.size());
        Iterator iterator = ConsultSessionManager.INSTANCE.userChannelMapping.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            System.out.println("key==" + key + "|||" + "value==" + ConsultSessionManager.INSTANCE.userChannelMapping.get(key));
        }
    }

    //插入监听器
    private void insertMonitor(String register_no, String type, String status) {
        HashMap<String, Object> monitorMap = new HashMap<String, Object>();
        monitorMap.put("id", IdGen.uuid());
        monitorMap.put("patient_register_service_id", register_no);
        monitorMap.put("status", status);
        monitorMap.put("types", type);
        messageService.insertMonitorConsultPhone(monitorMap);
    }

    //电话咨询运维定时拨打电话 sunxiao
    public void timingDial() {
        Map param = new HashMap();
        param.put("state", 1);
        param.put("dialDate", new Date());
        List<ConsultPhoneManuallyConnectVo> list = consultPhonePatientService.getConsultPhoneTimingDialByInfo(param);
        for (ConsultPhoneManuallyConnectVo vo : list) {
            String doctorPhone = vo.getDoctorPhone();
            String userPhone = vo.getUserPhone();
            Integer orderId = vo.getOrderId();
            long conversationLength = vo.getSurplusTime() / 1000;
            HashMap<String, Object> result = CCPRestSDK.callback(userPhone, doctorPhone,
                    "01057115120", "01057115120", null,
                    "true", null, orderId + "",
                    conversationLength + "", null, "0",
                    "1", "60", null);

            if ("000000".equals((String) result.get("statusCode"))) {
                HashMap<String, Object> dataMap = (HashMap) result.get("data");
                HashMap<String, Object> callBackMap = (HashMap) dataMap.get("CallBack");
                String callSid = (String) callBackMap.get("callSid");

                System.out.println(result);
                ConsultPhoneRegisterServiceVo cpvo = new ConsultPhoneRegisterServiceVo();
                cpvo.setId(orderId);
                cpvo.setUpdateTime(new Date());
                cpvo.setCallSid(callSid);
                consultPhonePatientService.updateOrderInfoBySelect(cpvo);

                ConsultPhoneManuallyConnectVo mvo = new ConsultPhoneManuallyConnectVo();
                mvo.setId(vo.getId());
                mvo.setState("2");
                consultPhonePatientService.updateConsultPhoneTimingDialInfo(mvo);
            }
        }
    }


    /*
     * 电话咨询
     * @author chenxiaoqiong
     */

    /**
     * 晚8点发送明天的电话咨询信息
     *
     * @Scheduled(cron = "0 0 20 * * ?")
     */
    public void sendMsgToDocAtNightPhoneConsult() {
        System.out.println("package.controller scheduled test -->sendMsgToDocAtNight_PhoneConsult");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");

        Date tomorrow = DateUtils.addDays(new Date(), 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(tomorrow);
        List<Integer> monitorList = new ArrayList<Integer>();
        List<HashMap<String, Object>> doctorOrderInfoList = getDoctorOrderInfoList(date, "night", monitorList);

        for (HashMap<String, Object> orderMap : doctorOrderInfoList) {
            String doctorName = (String) orderMap.get("doctorName");
            int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
            String nameList = "";
            for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                String babyName = (String) map.get("babyName");
                String begin_time = (String) map.get("begin_time");
                String end_time = (String) map.get("end_time");
                nameList = nameList + babyName + "(" + begin_time + "-" + end_time + ");";
            }
            nameList = nameList.substring(0, nameList.length() - 1);

            DoctorMsgTemplate.doctorPhoneConsultRemindAtNight2Sms((String) orderMap.get("doctorPhone"), doctorName,
                    String.valueOf(num), nameList);

            //如果医生有微信ID，则推送微信消息
            String openid = (String) orderMap.get("openid");
            if (StringUtils.isNotNull(openid)) {
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年mm月dd日");
                String url = sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/phoneConsultDoctor#/phoneConsultFirst/" + date;
                DoctorMsgTemplate.doctorPhoneConsultRemindAtNight2Wechat(simpleDateFormat1.format(tomorrow), String.valueOf(num), nameList, token,
                        url, openid);
            }
        }
        for (Integer monitor : monitorList) {
            //标记已发送预约成功短信
            insertMonitor(monitor + "", "3", "1");
        }
    }

    /**
     * 早7点，发送今天的咨询信息
     *
     * @Scheduled(cron = "0 0 7 * * ?")
     */
    public void sendMsgToDocAtMorningPhoneConsult() {
        System.out.println("package.controller scheduled test -->sendMsgToDocAtMorning_PhoneConsult");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map tokenMap = systemService.getDoctorWechatParameter();
        String token = (String) tokenMap.get("token");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        List<Integer> monitorList = new ArrayList<Integer>();
        List<HashMap<String, Object>> doctorOrderInfoList = getDoctorOrderInfoList(date, "morning", monitorList);

        for (HashMap<String, Object> orderMap : doctorOrderInfoList) {
            String doctorName = (String) orderMap.get("doctorName");
            int num = ((List<HashMap<String, Object>>) orderMap.get("patientList")).size();
            String nameList = "";
            for (Map map : (List<HashMap<String, Object>>) orderMap.get("patientList")) {
                String babyName = (String) map.get("babyName");
                String begin_time = (String) map.get("begin_time");
                String end_time = (String) map.get("end_time");
                nameList = nameList + babyName + "(" + begin_time + "-" + end_time + ");";
            }
            nameList = nameList.substring(0, nameList.length() - 1);

            DoctorMsgTemplate.doctorPhoneConsultRemindAtMoning2Sms((String) orderMap.get("doctorPhone"), doctorName,
                    String.valueOf(num), nameList);

            //如果医生有微信ID，则推送微信消息
            String openid = (String) orderMap.get("openid");
            if (StringUtils.isNotNull(openid)) {
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年mm月dd日");
                String url = sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/phoneConsultDoctor#/phoneConsultFirst/" + date;
                DoctorMsgTemplate.doctorPhoneConsultRemindAtMoning2Wechat(simpleDateFormat1.format(new Date()), String.valueOf(num), nameList,
                        token, url, openid);
            }

        }

        for (Integer monitor : monitorList) {
            //标记已发送预约成功短信
            insertMonitor(monitor + "", "3", "1");
        }
    }

    /**
     * 根据日期date获取当天的电话咨询订单和医生
     *
     * @param date
     * @return
     */
    public List<HashMap<String, Object>> getDoctorOrderInfoList(String date, String time, List<Integer> monitorList) {
        List<HashMap<String, Object>> doctorOrderInfoList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("date", date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if ("morning".equals(time)) {
            try {
                Date d = format.parse(date);
                d = DateUtils.addDays(d, -1);
                searchMap.put("startTime", format.format(d) + " 20:00:00");//昨晚8点
                searchMap.put("endTime", date + " 07:00:00");//今天7点
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //明天预约的订单信息，医生信息
        List<HashMap<String, Object>> doctorList = scheduleTaskService.getDoctorInfoByDate(searchMap);
        List<HashMap<String, Object>> list = scheduleTaskService.getOrderInfoByDate(searchMap);
        List<String> classList = new ArrayList<String>();

        for (int i = 0; i < doctorList.size(); i++) {
            HashMap<String, Object> listMap = new HashMap<String, Object>();
            HashMap<String, Object> mapi = doctorList.get(i);
            listMap.put("doctorName", mapi.get("doctorName"));
            listMap.put("hospitalName", mapi.get("hospitalName"));
            listMap.put("doctorPhone", mapi.get("phone"));
            listMap.put("openid", mapi.get("openid"));
            List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
            for (int j = 0; j < list.size(); j++) {
                HashMap<String, Object> newMap = new HashMap<String, Object>();
                HashMap<String, Object> mapj = list.get(j);
                if ((mapi.get("doctorName").equals(mapj.get("doctorName"))) &&
                        (mapi.get("phone").equals(mapj.get("phone"))) &&
                        (mapi.get("hospitalName").equals(mapj.get("hospitalName")))) {
                    newMap.put("babyName", mapj.get("babyName"));
                    newMap.put("begin_time", mapj.get("begin_time"));
                    newMap.put("end_time", mapj.get("end_time"));
                    newList.add(newMap);
                    classList.add((String) mapj.get("id"));
                }
                listMap.put("patientList", newList);
                monitorList.add((Integer) mapj.get("patient_register_service_id"));
            }
            doctorOrderInfoList.add(listMap);
        }

        return doctorOrderInfoList;
    }

    //预约咨询成功5min后
    public void sendMsgToDoc5minAfterSuccess() {
        System.out.println(new Date() + " package.controller scheduled test --> sendMsgToDoc5minAfterSuccess_PhoneConsult");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH");
        int time = Integer.parseInt(format.format(date));
        if (20 > time && time > 7) {
            List<HashMap<String, Object>> doctorMsg = scheduleTaskService.getOrderInfoToDocSuccess5minBefore();
            for (HashMap<String, Object> map : doctorMsg) {
                String doctorName = (String) map.get("doctorName");
                String babyName = (String) map.get("babyName");
                String doctorPhone = (String) map.get("phone");
                String visitDate = DateUtils.DateToStr((Date) map.get("date"), "date");
                String beginTime = DateUtils.DateToStr((Date) map.get("beginTime"), "time");
                String week = DateUtils.getWeekOfDate((Date) map.get("date"));
                //短信
                DoctorMsgTemplate.doctorPhoneConsultRemindAt5minLater2Sms(doctorName, babyName, doctorPhone, visitDate, week, beginTime);

                String openid = (String) map.get("openid");
                if (StringUtils.isNotNull(openid)) {
                    //微信推送
                    Map tokenMap = systemService.getDoctorWechatParameter();
                    String token = (String) tokenMap.get("token");
                    Integer sysPhoneConsultId = (Integer) map.get("sys_phoneConsult_service_id");
                    String doctorId = (String) map.get("doctorId");
                    String url = sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/phoneConsultDoctor#/phoneConsultDetails/" + sysPhoneConsultId + "," + doctorId;
                    DoctorMsgTemplate.doctorPhoneConsultRemindAt5minLater2Wechat(babyName, visitDate, week,
                            beginTime, token, url, (String) map.get("openid"));
                }
                //标记已发送预约成功短信
                insertMonitor((Integer) map.get("patient_register_service_id") + "", "3", "1");
            }
        }
    }

    //接听前5min
    public void sendMsgToDoc5minBeforeConnect() {
        System.out.println(new Date() + " package.controller scheduled test --> sendMsgToDoc5minBeforeConnect_PhoneConsult");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        List<HashMap<String, Object>> doctorMsg = scheduleTaskService.getOrderInfoToDocConnect5minAfter();
        for (HashMap<String, Object> map : doctorMsg) {
            String doctorName = (String) map.get("doctorName");
            String babyName = (String) map.get("babyName");
            String doctorPhone = (String) map.get("phone");
            String userPhone = (String) map.get("userPhone");
            Integer sysPhoneConsultId = (Integer) map.get("sys_phoneConsult_service_id");
            String doctorId = (String) map.get("doctorId");
            String url = sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/phoneConsultDoctor#/phoneConsultDetails/" + sysPhoneConsultId + "," + doctorId;
            Map tokenMap = systemService.getDoctorWechatParameter();
            String token = (String) tokenMap.get("token");
            //短信
            DoctorMsgTemplate.doctorPhoneConsultRemindAt5minBefore2Sms(token, doctorName, babyName, doctorPhone, userPhone, url);

            String openid = (String) map.get("openid");
            if (StringUtils.isNotNull(openid)) {
                //微信推送

                Date date = (Date) map.get("date");
                Date beginTime = (Date) map.get("beginTime");
                Date endTime = (Date) map.get("endTime");

                SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd");
                SimpleDateFormat format1 = new SimpleDateFormat("HH:MM");
                String week = DateUtils.getWeekOfDate(DateUtils.StrToDate(format.format(date), "yyyy/MM/dd"));

                String time = format.format(date) + " " + week + " " + format1.format(beginTime) + "-" + format1.format(endTime);
                DoctorMsgTemplate.doctorPhoneConsultRemindAt5minBefore2Wechat(babyName, time, userPhone, (String) map.get("register_no"), token, url, openid);
            }
        }
    }

    public void umbrellaSendWechatMessage() {
        babyUmbrellaInfoService.umbrellaSendWechatMessage();
    }

    /***
     * 每two minutes分钟检查一次医生的在线状态，如果医生出现异常断网的情况，则废除此channel
     ***/
    public void checkDoctorChannelStatusTask() {
        ConsultSessionManager.INSTANCE.checkDoctorChannelStatus();
    }

    /***
     * every two minutes to check the h5 user connection status
     ***/
    public void checkH5UserChannelStatusTask() {
        ConsultSessionManager.INSTANCE.checkH5UserChannelStatus();
    }


    //每天的两点提醒只玩了一关的用户
    public void olympicShareRemind() {
        //微信推送
        Map tokenMap = systemService.getWechatParameter();
        String token = (String) tokenMap.get("token");
        List<String> remindUser = scheduleTaskService.getOrderInfoByDate();
        for (String openid : remindUser) {
            WechatMessageUtil.templateModel("邀请卡", " 电烤箱、面包机、儿童被……众多大奖还在等你，赶紧邀请好友一起闯关赢豪礼吧！", "待办事项: 邀请好友玩游戏赢大奖\n优先级：很高哦", "", "", "马上去赚大奖", token, "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37", openid, "tCQGoqfVSv_bCYVGUPbXzsJ2sxKzyoiDbKAKB1KO_Qg");
        }
    }

    //每个月赠送给用户四次咨询机会
    public void updateMonthTime() {
        consultSessionPropertyService.updateMonthTime();
    }

}
