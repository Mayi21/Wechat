package Server;

import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JudgeUser {
	private static Connection connection;
	public static String judge(String id,String p){
		connection = MySqlDao.getConnection();
		String sql = "SELECT * FROM wechat";
		String status = "0";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				String string = resultSet.getString("id");
				if (string.equals(id) && resultSet.getString("passwd").equals(p)){
					status = "1";
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return status;
	}
}
