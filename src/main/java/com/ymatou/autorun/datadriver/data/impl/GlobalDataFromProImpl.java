package com.ymatou.autorun.datadriver.data.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.autorun.datadriver.data.GlobalData;

public class GlobalDataFromProImpl implements GlobalData{
	
	@Override
	public Map<String, String> getKeyVal() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("userId", "20336378");
		map.put("userName", "fyftest4");
		map.put("adminUserName", "testmn");
		map.put("adminUserPswd", "123123");
		map.put("activityId", "18946");
		
		
		return map;
	}

	
}
