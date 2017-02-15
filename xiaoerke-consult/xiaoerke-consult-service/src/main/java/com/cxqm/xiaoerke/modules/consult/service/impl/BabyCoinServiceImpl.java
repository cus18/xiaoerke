package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinDao;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by deliang on 16/09/05.
 */

@Service
public class BabyCoinServiceImpl implements BabyCoinService {

    @Autowired
    private BabyCoinDao babyCoinDao;

    @Autowired
    private BabyCoinRecordDao babyCoinRecordDao;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private MongoDBService<RedPacketInfoVo> redPacketInfoService;

    @Autowired
    private MongoDBService<RedPacketRecordVo> redPacketRecordService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    private static final String USER_SESSIONID_KEY = "redpacket";

    @Override
    public BabyCoinVo selectByBabyCoinVo(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectByBabyCoinVo(babyCoinVo);
    }

    @Override
    public List<BabyCoinVo> selectListByBabyCoinVo(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectListByBabyCoinVo(babyCoinVo);
    }

    @Override
    public List<BabyCoinVo> selectSubBabyCoin(BabyCoinVo babyCoinVo){
        return babyCoinDao.selectSubBabyCoin(babyCoinVo);
    }

    @Override
    public List<BabyCoinRecordVo> selectByBabyCoinRecordVo(BabyCoinRecordVo babyCoinRecordVo){
        return babyCoinRecordDao.selectByBabyCoinRecordVo(babyCoinRecordVo);
    }

    @Override
    public int insertBabyCoinRecord(BabyCoinRecordVo record){
        return babyCoinRecordDao.insertSelective(record);
    }

    @Override
    public int updateCashByOpenId(BabyCoinVo record){
        return babyCoinDao.updateCashByOpenId(record);
    }

    @Override
    public int updateBabyCoinInviteNumber(BabyCoinVo babyCoinVo){
        return babyCoinDao.updateBabyCoinInviteNumber(babyCoinVo);
    }

    @Override
    public int updateBabyCoinByOpenId(BabyCoinVo record){
        return babyCoinDao.updateByOpenId(record);
    }

    @Override
    public int insertBabyCoinSelective(BabyCoinVo record){
        return babyCoinDao.insertSelective(record);
    }

    @Override
    public int giveBabyCoin(String openid, Long count) {
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openid);
        babyCoinVo = selectByBabyCoinVo(babyCoinVo);
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openid);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        if (babyCoinVo == null || babyCoinVo.getCash() == null) {
            synchronized (this) {
                babyCoinVo = new BabyCoinVo();
                babyCoinVo.setCash(count);
                babyCoinVo.setCreateBy(openid);
                babyCoinVo.setCreateTime(new Date());
                babyCoinVo.setOpenId(openid);
                BabyCoinVo lastBabyCoinUser = new BabyCoinVo();
                lastBabyCoinUser.setCreateTime(new Date());
                lastBabyCoinUser = selectByBabyCoinVo(lastBabyCoinUser);
                if (wechatAttentionVo != null && wechatAttentionVo.getWechat_name() != null) {
                    babyCoinVo.setNickName(wechatAttentionVo.getWechat_name());
                }
                if (lastBabyCoinUser == null || lastBabyCoinUser.getMarketer() == null) {
                    babyCoinVo.setMarketer("110000000");//初始值
                } else {
                    babyCoinVo.setMarketer(String.valueOf(Integer.valueOf(lastBabyCoinUser.getMarketer()) + 1));
                }
                insertBabyCoinSelective(babyCoinVo);
            }
        }else{
            babyCoinVo.setCash(babyCoinVo.getCash()+count);
            updateBabyCoinByOpenId(babyCoinVo);
        }
        //发消息
        String url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/fieldwork/wechat/author?url=" + sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wechatInfo/getUserWechatMenId?url=48";
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        WechatMessageUtil.templateModel("亲！有宝宝币入账啦",wechatAttentionVo.getWechat_name(),count+"枚", DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"),"","时间有限，宝宝币要抓紧使用哦，不要过期浪费啦~\n" +
                "点击进入宝宝币兑换中心",token,url,openid,"U-0n4vv3HTXzOE4iD5hZ1siCjbpFVTPpFsXrxs4ASK8");
        return 0;
    }

    @Override
    public Map<String,Object> redPacketShare(String openid, String packetId) {
        Map<String,Object> resultMap = new HashMap<String, Object>();

        //先查询当前用户是否已经领取红包,如果领取过则直接返回领取结果
        Query queryRedPackRecord = new Query();
        queryRedPackRecord.addCriteria(Criteria.where("packetId").is(packetId));
        List<RedPacketRecordVo> recordVoList = redPacketRecordService.queryList(queryRedPackRecord);
        resultMap.put("recordVoList",recordVoList);

        for(RedPacketRecordVo recordVo : recordVoList){
            if(recordVo.getOpenid().equals(openid)){
                resultMap.put("balance",recordVo.getCount());
                resultMap.put("packetstatus","receive");
                return resultMap;
            }
        }


        //查询红包发配情况
        Query queryInLog = new Query();
        queryInLog.addCriteria(Criteria.where("_id").is(packetId));
        List<RedPacketInfoVo> li = redPacketInfoService.queryList(queryInLog);
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        if(null != li&&li.size()>0){
            RedPacketInfoVo vo =  li.get(0);

            String redPacketNum = sysPropertyVoWithBLOBsVo.getRedPacketNum();

            if(vo.getBalance()>0){
                int packentNum = Integer.parseInt(redPacketNum)-recordVoList.size();
                //红包数量
                long shareCoin = Math.round(vo.getBalance()/packentNum);
                if(packentNum>1){
                    if(Math.random()*10%2==0){
                        shareCoin =  new Double(shareCoin*(1+Math.random())).longValue();
                    }else{
                        shareCoin =  new Double(shareCoin*(1-Math.random())).longValue();
                    }
                }
                resultMap.put("balance",shareCoin);
                giveBabyCoin(openid,shareCoin);

                RedPacketRecordVo recordVo = new RedPacketRecordVo();
                recordVo.setPacketId(packetId);
                recordVo.setCreate_time(new Date());
                recordVo.setCount(shareCoin);
                recordVo.setOpenid(openid);
                Map parameter = systemService.getWechatParameter();
                String token = (String) parameter.get("token");
                WechatBean wechatInfo = WechatUtil.getWechatName(token,openid);
                recordVo.setHeadPic(wechatInfo.getHeadimgurl());
                recordVo.setNickName(wechatInfo.getNickname());
                redPacketRecordService.insert(recordVo);

                redPacketInfoService.upsert((queryInLog),
                        new Update().update("balance", vo.getBalance()-shareCoin));
                resultMap.put("packetstatus","share");
                resultMap.put("recordVoList",recordVoList);
            }else{
                resultMap.put("packetstatus","isend");
            }
        }
        return resultMap;
    }

    @Override
    public String redPacketInit(RedPacketInfoVo re) {
        String packetId =  (String)redisTemplate.opsForValue().get("redPacket_"+re.getOpenid());
        if(null == packetId){
            redPacketInfoService.insert(re);
            redisTemplate.opsForValue().set("redPacket_"+re.getOpenid(), re.getId(),24, TimeUnit.HOURS);
            packetId = re.getId();
        }
        return packetId;
    }

//    @Override
//    public String redPacketInfo(String packetId) {
//        Query queryInLog = new Query();
//        queryInLog.addCriteria(Criteria.where("id").is(packetId));
//        List<RedPacketInfoVo> li = redPacketInfoService.queryList(queryInLog);
//        if (null != li && li.size() > 0) {
//            RedPacketInfoVo vo = li.get(0);
//            vo.getBalance();
//         }
//        Query queryRedPackRecord = new Query();
//        queryInLog.addCriteria(Criteria.where("packetId").is(packetId));
//        List<RedPacketRecordVo> recordVoList = redPacketRecordService.queryList(queryRedPackRecord);
//        return "";
//    }

}
