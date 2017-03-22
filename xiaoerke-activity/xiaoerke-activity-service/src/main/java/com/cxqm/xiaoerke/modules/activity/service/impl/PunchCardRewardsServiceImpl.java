package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardRewardsDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardsVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
@Service
@Transactional(readOnly = false)
public class PunchCardRewardsServiceImpl implements PunchCardRewardsService {

        @Autowired
        private PunchCardRewardsDao punchCardRewardsDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return punchCardRewardsDao.deleteByPrimaryKey(id);
        }

        @Override
        public int insert(PunchCardRewardsVo record) {
                return punchCardRewardsDao.insert(record);
        }

        @Override
        public int insertSelective(PunchCardRewardsVo record) {
                return punchCardRewardsDao.insertSelective(record);
        }

        @Override
        public PunchCardRewardsVo selectByPrimaryKey(String id) {
                return punchCardRewardsDao.selectByPrimaryKey(id);
        }

        @Override
        public int updateByPrimaryKeySelective(PunchCardRewardsVo record) {
                return punchCardRewardsDao.updateByPrimaryKeySelective(record);
        }

        @Override
        public int updateByPrimaryKey(PunchCardRewardsVo record) {
                return punchCardRewardsDao.updateByPrimaryKey(record);
        }

        @Override
        public Map<String, Object> getSelfRewardsInfo(String openId) {
                return punchCardRewardsDao.getSelfRewardsInfo(openId);
        }

        @Override
        public List<Map<String,Object>> getPunchCardRewards(PunchCardRewardsVo vo) {
                return punchCardRewardsDao.getPunchCardRewards(vo);
        }

        @Override
        public int batchInsertPunchCardRewards(List list) {
                return punchCardRewardsDao.batchInsertPunchCardRewards(list);
        }


}
