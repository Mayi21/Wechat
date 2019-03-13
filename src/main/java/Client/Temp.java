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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Temp{
	public static void main(String[] args) throws Exception {
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM wechat");
			while (resultSet.next()){
				System.out.println(resultSet.getString("id"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}