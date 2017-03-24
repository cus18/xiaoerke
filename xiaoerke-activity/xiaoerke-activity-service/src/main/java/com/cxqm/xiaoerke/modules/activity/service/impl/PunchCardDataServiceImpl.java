package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardDataDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardDataVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-17.
 */

@Service
@Transactional(readOnly = false)
public class PunchCardDataServiceImpl implements PunchCardDataService {

        @Autowired
        private PunchCardDataDao punchCardDataDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return punchCardDataDao.deleteByPrimaryKey(id);
        }

        @Override
        public int insert(PunchCardDataVo record) {
                return punchCardDataDao.insert(record);
        }

        @Override
        public int insertSelective(PunchCardDataVo record) {
                return punchCardDataDao.insertSelective(record);
        }

        @Override
        public PunchCardDataVo selectByPrimaryKey(String id) {
                return punchCardDataDao.selectByPrimaryKey(id);
        }

        @Override
        public int updateByPrimaryKeySelective(PunchCardDataVo record) {
                return punchCardDataDao.updateByPrimaryKeySelective(record);
        }

        @Override
        public int updateByPrimaryKey(PunchCardDataVo record) {
                return punchCardDataDao.updateByPrimaryKey(record);
        }

        @Override
        public List<PunchCardDataVo> getLastOneData() {
                return punchCardDataDao.getLastOneData();
        }
}
