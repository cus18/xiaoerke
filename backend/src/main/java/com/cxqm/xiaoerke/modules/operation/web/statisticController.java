/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.EmojiFilter;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.utils.DateUtil;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;


/**
 * mongoController
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class statisticController extends BaseController {

    @Autowired
    private LogMongoDBServiceImpl logMongoDBService;

    @Autowired
    private OperationHandleService OperationHandleService;

    @Autowired
    private ConsultRecordService consultRecordService;

    /**
     * 根据open_id和log_content查询日志记录
     */
    @RequestMapping(value = "/statistic/getLogInfoByLogContent", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getLogInfoByLogContent(@RequestBody Map<String, Object> params) {

        return OperationHandleService.getLogInfoByLogContent(params);
    }

    @RequestMapping(value = "/statistic/consultRecordMongoTransMySql", method = {RequestMethod.POST, RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String, Object> consultRecordMongoTransMySql(Map<String, Object> params) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date startDate;//同步的开始时间

        ConsultRecordVo recordVo = new ConsultRecordVo();
        recordVo.setCreateDate(date);
        List<ConsultRecordVo> recordVos = consultRecordService.selectByVo(recordVo);
        if (recordVos != null && recordVos.size() > 0) {
            startDate = recordVos.get(0).getCreateDate();
        } else {
            startDate = calendar.getTime();//如果库里面一条数据都没有那么默认只同步一天的
        }
        Date endDate = startDate;//同步到结束时间
        long passDate = DateUtils.pastDays(startDate);
        if (passDate > 0) {
            for (long i = 1l; i <= passDate; i++) {
                try {
                    startDate = endDate;
                    endDate = DateUtils.addDate(startDate,1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transRecordData(startDate, endDate);
            }
        } else {
            transRecordData(startDate, endDate);
        }
        resultMap.put("status", "success");
        return resultMap;
    }

    private void transRecordData(Date startDate, Date endDate) {
        Query query = new Query().addCriteria(Criteria.where("createDate").gte(startDate).andOperator(Criteria.where("createDate").lte(endDate)));
        System.out.println("==============startDate" + DateUtils.DateToStr(startDate, "datetime"));
        System.out.println("==============endDate" + DateUtils.DateToStr(endDate, "datetime"));
        List<ConsultRecordVo> consultRecordVoList = new ArrayList<ConsultRecordVo>();
        List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.getCurrentUserHistoryRecord(query);
        Iterator<ConsultRecordMongoVo> iterator = consultRecordMongoVos.iterator();
        while (iterator.hasNext()) {
            ConsultRecordMongoVo consultRecordMongoVo = iterator.next();
            ConsultRecordVo consultRecordVo = new ConsultRecordVo();
            consultRecordVo.setId(IdGen.uuid());
            consultRecordVo.setSessionId(consultRecordMongoVo.getSessionId());
            consultRecordVo.setType(consultRecordMongoVo.getType());
            consultRecordVo.setUserId(consultRecordMongoVo.getUserId());
            consultRecordVo.setCreateDate(consultRecordMongoVo.getCreateDate());
            consultRecordVo.setDoctorName(consultRecordMongoVo.getDoctorName());
            consultRecordVo.setSenderName(consultRecordMongoVo.getSenderName());
            consultRecordVo.setMessage(EmojiFilter.coverEmoji(consultRecordMongoVo.getMessage()));
            consultRecordVo.setCsuserId(consultRecordMongoVo.getCsUserId());
            consultRecordVo.setSenderId(consultRecordMongoVo.getSenderId());
            consultRecordVoList.add(consultRecordVo);
            consultRecordMongoVo = null;
        }
        int insertFlag = consultRecordService.insertConsultRecordBatch(consultRecordVoList);
        consultRecordVoList = null;
        consultRecordMongoVos = null;
    }

}
