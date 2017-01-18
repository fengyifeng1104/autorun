package com.ymatou.autorun.dataservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;


@Repository
public class RunningDataImpl implements RunningDataDao {
	
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional
	public List<RunningDataModel> getRunningDataByCasesIdList(JSONArray caseIdList) {
		String sCaseIdList="";
		for(Object caseId :caseIdList){
			sCaseIdList=sCaseIdList+caseId.toString()+",";
		}
		sCaseIdList=sCaseIdList.substring(0,sCaseIdList.length()-1);
		
		String sql="SELECT * FROM case_model as c left join scene_model as s on c.scene_id=s.scene_id "
					+"left join template_model as t on t.template_id=s.template_id "
					+"where c.case_id in ("+sCaseIdList+")";
		
		System.out.println("查询的sql是："+sql);
		
		List<RunningDataModel> runningDataModelList=jdbcTemplate.query(sql, new RowMapper<RunningDataModel>()
		{
			@Override
			public RunningDataModel mapRow(ResultSet rs, int rowNum) throws SQLException 
			{
				RunningDataModel runningDataModel=new RunningDataModel();
				
				runningDataModel.setCaseId(rs.getInt("case_id"));
				runningDataModel.setCaseDescription(rs.getString("case_description"));
				runningDataModel.setModelDescription(rs.getString("model_description"));
				runningDataModel.setSceneHost(rs.getString("scene_host"));
				runningDataModel.setSceneApi(rs.getString("scene_api"));
				runningDataModel.setReqMethod(rs.getString("req_method"));
				runningDataModel.setTemplateName(rs.getString("template_name"));
				runningDataModel.setTemplateDetail(rs.getString("template_detail"));
				runningDataModel.setBeforeToDo(rs.getString("before_to_do"));
				runningDataModel.setAfterToDo(rs.getString("after_to_do"));
				runningDataModel.setInput2SqlMap(rs.getString("input2sql_map"));
				runningDataModel.setSql2MongoMap(rs.getString("sql2mongo_map"));
				runningDataModel.setOutput2SqlMap(rs.getString("output2sql_map"));
				runningDataModel.setOutput2MongoMap(rs.getString("output2mongo_map"));
				runningDataModel.setExtraInputList(rs.getString("extra_input_list"));
				runningDataModel.setExtraCheckList(rs.getString("extra_check_list"));
				runningDataModel.setDefaultCheckType(rs.getString("default_check_type"));

				System.out.println(runningDataModel.getCaseId());
				return runningDataModel;
			}
		});
		//System.out.println("这是dao层实现的数据："+runningDataModelList.get(0).getCaseId());
		return runningDataModelList;
	}
	
}
