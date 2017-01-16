package com.ymatou.autorun.datadriver.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.database.model.Stc_CaseData;
import com.ymatou.autorun.datadriver.base.database.sqlwapper.StcCaseQueryWapper;
import com.ymatou.autorun.datadriver.data.ImportData;


public class ImportDataFromDBImpl  implements ImportData{
	
	private StcCaseQueryWapper stcCaseQueryWapper = new StcCaseQueryWapper();
	
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
	
	
	public ImportDataFromDBImpl(){
		
	}
	
	public ImportDataFromDBImpl(int caseId){
		initData(caseId);
	}
	
	
	private void initData(int caseId){
		//search for mysql
		try {
			Stc_CaseData db_CaseData = stcCaseQueryWapper.getCaseDataById(caseId);
			
			caseId = db_CaseData.getCaseId();
			
			caseSummary = db_CaseData.getCaseDescription();
			
			scenario = db_CaseData.getModel();
			
			scenarioSummary = db_CaseData.getScene();
			
			scenarioModel = JSON.parseObject(db_CaseData.getTemplateDetail());
		
			modelUpdateMap = StringToMap(db_CaseData.getExtraInputList());
			
			caseAssert = StringToMap(db_CaseData.getExtraCheckList());
			
			api = db_CaseData.getScene_api();
			
			host = db_CaseData.getScene_host();
			
			for(String key : modelUpdateMap.keySet()){
				Object V = modelUpdateMap.get(key);
				if (V.toString().contains(".")){
					String[] args = V.toString().split("\\.");
					Integer beforeId = Integer.parseInt(args[0]);
					String pathKey = args[1];
					
					if (dependCaseIdsVal.keySet().contains(beforeId)){
						dependCaseIdsVal.get(beforeId).add(pathKey);
					}else{
						List<String> keysList = new ArrayList<String>();
						keysList.add(pathKey);
						dependCaseIdsVal.put(beforeId, keysList);
					}
					dependCaseIds.put(beforeId, new ImportDataFromDBImpl(beforeId));
				}	
			}
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
