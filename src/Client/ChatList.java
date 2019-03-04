package Client;

import Client.LoginCon;
import Client.SendMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ChatList extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		SendMessage sendMessage = new SendMessage();
		LoginCon loginCon = new LoginCon();
		String id = loginCon.getId();
		Label label = sendMessage.getIdLabel();
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("fxml/chatList.fxml"));
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			label.setText(id);
			primaryStage.setTitle("Chat");
			primaryStage.getIcons().add(new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\Client\\cityos (2).jpg"));
			Scene mainScene = new Scene(parent, 960, 540);
			mainScene.setRoot(parent);
			primaryStage.setResizable(false);
			primaryStage.setScene(mainScene);
			primaryStage.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
