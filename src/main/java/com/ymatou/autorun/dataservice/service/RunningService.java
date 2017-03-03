package com.ymatou.autorun.dataservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.AppRunConf;
import com.ymatou.autorun.datadriver.base.autoresult.ResultParser;
import com.ymatou.autorun.datadriver.base.autoresult.database.Wapper.TestCaseWapper;
import com.ymatou.autorun.datadriver.base.utils.JsonBeanUtil;
import com.ymatou.autorun.datadriver.base.utils.YMTDateUtil;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;
import com.ymatou.autorun.datadriver.face.SqlSearch;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunResultDataModel;
import com.ymatou.autorun.dataservice.model.RunningDataModel;

@Service
public class RunningService {
	
	@Resource
	private AppRunConf appRunConf;
	
	@Resource
	private RunningDataDao runningDataDao;
	
	@Resource
	private SqlSearch sqlSearch;
	
	
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONObject caseIdList){
		//run case集合
		List<RunningDataModel> rets = runningDataDao.getRunningDataByCasesIdList(caseIdList.getJSONArray("caseIdList"));
		
		//run 站点集合
		Set<String> domainList = new HashSet<>();
		rets.forEach(ele->domainList.add(ele.getSceneHost()));
		
		//run case 按站点分组
		List<List<RunningDataModel>> runSet = new ArrayList<>();
		domainList.forEach(domain->{
			runSet.add(rets.stream().filter(model->model.getSceneHost().equals(domain)).collect(Collectors.toList()));
		});
		
		//run case
		String runfolder = CaseExecute.executeAndCheck(runningDataDao,sqlSearch,runSet);
		
		
		//get result
		TestCaseWapper tcw = new TestCaseWapper();
		String env = appRunConf.getEnv();
		ResultParser resultParser = new ResultParser();
		String path = appRunConf.getResultPath()+"/"+runfolder;
		
		// 设置结果文件路径
		resultParser.setPath(path);

		//执行的批次号
		domainList.forEach(domain->{ 
			try {
				int passid = resultParser.parserXmlToDb(tcw, env, domain);
				YMTDateUtil.waitTime(2);
				System.out.println("domain:"+domain+",passid:"+passid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return rets;
	}
	
	
	
	
	@Transactional
	public RunResultDataModel getRunningDataByCasesId(JSONObject requestJson){
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
