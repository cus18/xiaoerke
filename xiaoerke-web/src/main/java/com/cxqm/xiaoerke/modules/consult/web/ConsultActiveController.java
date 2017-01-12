/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoThirdPartyService;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * @author deliang 统计数据，并行计算
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/active")
public class ConsultActiveController extends BaseController {

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private ConsultSessionService consultConversationService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private BabyUmbrellaInfoThirdPartyService babyUmbrellaInfoThirdPartyService;


    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("openId", "o3_NPwh2PkuPM-xPA2fZxlmB5Xqg");
        response = getUser2016Data(response);
        System.out.println(response);
    }


    @RequestMapping(value = "/getUser2016Data", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getUser2016Data(@RequestBody Map<String, Object> params) {

        Map<String, Object> response = new HashMap<String, Object>();
        params.put("openId", "o3_NPwh2PkuPM-xPA2fZxlmB5Xqg");
        String openId = String.valueOf(params.get("openId"));
        //---------------------------------------查询用户的关注时间-----------------------------------
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openId);
        sysWechatAppintInfoVo = wechatAttentionService.getAttentionInfoByOpenId(sysWechatAppintInfoVo);
        if (sysWechatAppintInfoVo != null) {
            String attention_time = sysWechatAppintInfoVo.getAttention_time();
            response.put("attentionDate", StringUtils.isNotBlank(attention_time) ? attention_time.split(" ")[0] : "null");
        } else {
            //从微信接口获取用户关注时间
        }

        //----------------------------查询用户第一次咨询时间，总共咨询多少次------------------------------
        Callable FirstConsultTime = new FirstConsultTime(openId);
        FutureTask calculateFirstConsultTimeTask = new FutureTask(FirstConsultTime);
        Thread calculateFirstConsultTimeThread = new Thread(calculateFirstConsultTimeTask);
        calculateFirstConsultTimeThread.start();
        //----------------------------日期+ 咨询时长最长的一次------------------------------
        Callable largestConsultTime = new LargestConsultTime(openId);
        FutureTask largestConsultTask = new FutureTask(largestConsultTime);
        Thread largestConsultThread = new Thread(largestConsultTask);
        largestConsultThread.start();
        //----------------------------评价（11693）：2016第一次评价------------------------------
        Callable firstEvaluation = new FirstEvaluation(openId);
        FutureTask firstEvaluationTask = new FutureTask(firstEvaluation);
        Thread firstEvaluationThread = new Thread(firstEvaluationTask);
        firstEvaluationThread.start();
        //----------------送心意（4572）： 2016年第一次为医生送心意时间+ 总共送心意……钱----------------
        Callable firstRedPacket = new FirstRedPacket(openId);
        FutureTask firstRedPacketTask = new FutureTask(firstRedPacket);
        Thread firstRedPacketThread = new Thread(firstRedPacketTask);
        firstRedPacketThread.start();
        //宝护伞（13153）： 2016年加入宝护伞时间
        Callable joinUmbrellaTime = new JoinUmbrellaTime(openId);
        FutureTask joinUmbrellaTimeTask = new FutureTask(joinUmbrellaTime);
        Thread joinUmbrellaTimeThread = new Thread(joinUmbrellaTimeTask);
        joinUmbrellaTimeThread.start();
        //邀请（3596）： 2016年有……人通过你认识宝大夫
        Callable joinBaoDaiFuForYou = new JoinBaoDaiFuForYou(openId);
        FutureTask joinBaoDaiFuForYouTask = new FutureTask(joinBaoDaiFuForYou);
        Thread joinBaoDaiFuForYouThread = new Thread(joinBaoDaiFuForYouTask);
        joinBaoDaiFuForYouThread.start();
        while (!calculateFirstConsultTimeTask.isDone() || !largestConsultTask.isDone() || !firstEvaluationTask.isDone() ||
                !firstRedPacketTask.isDone() || !joinUmbrellaTimeTask.isDone() || !joinBaoDaiFuForYouTask.isDone()) {
            try {
                Thread.sleep(300);
                System.out.println("等待...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Map map1 = (Map) calculateFirstConsultTimeTask.get();
            Map map2 = (Map) largestConsultTask.get();
            Map map3 = (Map) firstEvaluationTask.get();
            Map map4 = (Map) firstRedPacketTask.get();
            Map map5 = (Map) joinUmbrellaTimeTask.get();
            Map map6 = (Map) joinBaoDaiFuForYouTask.get();
            response.put("firstConsultTime", map1.get("firstConsultTime"));
            response.put("consultTitleNumber", map1.get("consultTitleNumber"));
            response.put("largestConsultTime", map2.get("largestConsultTime"));
            response.put("largestConsultDuration", map2.get("largestConsultDuration"));
            response.put("FirstEvaluationTime", map3.get("2016FirstEvaluationTime"));
            response.put("2016FirstRedPacketTime", map4.get("2016FirstRedPacketTime"));
            response.put("FirstRedPacketCount", map4.get("2016FirstRedPacketCount"));
            response.put("joinUmbrellaTime", map5.get("joinUmbrellaTime"));
            response.put("joinBaoDaiFuForYou", map6.get("joinBaoDaiFuForYou"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String DateToStr(Date date,String flag) {
        SimpleDateFormat format = null;
        if("time".equals(flag)){
            format = new SimpleDateFormat("HH:mm");
        }else if("date".equals(flag)) {
            format = new SimpleDateFormat("yyyy年MM月dd日");
        }else if("datetime".equals(flag)){
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else if("monthDate".equals(flag)){
            format = new SimpleDateFormat("MM/dd");
        }else if("monthDate:".equals(flag)){
            format = new SimpleDateFormat("MM:dd");
        }else if("flag1".equals(flag)){
            format = new SimpleDateFormat("MM-dd HH:mm");
        }else{
            format = new SimpleDateFormat(flag);
        }
        String dateStr = null;
        dateStr = format.format(date);
        return dateStr;
    }

    private class FirstConsultTime implements Callable {
        private String openId;

        private FirstConsultTime(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            ConsultSession consultSession = new ConsultSession();
            consultSession.setUserId(openId);
            List<ConsultSession> consultSessionList = consultSessionService.selectBySelective(consultSession);
            if (consultSessionList != null && consultSessionList.size() > 0) {
                consultSession = consultSessionList.get(consultSessionList.size() - 1);
                String date = DateToStr(consultSession.getCreateTime(), "date");
                response.put("firstConsultTime", date);
                response.put("consultTitleNumber", consultSessionList.size());
            } else {
                response.put("firstConsultTime", "null");
                response.put("consultTitleNumber", "null");
            }
            return response;
        }
    }


    private class LargestConsultTime implements Callable {
        private String openId;

        private LargestConsultTime(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            ConsultSession consultSession = new ConsultSession();
            consultSession.setUserId(openId);
            consultSession = consultConversationService.selectConsultDurationByOpenid(openId);
            if (consultSession != null && consultSession.getConsultNumber() != null) {
                response.put("largestConsultTime", DateToStr(consultSession.getCreateTime(), "date"));
                Integer consultNumber = consultSession.getConsultNumber() > 0 ? consultSession.getConsultNumber() : 118;
                response.put("largestConsultDuration", consultNumber > 500 ? "118" : consultNumber);//异常处理
            } else {
                response.put("largestConsultTime", "null");
                response.put("largestConsultDuration", "null");
            }
            return response;
        }
    }

    private class FirstEvaluation implements Callable {
        private String openId;

        private FirstEvaluation(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            Map registerPraiseInfo1 = patientRegisterPraiseService.select2016EvaluationByOpenId(openId);
            if (registerPraiseInfo1 != null && registerPraiseInfo1.size() > 0) {
                response.put("2016FirstEvaluationTime", DateToStr((Date) registerPraiseInfo1.get("createtime"), "date"));
            } else {
                response.put("2016FirstEvaluationTime", "null");
            }
            return response;
        }
    }

    private class FirstRedPacket implements Callable {
        private String openId;

        private FirstRedPacket(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            Map registerPraiseInfo2 = patientRegisterPraiseService.select2016EvaluationByOpenId_2(openId);
            if (registerPraiseInfo2 != null && registerPraiseInfo2.size() > 0) {
                response.put("2016FirstRedPacketTime", DateToStr((Date) registerPraiseInfo2.get("createtime"), "date"));
                response.put("2016FirstRedPacketCount", registerPraiseInfo2.get("redPacket"));
            } else {
                response.put("2016FirstRedPacketTime", "null");
                response.put("2016FirstRedPacketCount", "null");
            }
            return response;
        }
    }

    private class JoinUmbrellaTime implements Callable {
        private String openId;

        private JoinUmbrellaTime(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            Map<String, Object> openIdMap = new HashMap<String, Object>();
            openIdMap.put("openId", openId);
            List<Map<String, Object>> openIdList = babyUmbrellaInfoThirdPartyService.getIfBuyUmbrellaByOpenidOrPhone(openIdMap);
            if (openIdList != null && openIdList.size() > 0) {
                response.put("joinUmbrellaTime", DateToStr((Date) openIdList.get(0).get("createTime"), "date"));
            } else {
                response.put("joinUmbrellaTime", "null");
            }
            return response;
        }
    }

    private class JoinBaoDaiFuForYou implements Callable {
        private String openId;

        private JoinBaoDaiFuForYou(String openId) {
            this.openId = openId;
        }

        @Override
        public Object call() throws Exception {
            Map<String, Object> response = new HashMap<String, Object>();
            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(openId);
            List<SysWechatAppintInfoVo> sysWechatAppintInfoVos = wechatAttentionService.selectByOpenId(sysWechatAppintInfoVo);
            if (sysWechatAppintInfoVos != null && sysWechatAppintInfoVos.size() > 0) {
                response.put("joinBaoDaiFuForYou", sysWechatAppintInfoVos.size());
            } else {
                response.put("joinBaoDaiFuForYou", "null");
            }
            return response;
        }
    }

}

