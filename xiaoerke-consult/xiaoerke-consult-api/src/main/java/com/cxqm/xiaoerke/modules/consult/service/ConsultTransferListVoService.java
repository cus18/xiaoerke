package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
public interface ConsultTransferListVoService {

    public List<ConsultTransferListVo> findAllConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

    public int deleteConsultTransferListVo(String id);

    public int addConsultTransferListVo(ConsultTransferListVo consultTransferListVo);

}
