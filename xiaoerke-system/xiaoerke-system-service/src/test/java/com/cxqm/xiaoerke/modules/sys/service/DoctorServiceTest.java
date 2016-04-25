package com.cxqm.xiaoerke.modules.sys.service;

import com.cxqm.xiaoerke.common.test.SpringTransactionalContextTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DoctorServiceTest extends SpringTransactionalContextTests {
	
	@Autowired
	private DoctorInfoService doctorService;

    @Test
    public void findCountDoctorCountNmuber(){ 
    	int number = doctorService.findCountDoctorCountNmuber();
    	Assert.assertTrue(number < 0);
    }
    
    @Test
    public void test(){
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("id", "8f765bd2740846d9bb749a93a5a882f9");
        String doctorId = (String)doctorService.getDoctorIdByUserIdExecute(paramsMap).get("id");
        System.out.print(doctorId);
    }
    
    
    @Test
    public void findDoctorSettlementInfoByDate(){ 
    }
    
    @Test
    public void getAllWithdrawRecords(){ 
    }
	
}
