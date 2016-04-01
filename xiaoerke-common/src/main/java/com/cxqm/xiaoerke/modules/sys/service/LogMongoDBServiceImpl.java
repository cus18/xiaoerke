package com.cxqm.xiaoerke.modules.sys.service;


import java.util.List;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;

@Service
@Transactional(readOnly = false)
public class LogMongoDBServiceImpl extends MongoDBService<MongoLog>{

	@Override
	public int insert(MongoLog entity) {
		mongoTemplate.insert(entity, "syslog");
		return 0;
	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query,update,MongoLog.class);

	}


	@Override
	public MongoLog findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, MongoLog.class);
	}


	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query, update, MongoLog.class);

	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public long queryCount(Query query) {
		long count = mongoTemplate.count(query, MongoLog.class, "syslog");
		return count;
	}

	@Override
	public List<MongoLog> queryList(Query query){
		return this.mongoTemplate.find(query, MongoLog.class, "syslog");
	}


	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("syslog").distinct(key, query.getQueryObject());
	}
	@Override
	public List<MongoLog> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("syslog").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<MongoLog> results = mongoTemplate.mapReduce("syslog", map, reduce, MongoLog.class);
		return 0;
	}

	@Override
	public int insertByBatch(List<MongoLog> entities) {
		mongoTemplate.insert(entities, "syslog");
		return 0;
	}

	public static MongoLog buildMongoLog(Log log) {
		MongoLog mongoLog = new MongoLog();
		mongoLog.setId(log.getId());
		mongoLog.setCreate_by(log.getCreateBy() == null ? null : log.getCreateBy().getId());
		mongoLog.setCreate_date(log.getCreateDate());
		mongoLog.setException(log.getException());
		mongoLog.setOpen_id(log.getOpenId());
		mongoLog.setMethod(log.getMethod());
		mongoLog.setParams(log.getParams());
		mongoLog.setRemote_addr(log.getRemoteAddr());
		mongoLog.setRequest_uri(log.getRequestUri());
		mongoLog.setTitle(log.getTitle());
		mongoLog.setType(log.getType());
		mongoLog.setUser_agent(log.getUserAgent());
		mongoLog.setMarketer(log.getMarketer());
		mongoLog.setStatus(log.getStatus());
		mongoLog.setNickname(log.getNickname());
		mongoLog.setIsPay(log.getIsPay());
		mongoLog.setParameters(log.getParameters());
		mongoLog.setUserId(log.getUserId());
		return mongoLog;
	}
	
}
