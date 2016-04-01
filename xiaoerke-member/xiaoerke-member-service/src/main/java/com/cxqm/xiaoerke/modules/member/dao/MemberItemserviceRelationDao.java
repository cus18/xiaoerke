package com.cxqm.xiaoerke.modules.member.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberItemserviceRelationVo;

@MyBatisDao
public interface MemberItemserviceRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberItemserviceRelationVo memberItemserviceRelationVo);

    int insertSelective(MemberItemserviceRelationVo memberItemserviceRelationVo);

    MemberItemserviceRelationVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberItemserviceRelationVo memberItemserviceRelationVo);

    int updateByPrimaryKey(MemberItemserviceRelationVo memberItemserviceRelationVo);
}