package com.cxqm.xiaoerke.modules.activity.web;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangbaowei on 17/3/8.
 */

@Controller
@RequestMapping(value = "/cheakHusband")
public class CheakHusbandController {

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 检测用户是否关注平台
     * @param
     * @return map
     */
    @RequestMapping(value = "/isAttention", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> isAttention(HttpServletRequest request,  HttpSession session,@RequestParam String type){
        String openid = WechatUtil.getOpenId(session,request);
        Map<String,Object> responseMap = new HashMap<String, Object>();
        WechatAttention attention = wechatAttentionService.getAttentionByOpenId(openid);
        if(attention == null || attention.getDate() == null){//新用户
            responseMap.put("status","notattention");
            redisTemplate.opsForValue().set("husbandCheack"+openid, type,10, TimeUnit.DAYS);
        }else {//老用户
            responseMap.put("status","isattention");
        }
        return responseMap;
    }


//    /**
//     * 用于关注后展示测试结果
//     * @param
//     * @return map
//     */
//    @RequestMapping(value = "/showCheackInfo", method = {RequestMethod.POST, RequestMethod.GET})
//    public
//    @ResponseBody
//    Map<String,Object> showCheackInfo(HttpServletRequest request,  HttpSession session){
//        Map<String,Object> responseMap = new HashMap<String, Object>();
//        String openid = WechatUtil.getOpenId(session,request);
//        String type = (String) redisTemplate.opsForValue().get("husbandCheack"+openid);
//        responseMap.put("type",type);
//        return responseMap;
//    }
//
//
//    /**
//     * 保存评测结果
//     * */
//    @RequestMapping(value = "/saveCheackInfo", method = {RequestMethod.POST, RequestMethod.GET})
//    public
//    @ResponseBody
//    Map<String,Object> saveCheackInfo(HttpServletRequest request,  HttpSession session,String type){
//        Map<String,Object> responseMap = new HashMap<String, Object>();
//        String openid = WechatUtil.getOpenId(session,request);
//        redisTemplate.opsForValue().set("husbandCheack"+openid, type,10, TimeUnit.DAYS);
//        return responseMap;
//    }
}
