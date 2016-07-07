package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPayUserDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPayUserService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangbaowei on 16/6/27.
 */

@Service
public class ConsultPayUserServiceImpl implements ConsultPayUserService {

    @Autowired
    private ConsultPayUserDao consultPayUserDao;

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private InsuranceRegisterServiceService insuraceregisterservice;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
    private static final String MESSAGE_CONSULTSESSION_KEY = "consult.payMsg";
    private static final String PAYINFO_CONSULTS_KEY = "consult.payUserInfo";


    @Override
    public HashMap<String,Object> getneepPayConsultSession(Integer sessionid) {
        HashMap<String,Object> sessionMap = (HashMap<String,Object>) redisTemplate.opsForHash().get(PAYINFO_CONSULTS_KEY, sessionid);
        return sessionMap;
    }

    @Override
    public void putneepPayConsultSession(Integer sessionid,
                                         HashMap<String,Object> payInfo) {
        if(null !=sessionid||null !=payInfo){
            HashMap<String,Object> payConsult = getneepPayConsultSession(sessionid);

            System.out.println("通过Map.entrySet遍历key和value");
            if(null !=payConsult && payConsult.size()>0){
                for (Map.Entry<String, Object> entry : payConsult.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    Date joinDate = (Date)entry.getValue();
                    if(joinDate.getTime()+1000*60*5>new Date().getTime())
                        payConsult.remove( entry.getKey());
                }
            }

            if(null !=payConsult){
                payInfo.putAll(payConsult);
            }
            redisTemplate.opsForHash().put(PAYINFO_CONSULTS_KEY,
                    sessionid, payInfo);
        }
    }

    @Override
    public boolean angelChargeCheck(String userId) {
        ConsultSession consultSession = new ConsultSession();
        consultSession.setUserId(userId);
        //历史咨询次数三次及以上
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        //判断是否为预防接种系列渠道
        Integer insurace = consultPayUserDao.CheckInsuranceByOpenid(userId);
        //是否已经支付
        PayRecord payRecord = payRecordService.findRecordByOpenid(userId,"consultOnline");
        //判断时间条件
        Date morningStartTime = DateUtils.StrToDate(Global.getConfig("consultMorningStartTime"),"yyyy-MM-dd HH:mm");
        Date consultMorningEndTime = DateUtils.StrToDate(Global.getConfig("consultMorningEndTime"),"yyyy-MM-dd HH:mm");
        Date consultAfternoonStartTime = DateUtils.StrToDate(Global.getConfig("consultAfternoonStartTime"),"yyyy-MM-dd HH:mm");
        Date consultAfternoonEndTime = DateUtils.StrToDate(Global.getConfig("consultAfternoonEndTime"),"yyyy-MM-dd HH:mm");
        Date present = new Date();
        //判断日期条件是否满足要求
        if((morningStartTime.getTime()<present.getTime() &&consultMorningEndTime.getTime()>present.getTime())
                ||(consultAfternoonStartTime.getTime()<present.getTime()&&consultAfternoonEndTime.getTime()>present.getTime()))
        if( (null!=consultSessions&&consultSessions.size()>3)
                ||insurace>0
                ||(null !=payRecord && payRecord.getReceiveDate().getTime()+24*60*60*1000>new Date().getTime())
 )return true;
        return false;
    }

    @Override
    public void saveChargeUser(Integer sessionid, String openid) {
        if(null != openid&& null != sessionid){
            redisTemplate.opsForHash().put(MESSAGE_CONSULTSESSION_KEY,
                    sessionid, openid);
        }
    }

    @Override
    public String getChargeInfo(Integer sessionid) {
        if(null != sessionid){
            String sessionMap = (String) redisTemplate.opsForHash().get(MESSAGE_CONSULTSESSION_KEY, sessionid);
            return sessionMap;
        }
        return null;
    }

    @Override
    public void sendMessageToConsult(String openid,int type) {
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String token = (String) userWechatParam.get("token");
//        token = "Anpvh5m521VA-M5vTkANy0c7TN4VzBKUKHm9CkNkwW7yhMx0s8PkcUHwfSUKL4meLw5yi3SWD2f9jnSM2uq80Yt2njDOjZdfIujhUh5VUvYGtW0hwmE3ROHzDWA8UiuKFPAjABAYIT";
        String st = "";
        switch (type){
            case 1:
                st = "咨询高峰期，医生姐姐忙前忙后帮了好多妈妈和宝宝。请她喝杯茶吧，她们也需要您关心。" +
                        "\n》<a href='"+ConstantUtil.KEEPER_WEB_URL+"keeper/wxPay/patientPay.do?serviceType=doctorConsultPay'>你请喝茶，医生秒答</a>" +
                        "\n------------------------"+
                        "\n》<a href='"+ConstantUtil.ANGEL_WEB_URL+"angel/patient/consult#/customerService'>咨询客服</a>";
                break;
            case 2:
                st = "哎呀，遇到咨询高峰期，加个急诊费，即可让医生秒回。" +
                        "\n》<a href='"+ ConstantUtil.KEEPER_WEB_URL+"keeper/wxPay/patientPay.do?serviceType=doctorConsultPay'>急诊100%秒达</a>" +
                        "\n------------------------"+
                        "\n》<a href='"+ConstantUtil.ANGEL_WEB_URL+"angel/patient/consult#/customerService'>咨询客服</a>";
                break;
            case 3:
                st = "哎呀，遇到咨询高峰期，加个急诊费，即可让医生秒回。" +
                        "\n》<a>急诊100%秒达</a>" +
                        "\n》<a>咨询客服</a>";
                break;
            case 4:
                st = "您需要花点时间排队，请耐心等待哦！";
                break;
            case 5:
                st = "哇哦，这么大方，不赞你一下可惜了。医生正在闪电般赶来为您服务。";
                break;
        };
        WechatUtil.sendMsgToWechat(token,openid,st);
    }

}
