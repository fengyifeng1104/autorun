package com.ymatou.autorun.datadriver.execute.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.base.utils.YMTDateUtil;
import com.ymatou.autorun.datadriver.data.conf.SqlDSconf;
import com.ymatou.autorun.datadriver.face.SqlSearch;

public class DataValHandle {
	
	
	/***
	 * 获取前项依赖case的输出值
	 * 	某些不在前项输出值中的，可workaround 
	 * @param beforeApiRet
	 * @param beforeId
	 * @param pathKey
	 * @return
	 * @throws Exception
	 */
	public static Object processPreviousVal(SqlSearch sqlSearch,Map<Integer, JSONObject> beforeApiRet, Integer beforeId,String pathKey) throws Exception{
		if (beforeApiRet.get(beforeId)==null){
			throw new NullPointerException("before id ["+beforeId+"] is not exist.");
		}
		
		Object ret = null;
		JSONObject beforeRet = beforeApiRet.get(beforeId);
		
		
		//set value which is not in return message
		
		// 商品规格
		//if (pathKey.contains("CatalogIds") && !beforeRet.containsKey("CatalogIds")){
		
		if (isNeedAddKey(beforeRet, pathKey, "CatalogIds")){
			List<Map<String, Object>> retMaps = sqlSearch.selectBy(SqlDSconf.IntegratedProductStr, "select * from Ymt_Catalogs where SProductId='"+beforeRet.getString("ProductID")+"'");
			
			JSONArray CatalogAry = new JSONArray();
			for(Map<String, Object> retMap: retMaps){
				CatalogAry.add(retMap.get("sCatalogId").toString());
			}
			beforeRet.put("CatalogIds",CatalogAry);
		}
		
		
		//商品活动id
		if (isNeedAddKey(beforeRet, pathKey, "ProductInActivityId")){
			String productId = beforeRet.getJSONObject("JsonBeanRequest").getString("ProductId");
			List<Map<String, Object>> retMaps = sqlSearch.selectBy(SqlDSconf.IntegratedProductStr, "select * from Ymt_ProductsInActivity where sProductId='"+productId+"' order by dAddTime desc");
			
			JSONArray productInActivityIds = new JSONArray();
			for(Map<String,Object> retMap:retMaps){
				productInActivityIds.add(Integer.parseInt(retMap.get("iProductInActivityId").toString()));
			}
			beforeRet.put("ProductInActivityId",productInActivityIds);
		}
		
		
		
		//get value
		if (JsonBeanUtil.isJsonBeanNodeExist(beforeRet, pathKey)){
			ret = JsonBeanUtil.getJsonBeanNodeObj(beforeRet, pathKey);
		}else{
			Logger.comment("Before api id:"+ beforeId +" and return value:"+ beforeApiRet.get(beforeId).toString());
			throw new NullPointerException("before id ["+beforeId+"] path of key ["+pathKey+"]is not exist.");
		}
		
		
		
		return ret;
		
	}
	
	
	
	
	public static JSONObject processInputVal(SqlSearch sqlSearch,JSONObject jsonModel,Map<String, String> updateDataMap,Map<Integer,JSONObject> beforeApiRet) throws Exception{
		if (updateDataMap!=null){
			//逐个处理map里面的值
			for(String K:updateDataMap.keySet()){
			    String V = updateDataMap.get(K);
			    
			    //1 如果包含. 就说明是依赖前项的结果，然后去beforeResult中去捞
				if (V.contains(".")){
					String[] args1 = V.toString().split("\\.");
					
					//handle date
					if (args1[0].equals("date")){
						JsonBeanUtil.updateJsonBean(jsonModel,K,processDate(args1));
					
					}else{
						
						//需要从前项依赖的数据中取数据 来修改
						Integer beforeId = Integer.parseInt(args1[0]);
						String pathKey = args1[1];
						Object previousVal = DataValHandle.processPreviousVal(sqlSearch,beforeApiRet,beforeId,pathKey);
						JsonBeanUtil.updateJsonBean(jsonModel, K,previousVal);
					}
					
				}else{
					//默认updateMap 直接修改model
					JsonBeanUtil.updateJsonBean(jsonModel, K, V);
					
				}
			}
		}
		return jsonModel;
		
	}
	
	
	private static String processDate(String[] dataArgs){
		String currentDate = new SimpleDateFormat(YMTDateUtil.YMDHMS).format(new Date());
		
		
		for(int i=1;i<dataArgs.length;i++){
			String dateUpdateVal = dataArgs[i].replaceAll("\\[", "").replaceAll("]", "");
			String[] updateArgs = dateUpdateVal.split(",");
			String dataUnit = updateArgs[0].toLowerCase();
			int dataCalc = Integer.parseInt(updateArgs[1]);
			switch (dataUnit) {
			
			case "s":
				currentDate = YMTDateUtil.getDateBeforeOrNextSecond(currentDate, dataCalc);
				break;
			
			case "mm":
				currentDate = YMTDateUtil.getBeforeOrNextMinute(currentDate, dataCalc);
				break;
			
			case "h":
				currentDate = YMTDateUtil.getBeforeOrNextHour(currentDate, dataCalc);
				break;
				
			case "d":
				currentDate = YMTDateUtil.getBeforeOrNextDay(currentDate, dataCalc);
				break;

			case "m":
				currentDate = YMTDateUtil.getBeforeOrNextMonth(currentDate, dataCalc);
				break;
			
			case "y":
				currentDate = YMTDateUtil.getBeforeOrNextYear(currentDate, dataCalc);
				break;
			
			default:
				break;
			}
			
		}
		
		
		return currentDate;
		
		
	}
	
	
	private static boolean isNeedAddKey(JSONObject beforeRet,String pathKey,String strKey){
		return pathKey.contains(strKey) && ! beforeRet.containsKey(strKey);
	}
}
