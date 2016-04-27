/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.Page;
import org.springframework.stereotype.Repository;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
@Repository 
public interface UserDao extends CrudDao<User> {

	User getByLoginName(User user);

	List<User> findUserByOfficeId(User user);

	List<User> findUserByUserType(User user);

	long findAllCount(User user);

	int updatePasswordById(User user);

	int updateLoginInfo(User user);

	int deleteUserRole(User user);

	int insertUserRole(User user);

	int updateUserInfo(User user);

	Map getUserByLoginName(User user);
	
	User getUserByInfo();

	int checkUserAppointment(String openid);

	void insertUserAppointmentNum(Map<String, String> map);
	
	HashMap<String,Object> findPatientIdByPhoneExecute(HashMap<String, Object> executeMap);
	
	//对预约的状态进行操作（插入用户信息） @author 14_zdl
    void updateUserElementsExecute(User user);
    
    HashMap<String, Object> findUserDetailInfoByUserIdExecute(HashMap<String,Object> userId);

	//分页查询用户列表sunxiao
	Page<User> findUserList(Page<User> page,User user);

	//根据条件查询用户信息sunxiao
	List<User> getUserListByInfo(HashMap map);
}
