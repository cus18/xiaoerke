package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.activity.entity.EnglishActivityVo;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageActivityInfoVo;
import com.cxqm.xiaoerke.modules.activity.service.EnglishActivityService;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageActivityInfoService;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.interaction.dao.PatientRegisterPraiseDao;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.data.mongodb.core.query.Criteria.where;

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
    private EnglishActivityService englishActivityService;

//    @Autowired
//    private VaccineService vaccineService;

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private BabyUmbrellaInfoThirdPartyService babyUmbrellaInfoThirdPartyService;

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

    @Autowired
    private ConsultMemberRedsiCacheService consultMemberRedsiCacheService;

    private Map<String, OperationPromotionVo> keywordMap;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private RedPackageActivityInfoService redPackageActivityInfoService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static ExecutorService threadExecutorSingle = Executors.newSingleThreadExecutor();
    private static ExecutorService threadExecutorCash = Executors.newCachedThreadPool();

    private Lock lock = new ReentrantLock();


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
        String openId = xmlEntity.getFromUserName();
        String eventKey = xmlEntity.getEventKey();
        LogUtils.saveLog(xmlEntity.getFromUserName(), "msgComeInKeeper");
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
//                babyVaccineRemind(xmlEntity, token, sysPropertyVoWithBLOBsVo);
                //微量元素检测
//                traceElementsDetection(xmlEntity, token);
                send2016StoryEvent(xmlEntity, sysPropertyVoWithBLOBsVo);
                /**
                 *  疫苗站关注提醒 2016年12月12日11:23:52 jiangzg
                 */
                if (eventKey.contains("YMJZ_AH_")) {
                    attentionByThirdPlace(eventKey, openId, token, sysPropertyVoWithBLOBsVo);
                }

            } else if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                //扫描关注公众号或者搜索关注公众号都在其中
                respMessage = processSubscribeEvent(xmlEntity, request, response, sysPropertyVoWithBLOBsVo);

                send2016StoryEvent(xmlEntity, sysPropertyVoWithBLOBsVo);
                //疫苗提醒
//                babyVaccineRemind(xmlEntity, token, sysPropertyVoWithBLOBsVo);

                //微量元素检测
                traceElementsDetection(xmlEntity, token);

                /**
                 *  疫苗站关注提醒 2016年12月12日11:23:52 jiangzg
                 */
                if (eventKey.contains("YMJZ_AH_")) {
                    attentionByThirdPlace(eventKey, openId, token, sysPropertyVoWithBLOBsVo);
                }
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
                if (keywordRecovery(xmlEntity, token, OperationPromotionStatusVo.KEY_WORD) || !consultMemberRedsiCacheService.consultChargingCheck(xmlEntity.getFromUserName(), token, true)) {
                    LogUtils.saveLog(xmlEntity.getFromUserName(), "关键字拦截");
                    return "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String customerService = Global.getConfig("wechat.customservice");
            if ("false".equals(customerService)) {
                Runnable thread = new processConsultMessageThread(xmlEntity);
                threadExecutorSingle.execute(thread);
                return "";
            } else if ("true".equals(customerService)) {
                respMessage = transferToCustomer(xmlEntity);
            }
        }
        return respMessage;
    }

    /**
     * 2016年12月12日11:24:59 jiangzg
     */
    private void attentionByThirdPlace(String eventKey, String fromUserId, String token, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {
        if (StringUtils.isNotNull(eventKey) && eventKey.contains("YMJZ_AH_")) {
            StringBuilder sb = new StringBuilder();
            sb.append("点击领取：");
            sb.append("<a href='" + sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/vaccine#/vaccineList" + "'>");
            sb.append("疫苗接种告知单及相关知识>>");
            sb.append("</a>");
            WechatUtil.sendMsgToWechat(token, fromUserId, sb.toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("如有疼痛发热等症状及其他育儿问题，点击左下角");
            stringBuilder.append("\'" + "小键盘" + "\'，即可咨询儿科专家医生");
            stringBuilder.append("\n");
            stringBuilder.append("预防接种科咨询时间：19：00—21：00");
            WechatUtil.sendMsgToWechat(token, fromUserId, stringBuilder.toString());
            /* 暂时注掉
            String marketer = "";
            if(eventKey.contains("qrscene_")){
                marketer = eventKey.replace("qrscene_", "").trim();
            }else{
                marketer = eventKey.trim();
            }
            marketer = marketer.substring(marketer.lastIndexOf("_")+1,marketer.length());
            int marketerId = 0 ;
            if(marketer.startsWith("0")){
                marketerId = Integer.valueOf(marketer.substring(1));
            }else{
                marketerId = Integer.valueOf(marketer);
            }
            switch (marketerId){

            }*/
        }
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

    public class processConsultMessageThread extends Thread {
        private ReceiveXmlEntity xmlEntity;

        public processConsultMessageThread(ReceiveXmlEntity xmlEntity) {
            this.xmlEntity = xmlEntity;
        }

        public void run() {
            try {
                System.out.println(xmlEntity.getContent());
                SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("openId", xmlEntity.getFromUserName());
                jsonObject.put("messageType", xmlEntity.getMsgType());
                LogUtils.saveLog(xmlEntity.getFromUserName(), "sendMsgToAngel");
                if (xmlEntity.getMsgType().equals("text")) {
                    jsonObject.put("messageContent", URLEncoder.encode(xmlEntity.getContent(), "UTF-8"));
                    this.postByBody(sysPropertyVoWithBLOBsVo.getAngelWebUrl() + "angel/consult/wechat/conversation", jsonObject.toString());
                } else {
                    jsonObject.put("mediaId", xmlEntity.getMediaId());
                    this.postByBody(sysPropertyVoWithBLOBsVo.getAngelWebUrl() + "angel/consult/wechat/conversation", jsonObject.toString());
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

        public void postByBody(String urlPath, String param) {
            try {
                URL url = new URL(urlPath);
                URLConnection urlConnection = url.openConnection();
                // 设置doOutput属性为true表示将使用此urlConnection写入数据
                urlConnection.setDoOutput(true);
                // 定义待写入数据的内容类型，我们设置为application/x-www-form-urlencoded类型
                urlConnection.setRequestProperty("content-type", "text/plain");
                urlConnection.setRequestProperty("charset", "UTF-8");
                // 得到请求的输出流对象
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                // 把数据写入请求的Body
                out.write(param);
                out.flush();
                out.close();
                // 从服务器读取响应
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = in.readLine()) != null) {
                    line += line;
                }
                System.out.print(line);
                String encoding = urlConnection.getContentEncoding();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        if (EventKey.indexOf("doc") > -1) {
            Map<String, Object> map = wechatInfoDao.getDoctorInfo(EventKey.replace("doc", ""));
            article.setTitle("您已经成功关注" + map.get("hospitalName") + map.get("name") + "医生，点击即可预约");
            article.setDescription("");
            article.setPicUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/images/attentionDoc.jpg");
            article.setUrl(sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/doctorAppointment/" + map.get("id") + ",,,,,doctorShare,,");
            articleList.add(article);
        }
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
                threadExecutorSingle.execute(thread);
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
            }
        } else if (EventKey.indexOf("yufangjiezhong") > -1) {
            //有名片的医生扫码用户,推送文字消息.(扫码有名片医生二维码.)
            Runnable thread = new sendHasCardDoctorWechatMessage(EventKey, xmlEntity);
            threadExecutorSingle.execute(thread);
        } else if (EventKey.indexOf("PD_TPCB") > -1) {
//             SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            LogUtils.saveLog("TPCB_GZTS", xmlEntity.getFromUserName());
            String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=44";
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), "我们都是960万平方公里的一名中国人，\n 当国家安全利益受侵害时，我们更应团结起来一致对外，\n点击链接加入抵制萨德的队伍中，\n<a href='" + url + "'>让外国人看看我们也不是好欺负的>></a>");
        } else if (EventKey.indexOf("PD_YQKFC") > -1) {
//             SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            LogUtils.saveLog("TPCB_GZTS", xmlEntity.getFromUserName());
            String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=44";
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), "点击左下角小键盘，赶快咨询吧~");
        } else if (EventKey.indexOf("WifiConnected") > -1) {
            //该用户链接wifi
            String shopId = xmlEntity.getShopId();
            String openid = xmlEntity.getFromUserName();
            LogUtils.saveLog("WifiConnected", openid + "_" + shopId);
        } else if (EventKey.indexOf("YYHD_BDF_ZJLG") > -1) {
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            String type = (String) redisTemplate.opsForValue().get("husbandCheack" + xmlEntity.getFromUserName());
            String msg = "亲，你咋才来捏~不知道3.15也开始打击“假老公了”吗？想要知道你是不是也嫁了个假老公\n<a href='" + sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appHusband#/husResult/" + type + "'>麻利的赶紧点击链接》》</a>";
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), msg);
        } else if (EventKey.indexOf("PD_HPFX") > -1) {
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            String msg = "亲爱的，体验一次和可爱亲民的医生聊会天吧~  \n" +
                    "点击左下角小键盘立即咨询》";
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), msg);
        }


        String toOpenId = xmlEntity.getFromUserName();//扫码者openid
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("openid", toOpenId);
        List<Map<String, Object>> list1 = babyUmbrellaInfoService.getBabyUmbrellaInfo(param1);

        if (list1.size() == 0) {//用户第一次加入保护伞
            Runnable thread = new sendUBWechatMessage(toOpenId, EventKey);
            threadExecutorSingle.execute(thread);
        } else {
            if ("success".equals(list1.get(0).get("pay_result"))) {
                Runnable thread = new addUserType(toOpenId);
                threadExecutorSingle.execute(thread);
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

    private void send2016StoryEvent(ReceiveXmlEntity xmlEntity, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {

        String marketer = "";
        String openId = xmlEntity.getFromUserName();
        LogUtils.saveLog(openId, "ZX_NYBBDGS");
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String token = (String) userWechatParam.get("token");
        String EventKey = xmlEntity.getEventKey();
        if (StringUtils.isNotNull(EventKey)) {
            marketer = EventKey.replace("qrscene_", "");
        }
        String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=49";

        if (marketer.startsWith("PD_NDGS")) {
            String message = "快来看下2016你和宝宝在宝大夫的秘密故事吧<a href='" + url + "'>》》点击查看</a>";
            WechatUtil.sendMsgToWechat(token, openId, message);
        }
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
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        String newOpenId = xmlEntity.getFromUserName();
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(newOpenId);
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        //新用户从来没有关注过的
        if (wechatAttentionVo == null) {
            if (marketer.startsWith("110")) {
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

                    LogUtils.saveLog("老用户剩余的宝宝币数=" + afterCash + "邀请的好友为：" + newUserNickName, oldOpenId);

//                    WechatUtil.sendMsgToWechat(token, olderUser.getOpenId(), content);
                    String title = "恭喜您成功邀请 " + newUserNickName + " 加入宝大夫，您的您的宝宝币将增加" + cash + "枚！";
                    String templateId = sysPropertyVoWithBLOBsVo.getTemplateIdYWDTTX();
                    String keyword1 = "业务进度：您的宝宝币余额为 " + afterCash + "枚";
                    String keyword2 = "您有" + afterCash / 99 + "次免费咨询专家的机会，本月还可邀请好友" + (20 - olderUser.getInviteNumberMonth()) + "次";
                    String remark = "邀请更多好友加入>，获得更多机会！";
                    String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_MBXX";
//                    WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, oldOpenId, templateId);
                    String templateInfo = "\"first\": {\"value\":" + title + ",\"color\":\"#FF0000\"},\"keynote1\":{ \"value\":" + keyword1 + ",, \"color\":\"#000000\"},\"keynote2\": { \"value\":" + keyword2 + ", \"color\":\"#000000\" }, \"remark\":{ \"value\":" + remark + ",\"color\":\"#FF0000\"}";
                    WechatUtil.sendTemplateMsgToUser(token, oldOpenId, templateId, templateInfo);
                }
            } else if (marketer.startsWith("177") && marketer.length() == 10) {
                RedpackageActivityInfoVo vo = new RedpackageActivityInfoVo();
                vo.setMarket(Integer.valueOf(marketer));
                if (lock.tryLock()) {
                    try {
                        List<RedpackageActivityInfoVo> list = redPackageActivityInfoService.getRedpackageActivityBySelective(vo);
                        if (list != null && list.size() > 0) {
                            vo = list.get(0);
                            int nowCount = vo.getTotalInvitation() + 1;
                            int count;
                            if (nowCount % 2 == 0) {
                                if (nowCount % 10 == 0) {
                                    count = vo.getCardHealth() + 1;
                                    vo.setCardHealth(count);
                                } else {
                                    double ma = Math.random() * 100;
                                    if (ma < 25) {
                                        count = vo.getCardHappy() + 1;
                                        vo.setCardHappy(count);
                                    } else if (ma < 50) {
                                        count = vo.getCardLove() + 1;
                                        vo.setCardLove(count);
                                    } else if (ma < 75) {
                                        count = vo.getCardRuyi() + 1;
                                        vo.setCardRuyi(count);
                                    } else {
                                        count = vo.getCardYoushan() + 1;
                                        vo.setCardYoushan(count);
                                    }
                                }
                            }
                            vo.setTotalInvitation(nowCount);
                            redPackageActivityInfoService.updateByPrimaryKeySelective(vo);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        lock.unlock();   //释放锁
                    }
                } else {
                    System.out.print("获取锁失败");
                }
                // 邀请好友成功发送通知
                String title = "恭喜您有好友加入啦~ ";
                // 到账模板：U-0n4vv3HTXzOE4iD5hZ1siCjbpFVTPpFsXrxs4ASK8
                String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg"; //生产环境 业务动态提醒ID
                String keyword1 = "您已成功邀请" + vo.getTotalInvitation() + "位好友";
                String keyword2 = "已集齐卡片" + vo.getTotalInvitation() / 2 + "张，福利卡" + vo.getCardBig() + "张";
                String remark = "点击立即抽奖";
                String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appWfdb#/myCard";
                WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, vo.getOpenId(), templateId);
            } else if (marketer.startsWith("147") && marketer.length() == 10) {
                StringBuffer msg = new StringBuffer();
                msg.append("福利来啦：\n" +
                        "一起来加入我们赢取大量挑战金吧！\n");
                msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>" + "参加挑战》》</a>");
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                String sendResult = WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), newOpenId, msg.toString());
                LogUtils.saveLog("ZQTZ_XYH", newOpenId+ "--" + msg.toString());
            }else if (marketer.startsWith("188")) {
                //退消息 邀请书加1
                Map parameter = systemService.getBaoEnglishParameter();
                token = (String) parameter.get("token");
                EnglishActivityVo vo = englishActivityService.selectByPrimaryKey(Integer.parseInt(marketer));
                vo.setInviteNum(vo.getInviteNum()+1);
                englishActivityService.updateByPrimaryKeySelective(vo);
                String param = "宝妈你好,你已邀请"+vo.getInviteNum()+"宝妈加入";
                WechatUtil.sendMsgToWechat(token,xmlEntity.getFromUserName(),param);
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

            String welcomeMsg = "\uE022很高兴遇见您！我们是一群有爱又专业的医生朋友。\n\n" +
                    "宝大夫全新用户送您价值18.8元的两次咨询机会\n\n宝贝疾病或者育儿知识都可以哦,既专业又及时~!\uD83D\uDE80\n\n快来体验下吧!点击:<a href='http://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR#rd'>咨询大夫</a>";
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), welcomeMsg);

            String path = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=56";
            welcomeMsg = "\uE022点名咨询医生\n\n问题不急？点他成为你的专属医生哦~\n\n点击：<a href='"+path+"'>点名咨询医生</a>";
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), welcomeMsg);

            welcomeMsg = "\uE022送您一张5元无限制代金券,先到先得哦~\n这里有各大名牌母婴产品，足不出户享最真、最快、最优实惠商品。\n点击：<a href='https://h5.youzan.com/v2/ump/promocard/fetch?alias=194df7nq'>立即领取</a> " +
//                    "还可买到各大名牌母婴产品，足不出户享受最真、最快、最优的实惠商品\uD83D\uDC9D。点击：<a href='https://shop17975201.koudaitong.com/v2/home/4z2f8goy?reft=1477987510078&spm=g305026344&oid=0&sf=wx_sm'>立即购买</a>\n\n" +
                    "\n\n还有更多千万宝宝值得学习的育儿知识哦~   点击：<a href='https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI2MDAxOTY3OQ==&scene=124#wechat_redirect'>学习知识</a>";
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

            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("三甲医院妇儿专家    咨询秒回不等待");
            article.setDescription("小儿内科:       24小时全天 \n\n小儿皮肤科/保健科:   8:00 ~ 23:00\n\n妇产科:   19:00 ~ 21:00\n\n" +
                    "小儿其他专科:   20:00 ~ 21:00\n\n" +
                    "(外科、眼科、耳鼻喉科、口腔科、预防接种科)\n\n点击左下角键盘,输入内容或语音即可咨询");
            article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E9%A6%96%E9%A1%B5.png");
            article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
            articleList.add(article);
            WechatUtil.senImgMsgToWechat(token, xmlEntity.getFromUserName(), articleList);
            memberService.sendExtendOldMemberWechatMessage(xmlEntity.getFromUserName());
        } else if ("37".equals(xmlEntity.getEventKey())) {
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6fewi6uvSqjml1-SXSNZsNBlI", 1);
        } else if ("36".equals(xmlEntity.getEventKey())) {
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            WechatUtil.sendNoTextMsgToWechat(token, xmlEntity.getFromUserName(), "XdHp8YKja_ft7lQr3o6feyWoBTwEtsQ_wxFttviR7cI", 1);
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
            String openid = xmlEntity.getFromUserName();
            String msgContent = roleInfo.getReplyText();
            Query query2 = (new Query()).addCriteria(where("userId").is(openid)).with(new Sort(Sort.Direction.DESC, "createDate"));
            ConsultSessionStatusVo consultSessionStatusVo2 = consultRecordService.findOneConsultSessionStatusVo(query2);
            if (null != consultSessionStatusVo2 && ConsultSession.STATUS_ONGOING.equals(consultSessionStatusVo2.getStatus())) {
                msgContent = msgContent.replace("SESSIONID", consultSessionStatusVo2.getSessionId());
                Map param = new HashMap();
                param.put("userId", consultSessionStatusVo2.getCsUserId());
                param.put("consultSessionId", consultSessionStatusVo2.getSessionId());
                List<Map<String, Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(param);
                if (praiseList != null && praiseList.size() > 0) {
                    for (Map<String, Object> evaluationMap : praiseList) {
                        if (Integer.parseInt((String) evaluationMap.get("serviceAttitude")) == 0) {
                            msgContent = msgContent.replace("CUSTOMERID", (String) evaluationMap.get("id"));
                            break;
                        }
                    }
                }
            }
            LogUtils.saveLog("ZXPJXX_GJZHF_" + roleInfo.getReplyText(), openid);
            WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), msgContent);
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
     */
    public boolean nonRealTimeCheck(String whitelist, ReceiveXmlEntity xmlEntity, String token) {
        String openid = xmlEntity.getFromUserName();
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        System.out.println("白名单:" + whitelist);
        System.out.println(whitelist.indexOf(openid) == -1);
        if (StringUtils.isNotNull(whitelist) && whitelist.indexOf(openid) == -1) {
            return false;
        }
        ConsultSessionPropertyVo propertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(openid);

        String path = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=40";
        //所有机会都用完
        if ((propertyVo.getPermTimes() + propertyVo.getMonthTimes()) == 0) {

            String payContent = "亲爱的~你本月免费机会已用完，请医生喝杯茶，继续咨询\n\n" +
                    "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=35'>>>付费</a>";
            WechatUtil.sendMsgToWechat(token, openid, payContent);

            String invatUrl = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_WXCD";

            String bayContent = "什么？咨询要收费？\n不怕！邀请个好友加入宝大夫，免费机会立刻有！\n" +
                    "<a href='" + invatUrl + "'>>>邀请好友赚机会</a>";
            WechatUtil.sendMsgToWechat(token, openid, bayContent);

            String content = "问题不着急？\n试试给医生留言咨询吧， 医生会尽快回复哦\n" +
                    "<a href='" + path + "'>>>图文咨询</a>";
            WechatUtil.sendMsgToWechat(token, openid, content);

            LogUtils.saveLog("FSS_YHD_RK1_TS", openid);

            return true;
        }
        ConsultSession consultInfo = consultConversationService.selectByOpenid(openid);
//        首次
        if ((propertyVo.getMonthTimes() == 4 && propertyVo.getPermTimes() == 0 && consultInfo.getConsultNumber() > 26)) {
            String payContent = "亲爱的~你本月免费机会已用完，请医生喝杯茶，继续咨询\n\n" +
                    "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=35'>>>付费</a>";
            WechatUtil.sendMsgToWechat(token, openid, payContent);


            String content = "不想掏钱？\n来试试“点名咨询”。你可指定专家医生或曾咨询过的医生， 医生会在24h 内尽快对您的提问进行答复哦~\n（如有疑问，可直接拨打400-6237-120）\n" +
                    "<a href='" + path + "'>>>点名咨询医生</a>";
            WechatUtil.sendMsgToWechat(token, openid, content);

            LogUtils.saveLog("FSS_YHD_RK3_TS", openid);
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
