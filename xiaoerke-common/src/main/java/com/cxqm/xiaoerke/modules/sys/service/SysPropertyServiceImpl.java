package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
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
public class SysPropertyServiceImpl extends MongoDBService<SysPropertyVoWithBLOBsVo> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public int insert(SysPropertyVoWithBLOBsVo entity) {
        mongoTemplate.insert(entity, "SysPropertyVoWithBLOBsVo");
        return 0;
    }

    @Override
    public int insertByBatch(List<SysPropertyVoWithBLOBsVo> entities) {
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
    public List<SysPropertyVoWithBLOBsVo> queryList(Query query) {
        return null;
    }

    @Override
    public List<SysPropertyVoWithBLOBsVo> queryListDistinct(Query query, String key) {
        return this.mongoTemplate.getCollection("SysPropertyVoWithBLOBsVo").distinct(key, query.getQueryObject());
    }

    public SysPropertyVoWithBLOBsVo querySysProperty() {
        return this.mongoTemplate.find(new Query(), SysPropertyVoWithBLOBsVo.class, "SysPropertyVoWithBLOBsVo").get(0);
    }

    @Override
    public SysPropertyVoWithBLOBsVo findAndRemove(Query query) {
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
