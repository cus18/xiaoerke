package com.cxqm.xiaoerke.modules.member.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberServiceRelationVo;

import java.util.List;

/**
 * 会员与服务关系
 */
@MyBatisDao
public interface MemberServiceRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberServiceRelationVo memberServiceRelationVo);

    int insertSelective(MemberServiceRelationVo memberServiceRelationVo);

    MemberServiceRelationVo selectByPrimaryKey(Integer id);

    List<MemberServiceRelationVo> selectByOpenId(MemberServiceRelationVo memberServiceRelationVo);

    int updateByPrimaryKeySelective(MemberServiceRelationVo memberServiceRelationVo);

    int updateByPrimaryKey(MemberServiceRelationVo memberServiceRelationVo);


}