package com.ymatou.autorun.datadriver.data.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ymatou.autorun.datadriver.base.utils.ExcelUtil;
import com.ymatou.autorun.datadriver.data.GlobalData;

public class GlobalDataFromExcelImpl implements GlobalData{

	private Map<String, String> testData;
	
	
	public GlobalDataFromExcelImpl(Map<String, String>testData){
		this.testData = testData;
	}


	public GlobalDataFromExcelImpl(File excelFile,int sheetIndex){
		Map<String, String> retMap = new HashMap<String,String>();
		String[][] ret = ExcelUtil.getData(excelFile, sheetIndex);
		for(int i=0;i<ret.length;i++){
			retMap.put(ret[i][0], ret[i][1]);
		}
		testData = retMap;
	}
	
	
	
	
	@Override
	public Map<String, String> getKeyVal() {
		return testData;
	}
	
	
	
	

}
