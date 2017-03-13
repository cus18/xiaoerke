package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.activity.service.AdvisorySharingService;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by wangbaowei on 17/2/28.
 */
@Service
public class AdvisorySharingServiceImpl implements AdvisorySharingService{


    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private ConsultSessionService consultSessionService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Override
    public Map<String ,Object> conversationRecord(String sessionid,int pageNo,int pageSize) {
        Map<String ,Object> resultMap = new HashMap<String, Object>();

        //聊天记录
        Query query = new Query(where("sessionId").is(sessionid)).with(new Sort(Sort.Direction.ASC, "createDate"));
        PaginationVo<ConsultRecordMongoVo> pagination = consultRecordService.getRecordDetailInfo(pageNo, pageSize, query, "permanent");
        List<ConsultRecordMongoVo> recordList  = pagination.getDatas();
        resultMap.put("recordList",recordList);
        //医生信息
        ConsultSession consultSession = consultSessionService.selectByPrimaryKey(Integer.parseInt(sessionid));
        ConsultDoctorInfoVo doctorInfoVo = consultDoctorInfoService.getConsultDoctorInfoByUserId(consultSession.getCsUserId());
        resultMap.put("doctorInfoVo",doctorInfoVo);
        //留言信息
        String content = (String) redisTemplate.opsForValue().get("haveRecord"+sessionid);
        content = StringUtils.isNotNull(content)?content:"你的宝宝也需要这样一个医生哦～";
        resultMap.put("content",content);
        //患者信息
        WechatAttention attentionInfo = wechatAttentionService.getAttentionByOpenId(consultSession.getUserId());
        String headImgUrl = olyGamesService.getWechatMessage(consultSession.getUserId());//头像
        if(StringUtils.isNotNull(headImgUrl)){
            resultMap.put("headImgUrl", headImgUrl);
        }else {
            resultMap.put("headImgUrl", "http://img5.imgtn.bdimg.com/it/u=1846948884,880298315&fm=21&gp=0.jpg");
        }
        resultMap.put("attentionInfo",attentionInfo);
        return resultMap;
    }

    @Override
    public void sharSeConsult(String sessionid, String content) {
        redisTemplate.opsForValue().set(sessionid,content);
    }
}
