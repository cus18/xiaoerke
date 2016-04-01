package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author wangbaowei
 * @date 2015-11-04
 */

@Service
@Transactional(readOnly = false)
public class WechatAttentionServiceImpl implements WechatAttentionService {

   @Autowired
	private WechatAttentionDao wechatattentionDao;
	 
	   //根据openId查询关注信息
	 @Override
    public SysWechatAppintInfoVo findAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo) {
        SysWechatAppintInfoVo resultVo = wechatattentionDao.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        return resultVo;
    }

    @Override
    public HashMap<String,Object> getAttention(String open_id){
    	return wechatattentionDao.getAttention(open_id);
    }
    
    //根据openId查询关注信息列表
    @Override
	public List<SysWechatAppintInfoVo> findAttentionInfoByOpenIdLists(SysWechatAppintInfoVo sysWechatAppintInfoVo){
    	return wechatattentionDao.findAttentionInfoByOpenIdLists(sysWechatAppintInfoVo);
    }

    @Override
    public DoctorAttentionVo findDoctorAttentionVoInfoNoOpenId(HashMap<String,Object> hashMap){
    	return wechatattentionDao.findDoctorAttentionVoInfoNoOpenId(hashMap);
    }

}
