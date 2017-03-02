package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.account.exception.BalanceNotEnoughException;
import com.cxqm.xiaoerke.modules.account.exception.BusinessPaymentExceeption;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageActivityInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-2-20.
 */
public interface RedPackageActivityInfoService {

    int deleteByPrimaryKey(String id);

    int insert(RedpackageActivityInfoVo record);

    int insertSelective(RedpackageActivityInfoVo record);

    RedpackageActivityInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RedpackageActivityInfoVo record);

    int updateByPrimaryKey(RedpackageActivityInfoVo record);

    List<RedpackageActivityInfoVo> getRedpackageActivityBySelective(RedpackageActivityInfoVo vo);

    HashMap<String,Object> chooseCardsAndRewards(Map<String, Object> params ,HashMap<String, Object> response, HttpSession session, HttpServletRequest request) throws BusinessPaymentExceeption, BalanceNotEnoughException;

    int getRedPackageActivityTatalCount();

    Map<String,Object> payRedPackageToUser(Map<String, Object> params, Map<String, Object> response, HttpServletRequest request, HttpSession session) throws BalanceNotEnoughException, BusinessPaymentExceeption;

    List<RedpackageActivityInfoVo> getLastOneRedPackageActivity();
}
