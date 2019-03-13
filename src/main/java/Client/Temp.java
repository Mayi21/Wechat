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
		PreparedStatement preparedStatement = null;
		//ResultSet resultSet = null;
		try {
			String username = "123张三";
			String id = "123456";
			preparedStatement = connection.prepareStatement("UPDATE wechat SET userName='" + username + "' WHERE id=" + id);
			preparedStatement.executeUpdate();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

}