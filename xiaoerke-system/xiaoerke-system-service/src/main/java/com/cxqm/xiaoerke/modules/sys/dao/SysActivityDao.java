package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysActivityVo;

@MyBatisDao
public interface SysActivityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysActivityVo sysActivityVo);

    int insertSelective(SysActivityVo sysActivityVo);

    SysActivityVo selectByPrimaryKey(Integer id);

    SysActivityVo selectByQrcode(SysActivityVo sysActivityVo);

    int updateByPrimaryKeySelective(SysActivityVo sysActivityVo);

    int updateByPrimaryKey(SysActivityVo sysActivityVo);
}