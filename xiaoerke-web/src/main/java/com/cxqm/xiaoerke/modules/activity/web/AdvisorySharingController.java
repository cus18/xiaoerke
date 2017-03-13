package com.cxqm.xiaoerke.modules.activity.web;

import com.cxqm.xiaoerke.modules.activity.service.AdvisorySharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 17/3/10.
 */
@Controller
@RequestMapping(value = "advisoryShare")
public class AdvisorySharingController {

    @Autowired
    private AdvisorySharingService advisorySharingService;

    /**
     * 记录sessionid以及用户描述信息
     * @param
     * @return map
     */
    @RequestMapping(value = "/sharSeConsult", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> sharSeConsult(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String sessionId = (String) params.get("sessionId");
        String content = (String) params.get("content");
        advisorySharingService.sharSeConsult("haveRecord"+sessionId,content);
        return responseMap;
    }


    /**
     * 根据sessionid查询相对应的聊谈记录
     * @param
     * @return map
     */
    @RequestMapping(value = "/conversationRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> conversationRecord(@RequestBody Map<String, Object> params){
        String sessionId = (String) params.get("sessionId");
        int pageNum = (Integer) params.get("pageNum");
        int pageSize = (Integer) params.get("pageSize");
        return advisorySharingService.conversationRecord(sessionId,pageNum,pageSize);
    }
}
