package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultVoiceRecordMongoServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 会员Controller
 *
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/wechat")
public class ConsultWechatController extends BaseController {

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultPayUserService consultPayUserService;

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    private ConsultVoiceRecordMongoServiceImpl consultVoiceRecordMongoService;

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> conversation(HttpServletRequest request,
                                     @RequestParam(required = true) String openId,
                                     @RequestParam(required = true) String messageType,
                                     @RequestParam(required = false) String messageContent,
                                     @RequestParam(required = false) String mediaId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String, Object> result = new HashMap<String, Object>();
        System.out.println("openId=" + openId);
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("openId", openId);
        paramMap.put("messageType", messageType);
        paramMap.put("messageContent", messageContent);
        if (messageType.contains("voice") || messageType.contains("video") || messageType.contains("image")) {
            paramMap.put("mediaId", mediaId);
        }
        paramMap.put("serverAddress", ConstantUtil.SERVER_ADDRESS);
        Runnable thread = new processUserMessageThread(paramMap);
        threadExecutor.execute(thread);

        result.put("status", "success");
        return result;
    }

    public class processUserMessageThread extends Thread {
        private HashMap<String, Object> param;

        public processUserMessageThread(HashMap<String, Object> paramMap) {
            this.param = paramMap;
        }

        public void run() {

            //需要根据openId获取到nickname，如果拿不到nickName，则用利用openId换算出一个编号即可
            String openId = (String) this.param.get("openId");
            String messageType = (String) this.param.get("messageType");
            String messageContent = (String) this.param.get("messageContent");
            String serverAddress = (String) this.param.get("serverAddress");

            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(openId);
            SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);

            String userName = openId;
            String userId = openId;
            if (openId.length() > 20) {
                userName = openId.substring(openId.length() - 8, openId.length());
            } else {
                userName = openId.substring(0, 5);
                userId = openId.substring(0, 5);
            }

            if (wechatAttentionVo != null) {
                if (StringUtils.isNotNull(wechatAttentionVo.getWechat_name())) {
                    userName = wechatAttentionVo.getWechat_name();
                }
            }
            //此处，将openId统一转换成userId，以后，在咨询系统中，所有表示咨询用户唯一来源的id统一都用userId表示，
            // 在所有的日志记录，还是缓存中，所有的会话，都引入一个字段，source，标示，这个会话，
            // 是基于微信，还是H5，还是合作第三方的来源，以便按照不同的逻辑来处理。
            String source = "wxcxqm";
            Integer notifyType = 1001;

            Channel csChannel = null;
            //根据用户的openId，判断redis中，是否有用户正在进行的session
            Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
            System.out.println("sessionId------" + sessionId);
            HashMap<String, Object> createWechatConsultSessionMap = null;
            RichConsultSession consultSession = new RichConsultSession();

            //如果此用户不是第一次发送消息，则sessionId不为空
            if (sessionId != null) {
                //检测是否给用户推送以下消息-- 每个会话只推一次(您需要花点时间排队，请耐心等待哦)
                String sessionList = consultPayUserService.getChargeInfo(sessionId);
                if (null == sessionList && consultPayUserService.angelChargeCheck(openId)) {
                    consultPayUserService.saveChargeUser(sessionId, openId);
                    consultPayUserService.sendMessageToConsult(openId, 4);
                }
                consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
                System.out.println("csChannel------" + csChannel);
                if (csChannel == null) {
                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                    //更新会话操作时间
                    consultRecordService.saveConsultSessionStatus(consultSession);
                } else {
                    System.out.println("csChannel.isActive()------" + csChannel.isActive());
                    if (!csChannel.isActive()) {
                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                        //更新会话操作时间
                        consultRecordService.saveConsultSessionStatus(consultSession);
                    }
                }
            } else {
                //如果此用户是第一次发送消息，则sessionId为空
                consultSession.setCreateTime(new Date());
                consultSession.setUserId(userId);
                consultSession.setUserName(userName);
                consultSession.setSource(source);
                consultSession.setServerAddress(serverAddress);
                //创建会话，发送消息给用户，给用户分配接诊员
                createWechatConsultSessionMap = ConsultSessionManager.getSessionManager().createUserWXConsultSession(consultSession);
                if (createWechatConsultSessionMap != null) {
                    csChannel = (Channel) createWechatConsultSessionMap.get("csChannel");
                    consultSession = (RichConsultSession) createWechatConsultSessionMap.get("consultSession");
                    sessionId = consultSession.getId();
                }
                //咨询收费处理
                consultCharge(openId, sessionId, consultSession);
            }

            //会话创建成功，拿到了csChannel,给接诊员(或是医生)发送消息
            if (csChannel != null && csChannel.isActive()) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sessionId", sessionId);
                    obj.put("senderId", userId);
                    obj.put("dateTime", DateUtils.DateToStr(new Date()));
                    obj.put("senderName", userName);
                    obj.put("notifyType", notifyType);
                    obj.put("serverAddress", serverAddress);
                    System.out.println("serverAddress------" + serverAddress);
                    obj.put("source", consultSession.getSource());

                    StringBuffer sbf = new StringBuffer();
                    if (messageType.equals("text")) {
                        obj.put("type", 0);
                        //将模拟微信端发过来的信息内容进行修改
                        if (openId.length() <= 11) {
                            String[] openIdArr = openId.split("-");
                            sbf.append(openIdArr[0]).append("<--->").append(consultSession.getCsUserId());
                            sbf.append("<--->").append(openIdArr[1]);
                            messageContent = sbf.toString();
                            System.out.println(messageContent);
                        }
                        obj.put("content", URLDecoder.decode(messageContent, "utf-8"));

                    } else {
                        if (messageType.contains("image")) {
                            obj.put("type", 1);
                        } else if (messageType.contains("voice")) {
                            obj.put("type", 2);
                        } else if (messageType.contains("video")) {
                            obj.put("type", 3);
                        }
                        //收到语音，发送通知给用户，提示为了更好地咨询，最好文字聊天
                        //根据mediaId，从微信服务器上，获取到媒体文件，再将媒体文件，放置阿里云服务器，获取URL
                        if (messageType.contains("voice") || messageType.contains("video") || messageType.contains("image")) {
                            messageContent = voiceHandle(messageType, messageContent, sessionId, consultSession, obj);
                        }
                    }
                    System.out.println("here csChannel is" + csChannel);
                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());

                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);

                    //更新会话操作时间
                    consultRecordService.saveConsultSessionStatus(consultSession);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }

        private String voiceHandle(String messageType, String messageContent, Integer sessionId, RichConsultSession consultSession, JSONObject obj) {
            try {
                WechatUtil wechatUtil = new WechatUtil();
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                String mediaURL = wechatUtil.downloadMediaFromWx((String) userWechatParam.get("token"),
                        (String) this.param.get("mediaId"), messageType);
                obj.put("content", mediaURL);
                messageContent = mediaURL;
                if (messageType.contains("voice")) {
                    ConsultVoiceRecordMongoVo consultVoiceRecordMongoVo = new ConsultVoiceRecordMongoVo();
                    consultVoiceRecordMongoVo.setCsUserId(consultSession.getCsUserId());
                    consultVoiceRecordMongoVo.setSessionId(sessionId);
                    long consultCount = consultVoiceRecordMongoService.countConsultByVoice(consultVoiceRecordMongoVo);
                    if (consultCount < 1) {
                        consultVoiceRecordMongoVo.setCreateDate(new Date());
                        consultVoiceRecordMongoVo.setType(messageType);
                        consultVoiceRecordMongoVo.setUserId(consultSession.getUserId());
                        consultVoiceRecordMongoVo.setCsUserName(consultSession.getCsUserName());
                        consultVoiceRecordMongoVo.setUserName(consultSession.getUserName());
                        consultVoiceRecordMongoVo.setContent(mediaURL);
                        consultVoiceRecordMongoService.insert(consultVoiceRecordMongoVo);
                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), consultSession.getUserId(), "亲亲，语音会影响医生的判断哦，为了您的咨询更准确，要用文字提问呦~");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return messageContent;
        }

        private void consultCharge(String openId, Integer sessionId, RichConsultSession richConsultSession) {
            //检测用户是否是收费用户,1001 为正常用户(无标签) ,1002 需要付款用户(等待),1003 已付款用户;
//                try {
//                    if (consultPayUserService.angelChargeCheck(openId)) {
//                        ConcurrentHashMap<String, Object> payInfo = new ConcurrentHashMap<String, Object>();
//                        payInfo.put(openId, new Date());
//                        consultPayUserService.putneepPayConsultSession(consultSession.getCsUserId(), payInfo);
//                        notifyType = 1002;
//                        int type = Integer.parseInt(Global.getConfig("consultPayMsgType"));
//                        consultPayUserService.sendMessageToConsult(openId, type);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            //判断上次收费咨询是不是在24个小时以内 zdl
            Query query = new Query(new Criteria().where("userId").is(openId))
                    .with(new Sort(Sort.Direction.ASC, "firstTransTime")).limit(1);
            List<ConsultSessionStatusVo> consultSessionStatusVos = consultRecordService.queryUserMessageList(query);
            if(consultSessionStatusVos == null){
                ConsultSessionPropertyVo consultSessionPropertyVo = new ConsultSessionPropertyVo();
                consultSessionPropertyVo.setSysUserId(richConsultSession.getUserId());
                consultSessionPropertyVo.setCreateBy(richConsultSession.getUserId());
                consultSessionPropertyVo.setCreateTime(new Date());
                consultSessionPropertyVo.setMonthTimes(4);
                consultSessionPropertyVo.setPermTimes(0);
                consultSessionPropertyService.insertUserConsultSessionProperty(consultSessionPropertyVo);
            }
            if (null != consultSessionStatusVos && consultSessionStatusVos.size() > 0 && consultSessionStatusVos.get(0).getFirstTransTime()!=null) {
                long pastMillisSecond = DateUtils.pastMillisSecond(consultSessionStatusVos.get(0).getFirstTransTime());
                if (pastMillisSecond < 24 * 60 * 60 * 1000) {
                    richConsultSession.setPayStatus(ConstantUtil.WITHIN_24HOURS);
                } else {
                    Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                    String token = (String) userWechatParam.get("token");
                    String sysUserId = richConsultSession.getUserId();
                    //判断剩余次数,consultSessionStatusVo打标记
                    ConsultSessionPropertyVo consultSessionPropertyVo = new ConsultSessionPropertyVo();
                    consultSessionPropertyVo.setSysUserId(richConsultSession.getUserId());
                    ConsultSessionPropertyVo propertyVo = consultPayUserService.selectUserSessionPropertyByVo(consultSessionPropertyVo);
                    if (propertyVo != null ) {
                        String content ;
                        richConsultSession.setPayStatus(ConstantUtil.USE_TIMES);
                        if(propertyVo.getMonthTimes()>0){
                            content = "嗨，亲爱的，你本月还剩"+propertyVo.getMonthTimes()+"次免费咨询的机会" + "每次咨询24小时内有效^_^\n";
                        }else if(propertyVo.getPermTimes()>0){
                            content = "嗨，亲爱的，你还剩"+propertyVo.getMonthTimes()+"次永久免费咨询的机会" + "每次咨询24小时内有效^_^\n";
                        }else{
                            richConsultSession.setPayStatus(ConstantUtil.NO_PAY);
                            content = "嗨，亲爱的，本次咨询医生需要支付9.9元，享受24小时咨询时间\n" +
                                    ">>付费" + "<a href='http://120.25.161.33/keeper/wxPay/patientPay.do?serviceType=customerPay" + "&sessionId=" + sessionId + "'>点击这里去评价</a>" + "\n" +
                                    "-----------\n" +
                                    "求助客服请直接向分诊说明，不需付费\n";
                            WechatUtil.sendMsgToWechat(token,sysUserId, content);
                        }
                        WechatUtil.sendMsgToWechat(token, sysUserId, content);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/notifyPayInfo2Distributor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> notifyPayInfo2Distributor(@RequestParam(required = true) String openId) {
        Channel csChannel = null;
        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(openId);
        System.out.println("sessionId------" + sessionId);
//        HashMap<String, Object> createWechatConsultSessionMap = null;
//        RichConsultSession consultSession = new RichConsultSession();
        RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
        System.out.println("csChannel------" + csChannel);
//        consultPayUserService.removePayConsultSession(consultSession.getCsUserId(), openId);
//更新最后一次会话
        consultRecordService.updateConsultSessionStatusVo(new Query().addCriteria(new Criteria().where("sessionId").is(sessionId)), "complete");

        if (csChannel != null && csChannel.isActive()) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                obj.put("notifyType", "1001");
                obj.put("type", "4");
                obj.put("source", consultSession.getSource());
                TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                csChannel.writeAndFlush(frame.retain());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    ;


    /**
     * 只咨询客服调用此接口通知前台修改标示
     */
    @RequestMapping(value = "/consultCustomOnly", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> consultCustomOnly(HttpSession session, HttpServletRequest request) {
        String openId = WechatUtil.getOpenId(session, request);
        Channel csChannel = null;
        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(openId);
        System.out.println("sessionId------" + sessionId);
        HashMap<String, Object> createWechatConsultSessionMap = null;
        RichConsultSession consultSession = new RichConsultSession();
        if (null != sessionId) {
            consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
            consultPayUserService.removePayConsultSession(consultSession.getCsUserId(), openId);

            consultPayUserService.saveChargeUser(sessionId, openId);

            csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
            System.out.println("csChannel------" + csChannel);
            if (csChannel != null && csChannel.isActive()) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sessionId", sessionId);
                    obj.put("senderId", openId);
                    obj.put("dateTime", DateUtils.DateToStr(new Date()));
                    obj.put("notifyType", "1001");
                    obj.put("type", "4");
                    obj.put("source", consultSession.getSource());
                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    ;

    @RequestMapping(value = "/getneedPaylist", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getList(@RequestParam(required = true) String csuserId) {
        return consultPayUserService.getneepPayConsultSession(csuserId);
    }

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void test() {
        List<Object> objectList = sessionRedisCache.getSessionIdByKey();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        for (Object o : objectList){
           Integer sessionId = (Integer)o;
            ConsultSession consultSession = consultSessionService.selectByPrimaryKey(sessionId);
            if(consultSession!=null && consultSession.getCreateTime().getTime() < (calendar.getTimeInMillis())){
                sessionRedisCache.removeConsultSessionBySessionId(consultSession.getId());
                sessionRedisCache.removeUserIdSessionIdPair(consultSession.getUserId());
            }
        }

    }
}