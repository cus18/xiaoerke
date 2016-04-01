package com.cxqm.xiaoerke.modules.interaction.service;

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

}
