package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultEvaluateCoopDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultEvaluateCoopVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultEvaluateCoopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合作方评价信息接口实现
 * Created by jiangzhongge on 2016-8-24.
 */
@Service
@Transactional(readOnly = false)
public class ConsultEvaluateCoopServiceImpl implements ConsultEvaluateCoopService {

        @Autowired
        private ConsultEvaluateCoopDao consultEvaluateCoopDao ;

        @Override
        public int addConsultEvaluateCoops(ConsultEvaluateCoopVo consultEvaluateCoopVo) {
                return consultEvaluateCoopDao.saveConsultEvaluateCoops(consultEvaluateCoopVo);
        }

        @Override
        public List<ConsultEvaluateCoopVo> getConsultEvaluateCoops(ConsultEvaluateCoopVo consultEvaluateCoopVo) {
                return consultEvaluateCoopDao.findConsultEvaluateCoops(consultEvaluateCoopVo);
        }
}
