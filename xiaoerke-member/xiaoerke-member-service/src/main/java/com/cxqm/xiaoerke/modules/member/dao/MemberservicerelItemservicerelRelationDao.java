package com.cxqm.xiaoerke.modules.member.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface MemberservicerelItemservicerelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberservicerelItemservicerelRelationVo vo);

    int insertSelective(MemberservicerelItemservicerelRelationVo vo);

    MemberservicerelItemservicerelRelationVo selectByPrimaryKey(Integer id);

    List<MemberservicerelItemservicerelRelationVo> selectByUserId(MemberservicerelItemservicerelRelationVo vo);

    List<MemberservicerelItemservicerelRelationVo> selectAvailableListByUserId(MemberservicerelItemservicerelRelationVo vo);

    int updateByPrimaryKeySelective(MemberservicerelItemservicerelRelationVo vo);

    int updateByPrimaryKey(MemberservicerelItemservicerelRelationVo vo);

    int updateLeftTimesSubtractByPrimaryKey(MemberservicerelItemservicerelRelationVo vo);


    int updateLeftTimesAddByPrimaryKey(MemberservicerelItemservicerelRelationVo vo);

    List<MemberservicerelItemservicerelRelationVo> getMemberServiceInfo(MemberservicerelItemservicerelRelationVo vo);

    Page<MemberservicerelItemservicerelRelationVo> findMemberServiceList(Page<MemberservicerelItemservicerelRelationVo> page,MemberservicerelItemservicerelRelationVo vo);

    List<MemberservicerelItemservicerelRelationVo> getAllMemberServiceList(MemberservicerelItemservicerelRelationVo vo);

    List<Map<String, String>> getMemberUsageDetail(Integer id);

    int updateByMemberServiceRelationId(MemberservicerelItemservicerelRelationVo vo);
}