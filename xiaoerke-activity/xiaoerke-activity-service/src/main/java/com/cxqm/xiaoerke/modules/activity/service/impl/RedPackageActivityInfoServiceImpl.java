package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.account.service.impl.AccountServiceImpl;
import com.cxqm.xiaoerke.modules.activity.dao.RedpackageActivityInfoDao;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageActivityInfoVo;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageResultsInfoVo;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageActivityInfoService;
import com.cxqm.xiaoerke.modules.activity.service.RedPackageResultsInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-2-20.
 */
@Service
@Transactional(readOnly = false)
public class RedPackageActivityInfoServiceImpl implements RedPackageActivityInfoService {

    @Autowired
    private RedpackageActivityInfoDao redpackageActivityInfoDao ;

    @Autowired
    private SystemService systemService;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private RedPackageResultsInfoService redPackageResultsInfoService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Override
    public int deleteByPrimaryKey(String id) {
        return redpackageActivityInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RedpackageActivityInfoVo record) {
        return redpackageActivityInfoDao.insert(record);
    }

    @Override
    public int insertSelective(RedpackageActivityInfoVo record) {
        return redpackageActivityInfoDao.insertSelective(record);
    }

    @Override
    public RedpackageActivityInfoVo selectByPrimaryKey(String id) {
        return redpackageActivityInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RedpackageActivityInfoVo record) {
        return redpackageActivityInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RedpackageActivityInfoVo record) {
        return redpackageActivityInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public List<RedpackageActivityInfoVo> getRedpackageActivityBySelective(RedpackageActivityInfoVo vo) {
        return redpackageActivityInfoDao.getRedpackageActivityBySelective(vo);
    }

    @Override
    public int getRedPackageActivityTatalCount() {
        return redpackageActivityInfoDao.getRedPackageActivityTatalCount();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> chooseCardsAndRewards(Map<String, Object> params, HashMap<String, Object> response, HttpSession session, HttpServletRequest request) throws BusinessPaymentExceeption, BalanceNotEnoughException {
        String openId = String.valueOf(params.get("openId"));
        String id = String.valueOf(params.get("id"));
        String typeCard = String.valueOf(params.get("typeCard"));
        String moneyCount = String.valueOf(params.get("moneyCount"));
        String type = String.valueOf(params.get("type"));
        int cardType = Integer.valueOf(typeCard);
        response.put("type", type);
        response.put("moneyCount", moneyCount);
        RedpackageActivityInfoVo redpackageActivityInfoVo = null;
        if (StringUtils.isNotNull(id)) {
            redpackageActivityInfoVo = selectByPrimaryKey(id);
        } else if (StringUtils.isNotNull(openId)) {
            redpackageActivityInfoVo = new RedpackageActivityInfoVo();
            redpackageActivityInfoVo.setOpenId(openId);
            List<RedpackageActivityInfoVo> resultList = getRedpackageActivityBySelective(redpackageActivityInfoVo);
            if (resultList != null && resultList.size() > 0) {
                redpackageActivityInfoVo = resultList.get(0);
            }
        } else {
            response.put("status", "failure");
        }
        if (redpackageActivityInfoVo != null) {
            response.put("cardNum", "error");
            switch (cardType) {
                case 0:
                    if (redpackageActivityInfoVo.getCardBig() > 0) {
                        redpackageActivityInfoVo.setCardBig(redpackageActivityInfoVo.getCardBig() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;
                case 1:
                    if (redpackageActivityInfoVo.getCardRuyi() > 0) {
                        redpackageActivityInfoVo.setCardRuyi(redpackageActivityInfoVo.getCardRuyi() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;
                case 2:
                    if (redpackageActivityInfoVo.getCardYoushan() > 0) {
                        redpackageActivityInfoVo.setCardYoushan(redpackageActivityInfoVo.getCardYoushan() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;
                case 3:
                    if (redpackageActivityInfoVo.getCardHealth() > 0) {
                        redpackageActivityInfoVo.setCardHealth(redpackageActivityInfoVo.getCardHealth() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;
                case 4:
                    if (redpackageActivityInfoVo.getCardHappy() > 0) {
                        redpackageActivityInfoVo.setCardHappy(redpackageActivityInfoVo.getCardHappy() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;
                default:
                    if (redpackageActivityInfoVo.getCardLove() > 0) {
                        redpackageActivityInfoVo.setCardLove(redpackageActivityInfoVo.getCardLove() - 1);
                        response.put("cardNum", "yes");
                    }
                    break;

            }
            if ("yes".equalsIgnoreCase(String.valueOf(response.get("cardNum")))) {
                int num = updateByPrimaryKeySelective(redpackageActivityInfoVo);
                if (num > 0) {
                    if ("1".equalsIgnoreCase(type)) {
                        payRedPackageToUser(params, response, request, session);
                    }
                    String state = String.valueOf(params.get("state"));
                    RedpackageResultsInfoVo vo = new RedpackageResultsInfoVo();
                    vo.setId(IdGen.uuid());
                    vo.setCreateTime(new Date());
                    vo.setDelFlag(0);
                    vo.setType(Integer.valueOf(type));
                    vo.setState(Integer.valueOf(state));
                    vo.setMoneyCount(Double.parseDouble(moneyCount));
                    vo.setOpenId(openId);
                    num = redPackageResultsInfoService.insert(vo);
                    if (num > 0) {
                        response.put("status", "success");
                    }
                }
            }
        }
        return response;
    }

    /**
     * 调用企业支付接口,实现用户提现功能
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> payRedPackageToUser(Map<String, Object> params, Map<String, Object> response, HttpServletRequest request, HttpSession session)
            throws BalanceNotEnoughException, BusinessPaymentExceeption {
        response.put("payStatus", "failure");
        Integer takeCashOut = Integer.parseInt((String) params.get("moneyCount"));
        Float returnMoney = Float.valueOf(takeCashOut) * 100;
        String openid = String.valueOf(params.get("openId"));
        if (null == openid) {
            openid = CookieUtils.getCookie(request, "openId");
        }
        String ip = request.getRemoteAddr();
        String payResult = null;
        try {
            payResult = accountService.payUserAmount(returnMoney, openid, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("return_msg", payResult);
        response.put("amount", returnMoney);
        if ("SUCCESS".equals(payResult.toUpperCase())) {
            Map tokenMap = systemService.getWechatParameter();
            String token = (String) tokenMap.get("token");
            PatientMsgTemplate.withdrawalsSuccess2Wechat(openid, token, "", String.valueOf(returnMoney / 100), DateUtils.DateToStr(new Date(), "date"));
            response.put("payStatus", "success");
        }
        return response;
    }

}