/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort.Direction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * 会员Controller
 *
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/user")
public class ConsultUserController extends BaseController {

    @Autowired
    private ConsultSessionService consultConversationService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultMongoUtilsService consultMongoUtilsService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    /***
     * 用户在咨询完毕后，主动请求终止会话（医生和患者都可操作）
     * @param
     * @return
     *     {
     *        "result":"success"(失败返回failure)
     *      }
     */

    @RequestMapping(value = "/sessionEnd", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getSessionId(@RequestParam(required=true) String sessionId,@RequestParam(required=true) String userId) {

        String result = consultConversationService.clearSession(Integer.parseInt(sessionId),userId);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", result);
        return response;
    }

    @RequestMapping(value = "/getCurrentSessions", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCurrentSessions(@RequestParam(required=true) String csUserId) {
        ConsultSession consultSession = new ConsultSession();
        consultSession.setCsUserId(csUserId);
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessions = consultConversationService.selectBySelective(consultSession);
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

        List<ConsultSession> consultSessions = null;//consultConversationService.getAlreadyAccessUsers(consultSession);
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
    @ResponseBody
    Map<String, Object> getUserList(@RequestBody Map<String, Object> params) {
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
            Query query = new Query(where("openid").is(consultSessionForwardRecordsVo.getOpenid())).with(new Sort(Sort.Direction.DESC, "createDate"));
            ConsultRecordMongoVo oneConsultRecord = consultRecordService.findOneConsultRecord(query);
            Date date = oneConsultRecord.getCreateDate();
            oneConsultRecord.setInfoDate(DateUtils.DateToStr(date));

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
        int pageNo = 0;
        int pageSize = 0;
        String csUserId = String.valueOf(params.get("csUserId"));

        if(StringUtils.isNotNull(csUserId)){
            pageNo = (Integer) params.get("pageNo");
            pageSize = (Integer) params.get("pageSize");
            List<HashMap<String,Object>> responseList = new ArrayList<HashMap<String, Object>>();
            List<RichConsultSession> richConsultSessions = consultMongoUtilsService.queryRichConsultSessionList(new Query().addCriteria(Criteria.where("csUserId").is(csUserId)));
            if(richConsultSessions!=null && richConsultSessions.size()>0){
                for(RichConsultSession richConsultSession :richConsultSessions){
                    HashMap<String,Object> searchMap = new HashMap<String, Object>();
                    String userId = richConsultSession.getUserId();
                    Query query = new Query(where("userId").is(userId).and("csUserId")
                            .is(richConsultSession.getCsUserId())).with(new Sort(Direction.DESC, "createDate"));
                    pagination = consultRecordService.getPage(pageNo, pageSize, query,"temporary");
                    if(StringUtils.isNull(richConsultSession.getUserId())){
                        searchMap.put("patientId",richConsultSession.getUserId());
                    }
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
                response.put("alreadyJoinPatientConversation","");
            }
        }else {
            response.put("alreadyJoinPatientConversation","");
        }
        return response;
    }

    /***
     * 聊天记录查询接口（UserInfo 根据客户查找  message 根据聊天记录查找  分页
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
        String searchType = String.valueOf(params.get("searchType"));  //user 根据客户查找  message 根据聊天记录查找
        String searchInfo = String.valueOf(params.get("searchInfo"));

        if(searchType.equals("user")){
            Criteria cr = new Criteria();
            Query query = new Query();
            query.addCriteria(cr.orOperator(
                    Criteria.where("attentionNickName").regex(searchInfo)
            )).with(new Sort(Sort.Direction.DESC, "create_date"));
            pagination = consultRecordService.getPage(pageNo, pageSize, query,"permanent");
        }else if(searchType.equals("message")){
            Query query = new Query(where("message").regex(searchInfo)).with(new Sort(Sort.Direction.DESC, "create_date"));
            pagination = consultRecordService.getPage(pageNo, pageSize, query,"permanent");
        }
        //根据咨询记录查询对应的用户
        HashSet<String> openidSet = new HashSet<String>();
        for(ConsultRecordMongoVo consultRecordMongoVo :pagination.getDatas()){
            if(consultRecordMongoVo.getConsultType().equals("wx")){
                openidSet.add(consultRecordMongoVo.getSenderId());
            }
        }

        List<ConsultRecordMongoVo> consultSessionForwardRecordsVos = new ArrayList<ConsultRecordMongoVo>();
        Iterator iterator = openidSet.iterator();
        while(iterator.hasNext()){
            Query query = new Query(where("openid").is(iterator.next())).with(new Sort(Sort.Direction.DESC, "createDate"));
            ConsultRecordMongoVo oneConsultRecord = consultRecordService.findOneConsultRecord(query);
            if(oneConsultRecord!=null && StringUtils.isNotNull(oneConsultRecord.getSenderId())){
                consultSessionForwardRecordsVos.add(oneConsultRecord);
            }
        }

        response.put("userList",consultSessionForwardRecordsVos);
        response.put("records", pagination!=null?pagination.getDatas():"");
        response.put("pageNo",pageNo);
        response.put("pageSize",pageSize);
        return response;
    }

}
