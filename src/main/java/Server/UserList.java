package Server;


import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

public class UserList {
	public static LinkedList<String> getFriendList(String id){
		LinkedList<String> linkedList = new LinkedList<>();
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String table = "u" + id;
			System.out.println(table);
			resultSet = statement.executeQuery("SELECT user from " + table);
			while (resultSet.next()){
				linkedList.add(resultSet.getString("user"));
			}
		}catch (Exception e){
			System.out.println("发生在Class: UserList的getFriendList的方法中，异常是：\n" + e.getMessage());
		}
		return linkedList;
	}
}
