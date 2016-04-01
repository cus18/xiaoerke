package com.cxqm.xiaoerke.modules.wechat.service.impl;


import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class AttentionMongoDBServiceImpl extends MongoDBService<WechatAttention>{

	@Override
	public int insert(WechatAttention entity) {
		mongoTemplate.insert(entity, "sysattention");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query,update,WechatAttention.class);

	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query, update, WechatAttention.class);

	}

	@Override
	public WechatAttention findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, WechatAttention.class);
	}



	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public long queryCount(Query query) {
		long count = mongoTemplate.count(query, Log.class, "sysattention");
		return count;
	}

	@Override
	public List<WechatAttention> queryList(Query query){
		return this.mongoTemplate.find(query, WechatAttention.class, "sysattention");
	}

	@Override
	public List<WechatAttention> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("sysattention").distinct(key, query.getQueryObject());
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("sysattention").distinct(key, query.getQueryObject());
	}


	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<Log> results = mongoTemplate.mapReduce("sysattention", map, reduce, Log.class);
		return 0;
	}

	@Override
	public int insertByBatch(List<WechatAttention> entities) {
		mongoTemplate.insert(entities, "sysattention");
		return 0;
	}
	
}
