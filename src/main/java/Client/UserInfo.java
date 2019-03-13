package Client;

import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserInfo {
	public static String getId(String username){
		String id = null;
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT id from wechat where userName='" + username +"'");
			while (resultSet.next()){
				id = resultSet.getString("id");
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}
	public static String getUserName(String id){
		String userName = null;
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT userName from wechat where id='" + id + "'");
			while (resultSet.next()){
				userName = resultSet.getString("userName");
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return userName;
	}
}
