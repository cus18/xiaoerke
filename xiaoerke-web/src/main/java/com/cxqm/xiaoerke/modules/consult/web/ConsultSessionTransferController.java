package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPayUserService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2016-5-18.
 * 转接列表发起会话
 */
@Controller
@RequestMapping(value = "consultSession/transfer")
public class ConsultSessionTransferController {

    @Autowired
    private ConsultTransferListVoService consultTransferListVoService;

    @Autowired
    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @RequestMapping(value = "/createMoreUserConsultSession", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> createMoreWXUserConsultSession(@RequestBody Map<String, Object> params) {

        HashMap<String, Object> response = new HashMap<String, Object>();
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map<String, Object> specialistPatientContent = (Map<String, Object>) params.get("specialistPatientContent");
        Integer sessionId = sessionRedisCache.getSessionIdByUserId((String) specialistPatientContent.get("userId"));
        List<String> distributorsList = ConsultSessionManager.INSTANCE.distributorsList;
        if (sessionId != null) {
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
            if (richConsultSession != null) {
                if (ConsultSession.STATUS_ONGOING.equalsIgnoreCase(richConsultSession.getStatus())) {
                    response.put("status", "ongoing");
                    if (distributorsList.contains(richConsultSession.getCsUserId())) {
                        response.put("csuserName", richConsultSession.getCsUserName() + "护士");
                    } else {
                        response.put("csuserName", richConsultSession.getCsUserName() + "医生");
                    }
                    return response;
                }
            }
        }
        try {
            synchronized (this) {
                ConsultTransferListVo consultTransfer = new ConsultTransferListVo();
                consultTransfer.setId((Integer) specialistPatientContent.get("id"));
                ConsultTransferListVo consultTransferListVo1 = consultTransferListVoService.findOneConsultTransferListVo(consultTransfer);
                if (consultTransferListVo1 != null) {
                    if ("complete".equalsIgnoreCase(consultTransferListVo1.getStatus())) {
                        response.put("status", "complete");
                        return response;
                    } else {
                        HashMap<String, Object> resultMap = ConsultSessionManager.INSTANCE.createConsultSession((String) specialistPatientContent.get("userName"), (String) specialistPatientContent.get("userId"), sysPropertyVoWithBLOBsVo);
                        if (resultMap != null && "success".equalsIgnoreCase((String) resultMap.get("result"))) {
                            ConsultTransferListVo consultTransferListVo;
                            String status = "complete";
                            String delFlag = "0";
                            consultTransferListVo = new ConsultTransferListVo();
                            consultTransferListVo.setStatus(status);
                            consultTransferListVo.setDelFlag(delFlag);
                            consultTransferListVo.setId((Integer) specialistPatientContent.get("id"));
                            int count = consultTransferListVoService.updateConsultTransferByPrimaryKey(consultTransferListVo);
                            if (count > 0) {
                                ConsultSessionManager.INSTANCE.refreshConsultTransferList(UserUtils.getUser().getId());
                            }
                            response.put("userId", specialistPatientContent.get("userId"));
                            response.put("userName", specialistPatientContent.get("userName"));
                            response.put("result", resultMap.get("result"));
                            response.put("status", "success");
                        } else {
                            response.put("id", specialistPatientContent.get("id"));
                            response.put("result", resultMap.get("result"));
                            response.put("status", "failure");
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.getStackTrace();
        }
        return response;
    }
}
