package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.EnglishActivityDao;
import com.cxqm.xiaoerke.modules.activity.entity.EnglishActivityVo;
import com.cxqm.xiaoerke.modules.activity.service.EnglishActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangbaowei on 2017/4/12.
 */

@Service
@Transactional(readOnly = false)
public class EnglishActivityServiceImpl implements EnglishActivityService {

    @Autowired
    private EnglishActivityDao englishActivityDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return englishActivityDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(EnglishActivityVo record) {
        return englishActivityDao.insert(record);
    }

    @Override
    public int insertSelective(EnglishActivityVo record) {
        return englishActivityDao.insertSelective(record);
    }

    @Override
    public EnglishActivityVo selectByPrimaryKey(Integer id) {
        return englishActivityDao.selectByPrimaryKey(id);
    }

    @Override
    public EnglishActivityVo selectByopenId(String openid) {
        return englishActivityDao.selectByopenId(openid);
    }

    @Override
    public int updateByPrimaryKeySelective(EnglishActivityVo record) {
        return englishActivityDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(EnglishActivityVo record) {
        return englishActivityDao.updateByPrimaryKey(record);
    }
}
