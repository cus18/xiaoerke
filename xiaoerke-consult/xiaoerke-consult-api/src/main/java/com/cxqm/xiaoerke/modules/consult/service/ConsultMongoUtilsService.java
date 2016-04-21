package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
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

	List<ConsultRecordMongoVo> queryConsultRankRecordDistinct(String key,Query query);

	List<String> queryConsultRankUserCount(String key,Query query);
}