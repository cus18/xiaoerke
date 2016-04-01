package com.cxqm.xiaoerke.modules.wechat.web;

import com.cxqm.xiaoerke.common.utils.SignUtil;
import com.cxqm.xiaoerke.modules.wechat.service.WechatDoctorCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wangbaowei on 15/11/4.
 * 负责响应宝大夫医生端微信公众平台的请求
 *
 */


@Controller
@RequestMapping(value = "")
public class WechatDoctorController {

    @Autowired
    private WechatDoctorCoreService wechatDoctorCoreService;

    /**
     *用户校验是否是微信服务器发送的请求
     */
    @RequestMapping(value = "/doctor/wxChat", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String wxDoctorChat(HttpServletRequest request) {
        String method = request.getMethod().toUpperCase();
        // 修改服务器配置微信采用get请求方式
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
                respMessage = wechatDoctorCoreService.processDoctorRequest(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return respMessage;
        }
    }
}
