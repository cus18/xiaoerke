package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultStatisticDao;
import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.operation.dao.StatisticsTitleDao;
import com.cxqm.xiaoerke.modules.operation.entity.StatisticsTitle;
import com.cxqm.xiaoerke.modules.operation.service.BaseDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基础数据统计 实现
 *
 * @author zhangbo  @move deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class BaseDataServiceImpl implements BaseDataService {


    @Autowired
    private StatisticsTitleDao statisticsTitleDao;

	@Autowired
	private BabyUmbrellaInfoDao babyUmbrellaInfoDao;

	@Autowired
	private ConsultStatisticDao consultStatisticDao;

    @Override
    public int insertStatisticsTitle() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("startDate", "2015-9-25");
            map.put("endDate", "2015-10-1");
            List<HashMap<String, Object>> list = statisticsTitleDao.findStatisticsTitleList(map);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            //如果List为空则为Statistics表初始化数据
            if (list == null || list.size() == 0) {
                Date date1 = sdf.parse("2015-9-17");
                Date date2 = new Date();
                long timeDiff = date2.getTime() - date1.getTime();
                int dayDiff = (int) (timeDiff / 1000 / 3600 / 24);  //初始化开始时间距今几天。
                for (int i = 0; i < dayDiff; i++) {
                    cal.setTime(date1);
                    cal.add(Calendar.DATE, i);
                    HashMap<String, String> m = new HashMap<String, String>();
                    m.put("create_date", sdf.format(cal.getTime()));
                    StatisticsTitle addList = returnStatisticsTitleList(m, dayDiff);
                    statisticsTitleDao.insertStatisticsTitle(addList);
                }
                return dayDiff;
            } else {
                HashMap<String, String> m = new HashMap<String, String>();
                Date d = new Date();
                cal.setTime(d);
                cal.add(Calendar.DATE, -1);
                System.out.println(sdf.format(cal.getTime()));
                m.put("create_date", sdf.format(cal.getTime()));
                StatisticsTitle addList = returnStatisticsTitleList(m,1);
                return statisticsTitleDao.insertStatisticsTitle(addList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
	
    public StatisticsTitle returnStatisticsTitleList(HashMap<String, String> m, int num) {
        StatisticsTitle st = new StatisticsTitle();
        try {
            List<HashMap<String, Object>> order = statisticsTitleDao.selectIntoOrder(m);
//            List<HashMap<String, Object>> zyqSay = statisticsTitleDao.selectIntoZYQSayStatistics(m);
            List<HashMap<String, Object>> userAndDoctor = statisticsTitleDao.selectIntoUserAndDoctorNumsStatistics(m);

//			int loginNum = statisticsTitleDao.getVisiteUserNum(m);
//			st.setVisiteNum(loginNum);
			st.setVisiteNum(0);

			//新增会员数量
			int addVIP=statisticsTitleDao.getAddVipNum(m);
			st.setAddVIP(addVIP);
			
			//取消会员数量
			int diffVIP=statisticsTitleDao.getDiffVipNum(m);
			st.setDiffVIP(diffVIP);
			
			//净增会员数量
			st.setNetVIP(addVIP-diffVIP);
			
			//累计会员数量
			int totalVIP=statisticsTitleDao.getTotalVipNum(m)+st.getNetVIP();
			st.setTotalVIP(totalVIP);
			
            String date = m.get("create_date");
            //订单和咨询部分
            orderAndConsult(st, order, date);
            //郑玉巧说部分
//            zhengYuQiao(st, zyqSay, date);
            //用户和医生部分
            userAndDoctor(st, userAndDoctor, date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return st;
    }

    @Override
    public List<HashMap<String, Object>> findStatisticsTitleList(String startDateStr, String endDateStr) {
		Date startDate = DateUtils.StrToDate(startDateStr, "date");
		Date endDate = DateUtils.StrToDate(endDateStr,"date");

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Map<String, Object> searchMap0 = new HashMap<String, Object>();
		searchMap0.put("date", startDate);
		Integer totalUser0 = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalUser(searchMap0);

		Integer totalFamily0 = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalFamily(searchMap0);

		while(startDate.getTime() < DateUtils.addDays(endDate, 1).getTime()) {
			startDate = DateUtils.addDays(startDate, 1);
			Map<String, Object> searchMap = new HashMap<String, Object>();
			searchMap.put("date", startDate);
			Integer totalUser = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalUser(searchMap);
			Integer totalFamily = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalFamily(searchMap);
			Integer addUser = totalUser-totalUser0;
			Integer addFamily = totalFamily-totalFamily0;

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("date",DateUtils.formatDate(DateUtils.addDays(startDate, -1)));
			map.put("totalUser",totalUser);
			map.put("totalFamily",totalFamily);
			map.put("addUser",addUser);
			map.put("addFamily",addFamily);


			Map<String, Object> iniMap = new HashMap<String, Object>();
			iniMap.put("startDate",DateUtils.formatDate(DateUtils.addDays(startDate, -1)));
			iniMap.put("endDate",DateUtils.formatDate(DateUtils.addDays(startDate, -1)));
			List<Map<String, Object>> validateConsultAndFeedBackCountsList = consultStatisticDao.getValidateConsultAndFeedBackCounts(iniMap);
			List<Map<String, Object>> sendHeartPersonAndMoneyCountsList =  consultStatisticDao.getSendHeartPersonAndMoneyCounts(iniMap);
			for (int i = 0; i < validateConsultAndFeedBackCountsList.size(); i++) {
				Map<String, Object> validateMap = validateConsultAndFeedBackCountsList.get(i);
				map.put("feedBackCount",validateMap.get("feedBackCount"));
				map.put("validateConsultCount",validateMap.get("validateConsultCount"));
				for (int j = 0; j < sendHeartPersonAndMoneyCountsList.size(); j++) {
					Map<String, Object> sendMap = sendHeartPersonAndMoneyCountsList.get(j);
					if(sendMap.get("date").toString().equalsIgnoreCase(validateMap.get("date").toString())){
						map.put("sendHeartPersonCount",sendMap.get("sendHeartPersonCount").toString());
						map.put("sendHeartMoneyCount",sendMap.get("sendHeartMoneyCount").toString());
					}
				}
			}



 			list.add(map);
			totalFamily0 = totalFamily;
			totalUser0 = totalUser;
		}


        return list;
    }

    private void orderAndConsult(StatisticsTitle st, List<HashMap<String, Object>> order, String date) throws ParseException {
    	if(order.size()>0){
			 st.setAddOrder(0);
			 st.setCancelOrder(0);
			 st.setThatDayOrder(0);
			  st.setVictoryNums(0);
			for (HashMap<String, Object> hashMap : order) {
				int nums=Integer.parseInt(hashMap.get("type").toString());
				int count=Integer.parseInt(hashMap.get("count").toString());
			      if(nums==1){
			    	  st.setAddOrder(count);
			      }else if(nums==2){
			    	  st.setCancelOrder(count);
			      }else if(nums==4){
			    	  st.setThatDayOrder(count);
			      }
			}
			  st.setNetOrder(st.getAddOrder()-st.getCancelOrder());
			 List<HashMap<String,Object>> totalOrder= statisticsTitleDao.selectIntoDayToDaytotalOrder(date);
		      if(totalOrder.size()!=0){
		    	  st.setTotalAddOrder(0);
		    	  st.setTotalVictoryOrder(0);
		     for (HashMap<String, Object> m2: totalOrder) {
		    	int type=Integer.parseInt(m2.get("type").toString());
				int tcount=Integer.parseInt(m2.get("count").toString());
				if(type==1){
					st.setTotalAddOrder(tcount);
				}else if (type==2){
					st.setTotalVictoryOrder(tcount);
				}
			}
		     List<HashMap<String,Object>> mc=statisticsTitleDao.selectLastDayTotalOrder(date);
		     if(mc!=null&&mc.size()>0){
		    	 st.setTotalVictoryOrder(Integer.parseInt(mc.get(0).get("totalvictoryorder").toString())+st.getThatDayOrder());
		     }
		     if(date.equals("2015-09-30")){
		    	 st.setTotalVictoryOrder(737);
		     }
		      }
//		      List<HashMap<String,Object>> totalvOrder= statisticsTitleDao.selectRecord(date);
				HashMap<String,String> ma=new HashMap<String, String>();
				ma.put("create_date", date);
		      Integer victoryRecord=statisticsTitleDao.getTotalConsultNum(ma);
//				 List<HashMap<String,Object>> totalvRecord;
//				 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//				 Date nowDate=sdf.parse(date);
//				 Date lastDate=sdf.parse("2015-11-03");
//				 if(lastDate.before(nowDate)){
//					 totalvRecord=statisticsTitleDao.selectIntoDayToDaytotalRecordAfterNov(ma);
//				 }else{
//					 totalvRecord=statisticsTitleDao.selectIntoDayToDaytotalRecord(ma);
//				 }
//					 victoryRecord=totalvOrder.size()-totalvRecord.size();
					 st.setVictoryNums(victoryRecord);
		      List<HashMap<String,Object>> list=statisticsTitleDao.selectLastDayTotalRecord(date);
			if (list == null || list.size() == 0) {
				st.setTotalVictoryNums(victoryRecord);
			} else {
				st.setTotalVictoryNums(Integer.parseInt(list.get(0).get("totalvictorynums").toString())+victoryRecord);
			}
			if(date.equals("2015-09-17")){
				 st.setTotalVictoryNums(1297);
			 }
			if(date.equals("2015-09-30")){
				 st.setTotalVictoryNums(1833);
			 }
			List<HashMap<String,Object>> torder=statisticsTitleDao.getTruthOrderAndPayOrder(date);
			for (int i = 0; i < torder.size(); i++) {
				Integer status=Integer.parseInt(torder.get(i).get("status").toString());
				Integer count=Integer.parseInt(torder.get(i).get("count").toString());
				if(status==1){
					st.setTruthOrder(count);
				}
				if(status==2){
					st.setPayOrder(count);
				}
			}
		}
    }

    private void userAndDoctor(StatisticsTitle st, List<HashMap<String, Object>> userAndDoctor, String date) throws ParseException {
    	//用户和医生部分
		if(userAndDoctor.size()>0){
			st.setAddUserNum(0);
			st.setCancelNum(0);
			st.setAddDoctorNum(0);
			for (HashMap<String, Object> map : userAndDoctor) {
				int type=Integer.parseInt(map.get("type").toString());
				int count=Integer.parseInt(map.get("count").toString());
				if(type==1){
					st.setAddUserNum(count);
				}else if(type==2){
					st.setCancelNum(count);
				}else if(type==3){
					st.setAddDoctorNum(count);
				}
			}   
			st.setNetUserNum(st.getAddUserNum()-st.getCancelNum());
			List<HashMap<String,Object>> list=statisticsTitleDao.selectIntoUserAndDoctorTotalStatistics(date);
			st.setTotalUserNum(0);
			st.setNetDoctorNum(0);
			for (HashMap<String, Object> map : list) {
				int ttype=Integer.parseInt(map.get("type").toString());
				int tcount=Integer.parseInt(map.get("count").toString());
				if(ttype==1){
					st.setTotalUserNum(tcount);
				}else if(ttype==2){
					st.setNetDoctorNum(tcount);
				}
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			st.setCreateDate(sdf.parse(date));
			st.setUpdateDate(new  SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			//访问微信接口获取用户统计数据
			String acUrl="https://api.weixin.qq.com/datacube/getusersummary?access_token="+statisticsTitleDao.findWeChatToken();
			String params="{\"begin_date\": \""+date+"\",\"end_date\": \""+date+"\"}";
			int newcount=0;
			int cancelcount=0;
			JSONObject  dataJson=null;
			String newcancel=null;
			newcancel=HttpRequestUtil.wechatpost(acUrl, params);
			if(newcancel==null&&newcancel.indexOf("list")==-1){
				newcancel=HttpRequestUtil.wechatpost(acUrl, params);
			}
			if(newcancel!=null&&newcancel.indexOf("list")>0){
				dataJson=JSONObject.fromObject(newcancel);
				  if(dataJson!=null){
					  JSONArray data=dataJson.getJSONArray("list");
					  if(data!=null){
						JSONObject obj=null;
					for(int i=0,l=data.size();i<l;i++){
						obj=data.getJSONObject(i);
						newcount+=Integer.parseInt(obj.get("new_user").toString());
						cancelcount+=Integer.parseInt(obj.get("cancel_user").toString());
						}
					  }
					  }
			}
			st.setAddUserNum(newcount);
			st.setCancelNum(cancelcount);
			st.setNetUserNum(st.getAddUserNum()-st.getCancelNum());
			//获取累计用户数量
			String countUrl="https://api.weixin.qq.com/datacube/getusercumulate?access_token="+statisticsTitleDao.findWeChatToken();
			JSONObject  cJson=null;
			JSONArray cdata=null;
			 int count=0;
			 String countc=null;
			 countc=HttpRequestUtil.wechatpost(countUrl, params);
			 if(countc==null&&countc.indexOf("list")==-1){
				 countc=HttpRequestUtil.wechatpost(countUrl, params);
			 }
			 if(countc!=null&&countc.indexOf("list")>0){
				 cJson=JSONObject.fromObject(countc);
				 if(cJson!=null){
						cdata=cJson.getJSONArray("list");
						if(cdata!=null){
						 count=Integer.parseInt(cdata.getJSONObject(0).get("cumulate_user").toString());
						}
						 }
			 }
			 st.setTotalUserNum(count);
		}
    }

    private void zhengYuQiao(StatisticsTitle st, List<HashMap<String, Object>> zyqSay, String date) {
    	if(zyqSay.size()>0){
			st.setAddReadNums(0);
			st.setAddSecReadNums(0);
			st.setShareNums(0);
			for (HashMap<String, Object> map : zyqSay) {
				int type=Integer.parseInt(map.get("type").toString());
				int count=Integer.parseInt(map.get("count").toString());
				if(type==1){
					st.setAddReadNums(count);
				}else if(type==2){
					st.setAddSecReadNums(count);
				}else if(type==3){
					st.setShareNums(count);
				}
			}
			List<HashMap<String,Object>>  tread=statisticsTitleDao.selectIntoZYQSayTotal(date);
			st.setTotalReadNums(0);
			st.setTotalSecReadNums(0);
			st.setCookieTotal(0);
			for (HashMap<String, Object> hm : tread) {
				int ttype=Integer.parseInt(hm.get("type").toString());
				int tcount=Integer.parseInt(hm.get("count").toString());
				if(ttype==1){
					st.setTotalReadNums(tcount);
				}else if(ttype==2){
					st.setTotalSecReadNums(tcount);
				}else if(ttype==3){
					st.setCookieTotal(tcount);
				}
			}
		}
		 if(date.equals("2015-09-17")){
	    	 st.setTotalReadNums(32);
	     }else{
	    	 st.setTotalReadNums(statisticsTitleDao.selectLastDayTotalReadNums(date).size()==0?0:Integer.parseInt(statisticsTitleDao.selectLastDayTotalReadNums(date).get(0).get("totalreadnums").toString())+st.getAddReadNums());
	     }
		 st.setAddZYQPbe(statisticsTitleDao.getPatienBabyNum(date)==null?0:statisticsTitleDao.getPatienBabyNum(date).size());
		 Integer totalZYQPbe=statisticsTitleDao.getPatienBabyNumLastday(date).size()==0?0:Integer.parseInt(statisticsTitleDao.getPatienBabyNumLastday(date).get(0).get("totalZYQPbe").toString());
		 st.setTotalZYQPbe(totalZYQPbe+st.getAddZYQPbe());
    }

	@Override
	public String findWechatToken() {
		// TODO Auto-generated method stub
		return statisticsTitleDao.findWeChatToken();
	}
}
