package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface ConsultSessionPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultSessionPropertyVo record);

    int insertSelective(ConsultSessionPropertyVo record);

    ConsultSessionPropertyVo selectByField(ConsultSessionPropertyVo consultSessionPropertyVo);

    int updateByPrimaryKeySelective(ConsultSessionPropertyVo record);

    int updateByPrimaryKey(ConsultSessionPropertyVo record);

    ConsultSessionPropertyVo findConsultSessionPropertyByUserId(@Param("userId") String userId);

    int addPermTimes(@Param("userId") String userId);

    int updateMonthTime();

}