/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.EmojiFilter;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * mongoController
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "")
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

    @RequestMapping(value = "/statistic/getLogInfoByLogContent", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> consultRecordMongoTransMySql(@RequestBody Map<String, Object> params) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        Date date = new Date();

        ConsultRecordVo recordVo = new ConsultRecordVo();
        recordVo.setCreateDate(date);
        recordVo = consultRecordService.selectByVo(recordVo).get(0);

        Query query = new Query().addCriteria(Criteria.where("createDate").gte(recordVo.getCreateDate()));
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
        }
        int insertFlag = consultRecordService.insertConsultRecordBatch(consultRecordVoList);
        resultMap.put("status", insertFlag > 0 ? "success" : "falure");
        return resultMap;
    }

}
