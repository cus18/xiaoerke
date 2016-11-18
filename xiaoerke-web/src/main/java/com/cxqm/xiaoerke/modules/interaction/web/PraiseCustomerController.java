/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "interaction")
public class PraiseCustomerController extends BaseController {

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private ConsultBadEvaluateRemindUserService consultBadEvaluateRemindUserService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private ConsultSessionService consultSessionService ;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    private SystemService systemService = SpringContextHolder.getBean("systemService");

    @RequestMapping(value = "/user/customerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String customerEvaluation(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String openId = UserUtils.getUser().getOpenid();
        return patientRegisterPraiseService.customerEvaluation(params, StringUtils.isNotNull(openId) ? openId : "");
    }

    /**
     * 新版评价
     *
     * @param params
     * @return
     * @author 张博
     * @version 2.0 姜忠阁 添加差评提醒
     */
    @RequestMapping(value = "/user/updateCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String updateCustomerEvaluation(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        int result = patientRegisterPraiseService.updateCustomerEvaluation(params);
        String consultStatus = StringUtils.isNotNull(String.valueOf(params.get("consultStatus")))?String.valueOf(params.get("consultStatus")):"";
        if(StringUtils.isNotNull(consultStatus)){
            if(result > 0 && "wantConsult".equalsIgnoreCase(consultStatus)){
                SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                String tokenId = (String) userWechatParam.get("token");
                String customerId = (String) params.get("id");
                Map registerPraiseInfo = patientRegisterPraiseService.selectCustomerEvaluation(customerId);
                String userId = (String) registerPraiseInfo.get("openid");
                String messageToUser = sysPropertyVoWithBLOBsVo.getPushEvaluateAndConsultToUser();
                if(StringUtils.isNull(messageToUser)){
                    messageToUser = "推送：感谢您的评价，再发下您的问题，咨询医生吧~";
                }
                WechatUtil.sendMsgToWechat(tokenId, userId, messageToUser);
                LogUtils.saveLog("ZXPJ_ZXYS", userId);
            }
            if ("1".equalsIgnoreCase((String)params.get("starNum1")) && result >0) {
                Runnable thread = new SendBadEvaluationThread((HashMap)params);
                threadExecutor.execute(thread);
            }
        }else{
            if ("1".equalsIgnoreCase((String)params.get("starNum1")) && result >0) {
                Runnable thread = new SendBadEvaluationThread((HashMap)params);
                threadExecutor.execute(thread);
            }
        }
        return result + "";
    }
    /**
     * @description 多线程发送差评提醒
     * @author jiangzhongge
     * @date 2016-7-7 11:01:26
     */
    public class SendBadEvaluationThread implements Runnable {
        private HashMap<String, Object> params ;
        public SendBadEvaluationThread(HashMap<String,Object> paramMap){
            this.params = paramMap;
        }
        @Override
        public void run() {
            List<String> openIds = consultBadEvaluateRemindUserService.findConsultRemindUserId();
            String customerId = (String) params.get("id");
            Integer sessionId = Integer.valueOf((String)params.get("sessionId"));
            Map registerPraiseInfo = patientRegisterPraiseService.selectCustomerEvaluation(customerId);
            String userId = (String) registerPraiseInfo.get("openid");
            String csUserId = (String) registerPraiseInfo.get("doctorId");
            ConsultDoctorInfoVo consultDoctorInfoVo = consultDoctorInfoService.getConsultDoctorInfoByUserId(csUserId);
//            Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
            String userName = "";
            String csUserName = "";
            String csUserPhone = "暂无";
            String serviceTime = "";
            /*if (sessionId != null) {
                Query query = new Query(Criteria.where("sessionId").is(sessionId));
                query.with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
                List<ConsultSessionStatusVo> list = consultRecordService.querySessionStatusList(query);
                ConsultSessionStatusVo consultSessionStatusVo = list.get(0);
                csUserName = consultSessionStatusVo.getCsUserName();
                userName = consultSessionStatusVo.getUserName();
                serviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(consultSessionStatusVo.getLastMessageTime());
            } else {
                Query query = new Query(Criteria.where("csUserId").is(csUserId).and("userId").regex(userId));
                query.with(new Sort(Sort.Direction.DESC, "lastMessageTime"));
                List<ConsultSessionStatusVo> list = consultRecordService.querySessionStatusList(query);
                ConsultSessionStatusVo consultSessionStatusVo = list.get(0);
                csUserName = consultSessionStatusVo.getCsUserName();
                userName = consultSessionStatusVo.getUserName();
                serviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(consultSessionStatusVo.getLastMessageTime());
            }*/
            if(sessionId != null){
                ConsultSession consultSession = new ConsultSession();
                consultSession.setUserId(userId);
                consultSession.setCsUserId(csUserId);
                consultSession.setId(sessionId);
                List<ConsultSession> consultSessionList = consultSessionService.selectBySelective(consultSession);
                if(consultSessionList != null && consultSessionList.size() >0){
                    ConsultSession conSession = consultSessionList.get(0);
                    serviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(conSession.getUpdateTime());
//                    userName = StringUtils.isNotNull((String)params.get("userName"))? (String)params.get("userName") :"暂无";
                    userName = StringUtils.isNotNull(conSession.getUserId())?conSession.getUserId() : sessionId.toString();
                    csUserName = StringUtils.isNotNull(consultDoctorInfoVo.getName()) ? consultDoctorInfoVo.getName() : "暂无";
                }
            }else{
                serviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                userName = "未查到";
                csUserName = "暂无";
            }
            String message = "\"first\":{\"value\":\"有用户对咨询服务做出了评价，请及时处理！\",\"color\":\"#173177\"}," +
                    "\"keyword1\":{\"value\":\"" + userName + "\",\"color\":\"#173177\"}," +
                    "\"keyword2\":{\"value\":\"" + csUserPhone + "\",\"color\":\"#173177\"}," +
                    "\"keyword3\":{\"value\":\"" + serviceTime + "\",\"color\":\"#173177\"},";
            if (consultDoctorInfoVo != null) {
                String department = consultDoctorInfoVo.getDepartment();
                if (StringUtils.isNotNull(department)) {
                    message = message + "\"keyword4\":{\"value\":\"" + consultDoctorInfoVo.getDepartment() + csUserName + "\",\"color\":\"#173177\"},";
                } else {
                    message = message + "\"keyword4\":{\"value\":\"" + csUserName + "\",\"color\":\"#173177\"},";
                }
            } else {
                message = message + "\"keyword4\":{\"value\":\"" + csUserName + "\",\"color\":\"#173177\"},";
            }
            message = message + "\"keyword5\":{\"value\":\"" + params.get("content") + "\",\"color\":\"#173177\"}," +
                    "\"remark\":{\"value\":\"\",\"color\":\"#173177\"}";
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String) userWechatParam.get("token");
            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            String templateId =  sysPropertyVoWithBLOBsVo.getBadEvaluateTempleteId();
            if(StringUtils.isNull(templateId)){
                templateId = "xP7QzdilUu1RRTFzVv8krwwMOyv-1pg9l0ABsooub14";    //正式
            }
            String failureMessage = "";
            if (openIds != null && openIds.size() > 0) {
                for (String openId : openIds) {
                   String result = WechatUtil.sendTemplateMsgToUser(tokenId, openId, templateId, message);
                   if("messageOk".equals(result)){
                       continue ;
                   }else{
                       failureMessage = failureMessage + openId +",";
                   }
                }
            }
            if(StringUtils.isNotNull(failureMessage)){
                failureMessage = "以下用户差评提醒消息发送失败："+failureMessage+"请查看！";
                WechatUtil.sendMsgToWechat(tokenId,"o3_NPwuDSb46Qv-nrWL-uTuHiB8U",failureMessage);
            }
        }
    }
    /**
     * 新版评价
     *
     * @param params
     * @return
     * @author 张博
     */
    @RequestMapping(value = "/user/findCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findCustomerEvaluation(@RequestBody Map<String, Object> params, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        String id = params.get("id").toString();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = patientRegisterPraiseService.selectCustomerEvaluation(id);
        session.setAttribute("openId", map.get("openid"));
        String doctorId = map.get("doctorId").toString();
        result.put("evaluation", map);
        Map<String, Object> starInfo = new HashMap<String, Object>();
        starInfo = patientRegisterPraiseService.getCustomerStarInfoById(doctorId);
        Integer sing = Integer.parseInt(patientRegisterPraiseService.getCustomerStarSingById(doctorId).get("startNum").toString()) + 200;
        Integer count = Integer.parseInt(patientRegisterPraiseService.getCustomerStarCountById(doctorId).get("startNum").toString()) + 200;
        float num = (float) sing / count;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s = df.format(num);//返回的是String类型
        Query queryNew = (new Query()).addCriteria(where("csUserId").regex(doctorId));
        List<ConsultSessionStatusVo> resultList = consultRecordService.getConsultSessionStatusVo(queryNew);
        starInfo.put("startNum", s);
        result.put("starInfo", starInfo);
        result.put("serverNum", resultList.size());
        result.put("doctorHeadImage", patientRegisterPraiseService.getDoctorHeadImageURIById(doctorId));
        return result;
    }
}
