package com.ymatou.autorun.datadriver.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/************************************************************************************
 * 数据生成类
 * 
 * @File name : DataManager.java
 * @Author : zhouyi
 * @Date : 2015年3月10日
 * @Copyright : 洋码头
 ************************************************************************************/
public class DataManager {

	private static ArrayList<String> filelist = new ArrayList<String>();

	/**
	 * 获取一串唯一的clientPaymentId
	 * 本质为获取 添加同步锁后 sleep nanos 1  get System.nanoTime
	 * @return 不重复的nanoTime戳
	 */
	public static synchronized String getClientpaymentId() {
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return String.valueOf(System.nanoTime());

	}

	/**
	 * 获取当前时间 格式 yyyy-MM-dd
	 * 
	 * @return 获取当前时间
	 */
	public static String getSysTime() {
		Calendar calendar = Calendar.getInstance();
		String strSysTime = new SimpleDateFormat("yyyy-MM-dd")
				.format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取当前时间 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 获取当前时间
	 */
	public static String getSysTime(String format) {
		Calendar calendar = Calendar.getInstance();
		String strSysTime = new SimpleDateFormat(format).format(calendar
				.getTime());
		return strSysTime;
	}

	/**
	 * 把文件转换为base64编码串
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static String getBase64(String filepath) throws Exception {

		byte[] buf = null;
		try {
			InputStream fs = new FileInputStream(filepath);
			buf = new byte[fs.available()];
			fs.read(buf);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Class<?> clazz = Class
				.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, new Object[] { buf });
		return (String) retObj;

	}

	/**
	 * 获取md5串
	 * 
	 * @param source
	 *            源数据
	 * @return source对应的md5
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 初始化数据 获取ArrayList<String[]> 读取文件 每行一个string[] 空格分割
	 * 
	 * @param f
	 *            文件名
	 * @return ArrayList<String[]>
	 */
	public static ArrayList<String> getData(String fileName) {
		ArrayList<String> as = new ArrayList<String>();
		RandomAccessFile readFile = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				readFile = new RandomAccessFile(fileName, "r");
				String s = null;
				while ((s = readFile.readLine()) != null) {
					s = new String(s.getBytes("iso8859-1"), "utf-8");
					as.add(s);
				}
				try {
					readFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (as.size() == 0) {
			as.add("");
		}
		return as;
	}

	/**
	 * 获取该目录下所有文件路径list
	 * 
	 * @param filePath
	 *            目录路径
	 * @return 文件路径list
	 */
	public static ArrayList<String> getFilelist(String filePath) {
		filelist = new ArrayList<String>();
		getFiles(filePath);
		return filelist;
	}

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	private static void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file.getAbsolutePath());
				// Logger.debug("显示"+filePath+"下所有子目录:"+file.getAbsolutePath());
			} else {
				//排除空的xml文件
				if (file.length()>0){
					filelist.add(file.getAbsolutePath());
				}			
				// Logger.debug("显示"+filePath+"下所有文件:"+file.getAbsolutePath());
			}
		}
	}

	/**
	 * 把一串字符串追加写入一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param filename
	 *            文件名
	 * @param filebody
	 *            文件内容
	 *
	 */
	public static void appendFileToLien(String path, String filebody) {
		File f1 = new File(path);
		FileOutputStream fo1 = null;
		try {
			if (!f1.exists()) {
				f1.createNewFile();
			}
			fo1 = new FileOutputStream(f1, true);
			new PrintStream(fo1).println(filebody);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
	public static List<String> getRegexList(String rts, String regEx) {
		List<String> finds = new ArrayList<String>();
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(rts);
		while (m.find()) {
			finds.add(m.group());
		}
		return finds;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            要删除的文件
	 * @return 是否删除
	 */
	public static boolean removeFile(String path) {
		File f1 = new File(path);
		return f1.delete();
	}

	/**
	 * 获取文件路径分隔符
	 * @return 文件路径分隔符
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
}
