package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultCoopUserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-9-21.
 * 合作方用户信息Dao
 */

@MyBatisDao
public interface ConsultCoopUserInfoDao {

    int insertConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo);

    int deleteConsultCoopUserInfo(@Param(value = "id") Integer id);

    int updateConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo);

    ConsultCoopUserInfoVo getConsultCoopUserInfoById(@Param(value = "id") Integer id);

    List<ConsultCoopUserInfoVo> getConsultCoopUserInfoByCondition(ConsultCoopUserInfoVo consultCoopUserInfo);

}
