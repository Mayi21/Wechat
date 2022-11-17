package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlDao {
	private static final String userName = "root";
	private static final String passwd = "123456";
	private static final String connectionURL = "jdbc:mysql://127.0.0.1:3306/wechat";
	public static Connection AD(int a){
		Connection connection = getConnection();
		if (a == 0){
			releaseConnection(connection);
		}
		return connection;
	}
	// TODO 数据操作方式迁移
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connect = DriverManager.getConnection(connectionURL,userName,passwd);
			return connect;
		}catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
		return null;
	}
	public static void releaseConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
