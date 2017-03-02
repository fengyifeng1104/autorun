package com.ymatou.autorun.datadriver.base.ymttf;

import com.ymatou.autorun.datadriver.base.utils.FormatUtil;

/************************************************************************************
 * @File name :GetFileCall.java
 * @Author : zhouyi
 * @Date : 2015年6月29日
 * @Copyright : 洋码头
 ************************************************************************************/
public class GetFileCall extends YmatouBaseCall {
	private String file = "";

	public GetFileCall(String url) {
		super(url, "", "GET", "PARAM");
	}

	/**
	 * 添加默认header信息
	 */
	@Override
	public void addDefHeader() {
		super.addDefHeader();
		client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;text/html");
	}

	/* (non-Javadoc)
	 * @see com.ymt.base.BaseCall#callService()
	 */
	@Override
	public void callService() {
		addDefHeader();
		if (beanFormat.equals("PARAM")) {
			entityString = FormatUtil.beanToHttpString(basebean);
		} else if (beanFormat.equals("JSON")) {
			entityString = FormatUtil.beanToJSONString(basebean);
		}
		client.setEntity(entityString);
		file = client.sendRequestAndWriteResponseToFile();
	}

	/**
	 * 获取返回资源的文件路径
	 * 
	 * @return 文件路径
	 */
	public String getFile() {
		return file;
	}

	
}
