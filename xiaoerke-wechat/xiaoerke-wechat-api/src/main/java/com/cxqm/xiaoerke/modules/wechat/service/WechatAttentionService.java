package com.cxqm.xiaoerke.modules.wechat.service;


import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;

import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 微信关注服务接口
 * @author wangbaowei
 * @date 2015-11-04
 */

@Service
@Transactional(readOnly = false)
public interface WechatAttentionService {

	SysWechatAppintInfoVo findAttentionInfoByOpenId(
			SysWechatAppintInfoVo sysWechatAppintInfoVo);

	SysWechatAppintInfoVo getAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo);

	List<SysWechatAppintInfoVo> findAttentionInfo(SysWechatAppintInfoVo sysWechatAppintInfoVo);

	HashMap<String,Object> getAttention(String open_id);

	WechatAttention getAttentionByOpenId(String open_id);

	List<SysWechatAppintInfoVo> findAttentionInfoByOpenIdLists(
			SysWechatAppintInfoVo sysWechatAppintInfoVo);

	DoctorAttentionVo findDoctorAttentionVoInfoNoOpenId(HashMap<String,Object> hashMap);

}
