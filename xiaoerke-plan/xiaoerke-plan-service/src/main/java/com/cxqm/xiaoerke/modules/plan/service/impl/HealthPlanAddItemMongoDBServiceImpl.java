package com.cxqm.xiaoerke.modules.plan.service.impl;


import com.cxqm.xiaoerke.modules.plan.entity.HealthPlanAddItemVo;
import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class HealthPlanAddItemMongoDBServiceImpl extends MongoDBService<HealthPlanAddItemVo> {

    @Override
    public int insert(HealthPlanAddItemVo entity) {
        mongoTemplate.insert(entity, "HealthPlanAddItemVo");
        return 0;
    }

    @Override
    public int delete(String id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public HealthPlanAddItemVo findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, HealthPlanAddItemVo.class);
    }

    @Override
    public WriteResult upsert(Query query,Update update) {

        return mongoTemplate.upsert(query, update, HealthPlanAddItemVo.class);

    }



    @Override
    public long queryCount(Query query) {
        long count = mongoTemplate.count(query, HealthPlanAddItemVo.class, "HealthPlanAddItemVo");
        return count;
    }

    @Override
    public List<HealthPlanAddItemVo> queryList(Query query) {
        return this.mongoTemplate.find(query, HealthPlanAddItemVo.class, "HealthPlanAddItemVo");
    }

    @Override
    public List<HealthPlanAddItemVo> queryListDistinct(Query query, String key) {
        return this.mongoTemplate.getCollection("HealthPlanAddItemVo").distinct(key, query.getQueryObject());
    }

    @Override
    public List<String> queryStringListDistinct(Query query, String key) {
        return this.mongoTemplate.getCollection("HealthPlanAddItemVo").distinct(key, query.getQueryObject());
    }


    @Override
    public long mapReduce(String map, String reduce) {
        MapReduceResults<Log> results = mongoTemplate.mapReduce("HealthPlanAddItemVo", map, reduce, Log.class);
        return 0;
    }

    @Override
    public int insertByBatch(List<HealthPlanAddItemVo> entities) {
        mongoTemplate.insert(entities, "HealthPlanAddItemVo");
        return 0;
    }

    @Override
    public WriteResult updateMulti(Query query,Update update) {

        return mongoTemplate.updateMulti(query,update,HealthPlanAddItemVo.class);

    }

}
