package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultBadEvaluateRemindUserDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultStatisticDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultBadEvaluateRemindUserVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultBadEvaluateRemindUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangzhongge on 2016-6-22.
 * 咨询差评提醒
 */
@Service
@Transactional(readOnly = false)
public class ConsultBadEvaluateRemindUserServiceImpl implements ConsultBadEvaluateRemindUserService{

        @Autowired
        ConsultBadEvaluateRemindUserDao consultBadEvaluateRemindUserDao;

        @Autowired
        private ConsultStatisticDao consultStatisticDao ;

        @Override
        public List<String> findConsultRemindUserId() {
                List<String> result = consultBadEvaluateRemindUserDao.getCsUserOpenIds();
                if(result != null && result.size() >0){
                        return result;
                }else{
                        return null;
                }
        }

        /**
         *  jiangzhongge add 2016-11-21 16:45:50
         *  查询患者上次咨询是否进行评价
         * @param map
         * @return
         */
        @Override
        public List<HashMap<String, Object>> selectConsultStatisticVoByMap(HashMap<String, Object> map) {
                return consultStatisticDao.selectConsultStatisticVoByMap(map);
        }

        @Override
        public int saveNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo) {
                int result =0 ;
                if(consultBadEvaluateRemindUserVo != null){
                        result = consultBadEvaluateRemindUserDao.addNoticeCSUser(consultBadEvaluateRemindUserVo);
                }
                return result;
        }

        @Override
        public int modifyNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo) {
                int result = 0;
                if(consultBadEvaluateRemindUserVo != null){
                        result = consultBadEvaluateRemindUserDao.updateNoticeCSUser(consultBadEvaluateRemindUserVo);
                }
                return result;
        }

        @Override
        public List<ConsultBadEvaluateRemindUserVo> findConsultRemindUserByMoreConditions(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo) {
                List<ConsultBadEvaluateRemindUserVo> result= null ;
                if(consultBadEvaluateRemindUserVo != null){
                        result  = consultBadEvaluateRemindUserDao.findConsultRemindUser(consultBadEvaluateRemindUserVo);
                }
                return result;
        }

        @Override
        public int deleteConsultRemindUserByInfo(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo) {
                int result = 0;
                if(consultBadEvaluateRemindUserVo != null ){
                        result = consultBadEvaluateRemindUserDao.deleteConsultRemindUser(consultBadEvaluateRemindUserVo);
                }
                return result;
        }
}
