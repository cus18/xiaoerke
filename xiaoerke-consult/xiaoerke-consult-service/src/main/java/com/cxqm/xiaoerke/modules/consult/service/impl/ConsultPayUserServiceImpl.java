package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPayUserDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionPropertyDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPayUserService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private ConsultSessionPropertyDao consultSessionPropertyDao;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
    private static final String MESSAGE_CONSULTSESSION_KEY = "consult.payMsg";
    private static final String PAYINFO_CONSULTS_KEY = "consult.payUserInfo";


    @Override
    public  ConcurrentHashMap<String,Object> getneepPayConsultSession(String csuserId) {
        ConcurrentHashMap<String,Object> sessionMap = ( ConcurrentHashMap<String,Object>) redisTemplate.opsForHash().get(PAYINFO_CONSULTS_KEY, csuserId);
        return sessionMap;
    }

    @Override
    public ConsultSessionPropertyVo selectUserSessionPropertyByVo(ConsultSessionPropertyVo consultSessionPropertyVo){
        return consultSessionPropertyDao.selectByField(consultSessionPropertyVo);
    }

    @Override
    public void putneepPayConsultSession(String csuserId,
                                         ConcurrentHashMap<String,Object> payInfo) {
        if(null !=csuserId||null !=payInfo){
            ConcurrentHashMap<String,Object> payConsult = getneepPayConsultSession(csuserId);

            System.out.println("通过Map.entrySet遍历key和value");
            if(null !=payConsult && payConsult.size()>0){
                for (Map.Entry<String, Object> entry : payConsult.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    Date joinDate = (Date)entry.getValue();
                    if(joinDate.getTime()+1000*60*5<new Date().getTime())
                        payConsult.remove( entry.getKey());
                }
            }

            if(null !=payConsult){
                payInfo.putAll(payConsult);
            }
            redisTemplate.opsForHash().put(PAYINFO_CONSULTS_KEY,
                    csuserId, payInfo);
        }
    }

    @Override
    public void removePayConsultSession(String openid,String csuserid) {
        ConcurrentHashMap<String,Object> payMap = getneepPayConsultSession(csuserid);
        if(null !=payMap && payMap.size()>0){
            for (Map.Entry<String, Object> entry : payMap.entrySet()) {
                if(openid.equals(entry.getKey()))
                    payMap.remove( entry.getKey());
            }
        }
        putneepPayConsultSession(csuserid,payMap);
    }

    @Override
    public boolean angelChargeCheck(String userId) {
        ConsultSession consultSession = new ConsultSession();
        consultSession.setUserId(userId);
        //历史咨询次数三次及以上
        List<ConsultSession> consultSessions = consultSessionService.selectBySelective(consultSession);
        //判断是否为预防接种系列渠道
        Integer insurance = consultPayUserDao.CheckInsuranceByOpenid(userId);
        //是否已经支付
        PayRecord payRecord = payRecordService.findRecordByOpenid(userId,"doctorConsultPay");
        //判断时间条件
        Integer consultAfternoonStartTime = Integer.parseInt(Global.getConfig("consultAfternoonStartTime"));
        Integer consultAfternoonEndTime = Integer.parseInt(Global.getConfig("consultAfternoonEndTime"));

        Date consultBeginDate = DateUtils.StrToDate(Global.getConfig("consultBeginDate"),"yyyy-MM-dd");
        Date consultEndDate = DateUtils.StrToDate(Global.getConfig("consultEndDate"),"yyyy-MM-dd");

        Date nowDate = new Date();
        Long nowTime = nowDate.getTime();

        Calendar c1=Calendar.getInstance();
        c1.setTime(nowDate);
        int thisHour = c1.get(Calendar.HOUR_OF_DAY);

        //判断日期条件是否满足要求
        if(consultBeginDate.getTime()<nowTime&&consultEndDate.getTime()>nowTime)
        if(consultAfternoonStartTime<=thisHour&&consultAfternoonEndTime>=thisHour)
        if( (null!=consultSessions&&consultSessions.size()>2&&(null ==payRecord || payRecord.getReceiveDate().getTime()+24*60*60*1000<new Date().getTime()))
                &&insurance == 0
                )
            return true;
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
            String sessionMap = (String) redisTemplate.opsForHash().get(MESSAGE_CONSULTSESSION_KEY, sessionid+"");
            return sessionMap;
        }
        return null;
    }

    @Override
    public void sendMessageToConsult(String openid,int type) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String token = (String) userWechatParam.get("token");
        String st = "";
        switch (type){
            case 1:
                st = "咨询高峰期，医生姐姐忙前忙后帮了好多妈妈和宝宝。请她喝杯茶吧，她们也需要您关心。" +
                        "\n》<a href='"+ sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=35'>你请喝茶，医生秒答</a>" +
                        "\n------------------------"+
                        "\n》<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=34'>咨询客服</a>";
                break;
            case 2:
                st = "哎呀，遇到咨询高峰期，加个急诊费，即可让医生秒回。" +
                        "\n》<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=35'>支付急诊费100%秒达</a>" +
                        "\n------------------------"+
                        "\n》<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=34'>咨询客服</a>";
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
        LogUtils.saveLog("consult_chargetest_once_information", openid + ":" + type);
    }

}
