package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultBadEvaluateRemindUserVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultBadEvaluateRemindUserService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.impl.AttentionMongoDBServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by jiangzhongge on 2016-6-24.
 */
@Controller(value="ConsultEvaluateRemindController")
@RequestMapping(value="${adminPath}/consult/remindUser")
public class ConsultEvaluateRemindController {
        @Autowired
        private ConsultBadEvaluateRemindUserService consultBadEvaluateRemindUserService;

        @Autowired
        private AttentionMongoDBServiceImpl attentionMongoDBService;

        @RequestMapping(value="addRemindUser" , method = {RequestMethod.POST, RequestMethod.GET})
        public @ResponseBody
        HashMap<String, Object> addRemindUser(HttpServletRequest request, HttpServletResponse response){

                HashMap<String,Object> responseResult = new HashMap<String, Object>();
                ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo = new ConsultBadEvaluateRemindUserVo();
                consultBadEvaluateRemindUserVo.setOpenId(request.getParameter("openId"));
                consultBadEvaluateRemindUserVo.setCsUserName(request.getParameter("csUserName"));
                consultBadEvaluateRemindUserVo.setCreateDate(new Date());
                consultBadEvaluateRemindUserVo.setDelFlag("0");
                List<ConsultBadEvaluateRemindUserVo> result  =  consultBadEvaluateRemindUserService.findConsultRemindUserByMoreConditions(consultBadEvaluateRemindUserVo);
                if(result != null && result.size() >0){
                        responseResult.put("status","failure");
                        responseResult.put("data","该用户已经添加，请勿重复添加！");
                }else{
                        int returnResult = consultBadEvaluateRemindUserService.saveNoticeCSUser(consultBadEvaluateRemindUserVo);
                        if(returnResult > 0){
                                responseResult.put("status","success");
                                responseResult.put("data","用户添加成功！");
                        }else{
                                responseResult.put("status","failure");
                                responseResult.put("data","用户添加失败！");
                        }
                }
                return responseResult;
        }

        @RequestMapping(value="updateRemindUser" , method = {RequestMethod.POST, RequestMethod.GET})
        public @ResponseBody
        HashMap<String, Object> updateRemindUser(HttpServletRequest request, HttpServletResponse response){
                HashMap<String,Object> responseResult = new HashMap<String, Object>();
                ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo = new ConsultBadEvaluateRemindUserVo();
                consultBadEvaluateRemindUserVo.setOpenId(request.getParameter("openId"));
                consultBadEvaluateRemindUserVo.setCsUserName(request.getParameter("csUserName"));
                consultBadEvaluateRemindUserVo.setDelFlag("1");
                int result =consultBadEvaluateRemindUserService.modifyNoticeCSUser(consultBadEvaluateRemindUserVo);
                if(result > 0){
                        responseResult.put("status","success");
                }else{
                        responseResult.put("status","failure");
                }
                return responseResult;
        }

        /**
         * 从咨询通知人表中查询是否已添加，添加了提示已添加，未添加则添加
         * @param
         * @return
         */
        @RequestMapping(value="findRemindUser" , method = {RequestMethod.POST, RequestMethod.GET})
        public String findRemindUser(HttpServletRequest request, HttpServletResponse response, Model model){
                ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo = new ConsultBadEvaluateRemindUserVo();
                if(request.getParameter("openId") != null && StringUtils.isNotNull(request.getParameter("openId"))){
                        consultBadEvaluateRemindUserVo.setOpenId(request.getParameter("openId"));
                }else{
                        consultBadEvaluateRemindUserVo.setOpenId("");
                }
                if(request.getParameter("csUserName") != null && StringUtils.isNotNull(request.getParameter("csUserName"))){
                        consultBadEvaluateRemindUserVo.setCsUserName(request.getParameter("csUserName"));
                }else{
                        consultBadEvaluateRemindUserVo.setCsUserName("");
                }
//                List responseResult = new ArrayList();
                List<ConsultBadEvaluateRemindUserVo> result= consultBadEvaluateRemindUserService.findConsultRemindUserByMoreConditions(consultBadEvaluateRemindUserVo);
                if(result != null && result.size() > 0){
                        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                        for(ConsultBadEvaluateRemindUserVo consultBadEvaluateRemindUserVo1 : result){
                                HashMap<String,String> getData =new HashMap();
                                getData.put("id",consultBadEvaluateRemindUserVo1.getId().toString());
                                getData.put("csUserName",consultBadEvaluateRemindUserVo1.getCsUserName());
                                getData.put("openId",consultBadEvaluateRemindUserVo1.getOpenId());
                                getData.put("createDate",simpleDateFormat.format(consultBadEvaluateRemindUserVo1.getCreateDate()));
                                getData.put("delFlag",consultBadEvaluateRemindUserVo1.getDelFlag());
                                responseResult.add(getData);
                        }*/
                        model.addAttribute("consultBadEvaluateRemindUserVoList",result);
                }else{
                        model.addAttribute("consultBadEvaluateRemindUserVoList",result);
                }
                return "modules/consult/badEvoluateNotice";
        }

        /**
         * 从sys_attention中查询咨询通知人，已关注宝大夫
         * @param params
         * @return
         */
        @RequestMapping(value="getCSUserByCondition" , method = {RequestMethod.GET ,RequestMethod.POST})
        public @ResponseBody HashMap<String,Object> getCSUserByConditions(@RequestBody Map<String, Object> params){
                HashMap<String,Object> reponse = new HashMap<String, Object>();
                WechatAttention wechatAttention = new WechatAttention();
                if(params.get("openId") != null && StringUtils.isNotNull((String) params.get("openId"))){
                        wechatAttention.setOpenid((String)params.get("openId"));
                }else{
                        wechatAttention.setOpenid("");
                }
                if(params.get("csUserName") != null && StringUtils.isNotNull((String)params.get("csUserName"))){
                        wechatAttention.setNickname((String)params.get("csUserName"));
                }else{
                        wechatAttention.setNickname("");
                }
                Query query= new Query(Criteria.where("openid").regex(wechatAttention.getOpenid()).and("nickname").regex(wechatAttention.getNickname()));
                List<WechatAttention> list = attentionMongoDBService.queryList(query);
                List result =new ArrayList();
                if(list != null && list.size() > 0){
                        for(int i = 0;i< list.size(); i++){
                                HashMap<String,Object>  data = new HashMap<String, Object>();
                                data.put("csUserName",list.get(i).getNickname());
                                data.put("openId",list.get(i).getOpenid());
                                data.put("createDate",list.get(i).getDate());
                                result.add(data);
                        }
                        reponse.put("status","success");
                        reponse.put("data",result);
                }else{
                        reponse.put("status","failure");
                }
                return reponse;
        }

        @RequestMapping(value="deleteRemindUser" , method = {RequestMethod.POST, RequestMethod.GET})
        public @ResponseBody
        HashMap<String, Object> deleteRemindUser(@RequestBody Map<String, Object> params){
                HashMap<String,Object> response = new HashMap<String, Object>();
                return response;
        }
}
