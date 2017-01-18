package com.ymatou.autorun.datadriver.base.ymttf.tool;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ymatou.autorun.datadriver.base.ymttf.BaseBean;


/************************************************************************************
 * 用户格式化常用数据格式
 * 目前支持JSON和bean的互相转换以及bean转换成get/post通用表单String(key1=value1&key2=value2) TODO
 * 后期有空改造 全部使用Gson的方式实现现有功能？
 * 
 * @File name : FormatUtil.java
 * @Author : zhouyi
 * @Date : 2015年3月11日
 * @Copyright : 洋码头
 ************************************************************************************/
@SuppressWarnings("rawtypes")
public class FormatUtil {

	public static String beanToJSONString(BaseBean bean) {
		return JSONObject.toJSONString(bean);
	}

	/**
	 * List转换成JSONArray String
	 * 
	 * @param orb
	 * @return json串
	 */

	public static String listToJSONString(List list) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").disableHtmlEscaping().create();
		return gson.toJson(list).toString();
	}

	/**
	 * json转化成beanClass, 建议使用GSONToObject
	 * 
	 * @deprecated
	 * @param json
	 * @param beanClass
	 * @return beanObject
	 */
	public static Object JSONToObject(String json, Class beanClass) {
		return JSONObject.parseObject(json, beanClass);
	}

	/**
	 * json String 转化成net.sf.json.JSONObject
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject JSONToJSONObject(String json) {
		JSONObject jo = JSON.parseObject(json);
		return jo;
	}

	/**
	 * json转化成beanClass by gson
	 * 
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public static Object GSONToObject(String json, Class beanClass) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").disableHtmlEscaping().create();
		Object gs = gson.fromJson(json, beanClass);
		return gs;
	}
	/**
	 * json转化成beanClass by gson
	 * 
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public static Object GSONToObject(String json, Type type) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").disableHtmlEscaping().create();
		Object gs =gson.fromJson(json, type);
		return gs;
	}
	/**
	 * bean转换成json String by gson
	 * 
	 * @param bean
	 * @return String
	 */
	public static String beanToGSONString(Object bean) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").disableHtmlEscaping().create();
		return gson.toJson(bean).toString();
	}



	/**
	 * 把bean转换成key1=value1&key2=value2的String形式
	 * 
	 * @param bb
	 *            basebean
	 * @return 请求串
	 */
	public static String beanToHttpString(BaseBean bb) {
		String p = "";
		BeanInfo beanInfo = null;
		try {
			if (!(bb == null)) {
				beanInfo = Introspector.getBeanInfo(bb.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					String propertyName = pds[i].getName();
					if (!propertyName.equals("class")) {
						Method method = pds[i].getReadMethod();
						Object value=null;
						try{
							value = method.invoke(bb);
						}catch(NullPointerException e){
							//null时不拼接字符串，继续执行程序
						}
						if (null != value) {
							p += propertyName + "=" + urlEncode(value.toString()) + "&";
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (p.length() > 0) {
			p = p.substring(0, p.length() - 1);
		}
		return p;
	}

	/**
	 * 把bean转换成<xml><key><![CDATA[value]]></key></xml>的String形式
	 * 
	 * @param bb
	 *            basebean
	 * @return 请求串
	 */
	public static String beanToXmlString(BaseBean bb) {
		String p = "<xml>";
		BeanInfo beanInfo = null;
		try {
			if (!(bb == null)) {
				beanInfo = Introspector.getBeanInfo(bb.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					String propertyName = pds[i].getName();
					if (!propertyName.equals("class")) {
						Method method = pds[i].getReadMethod();
						Object value = method.invoke(bb);
						if (null != value) {
							p += "<" + propertyName + ">" + "<![CDATA[" + value.toString() + "]]></" + propertyName + ">";
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		p += "</xml>";
		return p;
	}

	/**
	 * 把bean转换成<key>value/key>的String形式
	 * 
	 * @param bb
	 *            basebean
	 * @return 请求串
	 */
	public static String beanToNoRootXmlString(BaseBean bb) {
		String p = "";
		BeanInfo beanInfo = null;
		try {
			if (!(bb == null)) {
				beanInfo = Introspector.getBeanInfo(bb.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					String propertyName = pds[i].getName();
					if (!propertyName.equals("class")) {
						Method method = pds[i].getReadMethod();
						Object value = method.invoke(bb);
						if (null != value) {
							p += "<" + propertyName + ">" + value.toString() + "</" + propertyName + ">";
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * 把bean转换成key1=value1&key2=value2的String形式
	 * 
	 * @param bb
	 *            basebean
	 * @return 请求串
	 */
	public static String beanToHttpString(BaseBean bb, boolean isAutoEncoding) {
		String p = "";
		BeanInfo beanInfo = null;
		try {
			if (!(bb == null)) {
				beanInfo = Introspector.getBeanInfo(bb.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					String propertyName = pds[i].getName();
					if (!propertyName.equals("class")) {
						Method method = pds[i].getReadMethod();
						Object value = method.invoke(bb);
						if (null != value) {
							if (isAutoEncoding) {
								p += propertyName + "=" + urlEncode(value.toString()) + "&";
							} else {
								p += propertyName + "=" + value.toString() + "&";
							}
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (p.length() > 0) {
			p = p.substring(0, p.length() - 1);
		}
		return p;
	}

	/**
	 * 把map转换成key1=value1&key2=value2的String形式
	 * 
	 * @param map
	 * @return 请求串
	 */
	public static String mapToHttpString(Map m, boolean isAutoEncoding) {
		String p = "";
		try {
			if (!(m == null)) {
				Iterator i = m.entrySet().iterator();
				while (i.hasNext()) {
					String obj = i.next().toString();
					int index = obj.indexOf("=");
					String propertyName = obj.substring(0, index);
					String value = obj.substring(index + 1, obj.length());
					if (isAutoEncoding) {
						p += propertyName + "=" + urlDecode(value.toString()) + "&";
					} else {
						p += propertyName + "=" + value.toString() + "&";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (p.length() > 0) {
			p = p.substring(0, p.length() - 1);
		}
		return p;
	}

	/**
	 * 把bean转换成key1=value1&key2=value2的String形式
	 * 
	 * @param bb
	 *            basebean
	 * @return 请求串
	 */
	public static String beanToHttpString(BaseBean bb, boolean isAutoEncoding, boolean isAutoLowerCase) {
		String p = "";
		BeanInfo beanInfo = null;
		try {
			if (isAutoLowerCase) {
				if (!(bb == null)) {
					beanInfo = Introspector.getBeanInfo(bb.getClass());
					PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
					for (int i = 0; i < pds.length; i++) {
						String propertyName = pds[i].getName();
						if (!propertyName.equals("class")) {
							Method method = pds[i].getReadMethod();
							Object value = method.invoke(bb);
							if (null != value) {
								if (isAutoEncoding) {
									p += propertyName + "=" + urlEncode(value.toString()) + "&";
								} else {
									p += propertyName + "=" + value.toString() + "&";
								}
							}
						}
					}
				}
			} else {
				Map<String,String> gson = (Map<String,String>) FormatUtil.GSONToObject(FormatUtil.beanToGSONString(bb),new TypeToken<Map<String, String>>() {}.getType());
				return FormatUtil.mapToHttpString(gson, isAutoEncoding);
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (p.length() > 0) {
			p = p.substring(0, p.length() - 1);
		}
		return p;
	}

	/**
	 * 转换string 为url编码
	 * 
	 * @param s
	 *            原始String
	 * @return URLEncoder转换后的String
	 */
	@SuppressWarnings("deprecation")
	public static String urlEncode(String s) {
		String enc = "";
		try {
			enc = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return enc;

	}

	/**
	 * 转换 url编码 string 为原始内容
	 * 
	 * @param s
	 *            url编码String
	 * @return Decoder后的正常String
	 */
	@SuppressWarnings("deprecation")
	public static String urlDecode(String s) {
		String dec = "";
		try {
			dec = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dec;

	}

	/**
	 * 把key=value&转换成map
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	public static Map httpStringToMap(String http) throws Exception {
		http = "&" + http + "&";
		Map m = new HashMap<String, String>();
		List<String> namelist = getRegexList(http, "(?<=&).*?(?==)");
		List<String> valuelist = getRegexList(http, "(?<==).*?(?=&)");
		if (valuelist.size() == namelist.size()) {
			for (int i = 0; i < namelist.size(); i++) {
				String value = valuelist.get(i).trim();
				String name = namelist.get(i);
				if (value.indexOf("\"") == 0 && value.lastIndexOf("\"") == value.length() - 1) {
					m.put(name, value.substring(1, value.length() - 1));
				} else {
					m.put(name, value);
				}
			}
		} else {
			throw new Exception("转换失败，结果集name value 不匹配");
		}
		return m;
	}

	/**
	 * 获取匹配正则表达式结果的结果列
	 * 
	 * @param rts
	 *            源数据
	 * @param regEx
	 *            正则表达式
	 * @return 结果数组
	 */
	private static List<String> getRegexList(String rts, String regEx) {
		List<String> finds = new ArrayList<String>();
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(rts);
		while (m.find()) {
			finds.add(m.group());
		}
		return finds;
	}

	public static void main(String args[]) {
		

	}
}
