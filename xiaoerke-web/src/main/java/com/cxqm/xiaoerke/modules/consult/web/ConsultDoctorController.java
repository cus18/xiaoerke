package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"consult/doctor"})
public class ConsultDoctorController extends BaseController
{

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private ConsultSessionForwardRecordsService consultSessionForwardRecordsService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @RequestMapping(value={"/getCurrentUserHistoryRecord"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> getCurrentUserHistoryRecord(@RequestBody Map<String, Object> params)
    {
        HashMap response = new HashMap();
        String userId = (String)params.get("userId");
        String dateTime = (String)params.get("dateTime");
        Integer pageSize = (Integer)params.get("pageSize");
        List currentUserHistoryRecord = null;
        Date date = null;
        if (dateTime.indexOf("-") != -1)
            date = DateUtils.StrToDate(dateTime, "datetime");
        else if (dateTime.indexOf("/") != -1)
            date = DateUtils.StrToDate(dateTime, "xiangang");

        Query query = new Query().addCriteria(Criteria.where("userId").is(userId).and("createDate").lt(date)).with(new Sort(Sort.Direction.DESC, new String[] { "createDate" })).limit(pageSize.intValue());

        currentUserHistoryRecord = this.consultRecordService.getCurrentUserHistoryRecord(query);
        if (currentUserHistoryRecord != null)
            response.put("consultDataList", ConsultUtil.transformCurrentUserListData(currentUserHistoryRecord));
        else
            response.put("consultDataList", "");

        return response;
    }

    @RequestMapping(value={"/rankList"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> findConversationRankList(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse)
    {
        Map searchMap = new HashMap();
        HashMap resultMap = new HashMap();
        try {
            searchMap.put("rankDate", params.get("rankDate"));
            List resultValue = this.consultSessionForwardRecordsService.findConversationRankList(searchMap);
            resultMap.put("rankListValue", resultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping(value={"/recordList"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> recordList(@RequestBody Map<String, Object> params)
    {
        Query query;
        String recordType = String.valueOf(params.get("recordType"));
        String userId = String.valueOf(params.get("userId"));
        String fromUserId = String.valueOf(params.get("fromUserId"));
        int pageNo = 0;
        int pageSize = 1;

        PaginationVo pagination = null;
        Map response = new HashMap();
        if ((null != params.get("pageNo")) && (null != params.get("pageSize"))) {
            pageNo = ((Integer)params.get("pageNo")).intValue();
            pageSize = ((Integer)params.get("pageSize")).intValue();
        }
        if ((recordType.equals("all")) && (StringUtils.isNotNull(userId)) && (pageSize > 0)) {
            query = new Query(Criteria.where("userId").is(userId)).with(new Sort(Sort.Direction.DESC, new String[] { "createDate" }));
            pagination = this.consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        }
        else if ((recordType.equals("doctor")) && (StringUtils.isNotNull(userId)) && (StringUtils.isNotNull(fromUserId))) {
            query = new Query(Criteria.where("toUserId").is(userId).and("fromUserId").is(fromUserId)).with(new Sort(Sort.Direction.DESC, new String[] { "createDate" }));

            pagination = this.consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        } else if ((recordType.equals("1")) || (recordType.equals("2"))) {
            query = new Query(Criteria.where("userId").is(userId).and("type").is(recordType)).with(new Sort(Sort.Direction.DESC, new String[] { "createDate" }));

            pagination = this.consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        }
        List recordMongoVos = new ArrayList();
        if ((pagination.getDatas() != null) && (pagination.getDatas().size() > 0)) {
            for (Iterator i$ = pagination.getDatas().iterator(); i$.hasNext(); ) { ConsultRecordMongoVo consultRecordMongoVo = (ConsultRecordMongoVo)i$.next();
                ConsultRecordMongoVo recordMongoVo = consultRecordMongoVo;
                recordMongoVo.setInfoDate(DateUtils.DateToStr(consultRecordMongoVo.getCreateDate(), "datetime"));
                recordMongoVos.add(recordMongoVo);
            }
            response.put("records", recordMongoVos);
        }
        response.put("currentPage", Integer.valueOf(pageNo));
        response.put("pageSize", Integer.valueOf(pageSize));
        response.put("totalPage", Integer.valueOf(pagination.getTotalPage()));
        return response;
    }

    @RequestMapping(value={"/produceRecord"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public void produceRecord(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse)
    {
        ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
        this.consultRecordService.saveConsultRecord(consultRecordMongoVo);
    }

    @RequestMapping(value={"/GetCSDoctorList"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> GetCSDoctorList(@RequestBody Map<String, Object> params)
    {
        Map response = new HashMap();

        User user = new User();
        user.setUserType("consultDoctor");
        String userId = String.valueOf(params.get("userId"));
        if (StringUtils.isNotNull(userId))
            user.setId(userId);

        List users = this.systemService.findUserByUserType(user);
        if ((users != null) && (users.size() > 0)) {
            response.put("CSList", users);
            response.put("status", "success");
        }
        return response;
    }

    @RequestMapping(value={"/createDoctorConsultSession"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public HashMap<String, Object> createDoctorConsultSession(@RequestBody Map<String, Object> params)
    {
        HashMap response = new HashMap();
        String userId = (String)params.get("userId");

        RichConsultSession richConsultSession = new RichConsultSession();
        richConsultSession.setStatus(null);
        richConsultSession.setUserId(userId);
        List richConsultSessions = this.consultSessionService.selectRichConsultSessions(richConsultSession);
        if (StringUtils.listNotNull(richConsultSessions)) {
            richConsultSession = (RichConsultSession)richConsultSessions.get(0);
        }

        if ((null != richConsultSession.getSource()) && (richConsultSession.getSource().equals("wxcxqm")))
        {
            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(userId);
            SysWechatAppintInfoVo wechatAttentionVo = this.wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
            String userName = userId.substring(userId.length() - 8, userId.length());
            if ((wechatAttentionVo != null) &&
                    (StringUtils.isNotNull(wechatAttentionVo.getWechat_name()))) {
                userName = wechatAttentionVo.getWechat_name();
            }

            ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
            consultSessionForwardRecordsVo.setConversationId(Long.valueOf(richConsultSession.getId().intValue()));
            consultSessionForwardRecordsVo.setStatus("waiting");
            List consultSessionForwardRecordsVos = this.consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);
            if (consultSessionForwardRecordsVos.size() > 0) {
                response.put("result", "existTransferSession");
            } else {
                Query query = new Query().addCriteria(Criteria.where("userId").is(richConsultSession.getUserId())).with(new Sort(Sort.Direction.DESC, new String[] { "lastMessageTime" }));
                ConsultSessionStatusVo consultSessionStatusVo = this.consultRecordService.findOneConsultSessionStatusVo(query);

                if (DateUtils.pastHour(consultSessionStatusVo.getLastMessageTime()) < 48L)
                {
                    if (richConsultSession.getStatus().equals("ongoing")) {
                        String doctorManagerStr = Global.getConfig("doctorManager.list");
                        String csUserId = UserUtils.getUser().getId();
                        if (doctorManagerStr.indexOf(csUserId) != -1)
                        {
                            richConsultSession.setCsUserId(csUserId);
                            setRichConsultSession(response, richConsultSession, userName);
                        }
                        else {
                            response.put("result", "noLicenseTransfer");
                        }

                    }
                    else
                    {
                        richConsultSession.setCsUserId(UserUtils.getUser().getId());
                        setRichConsultSession(response, richConsultSession, userName);
                    }
                }
                else response.put("result", "exceed48Hours");
            }
        }
        else if ((null != richConsultSession.getSource()) && (richConsultSession.getSource().equals("h5cxqm"))) {
            User user = this.systemService.getUser(userId);
            if (user != null);
            response.put("result", "notOnLine");
        } else {
            response.put("result", "failure");
        }

        return response;
    }

    private void setRichConsultSession(HashMap<String, Object> response, RichConsultSession richConsultSession, String userName) {
        richConsultSession.setCsUserName(UserUtils.getUser().getName());
        richConsultSession.setUserName(userName);
        richConsultSession.setNickName(userName);
        ConsultSessionManager.getSessionManager().putSessionIdConsultSessionPair(richConsultSession.getId(), richConsultSession);
        ConsultSessionManager.getSessionManager().putUserIdSessionIdPair(richConsultSession.getUserId(), richConsultSession.getId());
        ConsultSession consultSession = new ConsultSession();
        consultSession.setId(richConsultSession.getId());
        consultSession.setCsUserId(richConsultSession.getCsUserId());
        consultSession.setStatus("ongoing");
        int flag = this.consultSessionService.updateSessionInfo(consultSession);
        if (flag > 0) {
            response.put("result", "success");
            response.put("userId", richConsultSession.getUserId());
        } else {
            response.put("result", "failure");
        }
    }

    @RequestMapping(value={"/sessionEnd"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> sessionEnd(@RequestParam(required=true) String sessionId, @RequestParam(required=true) String userId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openid", userId);
        params.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        params.put("starNum1", 0);
        params.put("starNum2", 0);
        params.put("starNum3", 0);
        params.put("doctorId", UserUtils.getUser().getId());
        params.put("content", "");
        params.put("dissatisfied", null);
        params.put("redPacket", null);
        patientRegisterPraiseService.saveCustomerEvaluation(params);
        String st = "本次咨询体验怎么样?赶快来评价吧!【" +
                "<a href='http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=" + params.get("uuid") + "'>点击这里去评价</a>】";
        Map wechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        WechatUtil.sendMsgToWechat((String) wechatParam.get("token"), userId, st);
        String result = consultSessionService.clearSession(sessionId, userId);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", result);
        return response;
    }
}