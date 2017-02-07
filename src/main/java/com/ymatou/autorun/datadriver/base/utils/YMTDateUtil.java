package com.ymatou.autorun.datadriver.base.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YMTDateUtil {

	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String YMD = "yyyy-MM-dd";
	public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YMDTHMS_STRING="yyyy-MM-dd'T'HH:mm:ss";
	

	public static SimpleDateFormat getYMDHMSFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS);
		return sdf;
	}

	public static SimpleDateFormat getYMDFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(YMD);
		return sdf;
	}

	/***
	 * mongoDB ISODate 格式转换
	 * 
	 * @param mongoTime
	 * @return
	 */
	public static String getFromISODate(String mongoTime) {
		Matcher matcher = Pattern.compile(
				"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z").matcher(
				mongoTime);
		return matcher.find() ? matcher.group(0).replaceFirst("T", " ")
				.replaceFirst(".\\d{3}Z", "") : null;
	}

	/**
	 * 字符串转Date<br>
	 * 格式: "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseYMDHMSDate(String dateStr) {
		return parseDate(dateStr, YMDHMS);
	}

	/**
	 * 字符串转Date<br>
	 * 格式: "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		if (dateStr != null && "".equals(dateStr.trim()) == false) {
			try {
				date = new SimpleDateFormat(format).parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 字符串转Date<br>
	 * 格式: "yyyy-MM-dd"
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseYMDDate(String dateStr) {
		return parseDate(dateStr, YMD);
	}

	/**
	 * 获取当前时间 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 获取当前时间
	 */
	public static String getSysTime() {
		Calendar calendar = Calendar.getInstance();
		String strSysTime = new SimpleDateFormat(YMDHMS).format(calendar
				.getTime());
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
	 * 获取当前时间
	 * 
	 * @return date
	 */
	public static Date getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * Calendar对象转为String
	 * 
	 * @param c
	 * @param format
	 *            日期格式,例如: yyyy-MM-dd
	 * @return
	 */
	public static String calendarToString(Calendar c, String format) {
		return new SimpleDateFormat(format).format(c.getTime());
	}

	/**
	 * 获取格式化时间 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期对象
	 * @param format
	 *            格式 yyyy-MM-dd HH:mm:ss
	 * @return 格式化后的日期string
	 */
	public static String getFormatTime(Date date, String format) {
		String strSysTime = new SimpleDateFormat(format).format(date);
		return strSysTime;
	}

	/**
	 * 获取格式化时间 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期对象, 可以是Date,Timestamp等对象
	 * @param format
	 *            格式 yyyy-MM-dd HH:mm:ss
	 * @return 格式化后的日期string
	 */
	public static String getObjectFormatTime(Object date, String format) {
		String strSysTime = new SimpleDateFormat(format).format(date);
		return strSysTime;
	}

	/**
	 * 获取和当前时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n天的时间（n=1为明天，n=-1为昨天）
	 */
	public static String getBeforeOrNextDay(int n) {
		return addDays(n, YMDHMS);
	}

	private static String addDays(Calendar calendar, int n, String format) {
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	public static String addDays(int n, String format) {
		return addDays(Calendar.getInstance(), n, format);
	}

	/**
	 * 获取和指定时间差n天的时间
	 * 
	 * @param dateStr
	 * @param n
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static String getBeforeOrNextDay(String dateStr, int n, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr, format));
		return addDays(calendar, n, format);
	}
	
	public static String getBeforeOrNextMinutes(String dateStr, int n, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr, format));
		return addMinutes(calendar,n, format);
	}

	/**
	 * 获取和指定时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return dateStr+n天的时间
	 */
	public static String getBeforeOrNextDay(String dateStr, int n) {
		return getBeforeOrNextDay(dateStr, n, YMDHMS);
	}

	private static String addMonths(Calendar calendar, int n, String format) {
		calendar.add(Calendar.MONTH, n);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}
	
	private static String addMinutes(Calendar calendar, int n, String format) {
		calendar.add(Calendar.MINUTE, n);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}
	

	public static String addMonths(int n, String format) {
		return addMonths(Calendar.getInstance(), n, format);
	}

	public static String getBeforeOrNextMonth(String dateStr, int n,
			String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr, format));
		return addMonths(calendar, n, format);
	}

	/**
	 * 获取和指定时间差n天的时间, 格式yyyy-MM-dd
	 * 
	 * @param n
	 * @return dateStr+n天的时间
	 */
	public static String getBeforeOrNextDayStr(String dateStr, int n) {
		return getBeforeOrNextDay(dateStr, n, YMD);
	}

	/**
	 * 获取和当前时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n天的时间（n=1为下个月，n=-1为上个月）
	 */
	public static String getBeforeOrNextMonth(int n) {
		return addMonths(n, YMDHMS);
	}

	/**
	 * 获取和指定时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return dateStr+n个月的时间
	 */
	public static String getBeforeOrNextMonth(String dateStr, int n) {
		return getBeforeOrNextMonth(dateStr, n, YMDHMS);
	}
	
	public static String getBeforeOrNextMonth(int n, String format) {
		return addMonths(n, format);
	}

	private static String addYears(Calendar calendar, int n, String format) {
		calendar.add(Calendar.YEAR, n);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	public static String addYears(int n, String format) {
		return addYears(Calendar.getInstance(), n, format);
	}

	/**
	 * 获取和当前时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n年的时间（n=1为明年，n=-1为去年）
	 */
	public static String getBeforeOrNextYear(int n) {
		return addYears(n, YMDHMS);
	}

	public static String getBeforeOrNextYear(String dateStr, int n,
			String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr, format));
		return addYears(calendar, n, format);
	}

	/**
	 * 获取和指定时间差n天的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return dateStr+n年的时间
	 */
	public static String getBeforeOrNextYear(String dateStr, int n) {
		return getBeforeOrNextYear(dateStr, n, YMDHMS);
	}

	/**
	 * 获取和当前时间差n天的时间
	 * 
	 * @param n
	 * @return now+n天的时间（n=1为明天，n=-1为昨天）
	 */
	public static String getBeforeOrNextDay(int n, String format) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		strSysTime = new SimpleDateFormat(format).format(calendar.getTime());
		return strSysTime;
	}
	
	/**
	 * 获取和当前时间差n天的时间(date型)
	 * 
	 * @param n
	 * @return now+n天的时间（n=1为明天，n=-1为昨天）
	 */
	public static Date getBeforeOrNextDate(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return calendar.getTime();
	}

	
	/**
	 * 获取和当前时间差n秒的时间(date型)
	 * 
	 * @param n
	 * @return now+n秒的时间（n=1为后一秒，n=-1为前一秒）
	 */
	public static Date getDateBeforeOrNextSecond(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, n);
		return calendar.getTime();
	}
	
	/**
	 * 获取和当前时间差n小时的时间
	 * 
	 * @param n
	 * @return now+n小时的时间（n=1为一小时后，n=-1为一小时前）
	 */
	public static String getBeforeOrNextHour(int n, String format) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, n);
		strSysTime = new SimpleDateFormat(format).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取和指定时间差n小时的时间, 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return dateStr+n天的时间
	 */
	public static String getBeforeOrNextHour(String dateStr, int n) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseYMDHMSDate(dateStr));
		calendar.add(Calendar.HOUR_OF_DAY, n);
		strSysTime = new SimpleDateFormat(YMDHMS).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取和当前时间差n小时的时间，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n小时的时间（n=1为一小时后，n=-1为一小时前）
	 */
	public static String getBeforeOrNextHour(int n) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, n);
		// Calendar.m
		strSysTime = new SimpleDateFormat(YMDHMS).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取和当前时间差n分钟的时间，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n分钟的时间（n=1为一分钟后，n=-1为一分钟前）
	 */
	public static String getBeforeOrNextMunite(int n) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, n);
		// Calendar.m
		strSysTime = new SimpleDateFormat(YMDHMS).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取和当前时间差n秒的时间，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param n
	 * @return now+n秒的时间（n=1为一秒后，n=-1为一秒前）
	 */
	public static String getBeforeOrNextSecond(int n) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, n);
		// Calendar.m
		strSysTime = new SimpleDateFormat(YMDHMS).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * 获取和当前时间相差的时间，并以相应的形式返回 Ex，Mongo需要的时间格式:"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	 * 
	 * @param day
	 *            相差日
	 * @param hour
	 *            相差时
	 * @param minutes
	 *            相差分
	 * @param format
	 *            日期格式
	 * @return 符合格式的日期String
	 */
	public static String getDateWithFormat(int day, int hour, int minutes,
			String format) {
		String strSysTime = "";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, day);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE, minutes);
		// Calendar.m
		strSysTime = new SimpleDateFormat(format).format(calendar.getTime());
		return strSysTime;
	}

	/**
	 * Mongo数据库需要的实际格式
	 * 
	 * @see com.ymttest.utils.YMTDateUtil#getDateWithFormat(int, int, int,
	 *      String)
	 * @return String yyyy-MM-dd'T'HH:mm:ss.SSS'Z
	 */
	public static String getDateForMongoFormat(int day, int hour, int minutes) {
		return getDateWithFormat(day, hour, minutes,
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @param date2
	 * @return 时间差， date-date2,单位天
	 */
	public static long diffDay(Date date, Date date2) {
		return Long.valueOf((date.getTime() - date2.getTime())
				/ (1000 * 60 * 60 * 24));
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @param date2
	 * @return 时间差， date-date2,单位小时
	 */
	public static long diffHour(Date date, Date date2) {
		return Long.valueOf((date.getTime() - date2.getTime())
				/ (1000 * 60 * 60));
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @param date2
	 * @return 时间差， date-date2,单位分
	 */
	public static int diffMinute(Date date, Date date2) {
		return (int) ((date.getTime() - date2.getTime()) / (1000 * 60));
	}
	
	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @param date2
	 * @return 时间差， date-date2,单位S
	 */
	public static int diffSecond(Date date, Date date2) {
		return (int) ((date2.getTime() - date.getTime()) / 1000);
	}
	
	/**
	 * 获取时间差
	 * 
	 * @param long
	 * @param long2
	 * @return 时间差， date-date2,单位S
	 */
	public static int diffSecond(long date, long date2) {
		return (int) ((date2 - date) / 1000);
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @param date2
	 * @return 时间差， date-date2,单位ms
	 */
	public static int diffMs(Date date, Date date2) {
		return (int) ((date2.getTime() - date.getTime()));
	}

	/**
	 * 当前时间是否在9点到21点
	 * 
	 * @param date
	 * @return boolean
	 */
	public static boolean isBetweenDate(Date date) {
		String begin = getSysTime("yyyy-MM-dd") + " 09:00:00";
		String end = getSysTime("yyyy-MM-dd") + " 21:00:00";
		boolean result = false;
		try {
			if (date.before(new SimpleDateFormat(YMDHMS).parse(end))
					&& date.after(new SimpleDateFormat(YMDHMS).parse(begin))) {
				result = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 传入指定格式的时间，然后转化成另一种格式的时间
	 * 
	 * @param datestr
	 *            2015-12-13
	 * @param strPattern
	 *            yyyy-MM-dd
	 * @param newFormat
	 *            yyyy-MM-dd'T'HH:mm:ss.SSS'Z
	 * @return String newFormat
	 * @throws ParseException
	 */
	public static String convertDateStrToAnotherFormat(String datestr,
			String strPattern, String newFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(strPattern);
		Date date = sdf.parse(datestr);
		return getFormatTime(date, newFormat);
	}

	/**
	 * 转化时间类型为java中Date
	 *
	 * @author chenjiazhu
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromMongo(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return sdf.parse(date.substring(7, date.length() - 1));
	}
	
	public static Date getDate(String date,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				format);
		return sdf.parse(date);
	}
	/**
	 * 转化返回中的时间类型为java中Date
	 *
	 * @author chenjiazhu
	 * @param date
	 *            ,格式："2016-05-26T23:59:00"
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromReturn(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return sdf.parse(date.substring(0, 18));
	}

	/**
	 * 把传入Date转换成2015-12-11T23:59:00格式
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getFormatDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 将接口返回的格式为"2016-07-11T18:55:37.913"转换为yyyy-MM-dd HH:mm:ss.SSS的格式
	 * 
	 * @author songyefeng
	 * @param respDate
	 * @return
	 */
	public static String parseRespDate(Object respDate) {
		String s = "null";
		if (null != respDate && !"null".equals(respDate)) {
			s = String.valueOf(respDate);
			s = s.replace("T", " ");
			if (s.contains(".") == false) {
				s = s + ".0";
			}
		}
		return s;
	}

	/**
	 * 将接口返回的格式为"2016-07-11T18:55:37"转换为yyyy-MM-dd HH:mm:ss的格式
	 * 
	 * @author songyefeng
	 * @param respDate
	 * @return
	 */
	public static String parseRespDate2(Object respDate) {
		String s = "null";
		if (null != respDate && !"null".equals(respDate)) {
			s = String.valueOf(respDate);
			if (s.contains(".")) {
				s = s.split("\\.")[0];
			}
			s = s.replace("T", " ");
		}
		return s;
	}

	/**
	 * (yyyy-MM-dd HH:mm:ss)日期格式字符串 转 时间戳(秒)
	 * 
	 * @param dateStr
	 *            日期格式字符串
	 * @return
	 */
	public static Long date2TimeStamp(String dateStr) {
		Timestamp ts = Timestamp.valueOf(dateStr);
		return ts.getTime() / 1000;
	}
	
	/**
	 * (EEE MMM dd HH:mm:ss zzz yyyy)CST日期格式字符串 转 时间戳(秒)
	 * 
	 * @param dateStr
	 *            日期格式字符串
	 * @return
	 */
	public static Long CST2TimeStamp(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
		
		Date date = null;
		try {
			date = (Date) sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date.getTime();
	}
	
	/**
	 * (EEE MMM dd HH:mm:ss zzz yyyy)CST日期格式字符串 转 yyyy-MM-dd'T'HH:mm:ss.000XXX
	 * 
	 * @param dateStr
	 *            日期格式字符串
	 * @return
	 */
	public static String CST2ShoppingcartTime(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		
		Date date = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		try {
			date = (Date) sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sdf1.format(date);
	}
	
	
	public static void waitTime(double sec){
		try {
			Thread.sleep((long)(sec*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
}
