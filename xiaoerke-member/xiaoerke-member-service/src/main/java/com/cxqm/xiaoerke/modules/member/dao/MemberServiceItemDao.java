package com.cxqm.xiaoerke.modules.member.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberServiceItemVo;

@MyBatisDao
public interface MemberServiceItemDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberServiceItemVo memberServiceItemVo);

    int insertSelective(MemberServiceItemVo memberServiceItemVo);

    MemberServiceItemVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberServiceItemVo memberServiceItemVo);

    int updateByPrimaryKey(MemberServiceItemVo memberServiceItemVo);
}