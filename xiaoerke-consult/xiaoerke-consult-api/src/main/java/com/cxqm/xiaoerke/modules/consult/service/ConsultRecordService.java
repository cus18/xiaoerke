package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;


public interface ConsultRecordService {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultRecordVo record);

    int insertSelective(ConsultRecordVo record);

    ConsultRecordVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsultRecordVo record);

    int updateByPrimaryKey(ConsultRecordVo record);

    PaginationVo<ConsultRecordMongoVo> getPage(int pageNo, int pageSize, Query query);

    int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo);

    ConsultRecordMongoVo findOneConsultRecord(Query query);

    //根据sessionId查询consult用户信息
    List<ConsultRecordMongoVo> findUserConsultInfoBySessionId(ConsultRecordMongoVo consultRecordMongoVo);

    HashMap<String, Object> uploadMediaFile(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws UnsupportedEncodingException;
}