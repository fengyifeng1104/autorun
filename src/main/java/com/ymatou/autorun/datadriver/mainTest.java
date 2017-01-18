package com.ymatou.autorun.datadriver;

import com.alibaba.fastjson.JSON;
import com.ymatou.autorun.datadriver.execute.APICall;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecuteService;

public class mainTest {

	public static void main(String[] args) {
		String hostStr = "livemanage.iapi.ymatou.com";
		String apiStr = "/api/Activity/CloseActivity";
		String reqtypeStr = "POST";
		APICall apiCall = CaseExecuteService.generateApiCallInstance(hostStr,apiStr,reqtypeStr);
		
		apiCall.callAndGetReturnData(JSON.parseObject("{\"UserId\":20573576,\"ActivityId\":165499}"));
		
	}

}
