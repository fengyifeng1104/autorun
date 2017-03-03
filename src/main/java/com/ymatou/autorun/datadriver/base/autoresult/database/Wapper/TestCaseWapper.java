package com.ymatou.autorun.datadriver.base.autoresult.database.Wapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.ResultContent;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestCase;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestCaseResult;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestPass;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestSuite;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestSuiteResult;
import com.ymatou.autorun.datadriver.base.utils.DBUtil;
import com.ymatou.autorun.datadriver.base.utils.DataManager;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.base.utils.RefBean;



/************************************************************************************
 * @File name :TestcaseWapper.java
 * @Author : zhouyi
 * @Date : 2016年2月25日
 * @Copyright : 洋码头
 ************************************************************************************/
public class TestCaseWapper {
	private static DBUtil db;

	public TestCaseWapper() {
		if (db==null){
			db = new DBUtil();
		}
	}

	public DBUtil getDb() {		
		return db;
	}

	// Testsuite操作 增改查
	/**
	 * 插入Testsuite,如果存在更新description updatetime,如果url+applicationid查询超过1条报错
	 * @param bb TestSuite
	 * @throws SQLException
	 */
	public void insertTestsuite(TestSuite bb) throws Exception {
		List<TestSuite> list = selectTestSuite(bb.getUrl(), bb.getApplicationid());
		List<String> value = new ArrayList<String>();
		if (list.size() <= 0) {
			value.add(bb.getCreatetime());
			value.add(bb.getDescription());
			value.add(bb.getUrl());
			value.add(bb.getUpdatetime());
			value.add(bb.getApplicationid().toString());
			value.add("0");
			String insertsql = "insert into Testsuite (createtime,description,url,updatetime,applicationid,del) value (?,?,?,?,?,?)";
			db.update(insertsql, value);
		} else if (list.size() == 1) {
			TestSuite tts = list.get(0);
			if (tts.getDescription()==null||!tts.getDescription().equals(bb.getDescription())) {
				value.add(bb.getDescription());
				value.add(bb.getUpdatetime());
				value.add(bb.getApplicationid().toString());
				value.add(bb.getUrl());
				String updatesql = "update Testsuite set description=?,updatetime=? where applicationid=? and url=?";
				db.update(updatesql, value);
			}
		} else {
			throw new Exception("发现了重复casesutie,url:" + bb.getUrl() + "  appid:" + bb.getApplicationid());
		}
	}


	/**
	 * 查询testsuite
	 * @param url
	 * @param applicationid
	 * @return List<TestSuite>
	 * @throws SQLException
	 */
	public List<TestSuite> selectTestSuite(String url, Integer applicationid) throws SQLException {
		List<String> value = new ArrayList<String>();
		value.add(url);
		value.add(applicationid.toString());
		String sql = "select * from Testsuite where url=? and applicationid=?";
		ResultSet res = db.select(sql, value);
		List<TestSuite> list = new RefBean().getInstances(res, TestSuite.class);
		return list;
	}

	// Testcase操作 增改查
	/**
	 * 查询testcase
	 * @param name
	 * @param testsuiteid
	 * @return List<TestCase>
	 * @throws SQLException
	 */
	public List<TestCase> selectTestcase(String name, Integer testsuiteid) throws SQLException {
		List<String> value = new ArrayList<String>();
		value.add(name);
		value.add(testsuiteid.toString());
		String sql = "select * from Testcase where name=? and testsuiteid=?";
		ResultSet res = db.select(sql, value);
		List<TestCase> list = new RefBean().getInstances(res, TestCase.class);
		return list;
	}

	/**
	 * 插入testcase,如果存在更新description updatetime,如果name+testsuiteid查询超过1条报错
	 * @param bb TestCase
	 * @throws Exception
	 */
	public void insertTestcase(TestCase bb) throws Exception {
		List<TestCase> list = selectTestcase(bb.getName(), bb.getTestsuiteid());
		List<String> value = new ArrayList<String>();
		if (list.size() <= 0) {
			value.add(bb.getCreatetime());
			value.add(bb.getDescription());
			value.add(bb.getName());
			value.add(bb.getUpdatetime());
			value.add(bb.getTestsuiteid().toString());
			value.add("0");
			value.add(bb.getType());
			String insertsql = "insert into Testcase (createtime,description,name,updatetime,testsuiteid,del,type) value (?,?,?,?,?,?,?)";
			db.update(insertsql, value);
		} else if (list.size() == 1) {
			TestCase tts = list.get(0);
			if (!tts.getDescription().equals(bb.getDescription())||!tts.getType().equals(bb.getType())) {
				value.add(bb.getDescription());
				value.add(bb.getUpdatetime());
				value.add(bb.getType());
				value.add(bb.getName());
				value.add(bb.getTestsuiteid().toString());
				String updatesql = "update Testcase set description=?,updatetime=?,type=? where name=? and testsuiteid=?";
				db.update(updatesql, value);
			}
		} else {
			throw new Exception("发现了重复case,name:" + bb.getName() + "  testsuiteid:" + bb.getTestsuiteid());
		}
	}

	/**
	 * 使用id更新testcase
	 * update testcase set description=? where id=?
	 * 
	 * @param id
	 * @param description
	 * @throws SQLException
	 */
	public void updateTestcaseById(Integer id, String description) throws SQLException {
		List<String> value = new ArrayList<String>();
		value.add(description);
		value.add(id.toString());
		String sql = "update Testcase set description=? where id=?";
		db.update(sql, value);
	}

	// application 操作 查询
	/**
	 * 使用domain查询appid 用于applicationid字段
	 * 
	 * @param domain
	 * @return
	 * @throws SQLException
	 */
	public int selectAppIdByDomain(String domain) throws SQLException {
		int appid = 0;
		List<String> value = new ArrayList<String>();
		value.add(domain);
		String sql = "select id from Application where domain=?";
		ResultSet res = db.select(sql, value);
		res.next();
		appid = res.getInt(1);
		return appid;
	}

	// Testpass 操作
	/**
	 * 插入一次新的testpass
	 * 
	 * @param bb
	 * @return passid
	 * @throws Exception
	 */
	public int insertTestPass(TestPass bb) throws Exception {
		List<String> value = new ArrayList<String>();
		value.add(bb.getCreatetime());
		value.add(bb.getApplicationid().toString());
		value.add(bb.getEnvid().toString());
		value.add("0");
		String insertsql = "insert into Testpass (createtime,applicationid,envid,del) value (?,?,?,?)";
		db.update(insertsql, value);
		int passid = selectTestPass(bb.getCreatetime());
		return passid;
	}

	/**
	 * 使用createtime查询passid
	 * 
	 * @param createtime
	 * @return
	 * @throws Exception 
	 */
	public int selectTestPass(String createtime) throws Exception {
		int passid = 0;
		List<String> value = new ArrayList<String>();
		value.add(createtime);
		System.out.println(createtime);
		String sql = "select id from Testpass where createtime=?";
		ResultSet res = db.select(sql, value);
		if (res.next()) {
			passid = res.getInt(1);
		} else {
			throw new Exception("TestPass id not found:createtime " + createtime);
		}
		return passid;
	}

	// Testsuiteresult 操作
	/**
	 * 插入一次新的TestSuiteResult
	 * 
	 * @param bb TestSuiteResult
	 * @return TestSuiteResultId
	 * @throws Exception
	 */
	public int insertTestSuiteResult(TestSuiteResult bb) throws Exception {
		List<String> value = new ArrayList<String>();
		value.add(bb.getTestpassid().toString());
		value.add(bb.getTestsuiteid().toString());
		value.add("0");
		String insertsql = "insert into TestsuiteResult (testpassid,testsuiteid,del) value (?,?,?)";
		db.update(insertsql, value);
		int testSuiteResultId = selectTestSuiteResultId(bb.getTestpassid(),bb.getTestsuiteid());
		return testSuiteResultId;
	}
	/**
	 * 查询TestSuiteResultId
	 * @param testpassid
	 * @param testsuiteid
	 * @return testSuiteResultId
	 * @throws Exception
	 */
	public int selectTestSuiteResultId(Integer testpassid,Integer testsuiteid) throws Exception {
		int testSuiteResultId = 0;
		List<String> value = new ArrayList<String>();
		value.add(testpassid.toString());
		value.add(testsuiteid.toString());
		String sql = "select id from TestsuiteResult where testpassid=? and testsuiteid=?";
		ResultSet res = db.select(sql, value);
		if (res.next()) {
			testSuiteResultId = res.getInt(1);
		} else {			
			Logger.debug("TestSuiteResult id not found! sql=" +sql+" "+"testpassid="+ testpassid+":"+"testsuiteid="+testsuiteid);
		}
		return testSuiteResultId;
	}
	// Testcaseresult 操作
	/**
	 * 插入一次新的TestCaseResult
	 * 
	 * @param bb
	 * @return TestCaseResultId
	 * @throws Exception
	 */
	public int insertTestCaseResult(TestCaseResult bb) throws Exception {
		List<String> value = new ArrayList<String>();
		value.add(bb.getTestcaseid().toString());
		value.add(bb.getTestsuiteresultid().toString());
		value.add(bb.getStatus());
		value.add("0");
		//add by sean
		value.add(DataManager.getSysTime("yyyy-MM-dd HH:mm:ss"));
		String insertsql = "insert into TestcaseResult (testcaseid,testsuiteresultid,status,del,createtime) value (?,?,?,?,?)";
		//String insertsql = "insert into TestcaseResult (testcaseid,testsuiteresultid,status,del) value (?,?,?,?)";
		db.update(insertsql, value);
		int testCaseResultId = selectTestCaseResultId(bb.getTestcaseid(),bb.getTestsuiteresultid());
		return testCaseResultId;
	}
	/**
	 * 查询TestCaseResultId
	 * @param testcaseid
	 * @param testsuiteresultid
	 * @return
	 * @throws Exception
	 */
	public int selectTestCaseResultId(Integer testcaseid,Integer testsuiteresultid) throws Exception {
		int passid = 0;
		List<String> value = new ArrayList<String>();
		value.add(testcaseid.toString());
		value.add(testsuiteresultid.toString());
		//失败case 重新再跑，会有多个记录，每次取最大的值
		String sql = "select max(id) from TestcaseResult where testcaseid=? and testsuiteresultid=?";
		ResultSet res = db.select(sql, value);
		if (res.next()) {
			passid = res.getInt(1);
		} else {
			throw new Exception("TestCaseResult id not found: " + testcaseid+":"+testsuiteresultid);
		}
		return passid;
	}
	// resultcontent 操作
	/**
	 * 插入一次新的resultcontent
	 * 
	 * @param bb
	 * @return TestCaseResultId
	 * @throws Exception
	 */
	public void insertResultContent(ResultContent bb) throws Exception {
		List<String> value = new ArrayList<String>();
		value.add(bb.getContent());
		value.add(bb.getCreatetime());
		value.add(bb.getStatus());
		value.add(bb.getType());
		value.add(bb.getTestcaseresultid().toString());
		value.add("0");
		String insertsql = "insert into ResultContent (content,createtime,status,type,testcaseresultid,del) value (?,?,?,?,?,?)";
		db.update(insertsql, value);
	}
	// env操作
	/**
	 * 查询envid
	 * 
	 * @param env
	 *            环境名 SIT1 UAT
	 * @return envid
	 * @throws Exception
	 */
	public int selectEnvId(String env) throws Exception {
		int envid = 0;
		List<String> value = new ArrayList<String>();
		value.add(env);
		String sql = "select id from Env where name=?";
		ResultSet res = db.select(sql, value);
		if (res.next()) {
			envid = res.getInt(1);
		} else {
			throw new Exception("ENV not found:" + env);
		}
		return envid;
	}


}
