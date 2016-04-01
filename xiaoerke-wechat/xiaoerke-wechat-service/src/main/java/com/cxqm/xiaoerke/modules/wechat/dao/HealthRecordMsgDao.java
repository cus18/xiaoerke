package com.cxqm.xiaoerke.modules.wechat.dao;

import com.cxqm.xiaoerke.modules.wechat.entity.HealthRecordMsgVo;

public interface HealthRecordMsgDao {
    int deleteByPrimaryKey(Long id);

    int insert(HealthRecordMsgVo record);

    int insertSelective(HealthRecordMsgVo record);

    HealthRecordMsgVo selectByPrimaryKey(Long id);

    HealthRecordMsgVo findHealthRecordMsgByOpenid(HealthRecordMsgVo record);

    int updateByPrimaryKeySelective(HealthRecordMsgVo record);

    int updateByPrimaryKey(HealthRecordMsgVo record);
}