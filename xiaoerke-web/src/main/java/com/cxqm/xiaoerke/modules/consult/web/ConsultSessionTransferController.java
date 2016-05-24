package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONArray;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2016-5-18.
 * 转接列表发起会话
 */
@Controller
@RequestMapping(value = "consultSession/transfer")
public class ConsultSessionTransferController {

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private ConsultTransferListVoService consultTransferListVoService;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @RequestMapping(value = "/createMoreUserConsultSession", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String, Object> createMoreWXUserConsultSession(@RequestBody Map<String, Object> params){

        HashMap<String ,Object> response = new HashMap<String, Object>();
        List<HashMap<String,Object>> requestData = (List<HashMap<String,Object>>) params.get("content");
        ConsultSessionManager consultSessionManager = ConsultSessionManager.getSessionManager();
        List<HashMap<String,Object>> transferList = new ArrayList<HashMap<String, Object>>();
        if(requestData !=null && requestData.size() >0){
            for(int i=0 ; i<requestData.size();i++){
                HashMap<String,Object> data = new HashMap<String, Object>();
                data.put("id", requestData.get(i).get("id"));
                data.put("userId", requestData.get(i).get("userId"));
                Integer sessionId = sessionRedisCache.getSessionIdByUserId((String) requestData.get(i).get("userId"));
                if(sessionId != null){
                    RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                    if(richConsultSession != null){
                        if(ConsultSession.STATUS_ONGOING.equalsIgnoreCase(richConsultSession.getStatus())){
                            response.put("status","ongoing");
                            break ;
                        }
                    }
                }
                transferList.add(data);
            }
            if(transferList != null && transferList.size()>0){

                JSONArray jsonArray = new JSONArray();
                JSONArray failureIdArray = new JSONArray();
                for(HashMap<String,Object> hashMap : transferList){
                    try{
                        HashMap<String,Object> resultMap = consultSessionManager.createConsultSession(wechatAttentionService,(String)hashMap.get("userId"));
                        if(resultMap!= null && "success".equalsIgnoreCase((String) resultMap.get("result"))){
                            ConsultTransferListVo consultTransferListVo;
                            String status = "complete";
                            String delFlag = "0";
                            consultTransferListVo = new ConsultTransferListVo();
                            consultTransferListVo.setStatus(status);
                            consultTransferListVo.setDelFlag(delFlag);
                            consultTransferListVo.setId((Integer)hashMap.get("id"));
                            int count = consultTransferListVoService.updateConsultTransferByPrimaryKey(consultTransferListVo);
                            if(count > 0){
                                consultSessionManager.refreshConsultTransferList(UserUtils.getUser().getId());
                            }
                            jsonArray.add(resultMap.get("userId"));
                            response.put("status", "success");
                        }else{
                            HashMap<Object,Object> failureReson =  new HashMap<Object, Object>();
                            failureReson.put("id",hashMap.get("id"));
                            failureReson.put("result",resultMap.get("result"));
                            failureIdArray.add(failureReson);
                            response.put("status","failure");
                        }
                        response.put("userIds",jsonArray);
                        response.put("failureUserIds",failureIdArray);
                    }catch(Exception exception){
                        exception.getStackTrace();
                    }

                }
            }
        }
        return response;
    }
}
