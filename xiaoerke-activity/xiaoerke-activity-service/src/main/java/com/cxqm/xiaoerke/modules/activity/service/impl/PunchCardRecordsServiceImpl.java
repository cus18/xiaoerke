package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardRecordsDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRecordsVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRecordsService;
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
public class PunchCardRecordsServiceImpl implements PunchCardRecordsService {

        @Autowired
        private PunchCardRecordsDao punchCardRecordsDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return punchCardRecordsDao.deleteByPrimaryKey(id);
        }

        @Override
        public int insert(PunchCardRecordsVo record) {
                return punchCardRecordsDao.insert(record);
        }

        @Override
        public int insertSelective(PunchCardRecordsVo record) {
                return punchCardRecordsDao.insertSelective(record);
        }

        @Override
        public PunchCardRecordsVo selectByPrimaryKey(String id) {
                return punchCardRecordsDao.selectByPrimaryKey(id);
        }

        @Override
        public int updateByPrimaryKeySelective(PunchCardRecordsVo record) {
                return punchCardRecordsDao.updateByPrimaryKeySelective(record);
        }

        @Override
        public int updateByPrimaryKey(PunchCardRecordsVo record) {
                return punchCardRecordsDao.updateByPrimaryKey(record);
        }

        @Override
        public int getSelfPunchCardCount(String openId) {
                return punchCardRecordsDao.getSelfPunchCardCount(openId);
        }

        @Override
        public List<PunchCardRecordsVo> getSelfPunchCardRecords(PunchCardRecordsVo vo) {
                return punchCardRecordsDao.getSelfPunchCardRecords(vo);
        }

        @Override
        public List<PunchCardRecordsVo> getLastPunchCardRecord(PunchCardRecordsVo vo) {
                return punchCardRecordsDao.getLastPunchCardRecord(vo);
        }

        @Override
        public List<Map<String,Object>> getTodayPunchCardRecords(Map map) {
                return punchCardRecordsDao.getTodayPunchCardRecords(map);
        }

        @Override
        public List<Map<String, Object>> getTodayPayPersonNum(Map map) {
                return punchCardRecordsDao.getTodayPayPersonNum(map);
        }
}
