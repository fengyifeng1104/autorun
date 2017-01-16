package com.ymatou.autorun.dataservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/RunningController")
public class RunningController {
	
	@RequestMapping(value="/getCasesInfoByCaseIdList",method={RequestMethod.POST})
	public String getCasesInfoByCaseIdList(){
		return "接受成功";
	}

}
