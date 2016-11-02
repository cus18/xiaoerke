package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.MessageContentConfVo;

import java.util.List;
import java.util.Map;

/**
 * 文案配置service
 * @author sunxiao
 * 2016-10-31
 */
public interface MessageContentConfService {

    List<MessageContentConfVo> findMessageContentConfByInfo(MessageContentConfVo vo);

    String saveMessageContentConf(MessageContentConfVo vo);

    void deleteMessageContentConf(MessageContentConfVo vo);

}
