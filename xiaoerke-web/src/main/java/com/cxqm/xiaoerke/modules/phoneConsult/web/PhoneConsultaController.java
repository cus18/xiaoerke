package com.cxqm.xiaoerke.modules.phoneConsult.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.XMLUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.sdk.CCPRestSDK;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
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
    private ConsultPhoneOrderService consultPhoneOrderService;

    /**
     * 鉴权接口
     *
     * */
    @RequestMapping(value = "auth",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    String rongLianAuth(HttpServletRequest request){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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
            LogUtils.saveLog("00000109", "电话咨询鉴权" + map);//用户发起微信支付
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
    Map consultReconnect(@RequestParam Integer phoneConsultaServiceId){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> orderInfo = consultPhoneOrderService.getConsultConnectInfo(phoneConsultaServiceId);
        Integer orderId = (Integer)orderInfo.get("id");
        String userPhone = (String)orderInfo.get("userPhone");
        String doctorPhone = (String)orderInfo.get("doctorPhone");
        Long conversationLength = (Long)orderInfo.get("surplusTime")/1000;
        Date updateTime = (Date)orderInfo.get("update_time");
        String statusCode = "111111";
        if(conversationLength>10&&updateTime.getTime()+1000*60*5>new Date().getTime()){
            HashMap<String, Object> result = CCPRestSDK.callback(userPhone,doctorPhone,
                    "01057115120", "01057115120", null,
                    "true", null, orderId+"",
                    conversationLength+"", null, "0",
                    "1", "10", null);
            statusCode = (String) result.get("statusCode");
        }
        resultMap.put("statusCode",statusCode);
        return resultMap;
    }
}
