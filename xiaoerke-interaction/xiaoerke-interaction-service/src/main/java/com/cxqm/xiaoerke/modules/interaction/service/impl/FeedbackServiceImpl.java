package com.cxqm.xiaoerke.modules.interaction.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.interaction.dao.PatientRegisterPraiseDao;
import com.cxqm.xiaoerke.modules.interaction.dao.UserFeedbackDao;
import com.cxqm.xiaoerke.modules.interaction.entity.UserFeedbackVo;
import com.cxqm.xiaoerke.modules.interaction.service.FeedbackService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class FeedbackServiceImpl implements FeedbackService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageService messageService;

    @Autowired
    private PatientRegisterPraiseDao patientRegisterPraiseDao;

    @Autowired
    UserFeedbackDao userFeedbackDao;

    @Override
    public String questionnaireSurvey(Map<String, Object> params, String openId)
    {
        List<HashMap<String, Object>> li = (List) params.get("answer");
        List<HashMap<String, Object>> request = new ArrayList<HashMap<String, Object>>();
        for (HashMap<String, Object> map : li) {
            map.put("openid", openId);
            map.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
            request.add(map);
        }
        try {
        	patientRegisterPraiseDao.saveQuestionnaireSurvey(request);
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    //意见反馈
    @Override
    public Boolean sendAdvice(Map<String, Object> hashMap) {
        hashMap.put("id", IdGen.uuid());
        messageService.saveAdvice(hashMap);
        return false;
    }


    //意见反馈
    @Override
    public Boolean saveFeedBack(UserFeedbackVo vo) {
        userFeedbackDao.insertSelective(vo);
        return userFeedbackDao.insertSelective(vo)>-1;
    }

    @Override
    public Page<UserFeedbackVo> findUserFeedbackList(Page<UserFeedbackVo> page, UserFeedbackVo vo) {
        return userFeedbackDao.findUserFeedBackList(page,vo);
    }

    @Override
    public void changeSolve(UserFeedbackVo vo) {
        userFeedbackDao.updateUserFeedbackInfo(vo);
    }
}
