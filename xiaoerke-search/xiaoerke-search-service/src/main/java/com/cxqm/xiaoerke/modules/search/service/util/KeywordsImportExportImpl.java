package com.cxqm.xiaoerke.modules.search.service.util;


import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.utils.excel.ImportExcel;
import com.cxqm.xiaoerke.modules.search.dao.SearchKeywordIllnessRelationDao;
import com.cxqm.xiaoerke.modules.search.entity.SearchKeywordIllnessRelation;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.IllnessInfoService;

@Service
@Transactional(readOnly = false)
public class KeywordsImportExportImpl implements KeywordsImportExport {

	@Autowired
	private IllnessInfoService illnessInfoService;
	
	@Autowired
	private SearchKeywordIllnessRelationDao searchKeywordIllnessRelationDao;
	
	@Override
	public int importKeywordsIllnessRelation() {
		try {
			ImportExcel importExcel = new ImportExcel("C:\\Users\\ft\\Desktop\\123.xlsx", 1);
			int i = 1;
			int number = importExcel.getLastDataRowNum();
			while(i < number){
				Row row = importExcel.getRow(i);
				String keyword = row.getCell(0).getStringCellValue();
				String illness = row.getCell(1).getStringCellValue();
				int keywordId = (int) row.getCell(2).getNumericCellValue();
				//System.out.println("keyword=" + keyword + ", illness=" + illness + ", keywordId=" + keywordId);
				
		        List<IllnessVo> illnesses = illnessInfoService.findSecondIllnessByName(illness);
		        if(illnesses == null || illnesses.size() == 0) {
		        	System.out.println("keyword=" + keyword + ", illness=" + illness + ", keywordId=" + keywordId);
		        	i ++;
		        	continue;
		        }
		        for(IllnessVo illnessvo : illnesses) {
			        SearchKeywordIllnessRelation relation = new SearchKeywordIllnessRelation();
			        relation.setIllnessId(illnessvo.getId());
			        relation.setKeywordId(keywordId);
			        int a = searchKeywordIllnessRelationDao.insertSelective(relation);
		        }
		        
				i ++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int insertSelective(SearchKeywordIllnessRelation record) {
		return searchKeywordIllnessRelationDao.insertSelective(record);
	}

}
