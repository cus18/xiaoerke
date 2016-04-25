package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public interface  ConsultMongoUtilsService {

	int insertRichConsultSession(RichConsultSession richConsultSession);

	List<RichConsultSession> queryRichConsultSessionList(Query query);

	WriteResult upsertRichConsultSession(Query query, Update update);

	RichConsultSession  removeRichConsultSession(Query query);

	WriteResult removeConsultRankRecord(Query query);

	AggregationOutput queryConsultRankRecordGroup(DBObject group);

	List<String> queryConsultRankUserCount(String key,Query query);
}