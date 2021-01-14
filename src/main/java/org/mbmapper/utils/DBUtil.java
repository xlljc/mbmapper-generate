package org.mbmapper.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

	// 连接字符串
	private static String url;
	// 登录数据库用户名
	private static String user;
	// 登录数据库密码
	private static String pwd;

	/**
	 * 初始化连接参数
	 */
	public static void init(String url,String user,String password,String driver) throws ClassNotFoundException {
		Class.forName(driver);
		DBUtil.url = url;
		DBUtil.user = user;
		DBUtil.pwd = password;
	}

	/**
	 * 获取连接对象
	 */
	public static Connection getConn() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

	/**
	 * 关闭连接
	 */
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
