import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

/**
 * Created by feibendechayedan on 16/5/20.
 */
@MyBatisDao
public interface BabyUmberllaInfoDao {

    /**
     * 添加保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int saveBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

}
