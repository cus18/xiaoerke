package com.cxqm.xiaoerke.modules.mutualHelp.serviceImpl;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.mutualHelp.dao.MutualHelpDonationDao;
import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;
import com.cxqm.xiaoerke.modules.mutualHelp.service.MutualHelpDonationService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
@Service
@Transactional(readOnly = false)
public class MutualHelpDonationServiceImpl implements MutualHelpDonationService {

    @Autowired
    MutualHelpDonationDao dao;

    @Autowired
    SystemService systemService;

    @Override
    public Integer getCount(Integer donationType) {
        return dao.getCount(donationType);
    }

    @Override
    public Double getSumMoney(Integer donationType) {
        return dao.getSumMoney(donationType);
    }

    @Override
    public Map<String, Object> getDonatonDetail(HashMap<String, Object> paramMap) {
        Map<String, Object> response = new HashMap<String, Object>();

        String myOpenId = (String) paramMap.get("openId");
        String userId = (String) paramMap.get("userId");
        Integer donationType = (Integer) paramMap.get("donationType");

        //我的捐款信息
        Map<String, Object> myMap = new HashMap<String, Object>();
        HashMap<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("userId",userId);
        searchMap.put("donationType",donationType);
        Date lastTime = dao.getLastTime(searchMap);
        if(lastTime != null) {
            myMap.put("lastTime",lastTime);
            myMap.put("sumMoney", dao.getSumMoney(searchMap));
            if (myOpenId != null) {
                Map<String, Object> myWechatMap = getWechatMessage(myOpenId);
                myMap.put("wechatName", myWechatMap.get("wechatName"));
                myMap.put("headImgUrl", myWechatMap.get("headImgUrl"));
            } else {
                myMap.put("wechatName", "宝粉");
                myMap.put("headImgUrl", "");
            }
        }
        response.put("myMap",myMap);

        //所有捐款和留言信息
        List<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
        HashMap<String,Object> searchMap1 = new HashMap<String, Object>();
        searchMap1.put("donationType",donationType);
        List<MutualHelpDonation> resultList = dao.getDonationDetail(searchMap1);
        for(MutualHelpDonation mutualHelpDonation:resultList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id",mutualHelpDonation.getId());
            String openId = mutualHelpDonation.getOpenId();
            map.put("openid",openId);
            if(openId != null){
                Map<String,Object> wechatMap = getWechatMessage(mutualHelpDonation.getOpenId());
                map.put("wechatName",wechatMap.get("wechatName"));
                map.put("headImgUrl",wechatMap.get("headImgUrl"));
            }else{
                map.put("wechatName","宝粉");
                map.put("headImgUrl","");
            }

            map.put("money",mutualHelpDonation.getMoney());
            map.put("leaveNote",mutualHelpDonation.getLeaveNote());
            map.put("createTime",mutualHelpDonation.getCreateTime());
            list.add(map);
        }
        response.put("donationList",list);

        return response;
    }

    @Override
    public Map<String, Object> getLastNote(HashMap<String,Object> paramMap) {
        Map<String, Object> response  = new HashMap<String, Object>();
        MutualHelpDonation mutualHelpDonation = dao.getLastNote(paramMap);
        if(mutualHelpDonation != null) {
            response.put("id", mutualHelpDonation.getId());
            String openId = mutualHelpDonation.getOpenId();
            response.put("openid", openId);
            if (openId != null) {
                Map<String, Object> wechatMap = getWechatMessage(mutualHelpDonation.getOpenId());
                response.put("wechatName", wechatMap.get("wechatName"));
                response.put("headImgUrl", wechatMap.get("headImgUrl"));
            } else {
                response.put("wechatName", "宝粉");
                response.put("headImgUrl", "");
            }

            response.put("money", mutualHelpDonation.getMoney());
            response.put("leaveNote", mutualHelpDonation.getLeaveNote());
            response.put("createTime", mutualHelpDonation.getCreateTime());
        }
        return response;
    }

    @Override
    public int saveNoteAndDonation(MutualHelpDonation mutualHelpDonation) {
        return dao.saveNoteAndDonation(mutualHelpDonation);
    }

    /**
     * 获取微信头像、微信名
     * @param openId
     * @return
     */
    public Map<String, Object> getWechatMessage(String openId){
        Map<String, Object> wechatMap = new HashMap<String, Object>();

        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");

        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
        String param="";
        String json= WechatUtil.post(strURL, param, "GET");
        JSONObject jasonObject = JSONObject.fromObject(json);
        Map<String, Object> jsonMap = (Map) jasonObject;

        if(jsonMap.get("subscribe")!=null && (Integer)jsonMap.get("subscribe") == 1){
            wechatMap.put("wechatName",(String) jsonMap.get("nickname"));
            wechatMap.put("headImgUrl",(String) jsonMap.get("headimgurl"));

        }else{
            wechatMap.put("wechatName","宝粉");
            wechatMap.put("headImgUrl","");
        }

        return wechatMap;
    }
}
