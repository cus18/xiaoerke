package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@MyBatisDao
public interface ConsultSessionForwardRecordsDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultSessionForwardRecordsVo record);

    int insertSelective(ConsultSessionForwardRecordsVo record);

    ConsultSessionForwardRecordsVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsultSessionForwardRecordsVo record);

    int updateByPrimaryKey(ConsultSessionForwardRecordsVo record);

    List<Map<String, Object>> findConversationRankList(Map<String,Object> searchMap);
    
    int cancelTransfer(ConsultSessionForwardRecordsVo record);

    Page<ConsultSessionForwardRecordsVo> getConsultUserListRecently(Page<ConsultSessionForwardRecordsVo> page,
                                                                    HashMap<String, Object> hashMap);

    List<ConsultSessionForwardRecordsVo> findWaitJoinListByCsUserId(@Param("csUserId")String csUserId,@Param("status")String status);

    List<ConsultSessionForwardRecordsVo> selectConsultForwardList(ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo);
    
}