package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
         List<SysWechatAppintInfoVo> resultVo = wechatattentionDao.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
         SysWechatAppintInfoVo wechatAppintInfoVo = null;
         if(resultVo!=null && resultVo.size()>0){
             wechatAppintInfoVo = resultVo.get(0);
         }
        return wechatAppintInfoVo;
    }

    @Override
    public SysWechatAppintInfoVo getAttentionInfoByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo) {
        List<SysWechatAppintInfoVo> resultVo = wechatattentionDao.getAttentionInfoByOpenId(sysWechatAppintInfoVo);
        SysWechatAppintInfoVo wechatAppintInfoVo = new SysWechatAppintInfoVo();
        if(resultVo!=null && resultVo.size()>0){
            wechatAppintInfoVo = resultVo.get(0);
        }
        return wechatAppintInfoVo;
    }


    @Override
    public List<SysWechatAppintInfoVo> selectByOpenId(SysWechatAppintInfoVo sysWechatAppintInfoVo) {
        List<SysWechatAppintInfoVo> resultVo = wechatattentionDao.selectByOpenId(sysWechatAppintInfoVo);
        return resultVo;
    }

    @Override
    public List<SysWechatAppintInfoVo> findAttentionInfo(SysWechatAppintInfoVo sysWechatAppintInfoVo) {
        List<SysWechatAppintInfoVo> resultVo = wechatattentionDao.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        return resultVo;
    }

    @Override
    public HashMap<String,Object> getAttention(String open_id){
    	return wechatattentionDao.getAttention(open_id);
    }

    @Override
    public WechatAttention getAttentionByOpenId(String open_id){
        return wechatattentionDao.getAttentionByOpenId(open_id);
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

    @Override
    public Map findLastAttentionStatusByOpenId(String userId) {
        Map status = wechatattentionDao.findLastAttentionStatusByOpenId(userId);
        if(status != null){
            return status ;
        }else{
            return null;
        }
    }

}
