package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.CoopConsultUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by jiangzhongge on 2016-7-12.
 */
@Controller
@RequestMapping(value = "consult/cooperate")
public class ConsultH5CoopController {

        @Autowired
        private ConsultRecordService consultRecordService;

        @RequestMapping(value="/saveUserInfo",method = {RequestMethod.POST, RequestMethod.GET})
        public String saveCoopUserInfoByToken(@RequestParam(required= true) String token , Model model){
                String currentUrl = "http://rest.ihiss.com:9000/user/current";   //获取当前登录人信息
                String childrenUrl = "http://rest.ihiss.com:9000/user/children"; //获取当前登录人的孩子信息
                token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
                String access_token = "{'X-Access-Token':'" + token + "'}";
                String method = "GET";
                String dataType = "json";
                String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token,"",2);
                if(str != null){
                        model.addAttribute("currentUser",str);
                        String result = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token,"",2);
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        if(result != null){
                                model.addAttribute("currentChildren",result);
                                JSONObject jsonObject1 = JSONObject.fromObject(result);
                        }
                }
                return "" ;
        }

        @RequestMapping(value = "/getHistoryRecord", method = {RequestMethod.POST, RequestMethod.GET})
        public
        @ResponseBody
        HashMap<String, Object> getUserHistoryRecord(@RequestBody Map<String, Object> params) {
                HashMap<String, Object> response = new HashMap<String, Object>();
                String userId = (String) params.get("userId");
                String dateTime = (String) params.get("dateTime");
                Integer pageSize = (Integer) params.get("pageSize");
                String currentUrl = "http://rest.ihiss.com:9000/user/current";   //获取当前登录人信息
                String token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
                if(StringUtils.isNotNull((String)params.get("token"))){
                        token =  (String)params.get("token");
                }
                String access_token = "{'X-Access-Token':'" + token + "'}";
                String method = "GET";
                String dataType = "json";
                String result = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token, "", 2);
                String imgUrl = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png";
                if(result != null){
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        if(StringUtils.isNotNull((String)jsonObject.get("avatar"))){
                                imgUrl = (String)jsonObject.get("avatar");
                        }
                }
                String docHeaderImg = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png";
                List<ConsultRecordMongoVo> currentUserHistoryRecord = null;
                Date date = null;
                if (dateTime.indexOf("-") != -1) {
                        date = DateUtils.StrToDate(dateTime, "datetime");
                } else if (dateTime.indexOf("/") != -1) {
                        date = DateUtils.StrToDate(dateTime, "xiangang");
                }
                Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("createDate").lt(date)).
                        with(new Sort(Sort.Direction.DESC, "createDate")).limit(pageSize);
                currentUserHistoryRecord = consultRecordService.getCurrentUserHistoryRecord(query);
                List<HashMap<String,Object>> listData = new ArrayList<HashMap<String, Object>>();
                if(currentUserHistoryRecord != null){
                        for(ConsultRecordMongoVo dataVo:currentUserHistoryRecord){
                                HashMap<String,Object> dataMap = new HashMap<String, Object>();
                                if(StringUtils.isNotNull(dataVo.getSenderId())){
                                        if(userId.equals(dataVo.getSenderId())){
                                                dataMap.put("type",dataVo.getType());
                                                dataMap.put("content", dataVo.getMessage());
                                                dataMap.put("dateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dataVo.getCreateDate()));
                                                dataMap.put("senderId",dataVo.getSenderId());
                                                dataMap.put("senderName",dataVo.getSenderName());
                                                dataMap.put("sessionId",dataVo.getSessionId());
                                                dataMap.put("avatar",imgUrl);
                                        }else{
                                                dataMap.put("type",dataVo.getType());
                                                dataMap.put("content", dataVo.getMessage());
                                                dataMap.put("dateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dataVo.getCreateDate()));
                                                dataMap.put("senderId",dataVo.getSenderId());
                                                dataMap.put("senderName",dataVo.getSenderName());
                                                dataMap.put("sessionId",dataVo.getSessionId());
                                                dataMap.put("avatar",docHeaderImg);
                                        }
                                }
                                listData.add(dataMap);
                        }
                        response.put("consultDataList",listData);
                }else{
                        response.put("consultDataList", "");
                }
                return response ;
        }


}
