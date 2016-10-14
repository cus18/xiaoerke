
package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.service.AnswerService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;
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
    public @ResponseBody HashMap<String,Object> consultAnswerValue(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        String answerValue = "";
        String type = String.valueOf(params.get("type"));
        if(StringUtils.isNotNull(type)){
            answerValue = answerService.findConsultAnswer(type);
        }
        if(StringUtils.isNotNull(answerValue)){
            if(!answerValue.equals("noValue")){
                response.put("result","success");
                response.put("answerValue",answerValue);
            }else{
                response.put("result","failure");
            }
        }else{
            response.put("result","failure");
        }
        return response;
    }


    /***
     * 该接口有两个功能：1、医生修改自己的回复,没有的话先执行插入操作    2、修改公共回复，没有的话先执行插入操作 3、修改诊断描述
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
    @RequestMapping(value="/answer/modify",method=RequestMethod.POST, headers = {"content-type=application/json","content-type=application/xml"})
    public
    @ResponseBody Map<String, Object> modify(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> tranMap = new HashMap<String, Object>();

        String answerType = String.valueOf(params.get("answerType"));
        if(answerType.equals("myAnswer")){
            tranMap.put("myAnswer",params.get("answer"));
        }else if(answerType.equals("commonAnswer") || answerType.equals("diagnosis")){
            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            String doctorManagerStr = sysPropertyVoWithBLOBsVo.getDoctormanagerList();
        //    String csUserId = "8ab94e95afe448dab66403fc5407d0ca";// UserUtils.getUser().getId()
            String csUserId = UserUtils.getUser().getId();
            if (doctorManagerStr.indexOf(csUserId) != -1) {
                if(answerType.equals("diagnosis")){
                    tranMap.put("diagnosis",params.get("answer"));
                }else{
                    tranMap.put("commonAnswer",params.get("answer"));
                }
            }else {
                response.put("result","NoPermission");
                return response;
            }
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
