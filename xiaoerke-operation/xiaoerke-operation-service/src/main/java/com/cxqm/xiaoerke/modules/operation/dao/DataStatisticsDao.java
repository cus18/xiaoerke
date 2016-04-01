/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.operation.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

import java.util.HashMap;
import java.util.List;

/**
 * 医生具体出诊地址DAO接口
 *
 * @author sunxiao
 * @version 2015-08-04
 */
@MyBatisDao
public interface DataStatisticsDao {
    //获取统计数据
    List<HashMap<String,Object>> findStatistics(HashMap<String,Object> hashMap);

    //获取累计关注数
    int findStatisticsConcern();

    //获取取消关注数
    int findStatisticsCancelConcern();

    //获取所有当天所有预约信息（包括openId）
    List<HashMap<String,Object>> findAllAppointByDate(HashMap<String,Object> hashMap);

    //获取所有当天所有咨询信息(包括openId)
    List<HashMap<String,Object>> findAllConsultByDate(HashMap<String,Object> hashMap);

    //根据时间和openid查询，此新用户是否存在，不存在为老用户
    int judgeAppIsNew(HashMap<String,Object> hashMap);

    //判断此用户之前是否咨询过
    int judgeNewConIsExistWeChat(HashMap<String,Object> hashMap);

    //获取访问郑玉巧说的所有用户的openid/create_date
    List<HashMap<String, Object>> findAllReaderZhengYuQiao(HashMap<String, Object> hashMap);

    //获取已经咨询人数
    int alreadyConNumber();

    //查询总活跃用户数
    int findSumActive(HashMap<String,Object> hashMap);

    //查询一天的总活跃用户数
    int findSumActiveOneDay(HashMap<String,Object> hashMap);

    //保存统计数据
    void saveDataStatistics(HashMap<String,Object> saveMap);

    //判断此用户是否既预约又咨询
    int judgeNewOrOldAppCon(HashMap<String,Object> hashMap);

    //用户行为统计
    List<HashMap<String,Object>>  findUserActionStatistic(HashMap<String,Object> hashMap);

    //获取访问咨询频道总人数
    int getConsultUser(HashMap<String,Object> searchMap);

    //获取郑玉巧说的总人数
    List<String> getZhengYuQiaoShuoUser(HashMap<String,Object> hashMap);
    
    //获取用户分析中的次数   张博
    Integer getTotalNum(HashMap<String,Object> hashMap);

}
