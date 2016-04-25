package com.cxqm.xiaoerke.modules.phoneConsult.web;

import com.cxqm.xiaoerke.common.utils.XMLUtil;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.sdk.CCPRestSDK;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wangbaowei on 16/3/10.
 * 鉴权控制器
 *
 */

@Controller
@RequestMapping(value ="${xiaoerkePath}/consultPhone/")
public class PhoneConsultaController {

    @Autowired
    private ConsultPhoneService consultPhoneService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConsultPhoneOrderService consultPhoneOrderService;

    /**
     * 鉴权接口
     *
     * */
    @RequestMapping(value = "auth",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    String rongLianAuth(HttpServletRequest request){
        InputStream inStream = null;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");
            Map<String, Object> map = XMLUtil.doXMLParse(result);
            String action = (String)map.get("action");
            if (action.equals("CallAuth")) {
                // 解析呼叫鉴权
                return  consultPhoneService.parseCallAuth(map);
            } else if (action.equals("CallEstablish")) {
                // 解析摘机请求
                return consultPhoneService.parseCallEstablish(map);
            } else if (action.equals("Hangup")) {
                // 解析挂断请求
                return consultPhoneService.parseHangup(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 再次建立通讯接口
     * @param phoneConsultaServiceId 订单主见id
     * @return String 成功与否的返回值
     * */
    @RequestMapping(value = "consultReconnect",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    String consultReconnect(@RequestParam Integer phoneConsultaServiceId){

        Map<String,Object> orderInfo = consultPhoneOrderService.getConsultConnectInfo(phoneConsultaServiceId);
        Integer orderId = (Integer)orderInfo.get("id");
        String userPhone = (String)orderInfo.get("userPhone");
        String doctorPhone = (String)orderInfo.get("doctorPhone");
        Integer conversationLength = (Integer)orderInfo.get("surplusTime")/1000;

//        CCPRestSDK sdk = new CCPRestSDK();
//        sdk.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
//        sdk.setSubAccount("2fa43378da0a11e59288ac853d9f54f2", "0ad73d75ac5bcb7e68fb191830b06d6b");
//        sdk.setAppId("aaf98f8952f7367a0153084e29992035");

        HashMap<String, Object> result = CCPRestSDK.callback(userPhone,doctorPhone,
                "4006237120", "4006237120", null,
                "true", null, orderId+"",
                conversationLength+"", null, "0",
                "1", "10", null);
        String statusCode = (String) result.get("statusCode");
        return statusCode;
    }
}
