package com.ymatou.autorun.datadriver.base.utils;


import java.io.File;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
public class JsonBeanUtil {
	
	public static final String JsonBeanRequest = "JsonBeanRequest";
	public static final String JsonBeanFileFolder = System.getProperty("user.dir") + File.separator+"resource"+File.separator+"RequestBeanTpl"+File.separator;
	
	/**
	 * 初始化ConfigUtil 
	 * @param fileName 设置JsonBean文件名
	 */
	
	public static JSONObject getJsonBean(String fileName){
		try {
			File file = new File(JsonBeanFileFolder + fileName);
			if(!file.exists()){
				if (!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
				System.out.println("该文件新创建 ["+file.getAbsolutePath()+"], 请填写Json内容。");
			}
		
			String contentString = FileUtil.readFile(JsonBeanFileFolder + fileName);
			return JSONObject.parseObject(contentString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * Example:
	 * 
	 * requestBean = JsonBeanHelper.updateJsonBean(requestBean, "UserId",11);
	 * @param jsonBean
	 * @param key
	 * @param updateValue
	 * @return
	 */
	public static JSONObject updateJsonBean(JSONObject jsonBean, String key, Object updateValue){
		return updateJsonBean(jsonBean,MapUtil.hashMap(key, updateValue));
	}
	
	
	
	
	/***
	 * 	Example:
	 * 
	 * 	Map<String,Object> aa = new HashMap<String, Object>();
		aa.put("UserId", 11);
		aa.put("Product->SaleNum", 22);
		aa.put("Product->Catalogs[0]->Action", 33);
		aa.put("Product->MobileDescription", new JSONObject("{\"a\":1,\"b\":2}"));
			
		JSONObject requestBean = JsonBeanHelper.getJsonBean(TestData.JsonBeanPointName + "/LiveProductAdd.json");
			
		requestBean = JsonBeanHelper.updateJsonBean(requestBean, aa);
			
	 * @param jsonBean
	 * @param updateMap
	 * @return
	 */
	public static JSONObject updateJsonBean(JSONObject jsonBean,Map<String,Object> updateMap){
		
			for (String nodePath: updateMap.keySet()){
				try{
					JSONObject parenetObject = jsonBean;
					String[] nodesAry = nodePath.split("->");
					int len = nodesAry.length;
					
					//解析节点
					for(int i=0;i<len-1;i++){
						if (nodesAry[i].contains("[") && nodesAry[i].contains("]")){
						
							int StartPos = nodesAry[i].indexOf("[");
							int EndPos = nodesAry[i].indexOf("]");
							
							int childIndex = Integer.parseInt(nodesAry[i].substring(StartPos+1, EndPos));
							String nodeKey = nodesAry[i].substring(0, StartPos);
							
							//array节点
							parenetObject = parenetObject.getJSONArray(nodeKey).getJSONObject(childIndex);
						}else{
							//object节点
							parenetObject = parenetObject.getJSONObject(nodesAry[i]);
						}	
					}
					parenetObject.put(nodesAry[len-1], updateMap.get(nodePath));	
				}catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			
		return jsonBean;
	}
	
	
	
	/***
	 * 获取node值
	 * @param jsonBean
	 * @param nodePath
	 * @return
	 */
	public static Object getJsonBeanNodeObj(JSONObject jsonBean, String nodePath){
		try{
			JSONObject parenetObject = jsonBean;
			String[] nodesAry = nodePath.split("->");
			int len = nodesAry.length;
			
			//解析节点
			for(int i=0;i<len-1;i++){
				if (nodesAry[i].contains("[") && nodesAry[i].contains("]")){
				
					int StartPos = nodesAry[i].indexOf("[");
					int EndPos = nodesAry[i].indexOf("]");
					
					int childIndex = Integer.parseInt(nodesAry[i].substring(StartPos+1, EndPos));
					String nodeKey = nodesAry[i].substring(0, StartPos);
					
					//array节点
					parenetObject = parenetObject.getJSONArray(nodeKey).getJSONObject(childIndex);
				}else{
					//object节点
					parenetObject = parenetObject.getJSONObject(nodesAry[i]);
				}	
			}
			
			
			
			//获取节点
			if (nodesAry[len-1].contains("[") && nodesAry[len-1].contains("]")){
				
				int StartPos = nodesAry[len-1].indexOf("[");
				int EndPos = nodesAry[len-1].indexOf("]");
				
				int childIndex = Integer.parseInt(nodesAry[len-1].substring(StartPos+1, EndPos));
				String nodeKey = nodesAry[len-1].substring(0, StartPos);
				
				//workaround
				return JSON.parseArray(parenetObject.getString(nodeKey)).get(childIndex);
			}else{
				return parenetObject.get(nodesAry[len-1])==null? null:parenetObject.get(nodesAry[len-1]);
			}
			
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
		
		
		
		
	}
	
	/***
	 * node 是否存在
	 * @param jsonBean
	 * @param nodePath
	 * @return
	 */
	public static boolean isJsonBeanNodeExist(JSONObject jsonBean,String nodePath){
		
		JSONObject parenetObject = jsonBean;
		String[] nodesAry = nodePath.split("->");
		int len = nodesAry.length;
		
		//解析节点
		try{
			for(int i=0;i<len-1;i++){
				if (nodesAry[i].contains("[") && nodesAry[i].contains("]")){
				
					int StartPos = nodesAry[i].indexOf("[");
					int EndPos = nodesAry[i].indexOf("]");
					
					int childIndex = Integer.parseInt(nodesAry[i].substring(StartPos+1, EndPos));
					String nodeKey = nodesAry[i].substring(0, StartPos);
					
					//array节点
					if (parenetObject.containsKey(nodeKey)&&parenetObject.getJSONArray(nodeKey).size()>(childIndex)){
						parenetObject = parenetObject.getJSONArray(nodeKey).getJSONObject(childIndex);
					}else{
						return false;
					}
				}else{
					//object节点
					
					if (parenetObject.containsKey(nodesAry[i])){
						parenetObject = parenetObject.getJSONObject(nodesAry[i]);
					}else{
						return false;
						
					}
					
				}	
			}
			
	
			return parenetObject.containsKey(nodesAry[len-1].replaceAll("\\[.*\\]", ""));
			
			
			//return parenetObject.has(nodesAry[len-1]);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;

	}


}
