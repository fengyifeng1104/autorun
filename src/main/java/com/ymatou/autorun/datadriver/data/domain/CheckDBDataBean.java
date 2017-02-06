package com.ymatou.autorun.datadriver.data.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CheckDBDataBean {
	private String DBType;
	private String DBName;
	private String sqlStr;
	private Integer orderNum;
	private JSONObject assertMapJson;

	
	public CheckDBDataBean(String DBType,String DBName,String sqlStr,Integer orderNum,String  assertStr){
		this.DBType = DBType;
		this.DBName = DBName;
		this.sqlStr = sqlStr;
		this.orderNum = orderNum;
		this.assertMapJson = JSON.parseObject(assertStr);
	}
	

	public String getDBType() {
		return DBType;
	}


	public String getDBName() {
		return DBName;
	}


	public String getSqlStr() {
		return sqlStr;
	}

	public Integer getOrderNum() {
		return orderNum;
	}


	public JSONObject getAssertMap() {
		return assertMapJson;
	}



	public static void main(String[] args) {
	/*	String str = "Catalogs(scatalogId#CatalogId)";
		String regex = "\\(.*?\\)";
		Matcher matcher = Pattern.compile(regex).matcher(str);
		if(matcher.find()){
			System.out.println(matcher.group(0));
		}*/
		
		String aString = "{inum=20,iaction=0}";
		JSONObject aJsonObject= JSON.parseObject(aString.replace("=", ":"));
		int a =1;
		int c=a;
	}
	
}
