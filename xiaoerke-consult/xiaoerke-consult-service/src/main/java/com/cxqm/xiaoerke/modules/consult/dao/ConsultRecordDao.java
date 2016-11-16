package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;

import java.util.List;

@MyBatisDao
public interface ConsultRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultRecordVo record);

    int insertConsultRecordBatch(List<ConsultRecordVo> consultRecordVos);

    int insertSelective(ConsultRecordVo record);

    List<ConsultRecordVo> selectByVo(ConsultRecordVo record);

    int updateByPrimaryKeySelective(ConsultRecordVo record);

    int updateByPrimaryKey(ConsultRecordVo record);

    List<ConsultRecordMongoVo> findUserConsultInfoBySessionId(ConsultRecordMongoVo consultRecordMongoVo);
}