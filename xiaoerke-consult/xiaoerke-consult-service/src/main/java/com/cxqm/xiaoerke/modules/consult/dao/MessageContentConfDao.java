package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.MessageContentConfVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;

import java.util.Date;
import java.util.List;

/**
 * 文案配置dao
 *
 * Created by sunxiao on 2016-10-31.
 */
@MyBatisDao
public interface MessageContentConfDao {

    List<MessageContentConfVo> findMessageContentConfByInfo(MessageContentConfVo vo);

    void saveMessageContentConf(MessageContentConfVo vo);

    void updateMessageContentConf(MessageContentConfVo vo);

    void deleteMessageContentConf(MessageContentConfVo vo);

    MessageContentConfVo messageConfInfo(String msgType);
}
