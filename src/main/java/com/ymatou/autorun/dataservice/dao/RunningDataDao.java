package com.ymatou.autorun.dataservice.dao;

import java.util.List;

import com.ymatou.autorun.dataservice.model.RunningDataModel;

public interface RunningDataDao {
	
	public List<RunningDataModel> getRunningDataByCasesIdList(String caseIdList);

}
