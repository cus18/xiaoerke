/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.interaction.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "interaction")
public class QuestionnaireController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/user/questionnaireSurvey", method = {RequestMethod.GET, RequestMethod.POST})
    public String questionnaireSurvey(@RequestBody Map<String, Object> params, HttpSession session,
                                      HttpServletRequest request) {
        String openId = WechatUtil.getOpenId(session,request);
        return feedbackService.questionnaireSurvey(params,openId);
    }

}
