package com.ymatou.autorun.dataservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ymatou.autorun.dataservice.dao.RunningDataDao;
import com.ymatou.autorun.dataservice.model.RunningDataModel;


@Repository
public class RunningDataImpl implements RunningDataDao {
	
	@Resource
	private RunningDataInterface runningDataInterface;
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<RunningDataModel> getRunningDataByCasesIdList(List<Integer> caseIdList) {
		String idList=null;
		
		String sql="SELECT * FROM case_model as c left join scene_model as s on c.scene_id=s.scene_id"
					+"left join template_model as t on t.template_id=s.template_id"
					+"where c.case_id in ("+idList+")";
		List<RunningDataModel> runningDataModelList=jdbcTemplate.query(sql, new RowMapper<RunningDataModel>(){

			@Override
			public RunningDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				RunningDataModel runningDataModel=new RunningDataModel();
				runningDataModel.setCaseId(rs.getInt("case_id"));
				return runningDataModel;
			}
		});
		return runningDataModelList;
	}
	
}
