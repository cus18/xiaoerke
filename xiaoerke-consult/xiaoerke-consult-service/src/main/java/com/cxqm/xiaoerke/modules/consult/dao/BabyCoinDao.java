package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import org.springframework.stereotype.Repository;

@MyBatisDao
@Repository
public interface BabyCoinDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyCoinVo record);

    int insertSelective(BabyCoinVo record);

    BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo);

    int updateByPrimaryKeySelective(BabyCoinVo record);

    int updateByPrimaryKey(BabyCoinVo record);
}