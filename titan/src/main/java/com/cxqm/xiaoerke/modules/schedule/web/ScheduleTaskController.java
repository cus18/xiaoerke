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
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardDataService;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardsService;
import com.cxqm.xiaoerke.modules.activity.service.impl.RedPackageActivityInfoServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * titan定时器
 *
 * @author jiangzg
 * @version 2017-03-21
 */

public class ScheduleTaskController {

    @Autowired
    private PunchCardDataService punchCardDataService ;

    @Autowired
    private PunchCardRecordsService punchCardRecordsService ;

    @Autowired
    private PunchCardRewardsService punchCardRewardsService ;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @Autowired
    private RedPackageActivityInfoServiceImpl redPackageActivityInfoService ;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService ;

    @Autowired
    private PunchCardRewardDataService punchCardRewardDataService ;

    /**
     * 每天早晨5：50初始化数据表
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
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        float userCash = 0 ;
        int personNum = 0 ;
        int cashNum = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstStart = calendar.getTime();  // 今日5点
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstEnd = calendar.getTime();    // 今日8点
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date secondEnd = calendar.getTime();   //昨日八点
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date secondStart = calendar.getTime(); //昨日六点

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
                cashNum = punchCardDataVo.getTotalCash() ;
                personNum = punchCardDataVo.getTotalNum();
            }else{
                punchCardDataVo = punchCardDataVos.get(1);
                cashNum = punchCardDataVo.getTotalCash() ;
                personNum = punchCardDataVo.getTotalNum() ;
            }
        }
        String str ;
        if(personNum == 0){
            str = "0";
        }else{
            str =  String.valueOf(cashNum / personNum);
        }

        if(str.contains(".")){
            String[] splitStr = str.split(".");
            if("0".startsWith(splitStr[0])){
                if(splitStr[1].length() >=3){
                    str = "0."+splitStr[1].substring(0,2);
                }else if(splitStr[1].length() >0 && splitStr[1].length() <=2){
                    str = "0."+splitStr[1];
                }else{
                    str = "0";
                }
            }else{
                if(splitStr[1].length() >= 3){
                    str = splitStr[0]+"."+splitStr[1].substring(0,2);
                }else if(splitStr[1].length() > 0 && splitStr[1].length() <= 2){
                    str = splitStr[0]+"."+splitStr[1];
                }else{
                    str = "0";
                }
            }
        }else{
            if(str.length() >= 3){
                str = str.substring(0,3);
            }else if(str.length() >0 && str.length() <=2){

            }else{
                str = "0";
            }
        }
        userCash = Float.valueOf(str);
        List<Map<String,Object>> records = punchCardRecordsService.getTodayPunchCardRecords(requestMap);
        //成功人数
        int successNum = records.size();
        //失败人数
        int failureNum = personNum - successNum - 10000;
        punchCardDataVo.setFailure(failureNum);
        punchCardDataVo.setSuccess(successNum+10000);
        punchCardDataService.updateByPrimaryKeySelective(punchCardDataVo);
        List<Map<String,Object>> recordVos = punchCardRecordsService.getTodayPayPersonNum(requestMap);
        List batchInsert = new ArrayList();
        if("on".equalsIgnoreCase(sysPropertyVoWithBLOBsVo.getPunchCardDataSwitch())){
            List batchFalseData = new ArrayList();
            calendar.clear();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
            calendar.set(Calendar.DAY_OF_MONTH, currentDay);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 0);
            Date nowTime = calendar.getTime();
            String[] nickNames = {"奋斗的小蜗牛","成长的风雨","无限的能量","红豆峡","塞塞","舍得","慧明","开心小当家","罗兰西","皮带","浅浅","上神","明天会更好","未来无极限","美丽大无边","幸运树","你好，明天","我的未来不是梦","相信你","我的开心果"};
            String imageUrl = "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/activity/dataPic/";
            int j;
            if(currentDay % 5 == 0){
                j = 0;
            }else if(currentDay % 5 == 1){
                j = 3;
            }else if(currentDay % 5 == 2){
                j = 6;
            }else if(currentDay % 5 == 3){
                j = 9;
            }else{
                j = 19;
            }
            for(int i=1;i<=10;i++){
                Map falseMap = new HashMap();
                falseMap.put("id",IdGen.uuid());
                falseMap.put("openId","909"+i+i+j);
                falseMap.put("nickName",nickNames[j]);
                falseMap.put("createTime",nowTime);
                falseMap.put("updateTime",nowTime);
                falseMap.put("delFlag",0);
                falseMap.put("cashAmount",userCash);
                falseMap.put("dayNum", i);
                falseMap.put("image", imageUrl+(j+1)+".jpg");
                batchFalseData.add(falseMap);
                if(currentDay % 5 == 4){
                    j--;
                }else{
                    j++;
                }
            }
            int num = punchCardRewardDataService.batchInsertPunchCardData(batchFalseData);
            System.out.println(num+"===========================");
        }
        LogUtils.saveLog("punchCardCashToUser",userCash+"==="+recordVos.size());
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
                 * 2017-3-22 14:46:25
                 * 批量插入以及微信付款
                 */
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id", IdGen.uuid());
                map.put("openId",openId);
                map.put("nickName", nickName);
                map.put("cashAmount",userCash);
                map.put("delFlag", 0);
                map.put("createTime", new Date());
                batchInsert.add(map);
                Map params = map;
                params.put("desc", "打卡活动");
                params.put("moneyCount", str);
                LogUtils.saveLog("punchCardCashToUser", "openId="+openId + "=nickName=" + nickName);
                try {
                    LogUtils.saveLog("punchCardCashToUser", "准备付钱");
                    redPackageActivityInfoService.payCashToUser(map, new HashMap<String, Object>());
                    LogUtils.saveLog("punchCardCashToUser", "已经付钱");
                } catch (BalanceNotEnoughException e) {
                    LogUtils.saveLog("punchCardCashToUser", "异常1："+e.getMessage());
                    e.printStackTrace();
                } catch (BusinessPaymentExceeption businessPaymentExceeption) {
                    LogUtils.saveLog("punchCardCashToUser", "异常2："+businessPaymentExceeption.getMessage());
                    businessPaymentExceeption.printStackTrace();
                }catch(Exception ex){
                    LogUtils.saveLog("punchCardCashToUser", "异常3："+ex.getMessage());
                    ex.printStackTrace();
                }finally{
                    //推送分钱
                    LogUtils.saveLog("punchCardCashToUser", "准备分钱推送" + openId);
                    StringBuffer msg = new StringBuffer();
                    msg.append("你的早起打卡奖励已到账，请在微信钱包查收。\n" +
                            "任务名称："+takeTime+" 6:00-8:00早起打卡 \n"+
                            "任务类别：打卡挑战\n"+
                            "任务奖励金额："+userCash+"\n");
                    msg.append("<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=57'>" + "加油！参加下次挑战》》</a>");
                    LogUtils.saveLog("punchCardCashToUser", "推送消息："+msg.toString()+"=token="+tokenId);
                    String sendResult = "";
                    try{
                        sendResult = WechatUtil.sendMsgToWechat(tokenId, openId, msg.toString());
                    }catch (Exception e){
                        LogUtils.saveLog("punchCardCashToUser","推送报错："+e.getMessage());
                        e.printStackTrace();
                    }
                    LogUtils.saveLog("punchCardCashToUser", "推送消息结束："+sendResult);
                    LogUtils.saveLog("ZQTZ_TKTZ", openId + "--" + msg.toString());
                }
            }
            LogUtils.saveLog("punchCardCashToUser", "插入获奖信息开始");
            int num = punchCardRewardsService.batchInsertPunchCardRewards(batchInsert);
            LogUtils.saveLog("punchCardCashToUser", "插入获奖信息结束=="+num);
        }
    }

    /** 最初设计删
     * 每天早晨8:10 开始分发打卡金
     */
/*    public void punchCardCashToUser(){
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        float userCash = 0 ;
        int personNum = 0 ;
        int cashNum = 0;
        int todayPerson = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstStart = calendar.getTime();  // 今日6点
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        Date firstEnd = calendar.getTime();    // 今日8点
        calendar.add(Calendar.DATE, -1);
        Date secondEnd = calendar.getTime();   //昨日八点
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        Date secondStart = calendar.getTime(); //昨日六点
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
        String str =  String.valueOf(cashNum / personNum);
        if(str.contains(".")){
            String[] splitStr = str.split(".");
            if("0".endsWith(splitStr[0])){

            }else{
                str = str.replace(".","");
                if(str.length() > 3){
                    str = str.substring(0,3);
                }
            }
        }else{
            if(str.length() > 3){
                str = str.substring(0,3);
            }
        }
        userCash = Float.valueOf(str);
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
                *//**
                 * 2017-3-22 14:46:25
                 * 批量插入以及微信付款
                 *//*
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id", IdGen.uuid());
                map.put("openId",openId);
                map.put("nickName", nickName);
                map.put("cashAmount",userCash);
                map.put("delFlag", 0);
                map.put("createTime",new Date());
                batchInsert.add(map);
                Map params = map;
                params.put("desc","打卡活动");
                params.put("moneyCount",str);
                try {
                    redPackageActivityInfoService.payCashToUser(map, new HashMap<String, Object>());
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
    }*/

    /**
     * 每天早晨6：00 提醒打卡
     */
    public void pushRemindPunchCardMsg(){
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        PunchCardRecordsVo vo = new PunchCardRecordsVo();
        vo.setCreateTime(date);
        vo.setState(0);
        List<PunchCardRecordsVo> punchCardRecordsVos = punchCardRecordsService.getLastPunchCardRecord(vo);
        if(punchCardRecordsVos !=null && punchCardRecordsVos.size()>0){
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            for(int i= 0 ; i< punchCardRecordsVos.size();i++){
                String openId = punchCardRecordsVos.get(i).getOpenId();
                StringBuffer msg = new StringBuffer();
                msg.append("起来啦！起来啦！\n" +
                        "距离挑战结束还有2个小时，请在8:00"+
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
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        PunchCardRecordsVo vo = new PunchCardRecordsVo();
        vo.setUpdateTime(date);
        vo.setState(1);
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
