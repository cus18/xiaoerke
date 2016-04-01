package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.modules.operation.service.UserActionDetailService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户行为详细记录 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class UserActionDetailServiceImpl implements UserActionDetailService {

    @Autowired
    private MongoDBService<MongoLog> mongoDBServiceLog;

    /**
     * 统计：用户行为统计分析
     *
     * @throws ParseException
     */
    @Override
    public HashMap<String, Object> getUserFullToDoList(HashMap<String, Object> params) throws ParseException {

        List<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> r = new HashMap<String, Object>();
        String openid = params.get("openid") == null ? null : params.get("openid").toString();
        String startDate = params.get("begin_time").toString();
        String endDate = params.get("end_time").toString();
        //用户行为统计分析
        List<MongoLog> list = getUserFullToDoList(startDate, endDate, openid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int a=0;
        for (MongoLog m : list) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("create_date", sdf.format(m.getCreate_date()));
            map.put("request_uri", m.getRequest_uri());
            map.put("title", m.getTitle());
            map.put("index",++a);
            response.add(map);
        }
        r.put("user", response);
        return r;
    }

    //用户详细行为记录
    public List getUserFullToDoList(String startDate,
                                    String endDate, String openid) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        if (openid != null && !openid.equals("")) {
            queryInLog
                    .addCriteria(Criteria.where("open_id").is(openid)
                            .andOperator(
                                    new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                            sdf.parse(endDate))));
            queryInLog.with(new Sort(new Sort.Order(Sort.Direction.ASC, "create_date")));
        } else {
            queryInLog
                    .addCriteria(new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                            sdf.parse(endDate)));
            queryInLog.with(new Sort(new Sort.Order(Sort.Direction.ASC, "create_date")));
        }
        List list = mongoDBServiceLog.queryList(queryInLog);
        return list;
    }
}
