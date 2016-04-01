package com.cxqm.xiaoerke.modules.wechat.service.impl;


import com.cxqm.xiaoerke.common.bean.WechatRecord;
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
public class WechatRecordMongoDBServiceImpl extends MongoDBService<WechatRecord>{

	@Override
	public int insert(WechatRecord entity) {
		mongoTemplate.insert(entity, "syswechatrecord");
		return 0;
	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query,update,WechatRecord.class);

	}


	@Override
	public WechatRecord findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, WechatRecord.class);
	}


	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query,update,WechatRecord.class);

	}


	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "syswechatrecord");
	}

	public List<WechatRecord> queryList(Query query){
		return this.mongoTemplate.find(query, WechatRecord.class, "syswechatrecord");
	}

	@Override
	public List<WechatRecord> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("syswechatrecord").distinct(key, query.getQueryObject());
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("syswechatrecord").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<Log> results = mongoTemplate.mapReduce("syswechatrecord", map, reduce, Log.class);
		return 0;
	}

	public int insertByBatch(List<WechatRecord> entities) {
		mongoTemplate.insert(entities, "syswechatrecord");
		return 0;
	}
	
}
