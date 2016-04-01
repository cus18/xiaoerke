package com.cxqm.xiaoerke.modules.member.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberServiceVo;

@MyBatisDao
public interface MemberServiceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberServiceVo memberServiceVo);

    int insertSelective(MemberServiceVo memberServiceVo);

    MemberServiceVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberServiceVo memberServiceVo);

    int updateByPrimaryKey(MemberServiceVo memberServiceVo);
}