//package com.cxqm.xiaoerke.modules.marketing.web;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
//import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.cxqm.xiaoerke.common.utils.WechatUtil;
//
//
//@Controller
//@RequestMapping(value = "loveMarketing")
//public class LoveMarketingController {
//
//    @Autowired
//    LoveMarketingService loveMarketingService;
//
//    /**
//     * 生成海报
//     * */
//    @RequestMapping(value = "/MarkeImage", method = {RequestMethod.POST, RequestMethod.GET})
//    public @ResponseBody
//    Map<String, Object> MarkeImage(HttpSession session)throws  Exception {
//        Map<String,Object> map=new HashMap<String, Object>();
//        String openid=session.getAttribute("openId").toString();
//        LoveMarketing modle=new LoveMarketing();
//        modle.setOpenid(openid);
//        loveMarketingService.saveLoveMarketing(modle);
//        String id=modle.getId();
//        if(id!=null){
//           Map<String,Object> m= loveMarketingService.getNicknameAndHeadImageByOpenid(openid);
//        }else{
//            map.put("src","addFault");
//            return  map;
//        }
//        return map;
//    }
//
//
//
//
//
//}
