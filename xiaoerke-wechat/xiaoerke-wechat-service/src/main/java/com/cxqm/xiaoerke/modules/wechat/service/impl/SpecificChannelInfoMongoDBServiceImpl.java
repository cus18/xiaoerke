package com.cxqm.xiaoerke.modules.wechat.service.impl;


import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.SpecificChannelInfoVo;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class SpecificChannelInfoMongoDBServiceImpl extends MongoDBService<SpecificChannelInfoVo>{

	@Override
	public int insert(SpecificChannelInfoVo entity) {
		mongoTemplate.insert(entity, "SpecificChannelInfoVo");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query,update,SpecificChannelInfoVo.class);

	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query, update, SpecificChannelInfoVo.class);

	}

	@Override
	public SpecificChannelInfoVo findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, SpecificChannelInfoVo.class);
	}



	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public long queryCount(Query query) {
		long count = mongoTemplate.count(query, SpecificChannelInfoVo.class, "SpecificChannelInfoVo");
		return count;
	}

	@Override
	public List<SpecificChannelInfoVo> queryList(Query query){
		return this.mongoTemplate.find(query, SpecificChannelInfoVo.class, "SpecificChannelInfoVo");
	}

	@Override
	public List<SpecificChannelInfoVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("SpecificChannelInfoVo").distinct(key, query.getQueryObject());
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("SpecificChannelInfoVo").distinct(key, query.getQueryObject());
	}


	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<SpecificChannelInfoVo> results = mongoTemplate.mapReduce("SpecificChannelInfoVo", map, reduce, SpecificChannelInfoVo.class);
		return 0;
	}

	@Override
	public int insertByBatch(List<SpecificChannelInfoVo> entities) {
		mongoTemplate.insert(entities, "SpecificChannelInfoVo");
		return 0;
	}
	
}
