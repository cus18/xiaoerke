package com.cxqm.xiaoerke.modules.activity.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.activity.entity.EnglishActivityVo;
import com.cxqm.xiaoerke.modules.activity.service.EnglishActivityService;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 2017/4/12.
 */

@Controller
@RequestMapping(value = "englisactivity")
public class EnglisActivityController {


    @Autowired
    private EnglishActivityService englishActivityService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    SystemService systemService;
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
      String openId = WechatUtil.getOpenId(session, request);
        openId = "oogbDwCLH1_x-KLcQKqlrmUzG2ng";
      if(StringUtils.isNotNull(openId)){
          EnglishActivityVo englishActivityVo = englishActivityService.selectByopenId(openId);
          if(null == englishActivityVo){
              englishActivityVo = new EnglishActivityVo();
              englishActivityVo.setOpenid(openId);
              englishActivityVo.setUpdateTime(new Date());
              englishActivityService.insertSelective(englishActivityVo);
          }
          Map<String,Object> parameter = systemService.getBaoEnglishParameter();
          String token = (String)parameter.get("token");
          String userQRCode = olyGamesService.getUserQRCode(englishActivityVo.getId()+"",token);//二维码
          response.put("market", englishActivityVo.getId());
          response.put("userQRCode",userQRCode);
      }
      return response;
    }


}
