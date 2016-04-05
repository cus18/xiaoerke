package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.sys.entity.ReceiveXmlEntity;
import com.mongodb.WriteResult;

/**
 * Created by deliang on 16/3/18.
 */
public interface PatientConsultWechatService {

    void patientConsultService(ReceiveXmlEntity xmlEntity);

}
