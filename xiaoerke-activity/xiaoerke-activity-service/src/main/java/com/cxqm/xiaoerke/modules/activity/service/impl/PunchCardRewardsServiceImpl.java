package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.activity.dao.PunchCardRewardsDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardsVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.activity.service.PunchCardRewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
@Service
@Transactional(readOnly = false)
public class PunchCardRewardsServiceImpl implements PunchCardRewardsService {

    @Autowired
    private PunchCardRewardsDao punchCardRewardsDao;

    @Autowired
    private OlyGamesService olyGamesService ;

    @Override
    public int deleteByPrimaryKey(String id) {
        return punchCardRewardsDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PunchCardRewardsVo record) {
        return punchCardRewardsDao.insert(record);
    }

    @Override
    public int insertSelective(PunchCardRewardsVo record) {
        return punchCardRewardsDao.insertSelective(record);
    }

    @Override
    public PunchCardRewardsVo selectByPrimaryKey(String id) {
        return punchCardRewardsDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PunchCardRewardsVo record) {
        return punchCardRewardsDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PunchCardRewardsVo record) {
        return punchCardRewardsDao.updateByPrimaryKey(record);
    }

    @Override
    public Map<String, Object> getSelfRewardsInfo(String openId) {
        return punchCardRewardsDao.getSelfRewardsInfo(openId);
    }

    @Override
    public List<Map<String, Object>> getPunchCardRewards(PunchCardRewardsVo vo) {
        return punchCardRewardsDao.getPunchCardRewards(vo);
    }

    @Override
    public int batchInsertPunchCardRewards(List list) {
        return punchCardRewardsDao.batchInsertPunchCardRewards(list);
    }

    @Override
    public Page<Map<String, Object>> findPunchCardRewardsByPage(Page<PunchCardRewardsVo> page, HashMap<String, Object> hashMap) {
        return punchCardRewardsDao.findPunchCardRewardsByPage(page, hashMap);
    }

    public HashMap<String, Object> getPunchCardRewardByPage(HashMap<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String pageNo = String.valueOf(params.get("pageNo"));
        String pageSize = String.valueOf(params.get("pageSize"));
        //设值
        Page<PunchCardRewardsVo> page = FrontUtils.generatorPage(pageNo, pageSize);//暂设设固定值
        //取数据
        Page<Map<String, Object>> resultPage = this.findPunchCardRewardsByPage(page, params);
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<Map<String, Object>> list = resultPage.getList();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if(map != null && map.size()>0){
                    String openId = String.valueOf(map.get("openId"));
                    String headImgUrl = olyGamesService.getWechatMessage(openId);
                    if(StringUtils.isNotNull(headImgUrl)){
                        map.put("headImgUrl",headImgUrl);
                    }else {
                        map.put("headImgUrl","");
                    }
                    resultList.add(map);
                }
            }
            response.put("personRewardsList", resultList);
        } else {
            response.put("personRewardsList", new ArrayList());
        }
        return response;
    }

}
