package com.ymatou.autorun.datadriver.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.dataservice.model.RunningDataModel;


public class ImportDataFromMySQLImpl  implements ImportData{
	
	
	private int caseId;
	private String caseSummary;
	private String scenario;
	private String scenarioSummary;
	private JSONObject scenarioModel;
	private Map<String, String> modelUpdateMap = new HashMap<String, String>();
	private Map<String, String> caseAssert = new HashMap<String, String>();
	
	private Map<Integer,ImportData> dependCaseIds = new HashMap<Integer,ImportData>();
	private Map<Integer, List<String>> dependCaseIdsVal =new HashMap<Integer, List<String>>();
	private String api;
	private String host;
	private String reqType;
	
	
	public ImportDataFromMySQLImpl(){
		
	}
	
	public ImportDataFromMySQLImpl(RunningDataModel runningDataModel){
		initData(runningDataModel);
	}
	
	
	private void initData(RunningDataModel runningDataModel){
		//search for mysql
		try {
			
			RunningDataModel ret = runningDataModel;
			
			caseId = ret.getCaseId();
			
			caseSummary = ret.getCaseDescription();
			
			scenarioModel = JSON.parseObject(ret.getTemplateDetail());
		

			
			
			api = ret.getSceneApi();
			host = ret.getSceneHost();
			reqType = ret.getReqMethod();
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getCaseId() {
		return this.caseId;
	}

	@Override
	public String getCaseSummary() {
		return this.caseSummary;
	}

	@Override
	public String getScenario() {
		return this.scenario;
	}

	@Override
	public String getScenarioSummary() {
		return this.scenarioSummary;
	}

	@Override
	public JSONObject getScenarioModel() {
		return this.scenarioModel;
	}

	@Override
	public Map<String, String> getModelUpdateMap() {
		return this.modelUpdateMap;
	}

	@Override
	public Map<String, String> getCaseAssert() {
		return this.caseAssert;
	}

	@Override
	public Map<Integer, ImportData> getDependCaseIds() {
		return this.dependCaseIds;
	}

	@Override
	public Map<Integer, List<String>> getDependCaseIdsVal() {
		return this.dependCaseIdsVal;
	}

	@Override
	public String getApi() {
		return this.api;
	}

	@Override
	public String getHost() {
		return this.host;
	}
	
	@Override
	public String getReqType() {
		return this.reqType;
	}
	
	

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public void setCaseSummary(String caseSummary) {
		this.caseSummary = caseSummary;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public void setScenarioSummary(String scenarioSummary) {
		this.scenarioSummary = scenarioSummary;
	}

	public void setScenarioModel(JSONObject scenarioModel) {
		this.scenarioModel = scenarioModel;
	}

	public void setModelUpdateMap(Map<String, String> modelUpdateMap) {
		this.modelUpdateMap = modelUpdateMap;
	}

	public void setCaseAssert(Map<String, String> caseAssert) {
		this.caseAssert = caseAssert;
	}

	public void setDependCaseIds(Map<Integer, ImportData> dependCaseIds) {
		this.dependCaseIds = dependCaseIds;
	}

	public void setDependCaseIdsVal(Map<Integer, List<String>> dependCaseIdsVal) {
		this.dependCaseIdsVal = dependCaseIdsVal;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	/***
	 * a=b;c=d;T=1
	 * @param s
	 * @return
	 */
	private Map<String, String> StringToMap(String s){
		Map<String, String> map  =new HashMap<String,String>();
		if (s!=null){
			String[] pair  = s.split(";");
			if (pair.length>0 && pair[0].length()>0){
				for (int i=0;i<pair.length;i++){
					String[] keyval = pair[i].split("=");
					map.put(keyval[0].trim(), keyval[1].trim());
				}
			}
		}
		return map;
	}



	
	
	
	
	

}
