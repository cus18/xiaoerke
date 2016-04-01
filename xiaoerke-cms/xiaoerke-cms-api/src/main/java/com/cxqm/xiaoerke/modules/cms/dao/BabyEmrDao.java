package com.cxqm.xiaoerke.modules.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;

/**
 *  知识库宝宝信息
 * @author sunxiao
 *
 */
@MyBatisDao
public interface BabyEmrDao {

	/**
	 * 保存知识库宝宝信息
	 * 2015年11月27日 上午11:09:50
	 * @return Integer
	 * @author sunxiao
	 */
	public Integer saveBabyEmr(BabyEmrVo bev);

	/**
	 * 通过OpenID查询知识库的宝宝记录
	 * 2015年11月27日 上午11:10:53
	 * @return List<Map<String,Object>>
	 * @author sunxiao
	 */
	public List<BabyEmrVo>  getBabyEmrList(@Param("openid")String openid);
	
	/**
	 * 更新知识库宝宝信息
	 * 2015年11月27日 上午11:11:19
	 * @return Integer
	 * @author sunxiao
	 */
	public Integer updateBabyEmr(BabyEmrVo cbi);
	
	/**
	 * 通过OpenID获得微信昵称
	 * @param openid
	 * @return
	 */
	public String getNickName(@Param("openid")String openid);
}
