package com.cxqm.xiaoerke.modules.interaction.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.dao.DoctorConcernDao;
import com.cxqm.xiaoerke.modules.interaction.service.ConcernService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class ConcernServiceImpl implements ConcernService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DoctorInfoService doctorInfoService;

	@Autowired
	private DoctorConcernDao doctorConcernDao;
	
	@Autowired
	private RegisterService registerService;

    @Autowired
    SystemService systemService;

    @Override
    @SystemServiceLog(description = "00000010")//查询我的关注医生信息
    public void getMyConcernedDoctorList(Map<String, Object> params,HashMap<String, Object> response)
     {
        List<HashMap<String,Object>> doctorList = new ArrayList<HashMap<String, Object>>();
        String currentPage = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String orderBy = (String) params.get("orderBy");
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage, pageSize);
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("sys_user_id", UserUtils.getUser().getId());
        searchMap.put("orderBy", orderBy);

        Page<HashMap<String, Object>> resultPage = doctorConcernDao.findMyConcernDoctorInfo(searchMap, page);
        List<HashMap<String, Object>> list = resultPage.getList();
        registerService.generateDoctorDataVoList(list, doctorList);

        if (logger.isDebugEnabled()) {
            logger.debug("login, listSearchDoctor: {}", resultPage);
        }
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        response.put("doctorDataVo", doctorList);
    }

    @Override
    public void userConcernDoctor(Map<String, Object> params)
    {
        String doctorId = (String)params.get("doctorId");

        // 记录日志
        LogUtils.saveLog(Servlets.getRequest(), "00000011" , "doctorId"+doctorId);//关注医生

        //关注后，更新该医生的粉丝数量
        HashMap<String, Object> mapDr = new HashMap<String, Object>();
        mapDr.put("doctorId", doctorId);
        doctorInfoService.updateDoctorFansExecute(mapDr);

        //关注后，往sys_concern表中插入数据
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("id", IdGen.uuid());
        hashMap.put("doctorId", doctorId);
        hashMap.put("UserId", UserUtils.getUser().getId());
        doctorConcernDao.insertSysConcernExecute(hashMap);
    }

    @Override
    public void judgeIfUserConcernDoctor(Map<String, Object> params,HashMap<String, Object> response)
    {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        String doctorId = (String)params.get("doctorId");
        hashMap.put("doctorId", doctorId);
        hashMap.put("sysUserId", UserUtils.getUser().getId());
        HashMap<String, Object> map = doctorConcernDao.JudgeUserConcernDoctor(hashMap);
        if (map != null && !map.isEmpty()) {
            response.put("isConcerned", true);
        }
        else {
            response.put("isConcerned", false);
        }
    }

    @Override
    public void getMyFansList(Map<String, Object> params, HashMap<String, Object> response) {
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        String doctorId = (String)params.get("doctorId");
        String pageNo = (String)params.get("pageNo");
        String pageSize = String.valueOf(params.get("pageSize"));

        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo,pageSize);
        //分页查询粉丝列表
        Page<HashMap<String, Object>> resultPage = doctorConcernDao.getMyFansList(doctorId,page);
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");

        List<HashMap<String, Object>> resultList = resultPage.getList();
        List<HashMap<String, Object>> fansList = new ArrayList<HashMap<String, Object>>();
        if(resultList != null && resultList.size() > 0){
            for(HashMap<String, Object> resultMap:resultList){
                String wechatName = (String) resultMap.get("wechat_name");
                String headImgUrl = (String) resultMap.get("pic_url");

                //如果头像或微信名为空，通过微信获取并保存到数据库
                if(wechatName == null || headImgUrl == null){
                    String openId = (String) resultMap.get("openid");
                    if(openId == null){
                        headImgUrl = "";
                        wechatName = "宝粉";
                    }else{
                        //微信获取头像用户名
                        Map<String, Object> jsonMap = getWechatMessage(openId);
                        if(jsonMap.get("subscribe")!=null && (Integer)jsonMap.get("subscribe") == 1){
                            if(wechatName == null) {
                                wechatName = (String) jsonMap.get("nickname");
                            }
                            if(headImgUrl == null) {
                                headImgUrl = (String) jsonMap.get("headimgurl");
                            }
                            HashMap<String, Object> updateMap = new HashMap<String, Object>();
                            updateMap.put("headImgUrl",headImgUrl);
                            updateMap.put("wechatName", wechatName);
                            updateMap.put("id", (String) resultMap.get("id"));
                            doctorConcernDao.updateWechatNameAndImg(updateMap);//保存到数据库
                        }
                    }

                }

                HashMap<String, Object> fansMap = new HashMap<String, Object>();
                fansMap.put("headImgUrl", headImgUrl);
                fansMap.put("wechatName", wechatName);
                fansList.add(fansMap);
            }
        }
        response.put("fansList",fansList);
    }

    /**
     * 获取微信头像、微信名
     * @param openId
     * @return
     */
    public Map<String, Object> getWechatMessage(String openId){

        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");

        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
        String param="";
        String json= WechatUtil.post(strURL, param, "GET");
        JSONObject jasonObject = JSONObject.fromObject(json);
        Map<String, Object> jsonMap = (Map) jasonObject;

        return jsonMap;
    }
}
