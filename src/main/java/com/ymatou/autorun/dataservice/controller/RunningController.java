package com.ymatou.autorun.dataservice.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.ymatou.autorun.dataservice.service.RunningService;

@RestController
@RequestMapping(value="/RunningController")
public class RunningController {
	@Resource
	private RunningService runningService;
	
	@RequestMapping(value="/getRunningDataByCasesIdList",method={RequestMethod.POST})
	public String getRunningDataByCasesIdList(@RequestBody JSONArray caseIdList){
		runningService.getRunningDataByCasesIdList(caseIdList);
		return "接受成功";
	}

}
