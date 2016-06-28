package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultBadEvaluateRemindUserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-6-22.
 * 咨询差评
 */
@MyBatisDao
@Repository
public interface ConsultBadEvaluateRemindUserDao {

    int addNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    int updateNoticeCSUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    List<ConsultBadEvaluateRemindUserVo> findConsultRemindUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    int deleteConsultRemindUser(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo);

    List<String> getCsUserOpenIds();
}
