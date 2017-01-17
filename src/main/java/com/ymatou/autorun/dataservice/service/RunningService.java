package com.ymatou.autorun.dataservice.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

@Service
public class RunningService {
	
	@Resource
	private RunningDataDao runningDataDao;
	
	
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONArray caseIdList){
		return runningDataDao.getRunningDataByCasesIdList(caseIdList.toString());
	}

}
