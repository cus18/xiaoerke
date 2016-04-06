package com.cxqm.xiaoerke.modules.wechat.dao;

import java.util.HashMap;



import java.util.List;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

@MyBatisDao
public interface WechatAttentionDao {
	
	 //根据openId查询关注信息
    SysWechatAppintInfoVo findAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo);
    
    int deleteByPrimaryKey(String id);

    int insert(WechatAttention record);

    int insertSelective(WechatAttention record);

    WechatAttention selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WechatAttention record);

    int updateByPrimaryKey(WechatAttention record);
    
    HashMap<String,Object> getAttention(String open_id);

    //根据openId查询关注信息列表
    List<SysWechatAppintInfoVo> findAttentionInfoByOpenIdLists(SysWechatAppintInfoVo sysWechatAppintInfoVo);

    //保存SysWechatAppintInfo表记录
    void saveSysWechatAppintInfo(SysWechatAppintInfoVo sysWechatAppintInfoVo);
    
    //根据openid查询微信名、Openid、来源、关注时间、医生姓名、医生电话、医院、科室
    DoctorAttentionVo findDoctorAttentionVoInfo(HashMap<String,Object> hashMap);

    DoctorAttentionVo findDoctorAttentionVoInfoNoOpenId(HashMap<String,Object> hashMap);

    //根据openid查询最近关注的marketer，防止取消关注的时候marketer总是为空
    WechatAttention findMarketerByOpeinid(WechatAttention wechatAttention);

}