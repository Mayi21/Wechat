package Client;

import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Login extends Application {
	private static Stage p;
	@Override
	public void start(Stage primaryStage) {
		p = primaryStage;
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
			primaryStage.getIcons().add(new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\Client\\cityos (2).jpg"));
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setTitle("Chat");
			Scene mainScene = new Scene(parent, 600, 400);
			mainScene.setRoot(parent);
			primaryStage.setResizable(false);
			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	public static Stage getP(){
		return p;
	}
}