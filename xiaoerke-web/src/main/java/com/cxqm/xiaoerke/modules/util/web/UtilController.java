/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.util.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.plan.entity.HealthPlanAddItemVo;
import com.cxqm.xiaoerke.modules.plan.service.PlanMessageService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 工具 Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "util")
public class UtilController extends BaseController {

    @Autowired
    AccountService accountService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private PlanMessageService planMessageService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultSessionService consultSessionService ;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    // jiangzhongge add
    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");
    /**
     * 检测用户是否绑定登陆的接口
     * <p/>
     * response:
     * {
     * "status":"1"
     * }
     * //status为1表示用户已经绑定注册，0表示用户没有绑定注册
     */
    @RequestMapping(value = "/checkBind", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkBind(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String, Object> response = new HashMap<String, Object>();
        //判断用户是否绑定了手机号，如果没有绑定，将让用户跳转到手机号的绑定页面
        User user = UserUtils.getUser();
        String userId = user.getId();
        response.put("accountFund", 0);
        if (!"null".equals(user)) {
            //账户余额
            Float account = accountService.accountFund(userId);
            response.put("accountFund", account / 100);
        }

        //订单生成时间
        if (null != params.get("patient_register_service_id")) {
            Date createDate = patientRegisterService.getOrderCreateDate((String) params.get("patient_register_service_id"));
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(createDate);
            rightNow.add(Calendar.MINUTE, +30);//日期减1年
            createDate = rightNow.getTime();
            response.put("createDate", createDate.getTime());
        }

        String BondSwitch = Global.getConfig("webapp.BondSwitch");
        Boolean Bl = new Boolean(BondSwitch);
        response.put("bondSwitch", Bl);
        if (userId == null) {
            response.put("status", "0");
            return response;
        } else {
            response.put("status", "1");
            return response;
        }
    }


    /**
     * 根据手机号，获取验证码，验证码将根据手机号，下推至用户手机
     * <p/>
     * params:{"userPhone":"13601025662"}
     * <p/>
     * response:
     * {
     * "code":"35678",
     * "status":"1"
     * }
     * //status为1表示获取验证码成功，为0表示获取验证码失败
     */
    @RequestMapping(value = "/user/getCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getUserCode(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String userPhone = (String) params.get("userPhone");
        return utilService.sendIdentifying(userPhone);
    }

    @RequestMapping(value = "/user/recordHealthPlanAddItem", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> recordHealthPlanAddItem(@RequestBody Map<String, Object> params,
                                                HttpServletRequest request,HttpSession session) throws Exception{
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String openId = WechatUtil.getOpenId(session, request);
        String addValue = (String) params.get("addValue");
        HashMap<String ,Object> response = new HashMap<String,Object>();
        User user = UserUtils.getUser();
        if(user.getLoginName()!=null||openId!=null){
            HealthPlanAddItemVo healthPlanAddItemVo = new HealthPlanAddItemVo();
            healthPlanAddItemVo.setTelephone(user.getPhone());
            if(user.getOpenid()!=null){
                healthPlanAddItemVo.setOpenid(user.getOpenid());
            }else{
                healthPlanAddItemVo.setOpenid(openId);
            }
            healthPlanAddItemVo.setAddValue(addValue);
            planMessageService.insertHealthPlanAddItem(healthPlanAddItemVo);
            response.put("result","success");
        }else{
            response.put("result","failure");
        }

        return response;
    }

    @RequestMapping(value = "/user/findHealthPlanAddItem", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    List<HealthPlanAddItemVo> findHealthPlanAddItem(HttpServletRequest request,HttpSession session) throws Exception {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        String openId = WechatUtil.getOpenId(session, request);
        User user = UserUtils.getUser();
        if(user.getLoginName()!=null||openId!=null){
            HealthPlanAddItemVo healthPlanAddItemVo = new HealthPlanAddItemVo();
            healthPlanAddItemVo.setTelephone(user.getPhone());
            if(user.getOpenid()!=null){
                healthPlanAddItemVo.setOpenid(user.getOpenid());
            }else{
                healthPlanAddItemVo.setOpenid(openId);
            }
            List<HealthPlanAddItemVo> healthPlanAddItemVos = planMessageService.findHealthPlanAddItem(healthPlanAddItemVo);
            return healthPlanAddItemVos;
        }else{
            return null;
        }
    }

    /**
     * 根据手机号，获取验证码，验证码将根据手机号，下推至用户手机
     * <p/>
     * params:{"userPhone":"13601025662"}
     * <p/>
     * response:
     * {
     * "code":"35678",
     * "status":"1"
     * }
     * //status为1表示获取验证码成功，为0表示获取验证码失败
     */
    @RequestMapping(value = "/doctor/getCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getDoctorCode(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String userPhone = String.valueOf(params.get("userPhone"));
        return utilService.sendIdentifying(userPhone);
    }


    /**
     * 用户登出操作
     */
    @RequestMapping(value = "/logOut", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String logOut() {
        UserUtils.getSubject().logout();
        return "success";
    }

    /**
     * 供前台调用 保存页面点击的日志文件
     */
    @SystemControllerLog(description = "")
    @RequestMapping(value = "/recordLogs", method = {RequestMethod.GET, RequestMethod.POST},produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String recordLogs(HttpServletRequest request) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        try {
            String logContent = URLDecoder.decode(request.getParameter("logContent"), "UTF-8");
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 用户登出操作
     */
    @RequestMapping(value = "/getOpenid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getOpenid(HttpServletRequest request,HttpSession session){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
        if(openid==null||openid.equals("")){
            resultMap.put("openid","none");
            return resultMap;
        }
        resultMap.put("openid", openid);
        return resultMap;
    }

    /**
     * 读取系统配置参数 delang
     */
    @RequestMapping(value = "/getConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getConfig(HttpServletRequest request,HttpSession session){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        //可以公开的系统参数
        SysPropertyVoWithBLOBsVo publicSystemInfo = new SysPropertyVoWithBLOBsVo();
        publicSystemInfo.setFirstAddress(sysPropertyVoWithBLOBsVo.getFirstAddress());
        publicSystemInfo.setSecondAddress(sysPropertyVoWithBLOBsVo.getSecondAddress());
        publicSystemInfo.setInviteUrl(sysPropertyVoWithBLOBsVo.getInviteUrl());
        resultMap.put("publicSystemInfo",publicSystemInfo);
        return resultMap;
    }

    @RequestMapping(value = "/getDoctorConsultData", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getDoctorConsultData(@RequestBody Map param){

        return null ;
    }
    /**
     * 清理redis、mongo、mysql垃圾数据
     * 2016-10-12 15:51:46 jiangzg add
     */
    @RequestMapping(value = "/clearRedisData", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> clearRedisData(HttpServletRequest request,HttpSession session){
        //o3_NPwmmMGpZxC5eS491_JzFRHtU
        Map<String, Object> result = new HashMap<String, Object>();
        List dataList = new ArrayList();          //删除的数据
        List<Object> consultSessions = sessionRedisCache.getConsultSessionsByKey();
        List<Object> sessionIds = sessionRedisCache.getSessionIdByKey();
        List<Integer> sessionIdList = null;            //redis 所有sessionID
        if(sessionIds != null && sessionIds.size() >0){
            sessionIdList = new ArrayList<Integer>();
            for (Object o : sessionIds) {
                Integer sessionId = (Integer) o;
                sessionIdList.add(sessionId);
            }
        }
        Date nowDate = new Date();
        HashMap<Object,Object> dataMap = new HashMap<Object,Object>();
        /**
         * mysql
         */
        ConsultSession consultSession = new ConsultSession();
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessionList = consultSessionService.selectBySelective(consultSession);
        if(consultSessionList !=null && consultSessionList.size() >0){
            for(ConsultSession ongoingConsultSession : consultSessionList){
                if(nowDate.getTime() - ongoingConsultSession.getCreateTime().getTime() > 6*60*60*1000){
                    ongoingConsultSession.setStatus(ConsultSession.STATUS_COMPLETED);
                    consultSessionService.updateSessionInfo(ongoingConsultSession);
                    if(!dataMap.containsKey(ongoingConsultSession.getId())){
                        dataMap.put(ongoingConsultSession.getId(), ongoingConsultSession.getUserId());
                    }
                }
            }
        }
        /**
         * mongo
         */
        Query queryAgain = new Query(where("status").is("ongoing"));
        List<ConsultSessionStatusVo> consultSessionStatusAgainVos = consultRecordService.querySessionStatusList(queryAgain);
        if(consultSessionStatusAgainVos != null && consultSessionStatusAgainVos.size()>0){
            for(int i=0 ; i<consultSessionStatusAgainVos.size();i++){
                if(consultSessionStatusAgainVos.get(i).getLastMessageTime() != null && consultSessionStatusAgainVos.get(i).getLastMessageTime().getTime()-nowDate.getTime() > 6*60*60*1000){
                    //清除mongo内的残留数据
                    Query removeQuery = new Query().addCriteria(where("_id").is(consultSessionStatusAgainVos.get(i).getId()));
                    consultRecordService.updateConsultSessionStatusVo(removeQuery,"complete");
                    if(!dataMap.containsKey(consultSessionStatusAgainVos.get(i).getSessionId())){
                        dataMap.put(consultSessionStatusAgainVos.get(i).getSessionId(), consultSessionStatusAgainVos.get(i).getUserId());
                    }
                    /*if(!removelist.contains(consultSessionStatusAgainVos.get(i).getSessionId())) {
                         removelist.add(consultSessionStatusAgainVos.get(i).getSessionId());
                    }*/
                }
            }
        }
        /**
         * monog sessionId is null
         */
        List matchList = new ArrayList();
        matchList.add("null");
        matchList.add("");
        Query clearQuery = new Query(where("sessionId").in(matchList));
        consultRecordService.deleteMongoRecordBySelective(clearQuery, ConsultSessionStatusVo.class);

        /**
         * redis
         */
        Map noRemoveMap = new HashMap();
        if(consultSessions !=null && consultSessions.size()>0){
            for(Object consultSessionObject:consultSessions){
                RichConsultSession consultSessionValue = ConsultUtil.transferMapToRichConsultSession((HashMap<String, Object>) consultSessionObject);
                if(nowDate.getTime() - consultSessionValue.getCreateTime().getTime() > 6*60*60*1000){
                    if("ongoing".equalsIgnoreCase(consultSessionValue.getStatus())){
                        sessionRedisCache.removeConsultSessionBySessionId(consultSessionValue.getId());
                        if(!dataMap.containsKey(consultSessionValue.getId())) {
                            dataMap.put(consultSessionValue.getId(), consultSessionValue.getUserId());
                        }
                       /* if(!removelist.contains(consultSessionValue.getId())){
                            removelist.add(consultSessionValue.getId());
                        }*/
                        if(sessionIdList != null && sessionIdList.size() >0){
                            if(sessionIdList.contains(consultSessionValue.getId())){
                                Integer sessionId = sessionRedisCache.getSessionIdByUserId(consultSessionValue.getUserId());
                                if((sessionId !=null) && String.valueOf(sessionId).equalsIgnoreCase(String.valueOf(consultSessionValue.getId()))){
                                    sessionRedisCache.removeUserIdSessionIdPair(consultSessionValue.getUserId());
                                }else{
                                    HashMap userIdSessionIdPair = new HashMap();
                                    userIdSessionIdPair.put(sessionId,consultSessionValue.getUserId());
                                    noRemoveMap.put(consultSessionValue.getId(),userIdSessionIdPair);
                                }
                            }
                        }
                    }
                }
            }
        }
        dataList.add(dataMap);
        result.put("removeSessionList",dataList);
        result.put("noRemoveSessionList",noRemoveMap);
        return result ;
    }
}
