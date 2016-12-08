package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by wangbaowei on 16/12/8.
 */

@Service
@Transactional(readOnly = false)
public class ConsultMemberRedsiCacheServiceImpl implements ConsultMemberRedsiCacheService {

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private static final String CONUSLT_BASEIBFO = "consult.baseInfo";


    @Override
    public long saveConsultMember(String... value) {
        return  redisTemplate.opsForSet().add(CONUSLT_BASEIBFO,value) ;
    }

    @Override
    public String getConsultMember(String match) {
        String key = null;
        ScanOptions.ScanOptionsBuilder b =  new ScanOptions.ScanOptionsBuilder();
        b.match(match);
        ScanOptions ops = b.build();
        Cursor<Object> memberInfo =  redisTemplate.opsForSet().scan(CONUSLT_BASEIBFO,ops);
        while (memberInfo.hasNext()) {
            key = (String) memberInfo.next();
        }
        return key;
    }

    @Override
    public boolean cheackConsultMember(String key) {
        return redisTemplate.opsForSet().isMember(CONUSLT_BASEIBFO,key);
    }
}
