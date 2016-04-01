package com.cxqm.xiaoerke.modules.member.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberOrderRelationVo;

@MyBatisDao
public interface MemberOrderRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberOrderRelationVo memberOrderRelationVo);

    int insertSelective(MemberOrderRelationVo memberOrderRelationVo);

    MemberOrderRelationVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberOrderRelationVo memberOrderRelationVo);

    int updateByPrimaryKey(MemberOrderRelationVo memberOrderRelationVo);
}