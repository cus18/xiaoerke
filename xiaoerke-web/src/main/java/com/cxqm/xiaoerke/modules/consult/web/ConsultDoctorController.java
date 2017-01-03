
package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaMongoDBVo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * ConsultDoctor
 *
 * @author deliang
 * @version 2015-03-14
 */
@Controller
@RequestMapping(value = "consult/doctor")
public class ConsultDoctorController extends BaseController {

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoService;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private MessageContentConfService messageContentConfService;

    @RequestMapping(value = "/getCurrentUserHistoryRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getCurrentUserHistoryRecord(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String userId = (String) params.get("userId");
        String dateTime = (String) params.get("dateTime");
        Integer pageSize = (Integer) params.get("pageSize");
        List<ConsultRecordMongoVo> currentUserHistoryRecord = null;
        Date date = null;
        if (dateTime.indexOf("-") != -1) {
            date = DateUtils.StrToDate(dateTime, "datetime");
        } else if (dateTime.indexOf("/") != -1) {
            date = DateUtils.StrToDate(dateTime, "xiangang");
        }
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("createDate").lt(date)).
                with(new Sort(Sort.Direction.DESC, "createDate")).limit(pageSize);
        currentUserHistoryRecord = consultRecordService.getCurrentUserHistoryRecord(query);
        if (currentUserHistoryRecord != null) {
            response.put("consultDataList", ConsultUtil.transformCurrentUserListData(currentUserHistoryRecord));
        } else {
            response.put("consultDataList", "");
        }
        return response;
    }

    /***
     * 获取咨询今日会话的排名列表
     *
     * @param {"rankDate":"2016-03-14"}
     * @return {
     * "doctors": [
     * {
     * "csUserId": "fdasfa",
     * "doctorName": "liutao",
     * "consultNum": 20,
     * },
     * {
     * "csUserId": "fdasfa",
     * "doctorName": "liutao",
     * "consultNum": 26,
     * },
     * {
     * "csUserId": "fdasfa",
     * "doctorName": "liutao",
     * "consultNum": 40,
     * }
     * ]
     * }
     */
    @RequestMapping(value = "/rankList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> findConversationRankList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> searchMap = new HashMap<String, Object>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try {
            searchMap.put("rankDate", params.get("rankDate"));
            List<Map<String, Object>> resultValue = consultSessionForwardRecordsService.findConversationRankList(searchMap);
            resultMap.put("rankListValue", resultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /***
     * 获取聊天记录  分页
     *
     * @param
     * @return {
     * "pageNo":"2",
     * "pageSize":"20",
     * "records": [
     * {
     * "id":123
     * "session_id": 456,
     * "userId": "3Wisdfsdflaksjfsd234234j",
     * "message":"聊天内容"
     * "message_type": "yuyin",
     * "toUserId": "fdasfa",
     * "fromUserId": "liutao"
     * },
     * {
     * "id":456
     * "session_id": 345534,
     * "userId": "3Wisdfsdsdfsfjfsd234234j",
     * "message":"聊天内容"
     * "message_type": "yuyin",
     * "toUserId": "fdasfa",
     * "fromUserId": "liutao"
     * }
     * ]
     * }
     */
    @RequestMapping(value = "/recordList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordList(@RequestBody Map<String, Object> params) {
        String recordType = String.valueOf(params.get("recordType"));
        String userId = String.valueOf(params.get("userId"));
        String fromUserId = String.valueOf(params.get("fromUserId"));
        int pageNo = 0;
        int pageSize = 1;
        Query query;
        PaginationVo<ConsultRecordMongoVo> pagination = null;
        Map<String, Object> response = new HashMap<String, Object>();
        if (null != params.get("pageNo") && null != params.get("pageSize")) {
            pageNo = (Integer) params.get("pageNo");
            pageSize = (Integer) params.get("pageSize");
        }
        if (recordType.equals("all") && StringUtils.isNotNull(userId) && pageSize > 0) {
            query = new Query(where("userId").is(userId)).with(new Sort(Direction.ASC, "createDate"));//用户端获取与平台的所有聊天记录
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");

        } else if (recordType.equals("doctor") && StringUtils.isNotNull(userId) && StringUtils.isNotNull(fromUserId)) {//医生端获取与自己有关的所有聊天记录
            query = new Query(where("toUserId").is(userId).and("fromUserId")
                    .is(fromUserId)).with(new Sort(Direction.ASC, "createDate"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        } else if (recordType.equals("1") || recordType.equals("2")) {//查询语音、图片
            query = new Query(where("userId").is(userId).and("type")
                    .is(recordType)).with(new Sort(Direction.ASC, "createDate"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        }
        List<ConsultRecordMongoVo> recordMongoVos = new ArrayList<ConsultRecordMongoVo>();
        if (pagination.getDatas() != null && pagination.getDatas().size() > 0) {
            for (ConsultRecordMongoVo consultRecordMongoVo : pagination.getDatas()) {
                ConsultRecordMongoVo recordMongoVo = consultRecordMongoVo;
                recordMongoVo.setInfoDate(DateUtils.DateToStr(consultRecordMongoVo.getCreateDate(), "datetime"));
                recordMongoVos.add(recordMongoVo);
            }
            response.put("records", recordMongoVos);
        }
        response.put("currentPage", pageNo);
        response.put("pageSize", pageSize);
        response.put("totalPage", pagination.getTotalPage());
        return response;
    }

    /***
     * 生成聊天记录(咨询造数据)
     */
    @RequestMapping(value = "/produceRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void produceRecord(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
        consultRecordService.saveConsultRecord(consultRecordMongoVo);
    }

    /**
     * 获取客服医生列表
     */
    @RequestMapping(value = "/GetCSDoctorList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody//@RequestBody
    Map<String, Object> GetCSDoctorList(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> response = new HashMap<String, Object>();
        List<User> users;
        User user = new User();
        user.setUserType("consultDoctor");
        String userId = String.valueOf(params.get("userId"));
        if (StringUtils.isNotNull(userId)) {
            user.setId(userId);
        }
        String userName = String.valueOf(params.get("userName"));
        if (StringUtils.isNotNull(userName)) {
            user.setName(userName);
            users = consultDoctorInfoService.findUserByUserName(user);

        }else{
            users = consultDoctorInfoService.findUserOrderByDepartment(user);
        }
        if (users != null && users.size() > 0) {
            response.put("CSList", users);
            response.put("status", "success");
        }
        return response;
    }

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        String username = "13181557398";
//        User user = systemService.getUserByLoginName(username);
//        UserUtils.putCache("user", user);
//        User user1 = UserUtils.getUser();
//        System.out.println(user1);
        this.sendPost("http://s132.baodf.com/angel/consult/wechat/conversation", "openId=" + "123" +
                        "&messageType=" + "1" +
                        "&messageContent=" + "asd");
    }

    @RequestMapping(value = "/updateAddress", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void updateAddress(HttpSession session, HttpServletRequest request) {

        //获取正在咨询的会话
        ConsultSession consultSessionSearch = new ConsultSession();
        consultSessionSearch.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSessionSearch);
        //将用户的地址put上
        for(ConsultSession consultSession :consultSessions){
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(consultSession.getId());
            if(richConsultSession!=null){
                richConsultSession.setServerAddress("101.200.194.68");
                sessionRedisCache.putSessionIdConsultSessionPair(richConsultSession.getId(),richConsultSession);
            }

        }
    }

    public String sendPost(String url, String param) {
//                HttpRequestUtil.httpPost(param,url);

        PrintWriter out = null;
        BufferedReader in = null;

        String result = "";
        try {
            URL realUrl = new URL(url+"?"+param);
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

    /**
     * 医生选择一个用户，主动跟用户发起咨询会话  qwe
     */
    @RequestMapping(value = "/createDoctorConsultSession", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> createDoctorConsultSession(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String userId = (String) params.get("userId");
        String userName = (String) params.get("userName");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        //根据用户ID去查询，从历史会话记录中，获取用户最近的一条聊天记录，根据source判断会话来源
        HashMap<String, Object> response = ConsultSessionManager.INSTANCE.createConsultSession(userName, userId,sysPropertyVoWithBLOBsVo);
        return response;
    }

    /***
     * 用户在咨询完毕后，主动请求终止会话（医生和患者都可操作）
     *
     * @param
     * @return {
     * "result":"success"(失败返回failure)
     * }
     */
    @RequestMapping(value = "/sessionEnd", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> sessionEnd(@RequestParam(required = true) String sessionId,
                                   @RequestParam(required = true) String userId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        System.out.println("close session========" + sessionId + "==========userId========" + userId);
        LogUtils.saveLog(sessionId,userId);
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();
        params.put("openid", userId);
        params.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        params.put("starNum1", 0);
        params.put("starNum2", 0);
        params.put("starNum3", 0);
        params.put("doctorId", UserUtils.getUser().getId());
        params.put("content", "");
        params.put("dissatisfied", null);
        params.put("redPacket", null);
        params.put("evaluateSource", "realtimeConsult");
        if(StringUtils.isNotNull(sessionId)){
            params.put("consultSessionId",Integer.valueOf(sessionId));
        }
        //判断有没有正在转接的会话
        ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
        consultSessionForwardRecordsVo.setConversationId(Long.valueOf(sessionId));
        consultSessionForwardRecordsVo.setStatus("waiting");
        List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
        if (consultSessionForwardRecordsVos.size() > 0) {
            response.put("result", "existTransferSession");
            return response;
        }

//        patientRegisterPraiseService.saveCustomerEvaluation(params);
        if (StringUtils.isNotNull(sessionId)) {
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(Integer.valueOf(sessionId));
            if (richConsultSession != null) {
                if ("h5cxqm".equalsIgnoreCase(richConsultSession.getSource())) {
                    String patientId = richConsultSession.getUserId();
                    Channel userChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(patientId);
                    JSONObject obj = new JSONObject();
                    if (userChannel != null && userChannel.isActive()) {
                        obj.put("type", "4");
//                        obj.put("notifyType", "1003");
                        TextWebSocketFrame csframe = new TextWebSocketFrame(obj.toJSONString());
                        userChannel.writeAndFlush(csframe.retain());
                    } else {
                        String doctorId = richConsultSession.getCsUserId();
                        Channel doctorChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(doctorId);
                        obj.put("type", "4");
                        obj.put("notifyType", "0015");
                        TextWebSocketFrame csframe = new TextWebSocketFrame(obj.toJSONString());
                        doctorChannel.writeAndFlush(csframe.retain());
                    }
                } else if ("wxcxqm".equalsIgnoreCase(richConsultSession.getSource())) {

                    sessionEndWX(sessionId, userId, sysPropertyVoWithBLOBsVo, richConsultSession);
                }else if("h5bhq".equalsIgnoreCase(richConsultSession.getSource()) || "h5ykdl".equalsIgnoreCase(richConsultSession.getSource())){
                    sessionEndH5(sessionId, sysPropertyVoWithBLOBsVo, richConsultSession);
                }
            }
            String result = consultSessionService.clearSession(sessionId, userId);
            response.put("result", result);
            /*try{
                sendUmbrellaWechatMessage(userId,sysPropertyVoWithBLOBsVo);//咨询完毕发送保护伞消息，每个用户只发一次
            }catch (Exception e){
                System.out.println("咨询关闭发送保护伞消息"+e);
            }*/
            return response;
        } else {
            return null;
        }
    }

    private void sessionEndWX(@RequestParam(required = true) String sessionId, @RequestParam(required = true) String userId, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, RichConsultSession richConsultSession) {
        ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(richConsultSession.getUserId());

        if(consultSessionPropertyVo!=null && consultSessionPropertyVo.getMonthTimes() == 3){
            LogUtils.saveLog("ZXYQ_RK_TS_3", consultSessionPropertyVo.getSysUserId());
        }
        //判断是否有权限推送消息

        Map param = new HashMap();
        param.put("userId",richConsultSession.getCsUserId());
        param.put("consultSessionId",richConsultSession.getId());
        List<Map<String,Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(param);
        List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
        Map wechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String st = "";

        if(consultDoctorInfoVos !=null && consultDoctorInfoVos.size() >0) {
            if (null != consultDoctorInfoVos.get(0).getSendMessage() && consultDoctorInfoVos.get(0).getSendMessage().equals("1")) {
                if (praiseList != null && praiseList.size() > 0) {
                    for (Map<String, Object> evaluationMap : praiseList) {
                        if (Integer.parseInt((String) evaluationMap.get("serviceAttitude")) == 0) {
//                                        st = "医生太棒,要给好评;\n服务不好,留言吐槽. \n ----------\n【" +
//                                                "<a href='"+ sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=" +
//                                                evaluationMap.get("id") + "&sessionId=" + sessionId + "'>点击这里去评价</a>】";
                            //根据场景和日期查询是否有匹配的文案推送
                            MessageContentConfVo messageContentConfVo = messageContentConfService.messageConfInfo(MessageContentVo.SESSION_END.getVariable());
                            if(null != messageContentConfVo){
                                st = messageContentConfVo.getContent();
                                st = st.replace("CUSTOMERID", (String)evaluationMap.get("id"));
                                st = st.replace("SESSIONID",sessionId);
                            }
                            break;
                        } else {
                            st = "嗨，亲爱的,本次咨询已关闭。";
                            break;
                        }
                    }
                } else {
                    st = "嗨，亲爱的,本次咨询已关闭。";
                }
                WechatUtil.sendMsgToWechat((String) wechatParam.get("token"), userId, st);
                st =  "医生的及时解答很给力，有木有？下次还想要？\n戳戳手指，邀请好友加入宝大夫，免费机会就来咯！\n"+
                        "<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_3_backend'>>>邀请好友赚机会</a>";
                WechatUtil.sendMsgToWechat((String) wechatParam.get("token"), userId, st);
                LogUtils.saveLog("ZXYQ_RK_TS_N3",userId);

            }
            //分享的代码
//                    patientRegisterPraiseService.sendRemindMsgToUser(userId,sessionId);
        }
    }

    private void sessionEndH5(@RequestParam(required = true) String sessionId, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, RichConsultSession richConsultSession) {
        Map<String, Date> userConnectionTimeMapping = ConsultSessionManager.INSTANCE.getUserConnectionTimeMapping();
        Date oldDate = null;
        if(userConnectionTimeMapping.containsKey(richConsultSession.getUserId())){
            oldDate = userConnectionTimeMapping.get(richConsultSession.getUserId());
        }else{
            oldDate = new Date();
            userConnectionTimeMapping.put(richConsultSession.getUserId(),oldDate);
        }
        Channel userChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(richConsultSession.getUserId());
        Boolean flag = false ;
        if(userChannel !=null && userChannel.isActive()){
            for(int i= 0 ; i<= 1;i++){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("type", "4");
                jsonObj.put("notifyType", "0100");
                TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                userChannel.writeAndFlush(frame.retain());
                if(i == 1){
                    Date nowDate =  userConnectionTimeMapping.get(richConsultSession.getUserId());
                    if(nowDate != null && nowDate != oldDate){
                        flag = true;
                    }
                }else{
                    userConnectionTimeMapping.put(richConsultSession.getUserId(), new Date());
                }
            }
            if(flag){
                if("h5bhq".equalsIgnoreCase(richConsultSession.getSource())){
                    net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
                    noReadMsg.put("action","doctorCloseSession");
                    noReadMsg.put("sessionId",sessionId);
                    noReadMsg.put("uid",richConsultSession.getUserId());
                    String currentUrl = sysPropertyVoWithBLOBsVo.getCoopBhqUrl();
                    if(StringUtils.isNull(currentUrl)){
                        currentUrl = "http://coapi.baohuquan.com/baodaifu";
                    }
                    String method = "POST";
                    String dataType="json";
                    String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
                    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
                    if(jsonObject.containsKey("error_code") && (Integer)jsonObject.get("error_code") != 0 ){
                        CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
                    }
                }else if("h5ykdl".equalsIgnoreCase(richConsultSession.getSource())){
                    net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
                //    noReadMsg.put("action","doctorCloseSession");
                    /*if(richConsultSession.getCsUserName().contains("宝大夫")){
                        noReadMsg.put("doctorName",richConsultSession.getCsUserName());
                    }else{
                        noReadMsg.put("doctorName","宝大夫"+richConsultSession.getCsUserName());
                    }*/
                    noReadMsg.put("doctorName",richConsultSession.getCsUserName());
                    noReadMsg.put("doctorId",richConsultSession.getCsUserId());
                    noReadMsg.put("sessionId",sessionId);
                    noReadMsg.put("uid",richConsultSession.getUserId());
                    String currentUrl = sysPropertyVoWithBLOBsVo.getCoopYkdlUrl() +"/consult_over";
                    if(StringUtils.isNull(currentUrl)){
                        currentUrl = "https://wxsp.ykhys.com/thirdparty/baodaifu/consult_over";
                    }
                    String method = "POST";
                    String dataType="json";
                    String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
                    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
                    if(jsonObject.containsKey("error_msg") && !"success".equals(jsonObject.get("error_msg"))){
                        CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
                    }
                }
            }
        }else{
            if("h5bhq".equalsIgnoreCase(richConsultSession.getSource())){
                net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
                noReadMsg.put("action","doctorCloseSession");
                noReadMsg.put("sessionId",sessionId);
                noReadMsg.put("uid",richConsultSession.getUserId());
                String currentUrl = sysPropertyVoWithBLOBsVo.getCoopBhqUrl();
                if(StringUtils.isNull(currentUrl)){
                    currentUrl = "http://coapi.baohuquan.com/baodaifu";
                }
                String method = "POST";
                String dataType="json";
                String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
                if(jsonObject.containsKey("error_code") && (Integer)jsonObject.get("error_code") != 0 ){
                    CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
                }
            }else if("h5ykdl".equalsIgnoreCase(richConsultSession.getSource())){
                net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
                //    noReadMsg.put("action","doctorCloseSession");
               /* if(richConsultSession.getCsUserName().contains("宝大夫")){
                    noReadMsg.put("doctorName",richConsultSession.getCsUserName());
                }else{
                    noReadMsg.put("doctorName","宝大夫"+richConsultSession.getCsUserName());
                }*/
                noReadMsg.put("doctorId",richConsultSession.getCsUserId());
                noReadMsg.put("doctorName",richConsultSession.getCsUserName());
                noReadMsg.put("sessionId",sessionId);
                noReadMsg.put("uid",richConsultSession.getUserId());
                String currentUrl = sysPropertyVoWithBLOBsVo.getCoopYkdlUrl()+"/consult_over";
                if(StringUtils.isNull(currentUrl)){
                    currentUrl = "https://wxsp.ykhys.com/thirdparty/baodaifu/consult_over";
                }
                String method = "POST";
                String dataType="json";
                String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
                if(jsonObject.containsKey("error_msg") && !"success".equals(jsonObject.get("error_msg")) ){
                    CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
                }
            }
        }
    }

    private void sendUmbrellaWechatMessage(String openId,SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo){
        Query queryDate = new Query();
        queryDate.addCriteria(Criteria.where("openid").is(openId));
        List<UmbrellaMongoDBVo> openidlist = babyUmbrellaInfoService.getUmbrellaMongoDBVoList(queryDate);
        if(openidlist.size()==0){//没有发过的才发，只发一次
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("openid", openId);
            List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
            if(list.size()==0||!"success".equals(list.get(0).get("pay_result"))){
                Map parameter = systemService.getWechatParameter();
                String token = (String)parameter.get("token");
                int count = babyUmbrellaInfoService.getUmbrellaCount();
                String title = "免费送您一份40万的保障！";
                String description = "限时免费加入宝护伞爱心公益，小孩、大人得了重病都给钱！最高40万！包括75种疾病，还能安排专家治疗！";
                //String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellab";
                String url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/umbrella#/umbrellaLead/130000003/a";
                String picUrl = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella";
                String message = "{\"touser\":\""+ openId+"\",\"msgtype\":\"news\",\"news\":{\"articles\": [{\"title\":\""+ title +"\",\"description\":\""+description+"\",\"url\":\""+ url +"\",\"picurl\":\""+picUrl+"\"}]}}";

                String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" +
                        token + "", "POST", message);
                System.out.println(jsonobj + "===============================");
                UmbrellaMongoDBVo vo = new UmbrellaMongoDBVo();
                vo.setOpenid(openId);
                vo.setCreateDate(new Date());
                babyUmbrellaInfoService.saveOpenidToMongoDB(vo);
            }
        }
    }

    /**
     * 医生选择一个用户，主动跟用户发起咨询会话  qwe
     */
    @RequestMapping(value = "/getSystemTime", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getSystemTime() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("dateTime", new Date());
        return response;
    }

    /**
     * 获取咨询医生主页信息
     */
    @RequestMapping(value = "/getConsultDoctorHomepageInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getConsultDoctorHomepageInfo(@RequestParam(required = true) String userId) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response = consultDoctorInfoService.getConsultDoctorHomepageInfo(userId);
        return response;
    }

    /**
     * 获取咨询医生主页的信息
     */
    @RequestMapping(value = "/findDoctorAllEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> findDoctorAllEvaluation(@RequestParam(required = true) String userId,@RequestParam(required = true) int pageNo,@RequestParam(required = true) int pageSize) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("doctorId",userId);
        param.put("startRowNo",pageNo*10);
        param.put("pageSize",pageSize);
        response = consultDoctorInfoService.findDoctorAllEvaluation(param);
        return response;
    }
}
