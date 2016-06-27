
package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
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
    @ResponseBody
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
        users = systemService.findUserByUserType(user);
        if (users != null && users.size() > 0) {
            response.put("CSList", users);
            response.put("status", "success");
        }
        return response;
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

        //根据用户ID去查询，从历史会话记录中，获取用户最近的一条聊天记录，根据source判断会话来源
        HashMap<String, Object> response = ConsultSessionManager.getSessionManager().createConsultSession(userName, userId);
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

        System.out.println("close session========" + sessionId + "==========userId========" + userId);
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
        //判断有没有正在转接的会话
        ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
        consultSessionForwardRecordsVo.setConversationId(Long.valueOf(sessionId));
        consultSessionForwardRecordsVo.setStatus("waiting");
        List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
        if (consultSessionForwardRecordsVos.size() > 0) {
            response.put("result", "existTransferSession");
            return response;
        }

        patientRegisterPraiseService.saveCustomerEvaluation(params);
        if (StringUtils.isNotNull(sessionId)) {
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(Integer.valueOf(sessionId));
            if (richConsultSession != null) {
                if ("h5cxqm".equalsIgnoreCase(richConsultSession.getSource())) {
                    String patientId = richConsultSession.getUserId();
                    Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(patientId);
                    JSONObject obj = new JSONObject();
                    if (userChannel != null && userChannel.isActive()) {
                        obj.put("type", "4");
                        obj.put("notifyType", "1003");
                        TextWebSocketFrame csframe = new TextWebSocketFrame(obj.toJSONString());
                        userChannel.writeAndFlush(csframe.retain());
                    } else {
                        String doctorId = richConsultSession.getCsUserId();
                        Channel doctorChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(doctorId);
                        obj.put("type", "4");
                        obj.put("notifyType", "0015");
                        TextWebSocketFrame csframe = new TextWebSocketFrame(obj.toJSONString());
                        doctorChannel.writeAndFlush(csframe.retain());
                    }
                } else if ("wxcxqm".equalsIgnoreCase(richConsultSession.getSource())) {

                    //判断是否有权限推送消息
                    Map param = new HashMap();
                    param.put("userId",richConsultSession.getCsUserId());
                    List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
                    Map wechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                    if(consultDoctorInfoVos !=null && consultDoctorInfoVos.size() >0){
                        if(null !=consultDoctorInfoVos.get(0).getSendMessage() && consultDoctorInfoVos.get(0).getSendMessage().equals("1")){
                            String st = "医生太棒,要给好评;\n服务不好,留言吐槽. \n ----------\n【" +
                                    "<a href='http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=" +
                                    params.get("uuid") + "'>点击这里去评价</a>】";
                            WechatUtil.sendMsgToWechat((String) wechatParam.get("token"), userId, st);
                        }
                    }
                    //jiangzg 2016年6月21日16:22:59 add ps:在线咨询仅供参考
                    String csUserId = richConsultSession.getCsUserId();
                    if(StringUtils.isNotNull(csUserId)){
                        List<Map> consultDoctorInfo = consultDoctorInfoService.getDoctorInfoMoreByUserId(csUserId);
                        if(consultDoctorInfo != null && consultDoctorInfo.size() > 0){
                            if(consultDoctorInfo.get(0).get("userType") !=null && "consultDoctor".equalsIgnoreCase((String)consultDoctorInfo.get(0).get("userType"))){
                                WechatUtil.sendMsgToWechat((String) wechatParam.get("token"), userId, "感谢您咨询宝大夫，因不能面诊，在线咨询回复仅供参考！");
                            }
                        }
                    }
                }
            }
            String result = consultSessionService.clearSession(sessionId, userId);
            response.put("result", result);
            return response;
        } else {
            return null;
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
}
