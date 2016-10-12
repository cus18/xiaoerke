package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.mongodb.WriteResult;
import com.sun.java.util.jar.pack.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface ConsultRecordService {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultRecordVo record);

    int insertConsultRecordBatch(List<ConsultRecordVo> record);

    int insertSelective(ConsultRecordVo record);

    ConsultRecordVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsultRecordVo record);

    int updateByPrimaryKey(ConsultRecordVo record);

    PaginationVo<ConsultRecordMongoVo> getRecordDetailInfo(int pageNo, int pageSize, Query query,String recordType);

    PaginationVo<ConsultSessionStatusVo> getUserMessageList(int pageNo, int pageSize, Query query);

    List<ConsultSessionStatusVo> queryUserMessageList(Query query);

    int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo);

    ConsultRecordMongoVo findOneConsultRecord(Query query);

    ConsultSessionStatusVo findOneConsultSessionStatusVo(Query query);

    //根据sessionId查询consult用户信息
    List<ConsultRecordMongoVo> findUserConsultInfoBySessionId(ConsultRecordMongoVo consultRecordMongoVo);

    HashMap<String, Object> uploadMediaFile(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws UnsupportedEncodingException;

    void buildRecordMongoVo(@RequestParam(required = true) String senderId,@RequestParam(required = true) String type,
                            @RequestParam(required = false) String messageContent,RichConsultSession consultSession);

    void saveConsultSessionStatus(RichConsultSession consultSession);

    void modifyConsultSessionStatusVo(RichConsultSession consultSession);

    List<ConsultSessionStatusVo> querySessionStatusList(Query query);

    void  updateConsultSessionStatusVo(Query query,String status);

    void  deleteConsultTempRecordVo(Query query);

    List<ConsultRecordMongoVo> getCurrentUserHistoryRecord(Query query);

    List<ConsultSessionStatusVo> getConsultSessionStatusVo(Query query);


    WriteResult removeConsultRankRecord(Query query);

    //jiangzg add 修改mongo中ConsultSessionStatusVo第一次接入医生时间
    int updateConsultSessionFirstTransferDate(Query query ,Update update ,Class t);

    //jiangzg add 2016-10-12 11:56:31 删除mongo中记录
    int deleteMongoRecordBySelective(Query query , Class t);
}