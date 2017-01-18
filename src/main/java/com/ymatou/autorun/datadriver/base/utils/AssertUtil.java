package com.ymatou.autorun.datadriver.base.utils;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.ymatou.autorun.datadriver.base.ymttf.tool.Logger;



public class AssertUtil {
	
	public static void assertTime(Date expectedDate,Date actualDate,int timeDiffSecond){
		Logger.verifyNotNull(expectedDate, "期待时间不该为空");
		Logger.verifyNotNull(actualDate, "实际时间不该为空");
		
		long time1 = expectedDate.getTime();
		long time2 = actualDate.getTime();
		Logger.verifyEquals(true,Math.abs(time1-time2)<=timeDiffSecond*1000,
				"时间比较,期望时间["+expectedDate+"],实际时间["+actualDate+"] 误差应不超过 "+timeDiffSecond+"秒");
		
		
	}
	
	
	public static void assertResultContain(JSONObject jsonObject,List<String> nodepathList ){
		for (String nodepath: nodepathList){
			Logger.verifyEquals(true, JsonBeanUtil.isJsonBeanNodeExist(jsonObject,nodepath), "实际值应包含期望值-node paht:"+nodepath);
		}
	}
	
	/***
	 * 结果比较
	 * @param tgtRetMap 期待结果
	 *  tgtRetMap.put("a","1")
	 * 
	 * @param actRetMap 实际结果
	 * actRetMap.put("a","1")
	 * 
	 * 		HashMap<String, Object> tgtRetMap = new HashMap<>();
			tgtRetMap.put("sProductId", 123);
			tgtRetMap.put("sProduct", "name");
		
			YmtProductsIWapper ymtProductsIWapper = new YmtProductsIWapper();           
	        List<Map> rm = ymtProductsIWapper.selectProductByProductIdforMap("6cd2c583-314e-4f7e-964f-0f41eaae37e5");
	        //取出第n条，转换为HashMap
	        HashMap<String, Object> actRetMap = (HashMap<String, Object>) rm.get(0);
	        
	        AssertService.assertResultEqual(tgtRetMap, actRetMap);
	 * 
	 */
	public static void assertResultEqual(Map<String, Object> tgtRetMap,Map<String, Object> actRetMap){
		assertResultEqual(tgtRetMap,actRetMap,"");
	}
	
	public static void assertResultEqual(Map<String, Object> tgtRetMap,Map<String, Object> actRetMap,String desc){
//		Logger.verifyNotNull(tgtRetMap, "tgtRetMap should not be null");
//		Logger.verifyNotNull(actRetMap, "actRetMap should not be null");
		Logger.verifyEquals(true, tgtRetMap !=null, "tgtRetMap should not be null");
		Logger.verifyEquals(true, actRetMap !=null, "actRetMap should not be null");
		
		for(String actKey:tgtRetMap.keySet()){
			if (actRetMap.containsKey(actKey)){
				Object tgtNodeObj = tgtRetMap.get(actKey);
				Object actNodeObj = actRetMap.get(actKey);
				Object[] ret = checkWorkAround(tgtNodeObj,actNodeObj);
				
				Logger.verifyEquals(ret[0], ret[1], desc + " 期望值key:[" + actKey + "] 检查");
			}else{
				Logger.verifyEquals(true, false, "期望值在实际值中不存在  key:["+ actKey + "]");
			}
		}
	}
	
	
	
	public static void assertResultEqual(Map<String, String> tgtAndActKeyPaths,Map<String, Object> tgtRetMap,Map<String, Object> actRetMap){
		assertResultEqual(tgtAndActKeyPaths,new JSONObject(tgtRetMap),new JSONObject(actRetMap));
	}
	
	public static void assertResultEqual(Map<String, String> tgtAndActKeyPaths,JSONObject tgtObject,Map<String, Object> actRetMap){
		assertResultEqual(tgtAndActKeyPaths,tgtObject,new JSONObject(actRetMap));
	}
	
	
	public static void assertResultEqual(Map<String, String> tgtAndActKeyPaths,Map<String, Object> tgtRetMap,JSONObject actObject){
		assertResultEqual(tgtAndActKeyPaths,new JSONObject(tgtRetMap),actObject);
	}
	
	
	public static void assertResultEqual(Set<String> keyPaths,Map<String, Object> tgtRetMap,JSONObject actObject){
		assertResultEqual(keyPaths,new JSONObject(tgtRetMap),actObject);
	}
	
	public static void assertResultEqual(Set<String> keyPaths,JSONObject tgtObject,JSONObject actObject){
		Map<String,String> tgtAndActKeyPaths = new HashMap<String,String>();
		for (String key : keyPaths){tgtAndActKeyPaths.put(key, key);}
		assertResultEqual(tgtAndActKeyPaths, tgtObject, actObject);
		
	}
	
	public static void assertResultEqual(Map<String, String> tgtAndActKeyPaths,JSONObject tgtObject,JSONObject actObject){
//		Logger.verifyNotNull(tgtAndActKeyPaths, "tgtAndActKeyPaths should not be null");
//		Logger.verifyNotNull(tgtObject, "tgtObject should not be null");
//		Logger.verifyNotNull(actObject, "actObject should not be null");
		Logger.verifyEquals(true, tgtAndActKeyPaths !=null, "tgtAndActKeyPaths should not be null");
		Logger.verifyEquals(true, tgtObject !=null, "tgtObject should not be null");
		Logger.verifyEquals(true, actObject !=null, "actObject should not be null");
		
		
		for(String tgtKeyPath:tgtAndActKeyPaths.keySet()){
			String actKeyPath = tgtAndActKeyPaths.get(tgtKeyPath)==null?tgtKeyPath:tgtAndActKeyPaths.get(tgtKeyPath);
			
			if(JsonBeanUtil.isJsonBeanNodeExist(tgtObject, tgtKeyPath)){
				if (JsonBeanUtil.isJsonBeanNodeExist(actObject, actKeyPath)){
					Object tgtNodeObj = JsonBeanUtil.getJsonBeanNodeObj(tgtObject,tgtKeyPath);
					Object actNodeObj = JsonBeanUtil.getJsonBeanNodeObj(actObject,actKeyPath);
					Object[] ret = checkWorkAround(tgtNodeObj,actNodeObj);
			
					Logger.verifyEquals(ret[0], ret[1], "期望值 key:[" + tgtKeyPath + "]和实际值 key:["+actKeyPath+"] 比较检查");
					
				}
				else{
					Logger.verifyEquals(true, false, "实际值集合中不存在 key:["+ actKeyPath + "]");
				}
			}else{
				Logger.verifyEquals(true, false, "期望值集合中不存在 key:["+ tgtKeyPath + "]");
			}
		}
	}
	
	
	private static Object[] checkWorkAround(Object tgtNodeObj,Object actNodeObj){
		//for number
		if (tgtNodeObj!= null && actNodeObj!=null){
			//整数，小数，负数
			Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$|^\\d+$|-\\d+$");
			boolean tgtMathcPtnAndActMatchPth = (pattern.matcher(tgtNodeObj.toString()).find()&&pattern.matcher(actNodeObj.toString()).find());
			
			boolean tgtIsNotNumAndActIsNum = !(isNumeric(tgtNodeObj.toString())) && (isNumeric(actNodeObj.toString()));
			boolean tgtIsNumAndActIsNotNum = (isNumeric(tgtNodeObj.toString())) && !(isNumeric(actNodeObj.toString()));
			
			if (tgtIsNotNumAndActIsNum || tgtIsNumAndActIsNotNum || tgtMathcPtnAndActMatchPth){
				tgtNodeObj = Double.parseDouble(tgtNodeObj.toString());
				actNodeObj = Double.parseDouble(actNodeObj.toString());
			}
		}else{
			//for null and "","null" ->""
			tgtNodeObj = (tgtNodeObj == null)?"":tgtNodeObj;
			actNodeObj = (actNodeObj == null)?"":actNodeObj;
		}
		
		return new Object[]{tgtNodeObj,actNodeObj};
	}
	
	
	
	/**
	 * 2个数组之间对比(String 数组)
	 * @param array1 数组1
	 * @param array2 数组2
	 * @throws Exception
	 */
	public static void checkArray(String[] array1 , String[] array2) throws Exception {
		try {
			
			  Arrays.sort(array1);  
              Arrays.sort(array2); 
              boolean aa = Arrays.equals(array1, array2);
              if (aa == true) {  
                      Logger.verifyEquals(true, aa, "两个数组中的值相同");  
              } else {  
                      Logger.verifyEquals(true, aa, "两个数组中的值不相同");             
              }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 2个list转String数组对比
	 * @param strList1 list1
	 * @param strList2 list2
	 * @throws Exception
	 */
	public static void checkArray(List strList1 , List strList2) throws Exception  {
		Object[] objs1 = strList1.toArray();
		String[] strs1 = (String[]) strList1.toArray(new String[0]);
		
		Object[] objs2 = strList2.toArray();
		String[] strs2 = (String[]) strList2.toArray(new String[0]);
		
		checkArray(strs1, strs2);
		
	}
	
	
	
	/**
	 * 2个JSONArray转数组数组对比
	 * @param strList1
	 * @param strList2
	 * @throws Exception
	 */
	public static void checkArray(JSONArray strList1 , JSONArray strList2) throws Exception  {
		String[] strstring1 = new String[strList1.size()];
		String[] strstring2 = new String[strList2.size()];
		
		for(int i=0;i<strList1.size();i++){
			strstring1[i]= strList1.getString(i);
		}
		
		for(int i=0;i<strList2.size();i++){
			strstring2[i]= strList2.getString(i);
		}
		
		checkArray(strstring1, strstring2);
		
	}
	
	
	
	/**
	 * 2个数组之间对比(int 数组)
	 * @param array1 数组1
	 * @param array2 数组2
	 * @throws Exception
	 */
	public static void checkArray(int[] array1 , int[] array2) throws Exception {
		try {
			
			  Arrays.sort(array1);  
              Arrays.sort(array2); 
              boolean aa = Arrays.equals(array1, array2);
              if (aa == true) {  
                      Logger.verifyEquals(true, aa, "两个数组中的值相同");  
              } else {  
                      Logger.verifyEquals(true, aa, "两个数组中的值不相同");             
              }				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
	
	public static boolean assertPreCheck(Object expected,Object actual){
		Object[] retObj = checkWorkAround(expected,actual);
		
		if (retObj[0] == null || retObj[1] == null) {
            if (retObj[0] == retObj[1]) {
               return true;
            } else {
                return false;
            }
        } else if (retObj[0].equals(retObj[1])) {
           return true;
        } else {
           return false;
        }
	}
	
	
	public static boolean isNumeric(String str){
	  try{
		  Double.parseDouble(str);
		  return true;
	  }catch(NumberFormatException e){
		  return false;
	  }
	}
	
	public static void main(String[] args) {
		System.out.println(isNumeric("1,0"));
	}
	
	
	
}
