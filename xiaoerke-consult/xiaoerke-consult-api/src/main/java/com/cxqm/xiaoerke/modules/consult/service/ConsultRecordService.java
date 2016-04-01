package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import org.springframework.data.mongodb.core.query.Query;


public interface ConsultRecordService {
    int deleteByPrimaryKey(Long id);

    int insert(ConsultRecordVo record);

    int insertSelective(ConsultRecordVo record);

    ConsultRecordVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsultRecordVo record);

    int updateByPrimaryKey(ConsultRecordVo record);

    PaginationVo<ConsultRecordMongoVo> getPage(int pageNo, int pageSize, Query query);

    int saveRecord(ConsultRecordMongoVo consultRecordMongoVo);

    ConsultRecordMongoVo findOneConsultRecord(Query query);
}