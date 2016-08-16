/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.CoopConsultUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/user")
public class ConsultUserController extends BaseController {

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultPayUserService consultPayUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CoopThirdBabyInfoService coopThirdBabyInfoService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "/getCurrentSessions", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCurrentSessions(@RequestParam(required = true) String csUserId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        ConsultSession consultSession = new ConsultSession();
        consultSession.setCsUserId(csUserId);
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        List<Object> sessionIds = new ArrayList<Object>();
        for (ConsultSession session : consultSessions) {
            sessionIds.add(session.getId());
        }

        List<Object> sessions = ConsultSessionManager.getSessionManager().getCurrentSessions(sessionIds);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", 0);
        response.put("msg", "OK");
        response.put("sessions", sessions);
        return response;
    }


    /**
     * 获取会话接入次数
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserSessionTimesByUserId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserSessionTimesByUserId(@RequestParam(required = true) String userId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        ConsultSession consultSession = new ConsultSession();
        consultSession.setUserId(userId);
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        response.put("userSessionTimes", consultSessions == null ? 1 : consultSessions.size());
        return response;
    }

    @RequestMapping(value = "/getCurrentUserByCSId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCurrentUserByCSId(@RequestParam(required = true) String csUserId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> response = new HashMap<String, Object>();
        ConsultSession consultSession = new ConsultSession();
        if (StringUtils.isNull(csUserId)) {
            csUserId = UserUtils.getUser().getId();
        }
        consultSession.setCsUserId(csUserId);
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);

        List<ConsultSession> consultSessions = null;//consultSessionService.getAlreadyAccessUsers(consultSession);
        if (consultSessions != null && consultSessions.size() > 0) {
            response.put("alreadyAccessUsers", consultSessions);
        }
        response.put("status", 0);
        response.put("msg", "OK");

        return response;
    }

    /**
     * 聊天咨询文件上传
     *
     * @param file
     * @param data
     * @return {"status","success"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/uploadMediaFile", method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap<String, Object> UploadFile(@RequestParam("file") MultipartFile file,
                                              @RequestParam("data") String data) throws UnsupportedEncodingException {
        HashMap<String, Object> response = consultRecordService.uploadMediaFile(file, data);
        return response;
    }

    /**
     * 获取客户列表(或咨询过某个医生的客户)详细信息,按照时间降序排序
     */
    @RequestMapping(value = "/getUserList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserList(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> response = new HashMap<String, Object>();
        Integer pageNo = (Integer) params.get("pageNo");
        Integer pageSize = (Integer) params.get("pageSize");
        Integer dateNum = (Integer) params.get("dateNum");
        String csUserId = String.valueOf(params.get("CSDoctorId"));
        Date date = null;
        if (dateNum != 10000) {
            String dateTemp;
            Calendar ca = Calendar.getInstance();
            if (dateNum == 0) {
                ca.set(Calendar.HOUR, 0);
                ca.set(Calendar.SECOND, 0);
                ca.set(Calendar.MINUTE, 0);
            } else {
                ca.add(Calendar.DATE, -dateNum);// 30为增加的天数，可以改变的
            }
            dateTemp = DateUtils.DateToStr(ca.getTime(), "datetime");
            date = DateUtils.StrToDate(dateTemp, "datetime");
        }

        Query query;
        if (dateNum == 10000) {
            if (csUserId.equals("allCS")) {
                query = new Query().with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            } else {
                query = new Query().addCriteria(new Criteria().where("csUserId").regex(csUserId)).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            }
        } else if (!csUserId.equals("allCS")) {
            query = new Query().addCriteria(new Criteria().where("csUserId").regex(csUserId).andOperator(Criteria.where("lastMessageTime").gte(date))).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
        } else {
            query = new Query().addCriteria(Criteria.where("lastMessageTime").gte(date)).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            ;
        }

        PaginationVo<ConsultSessionStatusVo> pagination = consultRecordService.getUserMessageList(pageNo, pageSize, query);
        List<ConsultSessionStatusVo> resultList = new ArrayList<ConsultSessionStatusVo>();
        if (pagination.getDatas() != null && pagination.getDatas().size() > 0) {
            for (ConsultSessionStatusVo consultSessionStatusVo : pagination.getDatas()) {
                ConsultSessionStatusVo vo = new ConsultSessionStatusVo();
                vo.setUserName(consultSessionStatusVo.getUserName());
                vo.setUserId(consultSessionStatusVo.getUserId());
                vo.setCsUserId(consultSessionStatusVo.getCsUserId());
                vo.setCsUserName(consultSessionStatusVo.getCsUserName());
                vo.setLastMessageTime(consultSessionStatusVo.getLastMessageTime());
                //根据userId查询CsUserId
                ConsultSession consultSession = new ConsultSession();
                consultSession.setId(Integer.valueOf(consultSessionStatusVo.getSessionId()));
                List<ConsultSession> sessionList = consultSessionService.getCsUserByUserId(consultSession);
                if (sessionList != null && sessionList.size() > 0) {
                    String csUserName = "";
                    for (ConsultSession session : sessionList) {
                        if (session != null) {
                            csUserName = csUserName + " " + session.getNickName();
                        }
                    }
                    vo.setCsUserName(csUserName);
                }
                resultList.add(vo);
            }
        }
        response.put("totalPage", pagination.getTotalPage());
        response.put("currentPage", pagination.getPageNo());
        response.put("userList", resultList);
        return response;
    }

    /**
     * 数据去重，留作后用
     *
     * @param c
     * @param o
     * @return
     * @author deliang
     */
    public int frequency(List<ConsultSessionStatusVo> c, ConsultSessionStatusVo o) {
        int result = 0;
        if (o == null) {
            for (ConsultSessionStatusVo e : c)
                if (e == null)
                    result++;
        } else {
            for (ConsultSessionStatusVo e : c)
                if (o.getUserId().equals(e.getUserId()))
                    result++;
        }
        return result;
    }

    /**
     * 根据csUserId获取Session,最近聊天记录信息，咨询界面左侧已接入会话 asd
     */
    @RequestMapping(value = "/getCurrentUserList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getCurrentUserList(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        PaginationVo<ConsultRecordMongoVo> pagination = null;
        String csUserId = String.valueOf(params.get("csUserId"));
        String csuserType = (String) params.get("userType");
        ConcurrentHashMap<String, Object> needPayList = new ConcurrentHashMap<String, Object>();
        try {
            needPayList = consultPayUserService.getneepPayConsultSession((String) params.get("csUserId"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (StringUtils.isNotNull(csUserId)) {
            int pageNo = (Integer) params.get("pageNo");
            int pageSize = (Integer) params.get("pageSize");
            List<HashMap<String, Object>> responseList = new ArrayList<HashMap<String, Object>>();

            ConsultSession consultSessionSearch = new ConsultSession();
            consultSessionSearch.setCsUserId(csUserId);
            consultSessionSearch.setStatus(ConsultSession.STATUS_ONGOING);
            List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSessionSearch);

            if (consultSessions != null && consultSessions.size() > 0) {
                for (ConsultSession consultSession : consultSessions) {
                    HashMap<String, Object> searchMap = new HashMap<String, Object>();
                    RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(consultSession.getId());
                    if (richConsultSession != null && StringUtils.isNotNull(richConsultSession.getUserId())) {
                        String userId = richConsultSession.getUserId();
                        Query query = new Query(where("userId").is(userId)).with(new Sort(Direction.ASC, "createDate"));
                        pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "temporary");
                        searchMap.put("patientId", userId);
                        searchMap.put("source", richConsultSession.getSource());
                        searchMap.put("patientName", richConsultSession.getUserName());
                        searchMap.put("serverAddress", richConsultSession.getServerAddress());
                        searchMap.put("sessionId", richConsultSession.getId());
                        searchMap.put("isOnline", true);
                        searchMap.put("messageNotSee", true);
                        searchMap.put("dateTime", richConsultSession.getCreateTime());
                        searchMap.put("consultValue", ConsultUtil.transformCurrentUserListData(pagination.getDatas()));
                        if (null != needPayList && consultPayUserService.angelChargeCheck(userId)) {
                            if ("distributor".equals(csuserType)) {
                                Date createTime = (Date) needPayList.get(userId);
                                if (null != createTime && createTime.getTime() + 1000 * 60 * 5 > new Date().getTime()) {
                                    searchMap.put("notifyType", "1002");
                                } else {
                                    searchMap.put("notifyType", "1003");
                                }
                            }
                        }
                        responseList.add(searchMap);
                    }
                }
                response.put("alreadyJoinPatientConversation", responseList);
            } else {
                response.put("alreadyJoinPatientConversation", "");
            }
        } else {
            response.put("alreadyJoinPatientConversation", "");
        }
        return response;
    }

    /***
     * 聊天记录查询接口（UserInfo 根据客户查找  message 根据聊天记录查找  分页  123
     *
     * @param
     * @return {
     * "pageNo":"2",
     * "pageSize":"20",
     * "records": [
     * {
     * "id":123
     * "session_id": 456,
     * "openid": "3Wisdfsdflaksjfsd234234j",
     * "message":"聊天内容"
     * "message_type": "yuyin",
     * "toUserId": "fdasfa",
     * "fromUserId": "liutao"
     * },
     * {
     * "id":456
     * "session_id": 345534,
     * "openid": "3Wisdfsdsdfsfjfsd234234j",
     * "message":"聊天内容"
     * "message_type": "yuyin",
     * "toUserId": "fdasfa",
     * "fromUserId": "liutao"
     * }
     * ]
     * }
     */
    @RequestMapping(value = "/recordSearchList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordSearchList(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        int pageNo = 0;
        int pageSize = 1;
        Map<String, Object> response = new HashMap<String, Object>();
        if (null != params.get("pageNo") && null != params.get("pageSize")) {
            pageNo = (Integer) params.get("pageNo");
            pageSize = (Integer) params.get("pageSize");
        }

        PaginationVo<ConsultRecordMongoVo> pagination = null;
        PaginationVo<ConsultSessionStatusVo> consultSessionStatusVoPaginationVo = null;
        String searchType = String.valueOf(params.get("searchType"));  //user 根据客户查找  message 根据聊天记录查找
        String searchInfo = String.valueOf(params.get("searchInfo"));
        List<ConsultSessionStatusVo> resultList = new ArrayList<ConsultSessionStatusVo>();
        if (searchType.equals("user")) {
            Criteria cr = new Criteria();
            Query query = new Query();
            query.addCriteria(cr.orOperator(
                    Criteria.where("userName").regex(searchInfo), Criteria.where("userId").regex(searchInfo))).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            consultSessionStatusVoPaginationVo = consultRecordService.getUserMessageList(pageNo, pageSize, query);

            if (consultSessionStatusVoPaginationVo.getDatas() != null && consultSessionStatusVoPaginationVo.getDatas().size() > 0) {
                for (ConsultSessionStatusVo consultSessionStatusVo : consultSessionStatusVoPaginationVo.getDatas()) {
                    ConsultSessionStatusVo vo = consultSessionStatusVo;
                    //根据userId查询CsUserId
                    ConsultSession consultSession = new ConsultSession();
                    consultSession.setId(Integer.valueOf(consultSessionStatusVo.getSessionId()));
                    List<ConsultSession> sessionList = consultSessionService.getCsUserByUserId(consultSession);
                    if (sessionList != null && sessionList.size() > 0) {
                        String csUserName = "";
                        for (ConsultSession session : sessionList) {
                            if (session != null) {
                                csUserName = csUserName + " " + session.getNickName();
                            }
                        }
                        vo.setCsUserName(csUserName);
                    }
                    resultList.add(vo);
                }
            }
            response.put("totalPage", consultSessionStatusVoPaginationVo.getTotalPage());
            response.put("pageNo", pageNo);
            response.put("pageSize", pageSize);
            response.put("userList", resultList);
        } else if (searchType.equals("message")) {
            Query query = new Query(where("message").regex(searchInfo)).with(new Sort(Sort.Direction.DESC, "createDate"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
            response.put("userList", pagination != null ? pagination.getDatas() : "");
            List<HashMap<String, Object>> responseList = new ArrayList<HashMap<String, Object>>();
            if (pagination.getDatas() != null && pagination.getDatas().size() > 0) {
                for (ConsultRecordMongoVo consultRecordMongoVo : pagination.getDatas()) {
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    consultRecordMongoVo.setInfoDate(DateUtils.DateToStr(consultRecordMongoVo.getCreateDate(), "datetime"));
                    hashMap.put("currentRecord", consultRecordMongoVo);

                    Query beforeQuery = new Query(where("userId").is(consultRecordMongoVo.getUserId()).andOperator(Criteria.where("createDate").lt(consultRecordMongoVo.getCreateDate()))).with(new Sort(Direction.ASC, "createDate")).limit(5);
                    List<ConsultRecordMongoVo> beforeRecordList = consultRecordService.getCurrentUserHistoryRecord(beforeQuery);
                    modifyDate(beforeRecordList);
                    hashMap.put("beforeRecord", beforeRecordList);

                    Query laterQuery = new Query(where("userId").is(consultRecordMongoVo.getUserId()).andOperator(Criteria.where("createDate").gt(consultRecordMongoVo.getCreateDate()))).with(new Sort(Sort.Direction.ASC, "createDate")).limit(5);
                    List<ConsultRecordMongoVo> laterRecordList = consultRecordService.getCurrentUserHistoryRecord(laterQuery);
                    modifyDate(laterRecordList);
                    hashMap.put("laterRecord", laterRecordList);
                    responseList.add(hashMap);
                }
                response.put("totalPage", pagination.getTotalPage());
                response.put("pageNo", pageNo);
                response.put("pageSize", pageSize);
                response.put("userList", responseList);
            }
        }
        return response;
    }

    private void modifyDate(List<ConsultRecordMongoVo> laterRecordList) {
        for (ConsultRecordMongoVo recordMongoVo : laterRecordList) {
            recordMongoVo.setInfoDate(DateUtils.DateToStr(recordMongoVo.getCreateDate(), "datetime"));
        }
    }

    /**
     * 根据userId查询会话sessionId
     *
     * @param userId
     * @return response 返回前台的响应数据
     * @author guozengguang
     */
    @RequestMapping(value = "/getSessionId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getSessionId(@RequestParam(required = true) String userId) {

        Map<String, Object> response = new HashMap<String, Object>();
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
        if (sessionId != null) {
            response.put("status", 0);
            response.put("sessionId", String.valueOf(sessionId));
        } else {
            response.put("status", 1);
            response.put("sessionId", "");
        }

        return response;
    }

    /**
     * 根据userId查询会话sessionId
     *
     * @return response 返回前台的响应数据
     * @author guozengguang
     */
    @RequestMapping(value = "/getUserCurrentConsultContent", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserCurrentConsultContent(@RequestBody Map<String, Object> params) {

        Map<String, Object> response = new HashMap<String, Object>();
        String sessionId = (String) params.get("sessionId");
        String userId = (String) params.get("userId");
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("sessionId").is(sessionId)).
                with(new Sort(Sort.Direction.DESC, "createDate")).limit(1000);
        List<ConsultRecordMongoVo> currentUserHistoryRecord = consultRecordService.getCurrentUserHistoryRecord(query);
        if (currentUserHistoryRecord != null) {
            response.put("consultDataList", ConsultUtil.transformCurrentUserListData(currentUserHistoryRecord));
        } else {
            response.put("consultDataList", "");
        }
        return response;
    }

    /**
     * 根据userId查询会话sessionId
     *
     * @return response 返回前台的响应数据
     * @author guozengguang
     */
    @RequestMapping(value = "/createOrUpdateWJYPatientInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> createOrUpdateWJYPatientInfo(@RequestBody Map<String, Object> params) {

        Map<String, Object> response = new HashMap<String, Object>();
        HashMap<String, Object> request = new HashMap<String, Object>();
        String userPhone = (String) params.get("patientPhone");
        String userName = (String) params.get("patientName");
        Integer userSex = (Integer) params.get("patientSex");
        String source = "";
        String thirdId = "";
        if (params.containsKey("source")) {
            if (StringUtils.isNotNull((String) params.get("source"))) {
                source = (String) params.get("source");
                if (source.contains("wjy")) {
                    thirdId = (String) params.get("thirdId");
                    source = "WJY";
                }
            }
        }
        request.put("userPhone", userPhone);
        request.put("userName", userName);
        request.put("userSex", userSex);
        request.put("source", source);
        request.put("thirdId", thirdId);
        request.put("token", params.get("token"));
        String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
        request.put("sys_user_id", sys_user_id);
        request.put("remoteUrl", "http://rest.ihiss.com:9000/user/children");
        Map result = userInfoService.createOrUpdateThirdPartPatientInfo(request);
        if (result != null && result.size() > 0) {
            if (String.valueOf(result.get("result")).equals("1")) {
                /*Runnable thread = new saveCoopThirdBabyInfoThread(request);
                threadExecutor.execute(thread);*/
                String childrenUrl = (String) request.get("remoteUrl"); //获取当前登录人的孩子信息
                String token = (String) request.get("token");
                String access_token = "{'X-Access-Token':'" + token + "'}";
                String method = "GET";
                String dataType = "json";
                String babyInfo = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token, "", 2);
                JSONObject jsonObject = null;
                if (StringUtils.isNotNull(babyInfo)) {
                    JSONArray jsonArray = new JSONArray(babyInfo);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = JSONObject.fromObject(jsonArray.get(i).toString());
                        CoopThirdBabyInfoVo coopThirdBabyInfoVo = new CoopThirdBabyInfoVo();
                        coopThirdBabyInfoVo.setCreateDate(new Date());
                        coopThirdBabyInfoVo.setDelFlag("0");
                        coopThirdBabyInfoVo.setSource((String) request.get("source"));
                        coopThirdBabyInfoVo.setSysUserId((String) request.get("sys_user_id"));
                        try {
                            coopThirdBabyInfoVo.setBirthday(new SimpleDateFormat("yyyy-mm-DD").parse((String) jsonObject.get("birthday")));
                            coopThirdBabyInfoVo.setGender((String) jsonObject.get("sex"));
                            coopThirdBabyInfoVo.setName((String) jsonObject.get("name"));
                            coopThirdBabyInfoVo.setStatus((String) jsonObject.get("id"));
                            coopThirdBabyInfoService.addCoopThirdBabyInfo(coopThirdBabyInfoVo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            response.put("patientId", result.get("sys_user_id"));
        }
        return response;
    }


   /* public class saveCoopThirdBabyInfoThread implements Runnable {
        private HashMap<String, Object> params;

        public saveCoopThirdBabyInfoThread(HashMap<String, Object> params) {
            this.params = params;
        }

        @Override
        public void run() {
            String childrenUrl = (String) params.get("remoteUrl"); //获取当前登录人的孩子信息
            String token = (String) params.get("token");
            String access_token = "{'X-Access-Token':'" + token + "'}";
            String method = "GET";
            String dataType = "json";
            String babyInfo = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token, "", 2);
            net.sf.json.JSONObject jsonObject = null;
            if (StringUtils.isNotNull(babyInfo)) {
                JSONArray jsonArray = new JSONArray(babyInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = net.sf.json.JSONObject.fromObject(jsonArray.get(i).toString());
                    CoopThirdBabyInfoVo coopThirdBabyInfoVo = new CoopThirdBabyInfoVo();
                    coopThirdBabyInfoVo.setCreateDate(new Date());
                    coopThirdBabyInfoVo.setDelFlag("0");
                    coopThirdBabyInfoVo.setSource((String) params.get("source"));
                    coopThirdBabyInfoVo.setSysUserId((String) params.get("sys_user_id"));
                    try {
                        coopThirdBabyInfoVo.setBirthday(new SimpleDateFormat("yyyy-mm-DD").parse((String) jsonObject.get("birthday")));
                        coopThirdBabyInfoVo.setGender((String) jsonObject.get("sex"));
                        coopThirdBabyInfoVo.setName((String) jsonObject.get("name"));
                        coopThirdBabyInfoVo.setStatus((String) jsonObject.get("id"));
                        coopThirdBabyInfoService.addCoopThirdBabyInfo(coopThirdBabyInfoVo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/
}
