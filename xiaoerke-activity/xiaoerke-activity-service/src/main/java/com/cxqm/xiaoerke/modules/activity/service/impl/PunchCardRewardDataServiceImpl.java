package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardRewardDataDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardDataVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-27.
 */
@Service
@Transactional (readOnly = false)
public class PunchCardRewardDataServiceImpl implements PunchCardRewardDataService{

        @Autowired
        private PunchCardRewardDataDao punchCardRewardDataDao ;

        @Override
        public List<PunchCardRewardDataVo> getMoreDataBySelective(PunchCardRewardDataVo record) {
                return punchCardRewardDataDao.getMoreDataBySelective(record);
        }

        @Override
        public int batchInsertPunchCardData(List list) {
                return punchCardRewardDataDao.batchInsertPunchCardData(list);
        }
}
