package com.cxqm.xiaoerke.modules.search.service.impl;

import com.cxqm.xiaoerke.common.utils.PropertiesLoader;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.search.service.InternalSearchService;
import com.cxqm.xiaoerke.modules.search.service.util.KeywordsImportExport;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.opensearch.javasdk.CloudsearchSearch;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class InternalSearchServiceOpenSearchImpl implements InternalSearchService {
    private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    @Autowired
    private DoctorInfoService doctorHospitalRelationService;

    @Autowired
    private HospitalInfoService hospitalInfoService;

    @Autowired
    private KeywordsImportExport keywordsImportExport;

    @Override
    public String[] searchDoctors(String queryStr, Integer limit, Integer startIndex, String autoPromptFlag) throws Exception {
        CloudsearchSearch search = XiaoerkeOpenSearch.getInstance().getCloudsearch();
        String searchStr = queryStr;
        if (null != limit)
            search.setHits(limit);
        if (null != startIndex)
            search.setStartHit(startIndex);

        if (queryStr.length() > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("doctor_name:'");
            sb.append(queryStr);
            sb.append("'");
            queryStr = sb.toString();
        }
        search.setQueryString(queryStr);
        search.setFormat("json");
        String doctors = search.search();
        JSONObject cheackJson = JSONObject.fromObject(doctors);
        JSONObject result = cheackJson.getJSONObject("result");
        JSONArray items = result.getJSONArray("items");

        if ("autoPromp".equals(autoPromptFlag)) {
            String[] autoNames = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("name")) {
                    autoNames[i] = item.getString("name").replace("<front>", "").replace("</front>", "");
                } else if (item.has("illness_name")) {
                    autoNames[i] = item.getString("illness_name").replace("<span>", "").replace("</span>", "");
                }
            }
            autoNames = StringUtils.stringDuplicateRemove(autoNames);
            String[] resultStr = new String[autoNames.length];
            int j = 0;
            for (int i = 0; i < autoNames.length; i++) {
                if (autoNames[i].indexOf(searchStr) != -1) {
                    resultStr[j] = autoNames[i];
                    j++;
                }
            }
            return resultStr;
        } else {
            String[] doctorIds = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("doctor_id")) {
                    doctorIds[i] = item.getString("doctor_id");
                } else {
                    doctorIds[i] = item.getString("id");
                }
            }
            return doctorIds;
        }

    }


    @Override
    public int keywordsIllnessRelImport() {
        return keywordsImportExport.importKeywordsIllnessRelation();
    }

}
