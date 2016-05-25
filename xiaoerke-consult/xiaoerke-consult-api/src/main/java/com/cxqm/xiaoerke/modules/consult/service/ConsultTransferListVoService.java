package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
public interface ConsultTransferListVoService {

    List<ConsultTransferListVo> findAllConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    int deleteConsultTransferListVo(Integer id);

    int addConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    int updateConsultTransferByPrimaryKey(ConsultTransferListVo consultTransferListVo);

    ConsultTransferListVo findOneConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

}
