package com.ymatou.autorun.datadriver.base;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymt.base.YmatouBaseCall;
import com.ymt.core.tool.Logger;

public class YmatouAutoTestCall extends YmatouBaseCall{
	
	
	public YmatouAutoTestCall(String systemUrl, String opurl, String Mode,
			String beanFormat) {
		super(systemUrl, opurl, Mode, beanFormat);
	}
	
	@Override
	public void addDefHeader() {
		client.addHeader("Accept","text/xml,text/javascript,text/html,application/json");
		client.addHeader("Content-Type", "application/json; charset=UTF-8");
	}
	
	
	
	// for post and get 
	public JSONObject callAndGetReturnData(){
		return mode.toLowerCase().equals("get")?callAndGetReturnData(new String[]{}):callAndGetReturnData(null,null);
	}
	
	
	
	
	// for post and get 
	public  JSONObject callAndGetReturnData(Map<String, Object> updateMap){
		//for get type
		if (mode.toLowerCase().equals("get")){
			String[] val = new String[updateMap.size()];
			int index = 0;
			for (String key: updateMap.keySet()){
				val[index] = key+"="+updateMap.get(key).toString();
				index++;
			}
			return callAndGetReturnData(val);
		}else
			//for post type
		{
			return callAndGetReturnData(null,updateMap);
		}
	}
	

	// for post
		public JSONObject callAndGetReturnData(String fileName){
			return this.callAndGetReturnData(fileName,null);
		}
		
		
	/***
	 * for post
	 * 调用接口，并获取返回值
	 * 如发生错误，返回null
	 * @return
	 */
	public  JSONObject callAndGetReturnData(String fileName, Map<String, Object> updateMap){

		//获取json bean
		JSONObject jsonObject = this.getJsonBean(fileName);
		
		//修改bean
		if (updateMap!=null){JsonBeanUtil.updateJsonBean(jsonObject, updateMap);}
		
		//获取返回值
		return  callAndGetReturnData(jsonObject);

	}
	
	//for post type
	public  JSONObject callAndGetReturnData(JSONObject jsonBean){
		try{
			//设置bean
			this.setData(jsonBean.toString());
			
			//发送请求
			this.callService();
			
			//获取返回值
			JSONObject ret = JSON.parseObject(this.getReturnData());
			ret.put(JsonBeanUtil.JsonBeanRequest,jsonBean);
			return ret;
			
		}catch(Exception e){
			Logger.fail(e);
		}
		return null;
	}
	
	//for get type
	
	public JSONObject callAndGetReturnData(String[] args){
		
		try{
			//设置bean
			setData(StringUtils.join(args,"&"));
			
			//发送请求
			this.callService();
			
			//获取返回值
			return JSON.parseObject(this.getReturnData());
			
		}catch(Exception e){
			Logger.fail(e);
		}
		return null;
		
	}
	
	
	/***
	 * 根据call类的路径 获取json file
	 * 如指定fileName 就用指定的，如没有指定，就用默认模板
	 * @return
	 */
	public final JSONObject getJsonBean(String fileName){
		String clzName = this.getClass().getName();
		
		//remove class name
		String rootPath = clzName.substring(0,clzName.lastIndexOf("."));
		
		//remove service level name
		rootPath = rootPath.substring(0,rootPath.lastIndexOf("."));
		
		String parentPath = this.getClass().getSimpleName();
		fileName = (fileName==null || fileName.equals(""))?parentPath.toLowerCase() + "tpl.json":fileName;
		
		String fullPath = rootPath + File.separator + parentPath + File.separator + fileName;
		return JsonBeanUtil.getJsonBean(fullPath);	
		
	}
	
	
	
	public JSONObject callAndGetReturnData(List<Map<String, Object>> updateMapList){
		try{
			JSONArray jsonArray = new JSONArray();
			
			for(int i=0;i<updateMapList.size();i++){
				JSONObject jsonObject = new JSONObject();
				for(String key:updateMapList.get(i).keySet()){
					jsonObject.put(key, updateMapList.get(i).get(key));
				}
				
				jsonArray.add(i, jsonObject);
			}
			
			//设置bean
			this.setData(jsonArray.toString());
			
			//发送请求
			this.callService();
			
			//获取返回值
			JSONObject ret = JSON.parseObject(this.getReturnData());
			ret.put(JsonBeanUtil.JsonBeanRequest,jsonArray);
			return ret;
			
		}catch(Exception e){
			Logger.fail(e);
		}
		return null;
		
		
	}


}
