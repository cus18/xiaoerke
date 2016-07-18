package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultCountTotal;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultVoiceRecordMongoVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by jiangzhongge on 2016-6-21.
 * @version 1.0v
 * 语音聊天记录mongoDB存储Service
 */

@Service
@Transactional(readOnly = false)
public class ConsultVoiceRecordMongoServiceImpl extends MongoDBService<ConsultVoiceRecordMongoVo>{

        public int insertConsultCountTotal(ConsultCountTotal consultCountTotal) {
                mongoTemplate.insert(consultCountTotal, "consultCountTotal");
                return 0;
        }

        public long getConsultCountTotal(ConsultCountTotal consultCountTotal){
                Query query = new Query(where("createDate").lte(new Date()));
                long Consultcount = mongoTemplate.count(query,"consultCountTotal");
                return Consultcount;
        }
        public int findAndModify(ConsultCountTotal consultCountTotal) {
                Query query = new Query(Criteria.where("_id").is(consultCountTotal.getId()));
                Update update = new Update().set("csUserId", consultCountTotal.getCsUserId());
                mongoTemplate.updateFirst(query, update, ConsultCountTotal.class);
                return 0;
        }

        public int deleteConsultCountTotal(ConsultCountTotal consultCountTotal) {
                Query query = new Query(Criteria.where("_id").is(consultCountTotal.getId()));
                mongoTemplate.remove(query, "consultCountTotal");
                return 0;
        }

        public long countConsultByVoice(ConsultVoiceRecordMongoVo consultVoiceRecordMongoVo){
                Query query = new Query(where("sessionId").is(consultVoiceRecordMongoVo.getSessionId()).and("csUserId").is(consultVoiceRecordMongoVo.getCsUserId()));
                long Consultcount = mongoTemplate.count(query,"consultVoiceRecordMongoVo");
                return Consultcount;
        }

        @Override
        public int insert(ConsultVoiceRecordMongoVo consultVoiceRecordMongoVo) {
                mongoTemplate.insert(consultVoiceRecordMongoVo, "consultVoiceRecordMongoVo");
                return 0;
        }

        @Override
        public int insertByBatch(List<ConsultVoiceRecordMongoVo> entities) {
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
        public long queryCount(Query query) {return 0;}

        @Override
        public List<ConsultVoiceRecordMongoVo> queryList(Query query) {
                return null;
        }

        @Override
        public List<ConsultVoiceRecordMongoVo> queryListDistinct(Query query, String key) {
                return null;
        }

        @Override
        public ConsultVoiceRecordMongoVo findAndRemove(Query query) {
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
