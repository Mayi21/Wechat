package Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Temp extends Application {
	@Override
	public void start(Stage stage){

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("fxml/yuan.fxml"));
			Image image1 = new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\cityos (2).jpg");
			stage.getIcons().add(image1);
			stage.setTitle("Chat");
			Scene mainScene = new Scene(parent);
			//mainScene.getStylesheets().add(Login.class.getResource("login.css").toExternalForm());
			mainScene.setRoot(parent);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setResizable(false);
			stage.setScene(mainScene);
			stage.show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		launch(args);
	}

}