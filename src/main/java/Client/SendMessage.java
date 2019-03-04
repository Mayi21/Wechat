package Client;

import Client.Buubble.BubbleSpec;
import Client.Buubble.BubbledLabel;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ResourceBundle;

public class SendMessage implements Initializable{
	@FXML private Button messageSendButton;
	@FXML private TextArea messageBox;
	@FXML private ListView chatList;
	@FXML private Label idLabel;
	private String id;
	private String port;
	private String server;

	public void sendM(){
		if (!messageBox.getText().isEmpty()){
			System.out.println(messageBox.getText());
			recordMessage(messageBox.getText());
			messageBox.clear();
		}
	}
	/**
	 * TODO 记录两个用户的聊天信息。
	 */
	public void recordMessage(String string){
		Task<HBox> hBoxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				BubbledLabel bubbledLabel =new BubbledLabel();
				bubbledLabel.setText(string);
				bubbledLabel.setBackground(new Background(new BackgroundFill(Color.WHITE,null, null)));
				HBox x = new HBox();
				bubbledLabel.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
				x.getChildren().addAll(bubbledLabel);
				//logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
				//setOnlineLabel(Integer.toString(msg.getOnlineCount()));
				return x;
			}

		};
		hBoxTask.setOnSucceeded(event -> {
			chatList.getItems().add(hBoxTask.getValue());
		});
		Task<HBox> hboxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				BubbledLabel bubbledLabel = new BubbledLabel();
				bubbledLabel.setText("<此处应该是接收到的消息>");
				bubbledLabel.setBackground(new Background(new BackgroundFill(Color.GRAY,null,null)));
				HBox x = new HBox();
				bubbledLabel.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
				x.getChildren().add(bubbledLabel);
				return x;
			}
		};
		hboxTask.setOnSucceeded(event -> {
			chatList.getItems().add(hboxTask.getValue());
		});
		Thread thread1 = new Thread(hBoxTask);
		thread1.setDaemon(true);
		thread1.start();
	}
	public Label getIdLabel(){
		return idLabel;
	}
	public void L(){
		LoginCon loginCon = new LoginCon();
		port = loginCon.getPort();
		server = loginCon.getServer();
		id = loginCon.getId();
		//Listener listener = new Listener(server, port, id);
		//Thread thread = new Thread(listener);
		//thread.start();

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}