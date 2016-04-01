package com.cxqm.xiaoerke.modules.sys.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cxqm.xiaoerke.common.test.SpringTransactionalContextTests;

public class DoctorServiceTest extends SpringTransactionalContextTests {
	
	@Autowired
	private DoctorInfoService doctorService;

    @Test
    public void findCountDoctorCountNmuber(){ 
    	int number = doctorService.findCountDoctorCountNmuber();
    	Assert.assertTrue(number < 0);
    }
    
    @Test
    public void insertdoctor(){ 
    }
    
    
    @Test
    public void findDoctorSettlementInfoByDate(){ 
    }
    
    @Test
    public void getAllWithdrawRecords(){ 
    }
	
}
