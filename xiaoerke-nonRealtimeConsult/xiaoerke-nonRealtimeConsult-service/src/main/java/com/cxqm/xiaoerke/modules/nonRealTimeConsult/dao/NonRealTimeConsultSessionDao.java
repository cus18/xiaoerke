package com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;

import java.util.List;

@MyBatisDao
public interface NonRealTimeConsultSessionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(NonRealTimeConsultSessionVo record);

    int insertSelective(NonRealTimeConsultSessionVo record);

    List<NonRealTimeConsultSessionVo> selectByNonRealTimeConsultSessionVo(NonRealTimeConsultSessionVo realTimeConsultSessionVo);

    int updateByPrimaryKeySelective(NonRealTimeConsultSessionVo record);

    int updateByPrimaryKeyWithBLOBs(NonRealTimeConsultSessionVo record);

    int updateByPrimaryKey(NonRealTimeConsultSessionVo record);

    NonRealTimeConsultSessionVo selectByPrimaryKey(Integer id);

    void updateEvaluateEarchDay();

    void updateConsultNumEarchDay();
}