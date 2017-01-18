package com.ymatou.autorun.dataservice.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.dataservice.model.RunningDataModel;
import com.ymatou.autorun.dataservice.service.RunningService;

@RestController
@RequestMapping(value="/RunningController")
public class RunningController {
	@Resource
	private RunningService runningService;
	
	@RequestMapping(value="/getRunningDataByCasesIdList",method={RequestMethod.POST})
	public List<RunningDataModel> getRunningDataByCasesIdList(@RequestBody JSONObject caseIdList){
		
		
		System.out.println(caseIdList.get("userId"));
		System.out.println(caseIdList.get("caseIdList"));
		System.out.println("controller层运行222正常222fff");
		System.out.println("controller层运行正常ssssssss");

		return runningService.getRunningDataByCasesIdList(caseIdList);
	}

}
