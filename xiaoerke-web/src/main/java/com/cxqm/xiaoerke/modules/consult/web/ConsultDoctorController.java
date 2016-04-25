
package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultSessionServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.consult.utils.DateUtil;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @RequestMapping(value = "/getCurrentUserHistoryRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody HashMap<String, Object> getCurrentUserHistoryRecord(@RequestBody Map<String, Object> params) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        String userId = (String) params.get("userId");
        String dateTime = (String) params.get("dateTime");
        Integer pageSize = (Integer) params.get("pageSize");
        List<ConsultRecordMongoVo> currentUserHistoryRecord = null;
        Date date = DateUtils.StrToDate(dateTime,"datetime");
        currentUserHistoryRecord = consultRecordService.getCurrentUserHistoryRecord(userId, date, pageSize);
        if(currentUserHistoryRecord!=null){
            response.put("consultDataList", ConsultUtil.transformCurrentUserListData(currentUserHistoryRecord));
        }else{
            response.put("consultDataList", "");
        }
        return response;
    }

    /***
     * 获取咨询今日会话的排名列表
     *
     * @param  {"rankDate":"2016-03-14"}
     @return
     {
     "doctors": [
     {
     "csUserId": "fdasfa",
     "doctorName": "liutao",
     "consultNum": 20,
     },
     {
     "csUserId": "fdasfa",
     "doctorName": "liutao",
     "consultNum": 26,
     },
     {
     "csUserId": "fdasfa",
     "doctorName": "liutao",
     "consultNum": 40,
     }
     ]
     }
     */
    @RequestMapping(value = "/rankList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> findConversationRankList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String,Object> searchMap = new HashMap<String, Object>();
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        try{
            searchMap.put("rankDate", params.get("rankDate"));
            List<Map<String, Object>> resultValue = consultSessionForwardRecordsService.findConversationRankList(searchMap);
            resultMap.put("rankListValue", resultValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /***
     * 获取聊天记录  分页
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
                     "userId": "3Wisdfsdflaksjfsd234234j",
                     "message":"聊天内容"
                     "message_type": "yuyin",
                     "toUserId": "fdasfa",
                     "fromUserId": "liutao"
                 },
                 {
                     "id":456
                     "session_id": 345534,
                     "userId": "3Wisdfsdsdfsfjfsd234234j",
                     "message":"聊天内容"
                     "message_type": "yuyin",
                     "toUserId": "fdasfa",
                     "fromUserId": "liutao"
                 }
             ]
         }
     */
    @RequestMapping(value = "/recordList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> recordList(@RequestBody Map<String, Object> params) {
        String recordType = String.valueOf(params.get("recordType"));
        String userId = String.valueOf(params.get("userId"));
        String fromUserId = String.valueOf(params.get("fromUserId"));
        int pageNo = 0;
        int pageSize = 1;
        PaginationVo<ConsultRecordMongoVo> pagination = null;
        Map<String,Object> response = new HashMap<String, Object>();
        if(null != params.get("pageNo") && null != params.get("pageSize")){
            pageNo = (Integer)params.get("pageNo");
            pageSize = (Integer)params.get("pageSize");
        }
        if(recordType.equals("all") && StringUtils.isNotNull(userId) && pageSize > 0){
            Query query = new Query(where("userId").is(userId)).with(new Sort(Direction.DESC, "createDate"));//用户端获取与平台的所有聊天记录
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
            for(ConsultRecordMongoVo consultRecordMongoVo :pagination.getDatas()){
                ConsultRecordMongoVo recordMongoVo = consultRecordMongoVo;
                recordMongoVo.setCreateDate(consultRecordMongoVo.getCreateDate());//暂时
            }
        }else if (recordType.equals("doctor") && StringUtils.isNotNull(userId) && StringUtils.isNotNull(fromUserId)){//医生端获取与自己有关的所有聊天记录
            Query query = new Query(where("toUserId").is(userId).and("fromUserId")
                    .is(fromUserId)).with(new Sort(Direction.DESC, "create_date"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        }else if (recordType.contains("image") || recordType.contains("voice")){//查询语音、图片
            String openId = String.valueOf(params.get("openId"));
            Query query = new Query(where("openId").is(openId).and("messageType")
                    .is(recordType)).with(new Sort(Direction.DESC, "create_date"));
            pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        }
        response.put("records", pagination!=null?pagination.getDatas():"");
        response.put("pageNo",pageNo);
        response.put("pageSize",pageSize);
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
        Map<String,Object> response = new HashMap<String, Object>();
        List<User> users ;
        User user = new User();
        user.setUserType("consultDoctor");
        String userId = String.valueOf(params.get("userId"));
        if(StringUtils.isNotNull(userId)){
            user.setId(userId);
        }
        users = systemService.findUserByUserType(user);
        if(users != null && users.size()>0){
            response.put("CSList",users);
            response.put("status","success");
        }
        return response;
    }

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
    Map<String, Object> sessionEnd(@RequestParam(required=true) String sessionId,
                                   @RequestParam(required=true) String userId) {
        String result = consultSessionService.clearSession(sessionId,userId);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", result);
        return response;
    }
}
