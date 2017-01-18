package com.ymatou.autorun.datadriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.execute.APICall;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecuteService;
import com.ymatou.autorun.dataservice.controller.RunningController;

public class mainTest {

	public static void main(String[] args) {
		
		
		JSONObject aJsonObject = JSON.parseObject("{caseId:[20,23]}");
		System.out.println(aJsonObject.toJSONString());
		new RunningController().getRunningDataByCasesIdList(aJsonObject);
		
	/*	String hostStr = "livemanage.iapi.ymatou.com";
		String apiStr = "/api/Activity/CloseActivity";
		String reqtypeStr = "POST";
		APICall apiCall = CaseExecuteService.generateApiCallInstance(hostStr,apiStr,reqtypeStr);
		
		
		
		
		
		apiCall.callAndGetReturnData(JSON.parseObject("{\"UserId\":20573576,\"ActivityId\":165499}"));*/
		
	}

}
