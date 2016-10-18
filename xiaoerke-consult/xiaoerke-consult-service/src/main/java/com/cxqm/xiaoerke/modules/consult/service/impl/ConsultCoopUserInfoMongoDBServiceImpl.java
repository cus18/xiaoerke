package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultCoopUserInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-9-21.
 */
@Service
@Transactional(readOnly = false)
public class ConsultCoopUserInfoMongoDBServiceImpl extends MongoDBService<ConsultCoopUserInfoVo>{

        public long queryCountByCollectionName(Query query,String collectionName) {
                return this.mongoTemplate.count(query,collectionName) ;
        }

        @Override
        public int insert(ConsultCoopUserInfoVo entity) {
                this.mongoTemplate.insert(entity,"consultCoopUserInfoVo");
                return 0;
        }

        @Override
        public int insertByBatch(List<ConsultCoopUserInfoVo> entities) {
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
        public List<ConsultCoopUserInfoVo> queryList(Query query) {
                return null;
        }

        @Override
        public List<ConsultCoopUserInfoVo> queryListDistinct(Query query, String key) {
                return null;
        }

        @Override
        public ConsultCoopUserInfoVo findAndRemove(Query query) {
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
