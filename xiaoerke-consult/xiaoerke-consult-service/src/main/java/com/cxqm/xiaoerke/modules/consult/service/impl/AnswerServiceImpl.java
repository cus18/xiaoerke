package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.entity.AnswerMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.AnswerService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by deliang on 16/03/18.
 */

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private MongoDBService<AnswerMongoVo> answerMongoVoMongoDBService;

    @Override
    public String findConsultAnswer(String type) {
        Query query = new Query();
        if ("myAnswer".equals(type)) {
            query.addCriteria(where("type").is(type).andOperator(new Criteria().where("userId").is(UserUtils.getUser().getId())));
        } else {
            query.addCriteria(where("type").is(type));
        }
        List<AnswerMongoVo> answerMongoVos = answerMongoVoMongoDBService.queryList(query);
        if(answerMongoVos!=null && answerMongoVos.size()>0){
            return  answerMongoVos.get(0).getAnswerContent();
        }else{
            return "noValue";
        }
    }

    @Override
    public WriteResult updateConsultAnswer(String myanswer) throws Exception {
        return null;
    }

    @Override
    public WriteResult upsertConsultAnswer(String answerType,String answer) {
        User user = UserUtils.getUser();
        WriteResult writeResult =null;
        if(answerType.equals("myAnswer")){
            writeResult = answerMongoVoMongoDBService.upsert((new Query(where("userId").is(user.getId()).and("type").is("myAnswer"))),
                    new Update().update("answerContent", answer));
        }else if(answerType.equals("commonAnswer")){
            writeResult = answerMongoVoMongoDBService.upsert((new Query(where("userId").is(user.getId()).and("type").is("commonAnswer"))),
                    new Update().update("answerContent", answer));
        }
        return writeResult;
    }

    @Override
    public void deleteMyConsultAnswer() throws Exception{
        User user = UserUtils.getUser();
        answerMongoVoMongoDBService.findAndRemove(new Query(where("userId").is(user.getId())));
    }

}
