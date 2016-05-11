package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import org.springframework.data.mongodb.core.query.Query;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "consult")
public class ConsultSessionCheckController extends BaseController{

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultRecordService consultRecordService;

    /**
     * 手动清理残留的session
     */
    @RequestMapping(value="/sessionCheck",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String,Object> sessionCheck() throws UnsupportedEncodingException {

        ConsultSession consultSession = new ConsultSession();
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessionVOs = consultSessionService.selectBySelective(consultSession);
        for(ConsultSession consultSessionVO:consultSessionVOs){
            Query queryAgain = new Query(where("sessionId").is(String.valueOf(consultSessionVO.getId())));
            List<ConsultSessionStatusVo> consultSessionStatusAgainVos = consultRecordService.querySessionStatusList(queryAgain);
            if(consultSessionStatusAgainVos.size()>0){
                if(consultSessionStatusAgainVos.get(0).getStatus().equals("complete")){
                    consultSessionService.clearSession(consultSessionStatusAgainVos.get(0).getSessionId(),
                            consultSessionStatusAgainVos.get(0).getUserId());
                }
            }
        }

        List<Object> consultSessions = sessionRedisCache.getConsultSessionsByKey();
        if(consultSessions.size()>0){
            for(Object consultSessionObject:consultSessions){
                RichConsultSession consultSessionValue = ConsultUtil.transferMapToRichConsultSession((HashMap<String, Object>) consultSessionObject);
                Query queryAgain = new Query(where("sessionId").is(String.valueOf(consultSessionValue.getId())));
                List<ConsultSessionStatusVo> consultSessionStatusAgainVos = consultRecordService.querySessionStatusList(queryAgain);
                if(consultSessionStatusAgainVos.size()>0){
                    if(consultSessionStatusAgainVos.get(0).getStatus().equals("complete")){
                        //清除redis内的残留数据
                        sessionRedisCache.removeConsultSessionBySessionId(Integer.parseInt(consultSessionStatusAgainVos.get(0).getSessionId()));
                        sessionRedisCache.removeUserIdSessionIdPair(consultSessionStatusAgainVos.get(0).getUserId());
                    }
                }else{
                    //if consultSessionStatusAgainVos have no data, it proved that the data in redis is dirty data, delete it
                    sessionRedisCache.removeConsultSessionBySessionId(consultSessionValue.getId());
                    sessionRedisCache.removeUserIdSessionIdPair(consultSessionValue.getUserId());
                }
            }
        }
        return  null;
    }
}
