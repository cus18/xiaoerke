package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.RedpackageResultsInfoDao;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageResultsInfoVo;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageResultsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-2-20.
 */
@Service
public class RedPackageResultsInfoServiceImpl implements RedPackageResultsInfoService {

        @Autowired
        private RedpackageResultsInfoDao redpackageResultsInfoDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return redpackageResultsInfoDao.deleteByPrimaryKey(id);
        }

        @Override
        public int insert(RedpackageResultsInfoVo record) {
                return redpackageResultsInfoDao.insert(record);
        }

        @Override
        public int insertSelective(RedpackageResultsInfoVo record) {
                return redpackageResultsInfoDao.insertSelective(record);
        }

        @Override
        public RedpackageResultsInfoVo selectByPrimaryKey(String id) {
                return redpackageResultsInfoDao.selectByPrimaryKey(id);
        }

        @Override
        public int updateByPrimaryKeySelective(RedpackageResultsInfoVo record) {
                return redpackageResultsInfoDao.updateByPrimaryKeySelective(record);
        }

        @Override
        public int updateByPrimaryKey(RedpackageResultsInfoVo record) {
                return redpackageResultsInfoDao.updateByPrimaryKey(record);
        }

        @Override
        public List<RedpackageResultsInfoVo> getRedPackageResultsBySelective(RedpackageResultsInfoVo record) {
                return redpackageResultsInfoDao.getRedPackageResultsBySelective(record);
        }
}
