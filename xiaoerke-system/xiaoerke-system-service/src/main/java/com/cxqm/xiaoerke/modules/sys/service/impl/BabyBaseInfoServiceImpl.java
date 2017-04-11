package com.cxqm.xiaoerke.modules.sys.service.impl;

import com.cxqm.xiaoerke.modules.sys.dao.BabyBaseInfoDao;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangbaowei on 16/2/18.
 */

@Service
public class BabyBaseInfoServiceImpl implements BabyBaseInfoService {

    @Autowired
    private BabyBaseInfoDao babyBaseInfoDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
       return babyBaseInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(BabyBaseInfoVo record) {
       return babyBaseInfoDao.insert(record);
    }

    @Override
    public int insertSelective(BabyBaseInfoVo record) {
        return babyBaseInfoDao.insertSelective(record);
    }

    @Override
    public BabyBaseInfoVo selectByPrimaryKey(Integer id) {
        return babyBaseInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public List<BabyBaseInfoVo> selectByUserId(String userId) {
        return babyBaseInfoDao.selectByUserId(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(BabyBaseInfoVo record) {
        return babyBaseInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(BabyBaseInfoVo record) {
        return babyBaseInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public List<BabyBaseInfoVo> selectByOpenid(String openId) {
        return babyBaseInfoDao.selectByOpenId(openId);
    }

    @Override
    public List<BabyBaseInfoVo> selectUserBabyInfo(String openId) {
       return babyBaseInfoDao.selectUserBabyInfo(openId);
    }

    @Override
    public int updateUserId(BabyBaseInfoVo record){
        return babyBaseInfoDao.updateUserId(record);
    }
    
    /**
     * 根据条件查询宝宝信息
     * sunxiao
     */
    @Override
	public List<BabyBaseInfoVo> getBabyInfoByInfo(BabyBaseInfoVo vo) {
		// TODO Auto-generated method stub
		return babyBaseInfoDao.getBabyInfoByInfo(vo);
	}

    /**
     * int updateBabyInfoByUserId();
     */

    public int updateBabyInfoByUserId(BabyBaseInfoVo record){
        return babyBaseInfoDao.updateBabyInfoByUserId(record);
    }

    @Override
    public BabyBaseInfoVo selectLastBabyInfo(String openid) {
        return babyBaseInfoDao.selectLastBabyInfo(openid);
    }

    @Override
    public Integer insertssBean(BabyBaseInfoVo record) {
        return babyBaseInfoDao.insertBean(record);
    }
}
