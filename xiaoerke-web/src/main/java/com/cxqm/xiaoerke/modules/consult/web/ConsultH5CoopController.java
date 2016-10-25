package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.ConsultCoopUserInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultEvaluateCoopService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultCoopUserInfoMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 合作方controller
 * Created by jiangzhongge on 2016-7-12.
 */
@Controller
@RequestMapping(value = "consult/cooperate")
public class ConsultH5CoopController {

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultEvaluateCoopService consultEvaluateCoopService;

    @Autowired
    private ConsultCoopUserInfoService consultCoopUserInfoService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    //jiangzg add
    private ConsultCoopUserInfoMongoDBServiceImpl consultCoopUserInfoMongoDBService = SpringContextHolder.getBean("consultCoopUserInfoMongoDBServiceImpl");

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "createConsultSessionForCoop", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map createConsultSessionForCoop(@RequestBody HashMap param) {
        Map response = new HashMap();
        if (param.containsKey("source")) {
            String source = String.valueOf(param.get("source"));
            String userId = String.valueOf(param.get("userId"));
            ConsultCoopUserInfoVo consultCoopUserInfoVo = null;
            if (source.contains("ykdl") || source.contains("YKDL")) {
                source = "COOP_YKDL";
                String nickName = String.valueOf(param.get("nickName"));
                String headImg = String.valueOf(param.get("headImg"));
                param.put("serverAddress", ConstantUtil.SERVER_ADDRESS);
                param.put("source", source);
                Query query = new Query().addCriteria(where("userId").is(userId).and("source").regex("COOP_YKDL"));
                long count = consultCoopUserInfoMongoDBService.queryCountByCollectionName(query, "consultCoopUserInfoVo");
                if (count == 0) {
                    consultCoopUserInfoVo = new ConsultCoopUserInfoVo();
                    consultCoopUserInfoVo.setSource(source);
                    consultCoopUserInfoVo.setUserName(nickName);
                    consultCoopUserInfoVo.setCreateDate(new Date());
                    consultCoopUserInfoVo.setDelFlag(0);
                    consultCoopUserInfoVo.setHeadImg(headImg);
                    consultCoopUserInfoVo.setSex("暂无");
                    consultCoopUserInfoVo.setUserId(userId);
                    consultCoopUserInfoVo.setUserPhone("暂无");
                    consultCoopUserInfoMongoDBService.insert(consultCoopUserInfoVo);
                    consultCoopUserInfoService.saveConsultCoopUserInfo(consultCoopUserInfoVo);
                }else{
                    Runnable thread = new processUserMessageThread(param);
                    threadExecutor.execute(thread);
                    response.put("status", "success");
                    response.put("code", 0);
                    response.put("info", "connection is established");
                }
            }
        }else {
            response.put("status", "failure");
            response.put("code", 1);
            response.put("info", "no doctor online");
        }
        return response;
    }

    public class processUserMessageThread extends Thread {
        private HashMap<String, Object> param;

        public processUserMessageThread(HashMap<String, Object> paramMap) {
            this.param = paramMap;
        }

        public void run() {
            String source = String.valueOf(param.get("source"));
            RichConsultSession richConsultSession = null;
            Integer sessionId = null;
            Channel csChannel = null;
            if(source.equals("COOP_YKDL")){
                String userName = String.valueOf(param.get("nickName"));
                String message = String.valueOf(param.get("message"));
                String type = String.valueOf(param.get("type"));
                int messageType = 0;
                if(type.equals("0") || type.contains("text")){
                    messageType = 0;
                }else if(type.equals("1") || type.contains("image")) {
                    messageType = 1;
                }
                String headImg = String.valueOf(param.get("headImg"));
                String serverAddress = String.valueOf(param.get("serverAddress"));
                String userId = String.valueOf(param.get("userId"));
                if(StringUtils.isNull(userName)){
                    if (userId.length() > 20) {
                        userName = userId.substring(userId.length() - 8, userId.length());
                    } else {
                        userName = userId.substring(0, 5);
                    }
                }
                sessionId = sessionRedisCache.getSessionIdByUserId(userId);
                if(sessionId != null){
                    richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                }
                if(richConsultSession != null){
                    int currentNum = richConsultSession.getConsultNum() + 1;
                    richConsultSession.setConsultNum(currentNum);
                    sessionRedisCache.putSessionIdConsultSessionPair(sessionId, richConsultSession);
                    csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(richConsultSession.getCsUserId());
                    if (csChannel == null) {
                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(messageType), message, richConsultSession);
                        //更新会话操作时间
                        consultRecordService.saveConsultSessionStatus(richConsultSession);
                    } else {
                        System.out.println("csChannel.isActive()------" + csChannel.isActive());
                        if (!csChannel.isActive()) {
                            //保存聊天记录
                            consultRecordService.buildRecordMongoVo(userId, String.valueOf(messageType), message, richConsultSession);
                            //更新会话操作时间
                            consultRecordService.saveConsultSessionStatus(richConsultSession);
                        }
                    }
                }else{
                    //如果此用户是第一次发送消息，则sessionId为空
                    richConsultSession = new RichConsultSession();
                    richConsultSession.setCreateTime(new Date());
                    richConsultSession.setUserId(userId);
                    richConsultSession.setUserName(userName);
                    richConsultSession.setSource(source);
                    richConsultSession.setServerAddress(serverAddress);
                    /**
                     * 新增咨询次数字段 jiangzg 2016-9-8 17:33:17
                     */
                    richConsultSession.setConsultNum(1);
                    //创建会话，发送消息给用户，给用户分配接诊员
                    HashMap reqResult = ConsultSessionManager.INSTANCE.createUserConsultSessionForCoop(richConsultSession);
                    if (reqResult != null) {
                        csChannel = (Channel) reqResult.get("csChannel");
                        richConsultSession = (RichConsultSession) reqResult.get("consultSession");
                        sessionId = richConsultSession.getId();
                    }
                    //咨询收费处理
 //                   consultTimes = consultCharge(openId, sessionId, consultSession);
                    sessionRedisCache.putSessionIdConsultSessionPair(sessionId, richConsultSession);
                    sessionRedisCache.putUserIdSessionIdPair(richConsultSession.getUserId(), sessionId);
                }

                //会话创建成功，拿到了csChannel,给接诊员(或是医生)发送消息
                if (csChannel != null) {
                    try {
                        com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
                        obj.put("sessionId", sessionId);
                        obj.put("senderId", userId);
                        obj.put("dateTime", DateUtils.DateToStr(new Date()));
                        obj.put("senderName", userName);
                        obj.put("serverAddress", serverAddress);
                        System.out.println("serverAddress------" + serverAddress);
                        obj.put("source", richConsultSession.getSource());

                        StringBuffer sbf = new StringBuffer();
                        if (type.equals("text") || type.equals("0")) {
                            obj.put("type", 0);
                            obj.put("content", URLDecoder.decode(message, "utf-8"));
                        } else {
                            if (type.contains("image") || type.equals("1")) {
                                obj.put("type", 1);
                                obj.put("content", URLDecoder.decode(message, "utf-8"));
                            }
                        }
                        /**
                         * jiangzg add 2016-9-8 17:44:45 增加消息数量
                         */
                        obj.put("consultNum",richConsultSession.getConsultNum());
                        System.out.println("here csChannel is" + csChannel);
                        TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                        csChannel.writeAndFlush(frame.retain());

                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(messageType), message, richConsultSession);
                        consultRecordService.saveConsultSessionStatus(richConsultSession);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        e.getMessage();
                        System.out.println("=====sdf====="+e.getMessage()+"==="+e.getCause());
                    }
                }
            }

        }
    }


    @RequestMapping(value = "sendConsultMessageForCoop", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map sendConsultMessageForCoop(@RequestBody HashMap map) {
        Runnable threads = new SendMessageToCoopUser(map);
        threadExecutor.execute(threads);
        return null;
    }

    public class SendMessageToCoopUser implements Runnable {

        private HashMap<String, Object> params;

        public SendMessageToCoopUser(HashMap<String, Object> paramMap) {
            this.params = paramMap;
        }

        @Override
        public void run() {
            if (params.containsKey("source")) {
                String source = String.valueOf(params.get("source"));
                if (source.contains("ykdl") || source.contains("YKDL")) {
                    String userId = String.valueOf(params.get(""));
                    String csUserId = String.valueOf(params.get(""));
                    Integer sessionId = Integer.valueOf(String.valueOf(params.get("")));
                    String type = String.valueOf(params.get(""));
                    String senderId = String.valueOf(params.get(""));
                    String userName = String.valueOf(params.get(""));
                    String message = String.valueOf(params.get("message"));
                    RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                    consultRecordService.buildRecordMongoVo(senderId,type, message, richConsultSession);
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("userId",userId);
                    jsonObj.put("csUserId",csUserId);
                    jsonObj.put("sessionId",sessionId);
                    jsonObj.put("type",type);
                    jsonObj.put("senderId",senderId);
                    jsonObj.put("userName",userName);
                    jsonObj.put("source",source);
                    String currentUrl = "";
                    String method = "GET";
                    String dataType = "json";
                    String result = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType,null, jsonObj.toString() , 2);
                    if (result != null) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        if (StringUtils.isNotNull((String) jsonObject.get("avatar"))) {

                        }
                    }

                }
            } else {
                System.out.println("source is null");
            }
        }
    }

    @RequestMapping(value = "/saveUserInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String saveCoopUserInfoByToken(@RequestParam(required = true) String token, Model model) {
                /*String currentUrl = "http://rest.tx2010.com/user/current";   //获取当前登录人信息
                String childrenUrl = "http://rest.tx2010.com/user/children"; //获取当前登录人的孩子信息
                token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
                String access_token = "{'X-Access-Token':'" + token + "'}";
                String method = "GET";
                String dataType = "json";*/
                /*String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token,"",2);
                if(str != null){
                        model.addAttribute("currentUser",str);
                        String result = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token,"",2);
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        if(result != null){
                                model.addAttribute("currentChildren",result);
                                JSONObject jsonObject1 = JSONObject.fromObject(result);
                        }
                }*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 123);
        jsonObject.put("userId", "123test");
        jsonObject.put("csUserId", "12345");
        jsonObject.put("evaLevel", 0);
        jsonObject.put("suggestMsg", "不错哦");
        jsonObject.put("evaDate", "2016-08-24 12:30:20");
        jsonObject.put("source", "wxcxqm");
        consultEvaluateUserByCoop(jsonObject);
        return "11111";
    }

    @RequestMapping(value = "/getHistoryRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getUserHistoryRecord(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        String userId = (String) params.get("userId");
        String dateTime = (String) params.get("dateTime");
        Integer pageSize = (Integer) params.get("pageSize");
        String currentUrl = sysPropertyVoWithBLOBsVo.getCoopWjyUrl();
        if (StringUtils.isNull(currentUrl)) {
            currentUrl = "http://rest.tx2010.com/user/current";   //微家园获取当前登录人信息
        }
        String imgUrl = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png";
        String docHeaderImg = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png";
//                String token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
        if (params.containsKey("token") && StringUtils.isNotNull(String.valueOf(params.get("token")))) {
            String token = (String) params.get("token");
            String access_token = "{'X-Access-Token':'" + token + "'}";
            String method = "GET";
            String dataType = "json";
            String result = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token, "", 2);
            if (result != null) {
                JSONObject jsonObject = JSONObject.fromObject(result);
                if (StringUtils.isNotNull((String) jsonObject.get("avatar"))) {
                    imgUrl = (String) jsonObject.get("avatar");
                }
            }
        }
        if (params.containsKey("ykdlToken") && StringUtils.isNotNull(String.valueOf(params.get("ykdlToken")))) {
            String id = (String) params.get("ykdlToken");
            String remoteUrl = String.valueOf(params.get("remoteUrl"));
            String method = "POST";
            String dataType = "json";
            String data = "{\"user_uuid\":\""+id+"\"}";
            String result = CoopConsultUtil.getCurrentUserInfo(remoteUrl, method, dataType, null, data, 4);
            if (result != null) {
                JSONObject jsonObject = JSONObject.fromObject(result);
                if (StringUtils.isNotNull((String) jsonObject.get("headimgurl"))) {
                    imgUrl = (String) jsonObject.get("headimgurl");
                }
            }
        }
        List<ConsultRecordMongoVo> currentUserHistoryRecord = null;
        Date date = null;
        if (dateTime.indexOf("-") != -1) {
            date = DateUtils.StrToDate(dateTime, "datetime");
        } else if (dateTime.indexOf("/") != -1) {
            date = DateUtils.StrToDate(dateTime, "xianggang");
        }
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("createDate").lt(date)).
                with(new Sort(Sort.Direction.DESC, "createDate")).limit(pageSize);
        currentUserHistoryRecord = consultRecordService.getCurrentUserHistoryRecord(query);
        List<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
        if (currentUserHistoryRecord != null) {
            for (ConsultRecordMongoVo dataVo : currentUserHistoryRecord) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                if (StringUtils.isNotNull(dataVo.getSenderId())) {
                    if (userId.equals(dataVo.getSenderId())) {
                        dataMap.put("type", dataVo.getType());
                        dataMap.put("content", dataVo.getMessage());
                        dataMap.put("dateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dataVo.getCreateDate()));
                        dataMap.put("senderId", dataVo.getSenderId());
                        if(dataVo.getSource().contains("ykdl")){
                            dataMap.put("senderName", "YKDL-"+dataVo.getSenderName());
                        }else if(dataVo.getSource().contains("wjy")){
                            dataMap.put("senderName", "微家园-"+dataVo.getSenderName());
                        }else if(dataVo.getSource().contains("bhq")){
                            dataMap.put("senderName", "宝护圈-"+dataVo.getSenderName());
                        }else{
                            dataMap.put("senderName", dataVo.getSenderName());
                        }
                        dataMap.put("sessionId", dataVo.getSessionId());
                        dataMap.put("avatar", imgUrl);
                    } else {
                        dataMap.put("type", dataVo.getType());
                        dataMap.put("content", dataVo.getMessage());
                        dataMap.put("dateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dataVo.getCreateDate()));
                        dataMap.put("senderId", dataVo.getSenderId());
                        dataMap.put("senderName", dataVo.getSenderName());
                        dataMap.put("sessionId", dataVo.getSessionId());
                        dataMap.put("avatar", docHeaderImg);
                    }
                }
                listData.add(dataMap);
            }
            response.put("consultDataList", listData);
        } else {
            response.put("consultDataList", "");
        }
        return response;
    }

    @RequestMapping(value = "/baohuquan", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public
    @ResponseBody
    JSONObject theInterfaceOfBHQ(@RequestBody Map params) {
        JSONObject jsonObject = new JSONObject();
        if (params != null) {
            if (params.containsKey("action") && "evaluteDocker".equalsIgnoreCase(String.valueOf(params.get("action")))) {
                jsonObject = this.consultEvaluateUserByCoop(params);
            } else if (params.containsKey("action") && "getConsultDataByCoop".equalsIgnoreCase(String.valueOf(params.get("action")))) {
                jsonObject = this.getConsultDataByCoop(params);
            }
        }

        return jsonObject;
    }

    /**
     * 保护圈聊天记录抓取
     * jiangzg 2016-8-23 12:00:37
     */
    @RequestMapping(value = "/getConsultDataByCoop", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public
    @ResponseBody
    JSONObject getConsultDataByCoop(@RequestBody Map params) {

        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        String source = String.valueOf(params.get("source"));
        String startTime = String.valueOf(params.get("startTime"));
        String endTime = String.valueOf(params.get("endTime"));
        String secCode = String.valueOf(params.get("secCode"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(calendar.YEAR) - 2000;
        int currentMonth = calendar.get(calendar.MONTH) + 1;
        int currentDate = calendar.get(calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(calendar.HOUR_OF_DAY);

        JSONObject jsonObject = new JSONObject();
        String secCodeNow = "";
        if (StringUtils.isNotNull(source)) {
            if (currentMonth < 10) {
                if(currentDate < 10){
                    secCodeNow = "0"+currentDate + "0" + currentMonth + currentYear + source;
                }else{
                    secCodeNow = currentDate + "0" + currentMonth + currentYear + source;
                }
            } else {
                if(currentDate < 10){
                    secCodeNow = "0"+currentDate + currentMonth + currentYear + source;
                }else{
                    secCodeNow = currentDate + currentMonth + currentYear + source;
                }
            }
        } else {
            if (currentMonth < 10) {
                if(currentDate < 10){
                    secCodeNow = "0"+currentDate + "0" + currentMonth + currentYear;
                }else{
                    secCodeNow = currentDate + "0" + currentMonth + currentYear ;
                }
            } else {
                if(currentDate < 10){
                    secCodeNow = "0"+currentDate + currentMonth + currentYear;
                }else{
                    secCodeNow = ""+currentDate + currentMonth + currentYear;
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long differTime = 0;
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long startDate = start.getTime();
            long endDate = end.getTime();
            differTime = endDate - startDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotNull(source)) {
            if("COOP_BHQ".equalsIgnoreCase(source)){
                source = "h5bhq";
            }else if("COOP_YKDL".equalsIgnoreCase(source)){
                source = "h5ykdl";
            }
            if (currentHour > 0 && currentHour < 6) {
                if (secCodeNow.equalsIgnoreCase(secCode)) {
                    if (differTime > 1000 * 60 * 60 * 2) {
                        jsonObject.put("status", "failure");
                        jsonObject.put("code", 1);
                        jsonObject.put("info", "您好，您请求的数据时间间隔超过2小时！");
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            /**
                             * 加入聊天记录提取代码
                             */
                            Query query = new Query(Criteria.where("createDate").exists(true).andOperator(Criteria.where("createDate").gte(sdf.parse(startTime)), Criteria.where("createDate").lte(sdf.parse(endTime))).and("source").is(source));
                            query.with(new Sort(Sort.Direction.DESC, "createDate"));
                            List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.getCurrentUserHistoryRecord(query);
                            if (consultRecordMongoVos != null && consultRecordMongoVos.size() > 0) {
                                for (ConsultRecordMongoVo consultRecordMongoVo : consultRecordMongoVos) {
                                    JSONObject data = new JSONObject();
                                    data.put("id", consultRecordMongoVo.getSessionId());
                                    data.put("userName", consultRecordMongoVo.getNickName());
                                    data.put("userId", consultRecordMongoVo.getUserId());
                                    data.put("csUserId", consultRecordMongoVo.getCsUserId());
                                    data.put("senderId", consultRecordMongoVo.getSenderId());
                                    if(consultRecordMongoVo.getMessage().contains("：")){
                                        data.put("message", consultRecordMongoVo.getMessage().substring(consultRecordMongoVo.getMessage().indexOf("：")+1));
                                    }else{
                                        data.put("message", consultRecordMongoVo.getMessage());
                                    }
                                    data.put("type", consultRecordMongoVo.getType());
                                    data.put("createDate", consultRecordMongoVo.getCreateDate());
                                    jsonArray.add(data);
                                }
                            }
                            jsonObject.put("status", "success");
                            jsonObject.put("code", 0);
                            jsonObject.put("info", "数据提取成功！");
                            jsonObject.put("datalist", jsonArray);
                        } catch (Exception ex) {
                            jsonObject.put("status", "failure");
                            jsonObject.put("code", 1);
                            jsonObject.put("info", "连接请求超时！");
                            ex.getStackTrace();
                        }

                    }
                } else {
                    jsonObject.put("status", "failure");
                    jsonObject.put("code", 1);
                    jsonObject.put("info", "您好，请输入正确的安全密钥，谢谢！");
                }
            } else {
                jsonObject.put("status", "failure");
                jsonObject.put("code", 1);
                jsonObject.put("info", "您好，请不要在咨询高峰期间提取数据，请在凌晨0点到6点之间提取，谢谢！");
            }
        }
        return jsonObject;
    }

    /**
     * 保护圈评价消息接口
     * jiangzg 2016-8-23 12:00:37
     */
    @RequestMapping(value = "/consultEvaluateUserByCoop", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public
    @ResponseBody
    JSONObject consultEvaluateUserByCoop(@RequestBody Map params) {
        JSONObject jsonObject = new JSONObject();
        int sessionId = StringUtils.isNotNull(String.valueOf(params.get("sessionId"))) ? Integer.valueOf(String.valueOf(params.get("sessionId"))) : 0;
        String userId = String.valueOf(params.get("userId"));
        String csUserId = "";
        if (params.containsKey("csUserId")) {
            csUserId = String.valueOf(params.get("csUserId"));
        }
        int evaLevel = StringUtils.isNotNull(String.valueOf(params.get("socre"))) ? Integer.valueOf(String.valueOf(params.get("socre"))) : 0;
        //0：代表非常满意  1：代表一般 2：代表不满意
        String suggestMsg = String.valueOf(params.get("suggestMsg"));
        if(StringUtils.isNotNull(suggestMsg)){
            try {
                suggestMsg  = new String(org.springframework.security.crypto.codec.Base64.decode(suggestMsg.getBytes("utf-8")),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            suggestMsg = "";
        }

        String evaDate = String.valueOf(params.get("evaDate"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date evaluateDate = null;
        if (StringUtils.isNotNull(evaDate)) {
            try {
                evaluateDate = sdf.parse(evaDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            evaluateDate = new Date();
        }
        String source = String.valueOf(params.get("source"));
        int result = 0;
        /**
         *  加入评价信息添加
         */
        ConsultEvaluateCoopVo consultEvaluateCoopVo = new ConsultEvaluateCoopVo();
        consultEvaluateCoopVo.setSessionId(sessionId);
        consultEvaluateCoopVo.setSource(source);
        List<ConsultEvaluateCoopVo> count = consultEvaluateCoopService.getConsultEvaluateCoops(consultEvaluateCoopVo);
        if(count.size() > 0){
            System.out.println("已评价");
        } else{
            consultEvaluateCoopVo.setCreateDate(new Date());
            consultEvaluateCoopVo.setEvaluateDate(evaluateDate);
            consultEvaluateCoopVo.setEvaluateLevel(evaLevel);
            consultEvaluateCoopVo.setSessionId(sessionId);
            consultEvaluateCoopVo.setSuggestMessage(suggestMsg);
            consultEvaluateCoopVo.setUserId(userId);
            result = consultEvaluateCoopService.addConsultEvaluateCoops(consultEvaluateCoopVo);
        }
        if (result > 0) {
            jsonObject.put("status", "success");
            jsonObject.put("error_code", 0);
            jsonObject.put("info", "数据接收成功！");
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("error_code", "1");
            jsonObject.put("error_message", "已经评价过，无需多次评价！");
        }
        return jsonObject;
    }
}
