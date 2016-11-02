package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.MessageContentConfDao;
import com.cxqm.xiaoerke.modules.consult.entity.MessageContentConfVo;
import com.cxqm.xiaoerke.modules.consult.service.MessageContentConfService;
import com.cxqm.xiaoerke.modules.sys.utils.UUIDUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文案配置实现类
 * @author sunxiao
 * 2016-10-31
 */

@Service
public class MessageContentConfServiceImpl implements MessageContentConfService {

    @Autowired
    MessageContentConfDao messageContentConfDao;

    @Override
    public List<MessageContentConfVo> findMessageContentConfByInfo(MessageContentConfVo vo) {
        return messageContentConfDao.findMessageContentConfByInfo(vo);
    }

    @Override
    public String saveMessageContentConf(MessageContentConfVo vo) {
        if(StringUtils.isNotNull(vo.getId()+"")){
            messageContentConfDao.updateMessageContentConf(vo);
            return "修改成功";
        }else{
            List<MessageContentConfVo> list = messageContentConfDao.findMessageContentConfByInfo(vo);
            if(!"0".equals(vo.getPriority()) && list.size()==0){
                return "还没有优先级为0的场景！";
            }
            if(list.size()!=0){
                for(MessageContentConfVo temp : list){
                    if(temp.getPriority().equals(vo.getPriority())){
                        return "该优先级的场景已存在！";
                    }
                }
            }
            messageContentConfDao.saveMessageContentConf(vo);
            return "保存成功";
        }
    }


    @Override
    public void deleteMessageContentConf(MessageContentConfVo vo) {
        messageContentConfDao.deleteMessageContentConf(vo);
    }
}
