package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultBadEvaluateRemindUserVo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangzhongge on 2016-6-22.
 *  咨询差评提醒
 */
public interface ConsultBadEvaluateRemindUserService {

    int saveNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    int modifyNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    List<ConsultBadEvaluateRemindUserVo> findConsultRemindUserByMoreConditions(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    int deleteConsultRemindUserByInfo(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    List<String> findConsultRemindUserId();

    /**
     *  jiangzhongge add 2016-11-21 16:45:50
     *  查询是否患者咨询是否进行过评价
     * @param map
     * @return
     */
    List<HashMap<String,Object>> selectConsultStatisticVoByMap(HashMap<String,Object> map);
}
