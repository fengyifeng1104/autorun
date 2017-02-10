package com.ymatou.autorun.datadriver.execute.helper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.data.impl.GlobalDataFromProImpl;
import com.ymatou.autorun.datadriver.data.impl.ImportDataFromMySQLImpl;
import com.ymatou.autorun.datadriver.execute.CaseExecuteFlow;
import com.ymatou.autorun.datadriver.execute.impl.CaseExecuteFlowImpl;
import com.ymatou.autorun.datadriver.face.SqlSearch;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

public class CaseExecute    {
	
	public static void executeAndCheck(RunningDataDao runningDataDao,SqlSearch sqlSearch,List<RunningDataModel> runningDataModelList){
		for(RunningDataModel runningDataModel:runningDataModelList ){
			ImportData importData = new ImportDataFromMySQLImpl(runningDataDao,runningDataModel);
			GlobalData globalData = new GlobalDataFromProImpl();
			executeAndCheck(sqlSearch,importData,globalData);
		}
	}
	
	
	public static void executeAndCheck(SqlSearch sqlSearch,ImportData importData,GlobalData globalData) {
		CaseExecuteFlow caseExecuteFlow = new CaseExecuteFlowImpl(sqlSearch,importData,globalData);
		caseExecuteFlow.beforeCall();
		caseExecuteFlow.callbeforeApis();
		caseExecuteFlow.callApi();
		caseExecuteFlow.commonCheck();
		caseExecuteFlow.userDefinedCheck();
		caseExecuteFlow.afterCall(); 
	}



	public static JSONObject execute(SqlSearch sqlSearch,ImportData importData,GlobalData globalData) {
		CaseExecuteFlow caseExecuteFlow = new CaseExecuteFlowImpl(sqlSearch,importData,globalData);
		caseExecuteFlow.callbeforeApis();
		JSONObject ret = caseExecuteFlow.callApi();
		caseExecuteFlow.commonCheck();   
		caseExecuteFlow.userDefinedCheck();
		return ret;
	}
	

	/***
	 * 当做postman来使用
	 * @param runningDataDao
	 * @param sqlSearch
	 * @param runningDataModel
	 * @return
	 */
	public static JSONObject executeAsPostMan(RunningDataDao runningDataDao,SqlSearch sqlSearch,RunningDataModel runningDataModel){
		ImportData importData = new ImportDataFromMySQLImpl(runningDataDao,runningDataModel);
		GlobalData globalData = new GlobalDataFromProImpl();
		CaseExecuteFlow caseExecuteFlow = new CaseExecuteFlowImpl(sqlSearch,importData,globalData);
		caseExecuteFlow.beforeCall();
		caseExecuteFlow.callbeforeApis();
		JSONObject ret = caseExecuteFlow.callApi();
		caseExecuteFlow.afterCall(); 
		return ret;
	}
	
/*	
	
	
	public static void main(String[] args) throws Exception {

		System.out.println(System.getProperty("user.dir")+"\\resource\\TestData.xls");
		System.out.println(System.getProperty("user.dir")+"\\resource\\TestData.xls");
		
	    File file = new File(System.getProperty("user.dir")+"\\resource\\TestData.xls");
   	

		//data for MySql
		GlobalData globalData = new GlobalDataFromExcelImpl(file, 1);
		ImportData importData = new ImportDataFromDBImpl(10004);
		executeAndCheck(importData,globalData);


		
		
	}*/
		

		
}
