package com.ymatou.autorun.dataservice.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;
import com.ymatou.autorun.datadriver.face.SqlSearch;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunResultDataModel;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

@Service
public class RunningService {
	
	@Resource
	private RunningDataDao runningDataDao;
	
	@Resource
	private SqlSearch sqlSearch;
	
	
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONObject caseIdList){
		System.out.println("controller层运行正常");
		System.out.println("service层运行正常");
		List<RunningDataModel> rets = runningDataDao.getRunningDataByCasesIdList(caseIdList.getJSONArray("caseIdList"));
		
		
		CaseExecute.executeAndCheck(runningDataDao,sqlSearch,rets);
		return rets;
	}
	
	
	
	
	@Transactional
	public RunResultDataModel getRunningDataByCasesId(JSONObject requestJson){
		System.out.println("controller层运行正常");
		System.out.println("service层运行正常");
		int caseId = requestJson.getInteger("caseId");
		JSONArray caseIdAry = new JSONArray();
		caseIdAry.add(caseId);
		
		RunningDataModel rdm = runningDataDao.getRunningDataByCasesIdList(caseIdAry).get(0);
		JSONObject runResult =  CaseExecute.executeAsPostMan(runningDataDao, sqlSearch, rdm);
		
		String requestStr = runResult.getJSONObject(JsonBeanUtil.JsonBeanRequest).toJSONString();
		runResult.remove(JsonBeanUtil.JsonBeanRequest);
		String responseStr = runResult.toJSONString();
		
		
		RunResultDataModel resultDataModel = new RunResultDataModel();
		resultDataModel.setStrHost(rdm.getSceneHost());
		resultDataModel.setStrApi(rdm.getSceneApi());
		resultDataModel.setStrRequest(requestStr);
		resultDataModel.setStrResponse(responseStr);
		
		
		return resultDataModel;
	}
}
