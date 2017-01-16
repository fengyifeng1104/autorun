package com.ymatou.autorun.datadriver.base.database.mapping;

import java.util.List;

import com.ymatou.autorun.datadriver.base.database.model.Stc_CaseData;


public interface StcMapper {
	
	List<Stc_CaseData> selectCaseDataById(Integer caseId);
}
