package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.modules.consult.entity.consultMemberVo;

public interface consultMemberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(consultMemberVo record);

    int insertSelective(consultMemberVo record);

    consultMemberVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(consultMemberVo record);

    int updateByPrimaryKey(consultMemberVo record);
}