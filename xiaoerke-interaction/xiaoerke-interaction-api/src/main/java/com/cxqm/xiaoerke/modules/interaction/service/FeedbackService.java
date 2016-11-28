package com.cxqm.xiaoerke.modules.interaction.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.interaction.entity.UserFeedbackVo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 意见反馈服务
 * @author frank
 *
 */
public interface FeedbackService {
	
    Boolean sendAdvice(Map<String, Object> hashMap);

	String questionnaireSurvey(Map<String, Object> params, String openId);

    Page<UserFeedbackVo> findUserFeedbackList(Page<UserFeedbackVo> page,UserFeedbackVo vo);

    void changeSolve(UserFeedbackVo vo);

}
