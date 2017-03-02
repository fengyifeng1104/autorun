package com.ymatou.autorun.datadriver.execute;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface CaseExecuteFlow     {
	
	public void beforeCall(String resultfolerName);
	
	public Map<Integer,JSONObject> callbeforeApis(); 
	
	public JSONObject  callApi();
	
	public void commonCheck();
	
	public void userDefinedCheck();
	
	public void afterCall();
	
	
}
