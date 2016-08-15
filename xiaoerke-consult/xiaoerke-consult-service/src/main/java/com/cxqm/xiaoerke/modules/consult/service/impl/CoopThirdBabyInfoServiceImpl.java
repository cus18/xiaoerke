package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.CoopThirdBabyInfoDao;
import com.cxqm.xiaoerke.modules.consult.entity.CoopThirdBabyInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.CoopThirdBabyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-8-15.
 */
@Service
@Transactional(readOnly = false)
public class CoopThirdBabyInfoServiceImpl implements CoopThirdBabyInfoService{

        @Autowired
        private CoopThirdBabyInfoDao coopThirdBabyInfoDao ;

        @Override
        public int addCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo) {
                int result = coopThirdBabyInfoDao.addCoopThirdBabyInfo(coopThirdBabyInfoVo);
                return result;
        }

        @Override
        public List<CoopThirdBabyInfoVo> findCoopThirdBabyInfo(CoopThirdBabyInfoVo coopThirdBabyInfoVo) {
                List<CoopThirdBabyInfoVo> result = coopThirdBabyInfoDao.findCoopThirdBabyInfo(coopThirdBabyInfoVo);
                return result;
        }
}
