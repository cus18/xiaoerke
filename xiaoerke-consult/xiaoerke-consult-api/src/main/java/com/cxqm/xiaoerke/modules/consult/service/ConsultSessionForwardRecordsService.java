package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ConsultSessionForwardRecordsService {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultSessionForwardRecordsVo record);

    int insertSelective(ConsultSessionForwardRecordsVo record);

    ConsultSessionForwardRecordsVo selectByPrimaryKey(Long id);

    int updateRejectedTransfer(ConsultSessionForwardRecordsVo record);

    int updateByPrimaryKey(ConsultSessionForwardRecordsVo record);

    List<Map<String, Object>> findConversationRankList(Map<String,Object> searchMap);

	int save(ConsultSessionForwardRecordsVo forwardRecordsVo);
	
	int updateAcceptedTransfer(ConsultSessionForwardRecordsVo record);
	
	int cancelTransfer(ConsultSessionForwardRecordsVo record);

    void transferSession(Integer sessionId, String transferer, String remark);

    void cancelTransferringSession(Integer sessionId, String transferer, String remark);

    void react2Transfer(Map<String,Object> map);

    Page<ConsultSessionForwardRecordsVo> getConsultUserListRecently(Page<ConsultSessionForwardRecordsVo> page, HashMap<String, Object> hashMap);

    List<ConsultSessionForwardRecordsVo> getWaitJoinList(String csUserId);
}