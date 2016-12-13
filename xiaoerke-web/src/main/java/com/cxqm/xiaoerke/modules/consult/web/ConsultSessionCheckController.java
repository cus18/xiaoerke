package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
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
import java.util.Map;

@Controller
@RequestMapping(value = "consult")
public class ConsultSessionCheckController extends BaseController{

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    /**
     * 手动清理残留的session
     */
    @RequestMapping(value="/sessionCheck",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String,Object> sessionCheck() throws UnsupportedEncodingException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        //获取用户与平台最后交流时间
        Query query = new Query(where("status").is("ongoing"));
        List<ConsultSessionStatusVo> consultSessionStatusVos = consultRecordService.querySessionStatusList(query);
        if(consultSessionStatusVos != null && consultSessionStatusVos.size() > 0){
            for(ConsultSessionStatusVo consultSessionStatusVo : consultSessionStatusVos){
                if(consultSessionStatusVo !=null && consultSessionStatusVo.getLastMessageTime()!=null){
                    if(DateUtils.pastMinutes(consultSessionStatusVo.getLastMessageTime())>120L){
                        //根据sessionId查询consult_conversation_forward_records表，状态为waiting不执行
                        ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
                        consultSessionForwardRecordsVo.setConversationId(Long.parseLong(consultSessionStatusVo.getSessionId()));
                        consultSessionForwardRecordsVo.setStatus("waiting");
                        List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVos = consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
                        if(consultSessionForwardRecordsVos.size() == 0){
                            consultSessionService.clearSession(consultSessionStatusVo.getSessionId(),
                                    consultSessionStatusVo.getUserId());
                        }
                    }
                }
            }
        }

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
                RichConsultSession consultSessionValue = ConsultUtil.transferMapToRichConsultSession((Map<Object, Object>) consultSessionObject);
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
