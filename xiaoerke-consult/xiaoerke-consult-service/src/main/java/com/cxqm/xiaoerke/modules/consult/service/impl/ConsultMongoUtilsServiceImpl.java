package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMongoUtilsService;
import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class ConsultMongoUtilsServiceImpl implements ConsultMongoUtilsService {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public int insertRichConsultSession(RichConsultSession richConsultSession) {
        mongoTemplate.insert(richConsultSession, "richConsultSession");
        return 0;
    }

    @Override
    public List<RichConsultSession> queryRichConsultSessionList(Query query) {
        List<RichConsultSession> list = mongoTemplate.find(query, RichConsultSession.class, "richConsultSession");
        return list;
    }

    @Override
    public WriteResult upsertRichConsultSession(Query query, Update update) {
        return mongoTemplate.upsert(query, update, "richConsultSession");

    }

    @Override
    public RichConsultSession removeRichConsultSession(Query query) {
        return this.mongoTemplate.findAndRemove(query, RichConsultSession.class, "richConsultSession");
    }

    @Override
    public WriteResult removeConsultRankRecord(Query query) {
        return mongoTemplate.remove(query, "consultRankRecord");
    }

    @Override
    public AggregationOutput queryConsultRankRecordGroup(DBObject group){
        AggregationOutput output = mongoTemplate.getCollection("consultRankRecord").aggregate(group);
        return output;
    }

    @Override
    public List<String> queryConsultRankUserCount(String key,Query query){
        return this.mongoTemplate.getCollection("consultRankRecord").distinct(key, query.getQueryObject());
    }


}
