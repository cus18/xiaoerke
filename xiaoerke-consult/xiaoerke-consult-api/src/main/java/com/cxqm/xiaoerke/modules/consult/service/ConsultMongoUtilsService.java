package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public interface  ConsultMongoUtilsService {


	int insertRichConsultSession(RichConsultSession richConsultSession);

	List<RichConsultSession> queryRichConsultSessionList(Query query);
}