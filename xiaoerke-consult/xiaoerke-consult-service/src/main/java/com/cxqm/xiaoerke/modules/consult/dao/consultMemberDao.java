package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultMemberVo;

public interface ConsultMemberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultMemberVo record);

    int insertSelective(ConsultMemberVo record);

    ConsultMemberVo selectByPrimaryKey(Integer id);

    ConsultMemberVo selectByopenid(String openid);

    int updateByPrimaryKeySelective(ConsultMemberVo record);

    int updateByPrimaryKey(ConsultMemberVo record);
}