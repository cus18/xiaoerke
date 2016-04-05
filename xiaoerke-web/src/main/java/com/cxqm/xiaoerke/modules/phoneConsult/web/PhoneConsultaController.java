package com.cxqm.xiaoerke.modules.phoneConsult.web;

import com.cxqm.xiaoerke.common.bean.AccessToken;
import com.cxqm.xiaoerke.common.utils.XMLUtil;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.entity.CallAuthen;
import com.cxqm.xiaoerke.modules.consult.entity.CallEstablish;
import com.cxqm.xiaoerke.modules.consult.entity.CallHangup;
import com.cxqm.xiaoerke.modules.consult.entity.CallResponse;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
}
