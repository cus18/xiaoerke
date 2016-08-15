package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionPropertyDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by jiangzhongge on 2016-8-11.
 */
public class ConsultSessionPropertyServiceImpl implements ConsultSessionPropertyService{

        @Autowired
        private ConsultSessionPropertyDao consultSessionPropertyDao;

        @Override
        public ConsultSessionPropertyVo findConsultSessionPropertyByUserId(String userId) {
                ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyDao.findConsultSessionPropertyByUserId(userId);
                if(consultSessionPropertyVo != null){
                        return consultSessionPropertyVo;
                }else{
                        return null;
                }
        }

        @Override
        public int addPermTimes(String userId) {
                return consultSessionPropertyDao.addPermTimes(userId);
        }

        @Override
        public int updateByPrimaryKey(ConsultSessionPropertyVo consultSessionPropertyVo) {
                return consultSessionPropertyDao.updateByPrimaryKey(consultSessionPropertyVo);
        }
}
