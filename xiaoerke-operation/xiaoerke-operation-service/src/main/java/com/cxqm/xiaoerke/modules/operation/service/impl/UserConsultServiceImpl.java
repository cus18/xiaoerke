package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.operation.service.UserConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户咨询统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class UserConsultServiceImpl implements UserConsultService {

    @Autowired
    private MongoDBService<MongoLog> logMongoDBService;

    @Autowired
    private MongoDBService<WechatAttention> attentionMongoDBService;

    @Autowired
    private MongoDBService<WechatRecord> weChatRecordmongoDBService;

    @Autowired
    private OperationHandleService operationHandleService;

    @Override
    public HashMap<String, Object> getConsultStatisticData(@RequestBody Map<String, Object> params) {

        HashMap<String, Object> response = new HashMap<String, Object>();

        String startDate = (String) params.get("begin_time");
        String endDate = (String) params.get("end_time");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryWeChatRecord = new Query();

        try {
            queryWeChatRecord.addCriteria(new Criteria("infoTime").gte(sdf.parse(startDate)).lte(sdf.parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //用来返回数据
        List<HashMap<String, Object>> responseList = new ArrayList<HashMap<String, Object>>();

        //查出这段时间咨询过的Openid
        List<String> WechatRecordList = weChatRecordmongoDBService.queryStringListDistinct(queryWeChatRecord, "openid");

        //根据OpenId查询该用户的所有聊天记录（根据时间升序排序）
        for (String openId : WechatRecordList) {

            Query queryWeChatRecordOne = new Query();
            try {
                //所有的聊天记录按照时间排序
                queryWeChatRecordOne.addCriteria(new Criteria("openid").is(openId));
                queryWeChatRecordOne.with(new Sort(new Sort.Order(Sort.Direction.ASC, "infoTime")));
                List<WechatRecord> Recordlists = weChatRecordmongoDBService.queryList(queryWeChatRecordOne);

                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                int count = 0;//用户回复记录数
                //遍历。每次会话都会产生一条新记录
                for (WechatRecord wechatRec : Recordlists) {

                    if ("1001".equals(wechatRec.getOpercode())) {//开启会话
                        //根据Openid 查询 sys_attention 中的微信名、来源等
                        Query queryAttenInfo = new Query();
                        queryAttenInfo.addCriteria(new Criteria("openid").is(openId));
                        List<WechatAttention> attentionList = attentionMongoDBService.queryList(queryAttenInfo);
                        //防止nickName为空的情况,attentionList会有多条记录，有的nickName为空
                        String nickName = "";
                        String openid = "";
                        String marketer = "";
                        if (attentionList.size() > 0 && attentionList != null) {
                            openid = attentionList.get(0).getOpenid();
                            marketer = attentionList.get(0).getMarketer();
                            if (openid == null || "".equals(openid)) {
                                openid = "无";
                            }
                            if (marketer == null || "".equals(marketer)) {
                                marketer = "无";
                            }
                            if (attentionList.get(0).getDate() != null) {
                                hashMap.put("date", DateUtils.formatDateTime(attentionList.get(0).getDate()));
                            } else {
                                hashMap.put("date", "无");
                            }
                            for (int i = 0; i < attentionList.size(); i++) {
                                nickName = attentionList.get(i).getNickname();
                                if (StringUtils.isNotNull(nickName))
                                    break;
                            }
                        }
                        if (nickName == null || "".equals(nickName)) {
                            nickName = "无";
                        }
                        hashMap.put("nickname", nickName);
                        hashMap.put("openid", openid);
                        hashMap.put("marketer", marketer);
                        //关注时间处理
                        //根据 opercode = 1001 记录开始会话时间
                        if (wechatRec.getinfoTime() != null) {
                            hashMap.put("openWindow", DateUtils.formatDateTime(wechatRec.getinfoTime()));
                        } else {
                            hashMap.put("openWindow", "无");
                        }
                    } else if ("2002".equals(wechatRec.getOpercode()) && " 谢谢您的支持/:share".equals(wechatRec.getText())) {
                        hashMap.put("nullity", "无效咨询");//无效咨询
                    } else if ("2003".equals(wechatRec.getOpercode())) {
                        count++;//用户回复记录数+1
                    } else if ("1004".equals(wechatRec.getOpercode())) {//结束会话
                        if (wechatRec.getinfoTime() != null) {
                            hashMap.put("closeWindow", DateUtils.formatDateTime(wechatRec.getinfoTime()));
                        } else {
                            hashMap.put("closeWindow", "无");
                        }
                        //根据 opercode = 1004 记录会话结束时间
                        hashMap.put("consult_count", count);
                        if (count == 0) {
                            hashMap.put("nullity", "无效咨询");
                        }
                        if (StringUtils.isNull((String) hashMap.get("nullity"))) {
                            hashMap.put("nullity", "有效咨询");
                        }
                        //查询使用预约频道的次数、成功预约次数、约单次数、访问页面数
                        List<HashMap<String, Object>> list = operationHandleService.getValidReserveList(startDate, endDate, openId);
                        hashMap.put("validRNum", list.get(0).get("id"));
                        hashMap.put("victoryRNum", list.get(1).get("id"));
                        hashMap.put("validNum", list.get(2).get("id"));
                        hashMap.put("visitRNum", list.get(3).get("id"));
                        //组装数据
                        responseList.add(hashMap);
                        hashMap = new HashMap<String, Object>();//结束会话后hashMap清空，以此在确定对话框打开和关闭
                        count = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.put("consult", responseList);
        return response;
    }
}
