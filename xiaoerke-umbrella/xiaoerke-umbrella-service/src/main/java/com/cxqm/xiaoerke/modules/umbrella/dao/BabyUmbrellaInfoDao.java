package com.cxqm.xiaoerke.modules.umbrella.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/20.
 */
@MyBatisDao
public interface BabyUmbrellaInfoDao {


     BabyUmbrellaInfo selectByPrimaryKey(Integer id);

    /**
     * 添加保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int saveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoById(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 查询全部
     * @return
     */
    List<Map<String,Object>>  getBabyUmbrellaInfo(Map<String, Object> map);

    Integer getUserShareNums(@Param("id")String id);


    /**
     * 查询参加数量
     * @return
     */
    Integer  getBabyUmbrellaInfoTotal(Map<String, Object> map);


    Integer getTotalBabyUmbrellaInfoMoney(Map<String, Object> map);

    Map<String,Object> getOpenidStatus(@Param("openid")String openid);

    List getNotShareInfoFromLog(Map<String, Object> map);

    int getUmbrellaCount();


    /**
     * 更新保障金分享信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoIfShare(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 新版添加保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int newSaveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 根据Openid 删除一个宝护伞信息
     * @param openid
     * @return
     */
    int deleteUmbrellaByOpenid(String openid);

    /**
     * 更新支付信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoStatus(BabyUmbrellaInfo babyUmbrellaInfo);


    /**
     * 获取用户第几位加入
     * @param map
     * @return
     */
    int getUmbrellaRank(Map<String, Object> map);

    /**
     *根据手机号或者openid查询该用户是否购买宝护伞
     */
    List<Map<String,Object>> getIfBuyUmbrellaByOpenidOrPhone(Map<String,Object> map);

    /**
     *根据手机号查询该用户是否购买宝护伞
     */
    Map<String,Object> getStatusByPhone(Map<String,Object> map);
}
