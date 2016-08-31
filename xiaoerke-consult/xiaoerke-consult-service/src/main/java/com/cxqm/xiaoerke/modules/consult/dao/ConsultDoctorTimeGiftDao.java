/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorTimeGiftVo;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;

/**
 * 运维赠送咨询次数dao
 *
 * @author sunxiao
 * @version 2016-8-16
 */
@MyBatisDao
public interface ConsultDoctorTimeGiftDao {

    int insert(ConsultDoctorTimeGiftVo vo);

    Page<ConsultDoctorTimeGiftVo> findConsultDoctorOrderListByInfo(Page<ConsultDoctorTimeGiftVo> page,ConsultDoctorTimeGiftVo vo);

}
