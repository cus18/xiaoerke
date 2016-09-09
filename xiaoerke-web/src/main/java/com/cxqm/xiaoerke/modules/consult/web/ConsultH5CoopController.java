package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.CoopConsultUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultEvaluateCoopVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultEvaluateCoopService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 合作方controller
 * Created by jiangzhongge on 2016-7-12.
 */
@Controller
@RequestMapping(value = "consult/cooperate")
public class ConsultH5CoopController {

        @Autowired
        private ConsultRecordService consultRecordService;

        @Autowired
        private ConsultEvaluateCoopService consultEvaluateCoopService ;

        @RequestMapping(value="/saveUserInfo",method = {RequestMethod.POST, RequestMethod.GET})
        public @ResponseBody String saveCoopUserInfoByToken(@RequestParam(required= true) String token , Model model){
                /*String currentUrl = "http://rest.tx2010.com/user/current";   //获取当前登录人信息
                String childrenUrl = "http://rest.tx2010.com/user/children"; //获取当前登录人的孩子信息
                token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
                String access_token = "{'X-Access-Token':'" + token + "'}";
                String method = "GET";
                String dataType = "json";*/
                /*String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token,"",2);
                if(str != null){
                        model.addAttribute("currentUser",str);
                        String result = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token,"",2);
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        if(result != null){
                                model.addAttribute("currentChildren",result);
                                JSONObject jsonObject1 = JSONObject.fromObject(result);
                        }
                }*/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",123);
                jsonObject.put("userId","123test");
                jsonObject.put("csUserId","12345");
                jsonObject.put("evaLevel",0);
                jsonObject.put("suggestMsg","不错哦");
                jsonObject.put("evaDate","2016-08-24 12:30:20");
                jsonObject.put("source","wxcxqm");
                consultEvaluateUserByCoop(jsonObject);
                return "11111" ;
        }

        @RequestMapping(value = "/getHistoryRecord", method = {RequestMethod.POST, RequestMethod.GET })
        public
        @ResponseBody
        HashMap<String, Object> getUserHistoryRecord(@RequestBody Map<String, Object> params) {
                HashMap<String, Object> response = new HashMap<String, Object>();
                String userId = (String) params.get("userId");
                String dateTime = (String) params.get("dateTime");
                Integer pageSize = (Integer) params.get("pageSize");
                String currentUrl = Global.getConfig("COOP_WJY_URL");
                if(StringUtils.isNull(currentUrl)){
                        currentUrl = "http://rest.tx2010.com/user/current";   //微家园获取当前登录人信息
                }
                String imgUrl = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png";
                String docHeaderImg = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png";
//                String token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
                if(params.containsKey("token") && StringUtils.isNotNull(String.valueOf(params.get("token")))){
                        String token =  (String)params.get("token");
                        String access_token = "{'X-Access-Token':'" + token + "'}";
                        String method = "GET";
                        String dataType = "json";
                        String result = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, access_token, "", 2);
                        if(result != null){
                                JSONObject jsonObject = JSONObject.fromObject(result);
                                if(StringUtils.isNotNull((String)jsonObject.get("avatar"))){
                                        imgUrl = (String)jsonObject.get("avatar");
                                }
                        }
                }
                List<ConsultRecordMongoVo> currentUserHistoryRecord = null;
                Date date = null;
                if (dateTime.indexOf("-") != -1) {
                        date = DateUtils.StrToDate(dateTime, "datetime");
                } else if (dateTime.indexOf("/") != -1) {
                        date = DateUtils.StrToDate(dateTime, "xianggang");
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

        @RequestMapping(value = "/baohuquan",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json")
        public @ResponseBody JSONObject theInterfaceOfBHQ(@RequestBody Map params){
                JSONObject jsonObject = new JSONObject();
                if(params != null){
                        if(params.containsKey("action") && "evaluteDocker".equalsIgnoreCase(String.valueOf(params.get("action")))){
                              jsonObject =  this.consultEvaluateUserByCoop(params);
                        }else if(params.containsKey("action") && "getConsultDataByCoop".equalsIgnoreCase(String.valueOf(params.get("action")))){
                              jsonObject = this.getConsultDataByCoop(params);
                        }
                }

                return jsonObject ;
        }

        /**
         *  保护圈聊天记录抓取
         *  jiangzg 2016-8-23 12:00:37
         *
         */
        @RequestMapping(value="/getConsultDataByCoop",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json")
        public @ResponseBody JSONObject getConsultDataByCoop(@RequestBody Map params){

                DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
                String source = String.valueOf(params.get("source"));
                String startTime = String.valueOf(params.get("startTime"));
                String endTime = String.valueOf(params.get("endTime"));
                String secCode = String.valueOf(params.get("secCode"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int currentYear = calendar.get(calendar.YEAR)-2000;
                int currentMonth = calendar.get(calendar.MONTH)+1;
                int currentDate = calendar.get(calendar.DAY_OF_MONTH);
                int currentHour = calendar.get(calendar.HOUR_OF_DAY);

                JSONObject jsonObject = new JSONObject();
                String secCodeNow = "" ;
                if(StringUtils.isNotNull(source)){
                        if(currentMonth < 10){
                                secCodeNow = currentDate +"0"+currentMonth +currentYear + source;
                        }else{
                                secCodeNow = currentDate + currentMonth +currentYear + source;
                        }
                }else{
                        if(currentMonth < 10){
                                secCodeNow = currentDate +"0"+currentMonth +currentYear;
                        }else{
                                secCodeNow += currentDate + currentMonth +currentYear;
                        }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long differTme = 0;
                try {
                        Date start = sdf.parse(startTime);
                        Date end = sdf.parse(endTime);
                        long startDate = start.getTime();
                        long endDate = end.getTime();
                        differTme = endDate - startDate ;
                } catch (ParseException e) {
                        e.printStackTrace();
                }

                if(StringUtils.isNotNull(source) && "COOP_BHQ".equalsIgnoreCase(source)){
                        if(currentHour > 0 && currentHour < 6){
                                if(secCodeNow.equalsIgnoreCase(secCode)){
                                        if(differTme > 1000*60*60*2){
                                                jsonObject.put("status","failure");
                                                jsonObject.put("code",1);
                                                jsonObject.put("info","您好，您请求的数据时间间隔超过2小时！");
                                        }else{
                                                JSONArray jsonArray = new JSONArray();
                                                try{
                                                        /**
                                                         * 加入聊天记录提取代码
                                                         */
                                                        Query query = new Query(Criteria.where("createDate").exists(true).andOperator(Criteria.where("createDate").gte(10), Criteria.where("createDate").lte(40)).and("source").is("h5bhq"));
                                                        query.with(new Sort(Sort.Direction.DESC,"createDate"));
                                                        List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.getCurrentUserHistoryRecord(query);
                                                        if(consultRecordMongoVos != null && consultRecordMongoVos.size() >0){
                                                                for(ConsultRecordMongoVo consultRecordMongoVo : consultRecordMongoVos){
                                                                        JSONObject data = new JSONObject();
                                                                        data.put("id",consultRecordMongoVo.getSessionId());
                                                                        data.put("userName",consultRecordMongoVo.getNickName());
                                                                        data.put("userId",consultRecordMongoVo.getUserId());
                                                                        data.put("csUserId",consultRecordMongoVo.getCsUserId());
                                                                        data.put("senderId",consultRecordMongoVo.getSenderId());
                                                                        data.put("message",consultRecordMongoVo.getMessage());
                                                                        data.put("type",consultRecordMongoVo.getType());
                                                                        data.put("createDate",consultRecordMongoVo.getCreateDate());
                                                                        jsonArray.add(data);
                                                                }
                                                        }
                                                        jsonObject.put("status","success");
                                                        jsonObject.put("code",0);
                                                        jsonObject.put("info","数据提取成功！");
                                                        jsonObject.put("datalist",jsonArray);
                                                }catch (Exception ex){
                                                        jsonObject.put("status","failure");
                                                        jsonObject.put("code",1);
                                                        jsonObject.put("info","连接请求超时！");
                                                        ex.getStackTrace();
                                                }

                                        }
                                }else{
                                        jsonObject.put("status","failure");
                                        jsonObject.put("code",1);
                                        jsonObject.put("info","您好，请输入正确的安全密钥，谢谢！");
                                }
                        }else{
                                jsonObject.put("status","failure");
                                jsonObject.put("code",1);
                                jsonObject.put("info","您好，请不要在咨询高峰期间提取数据，请在凌晨0点到6点之间提取，谢谢！");
                        }
                }
                return jsonObject ;
        }

        /**
         *  保护圈评价消息接口
         *  jiangzg 2016-8-23 12:00:37
         *
         */
        @RequestMapping(value="/consultEvaluateUserByCoop",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json")
        public @ResponseBody JSONObject consultEvaluateUserByCoop(@RequestBody Map params){
                JSONObject jsonObject = new JSONObject();
                int sessionId = StringUtils.isNotNull(String.valueOf(params.get("sessionId")))?Integer.valueOf(String.valueOf(params.get("sessionId"))):0;
                String userId = String.valueOf(params.get("userId"));
                String csUserId = "";
                if(params.containsKey("csUserId")){
                       csUserId = String.valueOf(params.get("csUserId"));
                }
                int evaLevel = StringUtils.isNotNull(String.valueOf(params.get("socre")))?Integer.valueOf(String.valueOf(params.get("socre"))):0;
                //0：代表非常满意  1：代表一般 2：代表不满意
                String suggestMsg = String.valueOf(params.get("suggestMsg"));
                String evaDate = String.valueOf(params.get("evaDate"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date evaluateDate = null ;
                if(StringUtils.isNotNull(evaDate)){
                        try {
                                evaluateDate = sdf.parse(evaDate);
                        } catch (ParseException e) {
                                e.printStackTrace();
                        }
                }else{
                        evaluateDate = new Date();
                }
                String source = String.valueOf(params.get("source"));
                int result = 0 ;
                /**
                 *  加入评价信息添加
                 */
                ConsultEvaluateCoopVo consultEvaluateCoopVo = new ConsultEvaluateCoopVo();
                consultEvaluateCoopVo.setCsUserId(csUserId);
                consultEvaluateCoopVo.setSource(source);
                consultEvaluateCoopVo.setCreateDate(new Date());
                consultEvaluateCoopVo.setEvaluateDate(evaluateDate);
                consultEvaluateCoopVo.setEvaluateLevel(evaLevel);
                consultEvaluateCoopVo.setSessionId(sessionId);
                consultEvaluateCoopVo.setSuggestMessage(suggestMsg);
                consultEvaluateCoopVo.setUserId(userId);
                result = consultEvaluateCoopService.addConsultEvaluateCoops(consultEvaluateCoopVo);
                if(result > 0){
                        jsonObject.put("status","success");
                        jsonObject.put("error_code",0);
                        jsonObject.put("info","数据接收成功！");
                }else{
                        jsonObject.put("status","failure");
                        jsonObject.put("error_code","1");
                        jsonObject.put("error_message","数据发送失败！");
                }
                return jsonObject;
        }
}
