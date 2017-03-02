package com.ymatou.autorun.datadriver.execute.helper;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.AssertUtil;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.base.utils.MapUtil;
import com.ymatou.autorun.datadriver.base.utils.YMTDateUtil;
import com.ymatou.autorun.datadriver.data.AssertData;
import com.ymatou.autorun.datadriver.data.domain.CheckDBDataBean;
import com.ymatou.autorun.datadriver.execute.APICall;
import com.ymatou.autorun.datadriver.execute.impl.APICallImpl;
import com.ymatou.autorun.datadriver.face.SqlSearch;

public class CaseExecuteService    {
	
	
	/***
	 * 获取前项依赖case的输出值
	 * 	某些不在前项输出值中的，可workaround 
	 * @param beforeApiRet
	 * @param beforeId
	 * @param pathKey
	 * @return
	 * @throws Exception
	 */
/*	public static Object getPreviousVal(SqlSearch sqlSearch,Map<Integer, JSONObject> beforeApiRet, Integer beforeId,String pathKey) throws Exception{
		if (beforeApiRet.get(beforeId)==null){
			throw new NullPointerException("before id ["+beforeId+"] is not exist.");
		}
		
		Object ret = null;
		JSONObject beforeRet = beforeApiRet.get(beforeId);
		
		
		//set value which is not in return message
		if (pathKey.contains("CatalogIds") && !beforeRet.containsKey("CatalogIds")){
			List<Map<String, Object>> retMaps = sqlSearch.selectBy(SqlDSconf.IntegratedProductStr, "select * from Ymt_Catalogs where SProductId='"+beforeRet.getString("ProductID")+"'");
			
			JSONArray CatalogAry = new JSONArray();
			for(Map<String, Object> retMap: retMaps){
				CatalogAry.add(retMap.get("sCatalogId").toString());
			}
			beforeRet.put("CatalogIds",CatalogAry);
		}
		
		//get value
		if (JsonBeanUtil.isJsonBeanNodeExist(beforeRet, pathKey)){
			ret = JsonBeanUtil.getJsonBeanNodeObj(beforeRet, pathKey);
		}else{
			Logger.comment("Before api id:"+ beforeId +" and return value:"+ beforeApiRet.get(beforeId).toString());
			throw new NullPointerException("before id ["+beforeId+"] path of key ["+pathKey+"]is not exist.");
		}
		
		
		
		return ret;
		
	}*/
	
	public static JSONObject updateModelWithGlobalData(JSONObject jsonModel,Map<String, String> globalDataMap) throws JSONException{
		String strModel = jsonModel.toString();

		if(globalDataMap!=null){
			for(String k:globalDataMap.keySet()){
				//模板修改全局变量
				strModel=strModel.replaceAll("\\$\\{"+k+"\\}", globalDataMap.get(k));
			}
		}
		return JSON.parseObject(strModel);
	}
	
	public static String hostNameReverasl(String hostName){
		String[] vals = hostName.replace("\\", "").split("\\.");
		StringBuffer ret = new StringBuffer();
		for(int i=vals.length;i>0;i--){
			ret.append(vals[i-1]).append(".");
		}
		return ret.deleteCharAt(ret.length()-1).toString();
	}
	
	public static JSONObject updateModelWithData(SqlSearch sqlSearch,JSONObject jsonModel,Map<String, String> updateDataMap,Map<Integer,JSONObject> beforeApiRet) throws Exception{
		if (updateDataMap!=null){
			//逐个处理map里面的值
			for(String K:updateDataMap.keySet()){
			    String V = updateDataMap.get(K);
			    
			    //1 如果包含. 就说明是依赖前项的结果，然后去beforeResult中去捞
				if (V.contains(".")){
					String[] args1 = V.toString().split("\\.");
					Integer beforeId = Integer.parseInt(args1[0]);
					String pathKey = args1[1];
					
					Object previousVal = DataValHandle.processPreviousVal(sqlSearch,beforeApiRet,beforeId,pathKey);
					JsonBeanUtil.updateJsonBean(jsonModel, K,previousVal);
				}else{
					//for date  -  productEndTime:#(d,10);
					//如果包含# 就是说说明依赖时间
					if (V.contains("#")){
						String[] vString = V.replaceAll("\\#", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",");
						JsonBeanUtil.updateJsonBean(jsonModel,K,YMTDateUtil.getBeforeOrNextDay(Integer.parseInt(vString[1])));
						
					}else{
						//默认updateMap
						JsonBeanUtil.updateJsonBean(jsonModel, K, V);
					}
				}
			}
		}
		return jsonModel;
		
		
	}

	public static APICall generateApiCallInstance(String host,String api,String reqType){
		return new APICallImpl(host, api, reqType);

	}

	
	
	
	public static void checkSqlDB(SqlSearch sqlSearch,Map<Integer, JSONObject> beforeApiRet,List<CheckDBDataBean> sqlList) throws Exception{
		for(CheckDBDataBean sqlCheckBean: sqlList){
			String dbName = sqlCheckBean.getDBName();
			String sqlStr = sqlCheckBean.getSqlStr();
			Integer orderNum = sqlCheckBean.getOrderNum();
			JSONObject assertMapJson = sqlCheckBean.getAssertMap();
			
			//handle sql string 
			String regex = AssertData.SqlStr_Param_Regex;
			
			Matcher matcher = Pattern.compile(regex).matcher(sqlStr);
			
			while (matcher.find()){
				Logger.debug("Sql 参数："+ matcher.group(0));
				String replaceKey = matcher.group(0).replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "");
				
				String[] args1 = replaceKey.toString().split("\\.");
				Integer beforeId = Integer.parseInt(args1[0]);
				String pathKey = args1[1];
				
				Object previousVal = DataValHandle.processPreviousVal(sqlSearch,beforeApiRet,beforeId,pathKey);
				
				
				sqlStr = sqlStr.replace(matcher.group(0), previousVal.toString());
			}
			
			
			
			List<Map<String,Object>> sqlRetList = sqlSearch.selectBy(dbName,sqlStr);
			
			if (sqlRetList.size()<=orderNum){
				throw new ArrayIndexOutOfBoundsException("");
			}
			
			
			
			Map<String,Object> sqlRet = new CaseInsensitiveMap(sqlRetList.get(orderNum));
			
			for(String key:assertMapJson.keySet()){
				if (sqlRet.containsKey(key)){
					AssertUtil.assertResultEqual(MapUtil.hashMap(key, assertMapJson.get(key)), MapUtil.hashMap(key, sqlRet.get(key)));
				}else{
					Logger.verifyEquals(false, true, "key:["+key+"] is not in sql result, Sql:" + sqlStr);
				}
			}
			
			
			
		}
		
	}

	

	

}
