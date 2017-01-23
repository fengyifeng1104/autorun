package com.ymatou.autorun.dataservice.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.MapUtil;
import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.data.impl.ImportDataFromMySQLImpl;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

@Service
public class RunningService {
	
	@Resource
	private RunningDataDao runningDataDao;
	
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONObject caseIdList){
		System.out.println("controller层运行正常");
		System.out.println("service层运行正常");

		
		List<RunningDataModel> rets = runningDataDao.getRunningDataByCasesIdList(caseIdList.getJSONArray("caseIdList"));
		
		for(RunningDataModel runningDataModel: rets){
			GlobalData globalData = new GlobalData() {
				@Override
				public Map<String, String> getKeyVal() {
					return MapUtil.hashMap("userId", "3383");
				}
			};
			
			
			ImportData importData = new ImportDataFromMySQLImpl(runningDataModel);
			
			//run test case
			CaseExecute.executeAndCheck(importData,globalData);
		}
		
		
		
		
		return rets;
	}

}
