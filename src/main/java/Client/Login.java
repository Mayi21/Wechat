package Client;
import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Login extends Application {
	private static Stage p;
	@Override
	public void start(Stage primaryStage) {
		p = primaryStage;
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
			Image image1 = new Image("file:E:\\Study\\CodeProject\\Java\\Wechat\\src\\main\\resources\\cityos (2).jpg");
			primaryStage.getIcons().add(image1);
			primaryStage.setTitle("Chat");
			Scene mainScene = new Scene(parent, 600, 400);
			mainScene.getStylesheets().add(Login.class.getResource("login.css").toExternalForm());
			mainScene.setRoot(parent);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
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