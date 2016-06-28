/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultBadEvaluateRemindUserService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    @RequestMapping(value = "/user/customerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String customerEvaluation(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String openId = UserUtils.getUser().getOpenid();
        return patientRegisterPraiseService.customerEvaluation(params, StringUtils.isNotNull(openId)?openId:"");
    }
    
    /**
     * 新版评价
     * @author 张博
     * @param params
     * @return
     * @version 2.0 姜忠阁 添加差评提醒
     */
    @RequestMapping(value = "/user/updateCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String updateCustomerEvaluation(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        int result =  patientRegisterPraiseService.updateCustomerEvaluation(params);
        if(result > 0){
            List<String> openIds = consultBadEvaluateRemindUserService.findConsultRemindUserId();
            String customerId = (String)params.get("customerId");
            Integer sessionId = null ;
            String userName = "";
            String csUserName = "";
            String csUserPhone ="暂无";
            String serviceTime = "";
            if(StringUtils.isNotNull(customerId)){
                sessionId = Integer.valueOf(customerId);
                RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                if(richConsultSession != null){
                    csUserName = richConsultSession.getCsUserName();
                    userName = richConsultSession.getUserName();
                    Query query = new Query(Criteria.where("sessionId").is(sessionId));
                    List<ConsultSessionStatusVo> list = consultRecordService.querySessionStatusList(query);
                    ConsultSessionStatusVo consultSessionStatusVo = list.get(0);
                    serviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(consultSessionStatusVo.getLastMessageTime());
                }
            }
            String message= "\"first\":{\"value\":\"有用户对咨询服务做出了评价，请及时处理！\",\"color\":\"#173177\"}," +
                    "\"keyword1\":{\"value\":\""+userName+"\",\"color\":\"#173177\"}," +
                    "\"keyword2\":{\"value\":\""+csUserPhone+"\",\"color\":\"#173177\"}," +
                    "\"keyword3\":{\"value\":\""+serviceTime+"\",\"color\":\"#173177\"}," +
                    "\"keyword4\":{\"value\":\""+csUserName+"\",\"color\":\"#173177\"}," +
                    "\"keyword5\":{\"value\":\""+params.get("content")+"\",\"color\":\"#173177\"}," +
                    "\"remark\":{\"value\":\"点击查看详情\",\"color\":\"#173177\"}";
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            String tokenId = (String)userWechatParam.get("token");
            String templateId = "xP7QzdilUu1RRTFzVv8krwwMOyv-1pg9l0ABsooub14";
            if(openIds !=null && openIds.size() >0){
                for(String openId:openIds){
                    WechatUtil.sendTemplateMsgToUser(tokenId,openId,templateId,message);
                }
            }
        }
        return result+"";
    }

    /**
     * 新版评价
     * @author 张博
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/findCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findCustomerEvaluation(@RequestBody Map<String, Object> params, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
    	String id=params.get("id").toString();
    	Map<String, Object> result =new HashMap<String, Object>();
    	Map<String, Object> map=patientRegisterPraiseService.selectCustomerEvaluation(id);
        session.setAttribute("openId",map.get("openid"));
    	String doctorId=map.get("doctorId").toString();
    	result.put("evaluation", map);
        Map<String, Object> starInfo =new HashMap<String, Object>();
        starInfo=patientRegisterPraiseService.getCustomerStarInfoById(doctorId);
        Integer sing=Integer.parseInt(patientRegisterPraiseService.getCustomerStarSingById(doctorId).get("startNum").toString())+200;
        Integer count=Integer.parseInt(patientRegisterPraiseService.getCustomerStarCountById(doctorId).get("startNum").toString())+200;
        float num= (float)sing/count;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s = df.format(num);//返回的是String类型
        starInfo.put("startNum",s);
    	result.put("starInfo", starInfo);
        result.put("doctorHeadImage",patientRegisterPraiseService.getDoctorHeadImageURIById(doctorId));
        return result;
    }
    
}
