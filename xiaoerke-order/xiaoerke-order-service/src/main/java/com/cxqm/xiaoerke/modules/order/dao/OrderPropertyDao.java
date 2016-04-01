/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;

/**
 * 审批DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OrderPropertyDao {

    int deleteByPrimaryKey(String id);

    int insert(OrderPropertyVo orderPropertyVo);

    int insertSelective(OrderPropertyVo orderPropertyVo);

    OrderPropertyVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderPropertyVo orderPropertyVo);

    int updateByPrimaryKey(OrderPropertyVo orderPropertyVo);

}
