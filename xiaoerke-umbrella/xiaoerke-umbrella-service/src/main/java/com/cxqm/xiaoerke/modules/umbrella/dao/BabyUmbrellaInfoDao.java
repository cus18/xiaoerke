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
     * 查询激活人数
     * @param map
     * @return
     */
    int getUmbrellaActivationCount(Map<String, Object> map);

    /**
     * 查询未激活人数
     * @param map
     * @return
     */
    int getUmbrellaNotActivationCount(Map<String, Object> map);

    /**
     * 查询激活家庭人数
     * @param map
     * @return
     */
    int getUmbrellaActivationFamilyPeopleCount(Map<String, Object> map);
}
