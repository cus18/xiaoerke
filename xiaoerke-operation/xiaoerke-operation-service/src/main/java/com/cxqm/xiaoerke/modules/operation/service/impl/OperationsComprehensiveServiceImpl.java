package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.modules.operation.dao.OperationComprehensiveDao;
import com.cxqm.xiaoerke.modules.operation.dao.SysStatisticsDao;
import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;
import com.cxqm.xiaoerke.modules.operation.service.OperationsComprehensiveService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 业务综合统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class OperationsComprehensiveServiceImpl implements OperationsComprehensiveService {

    @Autowired
    public SysStatisticsDao sysStatisticsDao;

    @Autowired
    private OperationComprehensiveDao operationComprehensiveDao;

    @Override
    public List<SysStatistics> getOperationsComprehensiveList(SysStatistics sysStatisticsVo) {
        //获取业务综合统计数据
        List<SysStatistics> sysStatisticsList = sysStatisticsDao.getOperationsComprehensiveList(sysStatisticsVo);
        return sysStatisticsList;
    }

    @Override
    public int insertBatchUserStatistic(List<HashMap<String, Object>> userLog){
        return operationComprehensiveDao.insertBatchUserStatistic(userLog);
    }

    @Override
    public List<WechatAttention> getQDStatisticData(HashMap paramMap) {
        return operationComprehensiveDao.getQDStatisticData(paramMap);
    }

    @Override
    public List<WechatAttention> getQDMarketerData(HashMap paramMap) {
        return operationComprehensiveDao.getQDMarketerData(paramMap);
    }

    @Override
    public List<WechatAttention> getQDCancelStatisticData(HashMap paramMap) {
        return operationComprehensiveDao.getQDCancelStatisticData(paramMap);
    }

    @Override
    public List<HashMap<String, Object>> getChannelStatisticData(HashMap hashMap) {
        return operationComprehensiveDao.getChannelStatisticData(hashMap);
    }




}
