package com.ymatou.autorun.datadriver.data.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBCheckDataBean {
	private String tableName;
	private Map<String, Object> searchMap ;
	private Map<String, Object> assertMap ;

	
	
	
	public DBCheckDataBean(String tableName,String searchStr,String assertStr){
		this.tableName = tableName;
		this.searchMap = strToMap(searchStr);
		this.assertMap = strToMap(assertStr);
	}
	
	private Map<String, Object> strToMap(String str){
		Map<String, Object> retMap = new HashMap<String,Object>();
		String[] ret = str.split(",");
		for(int i=0;i<ret.length;i++){
			String[] keyVal = ret[i].split("#");
			retMap.put(keyVal[0], keyVal[1]);
		}
		return retMap;
	}

	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public Map<String, Object> getAssertMap() {
		return assertMap;
	}

	public void setAssertMap(Map<String, Object> assertMap) {
		this.assertMap = assertMap;
	}


	public static void main(String[] args) {
		String str = "Catalogs(scatalogId#CatalogId)";
		String regex = "\\(.*?\\)";
		Matcher matcher = Pattern.compile(regex).matcher(str);
		if(matcher.find()){
			System.out.println(matcher.group(0));
		}
		
	}
	
}
