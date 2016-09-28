package com.cxqm.xiaoerke.modules.vaccine.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface VaccineSendMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineSendMessageVo record);

    int insertSelective(VaccineSendMessageVo record);

    List<VaccineSendMessageVo> selectByVaccineSendMessage(VaccineSendMessageVo record);

    int updateByPrimaryKeySelective(VaccineSendMessageVo record);

    int updateByPrimaryKeyWithBLOBs(VaccineSendMessageVo record);

    int updateByPrimaryKey(VaccineSendMessageVo record);
}