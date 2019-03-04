package Client;

import javafx.application.Platform;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCon implements Initializable {
	@FXML private Button loginButton;
	@FXML private TextField userId;
	@FXML private PasswordField passwd;
	@FXML private TextField port;
	@FXML private TextField server;
	public ChatCon control;
	private Scene scene;
	public static LoginCon loginCon;
	public LoginCon(){
		loginCon = this;
	}
	public static LoginCon getLoginCon(){
		return loginCon;
	}
	public void login() throws Exception{
		String id = userId.getText();
		String pa = passwd.getText();
		/**
		 * TODO 此处应该是发送给服务器做验证
		 */
		Check check = new Check();
		Stage stage = Login.getP();
		if (Check.check(id, pa)) {
			FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("fxml/chatlist.fxml"));
			Parent parent = (Pane) fxmlLoader.load();
			control = fxmlLoader.<ChatCon>getController();
			Listener listener = new Listener(server.getText(),port.getText(),userId.getText(),control);
			Thread thread = new Thread(listener);
			thread.start();
			this.scene = new Scene(parent);
		} else {
			userId.clear();
			passwd.clear();
			System.out.println("error");
		}
	}
	public void showScene() throws IOException {
		Platform.runLater(() -> {
			Stage stage = (Stage) port.getScene().getWindow();
			stage.setResizable(true);
			stage.setOnCloseRequest((WindowEvent e) -> {
				Platform.exit();
				System.exit(0);
			});
			stage.setScene(this.scene);
			stage.setMinWidth(960);
			stage.setMinHeight(540);
			ResizeHelper.addResizeListener(stage);
			stage.centerOnScreen();
			control.setUserLabel(userId.getText());
		});
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	public String getId(){
		return userId.getText();
	}
	public String getServer(){
		return server.getText();
	}
	public String getPort(){
		return port.getText();
	}

}
