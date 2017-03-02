package com.ymatou.autorun.datadriver.execute.impl;


import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.YmatouAutoTestCall;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.execute.APICall;



public class APICallImpl implements APICall{
	private YmatouAutoTestCall ymatouBaseCall ;

	private String host;
	private String api;
	private String reqType;
	private String beanFormat;
	
	public APICallImpl(String host, String api, String reqType){
		this.host = "http://"+host;
		this.api = api;
		this.reqType = reqType;
		
		 this.beanFormat = "JSON";
		if (reqType.equals("GET")){
			this.beanFormat = "PARAM";
		}
		
		ymatouBaseCall = new YmatouAutoTestCall(this.host, this.api, this.reqType, beanFormat);
		
		
	}
	
	
	
	
	
	@Override
	public String getReqType() {
		return  reqType;
	}
	@Override
	public String getHost() {
		return host;
	}
	@Override
	public String getApi() {
		return api;
	}

	
	/***
	 * call api and get return data
	 * add request message and cookies if has
	 */
	@Override
	public JSONObject callAndGetReturnData(JSONObject params){
		try{
	
			if (params!=null){
				if(getReqType().equals("GET")){
					ymatouBaseCall.setData(params.toString());
					
				}else{
					
					//String paramStr = params.toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(":", "=").replaceAll(",", "&").replaceAll("\"", "");
					
					ymatouBaseCall.setData(params.toString());
				}
			}
			
			ymatouBaseCall.callService();	
			JSONObject retJson ;
			try{
				JSONObject retData = JSONObject.parseObject(ymatouBaseCall.getReturnData()).getJSONObject("Data");
				if (retData ==null){
					Logger.comment("Data is null");
					retJson =  new JSONObject();
				}else{
					retJson = retData ;
				}
			
			}catch (JSONException e) {
				Logger.comment("result is not json format");
				retJson = new JSONObject();
			}
			
			retJson.put(JsonBeanUtil.JsonBeanRequest,params);
			retJson.put("cookies", ymatouBaseCall.getCookies());
			return  retJson;
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public List<String> getCookies() {
		return ymatouBaseCall.getCookies();
	}

	@Override
	public void setCookies( String cookies) {
		ymatouBaseCall.setCookie(cookies);
	}



	
}
