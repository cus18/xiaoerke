package com.cxqm.xiaoerke.modules.account.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.account.entity.Record;
import com.cxqm.xiaoerke.modules.account.entity.WithdrawRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface WithdrawRecordDao {
    int deleteByPrimaryKey(String id);

    int insert(WithdrawRecord record);

    int insertSelective(WithdrawRecord record);

    WithdrawRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WithdrawRecord record);

    int updateByPrimaryKey(WithdrawRecord record);

    List<Record> selectWithDrawRecordInfoByUserId(String userId);
    
    WithdrawRecord selectDataById(String id);
    
    Page<WithdrawRecord> selectPageByUserId(@Param("userId") String userId, Page<WithdrawRecord> page);
    
    Page<WithdrawRecord> selectPage(WithdrawRecord record, Page<WithdrawRecord> page);

    int getWithDrawRecordNumByUserId(String userId);

    int checkUserPayRecod(String userId);

}