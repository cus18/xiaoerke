package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultSessionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultSession consultSession);

    int insertSelective(ConsultSession consultSession);

    ConsultSession selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultSession consultSession);

    int updateSessionInfoByUserId(ConsultSession consultSession);

    int updateByPrimaryKey(ConsultSession consultSession);

    List<Map<String,Object>> selectByPrOpenid(String openId);
    
    List<ConsultSession> selectBySelective(ConsultSession consultSession);

    List<HashMap<String,Object>> getOnlineCsListInfo(List<String> userList);

    List<ConsultSession> getAlreadyAccessUsers(ConsultSession consultSession);

    HashMap<String,Object> getAttention(String openId);
}