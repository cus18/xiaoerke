package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.modules.consult.dao.ConsultRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 咨询服务
 *
 * @author deliang
 * @version 2015-12-09
 */
@Service
@Transactional(readOnly = false)
public class ConsultRecordServiceImpl implements ConsultRecordService {

    @Autowired
    private ConsultRecordDao consultRecordDao;

    @Autowired
    ConsultRecordMongoDBServiceImpl consultRecordMongoDBService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return consultRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.insert(consultRecordVo);
    }

    @Override
    public int insertSelective(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.insert(consultRecordVo);
    }

    @Override
    public ConsultRecordVo selectByPrimaryKey(Long id) {
        return consultRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.updateByPrimaryKeySelective(consultRecordVo);
    }

    @Override
    public int updateByPrimaryKey(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.updateByPrimaryKey(consultRecordVo);
    }

    @Override
    public PaginationVo<ConsultRecordMongoVo> getPage(int pageNo, int pageSize, Query query) {
        return consultRecordMongoDBService.getPage(pageNo, pageSize, query);
    }

    @Override
    public int saveRecord(ConsultRecordMongoVo consultRecordMongoVo) {
        return consultRecordMongoDBService.saveRecord(consultRecordMongoVo);
    }


    @Override
    public ConsultRecordMongoVo findOneConsultRecord(Query query) {
        return consultRecordMongoDBService.findOneConsult(query);
    }




}
