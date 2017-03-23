package com.ymatou.autorun.datadriver.face.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ymatou.autorun.datadriver.data.conf.SqlDSconf;
import com.ymatou.autorun.datadriver.face.SqlSearch;


@Repository
public class SqlSearchImpl implements SqlSearch{

	@Autowired
	@Qualifier("YmtReleaseJdbcTemplate")
	private JdbcTemplate ymtreleaseJdbcTemplate;
	
	@Autowired
	@Qualifier("IntegratedProductJdbcTemplate")
	private JdbcTemplate integratedproductJdbcTemplate;
	
	@Autowired
	@Qualifier("AppProductReleaseJdbcTemplate")
	private JdbcTemplate appProductReleaseJdbcTemplate;
	
	@Autowired
	@Qualifier("EvaluatedbJdbcTemplate")
	private JdbcTemplate evaluatedbJdbcTemplate;
	
	
	@Override
	public List<Map<String, Object>> selectBy(String dataSourceName, String sqlStr) {
		String ucaseStr = sqlStr.toUpperCase();
		if (SqlDSconf.IntegratedProductStr.equalsIgnoreCase(dataSourceName)){
			return integratedproductJdbcTemplate.queryForList(ucaseStr);
			
		}else if (SqlDSconf.YmtReleaseStr.equalsIgnoreCase(dataSourceName)) {
			return ymtreleaseJdbcTemplate.queryForList(ucaseStr);
			
		}else if (SqlDSconf.AppProductReleaseStr.equals(dataSourceName)) {
			return appProductReleaseJdbcTemplate.queryForList(ucaseStr);
			
		}else if (SqlDSconf.EvaluatedbStr.equals(dataSourceName)){
			return evaluatedbJdbcTemplate.queryForList(ucaseStr);
		
		}else {
			return integratedproductJdbcTemplate.queryForList(ucaseStr);
			
		}
	}
	
	
	
	
	
/*	try {
		List<Map<String,Object>> map = sqlsearch.selectBy(DataSourceConfig.YmtReleaseStr,"select Top 10 * from PaymentOrder");
		List<Map<String,Object>> map1 = sqlsearch.selectBy(DataSourceConfig.IntegratedProductStr,"select Top 10 * from YMT_PRODUCTS");
		List<Map<String,Object>> map2 = sqlsearch.selectBy(DataSourceConfig.AppProductReleaseStr,"select top 10 * from Ymt_Activity");
		int a=1;
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/

}
