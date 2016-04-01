package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.modules.operation.dao.OperationComprehensiveDao;
import com.cxqm.xiaoerke.modules.operation.service.UserActionStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户行为统计 实现
 *
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class UserActionStatisticServiceImpl implements UserActionStatisticService {

    @Autowired
    private OperationComprehensiveDao OperationComprehensiveDao;

    @Override
    public HashMap<String, Object> userOperationData(HashMap<String, Object> newMap) throws Exception {
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

        List<HashMap<String, Object>> userAppList = this.getAppUserListBetweenStartAndEndDate(newMap);
        List<HashMap<String, Object>> userConsultList = this.getConsultUserListBetweenStartAndEndDate(newMap);
        List<HashMap<String, Object>> userList = new ArrayList<HashMap<String, Object>>();

        int totalNum = 0;
        int totalAppNum = 0;
        int totalConsultNum = 0;
        int totalOldAppNum = 0;
        int totalOldConsultNum = 0;
        int apdocNum = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date startDate = null;
        try {
            startDate = sdf.parse((String) newMap.get("startDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String equalList = "";
        for (HashMap<String, Object> userApp : userAppList) {
            totalAppNum++;
            HashMap<String, Object> init = new HashMap<String, Object>();
            init.put("openid", userApp.get("openid"));
            init.put("name", userApp.get("name"));
            init.put("attentiondate", userApp.get("attentiondate"));
            init.put("marketid", userApp.get("marketid"));
            equalList = equalList + "," + userApp.get("openid");

            if (userApp.get("attentiondate") != null) {
                try {
                    boolean flag = (sdf.parse(userApp.get("attentiondate").toString())).before(startDate);
                    if (flag) {
                        totalOldAppNum++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            userList.add(init);
        }

        for (HashMap<String, Object> userConsult : userConsultList) {
            totalConsultNum++;
            if (!(equalList.contains((String) userConsult.get("openid")))) {
                HashMap<String, Object> init = new HashMap<String, Object>();
                init.put("openid", userConsult.get("openid"));
                init.put("name", userConsult.get("name"));
                init.put("attentiondate", userConsult.get("date"));
                init.put("marketid", userConsult.get("marketer"));
                userList.add(init);
            }

            if (userConsult.get("date") != null) {
                try {
                    boolean flag = (sdf.parse(userConsult.get("date").toString())).before(startDate);
                    if (flag) {
                        totalOldConsultNum++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }


        List<HashMap<String, Object>> userListStatistic = this.getStatisticData(newMap);

        List<HashMap<String, Object>> userListConsultTimes = this.getUserConsultListTimes(newMap);

        for (HashMap<String, Object> userInfo : userList) {
            totalNum++;
            HashMap<String, Object> userStatisticInfo = new HashMap<String, Object>();
            userStatisticInfo.put("date", newMap.get("startDate") + "——" + newMap.get("endDate"));

            if (userInfo.get("name") != null) {
                userStatisticInfo.put("name", userInfo.get("name"));
            } else {
                userStatisticInfo.put("name", null);
            }

            userStatisticInfo.put("openid", userInfo.get("openid"));

            HashMap<String, Object> consultMap = new HashMap<String, Object>();
            consultMap.put("startDate", newMap.get("startDate"));
            consultMap.put("endDate", newMap.get("endDate"));
            consultMap.put("openid", userInfo.get("openid"));

            for (HashMap<String, Object> userConsultTimes : userListConsultTimes) {
                if (userConsultTimes.get("openid").equals(userInfo.get("openid"))) {
                    userStatisticInfo.put("consultTimes", userConsultTimes.get("consultTimes"));
                }
            }

            if (userInfo.containsKey("marketid") != false) {
                userStatisticInfo.put("marketid", userInfo.get("marketid"));
            }
            if (userInfo.containsKey("attentiondate") != false) {
                userStatisticInfo.put("attentiondate", userInfo.get("attentiondate"));
            }

            int alhos = 0, ahosaldoc = 0, ahosaldep = 0, ahosadepaldoc = 0,
                    alfirstill = 0, afirstalsecill = 0, asecillaldoc = 0, asecillahosaldoc = 0, asecillalhos = 0, adatealhos = 0, adatealdoc = 0,
                    adateahosaldoc = 0, adocdetail = 0, adocadateserv = 0, adocaservdetail = 0, apdoc = 0, canceldoc = 0, appraisedoc = 0, sharedoc = 0,
                    servattention = 0, attentiondoc = 0, checkservroute = 0, checkservstatus = 0, checkdocaplocation = 0, checkdoc7daylocation = 0,
                    checkdoc7dayapbylocation = 0, myfirstpage = 0, myappointment = 0, myapdetail = 0, myselfinfo = 0, attdoc = 0;

            for (HashMap<String, Object> userListStatisticInfo : userListStatistic) {
                if (userListStatisticInfo.get("openid").equals(userInfo.get("openid"))) {
                    alhos = alhos + Integer.valueOf(userListStatisticInfo.get("alhos").toString());
                    ahosaldoc = ahosaldoc + Integer.valueOf(userListStatisticInfo.get("ahosaldoc").toString());
                    ahosaldep = ahosaldep + Integer.valueOf(userListStatisticInfo.get("ahosaldep").toString());
                    ahosadepaldoc = ahosadepaldoc + Integer.valueOf(userListStatisticInfo.get("ahosadepaldoc").toString());
                    alfirstill = alfirstill + Integer.valueOf(userListStatisticInfo.get("alfirstill").toString());
                    afirstalsecill = afirstalsecill + Integer.valueOf(userListStatisticInfo.get("afirstalsecill").toString());
                    asecillaldoc = asecillaldoc + Integer.valueOf(userListStatisticInfo.get("asecillaldoc").toString());
                    asecillahosaldoc = asecillahosaldoc + Integer.valueOf(userListStatisticInfo.get("asecillahosaldoc").toString());
                    asecillalhos = asecillalhos + Integer.valueOf(userListStatisticInfo.get("asecillalhos").toString());
                    adatealhos = adatealhos + Integer.valueOf(userListStatisticInfo.get("adatealhos").toString());
                    adatealdoc = adatealdoc + Integer.valueOf(userListStatisticInfo.get("adatealdoc").toString());
                    adateahosaldoc = adateahosaldoc + Integer.valueOf(userListStatisticInfo.get("adateahosaldoc").toString());
                    adocdetail = adocdetail + Integer.valueOf(userListStatisticInfo.get("adocdetail").toString());
                    adocadateserv = adocadateserv + Integer.valueOf(userListStatisticInfo.get("adocadateserv").toString());
                    adocaservdetail = adocaservdetail + Integer.valueOf(userListStatisticInfo.get("adocaservdetail").toString());
                    apdoc = apdoc + Integer.valueOf(userListStatisticInfo.get("apdoc").toString());
                    canceldoc = canceldoc + Integer.valueOf(userListStatisticInfo.get("canceldoc").toString());
                    appraisedoc = appraisedoc + Integer.valueOf(userListStatisticInfo.get("appraisedoc").toString());
                    sharedoc = sharedoc + Integer.valueOf(userListStatisticInfo.get("sharedoc").toString());
                    servattention = servattention + Integer.valueOf(userListStatisticInfo.get("servattention").toString());
                    attentiondoc = attentiondoc + Integer.valueOf(userListStatisticInfo.get("attentiondoc").toString());
                    checkservroute = checkservroute + Integer.valueOf(userListStatisticInfo.get("checkservroute").toString());
                    checkservstatus = checkservstatus + Integer.valueOf(userListStatisticInfo.get("checkservstatus").toString());
                    checkdocaplocation = checkdocaplocation + Integer.valueOf(userListStatisticInfo.get("checkdocaplocation").toString());
                    checkdoc7daylocation = checkdoc7daylocation + Integer.valueOf(userListStatisticInfo.get("checkdoc7daylocation").toString());
                    checkdoc7dayapbylocation = checkdoc7dayapbylocation + Integer.valueOf(userListStatisticInfo.get("checkdoc7dayapbylocation").toString());
                    myfirstpage = myfirstpage + Integer.valueOf(userListStatisticInfo.get("myfirstpage").toString());
                    myappointment = myappointment + Integer.valueOf(userListStatisticInfo.get("myappointment").toString());
                    myapdetail = myapdetail + Integer.valueOf(userListStatisticInfo.get("myapdetail").toString());
                    myselfinfo = myselfinfo + Integer.valueOf(userListStatisticInfo.get("myselfinfo").toString());
                    attdoc = attdoc + Integer.valueOf(userListStatisticInfo.get("attdoc").toString());

                }
            }
            userStatisticInfo.put("alhos", alhos);
            userStatisticInfo.put("ahosaldoc", ahosaldoc);
            userStatisticInfo.put("ahosaldep", ahosaldep);
            userStatisticInfo.put("ahosadepaldoc", ahosadepaldoc);
            userStatisticInfo.put("alfirstill", alfirstill);
            userStatisticInfo.put("afirstalsecill", afirstalsecill);
            userStatisticInfo.put("asecillaldoc", asecillaldoc);
            userStatisticInfo.put("asecillahosaldoc", asecillahosaldoc);
            userStatisticInfo.put("asecillalhos", asecillalhos);
            userStatisticInfo.put("adatealhos", adatealhos);
            userStatisticInfo.put("adatealdoc", adatealdoc);
            userStatisticInfo.put("adateahosaldoc", adateahosaldoc);
            userStatisticInfo.put("adocdetail", adocdetail);
            userStatisticInfo.put("adocadateserv", adocadateserv);
            userStatisticInfo.put("adocaservdetail", adocaservdetail);
            userStatisticInfo.put("apdoc", apdoc);
            userStatisticInfo.put("canceldoc", canceldoc);
            userStatisticInfo.put("appraisedoc", appraisedoc);
            userStatisticInfo.put("sharedoc", sharedoc);
            userStatisticInfo.put("servattention", servattention);
            userStatisticInfo.put("attentiondoc", attentiondoc);
            userStatisticInfo.put("checkservroute", checkservroute);
            userStatisticInfo.put("checkservstatus", checkservstatus);
            userStatisticInfo.put("checkdocaplocation", checkdocaplocation);
            userStatisticInfo.put("checkdoc7daylocation", checkdoc7daylocation);
            userStatisticInfo.put("checkdoc7dayapbylocation", checkdoc7dayapbylocation);
            userStatisticInfo.put("myfirstpage", myfirstpage);
            userStatisticInfo.put("myappointment", myappointment);
            userStatisticInfo.put("myapdetail", myapdetail);
            userStatisticInfo.put("myselfinfo", myselfinfo);
            userStatisticInfo.put("attdoc", attdoc);

            apdocNum = apdocNum + apdoc;
            listData.add(userStatisticInfo);
        }


        response.put("startDate", newMap.get("startDate"));
        response.put("endDate", newMap.get("endDate"));
        response.put("statisticData", listData);
        response.put("totalNum", totalNum);
        response.put("totalAppNum", totalAppNum);
        response.put("totalConsultNum", totalConsultNum);
        response.put("totalOldAppNum", totalOldAppNum);
        response.put("totalOldConsultNum", totalOldConsultNum);
        response.put("totalNewAppNum", totalAppNum - totalOldAppNum);
        response.put("totalNewConsultNum", totalConsultNum - totalOldConsultNum);
        response.put("apdocNum", apdocNum);
        return response;
    }


    public List<HashMap<String, Object>> getAppUserListBetweenStartAndEndDate(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getAppUserListBetweenStartAndEndDate(hashMap);
        return resultHashMap;
    }

    public List<HashMap<String, Object>> getConsultUserListBetweenStartAndEndDate(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getConsultUserListBetweenStartAndEndDate(hashMap);
        return resultHashMap;
    }

    public List<HashMap<String, Object>> getStatisticData(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getStatisticData(hashMap);
        return resultHashMap;
    }

    public List<HashMap<String, Object>> getUserConsultListTimes(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getUserConsultListTimes(hashMap);
        return resultHashMap;
    }
}
