package com.cxqm.xiaoerke.modules.wechat.dao;

import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * about wechat info
 * Created by wangbaowei on 2015/8/24.
 */
@MyBatisDao
public interface WechatInfoDao {
/**
 * update markater info
 * */
    void insertAttentionInfo(HashMap<String,Object> codeMap);

    Map<String, Object> getDoctorInfo(@Param("id") String id);

    void getCustomerOnlineTime(ArrayList<CustomBean> list);

    void insertCustomerLocation(HashMap<String,Object> codeMap);
    
    List<Map<String, Object>> selectAttentions(@Param("userId") String userId, @Param("date") String date);

    int checkAttention(HashMap<String,Object> codeMap);

    List<HashMap<String,Object>> getUserListYesterday(String date);

    //new zdl
    void updateAttentionInfo(HashMap<String,Object> hashMap);

    //List<String> getUsersOpenIdList(Map<String, Object> param);

    //客服查询没有回复的用户列表sunxiao
    //List<Map<String,Object>> getNoAnswerUserList(Map<String, Object> param);

    //客服查询没有回复的用户列表sunxiao
    List<Map<String,Object>> findNoAnswerUserList(Map<String, Object> param);

}
