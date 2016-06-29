package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaMongoDBVo;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sunxiao on 16/5/20.
 */

@Service
@Transactional(readOnly = false)
public class UmbrellaMongoDBServiceImpl extends MongoDBService<UmbrellaMongoDBVo> {

    @Override
    public int insert(UmbrellaMongoDBVo entity) {
        mongoTemplate.insert(entity, "UmbrellaMongoDBVo");
        return 0;
    }

    @Override
    public int insertByBatch(List<UmbrellaMongoDBVo> entities) {
        return 0;
    }

    @Override
    public WriteResult updateMulti(Query query, Update update) {
        return null;
    }

    @Override
    public WriteResult upsert(Query query, Update update) {
        return null;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public long queryCount(Query query) {
        return 0;
    }

    @Override
    public List<UmbrellaMongoDBVo> queryList(Query query) {
        return this.mongoTemplate.find(query, UmbrellaMongoDBVo.class, "UmbrellaMongoDBVo");
    }

    @Override
    public List<UmbrellaMongoDBVo> queryListDistinct(Query query, String key) {
        return null;
    }

    @Override
    public UmbrellaMongoDBVo findAndRemove(Query query) {
        return null;
    }

    @Override
    public List<String> queryStringListDistinct(Query query, String key) {
        return null;
    }

    @Override
    public long mapReduce(String map, String reduce) {
        return 0;
    }
}
