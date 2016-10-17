package com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
@MyBatisDao
public interface NonRealTimeConsultRecordDao {
    int insert(NonRealTimeConsultRecordVo record);

    int insertSelective(NonRealTimeConsultRecordVo record);
}