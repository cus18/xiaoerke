package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
@MyBatisDao
@Repository
public interface ConsultTransferListVoDao {

    List<ConsultTransferListVo> findAllConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    int deleteConsultTransferListVoByPrimaryKey(Integer id);

    int addConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    int updateConsultTransferByPrimaryKeySelective(ConsultTransferListVo consultTransferListVo);

}
