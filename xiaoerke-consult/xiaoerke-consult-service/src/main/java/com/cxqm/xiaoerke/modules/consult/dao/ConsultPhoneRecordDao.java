package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface ConsultPhoneRecordDao {

    int deleteByPrimaryKey(Integer id);

    int insert(ConsultPhoneRecordVo record);

    int insertSelective(ConsultPhoneRecordVo record);

    ConsultPhoneRecordVo selectByPrimaryKey(Integer id);

    List<ConsultPhoneRecordVo> selectByUserData(@Param("userData")String userData,@Param("action")String action);

    int updateByPrimaryKeySelective(ConsultPhoneRecordVo record);

    int updateByPrimaryKey(ConsultPhoneRecordVo record);
}