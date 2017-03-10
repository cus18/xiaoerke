package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.service.AdvisorySharingService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by wangbaowei on 17/2/28.
 */
@Service
public class AdvisorySharingServiceImpl implements AdvisorySharingService{


    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Override
    public Map<String ,Object> conversationRecord(String sessionid,int pageNo,int pageSize) {
        Map<String ,Object> resultMap = new HashMap<String, Object>();
        Query query = new Query(where("sessionid").is(sessionid)).with(new Sort(Sort.Direction.ASC, "createDate"));
        PaginationVo<ConsultRecordMongoVo> pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        List<ConsultRecordMongoVo> recordList  = pagination.getDatas();
        resultMap.put("recordList",recordList);
        return resultMap;
    }

    @Override
    public void sharSeConsult(String sessionid, String content) {
        redisTemplate.opsForValue().set(sessionid,content);
    }
}
