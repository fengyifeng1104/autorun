package com.ymatou.autorun.dataservice.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ymatou.autorun.dataservice.model.RunResultRecordModel;

public interface RunResultRecordDao{
	public String saveRunResultRecord(RunResultRecordModel resultRecordModel);
	public String saveRunResultRecord(List<RunResultRecordModel> resultRecordModels);
	public Page getRunResultRecordList(Integer page,Integer size,String sort);
}
