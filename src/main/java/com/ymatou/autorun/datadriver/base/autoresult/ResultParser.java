package com.ymatou.autorun.datadriver.base.autoresult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.ResultContent;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestCase;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestCaseResult;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestPass;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestSuite;
import com.ymatou.autorun.datadriver.base.autoresult.database.Bean.TestSuiteResult;
import com.ymatou.autorun.datadriver.base.autoresult.database.Wapper.TestCaseWapper;
import com.ymatou.autorun.datadriver.base.utils.DataManager;
import com.ymatou.autorun.datadriver.base.utils.FormatUtil;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.base.utils.XmlUtil;





public class ResultParser {
	/**
	 * evn domain testsuites(List) {testsuites=[Ts_Accounting, Ts_CreateAccount,
	 * Ts_GetAccount, Ts_GetAccountBalance, Ts_GetAccountByUserId,
	 * Ts_GetAccountEntry, Ts_GetAccountEntryList, Ts_GetAccountListByUserId,
	 * Ts_SetAccountLockStatus], domain=api.accounting.i.ymatou.com,
	 * env=SIT1,applicationid=22}
	 */
	private Map<String, Object> testsuite_m = new HashMap<String, Object>();
	
	//定义xml结果文件绝对路径
	private String path;

	public ResultParser() {
	}

	/**
	 * 获取testsuite info
	 * 
	 * @return {testsuites=[com.ymatou.database.Bean.TestSuite],
	 *         domain=api.accounting.i.ymatou.com, env=SIT1, applicationid=22}
	 * @throws Exception
	 */
	public Map<String, Object> getTestSuiteInfo() throws Exception {
		ArrayList<String> list = DataManager.getFilelist(this.getPath() + File.separator
				+ testsuite_m.get("domain").toString());
		ArrayList<TestSuite> casesuitelist = new ArrayList<TestSuite>();
		for (String filepath : list) {
			if(filepath.substring(filepath.length()-4).equals(".xml")){
				TestSuite suiteMap = parserTestSuite(filepath);
				casesuitelist.add(suiteMap);
			}
		}
		testsuite_m.put("testsuites", casesuitelist);
		return testsuite_m;
	}

	/**
	 * 获取本次所有testcases info
	 * 
	 * @return [com.ymatou.database.Bean.TestCase]
	 * @throws Exception
	 */
	public List<TestCase> getTestCaseInfo() throws Exception {
		List<TestCase> allcases = new ArrayList<TestCase>();
		ArrayList<String> list = DataManager.getFilelist(this.getPath()+ File.separator
				+ testsuite_m.get("domain").toString());
		for (String filepath : list) {
			if(filepath.substring(filepath.length()-4).equals(".xml")){
				allcases.addAll(parserTestCase(filepath));
			}

		}
		return allcases;
	}

	/**
	 * 解析TestSuite 信息
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public TestSuite parserTestSuite(String filepath) throws Exception {
		TestSuite ts = new TestSuite();
		XmlUtil xmlu;
		try {
			xmlu = new XmlUtil(filepath);
		} catch (Exception e) {
			throw new Exception("解析xml文件异常：" + filepath + ":" + e.getMessage());
		}
		ts.setCreatetime(DataManager.getSysTime());
		ts.setUpdatetime(DataManager.getSysTime());
		// 获取applicationid
		ts.setApplicationid(Integer.valueOf(testsuite_m.get("applicationid").toString()));
		ts.setUrl(xmlu.getRoot().attributeValue("url"));
		ts.setDescription(xmlu.getRoot().attributeValue("description"));
		return ts;
	}

	/**
	 * 解析TestCase 信息
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public List<TestCase> parserTestCase(String filepath) throws Exception {
		List<TestCase> caselist = new ArrayList<TestCase>();
		XmlUtil xmlu;
		try {
			xmlu = new XmlUtil(filepath);
		} catch (Exception e) {
			throw new Exception("解析xml文件异常：" + filepath + ":" + e.getMessage());
		}
		Integer testsuiteid = new TestCaseWapper()
				.selectTestSuite(xmlu.getRoot().attributeValue("url"),
						Integer.valueOf(testsuite_m.get("applicationid").toString())).get(0).getId();
		List<Element> caseelement = xmlu.getElementList("Testcase");
		for (Element ce : caseelement) {
			TestCase c = new TestCase();
			c.setTestsuiteid(testsuiteid);
			c.setCreatetime(DataManager.getSysTime());
			c.setUpdatetime(DataManager.getSysTime());
			c.setName(ce.attributeValue("name"));
			c.setDescription(ce.attributeValue("description"));
			c.setType(ce.attributeValue("type"));
			caselist.add(c);
		}
		return caselist;
	}

	/**
	 * 设置env envid
	 * 
	 * @param env
	 * @throws Exception
	 */
	public void setEnv(String env) throws Exception {
		testsuite_m.put("env", env);
		int envid = new TestCaseWapper().selectEnvId(env);
		testsuite_m.put("envid", envid);
	}

	/**
	 * 设置applicationid，使用domain查询并设置
	 * 
	 * @param domain
	 *            站点名
	 * @throws Exception
	 */
	public void setDomain(String domain) throws Exception {
		testsuite_m.put("domain", domain);
		Integer applicationid = new TestCaseWapper().selectAppIdByDomain(this.getDomain());
		if (applicationid <= 0) {
			throw new Exception(this.getDomain() + " applicationid not found !");
		}
		testsuite_m.put("applicationid", applicationid);
	}

	public String getDomain() {
		return testsuite_m.get("domain").toString();
	}

	public Integer getApplicationid() {
		return (Integer) testsuite_m.get("applicationid");
	}

	public String getEnv() {
		return testsuite_m.get("env").toString();
	}

	public Integer getEnvId() {
		return (Integer) testsuite_m.get("envid");
	}
	
	public  void setPath(String path) {
		this.path=path;
	}
	
	public String getPath() {
		return this.path;
	}

	/**
	 * 解析所有case详细内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ResultContent> getCaseResult(int passid) throws Exception {
		List<ResultContent> allcases = new ArrayList<ResultContent>();
		ArrayList<String> list = DataManager.getFilelist(this.getPath()+ File.separator
				+ testsuite_m.get("domain").toString());
		for (String filepath : list) {
			if(filepath.substring(filepath.length()-4).equals(".xml")){
				allcases.addAll(parserTestCaseContent(filepath, passid));
			}
		}
		return allcases;
	}

	/**
	 * 解析每个文件中的case详细内容
	 * 
	 * @param filepath
	 * @param passid
	 * @return
	 * @throws Exception
	 */
	public List<ResultContent> parserTestCaseContent(String filepath, int passid) throws Exception {
		List<ResultContent> casercs = new ArrayList<ResultContent>();
		TestCaseWapper ttcw = new TestCaseWapper();
		XmlUtil xmlu;
		try {
			if(filepath.substring(filepath.length()-4).equals(".xml")){
				xmlu = new XmlUtil(filepath);
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new Exception("解析xml文件异常：" + filepath + ":" + e.getMessage());
		}
		// 通过url
		// applicationid查询TestSuiteId，插入TestSuiteResult后返回TestSuiteResultId
		TestSuiteResult tsrb = new TestSuiteResult();
		tsrb.setTestpassid(passid);
		Integer testsuiteid = null;
		try {
			testsuiteid = new TestCaseWapper()
					.selectTestSuite(xmlu.getRoot().attributeValue("url"),
							Integer.valueOf(testsuite_m.get("applicationid").toString())).get(0).getId();
		} catch (Exception e) {
			Logger.debug("testsuiteid not found!url:" + xmlu.getRoot().attributeValue("url") + ":applicationid:"
					+ testsuite_m.get("applicationid").toString());
		}
		tsrb.setTestsuiteid(testsuiteid);
		//先查询是否存在记录
		int tsrid = ttcw.selectTestSuiteResultId(tsrb.getTestpassid(),tsrb.getTestsuiteid());
		if (tsrid==0){
			Logger.debug("没有记录，重新新生成!");
			//没有记录，重新查询新生成
			tsrid = ttcw.insertTestSuiteResult(tsrb);			
		}
		List<Element> caseelement = xmlu.getElementList("Testcase");
		for (Element ce : caseelement) {
			// 每个case先去查询testcaseid,插入TestCaseResult后返回TestCaseResultId
			String casename = ce.attributeValue("name");
			String status = ce.attributeValue("result");
			Integer testcaseid = null;
			try {
				testcaseid = ttcw.selectTestcase(casename, testsuiteid).get(0).getId();
			} catch (Exception e) {
				Logger.debug("testcaseid not found!" + casename + ":testsuiteid:" + testsuiteid);
			}
			TestCaseResult tcrb = new TestCaseResult();
			tcrb.setStatus(status);
			tcrb.setTestcaseid(testcaseid);
			tcrb.setTestsuiteresultid(tsrid);
			int testcaseresultid = ttcw.insertTestCaseResult(tcrb);
			// 解析case节点下所有内容并记录到ResultContent
			List<Element> eles = ce.elements();
			for (Element element2 : eles) {
				ResultContent rc = new ResultContent();
				rc.setCreatetime(element2.attributeValue("timestamp").trim());
				String content = element2.getData().toString();
				// 内容最多记录2000
				if (content.length() > 2000) {
					content = content.substring(0, 2000)+" ......";
				}
				rc.setContent(content);
				rc.setStatus(element2.attributeValue("result"));
				rc.setType(element2.getName());
				rc.setTestcaseresultid(testcaseresultid);
				casercs.add(rc);
			}
		}
		return casercs;
	}
	
	/**
	 * 解析doman下所有xml 文件到数据库
	 * 
	 * @param TestCaseWapper 
	 * @param env
	 * @param domain
	 * @return  
	 * @throws Exception
	 */
	public int parserXmlToDb(TestCaseWapper tcw,String env,String domain) throws Exception{
		Logger.debug("程序初始化===");
		this.setDomain(domain);
		this.setEnv(env);
		Logger.debug("开始创建testpass===");
		// 插入testpass 获取passid
		TestPass pb = new TestPass();
		pb.setEnvid(this.getEnvId());
		pb.setApplicationid(this.getApplicationid());
		String createtime = DataManager.getSysTime("yyyy-MM-dd HH:mm:ss");
		pb.setCreatetime(createtime);
		int passid = tcw.insertTestPass(pb);
		Logger.debug("passid+++++++++:"+passid);
		Logger.debug("开始维护case===");
		// 读取文件夹Results 文件 获取testsuite info
		Map<String, Object> tsi = this.getTestSuiteInfo();
		ArrayList<TestSuite> list = (ArrayList<TestSuite>) tsi.get("testsuites");
		// 解析的过程中逐步插入
		for (TestSuite ts : list) {
			Logger.debug(FormatUtil.beanToGSONString(ts));
			tcw.insertTestsuite(ts);
		}
		// 读取文件夹Results 文件 获取testcase info
		List<TestCase> caselist = this.getTestCaseInfo();
		// 解析的过程中逐步插入
		for (TestCase tc : caselist) {
			Logger.debug(FormatUtil.beanToGSONString(tc));
			tcw.insertTestcase(tc);
		}
		Logger.debug("case维护完毕 开始获取result集合===");		
		parserTestContentsToDb(tcw,passid);
		
		return passid;
	}
	
	/**
	 * 解析接口下用例case详细内容并入库
	 * 
	 * @param tcw
	 * @param passid 
	 * @return
	 * @throws Exception
	 */
	public void parserTestContentsToDb(TestCaseWapper tcw,int passid)  throws Exception{
		List<ResultContent> rclist = getCaseResult(passid);
		Logger.debug("开始插入result===");
		try {
			// 取消自动提交使用commit模式，commit失败时rollback
			tcw.getDb().setAutoCommit(false);
			for (ResultContent rc : rclist) {
				Logger.debug(FormatUtil.beanToGSONString(rc));
				tcw.insertResultContent(rc);
			}
			tcw.getDb().commit();
			tcw.getDb().setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			tcw.getDb().rollback();
		} finally {
			// 此处待确认 目前Savepoint不生效直接使用commit rollback模式
			tcw.getDb().releaseSavepoint();
		}	
	}
	
	public static void main(String args[]) throws Exception {
		// args 传入测试环境 和 站点 SIT1 api.accounting.i.ymatou.com
		Logger.debug("程序初始化===");
		TestCaseWapper tcw = new TestCaseWapper();
		String env = "SIT1";
		String domain ="operate.trading.iapi.ymatou.com" ;
		ResultParser resultParser=new ResultParser();
		String path="E:\\workspace\\source\\YmatouTest\\Results";
		resultParser.setPath(path);
		resultParser.parserXmlToDb(tcw, env, domain);
		
	}

}
