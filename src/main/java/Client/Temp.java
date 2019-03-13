package Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Temp{
	public static void main(String[] args) throws Exception {
		Connection connection = MySqlDao.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			String s = "u123456";
			String sql = "select * from " + s;
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				System.out.println(resultSet.getString("user") + "  11");
			}
		} catch (Exception e){
			e.getMessage();
		}
	}

}