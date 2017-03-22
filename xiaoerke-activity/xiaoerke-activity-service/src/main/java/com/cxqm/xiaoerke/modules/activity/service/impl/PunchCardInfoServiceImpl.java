package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.PunchCardInfoDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardInfoVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
@Service
@Transactional(readOnly = false)
public class PunchCardInfoServiceImpl implements PunchCardInfoService {

        @Autowired
        private PunchCardInfoDao punchCardInfoDao ;

        @Override
        public int deleteByPrimaryKey(String id) {
                return punchCardInfoDao.deleteByPrimaryKey(id);
        }

        @Override
        public int insert(PunchCardInfoVo record) {
                return punchCardInfoDao.insert(record);
        }

        @Override
        public int insertSelective(PunchCardInfoVo record) {
                return punchCardInfoDao.insertSelective(record);
        }

        @Override
        public PunchCardInfoVo selectByPrimaryKey(String id) {
                return punchCardInfoDao.selectByPrimaryKey(id);
        }

        @Override
        public int updateByPrimaryKeySelective(PunchCardInfoVo record) {
                return punchCardInfoDao.updateByPrimaryKeySelective(record);
        }

        @Override
        public int updateByPrimaryKey(PunchCardInfoVo record) {
                return punchCardInfoDao.updateByPrimaryKey(record);
        }

        @Override
        public List<PunchCardInfoVo> getPunchCardInfoBySelective(PunchCardInfoVo vo) {
                return punchCardInfoDao.getPunchCardInfoBySelective(vo);
        }

        @Override
        public List<PunchCardInfoVo> getLastOnePunchCardInfoVo() {
                List<PunchCardInfoVo> resultList = punchCardInfoDao.getLastOnePunchCardInfoVo();
                return resultList ;
        }
}
