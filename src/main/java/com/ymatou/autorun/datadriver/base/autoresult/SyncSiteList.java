package com.ymatou.autorun.datadriver.base.autoresult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ymatou.autorun.datadriver.base.utils.DataManager;
import com.ymatou.autorun.datadriver.base.utils.Logger;

public class SyncSiteList {
	static TreeSet<String> sitelist=new TreeSet<String>();
	/**
	 * @param list
	 * @return Envname_Domain:Ip_ApplicationEnv.Id
	 * @throws SQLException
	 */
	public Map getSqlSiteMap(List<Map> list) throws SQLException {
		Map m = new HashMap<String, Object>();
		for (Map map : list) {
			if (m.containsKey( map.get("Domain")+ "_" +map.get("Envname") )) {
				Logger.debug("数据库中的数据重复:"  + map.get("Domain")+ "_"+map.get("Envname") );
			} else {
				m.put( map.get("Domain")+ "_" +map.get("Envname"), map.get("Ip") + "_" + map.get("Id"));
				sitelist.add(( map.get("Domain")+ "_" +map.get("Envname")));
			}
		}
		return m;
	}

	public Map getFileSiteMap(String file) throws SQLException {
		ArrayList<String> list = DataManager.getData(file);
		Map m = new HashMap<String, Object>();
		for (String s : list) {
			if (s.trim().length() < 5 || s.substring(0, 1).equals("=")) {
				continue;
			}
			//替换sitelist中数据为标准格式 空格分割
			s=s.replaceAll("  ", " ");
			s=s.replaceAll("	", " ");
			String[] ss = s.split(" ");
			if(ss.length == 3){
				m.put(ss[1]+ "_" +ss[0]  , ss[2]);
				sitelist.add(ss[1]+ "_" +ss[0]);
			}
			else {
				Logger.debug("数据解析错误:" + s);
			}
		}
		return m;
	}

	
}
