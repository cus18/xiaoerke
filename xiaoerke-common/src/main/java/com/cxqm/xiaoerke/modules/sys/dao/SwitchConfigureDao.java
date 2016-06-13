package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.SwitchConfigure;

import java.util.Map;

/**
 * 开关配置DAO接口
 * @author sunxiao
 * @version 2016-06-13
 */
@MyBatisDao
public interface SwitchConfigureDao {

    SwitchConfigure getUmbrellaSwitch(Map map);
}
