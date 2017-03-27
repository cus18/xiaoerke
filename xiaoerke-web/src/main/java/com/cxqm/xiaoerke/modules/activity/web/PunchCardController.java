package com.cxqm.xiaoerke.modules.activity.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.activity.entity.*;
import com.cxqm.xiaoerke.modules.activity.service.*;
import com.cxqm.xiaoerke.modules.activity.service.impl.PunchCardRewardsServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
@Controller
@RequestMapping(value = "punchCard/")
public class PunchCardController {

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private AccountService accountService;

    //用户每天打卡详细
    @Autowired
    private PunchCardRecordsService punchCardRecordsService;

    //打卡活动记录
    @Autowired
    private PunchCardDataService punchCardDataService;

    // 用户参加打卡活动信息
    @Autowired
    private PunchCardInfoService punchCardInfoService;

    //用户分的奖励
    @Autowired
    private PunchCardRewardsServiceImpl punchCardRewardsService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private PunchCardRewardDataService punchCardRewardDataService ;

    private Lock lock = new ReentrantLock();

    /**
     * 新用户生成邀请卡
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "makeNewInviteCard", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> makeNewInviteCard(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);//"oogbDwD_2BTQpftPu9QClr-mCw7U"
        String oldOpenId = String.valueOf(params.get("oldOpenId"));
        String marketer = String.valueOf(params.get("market"));
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String tokenId = (String) userWechatParam.get("token");
        String nickName = WechatUtil.getWechatName(tokenId, oldOpenId).getNickname();
        String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码
        String headImgUrl = olyGamesService.getWechatMessage(oldOpenId);//头像
        response.put("userQRCode", userQRCode);
        if (StringUtils.isNotNull(headImgUrl)) {
            response.put("headImgUrl", headImgUrl);
        } else {
            response.put("headImgUrl", "http://img5.imgtn.bdimg.com/it/u=1846948884,880298315&fm=21&gp=0.jpg");
        }
        response.put("nickName", nickName);
        return response;
    }

    /**
     * 进入打卡活动页
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "getPunchCardPage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String, Object> getPunchCardPage(@RequestBody HashMap<String, Object> params ,HttpServletRequest request, HttpSession session) {
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);
        String headImgUrl = olyGamesService.getWechatMessage(openId);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        responseMap.put("openId",openId);
        responseMap.put("headImgUrl",headImgUrl);
        if (StringUtils.isNotNull(openId)) {
            PunchCardInfoVo punchCardInfoVo = new PunchCardInfoVo();
            punchCardInfoVo.setOpenId(openId);
            List<PunchCardInfoVo> reList = punchCardInfoService.getPunchCardInfoBySelective(punchCardInfoVo);
            if(reList != null &&reList.size() > 0){
                punchCardInfoVo = reList.get(0);
            }else{
                punchCardInfoVo.setCreateTime(new Date());
                punchCardInfoVo.setDelFlag(0);
                punchCardInfoVo.setId(IdGen.uuid());
                punchCardInfoVo.setTotalDays(0);
                if (lock.tryLock()) {
                    try {
                        List<PunchCardInfoVo> punchCardInfoVos = punchCardInfoService.getLastOnePunchCardInfoVo();
                        int totalNum = 1470000001;
                        if (punchCardInfoVos != null) {
                            if (punchCardInfoVos.size() > 0) {
                                totalNum = punchCardInfoVos.get(0).getMarketer();
                                totalNum++;
                            }
                        }
                        punchCardInfoVo.setMarketer(totalNum);
                        int num = punchCardInfoService.insert(punchCardInfoVo);
                    } catch (Exception ex) {
                        System.out.print(ex.getStackTrace());
                    } finally {
                        lock.unlock();   //释放锁
                    }
                }
            }
            responseMap.put("marketer",punchCardInfoVo.getMarketer());
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = String.valueOf(userWechatParam.get("token"));
            String nickName = WechatUtil.getWechatName(tokenId, openId).getNickname();
            responseMap.put("nickName",nickName);
            Date date = null;
            Calendar calendarOld = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (currentHour >= 8 && currentHour <= 23) {
                calendarOld.clear();
                calendarOld.set(Calendar.YEAR, currentYear);
                calendarOld.set(Calendar.MONTH, currentMonth);
                calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                calendarOld.set(Calendar.HOUR_OF_DAY, 6);
                calendarOld.set(Calendar.MINUTE, 0);
                calendarOld.set(Calendar.SECOND, 0);
                date = calendarOld.getTime();

            } else if (currentHour >= 0 && currentHour < 6){
                calendarOld.clear();
                calendarOld.set(Calendar.YEAR, currentYear);
                calendarOld.set(Calendar.MONTH, currentMonth);
                calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                calendarOld.add(Calendar.DATE, -1);
                calendarOld.set(Calendar.HOUR_OF_DAY, 6);
                calendarOld.set(Calendar.MINUTE, 0);
                calendarOld.set(Calendar.SECOND, 0);
                date = calendarOld.getTime();
            }else{
                calendarOld.clear();
                calendarOld.set(Calendar.YEAR, currentYear);
                calendarOld.set(Calendar.MONTH, currentMonth);
                calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                calendarOld.add(Calendar.DATE, -1);
                calendarOld.set(Calendar.HOUR_OF_DAY, 6);
                calendarOld.set(Calendar.MINUTE, 0);
                calendarOld.set(Calendar.SECOND, 0);
                date = calendarOld.getTime();
            }
            PunchCardRecordsVo vo = new PunchCardRecordsVo();
            vo.setOpenId(openId);
            vo.setCreateTime(date);
            vo.setNickName(nickName);
            vo.setState(0);
            List<PunchCardRecordsVo> list = punchCardRecordsService.getLastPunchCardRecord(vo);
            if (list != null && list.size() > 0) {
                vo = list.get(0);
                if(vo.getState() == 1){
                    responseMap.put("isOrNotPay", "no");
                }else{
                    responseMap.put("isOrNotPay", "yes");
                }
                responseMap.put("punchCount", vo.getDayth());
            } else {
                responseMap.put("isOrNotPay", "no");
                responseMap.put("punchCount", punchCardRecordsService.getSelfPunchCardCount(openId));
            }
            //获奖人数数据及成功失败数
            if (currentHour >= 9) {
                //昨天数据
                calendarOld.clear();
                calendarOld.set(Calendar.YEAR, currentYear);
                calendarOld.set(Calendar.MONTH, currentMonth);
                calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                calendarOld.set(Calendar.HOUR_OF_DAY, 8);
                calendarOld.set(Calendar.MINUTE, 0);
                calendarOld.set(Calendar.SECOND, 0);
                date = calendarOld.getTime();
            } else {
                //前天数据
                calendarOld.clear();
                calendarOld.set(Calendar.YEAR, currentYear);
                calendarOld.set(Calendar.MONTH, currentMonth);
                calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                calendarOld.add(Calendar.DATE, -1);
                calendarOld.set(Calendar.HOUR_OF_DAY, 8);
                calendarOld.set(Calendar.MINUTE, 0);
                calendarOld.set(Calendar.SECOND, 0);
                date = calendarOld.getTime();
            }
            /**
             * 假数据开关
             */
            PunchCardRewardsVo punchCardRewardsVo = new PunchCardRewardsVo();
            punchCardRewardsVo.setCreateTime(date);
            List<Map<String, Object>> punchCardRewardsVos = punchCardRewardsService.getPunchCardRewards(punchCardRewardsVo);
            if("on".equalsIgnoreCase(sysPropertyVoWithBLOBsVo.getPunchCardDataSwitch())){
                responseMap.put("punchCardSwitch", "on");
                if(currentHour >=9){
                    calendarOld.clear();
                    calendarOld.set(Calendar.YEAR, currentYear);
                    calendarOld.set(Calendar.MONTH, currentMonth);
                    calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                    calendarOld.set(Calendar.HOUR_OF_DAY, 7);
                    calendarOld.set(Calendar.MINUTE, 59);
                    calendarOld.set(Calendar.SECOND, 0);
                    date = calendarOld.getTime();
                }else{
                    calendarOld.set(Calendar.YEAR, currentYear);
                    calendarOld.set(Calendar.MONTH, currentMonth);
                    calendarOld.set(Calendar.DAY_OF_MONTH, currentDay);
                    calendarOld.add(Calendar.DATE, -1);
                    calendarOld.set(Calendar.HOUR_OF_DAY, 7);
                    calendarOld.set(Calendar.MINUTE, 59);
                    calendarOld.set(Calendar.SECOND, 0);
                    date = calendarOld.getTime();
                }
                PunchCardRewardDataVo punchCardRewardDataVo = new PunchCardRewardDataVo();
                punchCardRewardDataVo.setCreateTime(date);
                List<PunchCardRewardDataVo> punchCardRewardDataVos = punchCardRewardDataService.getMoreDataBySelective(punchCardRewardDataVo);
                if(punchCardRewardDataVos !=null && punchCardRewardDataVos.size()>0){
                    responseMap.put("personRewardsList", punchCardRewardDataVos);
                    responseMap.put("personRewardsSize", punchCardRewardsVos.size());
                }else{
                    responseMap.put("personRewardsList", new ArrayList());
                    responseMap.put("personRewardsSize", 0);
                }
                responseMap.put("pageNo", params.get("pageNo"));
                responseMap.put("pageSize", params.get("pageSize"));
            }else{
                responseMap.put("punchCardSwitch", "off");
                params.put("createTime",date);
                Map resMap= punchCardRewardsService.getPunchCardRewardByPage(params);
                if (punchCardRewardsVos != null && punchCardRewardsVos.size() > 0) {
                    if(resMap != null && resMap.size()>0){
                        responseMap.put("personRewardsList", resMap.get("personRewardsList"));
                        responseMap.put("pageNo", resMap.get("pageNo"));
                        responseMap.put("pageSize", resMap.get("pageSize"));
                    }else{
                        responseMap.put("personRewardsList", new ArrayList());
                    }
                    responseMap.put("personRewardsSize", punchCardRewardsVos.size());
                } else {
                    if(resMap != null && resMap.size()>0){
                        responseMap.put("personRewardsList", resMap.get("personRewardsList"));
                        responseMap.put("pageNo", resMap.get("pageNo"));
                        responseMap.put("pageSize", resMap.get("pageSize"));
                    }else{
                        responseMap.put("personRewardsList", new ArrayList());
                    }
                    responseMap.put("personRewardsSize", 0);
                }
            }

            //查询总数   定时任务每天五点五十跑：创建punch_card_data表
            if (currentHour >= 6) {
                //今天数据
                PunchCardDataVo punchCardDataVo = null;
                List<PunchCardDataVo> result = punchCardDataService.getLastOneData();
                if (result != null && result.size() > 0) {
                    punchCardDataVo = result.get(0);
                    responseMap.put("totalPersonNum", punchCardDataVo.getTotalNum());
                    responseMap.put("totalCashNum", punchCardDataVo.getTotalCash());
                } else {
                    responseMap.put("totalPersonNum", "0");
                    responseMap.put("totalCashNum", "0");
                }
                if (currentHour >= 9) {
                    List<PunchCardDataVo> punchCardDataVos = punchCardDataService.getLastOneData();
                    if (punchCardDataVos != null && punchCardDataVos.size() > 1) {
                        responseMap.put("failNum", punchCardDataVos.get(1).getFailure());
                        responseMap.put("successNum", punchCardDataVos.get(1).getSuccess());
                    } else if (punchCardDataVos.size() >= 0) {
                        responseMap.put("failNum", punchCardDataVos.get(0).getFailure());
                        responseMap.put("successNum", punchCardDataVos.get(0).getSuccess());
                    } else {
                        responseMap.put("failNum", 0);
                        responseMap.put("successNum", 0);
                    }
                } else {
                    List<PunchCardDataVo> punchCardDataVos = punchCardDataService.getLastOneData();
                    if (punchCardDataVos != null && punchCardDataVos.size() > 0) {
                        responseMap.put("failNum", punchCardDataVos.get(0).getFailure());
                        responseMap.put("successNum", punchCardDataVos.get(0).getSuccess());
                    } else {
                        responseMap.put("failNum", 0);
                        responseMap.put("successNum", 0);
                    }
                }
            } else {
                //成功失败数
                List<PunchCardDataVo> punchCardDataVos = punchCardDataService.getLastOneData();
                if (punchCardDataVos != null && punchCardDataVos.size() > 0) {
                    responseMap.put("failNum", punchCardDataVos.get(0).getFailure());
                    responseMap.put("successNum", punchCardDataVos.get(0).getSuccess());
                } else {
                    responseMap.put("failNum", 0);
                    responseMap.put("successNum", 0);
                }
                //昨天数据
                PunchCardDataVo punchCardDataVo = null;
                List<PunchCardDataVo> result = punchCardDataService.getLastOneData();
                if (result != null && result.size() > 0) {
                    if (currentHour < 5) {
                        if (result.size() == 1) {
                            punchCardDataVo = result.get(0);
                            responseMap.put("totalPersonNum", punchCardDataVo.getTotalNum());
                            responseMap.put("totalCashNum", punchCardDataVo.getTotalCash());
                        } else {
                            punchCardDataVo = result.get(0);
                            responseMap.put("totalPersonNum", punchCardDataVo.getTotalNum());
                            responseMap.put("totalCashNum", punchCardDataVo.getTotalCash());
                        }
                    } else {
                        if (result.size() == 1) {
                            punchCardDataVo = result.get(0);
                            responseMap.put("totalPersonNum", punchCardDataVo.getTotalNum());
                            responseMap.put("totalCashNum", punchCardDataVo.getTotalCash());
                        } else {
                            punchCardDataVo = result.get(1);
                            responseMap.put("totalPersonNum", punchCardDataVo.getTotalNum());
                            responseMap.put("totalCashNum", punchCardDataVo.getTotalCash());
                        }
                    }
                } else {
                    responseMap.put("totalPersonNum", "0");
                    responseMap.put("totalCashNum", "0");
                }
            }
        }
        return responseMap;
    }

    /**
     * 参加打卡活动
     *
     * @param params
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "takePunchCardActivity", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String, Object> takePunchCardActivity(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        resultMap.put("status", "failure");
        String openId = String.valueOf(params.get("openId"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        PunchCardRecordsVo vo = new PunchCardRecordsVo();
        if (currentHour >= 6 && currentHour < 8) {
            if (StringUtils.isNotNull(openId)) {
                calendar.clear();
                calendar.set(Calendar.YEAR, currentYear);
                calendar.set(Calendar.MONTH, currentMonth);
                calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                calendar.set(Calendar.HOUR_OF_DAY, 6);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date date = calendar.getTime();
                PunchCardRecordsVo punchCardRecordsVo = new PunchCardRecordsVo();
                punchCardRecordsVo.setUpdateTime(date);
                punchCardRecordsVo.setOpenId(openId);
                punchCardRecordsVo.setState(1);
                List<PunchCardRecordsVo> resultList = punchCardRecordsService.getLastPunchCardRecord(punchCardRecordsVo);
                if(resultList != null && resultList.size() > 0){
                    System.out.println("打过一次卡，请明天再来");
                }else{
                    PunchCardInfoVo punchCardInfoVo = new PunchCardInfoVo();
                    punchCardInfoVo.setOpenId(openId);
                    List<PunchCardInfoVo> list = punchCardInfoService.getPunchCardInfoBySelective(punchCardInfoVo);
                    if (list != null && list.size() > 0) {
                        punchCardInfoVo = list.get(0);
                        punchCardInfoVo.setTotalDays(punchCardInfoVo.getTotalDays() + 1);
                        int num = punchCardInfoService.updateByPrimaryKeySelective(punchCardInfoVo);
                        if (num > 0) {
                            vo.setOpenId(openId);
                            List<PunchCardRecordsVo> vos = punchCardRecordsService.getLastPunchCardRecord(vo);
                            vo = vos.get(0);
                            vo.setState(1);
                            vo.setDayth(vo.getDayth() + 1);
                            num = punchCardRecordsService.updateByPrimaryKeySelective(vo);
                            if (num > 0) {
                                resultMap.put("status", "success");
                                if(lock.tryLock()){
                                    try {
                                        calendar.clear();
                                        calendar.set(Calendar.YEAR, currentYear);
                                        calendar.set(Calendar.MONTH, currentMonth);
                                        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                                        calendar.set(Calendar.HOUR_OF_DAY, 5);
                                        calendar.set(Calendar.MINUTE, 50);
                                        calendar.set(Calendar.SECOND, 0);
                                        date = calendar.getTime();
                                        PunchCardDataVo punchCardDataVo = new PunchCardDataVo();
                                        punchCardDataVo.setCreateTime(date);
                                        punchCardDataService.updatePunchCardNum(punchCardDataVo);
                                    } catch (Exception ex) {
                                        System.out.print(ex.getStackTrace());
                                    } finally {
                                        lock.unlock();   //释放锁
                                    }
                                }
                                //推送：打卡失败文案
                                if ("failure".equals(resultMap.get("status"))) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    StringBuffer msg = new StringBuffer();
                                    String takeTime = sdf.format(calendar.getTime());
                                    msg.append(takeTime + "早起打卡失败，不要气馁，" +
                                            "继续 \n");
                                    msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>" + "加油！参加下次挑战》》</a>");
                                    Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                                    String sendResult = WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), openId, msg.toString());
                                    LogUtils.saveLog("ZQTZ_DKSB", openId + "--" + msg.toString());
                                } else {
                                    //推送完成打卡
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    StringBuffer msg = new StringBuffer();
                                    String takeTime = sdf.format(calendar.getTime());
                                    msg.append(takeTime + "完成早起打卡" +
                                            "连续"+vo.getDayth()+"天挑战成功！你将会收到平分的挑战金。\n"+
                                            "24小时内发送至微信钱包。\n");
                                    msg.append("<a href='" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=57'>" + "加油！参加下次挑战》》</a>");
                                    Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                                    String sendResult = WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), openId, msg.toString());
                                    LogUtils.saveLog("ZQTZ_WCDK", openId+ "--" + msg.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultMap ;
    }

    /**
     * 查询自己的打卡记录
     *
     * @param params
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "findPunchCardBySelf", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String, Object> findPunchCardBySelf(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session) {
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        String openId = String.valueOf(params.get("openId"));
        String headImgUrl = olyGamesService.getWechatMessage(openId);
        responseMap.put("headImgUrl",headImgUrl);
        PunchCardRecordsVo punchCardRecordsVo = new PunchCardRecordsVo();
        punchCardRecordsVo.setOpenId(openId);
        List<PunchCardRecordsVo> resultList = punchCardRecordsService.getSelfPunchCardRecords(punchCardRecordsVo);
        if (resultList != null && resultList.size() > 0) {
            responseMap.put("dataList", resultList);
            Map<String, Object> result = punchCardRewardsService.getSelfRewardsInfo(openId);
            if (result != null && !result.isEmpty()) {
                responseMap.put("rewardsInfo", result);
            }
        }else{
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            String nickName = WechatUtil.getWechatName(tokenId, openId).getNickname();
            responseMap.put("nickName",nickName);
            responseMap.put("dataList", new ArrayList());
            responseMap.put("rewardsInfo", new ArrayList());
        }
        return responseMap;
    }

    /**
     * 用户支付JS
     *
     * @param
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "payPunchCardCash", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    String payPunchCardCash(HttpServletRequest request, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        //获取统一支付接口参数
        String payParameter = "" ;
        try{
            Map prepayInfo = accountService.getPrepayInfo(request, session, "punchCardActivity");
            prepayInfo.put("feeType", "punchCardActivity");
            System.out.println("feeType:" + prepayInfo.get("feeType").toString());
            //拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
            String userId = WechatUtil.getOpenId(session, request);
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            String nickName = WechatUtil.getWechatName(tokenId, userId).getNickname();
            //patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);
            payParameter = accountService.assemblyPayParameter(request, prepayInfo, session, userId, null);
            LogUtils.saveLog("punchCard-" + userId, payParameter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return payParameter;
    }

    @RequestMapping(value = "pushMsgToPayUser", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    HashMap<String,Object> pushMsgToPayUser(HttpServletRequest request, HttpSession session) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("status","failure");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        String userId = WechatUtil.getOpenId(session, request);
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        String tokenId = (String) userWechatParam.get("token");
        String nickName = WechatUtil.getWechatName(tokenId, userId).getNickname();
        PunchCardRecordsVo punchCardRecordsVo = new PunchCardRecordsVo();
        punchCardRecordsVo.setCreateTime(new Date());
        punchCardRecordsVo.setDayth(punchCardRecordsService.getSelfPunchCardCount(userId));
        punchCardRecordsVo.setDelFlag(0);
        punchCardRecordsVo.setId(IdGen.uuid());
        punchCardRecordsVo.setOpenId(userId);
        punchCardRecordsVo.setNickName(nickName);
        punchCardRecordsVo.setState(0);
        int num = punchCardRecordsService.insertSelective(punchCardRecordsVo);
        if (num > 0) {
            // 推送：挑战已开启文案
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            StringBuffer msg = new StringBuffer();
            Date date = null;
            if (currentHour >=0 && currentHour < 8) {
                date = calendar.getTime();
                String takeTime = sdf.format(date);
                msg.append("请于" + takeTime + " 6:00-8:00早起打卡，" +
                        "完成挑战。\n" +
                        "提示：大波挑战金，千万不要错过………\n");
            } else {
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                String takeTime = sdf.format(date);
                msg.append("请于" + takeTime + " 6:00-8:00早起打卡，" +
                        "完成挑战。\n" +
                        "提示：大波挑战金，千万不要错过………\n");
            }
            msg.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57'>" + "去打卡》》</a>");
            String sendResult = WechatUtil.sendMsgToWechat(tokenId, userId, msg.toString());
            LogUtils.saveLog("ZQTZ_QDK", userId + "--" + msg.toString());
            resultMap.put("status", "success");
        }
        return resultMap;
    }

    /**
     * 分页获取获奖信息
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "getPunchCardRewards", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getPunchCardRewards(@RequestBody HashMap<String, Object> params , HttpServletRequest request, HttpSession session) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Date date = null;
        if (currentHour >= 9) {
            //昨天数据
            calendar.clear();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
            calendar.set(Calendar.DAY_OF_MONTH, currentDay);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
        } else {
            //前天数据
            calendar.clear();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
            calendar.set(Calendar.DAY_OF_MONTH, currentDay);
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
        }
        PunchCardRewardsVo punchCardRewardsVo = new PunchCardRewardsVo();
        punchCardRewardsVo.setCreateTime(date);
        params.put("createTime",date);
        Map responseMap= punchCardRewardsService.getPunchCardRewardByPage(params);
        return responseMap;
    }
}
