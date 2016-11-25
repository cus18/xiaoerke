package com.cxqm.xiaoerke.modules.interaction.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.interaction.entity.PatientRegisterPraise;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import com.cxqm.xiaoerke.modules.interaction.entity.UserFeedbackVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户反馈dao
 * sunxiao
 * 2016-11-25
 */
@MyBatisDao
public interface UserFeedbackDao {

    int insertSelective(UserFeedbackVo vo);

    Page<UserFeedbackVo> findUserFeedBackList(UserFeedbackVo vo);

}