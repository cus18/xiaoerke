package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface BabyCoinDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyCoinVo record);

    int insertSelective(BabyCoinVo record);

    BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinVo> selectListByBabyCoinVo(BabyCoinVo babyCoinVo);

    List<BabyCoinVo> selectSubBabyCoin(BabyCoinVo babyCoinVo);

    int updateByPrimaryKeySelective(BabyCoinVo record);

    int updateByOpenId(BabyCoinVo record);

    int updateCashByOpenId(BabyCoinVo record);

    int updateBabyCoinInviteNumber(BabyCoinVo babyCoinVo);
}