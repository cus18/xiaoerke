package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.interaction.dao.PatientRegisterPraiseDao;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoThirdPartyService;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyRecordVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;
import com.cxqm.xiaoerke.modules.wechat.entity.*;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatPatientCoreService;
import com.cxqm.xiaoerke.modules.wechat.service.util.MessageUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional(readOnly = false)
public class WechatPatientCoreServiceImpl implements WechatPatientCoreService {

    @Autowired
    private WechatInfoDao wechatInfoDao;

    @Autowired
    private WechatAttentionDao wechatAttentionDao;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ConsultSessionService consultConversationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MongoDBService<WechatAttention> mongoDBService;

    @Autowired
    private MongoDBService<MongoLog> mongoLogService;

    @Autowired
    private MongoDBService<HealthRecordMsgVo> healthRecordMsgVoMongoDBService;

    @Autowired
    private MemberService memberService;

    private String mongoEnabled = Global.getConfig("mongo.enabled");

    @Autowired
    private PatientRegisterPraiseDao patientRegisterPraiseDao;

    @Autowired
    LoveMarketingService loveMarketingService;

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private BabyUmbrellaInfoThirdPartyService babyUmbrellaInfoThirdPartyService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private BabyCoinService babyCoinService;

    @Autowired
    private MongoDBService<SpecificChannelRuleVo> specificChannelRuleMongoDBService;

    @Autowired
    private MongoDBService<SpecificChannelInfoVo> specificChannelInfoMongoDBService;

    @Autowired
    private OperationPromotionService operationPromotionService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private TraceElementsServiceImpl traceElementsService;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    private Map<String, OperationPromotionVo> keywordMap;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    @Override
    public String processPatientRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("processPatientRequest===================================");
        String respMessage = "";
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        /** 解析xml数据 */
        ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(getXmlDataFromWechat(request));
        String msgType = xmlEntity.getMsgType();

        // xml请求解析
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
            // 事件类型
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String token = (String) userWechatParam.get("token");
            String eventType = xmlEntity.getEvent();
            if (eventType.equals(MessageUtil.SCAN)) {
                //已关注公众号的情况下扫描
                this.updateAttentionInfo(xmlEntity);
                //特定渠道优惠
                specificChanneldeal(xmlEntity, token);
                respMessage = processScanEvent(xmlEntity, "oldUser", request, response, sysPropertyVoWithBLOBsVo);
                //疫苗提醒
                babyVaccineRemind(xmlEntity, token, sysPropertyVoWithBLOBsVo);
                //微量元素检测
//                traceElementsDetection(xmlEntity, token);

            } else if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                //扫描关注公众号或者搜索关注公众号都在其中
                respMessage = processSubscribeEvent(xmlEntity, request, response, sysPropertyVoWithBLOBsVo);
                //疫苗提醒
                babyVaccineRemind(xmlEntity, token, sysPropertyVoWithBLOBsVo);

                //微量元素检测
                traceElementsDetection(xmlEntity, token);
            }
            // 取消订阅
            else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                processUnSubscribeEvent(xmlEntity, request);
            }
            // 自定义菜单点击事件
            else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                respMessage = processClickMenuEvent(xmlEntity, request, response);
            }
            // 结束咨询对话
            else if (eventType.equals(MessageUtil.KF_CLOSE)) {
                respMessage = processCloseEvent(xmlEntity, request, response);
            }
            //获取用户位置
            else if (eventType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                processGetLocationEvent(xmlEntity, request);
            }
        } else {
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String token = (String) userWechatParam.get("token");
            try {
                //关键字回复功能
                if (keywordRecovery(xmlEntity, token, OperationPromotionStatusVo.KEY_WORD)||nonRealTimeCheck(sysPropertyVoWithBLOBsVo.getWhitelist(),xmlEntity, token)) {
                    return "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String customerService = Global.getConfig("wechat.customservice");
            if ("false".equals(customerService)) {
                Runnable thread = new processConsultMessageThread(xmlEntity);
                threadExecutor.execute(thread);
                return "";
            } else if ("true".equals(customerService)) {
                respMessage = transferToCustomer(xmlEntity);
            }
        }
        return respMessage;
    }

    private void traceElementsDetection(ReceiveXmlEntity xmlEntity, String token) {
        String EventKey = xmlEntity.getEventKey();
        if (!StringUtils.isNotNull(EventKey)) return;
        String QRCode = EventKey.replace("qrscene_", "");
        if (QRCode.contains("PD_WLYS_")) {
            String openId = xmlEntity.getFromUserName();
//            查询当前用户是否已经生成标示,为用户再数据库创建相关信息,唯一标示2016100110500000(年月日时分加四位数字 不能重复)
            // 查询max的id值
            TraceElementsVo traceElementsVo = traceElementsService.selectByOpenid(openId);
            String userNo = DateUtils.formatDateToStr(new Date(), "yyyy MMdd");
            if (null == traceElementsVo) {
                traceElementsVo = new TraceElementsVo();
                traceElementsVo.setOpenid(openId);
                traceElementsVo.setUserNo(userNo);
                traceElementsVo.setCreateTime(new Date());
                traceElementsService.insertSelective(traceElementsVo);
//                traceElementsVo.setUserNo(userNo+" "+traceElementsVo.getId());
//                traceElementsService.updateByPrimaryKeySelective(traceElementsVo);
            }
//            推送相关文案
            WechatUtil.sendMsgToWechat(token, openId, "检测编码:" + DateUtils.formatDateToStr(traceElementsVo.getCreateTime(), "yyyy MMdd") + " " + String.format("%04d", traceElementsVo.getId()) + "\n\n此编码是检测您宝宝微量元素的唯一编码，请将编码标注在检测样本上，以便您能即时接收到检测结果！[爱心]");
        }
    }

    private void babyVaccineRemind(ReceiveXmlEntity xmlEntity, String token, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {

        String EventKey = xmlEntity.getEventKey();
        String QRCode = EventKey.replace("qrscene_", "");

        if (QRCode.indexOf("PD_YMTX") != -1) {
            String openId = xmlEntity.getFromUserName();
            VaccineBabyInfoVo vaccineBabyInfoVo = new VaccineBabyInfoVo();
            vaccineBabyInfoVo.setSysUserId(xmlEntity.getFromUserName());
            vaccineBabyInfoVo = vaccineService.selectByVaccineBabyInfoVo(vaccineBabyInfoVo);
            if (vaccineBabyInfoVo == null || StringUtils.isBlank(vaccineBabyInfoVo.getBabySeedNumber())) {
                String content = "欢迎加入宝大夫疫苗提醒功能\n" +
                        "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url="
                        + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=46," + QRCode + "'>>>点击开启提醒</a>";
                WechatUtil.sendMsgToWechat(token, openId, content);
            } else {
                HashMap<String, Object> searchMap = new HashMap<String, Object>();
                searchMap.put("QRCode", QRCode.split("_")[2]);
                searchMap.put("openId", openId);
                List<HashMap<String, Object>> resultList = vaccineService.getUserWillVaccination(searchMap);
                if (resultList != null && resultList.size() > 0) {
                    //保存疫苗接种记录
                    saveVaccineBabyRecord(openId, resultList);
                    StringBuffer stringBuffer = new StringBuffer();
                    String vaccinaName = "";
                    int count = 0;
                    for (Map map : resultList) {
                        if (map != null && StringUtils.isNotBlank(String.valueOf(map.get("willVaccineName")))) {
                            count++;
                            stringBuffer.append(map.get("willVaccineName"));
                            stringBuffer.append("、");
                            vaccinaName = stringBuffer.toString();

                            Calendar sendTime = Calendar.getInstance();
                            Calendar tempTime = Calendar.getInstance();
                            Date birthday = (Date) map.get("birthday");

                            Integer nextLastTimeInterval = Integer.valueOf(String.valueOf(map.get("nextLastTimeInterval")));
                            Integer allVaccineInterval = Integer.valueOf(ConstantUtil.ALL_VACCINE_INTERVAL.getVariable());
                            Integer nextVaccineMiniumAge = Integer.valueOf(String.valueOf(map.get("nextVaccineMiniumAge")));
                            //下次接种间隔>=30
                            if (nextLastTimeInterval >= allVaccineInterval)
                                tempTime.add(Calendar.DAY_OF_MONTH, nextLastTimeInterval);
                            else
                                tempTime.add(Calendar.DAY_OF_MONTH, allVaccineInterval);

                            double passDayByBirthday = DateUtils.getDistanceOfTwoDate(birthday, new Date(tempTime.getTimeInMillis()));

                            //下次接种疫苗的最小接种月龄>=宝宝到了下次接种间隔时的月龄,按最小接种月龄计算
                            if (nextVaccineMiniumAge > passDayByBirthday)
                                sendTime.setTimeInMillis(birthday.getTime() + nextVaccineMiniumAge * 24 * 3600 * 1000);
                            else
                                sendTime.setTimeInMillis(birthday.getTime() + Math.round(passDayByBirthday * 24 * 3600 * 1000));

                            Integer vaccineId = Integer.valueOf(String.valueOf(map.get("nextVaccineId")));
                            Date inoculationTime = sendTime.getTime();
                            //保存提前七天提醒消息
                            Date remindContentDate = sendTime.getTime();
                            sendTime.add(Calendar.DAY_OF_MONTH, -7);
                            StringBuffer sendContent7 = new StringBuffer();
                            sendContent7.append("宝宝该打疫苗了！！");
                            sendContent7.append("||");
                            sendContent7.append("宝宝在0000-00-00后需要接种" +map.get("willVaccineName")+"疫苗");
                            sendContent7.append("||");
                            sendContent7.append("很高哦！");
                            sendContent7.append("||");
                            sendContent7.append("接种疫苗可以帮助宝宝抵抗疾病，爸爸妈妈千万不要大意哦");
                            saveVaccineMessage(vaccineId, openId, sendContent7.toString(), sendTime.getTime(), "7", inoculationTime);

                            //保存提前一天提醒消息
                            sendTime.add(Calendar.DAY_OF_MONTH, 6);
                            StringBuffer sendContent1 = new StringBuffer();
                            sendContent1.append("宝宝该打疫苗了！！");
                            sendContent1.append("||");
                            sendContent1.append("明天宝宝需要接种" + map.get("willVaccineName") + "疫苗");
                            sendContent1.append("||");
                            sendContent1.append("很高哦！");
                            sendContent1.append("||");
                            sendContent1.append("接种疫苗可以帮助宝宝抵抗疾病，爸爸妈妈千万不要大意哦");
                            saveVaccineMessage(vaccineId, openId, sendContent1.toString(), sendTime.getTime(), "1", inoculationTime);

                            //跟当前码有关的提醒消息失效
                            VaccineSendMessageVo vaccineSendMessageVo = new VaccineSendMessageVo();
                            vaccineSendMessageVo.setVaccineId(Integer.valueOf(String.valueOf(map.get("vaccineInfoId"))));
                            List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);
                            for (VaccineSendMessageVo vo : vaccineSendMessageVos) {
                                vo.setId(vo.getId());
                                vo.setValidFlag(ConstantUtil.VACCINEINVALID.getVariable());
                                vaccineService.updateByPrimaryKeyWithBLOBs(vo);
                            }

                        }
                    }
                    babyVaccineExceptionalCase();

                    if (count >= 2 && count != 0) {
                        vaccinaName = vaccinaName.substring(0, vaccinaName.lastIndexOf("、")) + "和" + vaccinaName.substring(vaccinaName.lastIndexOf("、") + 1, vaccinaName.length());
                    }
                    WechatUtil.sendMsgToWechat(token, openId, "你的宝宝即将接种" + vaccinaName.toString().substring(0, vaccinaName.length() - 1));
                }
            }
        }
    }

    private void babyVaccineExceptionalCase() {
        //如果有下一次疫苗提醒消息的发送时间小于（当前时间+30天），说明用户没有按照约定的时间去接种疫苗，例如出现了这种情况，宝宝感冒了，延迟几天去接种疫苗,
        //那么之前生成的疫苗提醒就有时间误差（两次疫苗的接种时间>=30天，下次接种疫苗的时间有可能<30天了），在这里,需要重新调整用户的下一次疫苗提醒消息
        //调整规则如下：
        //1、下次提醒消息<=30天，但是下次提醒距离下下次提醒消息小于15天，那么下次提醒消息往后挪到和下下次提醒消息一起发送
        //2、下次提醒消息<=30天，但是下次提醒距离下下次提醒消息大于15天，那么下次提醒消息往后挪到距离现在的30天之后发
        VaccineSendMessageVo vaccineSendMessageVo = new VaccineSendMessageVo();
        Calendar calendar29 = Calendar.getInstance();
        calendar29.add(Calendar.DAY_OF_MONTH, 29);

        vaccineSendMessageVo.setStartSearchDate(new Date());
        vaccineSendMessageVo.setEndSearchDate(calendar29.getTime());
        vaccineSendMessageVo.setInoculationTime(new Date());
        vaccineSendMessageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());
        List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);
        //存在特殊情况
        if (vaccineSendMessageVos != null && vaccineSendMessageVos.size() > 0) {
            for (VaccineSendMessageVo vo : vaccineSendMessageVos) {
                Calendar calendar30 = Calendar.getInstance();
                calendar30.add(Calendar.DAY_OF_MONTH, 30);
                Calendar calendar45 = Calendar.getInstance();
                calendar45.add(Calendar.DAY_OF_MONTH, 45);
                VaccineSendMessageVo messageVo = new VaccineSendMessageVo();
                vaccineSendMessageVo.setStartSearchDate(calendar30.getTime());
                vaccineSendMessageVo.setEndSearchDate(calendar45.getTime());
                messageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());
                List<VaccineSendMessageVo> sendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);//时间升序排序

                if (sendMessageVos != null && sendMessageVos.size() > 0) {//下次提醒距离下下次提醒消息小于15天
                    VaccineSendMessageVo messageVo1 = sendMessageVos.get(0);
                    vo.setInoculationTime(messageVo1.getInoculationTime());
                    Calendar transCalendar = Calendar.getInstance();
                    transCalendar.setTime(messageVo1.getInoculationTime());
                    if (vo.getMsgType().equals("7")){
                        transCalendar.add(Calendar.DAY_OF_MONTH,-7);
                        vo.setSendTime(transCalendar.getTime());
                    }else{
                        transCalendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setSendTime(transCalendar.getTime());
                    }

                }else {//下次提醒距离下下次提醒消息大于15天,下次提醒消息往后挪到距离现在的30天之后发
                    Calendar transCalendar = Calendar.getInstance();
                    transCalendar.add(Calendar.DAY_OF_MONTH,30);
                    vo.setInoculationTime(transCalendar.getTime());
                    if (vo.getMsgType().equals("7")){
                        transCalendar.add(Calendar.DAY_OF_MONTH,-7);
                        vo.setSendTime(transCalendar.getTime());
                    }else{
                        transCalendar.add(Calendar.DAY_OF_MONTH,-1);
                        vo.setSendTime(transCalendar.getTime());
                    }
                }
                vaccineService.updateByPrimaryKeyWithBLOBs(vo);
            }
        }
    }

    private void saveVaccineBabyRecord(String openId, List<HashMap<String, Object>> resultList) {
        HashMap<String, Object> recordMap = resultList.get(0);
        VaccineBabyRecordVo vaccineBabyRecordVo = new VaccineBabyRecordVo();
        vaccineBabyRecordVo.setSysUserId(openId);
        vaccineBabyRecordVo.setVaccineInfoId(Integer.valueOf(String.valueOf(recordMap.get("vaccineInfoId"))));
        List<VaccineBabyRecordVo> vaccineBabyRecordVos = vaccineService.selectByVaccineBabyRecord(vaccineBabyRecordVo);
        if (vaccineBabyRecordVos == null || vaccineBabyRecordVos.size() == 0) {
            vaccineBabyRecordVo.setBabyName(String.valueOf(recordMap.get("babyName")));
            vaccineBabyRecordVo.setBabySeedNumber(String.valueOf(recordMap.get("babySeedNumber")));
            vaccineBabyRecordVo.setCreateBy(openId);
            vaccineBabyRecordVo.setCreateTime(new Date());
            vaccineBabyRecordVo.setVaccineInfoName(String.valueOf(recordMap.get("vaccineInfoName")));
            vaccineService.insertVaccineBabyRecord(vaccineBabyRecordVo);
        }
    }

    private void saveVaccineMessage(Integer nextVaccineId, String openId, String sendContent, Date sendTime, String msgType, Date inoculationTime) {
        VaccineSendMessageVo vaccineSendMessageVo = new VaccineSendMessageVo();
        vaccineSendMessageVo.setVaccineId(nextVaccineId);
        vaccineSendMessageVo.setSysUserId(openId);
        vaccineSendMessageVo.setMsgType(msgType);
        List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);
        if (vaccineSendMessageVos == null || vaccineSendMessageVos.size() == 0) {
            vaccineSendMessageVo.setContent(sendContent);
            vaccineSendMessageVo.setCreateBy(openId);
            vaccineSendMessageVo.setSendTime(sendTime);
            vaccineSendMessageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());
            vaccineSendMessageVo.setCreateTime(new Date());
            vaccineSendMessageVo.setMsgType(msgType);
            vaccineSendMessageVo.setInoculationTime(inoculationTime);
            vaccineService.insertVaccineSendMessage(vaccineSendMessageVo);
        }
    }

    public class processConsultMessageThread extends Thread {
        private ReceiveXmlEntity xmlEntity;

        public processConsultMessageThread(ReceiveXmlEntity xmlEntity) {
            this.xmlEntity = xmlEntity;
        }

        public void run() {
            try {
                System.out.println(xmlEntity.getContent());
                SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
                if (xmlEntity.getMsgType().equals("text")) {
                    this.sendPost(sysPropertyVoWithBLOBsVo.getAngelWebUrl() + "angel/consult/wechat/conversation",
                            "openId=" + xmlEntity.getFromUserName() +
                                    "&messageType=" + xmlEntity.getMsgType() +
                                    "&messageContent=" + URLEncoder.encode(xmlEntity.getContent(), "UTF-8"));
                } else {
                    this.sendPost(sysPropertyVoWithBLOBsVo.getAngelWebUrl() + "angel/consult/wechat/conversation",
                            "openId=" + xmlEntity.getFromUserName() +
                                    "&messageType=" + xmlEntity.getMsgType() +
                                    "&mediaId=" + xmlEntity.getMediaId());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url   发送请求的 URL
         * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return 所代表远程资源的响应结果
         */
        public String sendPost(String url, String param) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送 POST 请求出现异常！" + e);
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return result;
        }
    }

    private String getXmlDataFromWechat(HttpServletRequest request) {
        /** 读取接收到的xml消息 */
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String processScanEvent(ReceiveXmlEntity xmlEntity, String userType, HttpServletRequest request, HttpServletResponse response, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {
        String EventKey = xmlEntity.getEventKey();
        System.out.println(EventKey + "EventKey=========================================");
        Article article = new Article();
        List<Article> articleList = new ArrayList<Article>();
        NewsMessage newsMessage = new NewsMessage();
        newsMessage.setToUserName(xmlEntity.getFromUserName());
        newsMessage.setFromUserName(xmlEntity.getToUserName());
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setFuncFlag(0);
//        if (EventKey.indexOf("baoxian_000001") > -1 && xmlEntity.getEvent().equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//            TextMessage textMessage = new TextMessage();
//            textMessage.setToUserName(xmlEntity.getFromUserName());
//            textMessage.setFromUserName(xmlEntity.getToUserName());
//            textMessage.setCreateTime(new Date().getTime());
//            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//            textMessage.setFuncFlag(0);
//            textMessage.setContent("尊敬的诺安康VIP客户，您好！欢迎加入宝大夫，让您从此育儿不用愁！\n\n【咨询大夫】直接咨询北京三甲医院儿科专家，一分钟内极速回复！\n\n【妈妈活动】添加宝大夫客服微信：bdfdxb，加入宝大夫家长群，与众多宝爸宝妈一起交流分享，参与更多好玩的活动！\n\n如需人工协助，请您拨打：400-623-7120。\n");
//            return MessageUtil.textMessageToXml(textMessage);
//
//        } else if (EventKey.indexOf("YY0016") > -1) {
//            Map parameter = systemService.getWechatParameter();
//            String token = (String) parameter.get("token");
//            String msg = "欢迎来到宝大夫，请按照下方操作领走12本超值电子书籍 \n\n1·将海报图片（见下方）发送到朋友圈，集齐10个赞\n\n2·截图发给宝大夫客服（客服二维码见下方图片）\n\n以上操作完成后请静待客服把12本亲子书籍送到您手里。关注宝大夫后期会有更多福利送不停。";
//            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), msg);
//            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6fe8844t0Ewt49JlUhW7ukzDQ", 1);
//            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6fe-amTDnt4DhnNviDaW7kTNc", 1);
//        } else if (EventKey.indexOf("YY0018") > -1) {
//            Map parameter = systemService.getWechatParameter();
//            String token = (String) parameter.get("token");
//            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), "欢迎来到宝大夫，请按照下方操作领走精品食谱，儿歌，绘本，动画片等资源。\n" +
//                    "\n" +
//                    "1·将海报图片（见下方）发送到朋友圈，集齐10个赞。\n" +
//                    " 或  随机分享到母婴相关的5个微信群即可；\n" +
//                    "\n" +
//                    "2·截图发给宝大夫客服（客服二维码见下方图片）\n" +
//                    "\n" +
//                    "以上操作完成后请静待客服把众多资源送到您手里。关注宝大夫后期会有更多福利送不停哦。");
//            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6feyoI5F7e9v8waWTGfb56bcg", 1);
//            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6fe41AjIlPrqLyUz5-S99mCls", 1);
//        }

        if (EventKey.indexOf("doc") > -1) {
            Map<String, Object> map = wechatInfoDao.getDoctorInfo(EventKey.replace("doc", ""));
            article.setTitle("您已经成功关注" + map.get("hospitalName") + map.get("name") + "医生，点击即可预约");
            article.setDescription("");
            article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/attentionDoc.jpg");
            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/doctorAppointment/" + map.get("id") + ",,,,,doctorShare,,");
            articleList.add(article);
        }
//        } else if (EventKey.indexOf("267") > -1) {
//            article.setTitle("恭喜您,通过‘糖盒儿(tanghe2)’关注宝大夫,不仅可以随时免费咨询儿科专家,还可获赠一次预约名医的机会。");
//            article.setDescription("");
//            articleList.add(article);
//        } else if (EventKey.indexOf("263") > -1) {
//            article.setTitle("【郑玉巧育儿经】--宝大夫");
//            article.setDescription("智能匹配月龄，获取针对一对一育儿指导，建立宝宝专属健康档案，一路呵护，茁壮成长！");
//            article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/Follow.jpg");
//            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/knowledgeIndex");
//            articleList.add(article);
//        } else if (EventKey.indexOf("521") > -1) {
//            article.setTitle("宝大夫关爱儿童公益活动");
//            article.setDescription("赶快去邀请更多的人传递爱吧！");
//            article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/MessageImage/10.pic_hd.jpg");
//            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/knowledgeIndex");
//            articleList.add(article);
//            //更新二维码拥有者善款
//            loveMarketingService.updateInviteMan(EventKey, xmlEntity.getFromUserName());
//        }
         if (EventKey.indexOf("month") > -1) {
            if (userType.equals("newUser")) {
                Boolean value = activityService.judgeActivityValidity(EventKey.replace("qrscene_", ""));
                if (value == false) {
                    //推送赠送月会员URL消息
                    article.setTitle("月卡");
                    article.setDescription("感谢您关注宝大夫综合育儿服务平台，宝大夫现对新用户推出月卡服务。");
                    article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/Follow.jpg");
                    article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
                            sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/getUserWechatMenId?url=21");
                    articleList.add(article);
                    memberService.insertMemberSendMessage(xmlEntity.getFromUserName(), "1");
                } else if (value == true) {
                    article.setTitle("活动已过期，赠送周会员");
                    article.setDescription("您好，此活动已过期，不过别担心，您仍可参加免费体验宝大夫短期会员服务");
                    article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/Follow.jpg");
                    article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
                            sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/getUserWechatMenId?url=20");
                    articleList.add(article);
                }
            } else if (userType.equals("oldUser")) {
                article.setDescription("感谢您关注宝大夫综合育儿服务平台，本次活动只针对新关注用户，" +
                        "请把这份幸运分享给您身边的宝宝吧！再次感谢您对宝大夫的支持与信任！" +
                        "\n\n点击进入，全新郑玉巧玉儿经免费查阅！");
                articleList.add(article);
                article.setUrl("");
            }
        } else if (EventKey.indexOf("quarter") > -1) {
            if (userType.equals("newUser")) {
                Boolean value = activityService.judgeActivityValidity(EventKey.replace("qrscene_", ""));
                if (value == false) {
                    //推送赠送季会员URL消息
                    article.setTitle("季卡");
                    article.setDescription("感谢您关注宝大夫综合育儿服务平台，宝大夫现对新用户推出季卡服务。");
                    article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/Follow.jpg");
                    article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
                            sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/getUserWechatMenId?url=22");
                    articleList.add(article);
                    memberService.insertMemberSendMessage(xmlEntity.getFromUserName(), "1");
                } else if (value == true) {
                    article.setTitle("活动已过期，赠送周会员");
                    article.setDescription("您好，此活动已过期，不过别担心，您仍可参加免费体验宝大夫短期会员服务");
                    article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/Follow.jpg");
                    article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
                            sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/wechatInfo/getUserWechatMenId?url=20");
                    articleList.add(article);
                }
            } else if (userType.equals("oldUser")) {
                article.setDescription("感谢您关注宝大夫综合育儿服务平台，本次活动只针对新关注用户，" +
                        "请把这份幸运分享给您身边的宝宝吧！再次感谢您对宝大夫的支持与信任！" +
                        "\n\n点击进入，全新郑玉巧玉儿经免费查阅");
                article.setUrl("http://baodf.com/titan/wechatInfo/fieldwork/wechat/author?" +
                        "url=http://baodf.com/titan/wechatInfo/getUserWechatMenId?url=4");
                articleList.add(article);
            }
        } else if (EventKey.indexOf("xuanjianghuodong_zhengyuqiao_saoma") > -1) {
            article.setDescription("您好，欢迎关注！" +
                    "\n\n点击进入宝大夫-郑玉巧育儿经，一起交流学习育儿健康管理知识！");
            article.setUrl("http://baodf.com/titan/wechatInfo/fieldwork/wechat/author?" +
                    "url=http://baodf.com/titan/wechatInfo/getUserWechatMenId?url=4");
            articleList.add(article);
        } else if (EventKey.indexOf("FQBTG") > -1) {
            article.setTitle("防犬宝,一份温馨的安全保障");
            article.setDescription("只要19.8元，打狂犬疫苗最高可获得互助补贴1000元。不幸患狂犬病可获得互助补贴5万元！");
            article.setPicUrl("http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/FQBTGXX.png");
            article.setUrl(sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=26");
            articleList.add(article);
        } else if (EventKey.indexOf("homepage_qualityservices_kuaizixun") > -1) {//官网快咨询
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(xmlEntity.getFromUserName());
            textMessage.setFromUserName(xmlEntity.getToUserName());
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            textMessage.setContent("1、点击左下角“小键盘”输入文字或语音,即可咨询疾病或保健问题\t\t\n 2、免费在线咨询时间:\n小儿内科:   24小时全天\n小儿皮肤科:   9:00~22:00\n营养保健科:   9:00~22:00\n小儿其他专科:(外科、眼科、耳鼻喉科、口腔科、预防保健科、中医科)   19:00~21:00 \n妇产科   19:00~22:00");
            return MessageUtil.textMessageToXml(textMessage);
        } else if (EventKey.indexOf("homepage_qualityservices_mingyimianzhen") > -1) {//官网名医面诊
            article.setTitle("名医面诊");
            article.setDescription("三甲医院儿科专家，线上准时预约，线下准时就诊。");
            article.setPicUrl("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/gw%2Fmingyimianzhen");
            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/firstPage/appoint");
            articleList.add(article);
        } else if (EventKey.indexOf("homepage_qualityservices_mingyidianhua") > -1) {//官网名医电话
            article.setTitle("名医电话");
            article.setDescription("权威儿科专家，10分钟通话，个性化就诊和康复指导。");
            article.setPicUrl("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/gw%2Fmingyidianhua");
            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/firstPage/phoneConsult");
            articleList.add(article);
        } else if (EventKey.indexOf("qrscene_12") > -1 || EventKey.indexOf("qrscene_13") > -1) {//扫码分享
            String toOpenId = xmlEntity.getFromUserName();//扫码者openid
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("openid", toOpenId);
            List<Map<String, Object>> list1 = babyUmbrellaInfoService.getBabyUmbrellaInfo(param1);

            System.out.println(list1.size() + "list1.size()++++++++++++++++++++++++++++++++++++++++++++++");
            boolean sendsucmes = false;
            if (list1.size() == 0) {//用户第一次加入保护伞
                Runnable thread = new sendUBWechatMessage(toOpenId, EventKey);
                threadExecutor.execute(thread);
            } else {
                if ("success".equals(list1.get(0).get("pay_result"))) {
                    sendsucmes = true;
                }
            }
            System.out.println(sendsucmes + "sendsucmes=============sendsucmes============================");
            if (sendsucmes) {
                article.setTitle("恭喜您");
                article.setDescription("您已成功领到20万保障，分享1个好友，提升2万保障，最高可享受40万保障。\n\n点击进入，立即分享！");
                article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
                article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31");
                articleList.add(article);
            }

            if ("oldUser".equals(userType) && !sendsucmes) {//老用户扫码发送保护伞信息
                int count = babyUmbrellaInfoService.getUmbrellaCount();
                article.setTitle("宝大夫送你一份见面礼");
                article.setDescription("专属于宝宝的40万高额保障金5元即送，目前已有" + count + "位妈妈们领取，你也赶紧加入吧，运气好还能免单哦！");
                article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
                article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a");
                articleList.add(article);
            }
        } else if (EventKey.indexOf("qrscene_99") > -1) {
            //如果扫码者来自非微信平台
            String openId = xmlEntity.getFromUserName();//扫码者openid
            String marketer = EventKey.replace("qrscene_", "");//渠道
            StringBuffer sbf = new StringBuffer("12");
            Map<String, Object> map = new HashMap<String, Object>();

            String umbrellaid = sbf.append(marketer.substring(2)).toString();
            String userPhone = "";
            map.put("id", umbrellaid);
            List<Map<String, Object>> umbrellaList = babyUmbrellaInfoService.getBabyUmbrellaInfo(map);
            if (umbrellaList != null && umbrellaList.size() > 0) {
                userPhone = (String) umbrellaList.get(0).get("parent_phone");
            }

            Map<String, Object> openIdMap = new HashMap<String, Object>();
            openIdMap.put("openId", openId);
            List<Map<String, Object>> openIdList = babyUmbrellaInfoThirdPartyService.getIfBuyUmbrellaByOpenidOrPhone(openIdMap);
            List<Map<String, Object>> openIdAndPhoneList = null;
            if (openIdList != null && openIdList.size() > 0) {
                //该微信已经购买(其实这种情况是不可能发生的,防止非微信平台填写信息时判断出错,多一份保障;)
                openIdMap.put("userPhone", userPhone);
                openIdAndPhoneList = babyUmbrellaInfoThirdPartyService.getIfBuyUmbrellaByOpenidOrPhone(openIdMap);
                if (openIdAndPhoneList != null && openIdAndPhoneList.size() > 0) {
                    //此微信下已有宝护伞，并且该微信下的手机号与非微信平台购买的手机号一致,推送以下消息
                    article.setTitle("宝大夫送您一份见面礼");
                    article.setDescription("恭喜您已成功领取专属于宝宝的20万高额保障金");
                    article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
                    article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaJoin/1467962511697/130000002");
                    articleList.add(article);
                } else {
                    //此微信下已有宝护伞，并且该微信下的手机号与非微信平台购买的手机号不一致,推送以下消息
                    article.setTitle("您的微信已经加入宝护伞，请更换其他微信账号。");
                    article.setDescription("查看我的保障");
                    //article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
                    article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaJoin/1467962511697/130000002");

                    articleList.add(article);
                }
            } else {
                //全新用户，并且在非微信平台已购买宝护伞
                //首先完成绑定，然后推送消息
                PatientVo patientVo = utilService.bindUserForThirdParty(userPhone, openId);
                //BabyBaseInfoVo vo = new BabyBaseInfoVo();
                //vo.setUserid(patientVo.getSysUserId());
                //vo.setOpenid(openId);
                //int result = babyBaseInfoService.updateBabyInfoByUserId(vo);

                BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
                babyUmbrellaInfo.setId(Integer.valueOf(umbrellaid));
                babyUmbrellaInfo.setSource(marketer);
                babyUmbrellaInfo.setOpenid(openId);
                babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);

                //推送消息
                article.setTitle("宝大夫送您一份见面礼");
                article.setDescription("恭喜您已成功领取专属于宝宝的20万高额保障金");
                article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
                article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaJoin/1467962511697/130000002");
                articleList.add(article);
            }
        } else if (EventKey.indexOf("qrscene_15") > -1 || EventKey.startsWith("150")) {//扫码分享

            Integer openLevel = Integer.parseInt(Global.getConfig("OPEN_LEVEL"));
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");

//			奥运宝宝扫码
            if (EventKey.indexOf("150100000") > -1) {
                String st = "欢迎聪明的你来到“宝大夫”，向你推荐宝宝奥运大闯关游戏，" +
                        "<a href='http://s68.baodf.com/titan/appoint#/userEvaluate'>赶紧玩起来吧！</a>";
                WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
            } else {
//                String userOpenid = EventKey.replace("qrscene_", "");
//
//                OlyBabyGamesVo olyBabyGamesVo = olyGamesService.getBaseByMarketer(userOpenid);
//                Integer alreadyInviteNum = olyBabyGamesVo.getInviteFriendNumber() + 1;
//                if (alreadyInviteNum < 1 && openLevel >= 1) {
////					第一关
//                    olyBabyGamesVo.setGameLevel(1);
//                } else if (alreadyInviteNum < 3 && openLevel >= 2) {
////					第二关
//                    olyBabyGamesVo.setGameLevel(2);
//                } else if (alreadyInviteNum < 6 && openLevel >= 3) {
////						第三关
//                    olyBabyGamesVo.setGameLevel(3);
//                } else if (alreadyInviteNum < 10 && openLevel >= 4) {
////						第四关
//                    olyBabyGamesVo.setGameLevel(4);
//                } else if (alreadyInviteNum < 15 && openLevel >= 5) {
////						第五关
//                    olyBabyGamesVo.setGameLevel(5);
//                } else if (alreadyInviteNum >= 15 && openLevel >= 6) {
////						第六关
//                    olyBabyGamesVo.setGameLevel(6);
//                }

                //	如果是新用户推广者加一
//                if (olyGamesService.getNewAttentionByOpenId(xmlEntity.getFromUserName()) == 0 && !EventKey.startsWith("150")) {
//                    olyBabyGamesVo.setInviteFriendNumber(alreadyInviteNum);
//
//                    String msg = "";
//                    Integer gemeLevel = olyBabyGamesVo.getGameLevel();
//                    Integer needInviteNum = 0;
//                    for (int i = 0; i <= gemeLevel; i++) {
//                        needInviteNum += i;
//                    }
//                    needInviteNum -= alreadyInviteNum;
//                    if (gemeLevel >= openLevel) {
//                        msg = "已开通第" + gemeLevel + "关";
//                        if (openLevel == 6) msg = "满六关：您已成功开通所有关卡";
//
//                    } else {
//                        msg = "已开通第" + gemeLevel + "关，还需邀请" + needInviteNum + "位好友开通下一关";
//                    }
//                    WechatMessageUtil.templateModel("游戏首页", "恭喜您，已经有" + alreadyInviteNum + "位好友在宝宝奥运大闯关游戏中为你助力，赶紧继续闯关吧！", msg, "", "", "快去闯关玩游戏抽奖吧！", token, "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37", olyBabyGamesVo.getOpenId(), "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg");
//                    //				更新用户信息
//                    olyGamesService.updateByPrimaryKeySelective(olyBabyGamesVo);
//                }
                ;
//
//
//                String st = "感谢你的倾情助力，" + olyBabyGamesVo.getNickName() + "为“宝大夫”带盐，向您推荐宝宝奥运大闯关游戏，" +
//                        "<a href='http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37'>赶紧玩起来吧！</a>";
//                WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
            }
        } else if (EventKey.indexOf("yufangjiezhong") > -1) {//gzg
            //有名片的医生扫码用户,推送文字消息.(扫码有名片医生二维码.)
            Runnable thread = new sendHasCardDoctorWechatMessage(EventKey, xmlEntity);
            threadExecutor.execute(thread);
        }

        String toOpenId = xmlEntity.getFromUserName();//扫码者openid
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("openid", toOpenId);
        List<Map<String, Object>> list1 = babyUmbrellaInfoService.getBabyUmbrellaInfo(param1);

        if (list1.size() == 0) {//用户第一次加入保护伞
            Runnable thread = new sendUBWechatMessage(toOpenId, EventKey);
            threadExecutor.execute(thread);
        } else {
            if ("success".equals(list1.get(0).get("pay_result"))) {
                Runnable thread = new addUserType(toOpenId);
                threadExecutor.execute(thread);
            }
        }

        if (articleList.size() == 0) {
            return "";
        }
        // 设置图文消息个数
        newsMessage.setArticleCount(articleList.size());
        // 设置图文消息包含的图文集合
        newsMessage.setArticles(articleList);
        // 将图文消息对象转换成xml字符串
        String respMessage = MessageUtil.newsMessageToXml(newsMessage);
        return respMessage;
    }

    public class sendUBWechatMessage extends Thread {
        private String toOpenId;
        private String EventKey;

        public sendUBWechatMessage(String toOpenId, String EventKey) {
            this.toOpenId = toOpenId;
            this.EventKey = EventKey;
        }

        @Override
        public void run() {
            try {
                sendUBWechatMessage(toOpenId, EventKey);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void sendUBWechatMessage(String toOpenId, String EventKey) {
        Map<String, Object> param = new HashMap<String, Object>();
        String id = EventKey.split("_")[1];
        param.put("id", id);
        List<Map<String, Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
        if (list.size() != 0) {//有分享者时，修改分享者信息并发送给分享者信息
            String fromOpenId = (String) list.get(0).get("openid");//分享者openid
            String babyId = (String) list.get(0).get("baby_id");
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            int oldUmbrellaMoney = (Integer) list.get(0).get("umbrella_money");
            int newUmbrellaMoney = (Integer) list.get(0).get("umbrella_money") + 20000;
            int friendJoinNum = (Integer) list.get(0).get("friendJoinNum");
            WechatAttention wa = wechatAttentionService.getAttentionByOpenId(toOpenId);
            String nickName = "";
            if (wa != null) {
                if (StringUtils.isNotNull(wa.getNickname())) {
                    nickName = wa.getNickname();
                } else {
                    WechatBean userinfo = WechatUtil.getWechatName(token, toOpenId);
                    nickName = StringUtils.isNotNull(userinfo.getNickname()) ? userinfo.getNickname() : "";
                }
            }
            String title = "恭喜您，您的好友" + nickName + "已成功加入。您既帮助了朋友，也提升了2万保障金！";
            String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
            String keyword1 = "您已拥有" + newUmbrellaMoney / 10000 + "万的保障金，还需邀请" + (400000 - newUmbrellaMoney) / 20000 + "位好友即可获得最高40万保障金。";
            String remark = "邀请一位好友，增加2万保额，最高可享受40万保障！";
            if (oldUmbrellaMoney < 400000) {
                BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
                babyUmbrellaInfo.setId(Integer.parseInt(id));
                babyUmbrellaInfo.setUmberllaMoney(newUmbrellaMoney);
                babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
            }
            if (newUmbrellaMoney >= 400000) {
                title = "感谢您的爱心，第" + (friendJoinNum + 1) + "位好友" + nickName + "已成功加入，一次分享，一份关爱，汇聚微小力量，传递大爱精神！";
                templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
                keyword1 = "您已成功拥有40万的最高保障金。";
                remark = "您还可以继续邀请好友，传递关爱精神，让更多的家庭拥有爱的保障！";
            }
            BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
            babyUmbrellaInfo.setId(Integer.parseInt(id));
            babyUmbrellaInfo.setFriendJoinNum(friendJoinNum + 1);
            babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);

            String keyword2 = StringUtils.isNotNull(babyId) ? "观察期" : "待激活";
            String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
            WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, fromOpenId, templateId);
        }

    }


    /**
     * 给有名片的医生扫码进来的用户推送文字消息
     *
     * @author guozengguang
     */
    public class sendHasCardDoctorWechatMessage extends Thread {
        private String EventKey;
        private ReceiveXmlEntity xmlEntity;

        public sendHasCardDoctorWechatMessage(String EventKey, ReceiveXmlEntity xmlEntity) {
            this.EventKey = EventKey;
            this.xmlEntity = xmlEntity;
        }

        @Override
        public void run() {

            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            String marketer = EventKey.replace("qrscene_", "");
            String[] marketsArr = Global.getConfig("consult.doctor.marketers").split(",");
            List marketsList = Arrays.asList(marketsArr);
            //如果医生名片里包含用户所扫的二维码,则推送相应的文字消息.
            if (marketsList.contains(marketer)) {
                Map<String, Object> doctorMap = consultConversationService.getConsultCountsByDoctorName(marketer);
                StringBuffer consultRelateMsg = new StringBuffer();

                if (StringUtils.isNull(doctorMap.get("doctorName").toString())) {
                    doctorMap = consultConversationService.getDoctorInfoByMarketer(marketer);
                }

                consultRelateMsg.append(doctorMap.get("doctorName").toString()).append(" | ");
                consultRelateMsg.append(doctorMap.get("hospitalName").toString()).append(" | ");
                consultRelateMsg.append(doctorMap.get("professional").toString()).append(": ");
                consultRelateMsg.append(doctorMap.get("skill").toString());
                consultRelateMsg.append("\n\n点击左下角“小键盘”输入文字或图片，即可咨询疾病或保健问题。\n");
                WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), consultRelateMsg.toString());
            }
        }
    }

    //为保护伞用户,更改用户标签,匹配个性化菜单。

    public class addUserType extends Thread {
        private String toOpenId;

        public addUserType(String toOpenId) {
            this.toOpenId = toOpenId;
        }

        @Override
        public void run() {
            addUserType(toOpenId);
        }
    }

    public String addUserType(String id) {
        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + token;
        String jsonData = "{\"openid_list\":[\"" + id + "\"],\"tagid\" : 105}";
        String reJson = this.post(url, jsonData, "POST");
        System.out.println(reJson);
        JSONObject jb = JSONObject.fromObject(reJson);
        String errmsg = jb.getString("errmsg");
        if (errmsg.equals("ok")) {
            return "ok";
        } else {
            return errmsg;
        }
    }


    /**
     * 发送HttpPost请求
     *
     * @param strURL 服务地址
     * @param params json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     *               type (请求方式：POST,GET)
     * @return 成功:返回json字符串<br/>
     */
    public String post(String strURL, String params, String type) {
        System.out.println(strURL);
        System.out.println(params);
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                System.out.println(result);
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }

    private String processSubscribeEvent(ReceiveXmlEntity xmlEntity, HttpServletRequest request, HttpServletResponse response, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        String EventKey = xmlEntity.getEventKey();
        String marketer = "";
        if (StringUtils.isNotNull(EventKey)) {
            marketer = EventKey.replace("qrscene_", "");
        }
        //宝宝币
        babyCoinHandler(xmlEntity, token, marketer);
        this.insertAttentionInfo(xmlEntity, token, marketer);
        //特定渠道优惠
        specificChanneldeal(xmlEntity, token);

        return sendSubScribeMessage(xmlEntity, request, response, marketer, token, sysPropertyVoWithBLOBsVo);
    }

    private synchronized void specificChanneldeal(ReceiveXmlEntity xmlEntity, String token) {
        String EventKey = xmlEntity.getEventKey();
        String eventType = xmlEntity.getEvent();
        String openid = xmlEntity.getFromUserName();

        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openid);
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        //查找出当前的特定渠道遍历
        Query queryDate = new Query();
        List<SpecificChannelRuleVo> ruleList = specificChannelRuleMongoDBService.queryList(queryDate);
        for (SpecificChannelRuleVo vo : ruleList) {
            if (EventKey.contains(vo.getQrcode())) {
                queryDate = new Query();
                Criteria criteria = Criteria.where("openid").is(openid);
                queryDate.addCriteria(criteria);
                queryDate.with(new Sort(new Sort.Order(Sort.Direction.ASC, "date"))).limit(1);
                List<SpecificChannelInfoVo> infoList = specificChannelInfoMongoDBService.queryList(queryDate);
                //此条件判断当前用户是否参与过特殊渠道赠送活动,多个活动不能同时参与 ,如修改则需要在增加渠道判断条件
                if (infoList.size() == 0) {
                    WechatUtil.sendMsgToWechat(token, openid, vo.getDocuments());
                    BabyCoinVo babyCoin = new BabyCoinVo();
                    babyCoin.setOpenId(openid);

                    babyCoin = babyCoinService.selectByBabyCoinVo(babyCoin);//推荐人的babyCoin
                    if (null == babyCoin || null == babyCoin.getCash()) {//新用户，初始化宝宝币
                        babyCoin = new BabyCoinVo();
                        babyCoin.setCash(vo.getBabyCoin());
                        babyCoin.setCreateBy(openid);
                        babyCoin.setCreateTime(new Date());
                        babyCoin.setOpenId(openid);
                        babyCoin.setNickName(wechatAttentionVo.getWechat_name());
                        BabyCoinVo lastBabyCoinUser = new BabyCoinVo();
                        lastBabyCoinUser.setCreateTime(new Date());
                        lastBabyCoinUser = babyCoinService.selectByBabyCoinVo(lastBabyCoinUser);
                        if (lastBabyCoinUser == null || lastBabyCoinUser.getMarketer() == null) {
                            babyCoin.setMarketer("110000000");//初始值
                        } else {
                            babyCoin.setMarketer(String.valueOf(Integer.valueOf(lastBabyCoinUser.getMarketer()) + 1));
                        }
                        babyCoinService.insertBabyCoinSelective(babyCoin);
                    } else {
                        babyCoin.setCash(vo.getBabyCoin());
                        babyCoinService.updateCashByOpenId(babyCoin);
                    }
                    SpecificChannelInfoVo channelVo = new SpecificChannelInfoVo();
                    channelVo.setId(IdGen.uuid());
                    channelVo.setBabyCoin(vo.getBabyCoin());
                    channelVo.setCreateTime(new Date());
                    channelVo.setOpenid(openid);
                    channelVo.setQrcode(vo.getQrcode());
                    specificChannelInfoMongoDBService.insert(channelVo);
                } else {
                    WechatUtil.sendMsgToWechat(token, openid, vo.getRepeatdocuments());
                }
            }
        }


    }

    ;

    private void babyCoinHandler(ReceiveXmlEntity xmlEntity, String token, String marketer) {
        if (marketer.startsWith("110")) {
            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            String newOpenId = xmlEntity.getFromUserName();
            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(newOpenId);
            SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
            //新用户从来没有关注过的
            if (wechatAttentionVo == null) {

                BabyCoinVo olderUser = new BabyCoinVo();
                String cash = sysPropertyVoWithBLOBsVo.getBabyCoin();
//                synchronized (this) {
                olderUser.setMarketer(marketer);
                olderUser = babyCoinService.selectByBabyCoinVo(olderUser);//推荐人的babyCoin
                String oldOpenId = olderUser.getOpenId();
                if (olderUser.getInviteNumberMonth() <= 20) {
                    Long preCash = olderUser.getCash();
                    olderUser.setCash(Long.valueOf(cash));
                    olderUser.setInviteNumberMonth(1);
                    //推荐人宝宝币加sysPropertyVoWithBLOBsVo.getBabyCoin()个
                    babyCoinService.updateCashByOpenId(olderUser);
                    Long afterCash = preCash + Long.valueOf(cash);

                    BabyCoinRecordVo babyCoinRecordVo = new BabyCoinRecordVo();
                    babyCoinRecordVo.setBalance(Double.valueOf(sysPropertyVoWithBLOBsVo.getBabyCoin()));
                    babyCoinRecordVo.setCreateTime(new Date());
                    babyCoinRecordVo.setCreateBy(oldOpenId);
                    babyCoinRecordVo.setOpenId(oldOpenId);
                    babyCoinRecordVo.setSource("invitePresent");
                    int recordflag = babyCoinService.insertBabyCoinRecord(babyCoinRecordVo);
                    if (recordflag == 0) {
                        throw new RuntimeException("宝宝币插入异常");
                    }
                    //------------给当前用户推送消息------------
                    String content = "恭喜您获得4次免费咨询儿科专家的机会。\n" +
                            "点击左下角小键盘，即可咨询医生。";
                    WechatUtil.sendMsgToWechat(token, newOpenId, content);

                    //------------给推荐人推送消息-------------
                    //获取新用户newUserNickName
                    WechatBean wechatBean = WechatUtil.getWechatName(token, newOpenId);
                    String newUserNickName = wechatBean.getNickname();
                    if (StringUtils.isNull(newUserNickName)) {
                        if (wechatAttentionVo != null && wechatAttentionVo.getWechat_name() != null) {
                            newUserNickName = wechatAttentionVo.getWechat_name();
                        } else {
                            newUserNickName = "了一位朋友";
                        }
                    }

                    LogUtils.saveLog("老用户剩余的宝宝币数="+afterCash+"邀请的好友为："+newUserNickName,oldOpenId);

//                    WechatUtil.sendMsgToWechat(token, olderUser.getOpenId(), content);
                    String title = "恭喜您成功邀请 " + newUserNickName + " 加入宝大夫，您的您的宝宝币将增加" + cash + "枚！";
                    String templateId = sysPropertyVoWithBLOBsVo.getTemplateIdYWDTTX();
                    String keyword1 = "业务进度：您的宝宝币余额为 " + afterCash + "枚";
                    String keyword2 = "您有" + afterCash / 99 + "次免费咨询专家的机会，本月还可邀请好友"+(20-olderUser.getInviteNumberMonth())+"次";
                    String remark = "邀请更多好友加入>，获得更多机会！";
                    String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_MBXX";
//                    WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, oldOpenId, templateId);
                    String templateInfo = "\"first\": {\"value\":"+title+",\"color\":\"#FF0000\"},\"keynote1\":{ \"value\":"+keyword1+",, \"color\":\"#000000\"},\"keynote2\": { \"value\":"+keyword2+", \"color\":\"#000000\" }, \"remark\":{ \"value\":"+remark+",\"color\":\"#FF0000\"}";
                    WechatUtil.sendTemplateMsgToUser(token,oldOpenId,templateId,templateInfo);

                }

            }
        }
    }

    private void insertAttentionInfo(ReceiveXmlEntity xmlEntity, String token, String marketer) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String openId = xmlEntity.getFromUserName();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        WechatBean wechatBean = WechatUtil.getWechatName(token, openId);

        map.put("id", id);
        map.put("status", "0");
        map.put("openId", openId);
        map.put("marketer", marketer);
        map.put("doctorMarketer", marketer);
        map.put("updateTime", new Date());
        map.put("nickname", EmojiFilter.coverEmoji(wechatBean.getNickname()));
        int attentionNum = wechatInfoDao.checkAttention(map);
        if (attentionNum > 0) {
            map.put("ispay", 0);
        } else {
            map.put("ispay", 1);
        }
        wechatInfoDao.insertAttentionInfo(map);

        if ("true".equalsIgnoreCase(mongoEnabled)) {
            WechatAttention attention = new WechatAttention();
            attention.setId(id);
            attention.setDate(new Date());
            attention.setMarketer(marketer);
            attention.setNickname(wechatBean.getNickname());
            attention.setOpenid(openId);
            attention.setStatus("0");
            mongoDBService.insert(attention);

            MongoLog mongoLog = new MongoLog();
            mongoLog.setId(IdGen.uuid());
            mongoLog.setCreate_by(UserUtils.getUser().getId());
            mongoLog.setCreate_date(new Date());
            mongoLog.setTitle("00000001");//微信宝大夫用户版公众平台关注
            mongoLog.setIsPay(map.get("ispay").toString());
            mongoLog.setNickname((String) map.get("nickname"));
            mongoLog.setOpen_id(openId);
            mongoLog.setMarketer(marketer);
            mongoLog.setStatus((String) map.get("status"));
            mongoLogService.insert(mongoLog);
        }
    }

    private void updateAttentionInfo(ReceiveXmlEntity xmlEntity) {
        String EventKey = xmlEntity.getEventKey();
        Date updateDate = new Date();
        String openId = xmlEntity.getFromUserName();
        String marketer = EventKey.replace("qrscene_", "");
        HashMap<String, Object> updateTimeMap = new HashMap<String, Object>();
        updateTimeMap.put("openId", openId);
        updateTimeMap.put("updateTime", updateDate);
        updateTimeMap.put("doctorMarketer", marketer);
        //new zdl
        wechatInfoDao.updateAttentionInfo(updateTimeMap);
    }

    private String sendSubScribeMessage(ReceiveXmlEntity xmlEntity, HttpServletRequest request, HttpServletResponse response, String marketer, String token, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {
        HttpSession session = request.getSession();
        session.setAttribute("openId", xmlEntity.getFromUserName());
        LogUtils.saveLog(request, "00000001");//注：参数含义请参照sys_log_mapping表，如00000001表示“微信宝大夫用户版公众平台关注”
        String EventKey = xmlEntity.getEventKey();
        if (EventKey.indexOf("xuanjianghuodong_zhengyuqiao_saoma") <= -1 && EventKey.indexOf("baoxian_000001") <= -1 && EventKey.indexOf("YY0016") <= -1 && EventKey.indexOf("YY0018") <= -1) {
//			List<Article> articleList = new ArrayList<Article>();
//			Article article = new Article();
//			article.setTitle("对孩子健康负责的家长必看！万人推荐！");
//			article.setDescription("");
//			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%AE%9D%E6%8A%A4%E4%BC%9Ebanner2%20-%20%E5%89%AF%E6%9C%AC%20%E6%8B%B7%E8%B4%9D.png");
//			article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a");
//			articleList.add(article);
//
//			article = new Article();
//			article.setTitle("咨询大夫\n秒回不等待，7X24全年无休");
//			article.setDescription("三甲医院医生7X24全年无休   一分钟极速回复");
//			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%92%A8%E8%AF%A2%E5%A4%A7%E5%A4%AB.png");
//			article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
//			articleList.add(article);
//
//			article = new Article();
//			article.setTitle("名医面诊\n轻松预约专家，到点就诊不排队");
//			article.setDescription("三甲医院儿科专家，线上准时预约，线下准时就诊");
//			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%90%8D%E5%8C%BB%E9%9D%A2%E8%AF%8A.png");
//			article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=2");
//			articleList.add(article);
//
//			article = new Article();
//			article.setTitle("妈妈社群\n育儿交流名医讲座，福利发不停");
//			article.setDescription("添加宝大夫客服微信：bdfdsb，加入宝大夫家长群，与众多宝妈一起交流分享，参与更多好玩儿的活动");
//			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%A6%88%E5%A6%88%E6%B4%BB%E5%8A%A8.png");
//			article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236661&idx=3&sn=4c1fd3ee4eb99e6aca415f60dceb6834&scene=1&srcid=0616uPcrUKz7FVGgrmOcZqqq&from=singlemessage&isappinstalled=0&key=18e81ac7415f67c44d3973b3eb8e53f264f47c1109eceefa8d6be994349fa7f152bb8cfdfab15b36bd16a4400cd1bd87&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=ZgGIH5%2B8%2FkhHiHeeRG9v6qbPZmK5qPlBL02k0Qo%2FHCK7eLMOZexAypBy0dzPjzaZ");
//			articleList.add(article);

//			WechatUtil.senImgMsgToWechat(token,xmlEntity.getFromUserName(),articleList);


            String welcomeMsg = "很高兴遇见您！我们是一群有爱又专业的医生朋友。\ue022 \n\n" +
                    "在这里，您可以24H随时咨询妇产科和儿科专家！\uD83C\uDFE5科室齐全，回复超快！\uD83D\uDE80 点击:<a href='http://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR#rd'>咨询大夫</a> \n\n" +
                    "还可买到各大名牌母婴产品，足不出户享受最真、最快、最优的实惠商品\uD83D\uDC9D。点击：<a href='https://shop17975201.koudaitong.com/v2/home/4z2f8goy?reft=1477987510078&spm=g305026344&oid=0&sf=wx_sm'>立即购买</a>\n\n" +
                    "更可随时学习育儿知识\uD83D\uDC76，和千万宝宝一同快乐成长\uD83D\uDC6A！点击：<a href='https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI2MDAxOTY3OQ==&from=singlemessage&isappinstalled=0&scene=116#wechat_redirect'>学习知识</a>";
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), welcomeMsg);
        }
        return processScanEvent(xmlEntity, "newUser", request, response, sysPropertyVoWithBLOBsVo);
    }

    private void processUnSubscribeEvent(ReceiveXmlEntity xmlEntity, HttpServletRequest request) {
        // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
        HttpSession session = request.getSession();
        session.setAttribute("openId", xmlEntity.getFromUserName());
        LogUtils.saveLog(request, "00000002");//注：参数含义请参照sys_log_mapping表，如00000002表示“微信宝大夫用户版公众平台取消关注”
        HashMap<String, Object> map = new HashMap<String, Object>();
        String openId = xmlEntity.getFromUserName();
        map.put("openId", openId);
        //根据openid查询最近关注的marketer，防止取消关注的时候marketer总是为空
        WechatAttention wechatAttention = new WechatAttention();
        wechatAttention.setOpenid(openId);
        wechatAttention = wechatAttentionDao.findMarketerByOpeinid(wechatAttention);
        if (null != wechatAttention) {
            map.put("marketer", wechatAttention.getMarketer());
        }
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        map.put("id", id);
        map.put("status", "1");
        wechatInfoDao.insertAttentionInfo(map);

        if ("true".equalsIgnoreCase(mongoEnabled)) {
            WechatAttention attention = new WechatAttention();
            attention.setId(id);
            attention.setDate(new Date());
            if (null != wechatAttention)
                attention.setMarketer(wechatAttention.getMarketer());
            attention.setOpenid(openId);
            attention.setStatus("1");
            mongoDBService.insert(attention);

            MongoLog mongoLog = new MongoLog();
            mongoLog.setId(IdGen.uuid());
            mongoLog.setCreate_by(UserUtils.getUser().getId());
            mongoLog.setTitle("00000002");//微信宝大夫用户版公众平台取消关注
            mongoLog.setOpen_id((String) map.get("openId"));
            mongoLog.setStatus((String) map.get("status"));
            mongoLogService.insert(mongoLog);
        }
    }

    private String processClickMenuEvent(ReceiveXmlEntity xmlEntity, HttpServletRequest request, HttpServletResponse response) {
        // TODO 自定义菜单
        String respMessage = "";
        if ("39".equals(xmlEntity.getEventKey())) {
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(xmlEntity.getFromUserName());
            textMessage.setFromUserName(xmlEntity.getToUserName());
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            String st = "您好，我是宝大夫专职医护，愿为您提供医学服务，协助预约儿科专家，请您拨打：400-623-7120。";
            textMessage.setContent(st);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } else if ("38".equals(xmlEntity.getEventKey())) {
            HttpSession session = request.getSession();
            session.setAttribute("openId", xmlEntity.getFromUserName());
            LogUtils.saveLog(request, "00000003");//注：参数含义请参照sys_log_mapping表，如00000003表示“咨询医生消息推送”
//			TextMessage textMessage = new TextMessage();
//			textMessage.setToUserName(xmlEntity.getFromUserName());
//			textMessage.setFromUserName(xmlEntity.getToUserName());
//			textMessage.setCreateTime(new Date().getTime());
//			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//			textMessage.setFuncFlag(0);
//			textMessage.setContent("1、点击左下角“小键盘”输入文字或语音,即可咨询疾病或保健问题\t\t\n 2、免费在线咨询时间:\n小儿内科:   24小时全天\n小儿皮肤科:   9:00~22:00\n营养保健科:   9:00~22:00\n小儿其他专科:(外科、眼科、耳鼻喉科、口腔科、预防保健科、中医科)   19:00~21:00 \n妇产科   19:00~22:00");

//			respMessage = MessageUtil.textMessageToXml(textMessage);
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("三甲医院妇儿专家    咨询秒回不等待");
            article.setDescription("小儿内科:       24小时全天 \n\n小儿皮肤科/保健科:   8:00 ~ 23:00\n\n妇产科:   8:00 ~ 23:00\n" +
                    "\n小儿其他专科:   19:00 ~ 21:00\n\n" +
                    "(外科、眼科、耳鼻喉科、口腔科、预防接种科、中医科)\n\n点击左下角键盘,输入内容或语音即可咨询");
            article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E6%8E%A8%E9%80%81%E6%B6%88%E6%81%AF2.png");
            article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
            articleList.add(article);
            WechatUtil.senImgMsgToWechat(token, xmlEntity.getFromUserName(), articleList);
            memberService.sendExtendOldMemberWechatMessage(xmlEntity.getFromUserName());
        } else if ("36".equals(xmlEntity.getEventKey())) {
            List<Article> articleList = new ArrayList<Article>();
            // 创建图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(xmlEntity.getFromUserName());
            newsMessage.setFromUserName(xmlEntity.getToUserName());
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setFuncFlag(0);
            Article article = new Article();
            article.setTitle("咨询大夫，请关注最新公众账号!");
            article.setDescription("公告：本公众号将于2015年8月9日起停止服务。需要咨询大夫，" +
                    "请妈妈们识别二维码关注最新的公众号（微信号：BaodfWX）。专业的大夫在线等您来咨询哦！");
            article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/dGa0GvlZMicotZvyd4ZkHKjYe3ERsP5OD0spQbFz1CPTd" +
                    "qqWjrP1s5pr4BpDxvM97NgYNm4PiazfHv37t6kbP9dw/640?wx_fmt=jpeg&wxfrom=5");
            article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI0MjAwNjY0Nw==&mid=208340985&idx=1&sn=2beb0d78fc9097f10d073e182f1cb6c1&scene=0#rd");
            articleList.add(article);
            // 设置图文消息个数
            newsMessage.setArticleCount(articleList.size());
            // 设置图文消息包含的图文集合
            newsMessage.setArticles(articleList);
            // 将图文消息对象转换成xml字符串
            respMessage = MessageUtil.newsMessageToXml(newsMessage);
        }

        return respMessage;
    }

    private String processCloseEvent(ReceiveXmlEntity xmlEntity, HttpServletRequest request, HttpServletResponse response) {
        String respMessage = null;
        String openId = xmlEntity.getFromUserName();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openid", openId);
        params.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        params.put("starNum1", 0);
        params.put("starNum2", 0);
        params.put("starNum3", 0);
        params.put("doctorId", xmlEntity.getKfAccount());
        params.put("content", "");
        params.put("dissatisfied", null);
        params.put("redPacket", null);
        params.put("evaluateSource", "realtimeConsult");
        patientRegisterPraiseDao.saveCustomerEvaluation(params);
        String st = "感谢您对我们的信任与支持，为了以后能更好的为您服务，请对本次服务做出评价！【" +
                "<a href='http://s68.baodf.com/titan/appoint#/userEvaluate/" + params.get("uuid") + "'>我要评价</a>】";
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        WechatUtil.sendMsgToWechat(token, openId, st);
        LogUtils.saveLog(request, "00000004");//注：00000004表示“客服评价”

        return respMessage;
    }

    private void processGetLocationEvent(ReceiveXmlEntity xmlEntity, HttpServletRequest request) {
        LogUtils.saveLog(request, "00000005");//00000005：获取地理位置信息
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
        map.put("latitude", xmlEntity.getLatitude());
        map.put("precision", xmlEntity.getPrecision());
        map.put("createTime", xmlEntity.getCreateTime());
        map.put("openid", xmlEntity.getFromUserName());
        map.put("longitude", xmlEntity.getLongitude());
        wechatInfoDao.insertCustomerLocation(map);
    }

    private String transferToCustomer(ReceiveXmlEntity xmlEntity) {
        ConsultSession vo = new ConsultSession();
        vo.setUpdateTime(new Date());
        vo.setStatus("0");
        vo.setUserId(xmlEntity.getFromUserName());
        vo.setCreateTime(new Date());
        String title = UUID.randomUUID().toString().replaceAll("-", "");
        vo.setTitle(title);
        vo.setCsUserId("110@BaodfWX");
        consultConversationService.saveConsultInfo(vo);
//
//		if(xmlEntity.getContent().equals("营养")){
//			String st = "加入宝大夫营养管理（仅限1-3岁），科学喂养让宝宝成长更加健康！\n\n" +
//					"点击直接进入：<a href='http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/" +
//					"author?url=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=10'>营养管理(戳此链接)</a>" +
//					"\n" +
//					"微信入口：宝大夫—宝粉之家—健康管理—营养管理";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.senImgMsgToWechat(token, xmlEntity.getFromUserName(), "营养报告 | 科学喂养的重要性", "这里面的不仅仅是答案。",
//					"http://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=403036004&idx=1&sn=db524245950080a097b0574927c97001" +
//							"&scene=0&previewkey=lQ2vTkDCu67xf5wIiSxoGJ1iJUUG%2F7eLf7OA%2FVEtaJE%3D#wechat_redirect",
//					"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/MessageImage%2F7.jpg");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			Log log=new Log();
//			log.setTitle("YYHD-YY");
//			log.setCreateDate(new Date());
//			log.setUpdateDate(new Date());
//			log.setOpenId(xmlEntity.getFromUserName());
//			LogUtils.saveLogVo(log);
//			return "";
//		}
//		if(xmlEntity.getContent().equals("安全")){
//			String st = "恭喜您，参与抽奖成功。抽奖结果请关注4月5号微信文章。" +
//					"\n\n" +
//					"更重要的是：" +
//					"\n" +
//					"关注宝大夫即可直接免费咨询北京三甲医院的儿科专家，1分钟内极速回复。就像微信聊天一样简单！\n" +
//					"\n" +
//					"点击下方小键盘，赶紧发送您的健康疑问吧！";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			Log log=new Log();
//			log.setTitle("YYHD-AQ");
//			log.setCreateDate(new Date());
//			log.setUpdateDate(new Date());
//			log.setOpenId(xmlEntity.getFromUserName());
//			LogUtils.saveLogVo(log);
//			return "";
//		}
        //增加一个判断用户宝宝信息的接口,如果没有宝宝信息则推送消息
//		List<BabyBaseInfoVo> babyList = healthRecordsService.selectUserBabyInfo(xmlEntity.getFromUserName());
//		boolean healthRecordMsgAlreadySend = false;
//		try{
//		  healthRecordMsgAlreadySend = this.findHealthRecordMsgByOpenid(xmlEntity.getFromUserName(),"BB_MSG");
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		if(babyList.size()==0 && !healthRecordMsgAlreadySend){
//			String st = "在正式开始咨询前，医生需要了解一些宝宝的简单信息，以便更快速精准和细致地为宝宝服务！\n" +
//					"---------------------\n"+
//					"<a href='http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/author?url" +
//					"=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=24'>点击这里补充宝宝信息</a>";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			HealthRecordMsgVo healthRecordMsgVo = new HealthRecordMsgVo();
//			healthRecordMsgVo.setOpenId(xmlEntity.getFromUserName());
//			healthRecordMsgVo.setType("BB_MSG");
//			insertHealthRecordMsg(healthRecordMsgVo);
//		}


        return "<xml><ToUserName><![CDATA[" + xmlEntity.getFromUserName() +
                "]]></ToUserName><FromUserName><![CDATA[" + xmlEntity.getToUserName() +
                "]]></FromUserName><CreateTime><![CDATA[" + new Date().getTime() +
                "]]></CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType>" +
                "</xml>";

    }

    /***
     * 根据openid查询消息发送记录
     *
     * @param openid type
     * @return 有记录 true 无记录 false
     */
    @Override
    public boolean findHealthRecordMsgByOpenid(String openid, String type) {
        HealthRecordMsgVo healthRecordMsgVo = new HealthRecordMsgVo();
        healthRecordMsgVo.setOpenId(openid);
        Query queryRecord = new Query();
        Date date = new Date();
        date.setHours(0);
        queryRecord.addCriteria(Criteria.where("open_id").is(openid).and("create_date").gte(date));
        Long RecordCount = healthRecordMsgVoMongoDBService.queryCount(queryRecord);
        return RecordCount != 0l ? true : false;
    }

    /***
     * 插入消息发送记录
     *
     * @param
     * @return
     */
    @Override
    public int insertHealthRecordMsg(HealthRecordMsgVo healthRecordMsgVo) {
        healthRecordMsgVo.setId(IdGen.uuid());
        healthRecordMsgVo.setCreate_date(new Date());
        healthRecordMsgVo.setOpen_id(healthRecordMsgVo.getOpenId());
        healthRecordMsgVo.setOpenId(null);
        return healthRecordMsgVoMongoDBService.insert(healthRecordMsgVo);
    }

    /**
     * 关键词回复功能
     *
     * @return boolean
     * 根据返回状态判断是否结束程序
     */
    public boolean keywordRecovery(ReceiveXmlEntity xmlEntity, String token, OperationPromotionStatusVo type) {
//        做一个缓存放在系统中
        if (null == keywordMap) {
            keywordMap = operationPromotionService.getAllRoleListByKeyword();
        }
        OperationPromotionVo roleInfo = keywordMap.get(xmlEntity.getContent());
        if (null == roleInfo || !roleInfo.getMessageType().equals(type.getVariable())) {
            return false;
        }

        if (StringUtils.isNotNull(roleInfo.getReplyText())) {
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), roleInfo.getReplyText());
        }
        if (StringUtils.isNotNull(roleInfo.getReplyPicId())) {
            for (String mediaId : roleInfo.getReplyPicId().split(",")) {
                if (StringUtils.isNotNull(mediaId))
                    WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), mediaId, 1);
            }
        }
        return true;
    }

    /**
     * 非实时咨询检测
     * 1、用户的免费机会是否用完
     * 2、没有购买的机会、本月首次，咨询次数是否超过26次
     * */
    public boolean nonRealTimeCheck(String whitelist, ReceiveXmlEntity xmlEntity, String token){
        String openid = xmlEntity.getFromUserName();
        if(StringUtils.isNotNull(whitelist)&&openid.indexOf(whitelist)==-1){
            return false;
        }
        ConsultSessionPropertyVo propertyVo =consultSessionPropertyService.findConsultSessionPropertyByUserId(openid);

        String path = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com.com/keeper/wechatInfo/getUserWechatMenId?url=40";
        //所有机会都用完
        if((propertyVo.getPermTimes()+propertyVo.getMonthTimes()) == 0){
            String content = "问题不着急？\n来试试“点名咨询”。您可指定专家医生或曾咨询过的医生，医生会在24小时内尽快对您的提问进行答复\n" +
                    "<a href='"+path+"'>>>点名咨询医生</a>";
            WechatUtil.sendMsgToWechat(token,openid,content);
            LogUtils.saveLog("FSS_YHD_RK1_TS",openid);

            return true;
        }
        ConsultSession  consultInfo = consultConversationService.selectByOpenid(openid);
//        首次
        if((propertyVo.getMonthTimes() == 4 && propertyVo.getPermTimes()==0 && consultInfo.getConsultNumber() > 26)){
            String content = "不想掏钱？\n来试试“点名咨询”。你可指定专家医生或曾咨询过的医生， 医生会在24h 内尽快对您的提问进行答复哦~\n（如有疑问，可直接拨打400-6237-120）\n" +
                    "<a href='"+path+"'>>>点名咨询医生</a>";
            WechatUtil.sendMsgToWechat(token,openid,content);
            LogUtils.saveLog("FSS_YHD_RK3_TS",openid);
            return true;
        }
        return false;
    }

    /**
     * 关键字缓存更新接口
     */
    @Override
    public void updateKeywordRecovery() {
        //从数据库重新查一次
        keywordMap = operationPromotionService.getAllRoleListByKeyword();
    }
}
