package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;

@MyBatisDao
public interface ConsultSessionPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultSessionPropertyVo record);

    int insertSelective(ConsultSessionPropertyVo record);

    ConsultSessionPropertyVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultSessionPropertyVo record);

    int updateByPrimaryKey(ConsultSessionPropertyVo record);
}