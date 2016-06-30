package com.cxqm.xiaoerke.modules.mutualHelp.service;

import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public interface MutualHelpDonationService {

    //捐款总条数
    public Integer getCount(Integer donationType);

    //捐款总金额
    public Double getSumMoney(HashMap<String, Object> paramMap);

    //捐款和留言详情
    public Map<String,Object> getDonatonDetail(HashMap<String, Object> paramMap);

    public Map<String,Object> getLastNote(HashMap<String, Object> paramMap);

    public int saveNoteAndDonation(MutualHelpDonation mutualHelpDonation);

}
