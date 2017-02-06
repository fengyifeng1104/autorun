package com.ymatou.autorun.datadriver.data;


import java.util.List;
import java.util.Map;

import com.ymatou.autorun.datadriver.data.domain.CheckDBDataBean;

public interface AssertData {
	public String key_Param = "returndata";
	public String key_Sql= "sql";
	public String key_Mongo = "mongo";
	
	public String SqlStr_Param_Regex = "\\$\\{.*?\\}";
	
	public Map<String, Object> getReturnDataCheckMap();
	public List<CheckDBDataBean> getSqlCheckList();
	public List<CheckDBDataBean> getMongoCheckList() ;
	
}
