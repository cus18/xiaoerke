package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardDataDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardDataVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-17.
 */

@Service
public class PunchCardDataServiceImpl implements PunchCardDataService {

        @Autowired
        private PunchCardDataDao punchCardDataDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return 0;
        }

        @Override
        public int insert(PunchCardDataVo record) {
                return 0;
        }

        @Override
        public int insertSelective(PunchCardDataVo record) {
                return 0;
        }

        @Override
        public PunchCardDataVo selectByPrimaryKey(String id) {
                return null;
        }

        @Override
        public int updateByPrimaryKeySelective(PunchCardDataVo record) {
                return 0;
        }

        @Override
        public int updateByPrimaryKey(PunchCardDataVo record) {
                return 0;
        }

        @Override
        public List<PunchCardDataVo> getLastOneData() {
                return punchCardDataDao.getLastOneData();
        }
}
