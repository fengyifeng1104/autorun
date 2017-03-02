package com.ymatou.autorun.datadriver.base.autoresult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ymatou.autorun.datadriver.base.autoresult.database.Wapper.SyncSiteListWapper;
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

	public static void main(String args[]) throws Exception {
		SyncSiteListWapper synw = new SyncSiteListWapper();
		SyncSiteList syn = new SyncSiteList();
		List<Map> sitelistinfo=synw.selectSiteListInfo();
		TreeMap sitemap =new TreeMap(syn.getSqlSiteMap(sitelistinfo));
		Logger.debug(sitemap);
		TreeMap fsitemap = new TreeMap(syn.getFileSiteMap("./siteList.txt"));
		Logger.debug(fsitemap);
		for (String key:sitelist) {
			if(!sitemap.containsKey(key)){
				Logger.debug("数据库缺少:"+key);
				String applicationid=null;
				try{
					applicationid=synw.selectApplicationIdByDomain(key.split("_")[0]).get(0).get("Id").toString();
				}catch(Exception e){
					DataManager.appendFileToLien("./db.txt","没找到应用,记录失败:"+key);
					continue;
				}
//				DataManager.appendFileToLien("./db.txt", "数据库缺少:"+key+":ip:"+fsitemap.get(key).toString());
				String env=null;
				if(key.split("_")[1].equals("SIT1")){
					env="1";
				}else if(key.split("_")[1].equals("UAT")){
					env="3";
				}else if(key.split("_")[1].equals("STRESS")){
					env="4";
				}else{
					env=key.split("_")[1];
				}
				
				DataManager.appendFileToLien("./db.txt",key+":{\"appid\":\""+applicationid+"\",\"envid\":\""+env+"\"}");
			}else if(!fsitemap.containsKey(key)){
				Logger.debug("sitelist文件缺少:"+key);
				DataManager.appendFileToLien("./stlist.txt", "sitelist文件缺少:"+key);
			}else{
				String id=sitemap.get(key).toString().split("_")[1];
				String ip=sitemap.get(key).toString().split("_")[0];
				String newip=fsitemap.get(key).toString();
				if(ip.equals(newip)){
					Logger.debug("key:"+key+"数据一致不用更新");
				}
				else{
					Logger.debug("key:"+key+":dbstie"+sitemap.get(key)+":文件site:"+fsitemap.get(key));
					synw.updateSiteListInfo(sitemap.get(key).toString().split("_")[1], fsitemap.get(key).toString());
					DataManager.appendFileToLien("./update.txt", "更新记录:"+key+"原来:"+sitemap.get(key)+"现在:"+fsitemap.get(key));
				}
			}
		}
	}
}
