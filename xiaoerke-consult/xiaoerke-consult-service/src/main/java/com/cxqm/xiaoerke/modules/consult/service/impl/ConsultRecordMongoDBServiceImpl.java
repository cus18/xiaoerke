package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Transactional(readOnly = false)
public class ConsultRecordMongoDBServiceImpl extends MongoDBService<ConsultRecordMongoVo> {

	@Override
	public int insert(ConsultRecordMongoVo consultRecordMongoVo) {
		mongoTemplate.insert(consultRecordMongoVo, "consultRecordVo");
		return 0;
	}

	public int insertTempRecord(ConsultRecordMongoVo consultRecordMongoVo) {
		mongoTemplate.insert(consultRecordMongoVo, "consultTempRecordVo");
		return 0;
	}

	/**
	 * 通过条件查询,查询分页结果
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	public PaginationVo<ConsultRecordMongoVo> getPage(int pageNo, int pageSize, Query query,String recordType) {
		long totalCount = this.mongoTemplate.count(query, ConsultRecordVo.class);
		PaginationVo<ConsultRecordMongoVo> page = new PaginationVo<ConsultRecordMongoVo>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<ConsultRecordMongoVo> datas = null;
		if(recordType.equals("permanent")){
			datas = this.queryList(query);
		}else{
			datas = this.queryTempRecordList(query);
		}
		page.setDatas(datas);
		return page;
	}

	public ConsultRecordMongoVo findOneConsult(Query query) {
		ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
		consultRecordMongoVo = mongoTemplate.findOne(query,ConsultRecordMongoVo.class,"consultRecordVo");
		return consultRecordMongoVo;
	}


	public int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo) {
		insertTempRecord(consultRecordMongoVo);
		return this.insert(consultRecordMongoVo);
	}

	public void  deleteConsultSessionStatusVo(Query query) {
		mongoTemplate.remove(query, ConsultSessionStatusVo.class);
	}

	public void  deleteConsultTempRecordVo(Query query) {
		mongoTemplate.remove(query, ConsultSessionStatusVo.class,"consultTempRecordVo");
	}



	@Override
	public ConsultRecordMongoVo findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, ConsultRecordMongoVo.class);
	}

	@Override
	public WriteResult updateMulti(Query query,Update update) {

		return mongoTemplate.updateMulti(query, update, ConsultRecordMongoVo.class);

	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WriteResult upsert(Query query,Update update) {

		return mongoTemplate.upsert(query, update, ConsultRecordMongoVo.class);

	}

	public WriteResult upsertConsultSessionStatusVo(ConsultSessionStatusVo consultSessionStatusVo) {

		return mongoTemplate.upsert((new Query(where("sessionId").is(consultSessionStatusVo.getSessionId()))),
				new Update().update("ConsultSessionStatusVo", consultSessionStatusVo),ConsultSessionStatusVo.class);
	}
	//zdl
	public List<Object> querySessionStatusList(Query query){
		return this.mongoTemplate.find(query, Object.class, "consultSessionStatusVo");
	}


	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "consultRecordVo");
	}

	public List<ConsultRecordMongoVo> queryList(Query query){
		return this.mongoTemplate.find(query, ConsultRecordMongoVo.class, "consultRecordVo");
	}

	public List<ConsultRecordMongoVo> queryTempRecordList(Query query){
		return this.mongoTemplate.find(query, ConsultRecordMongoVo.class, "consultTempRecordVo");
	}

	@Override
	public List<ConsultRecordMongoVo> queryListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("consultRecordVo").distinct(key, query.getQueryObject());
	}

	@Override
	public List<String> queryStringListDistinct(Query query,String key){
		return this.mongoTemplate.getCollection("consultRecordVo").distinct(key, query.getQueryObject());
	}

	@Override
	public long mapReduce(String map, String reduce) {
		MapReduceResults<ConsultRecordMongoVo> results = mongoTemplate.mapReduce("consultRecordVo", map, reduce, ConsultRecordMongoVo.class);
		return 0;
	}

	public int insertByBatch(List<ConsultRecordMongoVo> entities) {
		mongoTemplate.insert(entities, "consultRecordVo");
		return 0;
	}
	
}
