package com.cxqm.xiaoerke.modules.account.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.account.entity.AccountBinding;

@MyBatisDao
public interface AccountBindingDao {

    int deleteByPrimaryKey(String id);

    int insert(AccountBinding record);

    int insertSelective(AccountBinding record);

    AccountBinding selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountBinding record);

    int updateByPrimaryKey(AccountBinding record);
}