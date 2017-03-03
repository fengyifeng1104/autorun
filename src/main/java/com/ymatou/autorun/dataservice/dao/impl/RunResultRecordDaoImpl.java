package com.ymatou.autorun.dataservice.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ymatou.autorun.dataservice.dao.RunResultRecordDao;
import com.ymatou.autorun.dataservice.dao.RunResultRecordInterface;
import com.ymatou.autorun.dataservice.model.RunResultRecordModel;

@Repository
public class RunResultRecordDaoImpl implements RunResultRecordDao{
	@Autowired
	@Qualifier("DataDriverJdbcTemplate")
	private JdbcTemplate JdbcTemplate;
	
	@Resource
	private RunResultRecordInterface runResultRecordInterface;
	
	
	@Override
	public String saveRunResultRecord(RunResultRecordModel resultRecordModel) {
		runResultRecordInterface.save(resultRecordModel);
		return "保存成功";
	}
	
	@Override
	public String saveRunResultRecord(List<RunResultRecordModel> resultRecordModels) {
		runResultRecordInterface.save(resultRecordModels);
		return "保存成功";
	}

	@Override
	public Page getRunResultRecordList(Integer page, Integer size, String sort) {
		Sort sortRequest=new Sort(Sort.Direction.DESC, sort);
		Pageable pageable=new PageRequest(page, size, sortRequest);
		return runResultRecordInterface.findAll(pageable);
	}



}
