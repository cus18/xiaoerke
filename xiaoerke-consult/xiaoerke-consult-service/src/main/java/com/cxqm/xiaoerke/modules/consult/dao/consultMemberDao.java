package com.cxqm.xiaoerke.modules.consult.dao;


public interface consultMemberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(consultMemberDao record);

    int insertSelective(consultMemberDao record);

    consultMemberDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(consultMemberDao record);

    int updateByPrimaryKey(consultMemberDao record);
}