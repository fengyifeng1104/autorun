package com.ymatou.autorun.datadriver.execute.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.data.AssertData;
import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.data.impl.AssertDataImpl;
import com.ymatou.autorun.datadriver.execute.APICall;
import com.ymatou.autorun.datadriver.execute.CaseExecuteFlow;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecuteService;
import com.ymatou.autorun.datadriver.face.SqlSearch;


public class CaseExecuteFlowImpl implements CaseExecuteFlow{
	
	
	private SqlSearch sqlSearch;
	private ImportData importData;
	private GlobalData globalData;
	private AssertData assertData;
	
	private Map<Integer,JSONObject> beforeApiRet = new HashMap<Integer,JSONObject>();
	private JSONObject allRet = new JSONObject();
	private APICall apiCall;
	
	
	public CaseExecuteFlowImpl(SqlSearch sqlSearch,ImportData importData,GlobalData globalData){
		this.sqlSearch = sqlSearch;
		this.importData = importData;
		this.globalData = globalData;
	
	}


	public void beforeCall(String resultFolderName) {
		String subfolderName = importData.getApi().toLowerCase().replace("/", "");
		Logger.createResultFile(resultFolderName,importData.getHost(),subfolderName,importData.getCaseSummary());
		Logger.comment("=================Start Case id:["+ importData.getCaseId() +"], case summary:"+importData.getCaseSummary());

	}

	public void afterCall() {
		Logger.comment("=================End Test Case=================");
		Logger.generateResult(importData.getApi());
		
	}
	
	
	public Map<Integer, JSONObject> callbeforeApis() {
		Map<Integer,JSONObject> ret = new HashMap<Integer,JSONObject>();
		Map<Integer,ImportData> beforeCasesMap = importData.getDependCaseIds();
		if(beforeCasesMap.size()>0){
			Logger.comment("<<<<<<<<<<call before Apis Start, ParentId:"+ importData.getCaseId() +">>>>>>>>>>");
			//ret = new HashMap<Integer,JSONObject>();
			for(Integer id: beforeCasesMap.keySet()){
				ImportData beforeData = beforeCasesMap.get(id);
				Logger.comment("before case Info - case id:["+ id +"], scenario:"+beforeData.getScenario()+", summary:"+beforeData.getScenarioSummary());
				JSONObject beforeRet = CaseExecute.execute(sqlSearch,beforeData,globalData);
				ret.put(id, beforeRet);
			}
			Logger.comment("<<<<<<<<<<call before Apis End, ParentId:"+ importData.getCaseId() +">>>>>>>>>>");
		}
		beforeApiRet = ret;
		return ret;
	}


	public JSONObject callApi() {
		Logger.start(true,importData.getCaseId(), importData.getCaseSummary());
		try {
			//get api class 
			apiCall = CaseExecuteService.generateApiCallInstance(importData.getHost(),importData.getApi(),importData.getReqType());
			
			
			//1 replace 全局数据
			JSONObject jsonModel = CaseExecuteService.updateModelWithGlobalData(importData.getScenarioModel(), globalData.getKeyVal());
			
			
			//2 replace with 输入数据 和依赖数据
			jsonModel = CaseExecuteService.updateModelWithData(sqlSearch,jsonModel,  importData.getModelUpdateMap(), beforeApiRet);
			
			
			
			
			//4 call API and return data
			JSONObject retJson = apiCall.callAndGetReturnData(jsonModel);
			
			//5 add before result
			for(Integer id:beforeApiRet.keySet()){retJson.put(id.toString(),beforeApiRet.get(id));}
			allRet = retJson;  
			return retJson;
			
			
			/*//1 replace 全局数据
			JSONObject jsonModel = CaseExecuteService.updateModelWithGlobalData(importData.getScenarioModel(), globalData.getKeyVal());

			
			//2 replace with 输入数据 和依赖数据
			jsonModel = CaseExecuteService.updateModelWithData(jsonModel,  importData.getModelUpdateMap(), beforeApiRet);
			
			//3 set cookie if has
			if (jsonModel.containsKey("cookies")){
				apiCall.setCookies(jsonModel.getString("cookies").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", ""));
				jsonModel.remove("cookies");
			}
			
			//4 call API and return data
			JSONObject retJson = apiCall.callAndGetReturnData(jsonModel);
			
			//5 add before result
			for(Integer id:beforeApiRet.keySet()){retJson.put(id.toString(),beforeApiRet.get(id));}
			allRet = retJson;
			return retJson;*/
			

		} catch (Exception e) {
			Logger.fail(e);;
		}
		return new JSONObject();
	}


	public void commonCheck() {
	}


	public void userDefinedCheck() {
		assertData = new AssertDataImpl(importData.getCaseAssert());
		try {
			CaseExecuteService.checkSqlDB(sqlSearch,beforeApiRet,assertData.getSqlCheckList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.fail(e);
		}
		
		
		//todo mongo and return data check 
		//CaseExecuteService.checkMongoDB(allRet,assertData.getMongoCheckList());
		//CaseExecuteService.checkReturnData(allRet,assertData.getReturnDataCheckMap());
		
	}




}
