package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultBadEvaluateRemindUserVo;

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
}
