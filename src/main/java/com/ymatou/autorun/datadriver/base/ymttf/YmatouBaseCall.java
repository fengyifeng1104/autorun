package com.ymatou.autorun.datadriver.base.ymttf;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.autorun.datadriver.base.utils.FormatUtil;
import com.ymatou.autorun.datadriver.base.utils.Logger;


/************************************************************************************
 * @File name : YmatouBaseCall.java
 * @Author : zhouyi
 * @Date : 2015年3月31日
 * @Copyright : 洋码头
 ************************************************************************************/
public class YmatouBaseCall extends BaseCall {
	protected String opurl;


	/**
	 * 重载方法，用于处理systemUrl不是默认地址时的call
	 * 
	 * @see com.ymt.base.BaseCall#BaseCall(String, String, String, String)
	 */
	public YmatouBaseCall(String systemUrl, String opurl, String Mode, String beanFormat) {
		super(systemUrl, opurl, Mode, beanFormat);
		if(opurl!=null&&opurl.length()>0){
			this.setOpurl(opurl);
		}
	}

	/**
	 * 添加默认header信息
	 */
	@Override
	public void addDefHeader() {
		super.addDefHeader();
		client.addHeader("Accept", "text/xml,text/javascript,text/html,application/json");
		client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;text/html");
	}

	/**
	 * 用于登陆后获取headcookie 这里做一下排除重复list内容的操作
	 * 
	 * @author zhouyi
	 * @return cookie
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCookies() {
		return removeDuplicateWithOrder(client.getCookies());
	}

	/**
	 * @see  com.ymt.core.client.CallClient#getAllHeader()
	 */
	public List<String> getAllHeader() {
		return client.getAllHeader();
	}

	public void setOpurl(String opurl) {
		this.opurl = opurl;
	}

	public String getOpurl() {
		return opurl;
	}
	public String getUrl() {
		return url;
	}
	/** 重写setData方法，将bean的数据写到结果文件里
	 * @see com.ymt.base.BaseCall#setData(com.ymt.base.BaseBean)
	 */
	@Override
	public void setData(BaseBean bb) {
		this.basebean = bb;
		try {
			Logger.comment("设置参数开始:"+this.getUrl());
			Class c = bb.getClass();
			Field[] fs = c.getDeclaredFields();
			for (Field field : fs) {
				field.setAccessible(true);
				// 接口list 引用类型不写到结果文件里
				if(field.getType() == String[].class){
					Logger.comment("设置数组参数:"+field.getName() + ":" + Arrays.toString((String[])field.get(bb)));
				}else if(field.getType() == List.class){
					String jsonlist=FormatUtil.listToJSONString((List) field.get(bb));
					Logger.comment("设置List参数:"+field.getName() + ":" + jsonlist);
				}else{
					Logger.comment("设置参数:"+field.getName() + ":" + field.get(bb));
				}
				field.setAccessible(false);
			}
		} catch (Exception e) {
			Logger.fail(e);
		}finally{
			Logger.comment("设置参数结束");
		}
	}
	
/*	重载setData 方法.
 * true  将bean数据写到结果文件
	*/
	public void setData(boolean b,BaseBean bb) {
		if (!b){
			this.basebean = bb;
		}	
		else{
			this.setData(bb);
		}			
	}
	public void setData(JSONObject bb) {
			this.setData(bb.toString());
	}
	
	/**
	 * list 去除重复值
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		return newList;
	}
}
