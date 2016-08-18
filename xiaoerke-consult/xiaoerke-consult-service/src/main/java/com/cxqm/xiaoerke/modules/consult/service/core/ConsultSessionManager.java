package com.cxqm.xiaoerke.modules.consult.service.core;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultSessionPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultVoiceRecordMongoServiceImpl;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.impl.UserInfoServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ConsultSessionManager {

    private transient static final Logger log = LoggerFactory.getLogger(ConsultSessionManager.class);

    public static final String REQUEST_TYPE_CHAT = "chat";

    public static final String REQUEST_TYPE_NOTIFICATION = "notification";

    public static final String KEY_SESSION_ID = "sessionId";

    public static final String KEY_REQUEST_TYPE = "type";

    public static final String KEY_CONSULT_CONTENT = "content";

    //<userId or cs-userId, Channel>
    public final Map<String, Channel> userChannelMapping = new ConcurrentHashMap<String, Channel>();

    //<cs-userId, Channel>
    private final Map<String, Channel> csUserChannelMapping = new ConcurrentHashMap<String, Channel>();

    //<cs-userId, ConnectionHeartTime>
    private final Map<String, Date> csUserConnectionTimeMapping = new ConcurrentHashMap<String, Date>();

    //<cs-userId, Channel>
    private final Map<String, Channel> distributors = new ConcurrentHashMap<String, Channel>();

    //<Channel, userId or cs-userId>
    private final Map<Channel, String> channelUserMapping = new ConcurrentHashMap<Channel, String>();

    public List<String> distributorsList = new ArrayList<String>();

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private PatientRegisterPraiseService patientRegisterPraiseService = SpringContextHolder.getBean("patientRegisterPraiseServiceImpl");

    private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");

    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService = SpringContextHolder.getBean("consultSessionForwardRecordsServiceImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    private SystemService systemService = SpringContextHolder.getBean("systemService");

    private UserInfoServiceImpl userInfoService = SpringContextHolder.getBean("userInfoServiceImpl");

    private static ExecutorService threadExecutor = Executors.newCachedThreadPool();

    private static ConsultSessionManager sessionManager = new ConsultSessionManager();

    //jiangzg add 2016年6月17日16:26:01
    private ConsultDoctorInfoService consultDoctorInfoService = SpringContextHolder.getBean("consultDoctorInfoServiceImpl");

    //jiangzg add
    private ConsultVoiceRecordMongoServiceImpl consultVoiceRecordMongoService = SpringContextHolder.getBean("consultVoiceRecordMongoServiceImpl");

    //jiangzg add
    private ConsultSessionPropertyServiceImpl consultSessionPropertyService = SpringContextHolder.getBean("consultSessionPropertyServiceImpl");

    private ConsultSessionManager() {
//        String distributorsStr = Global.getConfig("distributors.list");
//        distributorsList = Arrays.asList(distributorsStr.split(";"));
        User user = new User();
        user.setUserType("distributor");
        List<User> users = systemService.findUserByUserType(user);
        for (User u : users) {
            distributorsList.add(u.getId());
        }
    }

    public static ConsultSessionManager getSessionManager() {
        return sessionManager;
    }

    void createSocket(ChannelHandlerContext ctx, String url) {

        Channel channel = ctx.channel();

        String[] args = url.split("&");
        String fromType = args[1];

        if (args.length > 2) {
            if (fromType.equals("user")) {
                String userId = args[2];
                String source = args[3];
                doCreateSocketInitiatedByUser(userId, source, channel);
            } else if (fromType.equals("cs")) {
                String userId = args[2];
                User csUser = systemService.getUserById(userId);
                if (csUser.getUserType().equals("consultDoctor")) {
                    doCreateSocketInitiatedByCs(userId, channel);
                }
            } else if (fromType.equals("distributor")) {
                String userId = args[2];
                User csUser = systemService.getUserById(userId);
                if (csUser.getUserType().equals("distributor")) {
                    doCreateSocketInitiatedByDistributor(userId, channel);
                }
            }
        }
    }

    private void doCreateSocketInitiatedByCs(String csUserId, Channel channel) {
        System.out.println("doctor init ------" + csUserId);
        csUserChannelMapping.put(csUserId, channel);
        userChannelMapping.put(csUserId, channel);
        channelUserMapping.put(channel, csUserId);
        csUserConnectionTimeMapping.put(csUserId, new Date());
    }

    private void doCreateSocketInitiatedByDistributor(String distributorUserId, Channel channel) {
        System.out.println("doctor init ------" + distributorUserId);
        if (distributorsList.contains(distributorUserId)) {
            distributors.put(distributorUserId, channel);
            csUserChannelMapping.put(distributorUserId, channel);
            userChannelMapping.put(distributorUserId, channel);
            channelUserMapping.put(channel, distributorUserId);
            csUserConnectionTimeMapping.put(distributorUserId, new Date());
        } else {
            log.warn("Maybe a Simulated Distributor: The userId is " + distributorUserId);
        }
    }

    private void doCreateSocketInitiatedByUser(String userId, String source, Channel channel) {
        userChannelMapping.put(userId, channel);
        channelUserMapping.put(channel, userId);
    }

    public RichConsultSession createUserH5ConsultSession(String userId, Channel channel, String source) {

        Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
        RichConsultSession consultSession = null;
        Channel distributorChannel = null;

        if (sessionId != null)
            consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);

        if (consultSession == null) {
            User user = systemService.getUserById(userId);
            consultSession = new RichConsultSession();
            consultSession.setCreateTime(new Date());
            InetSocketAddress address = (InetSocketAddress) channel.localAddress();
            consultSession.setServerAddress(String.valueOf(address.getAddress()).replace("/", ""));
            consultSession.setUserId(userId);
            if (user != null) {
                consultSession.setUserName(user.getName() == null ? user.getLoginName() : user.getName());
            } else {
                consultSession.setUserName(userId);
            }
            consultSession.setSource(source);

            if (distributors.size() > 0) {
                for (int i = 0; i < distributorsList.size(); i++) {
                    String distributorId = RandomUtils.getRandomKeyFromMap(distributors);
                    distributorChannel = distributors.get(distributorId);
                    if (distributorChannel.isActive()) {
                        consultSession.setCsUserId(distributorId);
                        User csUser = systemService.getUserById(distributorId);
                        consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
                        break;
                    } else {
                        distributors.remove(distributorId);
                        csUserChannelMapping.remove(distributorId);
                    }
                }
            }

            if (distributorChannel == null) {
                if (csUserChannelMapping.size() != 0) {
                    //所有的接诊员不在线，随机分配一个在线医生
                    Iterator<Entry<String, Channel>> csUserChannel = csUserChannelMapping.entrySet().iterator();
                    List<HashMap<String, Object>> doctorOnLineList = new ArrayList();
                    while (csUserChannel.hasNext()) {
                        HashMap<String, Object> doctorOnLineMap = new HashMap<String, Object>();
                        Map.Entry<String, Channel> entry = csUserChannel.next();
                        Channel csChannel = entry.getValue();
                        if (csChannel.isActive()) {
                            doctorOnLineMap.put("csUserId", entry.getKey());
                            doctorOnLineMap.put("channel", entry.getValue());
                            doctorOnLineList.add(doctorOnLineMap);
                        }
                    }
                    //通过一个随机方法，从doctorOnLineList选择一个医生，为用户提供服务
                    Random rand = new Random();
                    if (doctorOnLineList != null && doctorOnLineList.size() > 0) {
                        int indexCS = rand.nextInt(doctorOnLineList.size());
                        consultSession.setCsUserId((String) doctorOnLineList.get(indexCS).get("csUserId"));
                        Channel csChannel = (Channel) doctorOnLineList.get(indexCS).get("channel");
                        if (csChannel.isActive()) {
                            //csChannel如果活着的话，证明，此医生处于登陆状态
                            User csUser = systemService.getUserById((String) doctorOnLineList.get(indexCS).get("csUserId"));
                            consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
                        } else {
                            csUserChannelMapping.remove(doctorOnLineList.get(indexCS).get("csUserId"));
                        }
                    } else {
                        JSONObject obj = new JSONObject();
                        obj.put("type", 4);
                        obj.put("notifyType", "1002");
                        TextWebSocketFrame msg = new TextWebSocketFrame(obj.toJSONString());
                        channel.writeAndFlush(msg.retain());
                        return null;
                    }
                } else {
                    JSONObject obj = new JSONObject();
                    obj.put("type", 4);
                    obj.put("notifyType", "1002");
                    TextWebSocketFrame msg = new TextWebSocketFrame(obj.toJSONString());
                    channel.writeAndFlush(msg.retain());
                    return null;
                }
            }
            Map praiseParam = new HashMap();
            praiseParam.put("userId", consultSession.getUserId());
            Integer sessionCount = consultSessionService.getConsultSessionByUserId(praiseParam);
            consultSession.setConsultNumber(sessionCount + 1);
            consultSessionService.saveConsultInfo(consultSession);
            sessionId = consultSession.getId();
            sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
            sessionRedisCache.putUserIdSessionIdPair(userId, sessionId);
            JSONObject csobj = new JSONObject();
            //通知用户，告诉会有哪个医生或者接诊员提供服务
            csobj.put("type", 4);
            csobj.put("notifyType", "1001");
            csobj.put("sessionId", consultSession.getId());
            TextWebSocketFrame csframe = new TextWebSocketFrame(csobj.toJSONString());
            channel.writeAndFlush(csframe.retain());
        }
        return consultSession;
    }

    public HashMap<String, Object> createUserWXConsultSession(RichConsultSession consultSession) {

        HashMap<String, Object> response = new HashMap<String, Object>();
        Channel csChannel = null;
        Channel distributorChannel = null;
        System.out.println("distributors.size()-----" + distributors.size());
        ConsultCountTotal consultCountTotal = new ConsultCountTotal();
        consultCountTotal.setUserId(consultSession.getUserId());
        consultCountTotal.setCreateDate(new Date());
        int count = this.getConsultTotal(consultCountTotal);
        List currentDistributorChannel = new ArrayList();
        if (distributors != null && distributors.size() > 0) {
            for (Map.Entry<String, Channel> entry : distributors.entrySet()) {
                if (entry.getValue().isActive()) {
                    HashMap<String, Channel> currentActiveChannel = new HashMap<String, Channel>();
                    currentActiveChannel.put(entry.getKey(), entry.getValue());
                    currentDistributorChannel.add(currentActiveChannel);
                } else {
                    distributors.remove(entry.getKey());
                    csUserChannelMapping.remove(entry.getKey());
                    userChannelMapping.remove(entry.getKey());
                }
            }
            if (currentDistributorChannel != null && currentDistributorChannel.size() > 0) {
                int number = count % currentDistributorChannel.size();
                HashMap<String, Channel> hashMap = (HashMap) currentDistributorChannel.get(number);
                for (Map.Entry<String, Channel> entry : hashMap.entrySet()) {
                    consultSession.setCsUserId(entry.getKey());
                    consultCountTotal.setCsUserId(entry.getKey());
                    User csUser = systemService.getUserById(entry.getKey());
                    consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
                    csChannel = entry.getValue();
                }
            }
        }

        if (csChannel != null) {
            this.updateConsultCountTotal(consultCountTotal);
        } else {
            /***接诊员不在线，随机分配在线医生***/
            /**
             *  jiangzhongge modify
             *  2016年7月4日11:52:04
             */
            this.deleteConsultCountTotal(consultCountTotal);
            Map<String, Channel> currentCSUserChannelMap = csUserChannelMapping;
            Channel nowChannel = null;
            if (currentCSUserChannelMap.size() > 0) {
                for (int i = 0; i < currentCSUserChannelMap.size(); i++) {
                    //所有的接诊员不在线，随机分配一个在线医生
                    String csUserId = RandomUtils.getRandomKeyFromMap(currentCSUserChannelMap);
                    nowChannel = currentCSUserChannelMap.get(csUserId);
                    if (nowChannel != null && nowChannel.isActive()) {
                        User csUser = systemService.getUserById(csUserId);
                        consultSession.setCsUserId(csUserId);
                        consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
                        csChannel = nowChannel;
                        break;
                    } else {
                        csUserChannelMapping.remove(csUserId);
                        csUserChannelMapping.remove(csUserId);
                        userChannelMapping.remove(csUserId);
                        currentCSUserChannelMap.remove(csUserId);
                    }
                }
            }
            /**
             * @author jiangzg
             * @date 2016-7-7 10:33:29
             */
            if (csChannel == null) {
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                String content = "抱歉，暂时没有医生在线，请稍后使用服务！";
                WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), consultSession.getUserId(), content);
                return null;
            }
        }

        if (StringUtils.isNotNull(consultSession.getCsUserId())) {
            HashMap<String, Object> perInfo = userInfoService.findPersonDetailInfoByUserId(consultSession.getCsUserId());
            consultSession.setCsUserName((String) perInfo.get("name"));
        }

        //可开启线程进行记录
        if (consultSession.getCsUserId() != null) {
            //查询该用户之前咨询次数
            Map praiseParam = new HashMap();
            praiseParam.put("userId", consultSession.getUserId());
            Integer sessionCount = consultSessionService.getConsultSessionByUserId(praiseParam);
            consultSession.setConsultNumber(sessionCount + 1);
            consultSessionService.saveConsultInfo(consultSession);
            Integer sessionId = consultSession.getId();
            System.out.println("sessionId-----" + sessionId + "consultSession.getCsUserId()" + consultSession.getUserId());
            sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
            sessionRedisCache.putUserIdSessionIdPair(consultSession.getUserId(), sessionId);
            saveCustomerEvaluation(consultSession);
            response.put("csChannel", csChannel);
            response.put("sessionId", sessionId);
            response.put("consultSession", consultSession);
            return response;
        } else {
            return null;
        }

    }

    /**
     * jiangzg add 2016-7-13 16:37:23
     *
     * @param consultCountTotal
     * @return
     */
    public int getConsultTotal(ConsultCountTotal consultCountTotal) {
        int count = 0;
        synchronized (this) {
            if (consultCountTotal != null) {
                consultVoiceRecordMongoService.insertConsultCountTotal(consultCountTotal);
                Long aa = consultVoiceRecordMongoService.getConsultCountTotal(consultCountTotal);
                count = aa.intValue();
            }
        }
        return count;
    }

    /**
     * jiangzg add 2016-7-13 16:37:23
     *
     * @param consultCountTotal
     * @return
     */
    public void updateConsultCountTotal(ConsultCountTotal consultCountTotal) {
        if (consultCountTotal != null) {
            consultVoiceRecordMongoService.findAndModify(consultCountTotal);
        }
    }

    public void deleteConsultCountTotal(ConsultCountTotal consultCountTotal){
        if(consultCountTotal != null){
            consultVoiceRecordMongoService.deleteConsultCountTotal(consultCountTotal);
        }
    }
    //记录评价信息
    private void saveCustomerEvaluation(RichConsultSession consultSession) {
        Map<String, Object> evaluationMap = new HashMap<String, Object>();
        evaluationMap.put("openid", consultSession.getUserId());
        evaluationMap.put("uuid", IdGen.uuid());
        evaluationMap.put("starNum1", 0);
        evaluationMap.put("starNum2", 0);
        evaluationMap.put("starNum3", 0);
        evaluationMap.put("doctorId", consultSession.getCsUserId());
        evaluationMap.put("content", "");
        evaluationMap.put("dissatisfied", null);
        evaluationMap.put("redPacket", null);
        evaluationMap.put("consultSessionId", consultSession.getId());
        patientRegisterPraiseService.saveCustomerEvaluation(evaluationMap);
    }

    /**
     * @param sessionId
     * @param toCsUserId
     * @param remark
     * @return
     */
    public int transferSession(Integer sessionId, String toCsUserId, String remark) {
        try {
            System.out.println("sessionId===" + sessionId + "toCsUserId====" + toCsUserId);
            RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);

            ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
            consultSessionForwardRecordsVo.setConversationId(Long.parseLong(String.valueOf(sessionId)));
            consultSessionForwardRecordsVo.setToUserId(toCsUserId);
            consultSessionForwardRecordsVo.setFromUserId(session.getCsUserId());
            List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVoList =
                    consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);

            if (consultSessionForwardRecordsVoList.size() > 0) {
                for (ConsultSessionForwardRecordsVo consultSessionForwardRecords : consultSessionForwardRecordsVoList) {
                    String status = consultSessionForwardRecords.getStatus();
                    if (status.equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING)) {
                        return 2;
                    }
                }
            }

            User toCsUser = systemService.getUser(toCsUserId);
            Channel channelToCsUser = userChannelMapping.get(toCsUserId);
            Channel channelFromCsUser = userChannelMapping.get(session.getCsUserId());
            System.out.println("toCsUserId========" + toCsUserId + "CsUserId========" + session.getCsUserId() + "channelFromCsUser.isActive()=========" + channelFromCsUser.isActive());
            if (channelFromCsUser.isActive()) {

                ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
                forwardRecord.setConversationId(sessionId.longValue());
                forwardRecord.setCreateBy(session.getCsUserId());
                forwardRecord.setCreateTime(new Date());
                forwardRecord.setFromUserId(session.getCsUserId());
                forwardRecord.setToUserId(toCsUserId);
                forwardRecord.setRemark(remark);
                forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING);
                consultSessionForwardRecordsService.save(forwardRecord);

                //通知发起转接的人，转接正在处理中在5秒钟内，接诊员有机会取消转接，如果，5秒后，接诊员不取消，则接诊员不能再取消转接
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("type", "4");
                jsonObj.put("notifyType", "0011");
                jsonObj.put("session", session);
                jsonObj.put("remark", remark);
                jsonObj.put("toCsUserId", toCsUserId);
                jsonObj.put("toCsUserName", toCsUser.getName());
                TextWebSocketFrame frameFromCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
                channelFromCsUser.writeAndFlush(frameFromCsUser.retain());

                Runnable thread = new processTransferThread(forwardRecord.getId(), channelToCsUser, channelFromCsUser, session, toCsUser, remark);
                threadExecutor.execute(thread);

                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public class processTransferThread extends Thread {
        private long forwardRecordId;
        private Channel channelToCsUser;
        private Channel channelFromCsUser;
        private RichConsultSession session;
        private User toCsUser;
        private String remark;

        public processTransferThread(long forwardRecordId, Channel channelToCsUser, Channel channelFromCsUser, RichConsultSession session, User toCsUser, String remark) {
            this.forwardRecordId = forwardRecordId;
            this.channelToCsUser = channelToCsUser;
            this.channelFromCsUser = channelFromCsUser;
            this.session = session;
            this.toCsUser = toCsUser;
            this.remark = remark;
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("forwardRecordId============" + forwardRecordId + "channelToCsUser==============" + channelToCsUser + "channelFromCsUser=======" + channelFromCsUser + "channelFromCsUser====" + channelFromCsUser);
            //查询SessionForwardRecords，如果此条记录，已经被接诊员取消，则不再通知医生转接
            ConsultSessionForwardRecordsVo sessionForwardRecordsVo = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId);
            if (!(sessionForwardRecordsVo.getStatus().equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED))) {
                if (channelToCsUser.isActive() && channelFromCsUser.isActive()) {
                    //通知被转接咨询医生，有用户需要转接
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("type", "4");
                    jsonObj.put("notifyType", "0009");
                    jsonObj.put("session", session);
                    jsonObj.put("toCsUserName", toCsUser.getName());
                    jsonObj.put("remark", remark);
                    TextWebSocketFrame frameToCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
                    channelToCsUser.writeAndFlush(frameToCsUser.retain());

                    //通知接诊员，不能再取消转接
                    jsonObj.clear();
                    jsonObj.put("type", "4");
                    jsonObj.put("notifyType", "0013");
                    jsonObj.put("session", session);
                    TextWebSocketFrame frameFromCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
                    channelFromCsUser.writeAndFlush(frameFromCsUser.retain());

                    try {
                        //一分钟后判断，如果，该会话，没有被医生转接走，则取消该次转接，将会话，还给接诊员
                        Thread.sleep(120000);
                        ConsultSessionForwardRecordsVo sessionForwardRecordsVoLater = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId);
                        if (sessionForwardRecordsVoLater.getStatus().equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING)) {
                            Long sessionId = sessionForwardRecordsVoLater.getConversationId();
                            RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(Integer.parseInt(String.valueOf(sessionId)));
                            ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
                            forwardRecord.setConversationId(sessionId);
                            forwardRecord.setFromUserId(session.getCsUserId());
                            forwardRecord.setToUserId(sessionForwardRecordsVoLater.getToUserId());
                            forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED);
                            int count = consultSessionForwardRecordsService.cancelTransfer(forwardRecord);
                            if (count > 0) {
                                //通知医生，转接取消
                                Channel channelToCsUser = userChannelMapping.get(sessionForwardRecordsVoLater.getToUserId());
                                jsonObj.clear();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType", "0012");
                                jsonObj.put("session", session);
                                TextWebSocketFrame frame1 = new TextWebSocketFrame(jsonObj.toJSONString());
                                channelToCsUser.writeAndFlush(frame1.retain());

                                //通知接诊员，退回此次转接
                                Channel channelFromCsUser = userChannelMapping.get(sessionForwardRecordsVoLater.getFromUserId());
                                jsonObj.clear();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType", "0014");
                                jsonObj.put("session", session);
                                TextWebSocketFrame frame2 = new TextWebSocketFrame(jsonObj.toJSONString());
                                channelFromCsUser.writeAndFlush(frame2.retain());
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void react2Transfer(Integer sessionId, Integer forwardRecordId, String toCsUserId, String toCsUserName, String operation) {
        System.out.println("sessionId============" + sessionId + "forwardRecordId==============" + forwardRecordId + "toCsUserId=======" + toCsUserId + "operation====" + operation);

        RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        if (session != null) {
            String fromCsUserId = session.getCsUserId();
            session.setCsUserId(toCsUserId);
            session.setCsUserName(toCsUserName);
            Channel channelFromCsUser = userChannelMapping.get(fromCsUserId);
            if (channelFromCsUser != null && channelFromCsUser.isActive()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("type", "4");
                jsonObj.put("notifyType", "0010");
                jsonObj.put("operation", operation);
                jsonObj.put("session", session);
                TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                channelFromCsUser.writeAndFlush(frame.retain());

                ConsultSessionForwardRecordsVo forwardRecord = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId.longValue());

                if (ConsultSessionForwardRecordsVo.REACT_TRANSFER_OPERATION_ACCEPT.equalsIgnoreCase(operation)) {
                    if (session != null) {
                        sessionRedisCache.putSessionIdConsultSessionPair(sessionId, session);
                        ConsultSession consultSession = new ConsultSession();
                        consultSession.setId(sessionId);
                        consultSession.setCsUserId(session.getCsUserId());
                        consultSessionService.updateSessionInfo(consultSession);
                    }
                    forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_ACCEPT);
                    consultSessionForwardRecordsService.updateAcceptedTransfer(forwardRecord);
                    session.setCsUserId(forwardRecord.getToUserId());
                    consultRecordService.modifyConsultSessionStatusVo(session);

                    //如果接收转接的是医生，那么给用户推送消息"我是XX科XX医生，希望能帮到您！"，其他不推送
                    List<Map> result = consultDoctorInfoService.getDoctorInfoMoreByUserId(toCsUserId);
                    if (result != null && result.size() > 0) {
                        String source = session.getSource();
                        Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(toCsUserId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObj.put("type", "4");
                        if (StringUtils.isNotNull((String) result.get(0).get("userType")) && "consultDoctor".equalsIgnoreCase((String) result.get(0).get("userType"))) {
                            String sendMsg = "";
                            StringBuffer responseNews = new StringBuffer();
                            responseNews.append("嗨，亲爱的，已为您接入");
                            if (StringUtils.isNotNull((String) result.get(0).get("department"))) {
                                responseNews.append(result.get(0).get("department"));
                            } else {
                                responseNews.append("宝大夫");
                            }
                            if (StringUtils.isNotNull(toCsUserName)) {
                                responseNews.append(toCsUserName);
                            } else {
                                responseNews.append("特约");
                            }
                            if (StringUtils.isNotNull(source) && "wxcxqm".equalsIgnoreCase(source)) {
                                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                                String userId = session.getUserId();
                                Query query = (new Query()).addCriteria(where("userId").is(userId)).with(new Sort(Sort.Direction.DESC, "createDate"));
                                ConsultSessionStatusVo consultSessionStatusVo = consultRecordService.findOneConsultSessionStatusVo(query);
                                if(consultSessionStatusVo != null){
                                    List<String> list = new ArrayList<String>();
                                    list.add("useTimes");
                                    list.add("paySuccess");
                                    if(ConstantUtil.WITHIN_24HOURS.equals(consultSessionStatusVo.getPayStatus())){
                       //                 Query query2 = (new Query()).addCriteria(where("userId").is(userId).and("payStatus").in(list)).with(new Sort(Sort.Direction.DESC, "createDate"));
                       //                 ConsultSessionStatusVo consultSessionStatusVo2 = consultRecordService.findOneConsultSessionStatusVo(query);
                                        /**
                                         * 剩余时间计算(暂不需要)
                                         */
                                        responseNews.append("医生，希望能帮到你O(∩_∩)O~");
                                        sendMsg = responseNews.toString();
                                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), session.getUserId(), sendMsg);
                                        jsonObj.put("notifyType", "1001");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }else if(ConstantUtil.USE_TIMES.equals(consultSessionStatusVo.getPayStatus())){
                       //                 Query query2 = (new Query()).addCriteria(where("userId").is(userId).and("payStatus").in(list)).with(new Sort(Sort.Direction.DESC, "createDate")).limit(2);
                       //                 List<ConsultSessionStatusVo> consultSessionStatusVoList = consultRecordService.queryUserMessageList(query2);
                                        /**
                                         * 接收转接的是医生，会免费次数减1
                                         */
                                        if(consultSessionStatusVo.getFirstTransTime() == null || "".equals(consultSessionStatusVo.getFirstTransTime())){
                                            /**
                                             *  更新第一次转医生的时间
                                             */
                                            query = new Query().addCriteria(where("id_").is(consultSessionStatusVo.getId()));
                                            Update update = new Update().set("firstTransTime", new Date());
                                            consultRecordService.updateConsultSessionFirstTransferDate(query,update,ConsultSessionStatusVo.class);
                                            ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(userId);
                                            if(consultSessionPropertyVo != null){
                                                int defaultTimes = consultSessionPropertyVo.getMonthTimes();
                                                int additionalTimes = consultSessionPropertyVo.getPermTimes();
                                                if(defaultTimes > 0){
                                                    defaultTimes--;
                                                    consultSessionPropertyVo.setMonthTimes(defaultTimes);
                                                }else{
                                                    if(additionalTimes > 0){
                                                        additionalTimes--;
                                                        consultSessionPropertyVo.setPermTimes(additionalTimes);
                                                    }
                                                }
                                                consultSessionPropertyService.updateByPrimaryKey(consultSessionPropertyVo);
                                            }
                                        }
                                        responseNews.append("医生，希望能帮到你O(∩_∩)O~");
                                        sendMsg = responseNews.toString();
                                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), session.getUserId(), sendMsg);
                                        jsonObj.put("notifyType", "1001");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }else if(ConstantUtil.PAY_SUCCESS.equals(consultSessionStatusVo.getPayStatus())){
                                        responseNews.append("医生，希望能帮到你O(∩_∩)O~");
                                        sendMsg = responseNews.toString();
                                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), session.getUserId(), sendMsg);
                                        jsonObj.put("notifyType", "1001");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }else{
                                        jsonObj.put("notifyType","1002");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }
                                }
                            } else if (StringUtils.isNotNull(source) && "h5cxqm".equalsIgnoreCase(source)) {
                                //暂时注掉H5
                                /*Channel userChannel = userChannelMapping.get(session.getUserId());
                                if(userChannel != null && userChannel.isActive()){
                                    JSONObject resToUser= new JSONObject();
                                    resToUser.put("type", "4");
                                    resToUser.put("notifyType", "1004");
                                    resToUser.put("operation", operation);
                                    resToUser.put("session", session);
                                    resToUser.put("content", responseNews.toString());
                                    TextWebSocketFrame resToUserFrame = new TextWebSocketFrame(resToUser.toJSONString());
                                    userChannel.writeAndFlush(resToUserFrame.retain());
                                }*/
                            }
                        }else{
                            if (StringUtils.isNotNull(source) && "wxcxqm".equalsIgnoreCase(source)) {
                                String userId = session.getUserId();
                                Query query = (new Query()).addCriteria(where("userId").is(userId)).with(new Sort(Sort.Direction.DESC, "createDate"));
                                ConsultSessionStatusVo consultSessionStatusVo = consultRecordService.findOneConsultSessionStatusVo(query);
                                String status = ConstantUtil.WITHIN_24HOURS +","+ConstantUtil.PAY_SUCCESS+","+ConstantUtil.USE_TIMES;
                                if(consultSessionStatusVo != null){
                                    if(status.contains(consultSessionStatusVo.getPayStatus())){
                                        jsonObj.put("notifyType", "1001");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }else{
                                        jsonObj.put("notifyType","1002");
                                        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(jsonObject));
                                        csChannel.writeAndFlush(csUserMsg.retain());
                                    }
                                }
                            } else if (StringUtils.isNotNull(source) && "h5cxqm".equalsIgnoreCase(source)) {
                                //暂时注掉H5
                                /*Channel userChannel = userChannelMapping.get(session.getUserId());
                                if(userChannel != null && userChannel.isActive()){
                                    JSONObject resToUser= new JSONObject();
                                    resToUser.put("type", "4");
                                    resToUser.put("notifyType", "1004");
                                    resToUser.put("operation", operation);
                                    resToUser.put("session", session);
                                    resToUser.put("content", responseNews.toString());
                                    TextWebSocketFrame resToUserFrame = new TextWebSocketFrame(resToUser.toJSONString());
                                    userChannel.writeAndFlush(resToUserFrame.retain());
                                }*/
                            }
                        }
                    }
                    //生成评价
                    Map praiseParam = new HashMap();
                    praiseParam.put("consultSessionId", session.getId());
                    praiseParam.put("doctorId",forwardRecord.getToUserId());
                    List<Map<String,Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParam);
                    if(praiseList !=null && praiseList.size() == 0){
                        saveCustomerEvaluation(session);
                    }
                } else {
                    forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_REJECT);
                    consultSessionForwardRecordsService.updateRejectedTransfer(forwardRecord);
                }
            }
        }

    }

    public void putSessionIdConsultSessionPair(Integer sessionId, RichConsultSession session) {
        System.out.println("putSessionIdConsultSessionPair=====" + sessionId + "RichConsultSession" + session.getId());
        sessionRedisCache.putSessionIdConsultSessionPair(sessionId, session);
    }

    public void putUserIdSessionIdPair(String userId, Integer sessionId) {
        System.out.println("putUserIdSessionIdPair=====" + userId + "sessionId" + sessionId);
        sessionRedisCache.putUserIdSessionIdPair(userId, sessionId);
    }

    public void cancelTransferringSession(Integer sessionId, String toCsUserId, String remark) {
        System.out.println("cancelTransferringSession=====" + sessionId + "toCsUserId" + toCsUserId);
        RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
        forwardRecord.setConversationId(sessionId.longValue());
        forwardRecord.setFromUserId(session.getCsUserId());
        forwardRecord.setToUserId(toCsUserId);
        forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED);
        consultSessionForwardRecordsService.cancelTransfer(forwardRecord);
    }

    public List<String> getOnlineCsList() {
        List<String> userIds = new ArrayList<String>();
        Iterator<Entry<String, Channel>> it = csUserChannelMapping.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Channel> entry = it.next();
            if (entry.getValue().isActive())
                userIds.add(entry.getKey());
        }
        return userIds;
    }

    public void checkDoctorChannelStatus() {
        if (csUserConnectionTimeMapping != null) {
            Iterator<Entry<String, Date>> it2 = csUserConnectionTimeMapping.entrySet().iterator();
            while (it2.hasNext()) {
                Entry<String, Date> entry = it2.next();
                Date dateTime = entry.getValue();
                long a = new Date().getTime();
                long b = dateTime.getTime();
                int c = (int) ((a - b) / 1000);
                if (c > 140) {
                    //超过2分钟，没有收到心跳回馈信息，清除此channel
                    removeUserSession(entry.getKey());
                    csUserConnectionTimeMapping.remove(entry.getKey());
                }
            }
        }

        Iterator<Entry<String, Channel>> it = csUserChannelMapping.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Channel> entry = it.next();
            if (entry.getValue().isActive()) {
                //像医生端发送心跳包，如果2分钟内，没有得到回复，
                // 则证明此医生已经掉线，将进行channel和已存在的会话清除处理工作
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("type", "4");
                jsonObj.put("notifyType", "0015");
                jsonObj.put("notifyAddress", ConstantUtil.SERVER_ADDRESS);
                TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                entry.getValue().writeAndFlush(frame.retain());
                csUserConnectionTimeMapping.put(entry.getKey(), new Date());
            }
        }
    }

    public Map<String, Channel> getUserChannelMapping() {
        return userChannelMapping;
    }

    public Map<Channel, String> getChannelUserMapping() {
        return channelUserMapping;
    }

    public List<Object> getCurrentSessions(List<Object> sessionIds) {
        return sessionRedisCache.getConsultSessionsBySessionIds(sessionIds);
    }

    public Map<String, Channel> getCsUserChannelMapping() {
        return csUserChannelMapping;
    }

    public Map<String, Date> getCsUserConnectionTimeMapping() {
        return csUserConnectionTimeMapping;
    }

    public void removeUserSession(String userId) {
        Iterator iterator = userChannelMapping.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (userId.equals(key)) {
                iterator.remove();
                userChannelMapping.remove(key);
                csUserChannelMapping.remove(key);
            }
        }
    }

    /**
     * Created by jiangzhongge on 2016-5-18.
     * 医生选择一个用户，主动跟用户发起咨询会话
     */
    public HashMap<String, Object> createConsultSession(String userName, String userId) {
        System.out.println("userId createConsultSession =====" + userId);
        //根据用户ID去查询，从历史会话记录中，获取用户最近的一条聊天记录，根据source判断会话来源
        HashMap<String, Object> response = new HashMap<String, Object>();
        RichConsultSession richConsultSession = new RichConsultSession();
        ConsultSession consultSession = new ConsultSession();
        consultSession.setStatus("ongoing");
        consultSession.setUserId(userId);
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        System.out.println("consultSessions.size() =====" + consultSessions.size());
        if (consultSessions.size() > 0) {
            //如果会话处于转接中，则不能抢过会话，如果会话非转接状态，则超级医生具有权限抢过会话
            ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
            consultSessionForwardRecordsVo.setConversationId(Long.valueOf(consultSessions.get(0).getId()));
            consultSessionForwardRecordsVo.setStatus("waiting");
            List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
            System.out.println("consultSessionForwardRecordsVos.size() =====" + consultSessionForwardRecordsVos.size());
            if (consultSessionForwardRecordsVos.size() > 0) {
                response.put("result", "existTransferSession");
            } else {
                if (consultSessions.get(0).getSource() != null && consultSessions.get(0).getSource().contains("h5")) {
                    response.put("result", "notOnLine");
                } else {
                    String doctorManagerStr = "";
                    StringBuffer sb = new StringBuffer();
                    ConsultDoctorInfoVo consultDoctorInfoVo = new ConsultDoctorInfoVo();
                    consultDoctorInfoVo.setGrabSession("1");
                    List<ConsultDoctorInfoVo> doctorList = consultDoctorInfoService.findManagerDoctorInfoBySelective(consultDoctorInfoVo);
                    if(doctorList != null && doctorList.size() > 0){
                        for(ConsultDoctorInfoVo c : doctorList){
                            sb.append(c.getId());
                            sb.append(",");
                        }
                        doctorManagerStr = sb.toString()+"67b66b5bac5d41c1ab2274d09362f13b";
                    }else{
                        doctorManagerStr = Global.getConfig("doctorManager.list")+"67b66b5bac5d41c1ab2274d09362f13b";  //增加抢断会话功能doctorManager.list；createConsult.list
                    }
                    String csUserId = UserUtils.getUser().getId();
                    if (doctorManagerStr.indexOf(csUserId) != -1) {
                        //此医生为管理员医生，有权限抢过会话，将会话抢过来
                        richConsultSession.setCsUserId(csUserId);
                        richConsultSession.setCsUserName(UserUtils.getUser().getName());
                        richConsultSession.setUserId(userId);
                        richConsultSession.setUserName(userName);
                        richConsultSession.setId(consultSessions.get(0).getId());
                        richConsultSession.setSource(consultSessions.get(0).getSource());
                        System.out.println("richConsultSession.id =====" + richConsultSession.getId());
                        setRichConsultSession(response, richConsultSession);
                    } else {
                        //如果是普通医生，没有权限抢断会话，直接返回提升没有权限操作
                        response.put("result", "noLicenseTransfer");
                    }
                }
            }
        } else {
            //用户目前没有任何进行的会话，切用户距离最近一次咨询，没有超过48小时，则可为用户重新创建一个会话
            Query query = (new Query()).addCriteria(where("userId").is(userId)).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            ConsultSessionStatusVo consultSessionStatusVo = consultRecordService.findOneConsultSessionStatusVo(query);
            System.out.println("lastTime=======" + consultSessionStatusVo.getLastMessageTime());
            if (DateUtils.pastHour(consultSessionStatusVo.getLastMessageTime()) < 48L) {
                if (consultSessionStatusVo.getSource() != null && consultSessionStatusVo.getSource().contains("h5")) {
                    response.put("result", "notOnLine");
                } else {
                    richConsultSession.setCsUserId(UserUtils.getUser().getId());
                    richConsultSession.setUserId(userId);
                    richConsultSession.setUserName(userName);
                    richConsultSession.setCsUserName(UserUtils.getUser().getName());
                    setRichConsultSession(response, richConsultSession);
                }
            } else {
                response.put("result", "exceed48Hours");
            }
        }
        return response;
    }

    private void setRichConsultSession(HashMap<String, Object> response, RichConsultSession richConsultSession) {
        ConsultSession consultSession = new ConsultSession();
        int flag = 0;
        System.out.println("richConsultSession.getId()------" + richConsultSession.getId());
        if (richConsultSession.getId() != null) {
            consultSession.setId(richConsultSession.getId());
            consultSession.setCsUserId(richConsultSession.getCsUserId());
            consultSession.setStatus("ongoing");
            consultSession.setSource("wxcxqm");
            consultSession.setCreateTime(new Date());
            Map praiseParam = new HashMap();
            praiseParam.put("userId", consultSession.getUserId());
            Integer sessionCount = consultSessionService.getConsultSessionByUserId(praiseParam);
            consultSession.setConsultNumber(sessionCount + 1);
            flag = consultSessionService.updateSessionInfo(consultSession);
        } else {
            consultSession.setCsUserId(richConsultSession.getCsUserId());
            consultSession.setStatus("ongoing");
            consultSession.setSource("wxcxqm");
            consultSession.setUserId(richConsultSession.getUserId());
            consultSession.setCreateTime(new Date());
            Map praiseParam = new HashMap();
            praiseParam.put("userId", consultSession.getUserId());
            Integer sessionCount = consultSessionService.getConsultSessionByUserId(praiseParam);
            consultSession.setConsultNumber(sessionCount + 1);
            flag = consultSessionService.saveConsultInfo(consultSession);
            richConsultSession.setId(consultSession.getId());
            richConsultSession.setSource(consultSession.getSource());
            richConsultSession.setCreateTime(consultSession.getCreateTime());
        }
        System.out.println("flag====" + flag);
        if (flag > 0) {
            response.put("result", "success");
            response.put("userId", richConsultSession.getUserId());
            //生成评价
            Map praiseParam = new HashMap();
            praiseParam.put("consultSessionId", richConsultSession.getId());
            praiseParam.put("doctorId",richConsultSession.getCsUserId());
            List<Map<String,Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParam);
            if(praiseList !=null && praiseList.size() == 0){
                saveCustomerEvaluation(richConsultSession);
            }
        } else {
            response.put("result", "failure");
        }
        ConsultSessionManager.getSessionManager().putSessionIdConsultSessionPair(richConsultSession.getId(), richConsultSession);
        ConsultSessionManager.getSessionManager().putUserIdSessionIdPair(richConsultSession.getUserId(), richConsultSession.getId());

    }

    /**
     * Created by jiangzhongge on 2016-5-18.
     * 通知所有在线接诊员刷新转诊列表
     */
    public void refreshConsultTransferList(String distributorId) {
        Map<String, Channel> csUserChannelMap = ConsultSessionManager.getSessionManager().getCsUserChannelMapping();
//        String distributorsStr = Global.getConfig("distributors.list");
//        List distributorsList = Arrays.asList(distributorsStr.split(";"));
        JSONObject csobj = new JSONObject();
        //通知用户，告诉会有哪个医生或者接诊员提供服务
        csobj.put("type", 4);
        csobj.put("notifyType", "3001");
        TextWebSocketFrame csframe = new TextWebSocketFrame(csobj.toJSONString());
        if (distributorsList != null && distributorsList.size() > 0) {
            for (Object object : distributorsList) {
                String distributor = (String) object;
                if (!distributorId.equals(distributor)) {
                    if (csUserChannelMap != null && csUserChannelMap.size() > 0) {
                        Channel channel = csUserChannelMap.get(distributor);
                        if (channel != null) {
                            if (channel.isActive()) {
                                channel.writeAndFlush(csframe.retain());
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }
}
