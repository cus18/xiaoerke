package com.cxqm.xiaoerke.modules.search.service;


import java.util.HashMap;

public interface InternalSearchService {
	
	String[] searchDoctors(String queryStr, Integer limit, Integer startIndex, String autoPromptFlag) throws Exception;

	int keywordsIllnessRelImport();

}
