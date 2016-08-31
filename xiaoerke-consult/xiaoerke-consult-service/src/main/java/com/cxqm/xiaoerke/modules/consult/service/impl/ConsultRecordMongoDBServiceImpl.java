package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultCountTotal;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
		mongoTemplate.insert(consultRecordMongoVo, "consultRecordTemporary");
		return 0;
	}

	public int insertConsultRankRecord(ConsultRecordMongoVo consultRecordMongoVo) {
		mongoTemplate.insert(consultRecordMongoVo, "consultRankRecord");
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
	public PaginationVo<ConsultRecordMongoVo> getRecordDetailInfo(int pageNo, int pageSize, Query query,String recordType) {
		long totalCount = this.mongoTemplate.count(query, ConsultRecordVo.class);
		PaginationVo<ConsultRecordMongoVo> page = new PaginationVo<ConsultRecordMongoVo>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<ConsultRecordMongoVo> datas = null;
		if(recordType.equals("permanent")){
			datas = this.queryList(query);
		}else if(recordType.equals("temporary")){
			datas = this.queryTempRecordList(query);
		}
		page.setDatas(datas);
		return page;
	}

	public PaginationVo<ConsultSessionStatusVo> getUserMessageList(int pageNo, int pageSize, Query query) {
		long totalCount = this.mongoTemplate.count(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
		PaginationVo<ConsultSessionStatusVo> page = new PaginationVo<ConsultSessionStatusVo>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<ConsultSessionStatusVo> datas = null;
		datas = queryUserMessageList(query);
		page.setDatas(datas);
		return page;
	}

	public List<ConsultSessionStatusVo> queryUserMessageList(Query query){
		return mongoTemplate.find(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
	}

	public ConsultRecordMongoVo findOneConsult(Query query) {
		ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
		consultRecordMongoVo = mongoTemplate.findOne(query,ConsultRecordMongoVo.class,"consultRecordVo");
		return consultRecordMongoVo;
	}

	public ConsultRecordMongoVo findOneConsultRecordTemporary(Query query) {
		ConsultRecordMongoVo consultRecordMongoVo = mongoTemplate.findOne(query,ConsultRecordMongoVo.class,"consultRecordTemporary");
		return consultRecordMongoVo;
	}

	public int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo) {
		insertConsultRankRecord(consultRecordMongoVo);//今日排名
		return this.insert(consultRecordMongoVo);//全部聊天记录
	}

	public void  updateConsultSessionStatusVo(Query query,String status) {
		mongoTemplate.updateMulti(query, new Update().set("status", status), ConsultSessionStatusVo.class);
	}

	public void  deleteConsultRecordTemporary(Query query) {
		mongoTemplate.remove(query, "consultRecordTemporary");
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

	public WriteResult upsert(Query query,Update update) {
		return mongoTemplate.upsert(query, update, ConsultRecordMongoVo.class);
	}


	public ConsultSessionStatusVo findOneConsultSessionStatusVo(Query query){
		return mongoTemplate.findOne(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
	}

	public WriteResult upsertConsultSessionStatusVo(ConsultSessionStatusVo consultSessionStatusVo) {
		Query query = new Query(where("sessionId").is(consultSessionStatusVo.getSessionId()));
		WriteResult writeResult = null;
		ConsultSessionStatusVo  StatusVo = this.findOneConsultSessionStatusVo(query);
		if(StatusVo != null){
			String payStauts = consultSessionStatusVo.getPayStatus();
			if(null != payStauts && ConstantUtil.PAY_SUCCESS.equals(payStauts)){
				writeResult = mongoTemplate.updateMulti(query,new Update().set("lastMessageTime", new Date()).set("payStatus",consultSessionStatusVo.getPayStatus()), ConsultSessionStatusVo.class);
			}else{
				writeResult = mongoTemplate.updateMulti(query,new Update().set("lastMessageTime", new Date()), ConsultSessionStatusVo.class);
			}

		}else {
			mongoTemplate.insert(consultSessionStatusVo, "consultSessionStatusVo");
		}
        return writeResult;
	}

	public WriteResult modifyConsultSessionStatusVo(ConsultSessionStatusVo consultSessionStatusVo) {
		Query query = new Query(where("sessionId").is(consultSessionStatusVo.getSessionId()));
		WriteResult writeResult = null;
		ConsultSessionStatusVo  StatusVo = this.findOneConsultSessionStatusVo(query);
		if(StatusVo != null){
			String csUserId = StatusVo.getCsUserId();
			if(csUserId.indexOf(consultSessionStatusVo.getCsUserId()) == -1){
				csUserId = csUserId + " " + consultSessionStatusVo.getCsUserId();
			}
			writeResult = mongoTemplate.updateMulti(query,new Update().set("csUserId", csUserId), ConsultSessionStatusVo.class);
		}
		return writeResult;
	}

	//zdl
	public List<ConsultSessionStatusVo> querySessionStatusList(Query query){
		return this.mongoTemplate.find(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
	}

	@Override
	public long queryCount(Query query)
	{
		return this.mongoTemplate.count(query, "consultRecordVo");
	}

	public List<ConsultRecordMongoVo> queryList(Query query){
		return this.mongoTemplate.find(query, ConsultRecordMongoVo.class, "consultRecordVo");
	}

	public List<ConsultRecordMongoVo> queryconsultRecordVoList(Query query){
		return this.mongoTemplate.find(query, ConsultRecordMongoVo.class, "consultSessionStatusVo");
	}

	public List<ConsultSessionStatusVo> getConsultSessionStatusList(Query query){
		return this.mongoTemplate.find(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
	}

	public List<ConsultRecordMongoVo> queryTempRecordList(Query query){
		return this.mongoTemplate.find(query, ConsultRecordMongoVo.class, "consultRecordTemporary");
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

	public WriteResult removeConsultRankRecord(Query query) {
		return mongoTemplate.remove(query, "consultRankRecord");
	}

	//jiangzg add 2016-8-11 18:37:50 修改集合中字段
	public int updateConsultSessionFirstTransferDate(Query query,Update update ,Class t){
		mongoTemplate.updateMulti(query, update,t);
		return 0 ;
	}

	long consultCount(Query query){
		long totalCount = this.mongoTemplate.count(query, ConsultSessionStatusVo.class, "consultSessionStatusVo");
		return totalCount;
	}
}
