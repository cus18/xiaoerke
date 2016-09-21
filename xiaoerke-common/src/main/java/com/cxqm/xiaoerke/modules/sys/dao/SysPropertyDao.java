package com.cxqm.xiaoerke.modules.sys.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
@MyBatisDao
public interface SysPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPropertyVoWithBLOBsVo record);

    int insertSelective(SysPropertyVoWithBLOBsVo record);

    SysPropertyVoWithBLOBsVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPropertyVoWithBLOBsVo record);

    int updateByPrimaryKeyWithBLOBs(SysPropertyVoWithBLOBsVo record);

    int updateByPrimaryKey(SysPropertyVo record);
}