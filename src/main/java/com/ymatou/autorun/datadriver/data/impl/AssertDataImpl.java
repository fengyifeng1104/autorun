package com.ymatou.autorun.datadriver.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ymatou.autorun.datadriver.data.AssertData;
import com.ymatou.autorun.datadriver.data.domain.CheckDBDataBean;



public class AssertDataImpl implements AssertData{
	private Map<String,Object> returnDataCheckMap = new HashMap<String,Object>();
	private List<CheckDBDataBean> sqlCheckList = new ArrayList<CheckDBDataBean>();
	private List<CheckDBDataBean>  mongoCheckList = new ArrayList<CheckDBDataBean>();
	
	
	public AssertDataImpl(){}
	
	public AssertDataImpl(Map<String, String> caseAssertMap){
		initData(caseAssertMap);
	}
	
	
	
	private void initData(Map<String, String> caseAssertMap){
		if (caseAssertMap !=null){
			for(String K:caseAssertMap.keySet()){
				boolean isKeyNameRight = false;
				String V = caseAssertMap.get(K);
				String[] _Kargs = K.split("\\.");
				
				if (_Kargs[0].equals(AssertData.key_Sql)){
					isKeyNameRight = true;
					
					//get sql str
					String sqlRegex =  "\\(.*?\\)";
					Matcher matcher = Pattern.compile(sqlRegex).matcher(K);
					
					if (matcher.find()){
						String sqlStr = matcher.group().replaceAll("\\(", "").replaceAll("\\)", "");
						_Kargs = matcher.replaceAll("").split("\\.");
						if (_Kargs.length==4){
							String dbType = _Kargs[0];
							String dbName = _Kargs[1];
							Integer orderNum = Integer.parseInt(_Kargs[3]);
							String assertStr = V.replaceAll("=", ":");
							sqlCheckList.add(new CheckDBDataBean(dbType, dbName, sqlStr, orderNum, assertStr));
						}else{
							throw new IllegalArgumentException("Assert - if key is " + AssertData.key_Sql + ", it should be sql.dbname.(sql string)[0]#{a=1,b=2} ");
						}
					}else{
						throw new NullPointerException("sql is not found ");
					}
					
					
				}
				
				
				//todo return data and mongo 
				
				
				//check invalid key name
				if (!isKeyNameRight){
					throw new IllegalArgumentException("Assert - if key [" + K + "] is invalid and must be one of "
							+AssertData.key_Param+","+AssertData.key_Sql+","+AssertData.key_Mongo);
				}
				
			}
		}
		
		
		
		
	}
	
	
	
	
	@Override
	public Map<String, Object> getReturnDataCheckMap() {
		// TODO Auto-generated method stub
		return returnDataCheckMap;
	}

	@Override
	public List<CheckDBDataBean> getSqlCheckList() {
		// TODO Auto-generated method stub
		return sqlCheckList;
	}

	@Override
	public List<CheckDBDataBean> getMongoCheckList() {
		// TODO Auto-generated method stub
		return mongoCheckList;
	}



}
