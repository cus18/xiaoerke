package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultCoopUserInfoDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultCoopUserInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultCoopUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-9-21.
 */
@Service
@Transactional(readOnly = false)
public class ConsultCoopUserInfoServiceImpl implements ConsultCoopUserInfoService{

        @Autowired
        private ConsultCoopUserInfoDao consultCoopUserInfoDao ;

        @Override
        public int saveConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo) {
                int result = consultCoopUserInfoDao.insertConsultCoopUserInfo(consultCoopUserInfo);
                return result;
        }

        @Override
        public int deleteConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo) {
                int result = consultCoopUserInfoDao.deleteConsultCoopUserInfo(consultCoopUserInfo.getId());
                return result;
        }

        @Override
        public int updateConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo) {
                int result = consultCoopUserInfoDao.updateConsultCoopUserInfo(consultCoopUserInfo);
                return result;
        }

        @Override
        public ConsultCoopUserInfoVo findConsultCoopUserInfoById(Integer id) {
                ConsultCoopUserInfoVo consultCoopUserInfoVo = null ;
                if(id !=null ){
                        consultCoopUserInfoVo = consultCoopUserInfoDao.getConsultCoopUserInfoById(id);
                }
                return consultCoopUserInfoVo;
        }

        @Override
        public List<ConsultCoopUserInfoVo> findConsultCoopUserInfoByCondition(ConsultCoopUserInfoVo consultCoopUserInfo) {
                List<ConsultCoopUserInfoVo> result = consultCoopUserInfoDao.getConsultCoopUserInfoByCondition(consultCoopUserInfo);
                return result;
        }
}
