package com.cxqm.xiaoerke.modules.wechat.web;

import com.cxqm.xiaoerke.common.utils.SignUtil;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatPatientCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 15/11/4.
 * 负责响应宝大夫用户端微信公众平台的请求
 *
 */

@Controller
@RequestMapping(value = "")
public class WechatUserController extends BaseController {

    @Autowired
    private WechatPatientCoreService wechatPatientCoreService;

    @Autowired
    private WechatAttentionService wechatAttentionService;


    /**
  *用户校验是否是微信服务器发送的请求
  */
  @RequestMapping(value = "/patient/wxChat", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  String wxPatientChat(HttpServletRequest request,HttpServletResponse response) {
      String method = request.getMethod().toUpperCase();
      if ("GET".equals(method)) {
          // 微信加密签名
          String signature = request.getParameter("signature");
          // 时间戳
          String timestamp = request.getParameter("timestamp");
          // 随机数
          String nonce = request.getParameter("nonce");
          // 随机字符串
          String echostr = request.getParameter("echostr");
          // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
          if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
          }
          return "";
      } else {
          // 调用核心业务类接收消息、处理消息
          String respMessage = null;
          try {
              respMessage = wechatPatientCoreService.processPatientRequest(request,response);
          } catch (IOException e) {
              e.printStackTrace();
          }
          return respMessage;
      }
  }

    /**
     *根据openid查询关注用户信息
     */
    @RequestMapping(value = "/patient/getAttentionInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map getAttentionInfo(HttpSession session, HttpServletRequest request) {
        Map map = new HashMap();
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(WechatUtil.getOpenId(session,request));
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        map.put("attentionInfo",wechatAttentionVo);
        return map;
    }


}
