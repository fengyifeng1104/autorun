package com.ymatou.autorun.datadriver.execute;

import java.util.List;
import com.alibaba.fastjson.JSONObject;

public interface APICall {
	
	public String getReqType();
	
	public String getHost();
	
	public String getApi();
	
	public JSONObject callAndGetReturnData(JSONObject params) ;
	
	public List<String> getCookies();
	
	public void setCookies(String cookies);
	
}
