package com.cxqm.xiaoerke.modules.mutualHelp.web;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;
import com.cxqm.xiaoerke.modules.mutualHelp.service.MutualHelpDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 蛋蛋捐款 controller
 * Created by Administrator on 2016/6/27 0027.
 */
@Controller
@RequestMapping(value = "mutualHelp/donation")
public class MutualHelpDonationController {
    @Autowired
    MutualHelpDonationService service;

    /**
     * 照片墙：我的捐款信息+所有捐款和留言信息
     * @param params
     * @return
     */
    @RequestMapping(value = "/photoWall", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getDetail(HttpServletRequest request, @RequestBody Map<String, Object> params){
        String openId = (String) params.get("openId");
        if(!StringUtils.isNotNull(openId)){
            openId = CookieUtils.getCookie(request, "openId");
        }

        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("openId", openId);
        searchMap.put("userId", (Integer) params.get("userId"));
        searchMap.put("donationType", (Integer) params.get("donationType"));
        return service.getDonatonDetail(searchMap);
    }

    /**
     * 捐款总人次
     * @param params
     * @return
     */
    @RequestMapping(value = "/count", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getManTime(@RequestBody Map<String, Object> params){
        Map<String,Object> response = new HashMap<String, Object>();
        Integer type = (Integer) params.get("donationType");
        response.put("count",service.getCount(type));

        return response;
    }

    /**
     * 捐款总金额
     * @param params
     * @return
     */
    @RequestMapping(value = "/sumMoney", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getSumMoney(@RequestBody Map<String, Object> params){
        Map<String,Object> response = new HashMap<String, Object>();
        Integer type = (Integer) params.get("donationType");
        response.put("count",service.getCount(type));

        return response;
    }


    /**
     * 获取最新一条留言
     * @param params
     * @return
     */
    @RequestMapping(value = "/lastNote", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getLastNote(@RequestBody Map<String, Object> params){
        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("donationType", (Integer) params.get("donationType"));

        return service.getLastNote(searchMap);
    }

    /**
     * 捐款或留言
     * @param params
     * @return
     */
    @RequestMapping(value = "/addNoteAndDonation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> addNoteAndDonation(HttpServletRequest request, @RequestBody Map<String, Object> params){
        String openId = (String) params.get("openId");
        if(!StringUtils.isNotNull(openId)){
            openId = CookieUtils.getCookie(request,"openId");
        }
        Integer money = (Integer) params.get("leaveNote");;
        String leaveNote = (String) params.get("leaveNote");

        Map<String, Object> response = new HashMap<String, Object>();

        if((money == null || money <= 0) && !StringUtils.isNotNull(leaveNote)) {
            response.put("insert", "failed：捐款和留言都为空");
        }else{
            MutualHelpDonation mutualHelpDonation = new MutualHelpDonation();
            mutualHelpDonation.setOpenId(openId);
            mutualHelpDonation.setUserId((String) params.get("userId"));
            mutualHelpDonation.setMoney(money);
            mutualHelpDonation.setLeaveNote(leaveNote);
            mutualHelpDonation.setDonationType((Integer) params.get("donationType"));
            int n = service.saveNoteAndDonation(mutualHelpDonation);
            if (n > 0) {
                response.put("insert", "success");
            } else {
                response.put("insert", "failed");
            }
        }

        return response;
    }

    /**
     * 支付页面openid
     */
    @RequestMapping(value = "/getOpenid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getOpenid(HttpServletRequest request,HttpSession session){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        if(openid==null||openid.equals("")){
            resultMap.put("openid","none");
            return resultMap;
        }
        resultMap.put("openid",openid);
        return resultMap;
    }
}
