import com.cxqm.xiaoerke.common.test.SpringTransactionalContextTests;
import com.cxqm.xiaoerke.modules.mutualHelp.entity.MutualHelpDonation;
import com.cxqm.xiaoerke.modules.mutualHelp.service.MutualHelpDonationService;
import net.sf.json.JSONSerializer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class serviceTest extends SpringTransactionalContextTests {

    @Autowired
    MutualHelpDonationService service;

    @Test
    public void test(){
//        System.out.println(service);
//        int n = service.getCount(1);
//        System.out.println(service.getDonatonDetail(null));
//        System.out.println(service.getCount(null));

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("userId","57b526cf36ab4dbe9498ef8f88824de2");
        map.put("openId","o3_NPwnW_RsWoGu2JmynSEL7LFBc");
        map.put("donationType",1);

        System.out.println(service.getDonatonDetail(map));

//        System.out.println(JSONSerializer.toJSON(service.getDonatonDetail(map)).toString());
        System.out.println(JSONSerializer.toJSON(service.getLastNote(map)).toString());
    }

    @Test
    public void insert(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        MutualHelpDonation mutualHelpDonation = new MutualHelpDonation();
//        mutualHelpDonation.setOpenId(null);
        mutualHelpDonation.setLeaveNote("愿安好！");
        mutualHelpDonation.setMoney(23);

        int n = service.saveNoteAndDonation(mutualHelpDonation);
        System.out.println();
//        System.out.println(JSONSerializer.toJSON(service.saveNoteAndDonation(map)).toString());
        
    }
}
