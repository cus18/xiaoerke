package com.cxqm.xiaoerke.modules.sys.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface BabyBaseInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyBaseInfoVo record);

    int insertSelective(BabyBaseInfoVo record);

    BabyBaseInfoVo selectByPrimaryKey(Integer id);

    List<BabyBaseInfoVo> selectByUserId(String userId);

    int updateByPrimaryKeySelective(BabyBaseInfoVo record);

    int updateByPrimaryKey(BabyBaseInfoVo record);

    List<BabyBaseInfoVo> selectByOpenId(String openId);

    List<BabyBaseInfoVo> selectUserBabyInfo(String openId);

    int updateUserId(BabyBaseInfoVo record);
    
    /**
     * 根据条件查询宝宝信息
     * sunxiao
     */
    List<BabyBaseInfoVo> getBabyInfoByInfo(BabyBaseInfoVo vo);
    
}