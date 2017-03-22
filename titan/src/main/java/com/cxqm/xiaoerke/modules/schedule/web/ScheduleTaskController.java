package com.cxqm.xiaoerke.modules.schedule.web;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardDataVo;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRecordsVo;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardDataService;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRecordsService;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardsService;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageActivityInfoService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * titan定时器
 *
 * @author jiangzg
 * @version 2017-03-21
 */

public class ScheduleTaskController extends BaseController {

    @Autowired
    private PunchCardDataService punchCardDataService ;

    @Autowired
    private PunchCardRecordsService punchCardRecordsService ;

    @Autowired
    private PunchCardRewardsService punchCardRewardsService ;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @Autowired
    private RedPackageActivityInfoService redPackageActivityInfoService ;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService ;

    SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

    /**
     * 每天早晨5：59初始化数据表
     */
    public void initPunchCardDataTable(){
        PunchCardDataVo vo = new PunchCardDataVo();
        vo.setCreateTime(new Date());
        vo.setFailure(0);
        vo.setSuccess(10000);
        vo.setId(IdGen.uuid());
        vo.setTotalCash(10000);
        vo.setTotalNum(10000);
        punchCardDataService.insertSelective(vo);
    }
    /**
     * 每天早晨8:10 开始分发打卡金
     */
    public void punchCardCashToUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        float userCash = 0 ;
        int personNum = 0 ;
        int cashNum = 0;
        int todayPerson = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstStart = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        Date firstEnd = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date secondEnd = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        Date secondStart = calendar.getTime();
        Map<String,Object> requestMap = new HashMap<String,Object>();
        requestMap.put("firstStart",firstStart);
        requestMap.put("firstEnd",firstEnd);
        requestMap.put("secondStart",secondStart);
        requestMap.put("secondEnd",secondEnd);
        List<PunchCardDataVo> punchCardDataVos = punchCardDataService.getLastOneData();
        PunchCardDataVo punchCardDataVo = null ;
        if(punchCardDataVos !=null && punchCardDataVos.size()>0){
            if(punchCardDataVos.size() == 1){
                punchCardDataVo = punchCardDataVos.get(0);
                cashNum = punchCardDataVo.getTotalCash() - 10000 ;
                personNum = punchCardDataVo.getTotalCash() - 10000 ;
            }else{
                punchCardDataVo = punchCardDataVos.get(1);
                cashNum = punchCardDataVo.getTotalCash() - 10000 ;
                personNum = punchCardDataVo.getTotalCash() - 10000 ;
            }
        }
        userCash = Float.valueOf(String.valueOf(cashNum / personNum).format("%.2f"));
        List<Map<String,Object>> recordVos = punchCardRecordsService.getTodayPunchCardRecords(requestMap);
        //成功人数
        int successNum = recordVos.size();
        //失败人数
        int failureNum = personNum - successNum ;
        punchCardDataVo.setFailure(failureNum);
        punchCardDataVo.setSuccess(successNum);
        punchCardDataService.updateByPrimaryKeySelective(punchCardDataVo);
        List batchInsert = new ArrayList();
        if(recordVos != null && recordVos.size()>0){
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.clear();
            calendar.setTime(new Date());
            String takeTime = sdf.format(calendar.getTime());
            for(int i=0;i<recordVos.size();i++){
                String openId = String.valueOf(recordVos.get(i).get("openId"));
                String nickName = String.valueOf(recordVos.get(i).get("nickName"));
                String dayTh = String.valueOf(recordVos.get(i).get("dayTh"));
                /**
                 * 批量插入以及微信付款
                 */
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id", IdGen.uuid());
                map.put("openId",openId);
                map.put("nickName", nickName);
                map.put("cashAmount",userCash);
                map.put("delFlag", 0);
                map.put("createTime",new Date());
                batchInsert.add(map);
                Map params = map;
                map.put("desc","打卡活动");
                try {
                    redPackageActivityInfoService.payRedPackageToUser(map,new HashMap<String, Object>(), request, session);
                } catch (BalanceNotEnoughException e) {
                    e.printStackTrace();
                } catch (BusinessPaymentExceeption businessPaymentExceeption) {
                    businessPaymentExceeption.printStackTrace();
                }
                //推送分钱
                StringBuffer msg = new StringBuffer();
                msg.append("你的早起打卡奖励已到账，请在微信钱包查收。\n" +
                        "任务名称："+takeTime+"6:00-8:00早起打卡 \n"+
                        "任务类别：打卡挑战\n"+
                        "任务奖励金额："+userCash+"\n");
                msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>"+ "加油！参加下次挑战》》</a>");
                String sendResult = WechatUtil.sendMsgToWechat(tokenId, openId, msg.toString());
                LogUtils.saveLog("ZQTZ_TKTZ", openId + "--" + msg.toString());
            }
            punchCardRewardsService.batchInsertPunchCardRewards(batchInsert);
        }
    }
    /**
     * 每天早晨6：00 提醒打卡
     */
    public void pushRemindPunchCardMsg(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        PunchCardRecordsVo vo = new PunchCardRecordsVo();
        vo.setCreateTime(date);
        List<PunchCardRecordsVo> punchCardRecordsVos = punchCardRecordsService.getLastPunchCardRecord(vo);
        if(punchCardRecordsVos !=null && punchCardRecordsVos.size()>0){
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            for(int i= 0 ; i< punchCardRecordsVos.size();i++){
                String openId = punchCardRecordsVos.get(i).getOpenId();
                StringBuffer msg = new StringBuffer();
                msg.append("起来啦！起来啦！\n" +
                        "距离挑战结束还有2个小时，请在8:00\n"+
                        "前完成早起打卡。\n");
                msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>"+ "去打卡》》</a>");
                String sendResult = WechatUtil.sendMsgToWechat(tokenId, openId, msg.toString());
                LogUtils.saveLog("ZQTZ_QDK", openId + "--" + msg.toString());
            }
        }
    }
    /**
     * 每天晚上22：00 挑战提醒
     */
    public void sendPunchCardChallengeMsg(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        PunchCardRecordsVo vo = new PunchCardRecordsVo();
        vo.setCreateTime(date);
        List<PunchCardRecordsVo> punchCardRecordsVos = punchCardRecordsService.getLastPunchCardRecord(vo);
        if(punchCardRecordsVos !=null && punchCardRecordsVos.size()>0){
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            for(int i= 0 ; i< punchCardRecordsVos.size();i++){
                String openId = punchCardRecordsVos.get(i).getOpenId();
                StringBuffer msg = new StringBuffer();
                msg.append("挑战提醒：\n" +
                        "明早上的“早起挑战”可以报名参加了！\n"+
                        "和小伙伴们一起早起迎接美好的时光。\n");
                msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>"+ "参加挑战》》</a>");
                String sendResult = WechatUtil.sendMsgToWechat(tokenId, openId, msg.toString());
                LogUtils.saveLog("ZQTZ_TZTX", openId + "--" + msg.toString());
            }
        }
    }

}
