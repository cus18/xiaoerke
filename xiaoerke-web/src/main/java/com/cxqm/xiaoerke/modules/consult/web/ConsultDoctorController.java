
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.AnswerService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultSessionServiceImpl;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.apache.batik.css.engine.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * ConsultDoctor
 *
 * @author deliang
 * @version 2015-03-14
 */
@Controller
@RequestMapping(value = "consult")
public class ConsultDoctorController extends BaseController {

    @Autowired
    private ConsultSessionForwardRecordsService consultConversationForwardRecordsService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ConsultSessionServiceImpl consultSessionService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private SystemService systemService;


    /***
     * 获取咨询今日会话的排名列表
     *
     * @param  {"rankDate":"2016-03-14"}
     @return
     {
         "doctors": [
             {
                 "id": "fdasfa",
                 "name": "liutao",
                 "consultNum": 20,
             },
             {
                 "id": "fdasfa",
                 "name": "liutao",
                 "consultNum": 26,
             },
             {
                 "id": "fdasfa",
                 "name": "liutao",
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
            searchMap.put("rankDate", String.valueOf(params.get("rankDate")));
            List<Map<String, Object>> resultValue = consultConversationForwardRecordsService.findConversationRankList(searchMap);
            resultMap.put("rankListValue",resultValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

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
    @RequestMapping(value = "/doctorList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> doctorList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {

        HashMap<String,Object> response = new HashMap<String, Object>();
        Page<DoctorVo> responsePage = null;

        String pageNo = String.valueOf(params.get("pageNo"));
        String pageSize = String.valueOf(params.get("pageSize"));

        if(StringUtils.isNotNull(pageNo) && StringUtils.isNotNull(pageSize)){

            Page<DoctorVo> page = FrontUtils.generatorPage(pageNo, pageSize);

            List<String> userList = consultSessionService.getOnlineCsList();

            if(userList!=null && userList.size()>0){
                responsePage = consultSessionService.getOnlineCsListInfo(page, userList);
                response.put("onLineDoctor",responsePage.getList());
            }
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
            "fromServer":""
        }
         @return
         {
             "result":"success",
             "sessionId":"fwefewf"
         }
     */
    @RequestMapping(value = "/transfer", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> transfer(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<String, Object>();

        Integer sessionId= (Integer)params.get("sessionId");
        String transferer= String.valueOf(params.get("transferer"));
        String remark = (String)params.get("remark");
        if(null != sessionId && StringUtils.isNotNull(transferer)){
            consultSessionForwardRecordsService.transferSession(sessionId,transferer,remark);
            response.put("result", "success");
        }else {
            response.put("result", "failure");
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
    @ResponseBody
    Map<String, Object> cancelTransferringSession(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<String, Object>();
        Integer sessionId= (Integer)params.get("sessionId");
        String transferer= String.valueOf(params.get("toCsUserId"));
        String remark = String.valueOf(params.get("remark"));
        if(null != sessionId && StringUtils.isNotNull(transferer)){
            consultSessionForwardRecordsService.cancelTransferringSession(sessionId, transferer, remark);
            response.put("result", "success");
        }
        response.put("result", "failure");
        return  response;
    }


    /***
     * 获取回复 type 为 myAnswer获取我的回复  common获取公共回复
     *
     * @param {"type":"myAnswer"} commonAnswer
     @return
     {
        （ "commonAnswer": [
             {
                 "firstId": "fdsaf",
                 "name": "fwef",
                 "secondAnswer": [
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     },
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     },
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     }
                 ]
             },
             {
                 "firstId": "fdsaf",
                 "name": "fwef",
                 "secondAnswer": [
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     },
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     },
                     {
                         "secondId": "fsad",
                         "name": "fdsf"
                     }
                 ]
             },
             {
                 "firstId": "fdsaf",
                 "name": "fwef",
                 "secondAnswer": [
                     {
                     "secondId": "fsad",
                     "name": "fdsf"
                     },
                     {
                     "secondId": "fsad",
                     "name": "fdsf"
                     },
                     {
                     "secondId": "fsad",
                     "name": "fdsf"
                     }
                ]
             }
         ]
     }）
     */

    @RequestMapping(value = "/commonAnswer", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String commonAnswer(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {

        String type = String.valueOf(params.get("type"));

        if(StringUtils.isNotNull(type)){

            return answerService.findConsultAnswer(type);
        }

        return "false";
    }


    /***
     * 医生修改自己的回复,没有的话先执行插入操作
     *
     * @param
        {
         "myanswer":"json串"
        }
     @return
     {
     "result":"success",
     }
     */
    @RequestMapping(value = "/myAnswer/modify", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> modify(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<String, Object>();
        String myanswer = String.valueOf(params.get("myanswer"));
        response.put("result","failure");
        try {
            answerService.upsertConsultAnswer(myanswer);
            response.put("result","success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


    /***
     * 医生删除自己的回复
     @return
     {
     "result":"success",
     }
     */
    @RequestMapping(value = "/myAnswer/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String delete(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        try{
            answerService.deleteMyConsultAnswer();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }


    /***
     * 接收或者拒绝转接
     * @param {"sessionId":123,"forwardRecordId":4234,"toCsUserId":"123","toCsUserName":"小明"，“operation”:""}
     @return
     {
      "result":"success",
     }
     */
    @RequestMapping(value = "/myAnswer/react2Transfer", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String react2Transfer(@RequestBody Map<String, Object> params) {
        try{
            consultSessionForwardRecordsService.react2Transfer(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
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

    @RequestMapping(value = "/recordList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {


        String recordType = String.valueOf(params.get("recordType"));  //user 用户端获取记录  doctor 医生端获取记录
        String toUserId = "5962a0ab127748c19e767c5808e7faa9" ;//String.valueOf(params.get("toUserId"))  便于前台测试临时
        String fromUserId = "edaf87cfa0d94b9e87dfdcde0ca491b0";//String.valueOf(params.get("fromUserId"))
        int pageNo = 0;
        int pageSize = 1;
        PaginationVo<ConsultRecordMongoVo> pagination = null;
        Map<String,Object> response = new HashMap<String, Object>();
        if(null != params.get("pageNo") && null != params.get("pageSize")){
            pageNo = Integer.parseInt((String)params.get("pageNo"));
            pageSize = Integer.parseInt((String)params.get("pageSize"));
        }

        if(recordType.equals("user") && StringUtils.isNotNull(toUserId) && pageSize > 0){
            Query query = new Query(where("toUserId").is(toUserId)).with(new Sort(Direction.DESC, "create_date"));
            pagination = consultRecordService.getPage(pageNo, pageSize, query);
        }
        else if (recordType.equals("doctor") && StringUtils.isNotNull(toUserId) && StringUtils.isNotNull(fromUserId)){
            Query query = new Query(where("toUserId").is(toUserId).and("fromUserId")
                    .is(fromUserId)).with(new Sort(Direction.DESC, "create_date"));
            pagination = consultRecordService.getPage(pageNo, pageSize, query);
        }

        response.put("records", pagination!=null?pagination.getDatas():"");
        response.put("pageNo",pageNo);
        response.put("pageSize",pageSize);


        return response;
    }

    /***
     * 生成聊天记录
     */
    @RequestMapping(value = "/produceRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void produceRecord(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {

        ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
        int insertNumber = consultRecordService.saveRecord(consultRecordMongoVo);

    }

    /**
     * 获取客服医生列表
     */
    @RequestMapping(value = "/getCSDoctorList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getCSDoctorList(Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String,Object> response = new HashMap<String, Object>();
        List<User> users ;
        User user = new User();
        user.setUserType("cs_doctor");
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
}
