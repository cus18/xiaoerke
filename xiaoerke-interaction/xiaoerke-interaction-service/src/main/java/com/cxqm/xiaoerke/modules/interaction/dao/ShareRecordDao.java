package com.cxqm.xiaoerke.modules.interaction.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.interaction.entity.ShareRecord;

@MyBatisDao
public interface ShareRecordDao {
    int deleteByPrimaryKey(String id);

    int insert(ShareRecord record);

    int insertSelective(ShareRecord record);

    ShareRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShareRecord record);

    int updateByPrimaryKey(ShareRecord record);
}