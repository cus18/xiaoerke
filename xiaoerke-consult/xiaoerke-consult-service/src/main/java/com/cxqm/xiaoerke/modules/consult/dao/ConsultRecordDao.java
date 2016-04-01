package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
@MyBatisDao
public interface ConsultRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultRecordVo record);

    int insertSelective(ConsultRecordVo record);

    ConsultRecordVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsultRecordVo record);

    int updateByPrimaryKey(ConsultRecordVo record);
}