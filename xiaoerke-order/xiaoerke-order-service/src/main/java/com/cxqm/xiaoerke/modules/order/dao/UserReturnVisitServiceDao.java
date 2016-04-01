/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.order.dao;

import java.util.HashMap;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.UserReturnVisitVo;

/**
 * 审批DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserReturnVisitServiceDao {

    //修改用户回访信息
    int updateUserReturnVisitInfo(UserReturnVisitVo vo);

    //插入用户回访信息
    void saveUserReturnVisitInfo(UserReturnVisitVo UserReturnVisitVo);

    //查询用户回访信息
    UserReturnVisitVo getUserReturnVisitByInfo(HashMap<String,Object> hashMap);
}
