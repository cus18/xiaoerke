
package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.AnswerService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping(value = "consult/doctor")
public class ConsultDoctorAnswerController extends BaseController {

    @Autowired
    private AnswerService answerService;

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
    @RequestMapping(value = "/answerValue", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody String consultAnswerValue(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {

        String type = String.valueOf(params.get("type"));

        if(StringUtils.isNotNull(type)){

            return answerService.findConsultAnswer(type);
        }

        return "";
    }


    /***
     * 该接口有两个功能：1、医生修改自己的回复,没有的话先执行插入操作    2、修改公共回复，没有的话先执行插入操作
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
    @RequestMapping(value="/Answer/modify",method=RequestMethod.POST, headers = {"content-type=application/json","content-type=application/xml"})
    public
    @ResponseBody Map<String, Object> modify(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> tranMap = new HashMap<String, Object>();

        String answerType = String.valueOf(params.get("answerType"));
        if(answerType.equals("myAnswer")){
            tranMap.put("myAnswer",params.get("answer"));
        }else if(answerType.equals("commonAnswer")){
            tranMap.put("commonAnswer",params.get("answer"));
        }

        String answer = JSON.toJSONString(tranMap);

        response.put("result","failure");
        try {
            answerService.upsertConsultAnswer(answerType,answer);
            response.put("result", "success");
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

}
