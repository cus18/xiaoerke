package com.cxqm.xiaoerke.modules.member.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.member.entity.MemberSendMessageVo;

@MyBatisDao
public interface MemberSendMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberSendMessageVo memberSendMessageVo);

    int insertSelective(MemberSendMessageVo memberSendMessageVo);

    MemberSendMessageVo selectByPrimaryKey(Integer id);

    MemberSendMessageVo selectByOpenId(MemberSendMessageVo memberSendMessageVo);

    int updateByPrimaryKeySelective(MemberSendMessageVo memberSendMessageVo);

    int updateByPrimaryKey(MemberSendMessageVo memberSendMessageVo);

    void updateByOpenid(MemberSendMessageVo memberSendMessageVo);
}