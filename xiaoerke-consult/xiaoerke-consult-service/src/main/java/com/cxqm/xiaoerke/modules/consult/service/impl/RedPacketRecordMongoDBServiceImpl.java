package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.RedPacketRecordVo;
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
public class RedPacketRecordMongoDBServiceImpl extends MongoDBService<RedPacketRecordVo> {

	@Override
	public int insert(RedPacketRecordVo redPacketRecordVo) {
		mongoTemplate.insert(redPacketRecordVo, "RedPacketRecordVo");
		return 0;
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {
		return mongoTemplate.updateMulti(query,update,RedPacketRecordVo.class);
	}

	@Override
	public WriteResult upsert(Query query,Update update) {
		return mongoTemplate.upsert(query,update,"RedPacketRecordVo");
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "RedPacketRecordVo");
	}

	public List<RedPacketRecordVo> queryList(Query query){
		return this.mongoTemplate.find(query, RedPacketRecordVo.class, "RedPacketRecordVo");
	}

	@Override
	public List<RedPacketRecordVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("RedPacketRecordVo").distinct(key, query.getQueryObject());
	}

	@Override
	public RedPacketRecordVo findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, RedPacketRecordVo.class,"RedPacketRecordVo");
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("RedPacketRecordVo").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<RedPacketRecordVo> results = mongoTemplate.mapReduce("RedPacketRecordVo", map, reduce, RedPacketRecordVo.class);
		return 0;
	}

	public int insertByBatch(List<RedPacketRecordVo> entities) {
		mongoTemplate.insert(entities, "RedPacketRecordVo");
		return 0;
	}
	
}
