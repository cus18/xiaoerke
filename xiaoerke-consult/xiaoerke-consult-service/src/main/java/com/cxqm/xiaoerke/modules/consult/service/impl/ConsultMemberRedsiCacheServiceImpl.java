package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultMemberDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultMemberVo;
import com.cxqm.xiaoerke.modules.consult.entity.memberRedisCachVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConsultMemberDao consultMemberDao;


    @Override
    public void saveConsultMemberInfo(ConsultMemberVo vo) {
        consultMemberDao.insertSelective(vo);
    }

    @Override
    public void updateConsultMemberInfo(ConsultMemberVo vo) {
        consultMemberDao.updateByPrimaryKeySelective(vo);
    }

    @Override
    public ConsultMemberVo getConsultMemberInfo(String openid) {
        return consultMemberDao.selectByopenid(openid);
    }

    @Override
    public void saveConsultMember(String key,String value) {
          redisTemplate.opsForValue().set(key,value) ;
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

    @Override
    public void payConsultMember(String openid,String timeLength,String totalFee) {
        //                   mysql 增加会员记录,延长redis的时间
        ConsultMemberVo consultMemberVo = getConsultMemberInfo(openid);
        Integer memberEndTime = Integer.parseInt(timeLength);
        if(null ==consultMemberVo){
            consultMemberVo = new ConsultMemberVo();
            consultMemberVo.setOpenid(openid);
            consultMemberVo.setMemberType("day");
            consultMemberVo.setNickname("");
            consultMemberVo.setPayAcount(totalFee);
            consultMemberVo.setEndTime(new Date(new Date().getTime()+memberEndTime*1000*60));
            saveConsultMemberInfo(consultMemberVo);
        }else{
            consultMemberVo.setEndTime(new Date(consultMemberVo.getEndTime().getTime()+memberEndTime*1000*60));
            updateConsultMemberInfo(consultMemberVo);
        }

        saveConsultMember(openid+ memberRedisCachVo.MEMBER_END_DATE,DateUtils.DateToStr(consultMemberVo.getEndTime(),"datetime"));

    }
}
