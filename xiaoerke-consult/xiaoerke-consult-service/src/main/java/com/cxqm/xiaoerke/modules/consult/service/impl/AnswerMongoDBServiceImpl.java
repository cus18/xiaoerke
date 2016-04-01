package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.AnswerMongoVo;
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
public class AnswerMongoDBServiceImpl extends MongoDBService<AnswerMongoVo> {

	@Override
	public int insert(AnswerMongoVo answerMongoVo) {
		mongoTemplate.insert(answerMongoVo, "AnswerMongoVo");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query,update,AnswerMongoVo.class);

	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query,update,"AnswerMongoVo");

	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "AnswerMongoVo");
	}

	public List<AnswerMongoVo> queryList(Query query){
		return this.mongoTemplate.find(query, AnswerMongoVo.class, "AnswerMongoVo");
	}

	@Override
	public List<AnswerMongoVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("AnswerMongoVo").distinct(key, query.getQueryObject());
	}

	@Override
	public AnswerMongoVo  findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, AnswerMongoVo.class,"AnswerMongoVo");
	}


	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("AnswerMongoVo").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<AnswerMongoVo> results = mongoTemplate.mapReduce("AnswerMongoVo", map, reduce, AnswerMongoVo.class);
		return 0;
	}

	public int insertByBatch(List<AnswerMongoVo> entities) {
		mongoTemplate.insert(entities, "AnswerMongoVo");
		return 0;
	}
	
}
