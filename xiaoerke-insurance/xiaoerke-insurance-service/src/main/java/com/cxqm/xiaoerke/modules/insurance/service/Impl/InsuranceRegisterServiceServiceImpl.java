package com.cxqm.xiaoerke.modules.insurance.service.Impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.insurance.dao.InsuranceRegisterServiceDao;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = false)
public class InsuranceRegisterServiceServiceImpl implements
		InsuranceRegisterServiceService {
	
	@Autowired
	InsuranceRegisterServiceDao insuranceRegisterServiceDao;

	@Autowired
    private UserDao userDao;
	
    @Autowired
	private UserInfoService userInfoService;
	
    @Autowired
    private BabyBaseInfoService babyBaseInfoService;
    
	@Override
	public Integer saveInsuranceRegisterService(
			InsuranceRegisterService insuranceRegisterService) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.saveInsuranceRegisterService(insuranceRegisterService);
	}

	@Override
	public InsuranceRegisterService getInsuranceRegisterServiceById(String id) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInsuranceRegisterServiceById(id);
	}

	@Override
	public List<Map<String, Object>> getInsuranceRegisterServiceListByUserid(
			String userid) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInsuranceRegisterServiceListByUserid(userid);
	}

	@Override
	public Page<InsuranceRegisterService> findInsuranceServiceList(
			Page<InsuranceRegisterService> page, InsuranceRegisterService vo) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.findInsuranceServiceList(page,vo);
	}

	@Override
	public List<Map<String, Object>> getInsuranceRegisterServiceIfValid(
			String babyId) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInsuranceRegisterServiceIfValid(babyId);
	}

	@Override
	public List<Map<String, Object>> getInsuranceRegisterServiceVisitLeadPageLogByOpenid(
			String openid) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInsuranceRegisterServiceVisitLeadPageLogByOpenid(openid);
	}

	@Override
	public List<Map<String, Object>> getInsuranceRegisterServiceByOpenid(
			String openid) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInsuranceRegisterServiceByOpenid(openid);
	}

	@Override
	public List<Map<String, Object>> getValidInsuranceRegisterServiceListByUserid(
			String userid) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getValidInsuranceRegisterServiceListByUserid(userid);
	}

	@Override
	public List<Map<String, Object>> getInvalidInsuranceRegisterServiceListByUserid(
			String userid) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getInvalidInsuranceRegisterServiceListByUserid(userid);
	}

	@Override
	public void updateInsuranceRegisterServiceByState() {
		// TODO Auto-generated method stub
		insuranceRegisterServiceDao.updateEndInsuranceOrder();
		insuranceRegisterServiceDao.updateInsuranceOrder();
	}

	@Override
	public void updateInsuranceRegisterService(InsuranceRegisterService vo) {
		// TODO Auto-generated method stub
		insuranceRegisterServiceDao.updateInsuranceRegisterService(vo);
	}

	@Override
	public Map<String,Object> getPayRecordById(String id) {
		// TODO Auto-generated method stub
		return insuranceRegisterServiceDao.getPayRecordById(id);
	}

	/**
	 * 运维后台赠送保险
	 * sunxiao
	 */
	@Override
	public String giveInsuranceRegisterService(
			InsuranceRegisterService insuranceRegisterService) {
		// TODO Auto-generated method stub
		String retStr = "";
		String phone = insuranceRegisterService.getParentPhone();
        User user = new User();
        user.setLoginName(phone);
        Map map = userDao.getUserByLoginName(user);
        String id = "";
        if (map == null) {
            // 如果没有查询到患者信息，则新建一个用户
            String uid = IdGen.uuid();
            User nuser = new User();
            nuser.setId(uid);
            nuser.setLoginName(phone);
            nuser.setPhone(phone);
            nuser.setCreateDate(new Date());
            userDao.insert(nuser);
            PatientVo patientVo = new PatientVo();
            String spid = IdGen.uuid();
            patientVo.setId(spid);
            patientVo.setSysUserId(uid);
            patientVo.setStatus("2");
            patientVo.setAccountNumber(0);
            userInfoService.savePatient(patientVo);
            id = uid;
        } else {
            id = (String) map.get("id");
        }
        
        BabyBaseInfoVo bbvo = new BabyBaseInfoVo();
        bbvo.setUserid(id);
        bbvo.setName(insuranceRegisterService.getBabyName());
        List<BabyBaseInfoVo> babyList = babyBaseInfoService.getBabyInfoByInfo(bbvo);
        String babyId = null;
        if(babyList.size()==0){
        	//如果没有查到宝宝信息，则新加一个
        	BabyBaseInfoVo vo = new BabyBaseInfoVo();
            vo.setSex(insuranceRegisterService.getGender());
            vo.setBirthday(insuranceRegisterService.getBirthday());
            vo.setName(insuranceRegisterService.getBabyName());
            vo.setUserid(id);
            int result = babyBaseInfoService.insertSelective(vo);
            babyId = vo.getId()+"";
        }else {
        	babyId = babyList.get(0).getId()+"";
        }
        
        InsuranceRegisterService param = new InsuranceRegisterService();
        param.setParentId(id);
        param.setBabyId(babyId);
        List<InsuranceRegisterService> insuranceList = insuranceRegisterServiceDao.getInsuranceRegisterServiceByInfo(param);
        if(insuranceList.size()==0){
        	insuranceRegisterService.setBabyId(babyId);
        	insuranceRegisterService.setId(ChangzhuoMessageUtil.createRandom(true, 10));
        	insuranceRegisterService.setSource("give");
        	insuranceRegisterService.setState("1");//有效
        	insuranceRegisterService.setInsuranceType("1");
        	insuranceRegisterService.setUpdateBy(UserUtils.getUser().getName());
        	this.saveInsuranceRegisterService(insuranceRegisterService);
        	retStr = "yes";
        }else{
        	retStr = "no";
        }
        return retStr;
	}

	/**
	 * 查询所有订单
	 * sunxiao
	 * @param vo
	 * @return
	 */
	@Override
	public List<InsuranceRegisterService> getInsuranceServiceList(
			InsuranceRegisterService vo) {
		// TODO Auto-generated method stub
		List<InsuranceRegisterService> list = insuranceRegisterServiceDao.getInsuranceServiceList(vo);
		Map<String, Object> statusMap = new LinkedHashMap<String, Object>();
		statusMap.put("0", "待生效");
		statusMap.put("1", "有效");
		statusMap.put("2", "待审核");
		statusMap.put("3", "已赔付");
		statusMap.put("4", "已到期");
		statusMap.put("5", "审核失败");
		statusMap.put("6", "待支付");
		for(InsuranceRegisterService ivo : list){
			ivo.setState((String)statusMap.get(ivo.getState()));
			ivo.setSource("buy".equals(ivo.getSource())?"购买":"赠送");
		}
		return list;
	}
}
