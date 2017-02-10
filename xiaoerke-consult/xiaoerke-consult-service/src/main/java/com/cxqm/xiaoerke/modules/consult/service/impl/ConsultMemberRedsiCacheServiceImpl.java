package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultMemberDao;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionTemplateService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;


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

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private OperationPromotionTemplateService operationPromotionTemplateService;


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
    public List<ConsultMemberVo> getConsultMemberList(ConsultMemberVo vo){
        return consultMemberDao.getConsultMemberList(vo);
    };

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
//        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
//        if(null != sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList()&&sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList().indexOf(openid)==-1){
//            return;
//        }
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
        LogUtils.saveLog("ZXTS_GMCG",openid);
    }

    @Override
    public boolean consultChargingCheck(String openid, String token,boolean prompt){
//        首次咨询赠送四次免费机会
        ConsultSessionPropertyVo consultSessionPropertyVo = consultSessionPropertyService.findConsultSessionPropertyByUserId(openid);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        //首次咨询
        if (consultSessionPropertyVo == null) {
            consultSessionPropertyVo = new ConsultSessionPropertyVo();
            consultSessionPropertyVo.setCreateTime(new Date());
            consultSessionPropertyVo.setMonthTimes(Integer.parseInt(sysPropertyVoWithBLOBsVo.getFreeConsultNum()));
            consultSessionPropertyVo.setPermTimes(0);
            consultSessionPropertyVo.setSysUserId(openid);
            consultSessionPropertyVo.setCreateBy(openid);
            consultSessionPropertyService.insertUserConsultSessionProperty(consultSessionPropertyVo);
        }
        //白名单用户
        if(null != sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList()&&sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList().indexOf(openid)==-1){
            return true;
        }
        //正常逻辑
        Date nowDate = new Date();
        //检测当前用户会员是否过期(没有会员按未过期处理)
        String memberEndTime = getConsultMember(openid+memberRedisCachVo.MEMBER_END_DATE);
        if(null==memberEndTime||DateUtils.StrToDate(memberEndTime,"datetime").getTime()<nowDate.getTime()){
//            判断是否是晚上咨询,若果是则直接让用户购买
            OperationPromotionTemplateVo templatevo = operationPromotionTemplateService.getFreeConsultInfo();
            //推送的消息文案
            if(null !=templatevo&&"收费".equals(templatevo.getInfo1())){
                WechatUtil.sendMsgToWechat(token,openid,templatevo.getInfo2());
                LogUtils.saveLog("ZXTS_YJSD",openid);
                return false;
            };
//      说明是新用户或者是用户的会员已过期,要检测是否是今日 首次咨询以及是否有机会
            String datetime = DateUtils.DateToStr(nowDate,"date");
            String latestConsultTime = getConsultMember(openid+memberRedisCachVo.LATEST_CONSULT_TIME);
            if(null == latestConsultTime ||!datetime.equals(latestConsultTime)){
//         用户是首次咨询
                ConsultSessionPropertyVo propertyVo =consultSessionPropertyService.findConsultSessionPropertyByUserId(openid);
                Query query2 = (new Query()).addCriteria(where("userId").is(openid)).with(new Sort(Sort.Direction.DESC, "createDate"));
                ConsultSessionStatusVo consultSessionStatusVo2 = consultRecordService.findOneConsultSessionStatusVo(query2);
                if(null != propertyVo && (propertyVo.getPermTimes()+propertyVo.getMonthTimes()) > 0){
//             用户有咨询机会
                    String content =templatevo!=null?templatevo.getInfo2():"";
                    if((propertyVo.getPermTimes()+propertyVo.getMonthTimes())==1) content += "\n----------\n别怕！邀请个好友加入宝大夫，免费机会立刻有！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
//                 查询用户最后一条记录是否ongoing的状态
                    if((null == consultSessionStatusVo2)||(null !=consultSessionStatusVo2 && prompt&&!ConsultSession.STATUS_ONGOING.equals(consultSessionStatusVo2.getStatus()))&&StringUtils.isNotNull(content))WechatUtil.sendMsgToWechat(token,openid,content);
                    LogUtils.saveLog("ZXTS_YMFJH",openid);
                    return true;
                }else{
                    //没有机会,推送购买链接
                    String content = "时间真快，您本月的免费咨询机会已用完\n更多咨询机会请\n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/getUserWechatMenId?url=35&from=fdsa'>>>猛戳这里购买吧！</a>";
                    content += "\n----------\n别怕！邀请个好友加入宝大夫，免费机会立刻有！\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
                    if(prompt)WechatUtil.sendMsgToWechat(token,openid,content);
                    LogUtils.saveLog("ZXTS_MYMFJH",openid);
                    return  false;
                }
            }
            //会员时间超时,推送购买链接
            String content = "亲爱的，您本次免费咨询时间已到\n" +"还没问完？ 畅享24小时随时提问，专业医生随时候答\n<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/getUserWechatMenId?url=35'>>>猛戳这里购买吧！</a>\n\n不急的麻麻可以等待\n24h后您的下次"+sysPropertyVoWithBLOBsVo.getFreeConsultMemberTime()+"分钟免费机会哦~";
            content += "\n----------\n没问够、不想掏钱？还可以\n戳戳手指，邀请个好友加入宝大夫，减免机会就来咯~\n" + "<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_RK_1_backend'>>>邀请好友赚机会</a>";
            WechatUtil.sendMsgToWechat(token,openid,content);
            LogUtils.saveLog("ZXTS_SYMFJH",openid);
            return false;
        }
        return true;
    }

    @Override
    public boolean cheackMemberTimeOut(String openid){
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        if(null != sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList()&&sysPropertyVoWithBLOBsVo.getConsultMemberWhiteList().indexOf(openid)==-1){
            return false;
        }
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

    @Override
    public void updateRedisConsultInfo(ConsultMemberVo vo) {
        List<ConsultMemberVo> volist = getConsultMemberList(vo);
            for(ConsultMemberVo v:volist){
                if(null != v.getEndTime())
                saveConsultMember(v.getNickname()+ memberRedisCachVo.MEMBER_END_DATE,DateUtils.DateToStr(v.getEndTime(),"datetime"));
            }
    }

    /**
     *根据后台数据找出不能用免费机会的时段
     * */
    @Override
    public boolean cheackFreeConsultRule(){
//  根据现时段找到相应的收费模式以及消息

        return false;
    }
}
