package com.cxqm.xiaoerke.modules.wechat.service.impl;


import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.HealthRecordMsgVo;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class HealthRecordMsgMongoDBServiceImpl extends MongoDBService<HealthRecordMsgVo> {

    @Override
    public int insert(HealthRecordMsgVo entity) {
        mongoTemplate.insert(entity, "healthRecordMsg");
        return 0;
    }

    @Override
    public int delete(String id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public WriteResult upsert(Query query,Update update) {

        return mongoTemplate.upsert(query, update, HealthRecordMsgVo.class);

    }

    @Override
    public HealthRecordMsgVo findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, HealthRecordMsgVo.class);
    }



    @Override
    public long queryCount(Query query) {
        long count = mongoTemplate.count(query, HealthRecordMsgVo.class, "healthRecordMsg");
        return count;
    }

    @Override
    public List<HealthRecordMsgVo> queryList(Query query) {
        return this.mongoTemplate.find(query, HealthRecordMsgVo.class, "healthRecordMsg");
    }

    @Override
    public List<HealthRecordMsgVo> queryListDistinct(Query query, String key) {
        return this.mongoTemplate.getCollection("healthRecordMsg").distinct(key, query.getQueryObject());
    }

    @Override
    public List<String> queryStringListDistinct(Query query, String key) {
        return this.mongoTemplate.getCollection("healthRecordMsg").distinct(key, query.getQueryObject());
    }


    @Override
    public long mapReduce(String map, String reduce) {
        MapReduceResults<Log> results = mongoTemplate.mapReduce("healthRecordMsg", map, reduce, Log.class);
        return 0;
    }

    @Override
    public int insertByBatch(List<HealthRecordMsgVo> entities) {
        mongoTemplate.insert(entities, "healthRecordMsg");
        return 0;
    }

    @Override
    public WriteResult updateMulti(Query query,Update update) {

        return mongoTemplate.updateMulti(query,update,HealthRecordMsgVo.class);

    }

}
