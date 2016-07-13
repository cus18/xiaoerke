package com.cxqm.xiaoerke.modules.marketing.web;

import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@RequestMapping(value = "loveMarketing")
public class LoveMarketingController {

    @Autowired
    LoveMarketingService loveMarketingService;

    @Autowired
    private SystemService systemService;



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

    @RequestMapping(value="/visitPage",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Map<String , Object> visitPage(HttpSession session){
                String openid=session.getAttribute("openId").toString();
//        String openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
//        session.setAttribute("openId","o3_NPwrrWyKRi8O_Hk8WrkOvvNOk");
        Map<String , Object> m= loveMarketingService.getUserInfo(openid);
        if(m==null){
            m=new HashMap<String, Object>();
            m.put("fault","null");
            return  m;
        }
        m.put("headImage",loveMarketingService.getNicknameAndHeadImageByOpenid(openid));
        return  m;
    }

    @RequestMapping(value="/getAllLoverHeart",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Map<String , Object> getAll(HttpSession session){
       List<Map<String,Object>> list=loveMarketingService.getAll();
        List<Map<String,Object>> userList=new ArrayList<Map<String, Object>>();
        for(int i=0;i<list.size();i++){
            String openid=list.get(i).get("openid").toString();
            Map<String,Object> user=loveMarketingService.getNicknameAndHeadImageByOpenid(openid);
            String subscribe=user.get("subscribe").toString();
            if(subscribe.equals(0)){
                continue;
            }else{
                userList.add(user);
                if(userList.size()==5){
                    break;
                }
            }
        }
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("countMoney",loveMarketingService.countMoney());
        result.put("counts", list.size());
        result.put("userImageList",userList);
        return result;

    }







}
