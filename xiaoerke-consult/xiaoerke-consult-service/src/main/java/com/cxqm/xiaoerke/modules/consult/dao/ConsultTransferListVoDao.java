package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
@MyBatisDao
public interface ConsultTransferListVoDao {

    List<ConsultTransferListVo> findAllConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    int deleteConsultTransferListVoByPrimaryKey(String id);

    int addConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

}
