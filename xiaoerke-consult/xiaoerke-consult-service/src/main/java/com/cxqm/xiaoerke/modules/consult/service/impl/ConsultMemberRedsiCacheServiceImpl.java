package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.entity.memberRedisCachVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Created by wangbaowei on 16/12/8.
 */

@Service
@Transactional(readOnly = false)
public class ConsultMemberRedsiCacheServiceImpl implements ConsultMemberRedsiCacheService {

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");


    @Override
    public long saveConsultMember(String key,String value) {
        return  redisTemplate.opsForValue().append(key,value) ;
    }

    @Override
    public String getConsultMember(String match) {
//        String key = null;
//        ScanOptions.ScanOptionsBuilder b =  new ScanOptions.ScanOptionsBuilder();
//        b.match(match);
//        ScanOptions ops = b.build();
//        Cursor<Object> memberInfo =  redisTemplate.opsForValue().(CONUSLT_BASEIBFO,ops);
//        while (memberInfo.hasNext()) {
//            key = (String) memberInfo.next();
//        }
        redisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
        return (String) redisTemplate.opsForValue().get(match);
    }

    @Override
    public boolean cheackConsultMember(String key) {
        return redisTemplate.opsForValue().get(key)==null? false:true;
    }

    @Override
    public boolean useFreeChance(String openid,String timeLength) {
        //检测该用户是否当天首次咨询,如果是则增加会员时间 并记录
        String latestConsultTime = getConsultMember(openid+ memberRedisCachVo.LATEST_CONSULT_TIME);
        Date nowDate = new Date();
        String datetime = DateUtils.DateToStr(nowDate,"date");
        Date afterDate = new Date(nowDate.getTime() + Integer.parseInt(timeLength)*1000*60);
        if(null == latestConsultTime ||!datetime.equals(latestConsultTime)){
            saveConsultMember(openid+ memberRedisCachVo.LATEST_CONSULT_TIME,datetime);
            saveConsultMember(openid+ memberRedisCachVo.MEMBER_END_DATE,DateUtils.DateToStr(afterDate,"datetime"));
        }
        return  false;
    }
}
