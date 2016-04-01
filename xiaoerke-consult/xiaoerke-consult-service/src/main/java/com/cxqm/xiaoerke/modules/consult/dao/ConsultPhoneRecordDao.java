package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;

@MyBatisDao
public interface ConsultPhoneRecordDao {

    int deleteByPrimaryKey(Integer id);

    int insert(ConsultPhoneRecordVo record);

    int insertSelective(ConsultPhoneRecordVo record);

    ConsultPhoneRecordVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultPhoneRecordVo record);

    int updateByPrimaryKey(ConsultPhoneRecordVo record);
}