package com.cxqm.xiaoerke.modules.account.dao;

import java.util.HashMap;

import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;

/**
 * Created by wangbaowei on 15/9/25.
 */

@MyBatisDao
public interface AccountInfoDao {

    int deleteByPrimaryKey(String id);

    int insert(AccountInfo record);

    int insertSelective(AccountInfo record);

    AccountInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountInfo record);

    int updateByPrimaryKey(AccountInfo record);

    AccountInfo selectAccountInfoByUserId(String userId);

    int saveOrUpdate(AccountInfo record);
    
    int updateBalanceByUser(@Param("userId") String userId, @Param("balance") Float balance);
    
    int deleteByUser(@Param("userId") String userId);


}
