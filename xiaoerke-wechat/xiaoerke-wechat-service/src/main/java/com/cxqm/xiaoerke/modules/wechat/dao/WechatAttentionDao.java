package com.cxqm.xiaoerke.modules.wechat.dao;

import java.util.HashMap;



import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface WechatAttentionDao {
	
	 //根据openId查询关注信息
    List<SysWechatAppintInfoVo> findAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo);

    List<SysWechatAppintInfoVo> getAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo);

    int deleteByPrimaryKey(String id);

    int insert(WechatAttention record);

    int insertSelective(WechatAttention record);

    WechatAttention selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WechatAttention record);

    int updateByPrimaryKey(WechatAttention record);
    
    HashMap<String,Object> getAttention(String open_id);

    WechatAttention getAttentionByOpenId(String open_id);

    //根据openId查询关注信息列表
    List<SysWechatAppintInfoVo> findAttentionInfoByOpenIdLists(SysWechatAppintInfoVo sysWechatAppintInfoVo);

    //保存SysWechatAppintInfo表记录
    void saveSysWechatAppintInfo(SysWechatAppintInfoVo sysWechatAppintInfoVo);
    
    //根据openid查询微信名、Openid、来源、关注时间、医生姓名、医生电话、医院、科室
    DoctorAttentionVo findDoctorAttentionVoInfo(HashMap<String,Object> hashMap);

    DoctorAttentionVo findDoctorAttentionVoInfoNoOpenId(HashMap<String,Object> hashMap);

    //根据openid查询最近关注的marketer，防止取消关注的时候marketer总是为空
    WechatAttention findMarketerByOpeinid(WechatAttention wechatAttention);

    //jiangzhongge add 根据用户openid查询用户最后一次关注的状态
    Map findLastAttentionStatusByOpenId(@Param("userId") String userId);

    List<String> getOpenIdListByNickName(@Param("nickname") String nickname);

    Page<WechatAttention> findUserChannelList(Page<WechatAttention> page,@Param("openidlist") List<String> openidlist,@Param("todayAttention") String todayAttention,@Param("todayConsult") String todayConsult);
}