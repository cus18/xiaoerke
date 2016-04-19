package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMongoUtilsService;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class ConsultMongoUtilsServiceImpl implements ConsultMongoUtilsService {
	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public int insertRichConsultSession(RichConsultSession richConsultSession) {
		mongoTemplate.insert(richConsultSession, "RichConsultSession");
		return 0;
	}
	@Override
	public List<RichConsultSession> queryRichConsultSessionList(Query query){
		 List<RichConsultSession> list = mongoTemplate.find(query, RichConsultSession.class, "RichConsultSession");
		return list;
	}

	@Override
	public WriteResult upsertRichConsultSession(Query query,Update update) {

		return mongoTemplate.upsert(query,update,"RichConsultSession");

	}

	@Override
	public RichConsultSession  removeRichConsultSession(Query query) {
		return this.mongoTemplate.findAndRemove(query, RichConsultSession.class,"RichConsultSession");
	}

}
