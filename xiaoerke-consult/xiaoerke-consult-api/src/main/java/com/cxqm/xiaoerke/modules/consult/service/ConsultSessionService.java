package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ConsultSessionService {

    List<Map<String,Object>> getConsultInfo(String openId);

    int saveConsultInfo(ConsultSession record);

    int updateSessionInfo(ConsultSession consultSession);

    int updateSessionInfoByUserId(ConsultSession consultSession);

    String removeSessionById(HttpServletRequest request, Map<String, Object> param);
    
    List<ConsultSession> selectBySelective(ConsultSession consultSession);

    List<String> getOnlineCsList();

    HashMap<String,Object> getOnlineCsListInfo(List<String> userList);

    List<ConsultSession> getAlreadyAccessUsers(ConsultSession richConsultSession);


    String clearSession(String sessionId, String userId);
}