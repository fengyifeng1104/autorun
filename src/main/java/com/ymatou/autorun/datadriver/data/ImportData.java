package com.ymatou.autorun.datadriver.data;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface ImportData {
	
	/***
	 * case id
	 * @return
	 */
	public int getCaseId();
	
	/***
	 * case概述
	 * @return
	 */
	public String getCaseSummary();
	
	
	/***
	 * 场景
	 * @return
	 */
	public String getScenario();
	
	/***
	 * 场景概述
	 * @return
	 */
	public String getScenarioSummary();
	
	
	/***
	 * 场景模板
	 * @return
	 */
	public JSONObject getScenarioModel();
	
	
	/***
	 * 输入数据
	 * @return
	 */
	public Map<String, String> getModelUpdateMap();
	
	/***
	 * 断言
	 * @return
	 */
	public Map<String, String> getCaseAssert();

	
	
	
	
	//public List<String> outValues();
	
	
	/***
	 * 依赖ids
	 * @return
	 */
	public Map<Integer,ImportData> getDependCaseIds();
	
	public Map<Integer, List<String>> getDependCaseIdsVal();
	
	/***
	 * api
	 */
	
	public String getApi();
	
	public String getHost();
	
	public String getReqType();
	

}
