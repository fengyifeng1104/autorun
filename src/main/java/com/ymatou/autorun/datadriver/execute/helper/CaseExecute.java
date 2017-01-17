package com.ymatou.autorun.datadriver.execute.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.execute.CaseExecuteFlow;
import com.ymatou.autorun.datadriver.execute.impl.CaseExecuteFlowImpl;

public class CaseExecute    {
	
	public static void executeAndCheck(ImportData importData,GlobalData globalData) {
		CaseExecuteFlow caseExecuteFlow = new CaseExecuteFlowImpl(importData,globalData);
		caseExecuteFlow.beforeCall();
		caseExecuteFlow.callbeforeApis();
		caseExecuteFlow.callApi();
		caseExecuteFlow.commonCheck();
		caseExecuteFlow.userDefinedCheck();
		caseExecuteFlow.afterCall(); 
	}

	public static void executeAndCheck(List<ImportData> importDatas,GlobalData globalData){
		for(ImportData importData : importDatas){
			executeAndCheck(importData,globalData);
		}
	}

	public static JSONObject execute(ImportData importData,GlobalData globalData) {
		CaseExecuteFlow caseExecuteFlow = new CaseExecuteFlowImpl(importData,globalData);
		caseExecuteFlow.callbeforeApis();
		JSONObject ret = caseExecuteFlow.callApi();
		caseExecuteFlow.commonCheck();   
		caseExecuteFlow.userDefinedCheck();
		return ret;
	}
	
	public static List<JSONObject> execute(List<ImportData> importDatas,GlobalData globalData){
		 List<JSONObject> ret = new ArrayList<JSONObject>();
		 for(ImportData importData : importDatas){
			 ret.add(execute(importData, globalData));
			}
		 return ret;
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception {

		System.out.println(System.getProperty("user.dir")+"\\resource\\TestData.xls");
		System.out.println(System.getProperty("user.dir")+"\\resource\\TestData.xls");
		
	    File file = new File(System.getProperty("user.dir")+"\\resource\\TestData.xls");
   	
/*
		//data for MySql
		GlobalData globalData = new GlobalDataFromExcelImpl(file, 1);
		ImportData importData = new ImportDataFromDBImpl(10004);
		executeAndCheck(importData,globalData);*/


		
		
	}
		

		
}
