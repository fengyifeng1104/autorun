package com.ymatou.autorun.datadriver.execute.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.AssertUtil;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.base.utils.MapUtil;
import com.ymatou.autorun.datadriver.base.utils.YMTDateUtil;
import com.ymatou.autorun.datadriver.data.AssertData;
import com.ymatou.autorun.datadriver.data.domain.DBCheckDataBean;
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
	public static Object getPreviousVal(Map<Integer, JSONObject> beforeApiRet, Integer beforeId,String pathKey) throws Exception{
		return null;
		
	}
	
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
	
	public static JSONObject updateModelWithData(JSONObject jsonModel,Map<String, String> updateDataMap,Map<Integer,JSONObject> beforeApiRet) throws Exception{
		if (updateDataMap!=null){
			for(String K:updateDataMap.keySet()){
			    String V = updateDataMap.get(K);
				if (V.contains(".")){
					String[] args1 = V.toString().split("\\.");
					Integer beforeId = Integer.parseInt(args1[0]);
					String pathKey = args1[1];
					
					Object previousVal = getPreviousVal(beforeApiRet,beforeId,pathKey);
					/*if (K.equals("cookies")){
						//cookies:10007.cookies
						Logger.debug("Cookies:" + previousVal.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", ""));
						apiCall.setCookies(previousVal.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", ""));
					}else{
						
					}*/
					
					JsonBeanUtil.updateJsonBean(jsonModel, K,previousVal);
				}else{
					//for date  -  productEndTime:#(d,10);
					if (V.contains("#")){
						String[] vString = V.replaceAll("\\#", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",");
						JsonBeanUtil.updateJsonBean(jsonModel,K,YMTDateUtil.getBeforeOrNextDay(Integer.parseInt(vString[1])));
						
					}else{
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

	public static void checkUserDefined(AssertData assertData){
		Map<String, Object> paramMap = assertData.getParamMap();
		List<DBCheckDataBean> sqlList = assertData.getSqlCheckList();
		List<DBCheckDataBean> mongoList = assertData.getMongoCheckList();
		
		for(DBCheckDataBean sqlCheckBean:sqlList){
			 String tableName = sqlCheckBean.getTableName();
			 Map<String, Object> searchMap = sqlCheckBean.getSearchMap();
			 
			 //param 替换 搜索map中的值
			 for(String searchKey:searchMap.keySet()){
				 if (paramMap.containsKey(searchMap.get(searchKey).toString())){
					 searchMap.put(searchKey, paramMap.get(searchMap.get(searchKey).toString()));
				 }
			 }
			 
			 Map<String, Object> assertMap = sqlCheckBean.getAssertMap();
			 String dbName = AssertData.sqlClassPath +"."+tableName+"Wapper";
			 try {
				SqlSearch sqlsearch = (SqlSearch)Class.forName(dbName).newInstance();
				Map<String, Object> tgtMap =new HashMap<String, Object>();;
				for(String key:assertMap.keySet()){
					if (tgtMap.containsKey(key)){
					AssertUtil.assertResultEqual(MapUtil.hashMap(key, assertMap.get(key)), MapUtil.hashMap(key, tgtMap.get(key)));
					}else{
						System.out.println("error");
						//Logger.verifyEquals(false, true, "key:["+key+"] is not in sql result, table is "+ tableName);
					}
				}

			
			 } catch (InstantiationException e) {
				e.printStackTrace();
			 } catch (IllegalAccessException e) {
				e.printStackTrace();
			 } catch (ClassNotFoundException e) {
				e.printStackTrace();
			 }
			
		}
		
	
		
	}

	

	

}
