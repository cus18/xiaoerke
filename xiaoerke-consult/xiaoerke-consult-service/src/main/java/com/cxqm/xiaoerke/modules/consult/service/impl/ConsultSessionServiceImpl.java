package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMongoUtilsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class ConsultSessionServiceImpl implements ConsultSessionService {

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultMongoUtilsService consultMongoUtilsService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultSessionDao consultSessionDao;

    @Override
    public List<Map<String, Object>> getConsultInfo(String openId) {
        return consultSessionDao.selectByPrOpenid(openId);
    }

    @Override
    public int saveConsultInfo(ConsultSession record) {
        return consultSessionDao.insertSelective(record);
    }

    @Override
    public int updateSessionInfo(ConsultSession consultSession) {
        return consultSessionDao.updateByPrimaryKeySelective(consultSession);
    }

    @Override
    public int updateSessionInfoByUserId(ConsultSession consultSession) {
        return consultSessionDao.updateSessionInfoByUserId(consultSession);
    }

    @Override
    public String removeSessionById(HttpServletRequest request, Map<String, Object> param) {
        String sessionId = (String) param.get("sessionId");
        if (StringUtils.isNotNull(sessionId)) {
            request.getSession().removeAttribute(sessionId);
            return "success";
        }
        return "failure";
    }

	@Override
	public List<ConsultSession> selectBySelective(ConsultSession consultSession) {
		return consultSessionDao.selectBySelective(consultSession);
	}

    @Override
    public List<String> getOnlineCsList() {
        return ConsultSessionManager.getSessionManager().getOnlineCsList();
    }

    @Override
    public HashMap<String,Object> getOnlineCsListInfo(List<String> userList){
        return  consultSessionDao.getOnlineCsListInfo(userList);
    }

    @Override
    public List<ConsultSession> getAlreadyAccessUsers(ConsultSession richConsultSession) {
        return null;
    }

    @Override
    public String clearSession(String sessionId, String userId){
        try{
            //数据库中的consultSession，状态由ongoing变成completed
            ConsultSession consultSession = new ConsultSession();
            consultSession.setId(Integer.parseInt(sessionId));
            consultSession.setUserId(userId);
            consultSession.setStatus(ConsultSession.STATUS_ONGOING);
            List<ConsultSession> consultSessionList = this.selectBySelective(consultSession);
            consultSession = consultSessionList.get(0);
            consultSession.setStatus(ConsultSession.STATUS_COMPLETED);
            int status = this.updateSessionInfo(consultSession);
            if(status==1){
                //清除redis内的数据
                sessionRedisCache.removeConsultSessionBySessionId(Integer.parseInt(sessionId));
                sessionRedisCache.removeUserIdSessionIdPair(userId);

                //清除内存内的数据
                ConsultSessionManager.getSessionManager().removeUserSession(userId);

                //删除最后一次会话
                consultRecordService.deleteConsultSessionStatusVo(new Query().addCriteria(new Criteria().where("sessionId").is(sessionId)));

                //删除用户的临时聊天记录
                consultRecordService.deleteConsultTempRecordVo(new Query().addCriteria(new Criteria().where("userId").is(userId)));
            }

            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "failure";
        }
    }
}