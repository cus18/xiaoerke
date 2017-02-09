package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinDao;
import com.cxqm.xiaoerke.modules.consult.dao.BabyCoinRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            babyCoinVo.setCash(babyCoinVo.getCash()+10);
            updateBabyCoinByOpenId(babyCoinVo);
        }
        //发消息
        String url = "";
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        WechatMessageUtil.templateModel("亲！有宝宝币入账啦",wechatAttentionVo.getWechat_name(),count+"个", DateUtils.DateToStr(new Date(),"yyyy年MM月dd日"),"","时间有限，宝宝币要抓紧使用哦，不要过期浪费啦~\n" +
                "点击进入宝宝币兑换中心",token,url,openid,"U-0n4vv3HTXzOE4iD5hZ1siCjbpFVTPpFsXrxs4ASK8");
        return 0;
    }

}
