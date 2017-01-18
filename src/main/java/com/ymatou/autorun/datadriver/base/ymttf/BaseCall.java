package com.ymatou.autorun.datadriver.base.ymttf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ymatou.autorun.datadriver.base.ymttf.tool.FormatUtil;
import com.ymatou.autorun.datadriver.base.ymttf.tool.Logger;


/************************************************************************************
 * 基础Call类，实现HttpClient发送请求基础功能
 * 
 * @File name : BaseCall.java
 * @Author : zhouyi
 * @Date : 2015年3月10日
 * @Copyright : 洋码头
 ************************************************************************************/
public class BaseCall {

	protected CallClient client = null;

	/**
	 * @author zhouyi 新增用于记录cookie 的变量 cookie
	 */
	protected String cookie = "";

	protected String url = null;
	protected BaseBean basebean;
	/**
	 * bean要转换成的格式，目前只有PARAM和JSON PARAM转换成key=value&...形式，JSON转换成JSON格式
	 */
	protected String beanFormat = "PARAM";

	protected String entityString;

	protected String lastentityString;

	protected String mode;

	protected String returndata;

	/**
	 * 初始化接口
	 * 
	 * @param systemUrl
	 *            例如http://api.alpha.ymatou.com/ 则设置system为"appapi"来获取到baseurl
	 * @param opurl
	 *            相对路径 <br>
	 *            例如 api/User/LoginAuth <br>
	 *            最终url为baseurl+opurl <br>
	 *            示例中最终的url为http://api.alpha.ymatou.com/api/User/LoginAuth
	 * @param Mode
	 *            GET or POST or FILE
	 * @param beanFormat
	 *            格式 <br>
	 *            bean要转换成的格式，目前只有PARAM和JSON <br>
	 *            PARAM转换成key=value&...形式，JSON转换成JSON格式
	 */
	public BaseCall(String systemUrl, String opurl, String Mode, String beanFormat) {
		this.beanFormat = beanFormat;
		this.mode = Mode;
		// 每个系统这里URL都不一样 配置文件全都deploy.properties
		this.url = systemUrl + opurl;
		if (client == null) {
			client = new CallClient(url, Mode);
		}

	}

	/**
	 * 设置参数，用于BaseBean转换成JSONString的Post请求
	 * 
	 * @param bb
	 *            basebean
	 */
	public void setData(BaseBean bb) {
		this.basebean = bb;

	}

	/**
	 * 设置参数 请求String
	 * 
	 * @param s
	 *            String
	 */
	public void setData(String s) {
		this.entityString = s;

	}

	/**
	 * 发送请求
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void callService() throws ClientProtocolException, IOException {
		addDefHeader();
		if (entityString != null) {
		} else if (beanFormat.equals("PARAM")) {
			entityString = FormatUtil.beanToHttpString(basebean);
		} else if (beanFormat.equals("JSON")) {
			entityString = FormatUtil.beanToGSONString(basebean);
		} else if (beanFormat.equals("STRING")) {
			// nothing entityString使用 setData(String s) 直接赋值
		}
		client.setEntity(entityString);
		Logger.comment("发送请求url:" + this.url);
		Logger.comment("请求报文:" + entityString);
		client.sendRequest();
		lastentityString = entityString;
		entityString = null;
		returndata = client.getResponseString();
		Logger.comment("返回报文" + returndata);
		Logger.comment("请求耗时" + client.getDuration());
	}

	/**
	 * 发送请求
	 * 
	 * @param isAutoEncoding
	 *            参数是否做urlEncode操作，false不处理
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void callService(boolean isAutoEncoding) throws ClientProtocolException, IOException {
		addDefHeader();
		if (entityString != null) {
		} else if (beanFormat.equals("PARAM")) {
			entityString = FormatUtil.beanToHttpString(basebean, isAutoEncoding);
		} else if (beanFormat.equals("JSON")) {
			entityString = FormatUtil.beanToGSONString(basebean);
		} else if (beanFormat.equals("STRING")) {
			// nothing entityString使用 setData(String s) 直接赋值
		}
		client.setEntity(entityString);
		Logger.comment("发送请求url:" + this.url);
		Logger.comment("请求报文:" + entityString);
		client.sendRequest();
		lastentityString = entityString;
		entityString = null;
		returndata = client.getResponseString();
		Logger.comment("返回报文" + returndata);
		Logger.comment("请求耗时" + client.getDuration());
	}

	/**
	 * 获取返回报文
	 * 
	 * @return 返回报文
	 */
	public String getReturnData() {
		return returndata;
	}

	/**
	 * 获取请求消耗时间
	 * 
	 * @return 时间 秒
	 */
	public double getResponseTime() {
		return client.getDuration();
	}

	/**
	 * 获取http请求状态码
	 * 
	 * @return http请求状态码
	 */
	public int getStatusCode() {
		return client.getStatusCode();
	}

	/**
	 * 用于登陆后获取headcookie
	 * 
	 * @author zhouyi
	 * @return cookie
	 */
	public List<String> getCookies() {
		return client.getCookies();
	}

	/**
	 * 用于设置cookie
	 * 
	 * @author zhouyi
	 * @param cookie
	 *            登陆后获取到的cookie
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;

	}

	/**
	 * 用于使用List<String>设置cookie,该值可从getCookies获取
	 * 
	 * @author zhouyi
	 * @param cookies
	 *            list格式的一串数据
	 * 
	 */
	public void setCookies(List<String> cookies) {
		for (String s : cookies) {
			this.cookie += s + ";";
		}

	}

	/**
	 * 添加默认header信息
	 */
	public void addDefHeader() {
		client.addHeader("Accept", "text/xml,text/javascript,text/html,application/json");
		// 每个系统默认头信息都不同
		// client.addHeader("Content-Type",
		// "application/x-www-form-urlencoded;charset=UTF-8;text/html");
		/**
		 * @author zhouyi 在Cookie中添加headsession
		 */
		client.addHeader("Cookie", cookie);

	}

	/**
	 * addHeader 添加header
	 * 
	 * @param key
	 *            header key
	 * @param value
	 *            header value
	 */
	public void addHeader(String key, String value) {
		client.addHeader(key, value);
	}

	/**
	 * 获取发送报文
	 * 
	 * @return 发送报文内容
	 */
	public String getEntityString() {
		return lastentityString;
	}

	/**
	 * 获取返回格式为Json报文格式的对象中key对应的value
	 * 
	 * @param key
	 *            JsonKey
	 * @return String value
	 */
	public String getString(String key) {
		String value = null;
		try {
			JSONObject jo = JSONObject.parseObject(getReturnData());
			value = jo.get(key).toString();
		} catch (Exception e) {
			Logger.debug("获取"+key+"错误:"+e.getMessage());
		}
		return value;
	}


	/**
	 * 如果返回结果是json格式可以使用此方式返回一个Map结构的返回
	 * @return 返回一个map
	 */
	public Map getMap() {
		return new Gson().fromJson(getReturnData(), Map.class);
	}
	/**
	 * 返回自定义类型结果
	 * @param t
	 * @return
	 */
	public  Object getDataForType(Type t) {
		return new Gson().fromJson(getReturnData(), t);
	}
	/**
	 * 返回json object
	 * @return
	 */
	public  JsonObject getJsonObject() {
		JsonObject j=new Gson().fromJson(getReturnData(), JsonObject.class);
		return j;
	}
	/**
	 * 设置url 调用这个方法可以强制设置Call的URL
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
		client = new CallClient(url, mode);
	}

	/**
	 * 添加文件，用于文件上传
	 * 
	 * @param name
	 *            参数名
	 * @param fliename
	 *            文件全路径
	 */
	public void addPart(String name, String fliename) {
		client.addPart(name, fliename);
	}

	/**
	 * @see CallClient#setConnectionTimeout(int)
	 */
	public void setConnectionTimeout(int timeout) {
		client.setConnectionTimeout(timeout);
	}

	/**
	 * @see CallClient#setSoTimeout(int)
	 */
	public void setSoTimeout(int timeout) {
		client.setSoTimeout(timeout);
	}

	/**
	 * @see CallClient#removeAllHeader()
	 */
	public void removeAllHeader() {
		client.removeAllHeader();
	}

	/**
	 * @see CallClient#closc
	 */
	public void close() {
		client.close();
	}
	/**
	 * 设置是否自动重定向
	 * @param isRedirects
	 */
	public void setRedirects(boolean isRedirects) {
		client.setRedirects(isRedirects);
	}

	public String getMode() {
		return mode;
	}
	
}
