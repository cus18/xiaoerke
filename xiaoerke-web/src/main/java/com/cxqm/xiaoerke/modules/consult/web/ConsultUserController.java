/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;
import java.util.*;

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
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @RequestMapping(value = "/getCurrentSessions", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCurrentSessions(@RequestParam(required=true) String csUserId) {
        ConsultSession consultSession = new ConsultSession();
        consultSession.setCsUserId(csUserId);
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        List<Object> sessionIds = new ArrayList<Object>();
        for(ConsultSession session : consultSessions) {
            sessionIds.add(session.getId());
        }

        List<Object> sessions = ConsultSessionManager.getSessionManager().getCurrentSessions(sessionIds);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", 0);
        response.put("msg", "OK");
        response.put("sessions", sessions);
        return response;
    }

    @RequestMapping(value = "/getCurrentUserByCSId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCurrentUserByCSId(@RequestParam(required=true) String csUserId) {

        Map<String, Object> response = new HashMap<String, Object>();
        ConsultSession consultSession = new ConsultSession();
        if(StringUtils.isNull(csUserId)){
            csUserId = UserUtils.getUser().getId();
        }
        consultSession.setCsUserId(csUserId);
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);

        List<ConsultSession> consultSessions = null;//consultSessionService.getAlreadyAccessUsers(consultSession);
        if(consultSessions!=null && consultSessions.size()>0){
            response.put("alreadyAccessUsers",consultSessions);
        }
        response.put("status", 0);
        response.put("msg", "OK");

        return response;
    }

    /**
     * 聊天咨询文件上传
     * @param file
     * @param data
     * @return {"status","success"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/uploadMediaFile",method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap<String,Object> UploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("data") String data) throws UnsupportedEncodingException {
        HashMap<String, Object> response = consultRecordService.uploadMediaFile(file, data);
        return response;
    }

    /**
     * 获取客户列表(或咨询过某个医生的客户)详细信息,按照时间降序排序
     */
    @RequestMapping(value = "/getUserList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> getUserList(@RequestBody Map<String, Object> params) {
        Map<String,Object> response = new HashMap<String, Object>();
        Integer pageNo = (Integer) params.get("pageNo");
        Integer pageSize = (Integer) params.get("pageSize");
        Integer dateNum = (Integer) params.get("dateNum");
        String csUserId = String.valueOf(params.get("CSDoctorId"));
        Date date = null;
        if(dateNum != null){
            String dateTemp;
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE, -dateNum);// 30为增加的天数，可以改变的
            dateTemp = DateUtils.DateToStr(ca.getTime(), "datetime");
            date = DateUtils.StrToDate(dateTemp,"datetime");
        }

        Query query;
        if(dateNum == null){
            if(csUserId == "null"){
                query = new Query().with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            }else {
                query = new Query().addCriteria(new Criteria().where("csUserId").is(csUserId)).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            }
        }else if(csUserId != "null"){
            query = new Query().addCriteria(new Criteria().where("csUserId").is(csUserId).andOperator(Criteria.where("lastMessageTime").gte(date))).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
        } else {
            query = new Query().addCriteria(Criteria.where("lastMessageTime").gte(date));
        }




        PaginationVo<ConsultSessionStatusVo> pagination = consultRecordService.getUserMessageList(pageNo, pageSize, query);
        List<ConsultSessionStatusVo> resultList = new ArrayList<ConsultSessionStatusVo>();
        if(pagination.getDatas()!=null && pagination.getDatas().size()>0){
            for(ConsultSessionStatusVo consultSessionStatusVo :pagination.getDatas()){
                ConsultSessionStatusVo vo = new ConsultSessionStatusVo();
                vo.setUserName(consultSessionStatusVo.getUserName());
                vo.setUserId(consultSessionStatusVo.getUserId());
                vo.setCsUserId(consultSessionStatusVo.getCsUserId());
                vo.setCsUserName(consultSessionStatusVo.getCsUserName());
                vo.setLastMessageTime(consultSessionStatusVo.getLastMessageTime());
                //根据userId查询CsUserId
                ConsultSession consultSession =new ConsultSession();
                consultSession.setUserId(consultSessionStatusVo.getUserId());
                List<ConsultSession> sessionList = consultSessionService.getCsUserByUserId(consultSession);
                if(sessionList!=null && sessionList.size() > 0){
                    String csUserName = "";
                    for(ConsultSession session :sessionList){
                        if(session!=null){
                            csUserName = csUserName + " " +session.getNickName();
                        }
                    }
                    vo.setCsUserName(csUserName);
                }
                resultList.add(vo);
            }
        }
        removeDuplicateList(resultList);
        response.put("userList",resultList);
        return response;
    }

    private void removeDuplicateList(List<ConsultSessionStatusVo> resultList) {
        int size = resultList.size();
        for(int i = 0;i<size;i++){
            if(frequency(resultList, resultList.get(i)) > 1){
                resultList.remove(i);
                size = resultList.size();
            }
        }
    }

    /**
     * @author deliang
     * @param c
     * @param o
     * @return
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
     * 获取客户列表(或咨询过某个医生的客户)详细信息,按照时间降序排序
     */
    @RequestMapping(value = "/getUserList_back", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserList_back(@RequestBody Map<String, Object> params) {
        Map<String,Object> response = new HashMap<String, Object>();

        //分页获取最近会话记录
        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        Integer dateNum = (Integer)params.get("dateNum");
        String CSDoctorId = (String)params.get("CSDoctorId");

        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        if(dateNum != null){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-dateNum);
            Long timeMillis = calendar.getTimeInMillis();
            String searchDate = DateUtils.formatDateTime(timeMillis);
            searchMap.put("searchDate",searchDate);
        }
        searchMap.put("CSDoctorId",CSDoctorId);
        Page<ConsultSessionForwardRecordsVo> userPage  = FrontUtils.generatorPage(pageNo, pageSize);
        Page<ConsultSessionForwardRecordsVo> resultPage = consultSessionForwardRecordsService.getConsultUserListRecently(userPage,searchMap);

        //根据会话记录查询客户列表的详细信息
        List<ConsultRecordMongoVo> consultSessionForwardRecordsVos = new ArrayList<ConsultRecordMongoVo>();
        for(ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo : resultPage.getList()){
            Query query = new Query(where("fromUserId").is(consultSessionForwardRecordsVo.getFromUserId())).with(new Sort(Sort.Direction.DESC, "createDate"));
            ConsultRecordMongoVo oneConsultRecord = consultRecordService.findOneConsultRecord(query);
//            Date date = oneConsultRecord.getCreateDate();
//            oneConsultRecord.setInfoDate(DateUtils.DateToStr(date));

            if (oneConsultRecord != null && StringUtils.isNotNull(oneConsultRecord.getSenderId())){
                consultSessionForwardRecordsVos.add(oneConsultRecord);
            }

        }

        response.put("userList",consultSessionForwardRecordsVos);
        response.put("status", "success");
        return response;
    }

    /**
     * 根据csUserId获取Session,最近聊天记录信息，咨询界面左侧已接入会话 asd
     */
    @RequestMapping(value = "/getCurrentUserList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getCurrentUserList(@RequestBody Map<String, Object> params) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        PaginationVo<ConsultRecordMongoVo> pagination = null;
        String csUserId = String.valueOf(params.get("csUserId"));

        if(StringUtils.isNotNull(csUserId)){
            int pageNo = (Integer) params.get("pageNo");
            int pageSize = (Integer) params.get("pageSize");
            List<HashMap<String,Object>> responseList = new ArrayList<HashMap<String, Object>>();

            ConsultSession consultSessionSearch = new ConsultSession();
            consultSessionSearch.setCsUserId(csUserId);
            consultSessionSearch.setStatus(ConsultSession.STATUS_ONGOING);
            List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSessionSearch);

            if(consultSessions!=null && consultSessions.size()>0){
                for(ConsultSession consultSession :consultSessions){
                    HashMap<String,Object> searchMap = new HashMap<String, Object>();
                    RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(consultSession.getId());
                    String userId = richConsultSession.getUserId();
                    Query query = new Query(where("userId").is(userId).and("csUserId")
                            .is(consultSession.getCsUserId())).with(new Sort(Direction.ASC, "createDate"));
                    pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "temporary");
                    searchMap.put("patientId",userId);
                    searchMap.put("source",richConsultSession.getSource());
                    searchMap.put("patientName", richConsultSession.getUserName());
                    searchMap.put("fromServer",richConsultSession.getServerAddress());
                    searchMap.put("sessionId",richConsultSession.getId());
                    searchMap.put("isOnline",true);
                    searchMap.put("messageNotSee",true);
                    searchMap.put("dateTime",richConsultSession.getCreateTime());
                    searchMap.put("consultValue",ConsultUtil.transformCurrentUserListData(pagination.getDatas()));
                    responseList.add(searchMap);
                }
                response.put("alreadyJoinPatientConversation",responseList);
            }else{
                response.put("alreadyJoinPatientConversation", "");
            }
        }else {
            response.put("alreadyJoinPatientConversation","");
        }
        return response;
    }

    /***
     * 聊天记录查询接口（UserInfo 根据客户查找  message 根据聊天记录查找  分页  123
     *
     * @param
     @return
     {
         "pageNo":"2",
         "pageSize":"20",
         "records": [
             {
             "id":123
             "session_id": 456,
             "openid": "3Wisdfsdflaksjfsd234234j",
             "message":"聊天内容"
             "message_type": "yuyin",
             "toUserId": "fdasfa",
             "fromUserId": "liutao"
             },
             {
             "id":456
             "session_id": 345534,
             "openid": "3Wisdfsdsdfsfjfsd234234j",
             "message":"聊天内容"
             "message_type": "yuyin",
             "toUserId": "fdasfa",
             "fromUserId": "liutao"
             }
         ]
     }
     */
    @RequestMapping(value = "/recordSearchList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordSearchList(@RequestBody Map<String, Object> params) {

        int pageNo = 0;
        int pageSize = 1;
        Map<String,Object> response = new HashMap<String, Object>();
        if(null != params.get("pageNo") && null != params.get("pageSize")){
            pageNo = Integer.parseInt((String)params.get("pageNo"));
            pageSize = Integer.parseInt((String)params.get("pageSize"));
        }

        PaginationVo<ConsultRecordMongoVo> pagination = null;
        PaginationVo<ConsultSessionStatusVo> consultSessionStatusVoPaginationVo = null;
        String searchType = String.valueOf(params.get("searchType"));  //user 根据客户查找  message 根据聊天记录查找
        String searchInfo = String.valueOf(params.get("searchInfo"));
        List<ConsultSessionStatusVo> resultList = new ArrayList<ConsultSessionStatusVo>();
        if(searchType.equals("user")){
            Criteria cr = new Criteria();
            Query query = new Query();
            query.addCriteria(cr.orOperator(
                    Criteria.where("userName").regex(searchInfo)
            )).with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
            consultSessionStatusVoPaginationVo = consultRecordService.getUserMessageList(pageNo, pageSize, query);

            if(consultSessionStatusVoPaginationVo.getDatas()!=null && consultSessionStatusVoPaginationVo.getDatas().size()>0){
                for(ConsultSessionStatusVo consultSessionStatusVo :consultSessionStatusVoPaginationVo.getDatas()){
                    ConsultSessionStatusVo vo = consultSessionStatusVo;
                    //根据userId查询CsUserId
                    ConsultSession consultSession =new ConsultSession();
                    consultSession.setUserId(consultSessionStatusVo.getUserId());
                    List<ConsultSession> sessionList = consultSessionService.getCsUserByUserId(consultSession);
                    if(sessionList!=null && sessionList.size() > 0){
                        String csUserName = "";
                        for(ConsultSession session :sessionList){
                            if(session!=null){
                                csUserName = csUserName + " " +session.getNickName();
                            }
                        }
                        vo.setCsUserName(csUserName);
                    }
                    resultList.add(vo);
                }
            }
            removeDuplicateList(resultList);
            response.put("userList",resultList);
        }else if(searchType.equals("message")){
            Query query = new Query(where("message").regex(searchInfo)).with(new Sort(Sort.Direction.DESC, "createDate"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query,"permanent");
            response.put("userList", pagination!=null?pagination.getDatas():"");
            List<HashMap<String,Object>> responseList = new ArrayList<HashMap<String, Object>>();
            if(pagination.getDatas()!=null && pagination.getDatas().size()>0){
                for(ConsultRecordMongoVo consultRecordMongoVo : pagination.getDatas()){
                    HashMap<String,Object> hashMap = new HashMap<String, Object>();
                    consultRecordMongoVo.setInfoDate(DateUtils.DateToStr(consultRecordMongoVo.getCreateDate(),"datetime"));
                    hashMap.put("currentRecord",consultRecordMongoVo);

                    Query beforeQuery = new Query(where("userId").is(consultRecordMongoVo.getUserId()).andOperator(Criteria.where("createDate").lt(consultRecordMongoVo.getCreateDate()))).with(new Sort(Direction.ASC, "createDate")).limit(5);
                    List<ConsultRecordMongoVo> beforeRecordList = consultRecordService.getCurrentUserHistoryRecord(beforeQuery);
                    modifyDate(beforeRecordList);
                    hashMap.put("beforeRecord",beforeRecordList);

                    Query laterQuery = new Query(where("userId").is(consultRecordMongoVo.getUserId()).andOperator(Criteria.where("createDate").gt(consultRecordMongoVo.getCreateDate()))).with(new Sort(Sort.Direction.ASC, "createDate")).limit(5);
                    List<ConsultRecordMongoVo> laterRecordList = consultRecordService.getCurrentUserHistoryRecord(laterQuery);
                    modifyDate(laterRecordList);
                    hashMap.put("laterRecord",laterRecordList);
                    responseList.add(hashMap);
                }
                response.put("userList",responseList);
            }
        }
        response.put("pageNo",pageNo);
        response.put("pageSize",pageSize);
        return response;
    }

    private void modifyDate(List<ConsultRecordMongoVo> laterRecordList) {
        for(ConsultRecordMongoVo recordMongoVo : laterRecordList){
            recordMongoVo.setInfoDate(DateUtils.DateToStr(recordMongoVo.getCreateDate(), "datetime"));
        }
    }

    /**
     * 根据userId查询会话sessionId
     * @author guozengguang
     * @param userId
     * @return response 返回前台的响应数据
     */
    @RequestMapping(value = "/getSessionId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getSessionId(@RequestParam(required=true) String userId) {

        Map<String, Object> response = new HashMap<String, Object>();
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
        if(sessionId != null){
            response.put("status", 0);
            response.put("sessionId", sessionId);
        }else{
            response.put("status", 1);
            response.put("sessionId", "");
        }

        return response;
    }

}
