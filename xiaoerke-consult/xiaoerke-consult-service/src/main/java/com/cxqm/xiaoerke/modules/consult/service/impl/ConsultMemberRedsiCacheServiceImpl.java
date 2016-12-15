package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultMemberDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultMemberVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.entity.memberRedisCachVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private WechatAttentionService wechatAttentionService;


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
    public void payConsultMember(String openid,String timeLength,String totalFee,String token) {
        //                   mysql 增加会员记录,延长redis的时间
        ConsultMemberVo consultMemberVo = getConsultMemberInfo(openid);
        Integer memberEndTime = Integer.parseInt(timeLength);
        if(null == consultMemberVo){
            consultMemberVo = new ConsultMemberVo();
            consultMemberVo.setEndTime(new Date(new Date().getTime()+memberEndTime*1000*60));
        }else{
            Date nowTime = new Date();
            Date oldMemberTime = consultMemberVo.getEndTime().getTime()>nowTime.getTime()?consultMemberVo.getEndTime():nowTime;
            consultMemberVo.setEndTime(new Date(oldMemberTime.getTime()+memberEndTime*1000*60));
        }
        consultMemberVo.setId(null);
        consultMemberVo.setCreateDate(new Date());
        consultMemberVo.setOpenid(openid);
        consultMemberVo.setMemberType("day");
        consultMemberVo.setPayAcount(totalFee);
        WechatAttention wa = wechatAttentionService.getAttentionByOpenId(openid);
        if(null != wa&&null !=openid&& StringUtils.isNotNull(wa.getNickname())){
            consultMemberVo.setNickname(wa.getNickname());
        }
        saveConsultMemberInfo(consultMemberVo);
        saveConsultMember(openid+ memberRedisCachVo.MEMBER_END_DATE,DateUtils.DateToStr(consultMemberVo.getEndTime(),"datetime"));
        WechatUtil.sendMsgToWechat(token, openid, " 购买成功啦！\n亲爱的，现在可以开始咨询啦，赶紧和医生对话吧~\n会员有效期:"+DateUtils.DateToStr(new Date(),"yyyy年MM月dd日 HH时mm分")+"至"+DateUtils.DateToStr(consultMemberVo.getEndTime(),"yyyy年MM月dd日 HH时mm分"));

    }

    @Override
    public boolean consultChargingCheck(String openid, String token,boolean prompt){
//        String openid = xmlEntity.getFromUserName();
        Date nowDate = new Date();
        //检测当前用户会员是否过期(没有会员按未过期处理)
        String memberEndTime = getConsultMember(openid+memberRedisCachVo.MEMBER_END_DATE);
        if(null == memberEndTime||DateUtils.StrToDate(memberEndTime,"datetime").getTime()<nowDate.getTime()){
            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
//            说明是新用户或者是用户的会员已过期,要检测是否是今日 首次咨询以及是否有机会
            String datetime = DateUtils.DateToStr(nowDate,"date");
            String latestConsultTime = getConsultMember(openid+memberRedisCachVo.LATEST_CONSULT_TIME);
            if(null == latestConsultTime ||!datetime.equals(latestConsultTime)){
//                    用户是首次咨询
                ConsultSessionPropertyVo propertyVo =consultSessionPropertyService.findConsultSessionPropertyByUserId(openid);
                if(null != propertyVo && (propertyVo.getPermTimes()+propertyVo.getMonthTimes()) > 0){
//                        用户有咨询机会
                    String content = "亲爱的，你今天的"+sysPropertyVoWithBLOBsVo.getFreeConsultMemberTime()+"分钟免费咨询还未启用。\n为减少其他生病宝宝的焦急等待，从医生接入时开始计时";
                    if((propertyVo.getPermTimes()+propertyVo.getMonthTimes())==1) content += "\n----------\n别怕！邀请个好友加入宝大夫，免费机会立刻有！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
                    if(prompt)WechatUtil.sendMsgToWechat(token,openid,content);
                    return true;
                }else{
                    //没有机会,推送购买链接
                    String content = "求助客服点击这里欧！\n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/angel/patient/consult#/patientCustomerService'>H5咨询入口</a>";
                    if(prompt)WechatUtil.sendMsgToWechat(token,openid,content);

                    content = "时间真快，您本月的免费咨询机会已用完\n更多咨询机会请\n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/getUserWechatMenId?url=35'>>>猛戳这里购买吧！</a>";
                    if(prompt)WechatUtil.sendMsgToWechat(token,openid,content);
                    return  false;
                }
            }
            //会员时间超时,推送购买链接
            String content = "求求助客服点击这里欧！\n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/angel/patient/consult#/patientCustomerService'>H5咨询入口</a>";
            WechatUtil.sendMsgToWechat(token,openid,content);

            content = "亲爱的，您本次30分钟的免费咨询时间已到，其他宝妈还在焦急排队中……\n24h后您可开启新一天30分钟免费机会。如果您着急，可以 \n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/getUserWechatMenId?url=35'>>>猛戳这里购买吧！</a>";
            WechatUtil.sendMsgToWechat(token,openid,content);

            return false;
        }
        return true;
    }

    @Override
    public boolean cheackMemberTimeOut(String openid){
        String memberEndTime = getConsultMember(openid+memberRedisCachVo.MEMBER_END_DATE);
        if(null == memberEndTime||DateUtils.StrToDate(memberEndTime,"datetime").getTime()<new Date().getTime()) {
            return false;
        }
        return true;
    }

    @Override
    public Page<ConsultMemberVo> findConsultMemberList(ConsultMemberVo vo, Page<ConsultMemberVo> page) {
        return consultMemberDao.findConsultMemberList(vo,page);
    }
}
