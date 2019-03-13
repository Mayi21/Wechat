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
			String table = "u123456";
			String anotherId = "12345";
			String status = "1";
			preparedStatement = connection.prepareStatement("update " + table + " set status=" + status + " where user=" + anotherId);
			preparedStatement.executeUpdate();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

}