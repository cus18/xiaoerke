package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.RedPacketInfoVo;
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
public class RedPacketInfoMongoDBServiceImpl extends MongoDBService<RedPacketInfoVo> {

	@Override
	public int insert(RedPacketInfoVo redPacketInfoVo) {
		mongoTemplate.insert(redPacketInfoVo, "RedPacketInfoVo");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {
		return mongoTemplate.updateMulti(query,update,RedPacketInfoVo.class);
	}

	@Override
	public WriteResult upsert(Query query,Update update) {
		return mongoTemplate.upsert(query,update,"RedPacketInfoVo");
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "RedPacketInfoVo");
	}

	public List<RedPacketInfoVo> queryList(Query query){
		return this.mongoTemplate.find(query, RedPacketInfoVo.class, "RedPacketInfoVo");
	}

	@Override
	public List<RedPacketInfoVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("RedPacketInfoVo").distinct(key, query.getQueryObject());
	}

	@Override
	public RedPacketInfoVo findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, RedPacketInfoVo.class,"RedPacketInfoVo");
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("RedPacketInfoVo").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<RedPacketInfoVo> results = mongoTemplate.mapReduce("RedPacketInfoVo", map, reduce, RedPacketInfoVo.class);
		return 0;
	}

	public int insertByBatch(List<RedPacketInfoVo> entities) {
		mongoTemplate.insert(entities, "RedPacketInfoVo");
		return 0;
	}
	
}
