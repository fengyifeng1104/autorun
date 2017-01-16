package com.ymatou.autorun.datadriver.base.database.sqlwapper;

import java.util.List;

import com.ymatou.autorun.datadriver.base.database.model.Stc_CaseData;
import com.ymt.core.tool.Logger;
import com.ymt.database.SqlManager;

/************************************************************************************
 * @File name : StcCaseQueryWapper.java
 * @Author : ligaoge
 * @Date : 2016年11月17日
 * @Copyright : 洋码头
 ************************************************************************************/

public class StcCaseQueryWapper {
	SqlManager manage;

	public StcCaseQueryWapper() {
		manage = new SqlManager("stc");
	}
	
	/**
	 * 任务中心用更具用例ID查询用例数据接口
	 * 
	 * @param  caseId
	 * @return Stc_CaseData
	 */
	@SuppressWarnings("unchecked")
	public Stc_CaseData getCaseDataById(int caseId){
		List<Stc_CaseData> ret =  manage.getSelectResults(
				"com.ymatou.autorun.datadriver.base.database.mapping.StcMapper.selectCaseDataById",caseId);
		if (ret.size()<1){
			throw  new IndexOutOfBoundsException("case id ["+ String.valueOf(caseId) +"] is not found in DB - StcCaseQueryWapper");
		}
		return ret.get(0);
	}
	
	public static void main (String args[]){
		Stc_CaseData aCaseDatas = new StcCaseQueryWapper().getCaseDataById(10001);
		Logger.comment(aCaseDatas.getScene_host());
		Logger.comment(aCaseDatas.getScene_api());
		System.out.println("Done");
	}


}
