package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultVoiceRecordMongoServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * 微信咨询Controller
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

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private ConsultBadEvaluateRemindUserService consultBadEvaluateRemindUserService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    private static ExecutorService cacheExecutor = Executors.newCachedThreadPool();

    @Autowired
    private ConsultVoiceRecordMongoServiceImpl consultVoiceRecordMongoService;

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> conversation(HttpServletRequest request, @RequestBody String body) throws InterruptedException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        JSONObject json = JSONObject.parseObject(body);
        String openId = (String) json.get("openId");
        String messageType = (String) json.get("messageType");
        String messageContent = "";
        try {
            messageContent = json.get("messageContent") != null ? URLDecoder.decode((String) json.get("messageContent"), "UTF-8") : "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String mediaId = (String) json.get("mediaId");

        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        if (openId.indexOf(",") != -1) {
            openId = openId.split(",")[0];
        }
        if (messageType.indexOf(",") != -1) {
            messageType = messageType.split(",")[0];
        }
        if (mediaId != null && mediaId.indexOf(",") != -1) {
            mediaId = mediaId.split(",")[0];
        }
        paramMap.put("openId", openId);
        paramMap.put("messageType", messageType);
        paramMap.put("messageContent", messageContent);


        LogUtils.saveLog(openId, "咨询进入openId为" + openId);
        LogUtils.saveLog(openId, "咨询进入messageType为" + messageType);
        LogUtils.saveLog(openId, "咨询进入messageContent为" + messageContent);


        if (messageType.contains("voice") || messageType.contains("video") || messageType.contains("image")) {
            paramMap.put("mediaId", mediaId);
        }
        LogUtils.saveLog(openId, "1");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openId);
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        LogUtils.saveLog(openId, "2");
        try {
            String locaHostIp = HttpUtils.getRealIp(sysPropertyVoWithBLOBsVo);
            LogUtils.saveLog(openId, "3 locaHostIp" + locaHostIp);
            paramMap.put("serverAddress", locaHostIp);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        LogUtils.saveLog(openId, "4");
        Runnable thread = new processUserMessageThread(paramMap, sysPropertyVoWithBLOBsVo, wechatAttentionVo);
        threadExecutor.execute(thread);
        LogUtils.saveLog(openId,"推送消息完成");
        result.put("status", "success");
        return result;
    }

    public class processUserMessageThread extends Thread {
        private HashMap<String, Object> param;
        private SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo;
        private SysWechatAppintInfoVo wechatAttentionVo;

        public processUserMessageThread(HashMap<String, Object> paramMap, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, SysWechatAppintInfoVo wechatAttentionVo) {
            this.param = paramMap;
            LogUtils.saveLog((String) this.param.get("openId"), "构造方法");
            this.sysPropertyVoWithBLOBsVo = sysPropertyVoWithBLOBsVo;
            this.wechatAttentionVo = wechatAttentionVo;
        }

        public void run() {

            LogUtils.saveLog((String) this.param.get("openId"), "开始线程");
            //需要根据openId获取到nickname，如果拿不到nickName，则用利用openId换算出一个编号即可
            String openId = (String) this.param.get("openId");
            LogUtils.saveLog(openId, "44");
            String messageType = (String) this.param.get("messageType");
            LogUtils.saveLog(openId, "5");
            String messageContent = (String) this.param.get("messageContent");
            LogUtils.saveLog(openId, "6");
            String serverAddress = (String) this.param.get("serverAddress");
            LogUtils.saveLog(openId, "开始处理消息single" + openId + "  " + messageType + "  " + messageContent + " " + serverAddress);
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
            Integer consultTimes = 0;
            Channel csChannel = null;
            //根据用户的openId，判断redis中，是否有用户正在进行的session
            LogUtils.saveLog(openId, "开始从redis取sessionId");
            Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
            LogUtils.saveLog(openId, "从redis取sessionId完成" + sessionId);
            HashMap<String, Object> createWechatConsultSessionMap = null;
            RichConsultSession consultSession = new RichConsultSession();

            //如果此用户不是第一次发送消息，则sessionId不为空
            if (sessionId != null) {
                LogUtils.saveLog(userId, "用户不是第一次发送消息");
                //检测是否给用户推送以下消息-- 每个会话只推一次(您需要花点时间排队，请耐心等待哦)
                String sessionList = consultPayUserService.getChargeInfo(sessionId);
                if (null == sessionList && consultPayUserService.angelChargeCheck(openId)) {
                    consultPayUserService.saveChargeUser(sessionId, openId);
                    consultPayUserService.sendMessageToConsult(openId, 4);
                }
                LogUtils.saveLog(openId, "根据sessionId取consultSession" + sessionId);
                consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                LogUtils.saveLog(openId, "根据sessionId取consultSession完成" + sessionId);

                /**
                 * 2016-9-8 17:42:43 jiangzg 增加消息数量
                 */
                if (consultSession != null) {
                    int currentNum = consultSession.getConsultNum() + 1;
                    consultSession.setConsultNum(currentNum);
                    LogUtils.saveLog(openId, "增加消息数量" + sessionId);
                    sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
                    LogUtils.saveLog(openId, "增加消息数量完成" + sessionId);
                }
                csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
                LogUtils.saveLog(openId, "该用户分配的医生为：" + consultSession.getCsUserId() + consultSession.getCsUserName());

                if (csChannel == null) {
                    LogUtils.saveLog(openId, "csChannel为空，保存聊天记录");
                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                    //更新会话操作时间
                    consultRecordService.saveConsultSessionStatus(consultSession);
                    LogUtils.saveLog(openId, "更新会话操作时间结束");
                } else {
                    if (!csChannel.isActive()) {
                        LogUtils.saveLog(openId, "csChannel is notActive，保存聊天记录");
                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                        //更新会话操作时间
                        consultRecordService.saveConsultSessionStatus(consultSession);
                        LogUtils.saveLog(openId, "csChannel is notActive，保存聊天记录结束");

                    }
                }
            } else {
                //如果此用户是第一次发送消息，则sessionId为空
                LogUtils.saveLog(userId, "用户第一次发送消息");

                consultSession.setCreateTime(new Date());
                consultSession.setUserId(userId);
                consultSession.setUserName(userName);
                consultSession.setSource(source);
                consultSession.setServerAddress(serverAddress);
                /**
                 * 新增咨询次数字段 jiangzg 2016-9-8 17:33:17
                 */
                consultSession.setConsultNum(1);
                //创建会话，发送消息给用户，给用户分配接诊员
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                String token = (String) userWechatParam.get("token");
                String whiteNameStr = sysPropertyVoWithBLOBsVo.getWhiteNameList();
                String consultNumStr = sysPropertyVoWithBLOBsVo.getConsultLimitNum();

                LogUtils.saveLog(openId, " " + token + " " + whiteNameStr + " " + consultNumStr);

                int consultLimitNum = 24;
                if (StringUtils.isNotNull(consultNumStr)) {
                    consultLimitNum = Integer.valueOf(consultNumStr);
                }
                if (StringUtils.isNotNull(whiteNameStr)) {
                    LogUtils.saveLog(openId, "whiteNameStr ：" + whiteNameStr);
                    if (whiteNameStr.contains(openId)) {
                        LogUtils.saveLog(openId, "whiteNameStr ：openId包含在白名单中");
                        ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(openId);
                        if (consultSessionPropertyVo != null) {
                            if (consultSessionPropertyVo.getMonthTimes() > 0) {
                                Query query = (new Query()).addCriteria(where("userId").is(openId)).with(new Sort(Sort.Direction.DESC, "createDate"));
                                List<ConsultSessionStatusVo> consultSessionStatusVoList = consultRecordService.getConsultSessionStatusVo(query);
                                if (consultSessionStatusVoList != null && consultSessionStatusVoList.size() > 0) {
                                    if (consultSessionStatusVoList.size() > consultLimitNum) {
                                        HashMap praiseParam = new HashMap();
                                        Map praiseParamMap = new HashMap();
                                        praiseParam.put("sessionId", Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                        praiseParam.put("openId", openId);
                                        if (StringUtils.isNotNull(consultSessionStatusVoList.get(0).getCsUserId())) {
                                            String[] csUserIds = consultSessionStatusVoList.get(0).getCsUserId().toString().split(" ");
                                            Map status = new HashMap();
                                            status.put("state", "no");
                                            for (int j = 0; j < csUserIds.length; j++) {
                                                praiseParam.put("doctorId", csUserIds[j]);
                                                List<HashMap<String, Object>> praiseList = consultBadEvaluateRemindUserService.selectConsultStatisticVoByMap(praiseParam);
                                                if (praiseList != null && !"0".equalsIgnoreCase(String.valueOf(praiseList.get(0).get("serviceAttitude")))) {
                                                    status.put("state", "yes");
                                                    break;
                                                }
                                            }
                                            if ("yes".equalsIgnoreCase(String.valueOf(status.get("state")))) {
                                                createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                            } else {
                                                praiseParamMap.put("consultSessionId", Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                                List<Map<String, Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParamMap);
                                                if (sysPropertyVoWithBLOBsVo.getDistributorList().contains(String.valueOf(praiseList.get(0).get("doctorId")))) {
                                                    createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                                } else {
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append("<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wxPay/patientPay.do?consultStatus=wantConsult&serviceType=customerPay&customerId=");
                                                    stringBuilder.append(praiseList.get(0).get("id"));
                                                    stringBuilder.append("&sessionId=");
                                                    stringBuilder.append(Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                                    stringBuilder.append("'>评价医生>></a>");
                                                    String textMsg = sysPropertyVoWithBLOBsVo.getPushNeedEvaluateMsgToUser();
                                                    if (StringUtils.isNull(textMsg)) {
                                                        textMsg = "亲爱的，请为上次的服务做出评价，评价后才可以继续咨询哦~";
                                                    }
                                                    textMsg = textMsg + "\n" + stringBuilder.toString();
                                                    LogUtils.saveLog("ZXPJ_PJYS", openId);
                                                    WechatUtil.sendMsgToWechat(token, openId, textMsg);
                                                    return;
                                                }
                                            }
                                        } else {
                                            createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                        }
                                    } else {
                                        createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                    }
                                } else {
                                    createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                }
                            } else {
                                createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                            }
                        } else {
                            createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                        }
                    } else {
                        createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                    }
                } else {
                    ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(openId);
                    LogUtils.saveLog(openId, "咨询属性");
                    if (consultSessionPropertyVo != null) {
                        if (consultSessionPropertyVo.getMonthTimes() > 0) {
                            Query query = (new Query()).addCriteria(where("userId").is(openId)).with(new Sort(Sort.Direction.DESC, "createDate"));
                            List<ConsultSessionStatusVo> consultSessionStatusVoList = consultRecordService.getConsultSessionStatusVo(query);
                            if (consultSessionStatusVoList != null && consultSessionStatusVoList.size() > 0) {
                                if (consultSessionStatusVoList.size() > consultLimitNum) {
                                    HashMap praiseParam = new HashMap();
                                    Map praiseParamMap = new HashMap();
                                    praiseParam.put("sessionId", Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                    praiseParam.put("openId", openId);
                                    if (StringUtils.isNotNull(consultSessionStatusVoList.get(0).getCsUserId())) {
                                        String[] csUserIds = consultSessionStatusVoList.get(0).getCsUserId().toString().split(" ");
                                        Map status = new HashMap();
                                        status.put("state", "no");
                                        for (int j = 0; j < csUserIds.length; j++) {
                                            praiseParam.put("doctorId", csUserIds[j]);
                                            List<HashMap<String, Object>> praiseList = consultBadEvaluateRemindUserService.selectConsultStatisticVoByMap(praiseParam);
                                            if (praiseList != null && !"0".equalsIgnoreCase(String.valueOf(praiseList.get(0).get("serviceAttitude")))) {
                                                status.put("state", "yes");
                                                break;
                                            }
                                        }
                                        if ("yes".equalsIgnoreCase(String.valueOf(status.get("state")))) {
                                            createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                        } else {
                                            praiseParamMap.put("consultSessionId", Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                            List<Map<String, Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParamMap);
                                            if (sysPropertyVoWithBLOBsVo.getDistributorList().contains(String.valueOf(praiseList.get(0).get("doctorId")))) {
                                                createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                            } else {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wxPay/patientPay.do?consultStatus=wantConsult&serviceType=customerPay&customerId=");
                                                stringBuilder.append(praiseList.get(0).get("id"));
                                                stringBuilder.append("&sessionId=");
                                                stringBuilder.append(Integer.valueOf(consultSessionStatusVoList.get(0).getSessionId()));
                                                stringBuilder.append("'>评价医生>></a>");
                                                String textMsg = sysPropertyVoWithBLOBsVo.getPushNeedEvaluateMsgToUser();
                                                if (StringUtils.isNull(textMsg)) {
                                                    textMsg = "亲爱的，请为上次的服务做出评价，评价后才可以继续咨询哦~";
                                                }
                                                textMsg = textMsg + "\n" + stringBuilder.toString();
                                                WechatUtil.sendMsgToWechat(token, openId, textMsg);
                                                LogUtils.saveLog("ZXPJ_PJYS", openId);
                                                return;
                                            }
                                        }
                                    } else {
                                        createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                    }
                                } else {
                                    createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                                }
                            } else {
                                createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                            }
                        } else {
                            createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                        }
                    } else {
                        createWechatConsultSessionMap = ConsultSessionManager.INSTANCE.createUserWXConsultSession(consultSession);
                    }
                }

                if (createWechatConsultSessionMap != null) {
                    csChannel = (Channel) createWechatConsultSessionMap.get("csChannel");
                    consultSession = (RichConsultSession) createWechatConsultSessionMap.get("consultSession");
                    sessionId = consultSession.getId();
                }
                //咨询收费处理
                consultTimes = consultCharge(openId, sessionId, consultSession, sysPropertyVoWithBLOBsVo);
                LogUtils.saveLog(openId, "咨询收费处理结束");
                sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
                LogUtils.saveLog(openId, "putSessionIdConsultSessionPair");
                sessionRedisCache.putUserIdSessionIdPair(consultSession.getUserId(), sessionId);
                LogUtils.saveLog(openId, "putUserIdSessionIdPair");
            }

            //会话创建成功，拿到了csChannel,给接诊员(或是医生)发送消息
            if (csChannel != null) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sessionId", sessionId);
                    obj.put("senderId", userId);
                    obj.put("dateTime", DateUtils.DateToStr(new Date()));
                    obj.put("senderName", userName);
                    if (ConstantUtil.PAY_SUCCESS.getVariable().indexOf(consultSession.getPayStatus()) > -1) {
                        obj.put("notifyType", "1001");
                    } else if (ConstantUtil.NO_PAY.getVariable().indexOf(consultSession.getPayStatus()) > -1) {
                        obj.put("notifyType", "1002");
                    } else if (ConstantUtil.NOT_INSTANT_CONSULTATION.getVariable().indexOf(consultSession.getPayStatus()) > -1) {
                        obj.put("notifyType", "1003");
                    } else {
                        obj.put("notifyType", "1004");
                    }

                    obj.put("serverAddress", serverAddress);
                    obj.put("source", consultSession.getSource());
                    LogUtils.saveLog(openId, "发送给医生的消息为：" + obj.toJSONString());
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
                        /**
                         * jiangzg add 2016-9-8 17:44:45 增加消息数量
                         */
                        LogUtils.saveLog(openId, "消息数量为" + consultSession.getConsultNum());
                        obj.put("consultNum", consultSession.getConsultNum());
                        TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                        csChannel.writeAndFlush(frame.retain());
                        LogUtils.saveLog(openId, "消息推送给医生结束");

                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                        LogUtils.saveLog(consultSession.getUserId(), "保存ConsultSessionStatusVo");
                        consultRecordService.saveConsultSessionStatus(consultSession);
                        LogUtils.saveLog(consultSession.getUserId(), "保存ConsultSessionStatusVo成功！");

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

                            Runnable cthread = new Thread(new MultiSendMsg(openId,csChannel,userId,messageType, messageContent, sessionId, consultSession, obj, sysPropertyVoWithBLOBsVo,param));
                            try {
                                Future future = cacheExecutor.submit(cthread);
                                if(future!=null){
                                    future.get(5000, TimeUnit.MILLISECONDS);
                                    if(!future.isDone()){
                                        future.cancel(true);
                                        LogUtils.saveLog(openId,"任务是否已经取消"+future.isCancelled());
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }

        }
        private class MultiSendMsg implements Runnable{

            private String openId ;
            private Channel csChannel;
            private String userId ;
            private String messageType ;
            private String messageContent ;
            private Integer sessionId ;
            private  RichConsultSession consultSession ;
            private JSONObject obj ;
            private SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo ;
            private HashMap param ;

            public MultiSendMsg( String openId , Channel csChannel ,String userId , String messageType, String messageContent, Integer sessionId, RichConsultSession consultSession, JSONObject obj, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo,HashMap param ){
                this.openId = openId ;
                this.csChannel = csChannel ;
                this.userId = userId;
                this.messageType = messageType ;
                this.messageContent = messageContent ;
                this.consultSession = consultSession ;
                this.sessionId = sessionId ;
                this.obj = obj ;
                this.sysPropertyVoWithBLOBsVo = sysPropertyVoWithBLOBsVo ;
                this.param = param ;
            }

            private String voiceHandle(String openId , Channel csChannel ,String userId , String messageType, String messageContent, Integer sessionId, RichConsultSession consultSession, JSONObject obj, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo,HashMap param) {
                try {
                    WechatUtil wechatUtil = new WechatUtil();
                    Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                    String mediaURL = wechatUtil.downloadMediaFromWx((String) userWechatParam.get("token"),
                            (String) this.param.get("mediaId"), messageType, sysPropertyVoWithBLOBsVo);
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
                    /**
                     * jiangzg add 2016-9-8 17:44:45 增加消息数量
                     */
                    LogUtils.saveLog(openId, "消息数量为" + consultSession.getConsultNum());
                    obj.put("consultNum", consultSession.getConsultNum());
                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());
                    LogUtils.saveLog(openId, "消息推送给医生结束");

                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                    LogUtils.saveLog(consultSession.getUserId(), "保存ConsultSessionStatusVo");
                    consultRecordService.saveConsultSessionStatus(consultSession);
                    LogUtils.saveLog(consultSession.getUserId(), "保存ConsultSessionStatusVo成功！");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return messageContent;
            }

            @Override
            public void run() {
                voiceHandle(openId,csChannel,userId,messageType, messageContent, sessionId, consultSession, obj, sysPropertyVoWithBLOBsVo,param);
            }
        }

        private Integer consultCharge(String openId, Integer sessionId, RichConsultSession richConsultSession, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {

            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String token = (String) userWechatParam.get("token");
            //查询最近的一次咨询状态
            Query query = new Query(new Criteria().where("userId").is(openId))
                    .with(new Sort(Sort.Direction.DESC, "firstTransTime")).limit(1);
            List<ConsultSessionStatusVo> consultSessionStatusVos = consultRecordService.queryUserMessageList(query);
            richConsultSession.setPayStatus(ConstantUtil.USE_TIMES.getVariable());
            String chargeType = "dis";
            int messageFlag = 0;
            Integer monthTime = 0;
            ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(richConsultSession.getUserId());

            //首次咨询
            if (consultSessionPropertyVo == null) {
                chargeType = "mt";
                consultSessionPropertyVo = new ConsultSessionPropertyVo();
                consultSessionPropertyVo.setCreateTime(new Date());
                consultSessionPropertyVo.setMonthTimes(Integer.parseInt(sysPropertyVoWithBLOBsVo.getFreeConsultNum()));
                consultSessionPropertyVo.setPermTimes(0);
                consultSessionPropertyVo.setSysUserId(openId);
                consultSessionPropertyVo.setCreateBy(openId);
                consultSessionPropertyService.insertUserConsultSessionProperty(consultSessionPropertyVo);
                monthTime = consultSessionPropertyVo.getMonthTimes();
//                String content = "嗨，亲爱的，你本月还可享受" + monthTime + "次24小时咨询服务哦^-^";
                messageFlag = 1;
//                WechatUtil.sendMsgToWechat(token, openId, content);
                onlyDoctorOnlineHandle(richConsultSession, consultSessionPropertyVo);
                if (monthTime == 1) {
                    LogUtils.saveLog("ZXYQ_RK_TS_1", openId);
                } else if (monthTime == 4) {
                    LogUtils.saveLog("ZXYQ_RK_TS_2", openId);
                }
            }
            //没有接入过医生
            monthTime = consultSessionPropertyVo.getMonthTimes();
            if (null == consultSessionStatusVos || consultSessionStatusVos.size() == 0 || consultSessionStatusVos.get(0).getFirstTransTime() == null) {
                if (messageFlag == 0 && monthTime > 0) {
                    String content = "嗨，亲爱的~你本月还可享受" + monthTime + "次24小时咨询服务哦^-^";
                    if (monthTime == 1) {
                        content += "\n-----------\n" + "下次咨询要付费了肿么办？\n戳戳手指，邀请好友加入宝大夫，免费机会就来咯！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
                    }
//                    WechatUtil.sendMsgToWechat(token, openId, content);
                    if (monthTime == 1) {
                        LogUtils.saveLog("ZXYQ_RK_TS_N1", openId);
                    } else if (monthTime == 4) {
                        LogUtils.saveLog("ZXYQ_RK_TS_2", openId);
                    }
                    chargeType = "mt";
                    onlyDoctorOnlineHandle(richConsultSession, consultSessionPropertyVo);
                }
            } else {
                long pastMillisSecond = DateUtils.pastMillisSecond(consultSessionStatusVos.get(0).getFirstTransTime());
                //咨询时间小于20小时
                if (pastMillisSecond < 24 * 60 * 60 * 1000) {
                    richConsultSession.setPayStatus(ConstantUtil.WITHIN_24HOURS.getVariable());
                    chargeType = "24h";
                } else {
                    String sysUserId = richConsultSession.getUserId();
                    //判断剩余次数,consultSessionStatusVo打标记
                    if (consultSessionPropertyVo != null && messageFlag == 0) {
                        String content;
                        monthTime = consultSessionPropertyVo.getMonthTimes();
                        //先扣月次数
                        if (monthTime > 0) {
//                            content = "亲爱的，你本月还有" + monthTime + "次24小时咨询服务就开始付费了";
                            if (consultSessionPropertyVo.getMonthTimes() == 1) {
                                content = "别怕！邀请个好友加入宝大夫，免费机会立刻有！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
                                WechatUtil.sendMsgToWechat(token, sysUserId, content);
                            }
//                            WechatUtil.sendMsgToWechat(token, sysUserId, content);
                            onlyDoctorOnlineHandle(richConsultSession, consultSessionPropertyVo);
                            if (monthTime == 1) {
                                LogUtils.saveLog("ZXYQ_RK_TS_N1", openId);
                            } else if (monthTime == 4) {
                                LogUtils.saveLog("ZXYQ_RK_TS_2", openId);
                            }
                            chargeType = "mt";
                        } else if (consultSessionPropertyVo.getPermTimes() > 0) {
                            content = "嗨，亲爱的，你还可享受" + consultSessionPropertyVo.getPermTimes() + "次24小时咨询服务哦^-^";
                            richConsultSession.setPayStatus(ConstantUtil.PAY_SUCCESS.getVariable());
                            chargeType = "pt";
//                            WechatUtil.sendMsgToWechat(token, sysUserId, content);
                            onlyDoctorOnlineHandle(richConsultSession, consultSessionPropertyVo);
                        } else if (messageFlag == 0) {

                            boolean flag = consultSessionService.cheakInstantConsultation(sysUserId);

                            richConsultSession.setPayStatus(ConstantUtil.NO_PAY.getVariable());
                            content = "亲爱的~你本月免费机会已用完，请医生喝杯茶，继续咨询\n\n" +
                                    "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/wechatInfo/getUserWechatMenId?url=35'>>>付费</a>";
//                            WechatUtil.sendMsgToWechat(token, sysUserId, content);
                            content = "什么？咨询要收费？\n 不怕！邀请个好友加入宝大夫，免费机会立刻有！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_2_backend'>>>邀请好友赚机会</a>";
//                            WechatUtil.sendMsgToWechat(token, sysUserId, content);
                            LogUtils.saveLog("consult_charge_twice_information", sysUserId);
                            LogUtils.saveLog("ZXYQ_RK_TS_N2", sysUserId);
                        }
                    }
                }
            }
            ConsultSession consultSession = new ConsultSession();
            consultSession.setId(richConsultSession.getId());
            consultSession.setChargeType(chargeType);
            consultSessionService.updateSessionInfo(consultSession);
            return consultSessionPropertyVo != null ? (consultSessionPropertyVo.getMonthTimes() + consultSessionPropertyVo.getPermTimes()) : null;
        }

        private void onlyDoctorOnlineHandle(RichConsultSession richConsultSession, ConsultSessionPropertyVo consultSessionPropertyVo) {
            //更新会话操作时间
            consultRecordService.saveConsultSessionStatus(richConsultSession);
            Query query;
            if (richConsultSession.getUserType().equals(ConstantUtil.CONSULTDOCTOR.getVariable())) {
                Query qry = (new Query()).addCriteria(where("userId").is(richConsultSession.getUserId())).with(new Sort(Sort.Direction.DESC, "createDate"));
                ConsultSessionStatusVo consultSessionStatusVo = consultRecordService.findOneConsultSessionStatusVo(qry);
                query = new Query().addCriteria(where("_id").is(consultSessionStatusVo.getId()));
                Update update = new Update().set("firstTransTime", new Date());
                consultRecordService.updateConsultSessionFirstTransferDate(query, update, ConsultSessionStatusVo.class);
                ConsultSessionManager.INSTANCE.minusConsultTimes(consultSessionPropertyVo);
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

        RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
        System.out.println("csChannel------" + csChannel);

        consultSession.setPayStatus(ConstantUtil.PAY_SUCCESS.getVariable());
        //更新会话操作时间
        consultRecordService.saveConsultSessionStatus(consultSession);

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

            csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
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

    //测试接口,以废弃
    @RequestMapping(value = "/getneedPaylist", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getList(@RequestParam(required = true) String csuserId) {
        return consultPayUserService.getneepPayConsultSession(csuserId);
    }

    @RequestMapping(value = "/confirmInstantConsultation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> confirmInstantConsultation(HttpServletRequest request, HttpSession session) {
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String token = (String) userWechatParam.get("token");
        String openId = WechatUtil.getOpenId(session, request);
        LogUtils.saveLog("feishishizixundianji", openId);
        if (null == openId) return null;
        Channel csChannel = null;
        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(openId);
        System.out.println("sessionId------" + sessionId);
        if (null == sessionId) return null;
        RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
        System.out.println("csChannel------" + csChannel);

        consultSession.setPayStatus(ConstantUtil.NOT_INSTANT_CONSULTATION.getVariable());
        //更新会话操作时间
        consultRecordService.saveConsultSessionStatus(consultSession);

        if (csChannel != null && csChannel.isActive()) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                obj.put("notifyType", "1003");
                obj.put("type", "4");
                obj.put("source", consultSession.getSource());
                TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                csChannel.writeAndFlush(frame.retain());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WechatUtil.sendMsgToWechat(token, openId, " 可以开始啦，请尽可能详细的描述您的问题，\n医生会按照先后顺序自动接诊！\n点击左下角小键盘，输入文字、图片即可。");
        return null;
    }


    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
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
}