package Client;

import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Check {
	private static String id;
	private static String pa;
	public static final String SQL = "SELECT * FROM wechat";
	public static Connection connection;
	public static String userName;

	/**
	 * 检查账户和密码是否对
	 * */
	public static boolean check(String account, String passwd){
		boolean status = false;
		connection = MySqlDao.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SQL);
			while (resultSet.next()){
				Check.id = resultSet.getString("id");
				Check.pa = resultSet.getString("passwd");
				if (account.equals(Check.id) & passwd.equals(Check.pa)){
					userName = resultSet.getString("userName");
					status = true;
					break;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return status;
	}
}
