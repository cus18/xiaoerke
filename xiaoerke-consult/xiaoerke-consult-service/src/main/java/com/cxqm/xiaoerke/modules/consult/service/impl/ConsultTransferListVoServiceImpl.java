package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultTransferListVoDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
@Service
@Transactional(readOnly = false)
public class ConsultTransferListVoServiceImpl implements ConsultTransferListVoService{

    @Autowired
    ConsultTransferListVoDao consultTransferListVoDao;

    @Override
    public List<ConsultTransferListVo> findAllConsultTransferListVo(ConsultTransferListVo consultTransferListVo) {

        return null;
    }

    @Override
    public int deleteConsultTransferListVo(String id) {
        int count = consultTransferListVoDao.deleteConsultTransferListVoByPrimaryKey(id);
        if(count !=0){
            return count;
        }else{
            return 0;
        }
    }

    @Override
    public int addConsultTransferListVo(ConsultTransferListVo consultTransferListVo) {

        if(consultTransferListVo != null){
            int count = consultTransferListVoDao.addConsultTransferListVo(consultTransferListVo);
            if(count !=0){
                return count;
            }else{
                return 0;
            }
        }else{
            System.out.println("转接列表实体不能为空！");
            return 0;
        }
    }
}
