package com.ymatou.autorun.datadriver.base.autoresult.database.Wapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ymatou.autorun.datadriver.base.utils.DBUtil;
import com.ymatou.autorun.datadriver.base.utils.DataManager;
import com.ymatou.autorun.datadriver.base.utils.Logger;
import com.ymatou.autorun.datadriver.base.utils.RefBean;



public class SyncSiteListWapper {
	private DBUtil db;

	public SyncSiteListWapper() {
		db = new DBUtil();
	}

	public DBUtil getDb() {
		return db;
	}

	/**
	 * 查询testsuite
	 * 
	 * @param url
	 * @param applicationid
	 * @return List<TestSuite>
	 * @throws SQLException
	 */
	public List<Map> selectSiteListInfo() throws SQLException {
		String sql = "select aenv.id as id,a.domain as domain,vf.ip as ip,Env.name as envname from ApplicationEnv aenv  left join  Application a on a.id=aenv.applicationid left join Env on Env.id=aenv.envid left join  VmInfo vf on vf.id=aenv.vminfoid where a.departmentid!=4 and a.del!=1";
		ResultSet res = db.select(sql, null);
		List list = new RefBean().getInstancesMap(res);
		return list;
	}

	public List<Map> selectIpById(String ip) throws SQLException {
		List<String> value = new ArrayList<String>();
		value.add(ip);
		String sql = "select * from VmInfo where ip=?";
		ResultSet res = db.select(sql, value);
		List list = new RefBean().getInstancesMap(res);
		return list;
	}
	public List<Map> selectApplicationIdByDomain(String domain) throws SQLException {
		List<String> value = new ArrayList<String>();
		value.add(domain);
		String sql = "select * from Application where Domain=?";
		ResultSet res = db.select(sql, value);
		List list = new RefBean().getInstancesMap(res);
		return list;
	}
	public void updateSiteListInfo(String id, String ip) throws Exception {
		List<Map> list = selectIpById(ip);
		String vminfoid = "";
		if (list.size() == 1) {
//			Logger.debug(list.get(0));
			vminfoid = list.get(0).get("Id").toString();
			Logger.debug("id:"+id+":vminfoid:"+vminfoid+":ip:"+ip);
			List<String> value = new ArrayList<String>();
			value.add(vminfoid);
			value.add(id);
			String updatesql = "update ApplicationEnv set vminfoid=? where id=?";
			db.update(updatesql, value);
		} else {
			Logger.debug("没有找到合适的ip:" + ip);
			if (list.size() <= 0) {
				DataManager.appendFileToLien("./noip.txt", "vminfo缺少ip:" + ip);
			} else {
				DataManager.appendFileToLien("./noip.txt", "vminfo有多个ip:" + ip);
			}
		}
		// db.commit();
	}

}
