package com.cxqm.xiaoerke.modules.search.service.util;

import com.cxqm.xiaoerke.modules.search.entity.SearchKeywordIllnessRelation;

public interface KeywordsImportExport {
	
	int importKeywordsIllnessRelation();
	
	int insertSelective(SearchKeywordIllnessRelation record);

}
