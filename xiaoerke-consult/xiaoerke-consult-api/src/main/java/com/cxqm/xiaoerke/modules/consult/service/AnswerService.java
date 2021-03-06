package com.cxqm.xiaoerke.modules.consult.service;

import com.mongodb.WriteResult;


/**
 * Created by deliang on 16/3/18.
 */
public interface AnswerService {

    String findConsultAnswer(String type);

    WriteResult updateConsultAnswer(String myanswer) throws Exception;

    WriteResult upsertConsultAnswer(String answerType,String answer);

    void deleteMyConsultAnswer() throws Exception;


}
