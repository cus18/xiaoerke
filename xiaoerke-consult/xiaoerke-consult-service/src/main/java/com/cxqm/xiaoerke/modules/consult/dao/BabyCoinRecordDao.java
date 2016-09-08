package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface BabyCoinRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyCoinRecordVo record);

    int insertSelective(BabyCoinRecordVo record);

    List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo record);

    int updateByPrimaryKeySelective(BabyCoinRecordVo record);

    int updateByPrimaryKey(BabyCoinRecordVo record);
}