package com.cxqm.xiaoerke.modules.mutualHelp.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
@MyBatisDao
public interface MutualHelpDonationDao {

    //捐款总人数
    Integer getCount(@Param("donationType")Integer donationType);

    //捐款总金额
    Double getSumMoney(@Param("donationType")Integer donationType);

    //捐款总额
    Double getSumMoney(HashMap<String,Object> searchMap);

    //最后留言或捐款的时间
    Date getLastTime(HashMap<String, Object> searchMap);

    //捐款和留言详细
    List<MutualHelpDonation> getDonationDetail(HashMap<String, Object> searchMap);

    //最后一条留言
    MutualHelpDonation getLastNote(HashMap<String, Object> searchMap);

    //添加捐款或留言
    int saveNoteAndDonation(MutualHelpDonation mutualHelpDonation);
}
