package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionForwardRecordsDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;

import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class ConsultSessionForwardRecordsServiceImpl implements ConsultSessionForwardRecordsService {

    @Autowired
    private ConsultSessionForwardRecordsDao forwardRecordsDao;

    @Autowired
    private ConsultSessionDao consultConversationDao;
    
    @Override
    public int save(ConsultSessionForwardRecordsVo forwardRecordsVo) {
        return forwardRecordsDao.insertSelective(forwardRecordsVo);
    }
    
    @Override
    public int deleteByPrimaryKey(Long id) {
        return forwardRecordsDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsultSessionForwardRecordsVo forwardRecordsVo) {
        return forwardRecordsDao.insert(forwardRecordsVo);
    }

    @Override
    public int insertSelective(ConsultSessionForwardRecordsVo forwardRecordsVo) {
        return forwardRecordsDao.insertSelective(forwardRecordsVo);
    }

    @Override
    public ConsultSessionForwardRecordsVo selectByPrimaryKey(Long id) {
        return forwardRecordsDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateRejectedTransfer(ConsultSessionForwardRecordsVo forwardRecordsVo) {
        return forwardRecordsDao.updateByPrimaryKeySelective(forwardRecordsVo);
    }

    @Override
    public int updateByPrimaryKey(ConsultSessionForwardRecordsVo forwardRecordsVo) {
        return forwardRecordsDao.updateByPrimaryKey(forwardRecordsVo);
    }

    @Override
    public List<Map<String, Object>> findConversationRankList(Map<String,Object> searchMap) {
        return forwardRecordsDao.findConversationRankList(searchMap);
    }

	@Override
	public int updateAcceptedTransfer(ConsultSessionForwardRecordsVo record) {

		ConsultSession consultSession = new ConsultSession();
    	consultSession.setId(record.getConversationId().intValue());
    	consultSession.setCsUserId(record.getToUserId());
    	consultConversationDao.updateByPrimaryKeySelective(consultSession);

		return forwardRecordsDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int cancelTransfer(ConsultSessionForwardRecordsVo record) {
		return forwardRecordsDao.cancelTransfer(record);
	}

    @Override
    public  void transferSession(Integer sessionId, String transferer, String remark){
        ConsultSessionManager.getSessionManager().transferSession(sessionId,transferer,remark);
    }

    @Override
    public void cancelTransferringSession(Integer sessionId, String transferer, String remark) {
        ConsultSessionManager.getSessionManager().cancelTransferringSession(sessionId, transferer, remark);
    }

    @Override
    public void react2Transfer(Map<String, Object> map) {
        Integer sessionId = (Integer)map.get("sessionId");
        Integer forwardRecordId = (Integer)map.get("forwardRecordId");
        String toCsUserId = (String)map.get("toCsUserId");
        String toCsUserName = (String)map.get("toCsUserName");
        String operation = (String)map.get("operation");
        ConsultSessionManager.getSessionManager().react2Transfer(sessionId, forwardRecordId, toCsUserId,
                toCsUserName, operation);
    }

    @Override
    public Page<ConsultSessionForwardRecordsVo> getConsultUserListRecently(Page<ConsultSessionForwardRecordsVo> page,HashMap<String, Object> hashMap){
       return  forwardRecordsDao.getConsultUserListRecently(page,hashMap);
    }

    @Override
    public List<ConsultSessionForwardRecordsVo> getWaitJoinList(String csUserId) {
        return forwardRecordsDao.findWaitJoinListByCsUserId(csUserId,ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING);
    }

}