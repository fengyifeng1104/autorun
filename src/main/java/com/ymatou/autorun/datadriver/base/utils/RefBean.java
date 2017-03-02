package com.ymatou.autorun.datadriver.base.utils;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.net.www.protocol.mailto.Handler;

/************************************************************************************
 * @File name :RefBean.java
 * @Author : zhouyi
 * @Date : 2016年2月29日
 * @Copyright : 洋码头
 ************************************************************************************/
public class RefBean<T> {

	public List<T> getInstances(ResultSet rs,Class<T> cls){
		List<T> lists = new ArrayList<T>();
		T bean = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			while(rs.next()){
				bean = cls.newInstance();
				for (int i = 0; i < cols; i++) {
					String elename = rsmd.getColumnName(i+1);
					elename=elename.substring(0, 1).toUpperCase()+elename.substring(1);
					Method getele = cls.getMethod("get"+elename);
					Method setele = cls.getMethod("set" +elename,getele.getReturnType());
					Object value = rs.getObject(i+1);
					setele.invoke(bean, value);
				}
				lists.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists;
	}
	public List<Map<String, Object>> getInstancesMap(ResultSet rs){
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			while(rs.next()){
				Map map = new HashMap<String, Object>();
				for (int i = 0; i < cols; i++) {
					String elename = rsmd.getColumnName(i+1);
					elename=elename.substring(0, 1).toUpperCase()+elename.substring(1);
					Object value = rs.getObject(i+1);
					map.put(elename, value);
				}
				lists.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists;
	}
}