package com.ymatou.autorun.datadriver.base.ymttf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.ymatou.autorun.datadriver.base.ymttf.tool.Logger;

import org.apache.http.params.HttpParams;


/************************************************************************************
 * CallClient基础类，封装HttpClient/request/response
 * 
 * @File name : CallClient.java
 * @Author : zhouyi
 * @Date : 2015年3月17日
 * @Copyright : 洋码头
 ************************************************************************************/
@SuppressWarnings("deprecation")
public class CallClient {
	/**
	 * DefaultHttpClient http请求均走此处
	 */
	private DefaultHttpClient client = null;

	/**
	 * 默认POST请求
	 */
	private String mode = "POST";

	/**
	 * 用于DefaultHttpClient execute
	 */
	private HttpRequestBase req = null;

	/**
	 * 请求发送后返回数据
	 */
	private String responseString = null;

	/**
	 * 请求消耗时间，单位秒
	 */
	private double duration;

	/**
	 * response
	 */
	private CloseableHttpResponse resp;

	/**
	 * 请求开始时间 yyyyMMddhhmmssSSS格式
	 */
	private String requestStartTime;

	/**
	 * 请求结束时间 yyyyMMddhhmmssSSS格式
	 */
	private String requestStopTime;

	/**
	 * 响应结束时间 yyyyMMddhhmmssSSS格式
	 */
	private String responseStopTime;
	
	private String iurl;
	/**
	 * 文件传输用的request
	 */
	MultipartEntity reqEntity = new MultipartEntity();
	private static ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
			new SchemeRegistryFactory().createDefault(), 0, TimeUnit.SECONDS);

	/**
	 * 初始化 CallClient
	 * 
	 * @param url
	 *            Requesturl地址
	 * @param Mode
	 *            GET or POST or FILE
	 */
	@SuppressWarnings({ "static-access" })
	public CallClient(String url, String Mode) {
		iurl=url;
		req = createRequest(url, Mode);
		client = new DefaultHttpClient(cm);

	}
	public void setRedirects(boolean isRedirects){
		HttpParams params = client.getParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, isRedirects);  
	}
	/**
	 * 使用url创建Request
	 * 
	 * @param url
	 *            Requesturl地址
	 * @param Mode
	 *            GET or POST or FILE
	 * @return HttpRequestBase
	 */
	private HttpRequestBase createRequest(String url, String Mode) {
		this.mode = Mode;
		HttpPost httppost = null;
		if (Mode.toUpperCase().equals("GET")) {
			HttpGet httpget = new HttpGet(url);
			return httpget;
		} else if (Mode.toUpperCase().equals("POST")) {
			httppost = new HttpPost(url);
			return httppost;
		} else if (Mode.toUpperCase().equals("FILE")) {
			httppost = new HttpPost(url);
			return httppost;
		}else if (Mode.toUpperCase().equals("DELETE")) {
			HttpDelete httpdelete=new HttpDelete(url);
			return httpdelete;
		} else {
			httppost = new HttpPost(url);
		}
		
		// 默认返回POST
		return httppost;
	}

	/**
	 * 添加Header
	 * 
	 * @param name
	 *            header的name
	 * @param value
	 *            header的value
	 * @throws UnsupportedEncodingException
	 */
	public void addHeader(String name, String value) {
		try {
			req.addHeader(name, new String(value.getBytes(), "UTF-8"));
		} catch (Exception e) {
			req.addHeader(name, value);
			Logger.debug("addHeader value to UTF8 Failed,not charge!");
		}
	}

	/**
	 * 设置内容
	 * 
	 * @param EntityString
	 *            报文
	 */
	public void setEntity(String entityString) {
		try {
			// post
			if (mode.equals("POST")) {
				StringEntity se = new StringEntity(entityString, HTTP.UTF_8);
				((HttpPost) req).setEntity(se);
			}
			// get 如果之前的url有?则直接拼接参数到url后;如果没有?则加上后拼接
			else if (mode.equals("GET")) {
				req.setURI(new URI(iurl));
				if (entityString.length() > 0) {
					if (req.getURI().toString().contains("?")) {
						req.setURI(new URI(req.getURI() + "&" + entityString));
					} else {
						req.setURI(new URI(req.getURI() + "?" + entityString));
					}
				}
			}
			// file 文件使用post 添加文件请看addPart
			else if (mode.equals("FILE")) {
				if (entityString.length() > 0) {
					req.setURI(new URI(iurl+ "?" + entityString));
				}
				((HttpPost) req).setEntity(reqEntity);
			}
			//DELETE 暂时用get 如果有需要再改造
			else if (mode.equals("DELETE")) {
				req.setURI(new URI(iurl));
				if (entityString.length() > 0) {
					if (req.getURI().toString().contains("?")) {
						req.setURI(new URI(req.getURI() + "&" + entityString));
					} else {
						req.setURI(new URI(req.getURI() + "?" + entityString));
					}
				}
			} else {
				// 默认暂为POST
				StringEntity se = new StringEntity(entityString, HTTP.UTF_8);
				((HttpPost) req).setEntity(se);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送请求
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void sendRequest() throws ClientProtocolException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		long st = System.currentTimeMillis();
		requestStartTime = sdf.format(st);
		double starttime = st;
		resp = client.execute(req);
		long stop = System.currentTimeMillis();
		requestStopTime = sdf.format(stop);
		double endtime = stop;
		duration = (endtime - starttime) / 1000;
		HttpEntity entity = resp.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			responseString = sb.toString();
			req.releaseConnection();
			req.reset();
			req.completed();
			long respstop = System.currentTimeMillis();
			responseStopTime = sdf.format(respstop);
			Logger.debug(req.getURI() + " 消耗时间:" + getDuration());
		} else {
			Logger.debug(req.getURI() + " 没有返回内容");
		}

	}

	/**
	 * 发送请求,把结果以文件形式保存下来
	 * 
	 * @return 文件路径
	 */
	public String sendRequestAndWriteResponseToFile() {
		String file = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
			long st = System.currentTimeMillis();
			requestStartTime = sdf.format(st);
			double starttime = st;
			resp = client.execute(req);
			long stop = System.currentTimeMillis();
			requestStopTime = sdf.format(stop);
			double endtime = stop;
			duration = (endtime - starttime) / 1000;
			HttpEntity entity = resp.getEntity();
			InputStream in = entity.getContent();

			file = System.getProperty("user.dir") +  File.separator+"temp"+ File.separator+"temp" + System.currentTimeMillis();
			File f = new File(file);
			File floder = new File(System.getProperty("user.dir") +  File.separator+"temp");
			if (!(floder.exists() && floder.isDirectory())) {
				floder.mkdir();
			}
			f.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			byte[] buff = new byte[1];
			while (in.read(buff) > 0) {
				out.write(buff);
			}
			out.flush();
			out.close();
			in.close();

			req.releaseConnection();
			long respstop = System.currentTimeMillis();
			responseStopTime = sdf.format(respstop);
			Logger.debug(req.getURI() + " 消耗时间:" + getDuration());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 返回请求消耗时间，单位秒
	 * 
	 * @return duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * 获取http请求状态
	 * 
	 * @return 状态返回码 如遇到异常返回-1
	 */
	public int getStatusCode() {

		int statusCode = -1;
		try {
			statusCode = resp.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusCode;
	}

	/**
	 * 获取 head 中的 Cookie
	 * 
	 * @return 获得 Cookie
	 */
	public List<String> getCookies() {
		List<String> Cookies = new ArrayList<String>();
		try {
			Header[] respheader = resp.getAllHeaders();

			// 添加response Set-Cookie
			for (int i = 0; i < respheader.length; i++) {
				if (respheader[i].getName().equalsIgnoreCase("Set-Cookie")) {
					if (respheader[i].getValue().trim().length() > 0) {
						Cookies.add(respheader[i].getValue());
					}
				}
			}
			Header[] reqheader = req.getAllHeaders();
			// 添加request Cookie
			for (int i = 0; i < reqheader.length; i++) {
				if (reqheader[i].getName().equalsIgnoreCase("Cookie")) {
					if (reqheader[i].getValue().trim().length() > 0) {
						Cookies.add(reqheader[i].getValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Cookies;
	}

	/**
	 * @return 获取所有CloseableHttpResponse header Name:Value
	 */
	public List<String> getAllHeader() {
		List<String> Cookies = new ArrayList<String>();
		try {
			Header[] respheader = resp.getAllHeaders();
			for (int i = 0; i < respheader.length; i++) {
				if (respheader[i].getValue().trim().length() > 0) {
					Cookies.add(respheader[i].getName() + ":" + respheader[i].getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Cookies;
	}

	/**
	 * removeAllHeader 移除 HttpRequestBase 的所有 Header
	 */
	public void removeAllHeader() {
		try {
			Header[] header = req.getAllHeaders();
			for (int i = 0; i < header.length; i++) {
				req.removeHeader(header[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取返回Header
	 * 
	 * @param headerid
	 *            headerid
	 * @return header value
	 */
	public String getResposeHeader(String headerid) {
		String header = null;
		try {
			Header[] headers = resp.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].getName().equalsIgnoreCase(headerid)) {
					header = headers[i].getValue();
					return header;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}

	/**
	 * 获取response 返回内容 String
	 * 
	 * @return responseString
	 */
	public String getResponseString() {
		return responseString;
	}

	/**
	 * 请求开始时间
	 * 
	 * @return yyyyMMddhhmmssSSS格式
	 */
	public String getRequestStartTime() {
		return requestStartTime;
	}

	/**
	 * 请求结束时间
	 * 
	 * @return yyyyMMddhhmmssSSS格式
	 */
	public String getRequestStopTime() {
		return requestStopTime;
	}

	/**
	 * 响应读取结束时间
	 * 
	 * @return yyyyMMddhhmmssSSS格式
	 */
	public String getResponseStopTime() {
		return responseStopTime;
	}

	/**
	 * 添加文件
	 * 
	 * @param name
	 *            参数名
	 * @param fn
	 *            文件路径
	 */
	public void addPart(String name, String fn) {
		FileBody fb = new FileBody(new File(fn));
		reqEntity.addPart(name, fb);
	}

	/**
	 * 设置连接超时时间
	 * 
	 * @param timeout
	 *            连接超时时间，单位ms
	 */
	public void setConnectionTimeout(int timeout) {
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
	}

	/**
	 * 设置读取超时时间
	 * 
	 * @param timeout
	 *            读取超时时间，单位ms
	 */
	public void setSoTimeout(int timeout) {
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
	}

	/**
	 * closc client
	 */
	public void close() {
		client.close();
	}
}
