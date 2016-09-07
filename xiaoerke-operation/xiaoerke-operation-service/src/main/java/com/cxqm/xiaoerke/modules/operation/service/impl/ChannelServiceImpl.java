package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionDao;
import com.cxqm.xiaoerke.modules.operation.dao.SysStatisticsDao;
import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;
import com.cxqm.xiaoerke.modules.operation.service.ChannelService;
import com.cxqm.xiaoerke.modules.operation.service.OperationsComprehensiveService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    //    private OperationComprehensiveDao operationComprehensiveDao;
    private OperationsComprehensiveService operationsComprehensiveService;
    @Autowired
    public SysStatisticsDao sysStatisticsDao;

    @Autowired
    private WechatAttentionDao wechatattentionDao;

    @Override
    public List<HashMap<String, Object>> getTuiStatisticData(HashMap hashMap) {
        //渠道统计
        return operationsComprehensiveService.getChannelStatisticData(hashMap);
        // 备用 误删
        //        List<HashMap<String, Object>> listQdData = new ArrayList<HashMap<String, Object>>();
        //        //根据Marketer对sys_attention进行分组查询
        //        List<WechatAttention> listMarketer = operationsComprehensiveService.getQDMarketerData(hashMap);
        //        //查询sys_attention表查出这一段时间所有的关注用户
        //        List<WechatAttention> listData = operationsComprehensiveService.getQDStatisticData(hashMap);
        //        //获取这一段时间内的取消关注用户
        //        List<WechatAttention> cancelListData = operationsComprehensiveService.getQDCancelStatisticData(hashMap);
        //
        //        int newAddNum = 0;//新关注用户
        //        int cancelNum = 0;//取消关注用户
        //        int jinAddNum = 0;
        //        for (WechatAttention dataMarketer : listMarketer) {//遍历所有的Marketer
        //            if ((dataMarketer.getMarketer() != null && !(dataMarketer.getMarketer().equals("")))) {
        //                HashMap<String, Object> dataMap = new HashMap<String, Object>();
        //                for (WechatAttention data : listData) {//遍历所有的关注用户
        //                    if (data.getMarketer() != null && data.getStatus() != null) {
        //                        //如果所有的Marketer = 遍历所有的关注用户
        //                        if (data.getMarketer().equals(dataMarketer.getMarketer()) && data.getStatus().equals("0")) {
        //                            newAddNum++;
        //                            for (WechatAttention cancelData : cancelListData) {//如果所有的Marketer = 遍历所有的取消关注用户
        //                                if (cancelData.getOpenid().equals(data.getOpenid())) {
        //                                    cancelNum++;
        //                                }
        //                            }
        //                        }
        //
        //                    }
        //                }
        //                jinAddNum = newAddNum - cancelNum;
        //                dataMap.put("newAddNum", newAddNum);//新关注用户数
        //                dataMap.put("cancelNum", cancelNum);//取消关注用户数
        //                dataMap.put("jinAddNum", jinAddNum);//净关注用户数
        //                dataMap.put("marketer", dataMarketer.getMarketer());//推广渠道
        //                listQdData.add(dataMap);
        //                newAddNum = 0;//计算另一个marketer
        //                cancelNum = 0;
        //            }
        //        }
        //
        //        hashMap.put("startDate", "2015-01-01");
        //
        //        List<HashMap<String, Object>> listQdDataLeiji = new ArrayList<HashMap<String, Object>>();
        //        List<WechatAttention> listMarketerLeiji = operationsComprehensiveService.getQDMarketerData(hashMap);
        //        List<WechatAttention> listDataLeiji = operationsComprehensiveService.getQDStatisticData(hashMap);
        //        List<WechatAttention> cancelListDataLeiji = operationsComprehensiveService.getQDCancelStatisticData(hashMap);
        //
        //        int newAddNumLeiji = 0;//累计关注数
        //        int cancelNumLeiji = 0;//累计取消关注数
        //        int jinAddNumLeiji;//净增累计
        //        for (WechatAttention dataMarketer : listMarketerLeiji) {
        //            if (!(dataMarketer.getMarketer() == null)) {
        //                if (!(dataMarketer.getMarketer().equals(""))) {
        //                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
        //                    for (WechatAttention data : listDataLeiji) {
        //                        if (!(data.getMarketer() == null)) {
        //                            if (!(data.getStatus() == null)) {
        //                                if (data.getMarketer().equals(dataMarketer.getMarketer()) && data.getStatus().equals("0")) {
        //                                    newAddNumLeiji++;
        //                                    for (WechatAttention cancelData : cancelListDataLeiji) {
        //                                        if (cancelData.getOpenid().equals(data.getOpenid())) {
        //                                            cancelNumLeiji++;
        //                                        }
        //                                    }
        //                                }
        //                            }
        //                        }
        //                    }
        //                    jinAddNumLeiji = newAddNumLeiji - cancelNumLeiji;//全部关注-全部取消
        //                    dataMap.put("jinAddNumLeiji", jinAddNumLeiji);
        //                    dataMap.put("marketer", dataMarketer.getMarketer());
        //                    listQdDataLeiji.add(dataMap);
        //                    newAddNumLeiji = 0;
        //                    cancelNumLeiji = 0;
        //                }
        //            }
        //        }
        //
        //        for (Map listLeiJiData : listQdDataLeiji) {
        //            for (Map leiji : listQdData) {//所有marketer = 计算好了的累计关注数
        //                if (leiji.get("marketer").equals(listLeiJiData.get("marketer"))) {
        //                    leiji.put("leijiNum", listLeiJiData.get("jinAddNumLeiji"));//累计用户数
        //                }
        //            }
        //        }
        //        return listQdData;
        //        利用以下sql进行改造
        //        select * from (select marketer,count(*) from sys_attention where date BETWEEN '2016-02-20 00:00:00' and now()  and STATUS = '0' GROUP BY marketer ) a
        //        left join (select marketer,count(*) from sys_attention where date BETWEEN '2016-02-20 00:00:00' and now()  and STATUS = '1' GROUP BY marketer ) b
        //        on a.marketer = b.marketer

    }

    @Override
    public int insertChannel(ChannelInfo channelInfo){
        return sysStatisticsDao.insertChannel(channelInfo);
    }

    @Override
    public List<ChannelInfo> getChannelInfos(){
        return sysStatisticsDao.getChannelInfos();
    }
    @Override
    public List<HashMap<String, Object>> getChannelCategoryStatistics(HashMap hashMap){
        return sysStatisticsDao.getChannelCategoryStatistics(hashMap);
    }
    @Override
    public List<HashMap<String, Object>> getChannelDetailStatistics(HashMap hashMap){
        return sysStatisticsDao.getChannelDetailStatistics(hashMap);
    }

    @Override
    public List<HashMap<String, Object>> getUserStatisticsDepartment(HashMap hashMap){
        return sysStatisticsDao.getUserStatisticsDepartment(hashMap);
    }

    @Override
    public List<HashMap<String, Object>> getUserStatisticsChannel(HashMap hashMap) {
        return sysStatisticsDao.getUserStatisticsChannel(hashMap);
    }

    @Override
    public List<String> getAllChannels(){
        return sysStatisticsDao.getAllChannels();
    }

    @Override
    public List<HashMap<String, Object>> getAllConsultCountsByChannel(Map<String, Object> map){
        List<HashMap<String, Object>> totalList = sysStatisticsDao.getTotalConsultCountsByChannel(map);
        List<HashMap<String, Object>> newList = sysStatisticsDao.getNewConsultCountsByChannel(map);
        for (int i = 0; i < totalList.size(); i++) {
            Map<String,Object> totalMap = totalList.get(i);
            for (int j = 0; j < newList.size(); j++) {
                Map<String,Object> newMap = newList.get(j);
                if(newMap.get("marketer").toString().equalsIgnoreCase(totalMap.get("marketer").toString())){
                    totalMap.put("newConsultCounts",newMap.get("newConsultCounts").toString());
                }
            }
        }
        return totalList;
    }

    @Override
    public List<HashMap<String, Object>> getAllConsultCountsByDepartment(Map<String, Object> map){
        List<HashMap<String, Object>> totalList = sysStatisticsDao.getTotalConsultCountsByDepartment(map);
        List<HashMap<String, Object>> newList = sysStatisticsDao.getNewConsultCountsByDepartment(map);
        for (int i = 0; i < totalList.size(); i++) {
            Map<String,Object> totalMap = totalList.get(i);
            for (int j = 0; j < newList.size(); j++) {
                Map<String,Object> newMap = newList.get(j);
                if(newMap.get("department").toString().equalsIgnoreCase(totalMap.get("department").toString())){
                    totalMap.put("newConsultCounts",newMap.get("newConsultCounts").toString());
                }
            }
        }
        return totalList;
    }

    @Override
    public boolean isExistSameMarketer(Map<String, Object> map){
        List<Map<String, Object>> marketerList = sysStatisticsDao.getAllChannelsByMarketer(map);
        if(marketerList !=null && marketerList.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public int deleteChannelInfo(String channelId){
        return sysStatisticsDao.deleteChannelInfo(channelId);
    }

    @Override
    public List<HashMap<String, Object>> getNewUserAttentionAndRemainStatistics(Map<String, Object> map){
        return sysStatisticsDao.getNewUserAttentionAndRemainStatistics(map);
    }

    @Override
    public Page<WechatAttention> userChannelSearch(Page<WechatAttention> param, String openid,String nickname ,String todayAttention, String todayConsult) {
        Page page = wechatattentionDao.findUserChannelList(param,openid,nickname,todayAttention,todayConsult);
        return page;
    }
}
