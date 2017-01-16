package com.ymatou.autorun.datadriver.data;


import java.util.List;
import java.util.Map;

import com.ymatou.autorun.datadriver.data.domain.DBCheckDataBean;

public interface AssertData {
	public String key_Param = "param";
	public String key_Sql= "sql";
	public String key_Mongo = "mongo";
	
	public String sqlClassPath = "com.ymttest.database.sqlwapper";
	
	public Map<String, Object> getParamMap();
	public List<DBCheckDataBean> getSqlCheckList();
	public List<DBCheckDataBean> getMongoCheckList() ;
	
}
