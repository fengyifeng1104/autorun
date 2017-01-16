package com.ymatou.autorun.datadriver.execute.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.data.AssertData;
import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.execute.APICall;
import com.ymatou.autorun.datadriver.execute.CaseExecuteFlow;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;
import com.ymt.core.tool.Logger;

public class CaseExecuteFlowImpl implements CaseExecuteFlow{
	
	private ImportData importData;
	private GlobalData globalData;
	private AssertData assertData;
	
	private Map<Integer,JSONObject> beforeApiRet = new HashMap<Integer,JSONObject>();
	private JSONObject allRet = new JSONObject();
	private APICall apiCall;
	
	
	public CaseExecuteFlowImpl(ImportData importData,GlobalData globalData){
		this.importData = importData;
		this.globalData = globalData;
	
	}


	public void beforeCall() {
		Logger.createResultFile(importData.getCaseSummary());
		Logger.comment("=================Start Case id:["+ importData.getCaseId() +"], scenario:"+importData.getScenario()+", summary:"+importData.getScenarioSummary()+"=================");

	}

	public void afterCall() {
		Logger.comment("=================End Test Case=================");
		Logger.generateResult(importData._getApi());
		
	}
	
	
	public Map<Integer, JSONObject> callbeforeApis() {
		Map<Integer,JSONObject> ret = new HashMap<Integer,JSONObject>();
		Map<Integer,ImportData> beforeCasesMap = importData._getDependCaseIds();
		if(beforeCasesMap.size()>0){
			Logger.comment("<<<<<<<<<<call before Apis Start, ParentId:"+ importData.getCaseId() +">>>>>>>>>>");
			//ret = new HashMap<Integer,JSONObject>();
			for(Integer id: beforeCasesMap.keySet()){
				ImportData beforeData = beforeCasesMap.get(id);
				Logger.comment("before case Info - case id:["+ id +"], scenario:"+beforeData.getScenario()+", summary:"+beforeData.getScenarioSummary());
				JSONObject beforeRet = CaseExecute.execute(beforeData,globalData);
				ret.put(id, beforeRet);
			}
			Logger.comment("<<<<<<<<<<call before Apis End, ParentId:"+ importData.getCaseId() +">>>>>>>>>>");
		}
		beforeApiRet = ret;
		return ret;
	}


	public JSONObject callApi() {
		return null;
	}


	public void commonCheck() {
	}


	public void userDefinedCheck() {
	}




}
