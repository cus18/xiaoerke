package com.cxqm.xiaoerke.modules.marketing.web;

import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "loveMarketing")
public class LoveMarketingController {

    @Autowired
    LoveMarketingService loveMarketingService;

    @Autowired
    private SystemService systemService;

    /**
     * 生成海报
     * */
    @RequestMapping(value = "/MarkeImage", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    Map<String, Object> MarkeImage(HttpSession session)throws  Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        String openid=session.getAttribute("openId").toString();
        LoveMarketing modle=new LoveMarketing();
        modle.setOpenid(openid);
        loveMarketingService.saveLoveMarketing(modle);
        String id=modle.getId();
        if(id!=null){
           Map<String,Object> m= loveMarketingService.getNicknameAndHeadImageByOpenid(openid);
        }else{
            map.put("src","addFault");
            return  map;
        }
        return map;
    }

    @RequestMapping(value="/addComment",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    Map<String, Object> saveLoveActivityComment(HttpSession session, HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>() ;
        LoveActivityComment loveActivityComment = new LoveActivityComment();
        String openId = session.getAttribute("openId").toString();
        Date nowDate = new Date();
        loveActivityComment.setUserId(session.getAttribute("openId").toString());
        loveActivityComment.setContent(request.getParameter("content"));
        loveActivityComment.setCreateDate(nowDate);
        loveMarketingService.saveLoveActivityComment(loveActivityComment);
        if(loveActivityComment.getId() !=null){
            String loveActivityCommentId = loveActivityComment.getId();
            map.put("status","success");
            return map;
        }else{
            map.put("status","falure");
            return map;
        }
    }

    @RequestMapping(value="/findFirstComment",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody HashMap<String , Object> findOneLoveActivityComment(){
        HashMap<String,Object> response = new HashMap<String, Object>();
        LoveActivityComment  loveActivityComment = loveMarketingService.findLoveActivityComment();
        if(loveActivityComment != null ){
            response.put("content",loveActivityComment);
            return response;
        }else{
            return null ;
        }
    }




}
