package com.ymatou.autorun.dataservice.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

@Service
public class RunningService {
	
	@Resource
	private RunningDataDao runningDataDao;
	
	
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONObject caseIdList){
		
		
		System.out.println("controller层运行正常");
		System.out.println(caseIdList.get("userId"));
		System.out.println(caseIdList.get("caseIdList"));
		System.out.println("service层运行正常");
		
		
		return runningDataDao.getRunningDataByCasesIdList(caseIdList.getJSONArray("caseIdList"));
	}

}
