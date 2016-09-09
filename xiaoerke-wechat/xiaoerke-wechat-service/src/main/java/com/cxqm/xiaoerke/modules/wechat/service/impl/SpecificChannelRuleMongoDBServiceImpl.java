package com.cxqm.xiaoerke.modules.wechat.service.impl;


import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.SpecificChannelRuleVo;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class SpecificChannelRuleMongoDBServiceImpl extends MongoDBService<SpecificChannelRuleVo>{

	@Override
	public int insert(SpecificChannelRuleVo entity) {
		mongoTemplate.insert(entity, "SpecificChannelRuleVo");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query,update,SpecificChannelRuleVo.class);

	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query, update, SpecificChannelRuleVo.class);

	}

	@Override
	public SpecificChannelRuleVo findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, SpecificChannelRuleVo.class);
	}



	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public long queryCount(Query query) {
		long count = mongoTemplate.count(query, SpecificChannelRuleVo.class, "SpecificChannelRuleVo");
		return count;
	}

	@Override
	public List<SpecificChannelRuleVo> queryList(Query query){
		return this.mongoTemplate.find(query, SpecificChannelRuleVo.class, "SpecificChannelRuleVo");
	}

	@Override
	public List<SpecificChannelRuleVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("SpecificChannelRuleVo").distinct(key, query.getQueryObject());
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("SpecificChannelRuleVo").distinct(key, query.getQueryObject());
	}


	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<SpecificChannelRuleVo> results = mongoTemplate.mapReduce("SpecificChannelRuleVo", map, reduce, SpecificChannelRuleVo.class);
		return 0;
	}

	@Override
	public int insertByBatch(List<SpecificChannelRuleVo> entities) {
		mongoTemplate.insert(entities, "SpecificChannelRuleVo");
		return 0;
	}
	
}
