package com.ymatou.autorun.datadriver.base.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;



/**
 * DBUtils 用于mysql操作
 */
public final class DBUtil {
	private static String url = "jdbc:mysql://172.16.100.30:3306/better"; // 数据库地址
	private static String username = "root"; // 数据库用户名
	private static String password = "123456";// 数据库密码
	private Connection conn;
	private Savepoint savepoint;

	/**
	 * 初始化加载mysql驱动
	 * @throws SQLException
	 */
	public DBUtil()  {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			Logger.debug("mqsql驱动加载失败！");
			e.printStackTrace();
		} catch (SQLException e) {
			Logger.debug("连接数据库异常，请检查配置！");
			e.printStackTrace();
		}
	}

	/**
	 * 获得连接
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (conn.isClosed()) {
			Logger.debug("reconnecting...");
			conn = DriverManager.getConnection(url, username, password);
		}
		return conn;
	}
	
	/**
	 * 释放连接
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 设置一个savepoint 用于事务
	 * @throws SQLException
	 */
	public void savePoint() throws SQLException{
		savepoint=conn.setSavepoint();
	}
	/**
	 * 释放savepoint
	 * @throws SQLException
	 */
	public void releaseSavepoint() throws SQLException{
		conn.releaseSavepoint(savepoint);
		savepoint=null;
	}
	/**
	 * 回滚操作,返回到上一次 savePoint
	 * @throws SQLException
	 */
	public void rollbackToSave() throws SQLException{
		conn.rollback(savepoint);
	}
	/**
	 *  回滚操作
	 */
	public void rollback() throws SQLException{
		conn.rollback();
	}
	/**
	 * 提交
	 * @throws SQLException
	 */
	public void commit() throws SQLException{
		conn.commit();
	}
	/**
	 * 设置是否自动提交sql
	 * @param isAutoCommit
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean isAutoCommit) throws SQLException{
		conn.setAutoCommit(isAutoCommit);
	}
	/**
	 * 执行select sql
	 * 
	 * @param sql sql语句
	 * @param value 参数
	 * @return 查询出来的结果集
	 * @throws SQLException
	 */
	public ResultSet select(String sql, List<String> value) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		int i = 1;
		if (value != null && value.size() > 0) {
			for (String s : value) {
				st.setString(i, s);
				i++;
			}
		}
		ResultSet rs = st.executeQuery();
		return rs;
	}

	/**
	 * 执行update&&insert sql
	 * 
	 * @param sql update&&insert sql
	 * @param value 参数
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int update(String sql, List<String> value) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		int i = 1;
		if (value != null && value.size() > 0) {
			for (String s : value) {
				st.setString(i, s);
				i++;
			}
		}
		int rs = st.executeUpdate();
		return rs;
	}

}