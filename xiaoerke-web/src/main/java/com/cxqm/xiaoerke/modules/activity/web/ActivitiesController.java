package com.cxqm.xiaoerke.modules.activity.web;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageActivityInfoVo;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageResultsInfoVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageActivityInfoService;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageResultsInfoService;
import com.cxqm.xiaoerke.modules.alipay.util.httpClient.StringUtil;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.sys.entity.Log;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import groovy.transform.Synchronized;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jiangzhongge on 2017-2-20.
 */
@Controller
@RequestMapping(value = "activity")
public class ActivitiesController {

    @Autowired
    private RedPackageActivityInfoService redPackageActivityInfoService;

    @Autowired
    private RedPackageResultsInfoService redPackageResultsInfoService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private BabyCoinService babyCoinService;

    @Autowired
    private AccountService accountService;

    private Lock lock = new ReentrantLock();

    /**
     * 邀请卡新用户点击生成页面
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/createInviteCardInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> createInviteCardInfo(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);//"oogbDwD_2BTQpftPu9QClr-mCw7U"
        String oldOpenId = String.valueOf(params.get("oldOpenId"));
        String marketer = String.valueOf(params.get("market"));
        RedpackageActivityInfoVo vo = new RedpackageActivityInfoVo();
        vo.setOpenId(oldOpenId);
        List<RedpackageActivityInfoVo> result = redPackageActivityInfoService.getRedpackageActivityBySelective(vo);
        if (result != null && result.size() > 0) {
            vo = result.get(0);
        }
        String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码
        String headImgUrl = olyGamesService.getWechatMessage(oldOpenId);//头像
        response.put("userQRCode", userQRCode);
        if (StringUtils.isNotNull(headImgUrl)) {
            response.put("headImgUrl", headImgUrl);
        } else {
            response.put("headImgUrl", "http://img5.imgtn.bdimg.com/it/u=1846948884,880298315&fm=21&gp=0.jpg");
        }
        response.put("redPackageVo", vo);
        return response;
    }

    /**
     * 合成五张卡片
     *
     * @param params
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateRedPackageInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> updateRedPackageInfo(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String id = String.valueOf(params.get("id"));
        String openId = String.valueOf(params.get("openId"));
        resultMap.put("status", "failure");
        if (StringUtils.isNull(id)) {
            LogUtils.saveLog("ACTITY_RPC_Err", "id is null , openId = " + openId);
        } else {
            /*String type = String.valueOf(params.get("type"));
            String moneyCount = String.valueOf(params.get("money"));
            String cardType = String.valueOf(params.get("cardType"));*/
            RedpackageActivityInfoVo vo = redPackageActivityInfoService.selectByPrimaryKey(id);
            if (vo != null) {
                int card = vo.getCardBig();
                int card1 = vo.getCardHappy();
                int card2 = vo.getCardHealth();
                int card3 = vo.getCardLove();
                int card4 = vo.getCardRuyi();
                int card5 = vo.getCardYoushan();
                if (card1 > 0 && card2 > 0 && card3 > 0 && card4 > 0 && card5 > 0) {
                    vo.setCardBig(card + 1);
                    vo.setCardHappy(card1 - 1);
                    vo.setCardHealth(card2 - 1);
                    vo.setCardLove(card3 - 1);
                    vo.setCardRuyi(card4 - 1);
                    vo.setCardYoushan(card5 - 1);
                    int num = redPackageActivityInfoService.updateByPrimaryKeySelective(vo);
                    if (num > 0) {
                        resultMap.put("status", "success");
                    } else {
                        resultMap.put("info", "combineErr");
                    }
                } else {
                    resultMap.put("info", "cardsErr");
                }
            } else {
                resultMap.put("info", "voErr");
                LogUtils.saveLog("ACTITY_RPC_Err", "vo is null ,id=" + id);
            }
        }
        return resultMap;
    }

    @RequestMapping(value = "/chooseCard", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> chooseCard(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "failure");
        double ma = Math.random() * 100;
        if (ma < 10) {
            ma = ma * 10;
        }
        String randomString = String.valueOf(ma);
        int moneyNum = Integer.valueOf(randomString.substring(0, 1));
        int type;
        double moneyCount;
        if (ma <= 80) {
            type = 0;
            if (ma <= 30) {
                moneyCount = 5;
            } else if (ma <= 50) {
                moneyCount = 10;
            } else if (ma <= 60) {
                moneyCount = 20;
            } else if (ma <= 70) {
                moneyCount = 30;
            } else {
                moneyCount = 40;
            }
        } else {
            type = 1;
            if (ma <= 90) {
                moneyCount = Double.valueOf("1." + moneyNum + "5");
            } else if (ma <= 93) {
                moneyCount = Double.valueOf("2." + moneyNum + "5");
            } else if (ma <= 96) {
                moneyCount = Double.valueOf("3." + moneyNum + "5");
            } else {
                moneyCount = Double.valueOf("4." + moneyNum + "5");
            }
        }
        params.put("type", type);
        params.put("moneyCount", moneyCount);
        params.put("state", 0);
        try {
            redPackageActivityInfoService.chooseCardsAndRewards(params, response, session, request);
        } catch (BusinessPaymentExceeption businessPaymentExceeption) {
            businessPaymentExceeption.printStackTrace();
        } catch (BalanceNotEnoughException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 抽卡片记录获奖信息以及更新卡片数量
     *
     * @param params
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveRewardsInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> saveRewardsInfo(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);
        resultMap.put("status", "failure");
        String type = String.valueOf(params.get("type"));
        String moneyCount = String.valueOf(params.get("moneyCount"));
        String state = "0";
        RedpackageResultsInfoVo vo = new RedpackageResultsInfoVo();
        vo.setId(IdGen.uuid());
        vo.setCreateTime(new Date());
        vo.setDelFlag(0);
        vo.setType(Integer.valueOf(type));
        vo.setState(Integer.valueOf(state));
        vo.setMoneyCount(Double.parseDouble(moneyCount));
        vo.setOpenId(openId);
        int reNum = redPackageResultsInfoService.insert(vo);
        if (reNum > 0) {
            resultMap.put("status", "success");
        }
        return resultMap;
    }

    /**
     * 进入活动主页，获取信息接口
     *
     * @param params
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCardInfoList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> getCardInfoList(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);
        RedpackageActivityInfoVo vo = new RedpackageActivityInfoVo();
        if (StringUtils.isNotNull(openId)) {
            vo.setOpenId(openId);
            List<RedpackageActivityInfoVo> result = redPackageActivityInfoService.getRedpackageActivityBySelective(vo);
            if (result != null && result.size() > 0) {
                vo = result.get(0);
                RedpackageResultsInfoVo resultsVo = new RedpackageResultsInfoVo();
                resultsVo.setOpenId(openId);
                List<RedpackageResultsInfoVo> resultList = redPackageResultsInfoService.getRedPackageResultsBySelective(resultsVo);
                if (resultList != null && resultList.size() > 0) {
                    resultsVo = resultList.get(0);
                    resultMap.put("rewardsVo", resultList);
                    resultMap.put("rewardStatus", "rewardsSuccess");
                } else {
                    resultMap.put("rewardStatus", "rewardsFailure");
                }
                resultMap.put("status", "success");
            } else {
                vo.setCardBig(0);
                vo.setCardHappy(0);
                vo.setCardHealth(0);
                vo.setCardLove(0);
                vo.setCardRuyi(0);
                vo.setCardYoushan(0);
                vo.setCreateTime(new Date());
                vo.setDelState(0);
                vo.setId(IdGen.uuid());
                vo.setTotalInvitation(0);
                if (lock.tryLock()) {
                    try {
                        int totalNum = redPackageActivityInfoService.getRedPackageActivityTatalCount();
                        String market = "";
                        String value = String.valueOf(totalNum);
                        int len = value.length();
                        switch (len) {
                            case 1:
                                market = "707000000" + value;
                                break;
                            case 2:
                                market = "70700000" + value;
                                break;
                            case 3:
                                market = "7070000" + value;
                                break;
                            case 4:
                                market = "707000" + value;
                                break;
                            case 5:
                                market = "70700" + value;
                                break;
                            case 6:
                                market = "7070" + value;
                                break;
                            default:
                                market = "707" + value;
                        }
                        vo.setMarket(Integer.valueOf(market));
                        int num = redPackageActivityInfoService.insert(vo);
                        if (num > 0) {
                            resultMap.put("dataStatus", "insertSuccess");
                            resultMap.put("data", vo);
                        } else {
                            resultMap.put("dataStatus", "failure");
                        }
                    } catch (Exception ex) {
                        System.out.print(ex.getStackTrace());
                    } finally {
                        lock.unlock();   //释放锁
                    }
                }
            }
        } else {
            resultMap.put("status", "failure");
        }
        resultMap.put("cardInfoVo", vo);
        return resultMap;
    }
}
