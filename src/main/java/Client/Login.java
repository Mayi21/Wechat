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
			Image image1 = new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\cityos (2).jpg");
			Image image = new Image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551970832740&di=727544cf3f3f0169e36df942e8d11aea&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd0c8a786c9177f3e9f8b7f297ecf3bc79f3d5653.jpg");
			primaryStage.getIcons().add(image1);
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