package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionDao;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class ConsultSessionServiceImpl implements ConsultSessionService {

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
    public Page<DoctorVo> getOnlineCsListInfo(Page<DoctorVo> page,List<String> userList){
        return  consultSessionDao.getOnlineCsListInfo(page, userList);
    }
}