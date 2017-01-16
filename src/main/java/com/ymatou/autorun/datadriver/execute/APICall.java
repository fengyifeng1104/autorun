package com.ymatou.autorun.datadriver.execute;

import java.util.List;

import org.json.JSONObject;

public interface APICall {
	
	public String getMode();
	
	public String getSysUrl();
	
	public String getApi();
	
	public JSONObject callAndGetReturnData(JSONObject params) ;
	
	public List<String> getCookies();
	
	public void setCookies(String cookies);
	
}
