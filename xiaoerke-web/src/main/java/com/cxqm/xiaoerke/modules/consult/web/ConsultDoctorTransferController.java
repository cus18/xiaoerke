
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
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class ConsultDoctorTransferController extends BaseController {

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private SystemService systemService;


    /***
     * 获取在线医生列表（分页）
     *
     * @param
     @return
     {
         "pageNo":"2",
         "pageSize":"20",
         "doctors": [
             {
                 "id": "fdasfa",
                 "name": "liutao",
                 "worker":"123baodaifu"
                 "status":"onLine"
             },
             {
                 "id": "fdasfa",
                 "name": "liutao",
                 "worker":"234baodaifu"
                 "status":"offLine"
             }
         ]
     }
     */
    @RequestMapping(value = "/doctorOnLineList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody HashMap<String, Object> doctorOnLineList(@RequestBody Map<String, Object> params) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        List<String> userList = ConsultSessionManager.getSessionManager().getOnlineCsList();
        if(userList!=null && userList.size()>0){
            response.put("onLineCsUserList",consultSessionService.getOnlineCsListInfo(userList));
        }
        return response;
    }

    /***
     * 转发会话到其他客服
     *
     * @param
       {
            "doctorId": "dafdsaf",
            "sessionId":53534534,
            "patientName":"liutao",
            "transferer":"frank",
            "serverAddress":""
        }
         @return
         {
             "result":"success",
             "sessionId":"fwefewf"
         }
     */
    @RequestMapping(value = "/transfer", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> transfer(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<String, Object>();
        Integer sessionId= (Integer)params.get("sessionId");
        String doctorId= String.valueOf(params.get("doctorId"));//转接的话，必须要用医生ID
        String remark = (String)params.get("remark");
        int status = 0;
        if(null != sessionId && StringUtils.isNotNull(doctorId)){
            status =consultSessionForwardRecordsService.transferSession(sessionId,doctorId,remark);
        }
        if(status==0){
            response.put("result", "failure");
        }else if(status==1){
            response.put("result", "success");
        }else if(status==2){
            response.put("result", "transferring");
        }
        response.put("sessionId",sessionId);
        return  response;
    }

    /***
     * 取消转接
     *
     * @param
        {
        "sessionId":53534534,
        "toCsUserId":"123124"
        "remark":"请注意"
        }
         @return
         {
         "result":"success",
         }
     */
    @RequestMapping(value = "/cancelTransfer", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> cancelTransferringSession(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<String, Object>();
        Integer sessionId= (Integer)params.get("sessionId");
        String transferer= String.valueOf(params.get("toCsUserId"));
        String remark = String.valueOf(params.get("remark"));
        if(null != sessionId && StringUtils.isNotNull(transferer)){
            consultSessionForwardRecordsService.cancelTransferringSession(sessionId, transferer, remark);
            response.put("result", "success");
        }else{
            response.put("result", "failure");
        }
        return  response;
    }

    /***
     * 接收或者拒绝转接
     * @param {"sessionId":123,"forwardRecordId":4234,"toCsUserId":"123","toCsUserName":"小明"，“operation”:""}
     @return
     {
      "result":"success",
     }
     */
    @RequestMapping(value = "/react2Transfer", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String, Object> react2Transfer(@RequestBody Map<String, Object> params) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        String forwardSessionIds = (String) params.get("forwardSessionIds");
        String operation = (String) params.get("operation");
        String[] forwardSessionIdArray = forwardSessionIds.split(";");
        for(int i=0;i<forwardSessionIdArray.length;i++){
            HashMap<String,Object> param = new HashMap<String, Object>();
            ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = consultSessionForwardRecordsService.selectByPrimaryKey(Long.parseLong(forwardSessionIdArray[i]));
            if(consultSessionForwardRecordsVo!=null){
                if(consultSessionForwardRecordsVo.getStatus().equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING)){
                    param.put("sessionId",consultSessionForwardRecordsVo.getConversationId());
                    param.put("forwardRecordId",consultSessionForwardRecordsVo.getId());
                    param.put("toCsUserId",consultSessionForwardRecordsVo.getToUserId());
                    User user = systemService.getUser(consultSessionForwardRecordsVo.getToUserId());
                    param.put("toCsUserName",user.getName());
                    param.put("operation", operation);
                    consultSessionForwardRecordsService.react2Transfer(param);
                }
            }
        }
        response.put("result","success");
        return response;
    }

    /**
     * 获取待接入用户列表
     */
    @RequestMapping(value = "/waitJoinList", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String,Object> waitJoinList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        List<HashMap<String,Object>> dataList = new ArrayList<HashMap<String, Object>>();
        String csUserId = (String) params.get("csUserId");
        List<ConsultSessionForwardRecordsVo> waitJoinListVoList = consultSessionForwardRecordsService.getWaitJoinList(csUserId);
        for(ConsultSessionForwardRecordsVo waitJoinListVo:waitJoinListVoList){
            HashMap<String,Object> dataValue = new HashMap<String, Object>();
            dataValue.put("sessionId", waitJoinListVo.getConversationId());
            dataValue.put("forwardSessionId", waitJoinListVo.getId());
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(Integer.parseInt(String.valueOf(waitJoinListVo.getConversationId())));
            if(richConsultSession!=null){
                Query query = new Query().addCriteria(Criteria.where("userId").is(richConsultSession.getUserId()).and("createDate").lt(new Date())).
                        with(new Sort(Sort.Direction.DESC, "createDate")).limit(100);
                List<ConsultRecordMongoVo> consultRecordMongoVo = consultRecordService.getCurrentUserHistoryRecord(query);
                if(consultRecordMongoVo.size()!=0){
                    dataValue.put("messageContent", consultRecordMongoVo.get(0).getMessage());
                    dataValue.put("type", consultRecordMongoVo.get(0).getType());
                    dataValue.put("messageNum", consultRecordMongoVo.size());
                    dataValue.put("messageDateTime", DateUtils.DateToStr(consultRecordMongoVo.get(0).getCreateDate()));
                }
                dataValue.put("userId",richConsultSession.getUserId());
                dataValue.put("userName",richConsultSession.getUserName());
                dataValue.put("source",richConsultSession.getSource());
                dataValue.put("serverAddress",richConsultSession.getServerAddress());
                dataValue.put("sessionCreateTime",DateUtils.DateToStr(richConsultSession.getCreateTime()));
                dataValue.put("dateTime", DateUtils.DateToStr(new Date()));
                User user = systemService.getUser(waitJoinListVo.getFromUserId());
                dataValue.put("fromCsUserName",user.getName());
                dataValue.put("fromCsUserId",waitJoinListVo.getFromUserId());
                dataValue.put("chooseFlag",true);
                dataList.add(dataValue);
            }
        }
        response.put("waitJoinList",dataList);
        response.put("waitJoinNum",dataList.size());
        return response;
    }

}
