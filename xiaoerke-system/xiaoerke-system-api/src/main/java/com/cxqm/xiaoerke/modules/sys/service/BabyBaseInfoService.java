package com.cxqm.xiaoerke.modules.sys.service;

import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;

import java.util.List;

/**
 * Created by wangbaowei on 16/2/18.
 */
public interface BabyBaseInfoService {


    int deleteByPrimaryKey(Integer id);

    int insert(BabyBaseInfoVo record);

    int insertSelective(BabyBaseInfoVo record);

    BabyBaseInfoVo selectByPrimaryKey(Integer id);

    List<BabyBaseInfoVo> selectByUserId(String userId);

    int updateByPrimaryKeySelective(BabyBaseInfoVo record);

    int updateByPrimaryKey(BabyBaseInfoVo record);

    List<BabyBaseInfoVo> selectByOpenid(String openId);

    List<BabyBaseInfoVo> selectUserBabyInfo(String openId);


    int updateUserId(BabyBaseInfoVo record);
    
    /**
     * 根据条件查询宝宝信息
     * sunxiao
     */
    List<BabyBaseInfoVo> getBabyInfoByInfo(BabyBaseInfoVo vo);
    /**
     * 根据userId更新宝宝信息(openId)
     */
    int updateBabyInfoByUserId(BabyBaseInfoVo vo);

    BabyBaseInfoVo selectLastBabyInfo(String  openid);

    Integer insertssBean(BabyBaseInfoVo record);
}
