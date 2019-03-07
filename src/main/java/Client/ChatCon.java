package Client;

import Client.Buubble.BubbleSpec;
import Client.Buubble.BubbledLabel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.plaf.metal.MetalBorders;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatCon implements Initializable {
	@FXML private TextArea messageBox;
	@FXML private Button messageSendButton;
	@FXML private Label idLabel;
	@FXML private ListView userList;
	@FXML private ListView chatList;
	@FXML private Label onlineLabel;
	@FXML private Label currentId;
	public static String current;
	public synchronized void addChat(JSONObject message) throws Exception{
		Task<HBox> hBoxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				BubbledLabel bubbledLabel =new BubbledLabel();
				ImageView imageView = new ImageView(new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\cityos (2).jpg"));
				bubbledLabel.setText(message.getString("Message"));
				bubbledLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,null, null)));
				HBox x = new HBox();
				x.setAlignment(Pos.TOP_RIGHT);
				bubbledLabel.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
				x.getChildren().addAll(bubbledLabel);
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
				bubbledLabel.setText(message.getString("SendId") + " : " + message.getString("Message"));
				bubbledLabel.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.GRAY,null,null)));
				HBox x = new HBox();
				x.setAlignment(Pos.TOP_LEFT);
				bubbledLabel.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
				x.getChildren().add(bubbledLabel);
				return x;
			}
		};
		hboxTask.setOnSucceeded(event -> {
			chatList.getItems().add(hboxTask.getValue());
		});
		if (message.getString("SendId").equals(idLabel.getText())){
			Thread thread = new Thread(hBoxTask);
			thread.setDaemon(true);
			thread.start();
		} else {
			Thread thread = new Thread(hboxTask);
			thread.setDaemon(true);
			thread.start();
		}
	}
	public void setUserLabel(String id){
		this.idLabel.setText(id);
	}
	public void send() throws Exception{
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()){
			Listener.send(msg);
			messageBox.clear();
		}
	}
	public void setOnlineLabel(String usercount) {
		Platform.runLater(() -> onlineLabel.setText(usercount));
	}

	public void setUserList(JSONObject message) throws Exception{

		Platform.runLater(() ->  {
			List<String> list = new LinkedList<>();
			try {
				JSONArray jsonArray = message.getJSONArray("List");
				for (int i = 0;i < jsonArray.length();i++){
					System.out.println(jsonArray + "  " + idLabel.getText());
					if (!jsonArray.getString(i).equals(idLabel.getText())){
						list.add(jsonArray.getString(i));
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			ObservableList<String> users = FXCollections.observableList(list);
			userList.setItems(users);
			userList.setCellFactory(new CellRenderer());
			setOnlineLabel(String.valueOf(list.size()));
		});
	}
	public void getToUser(){
		userList.getSelectionModel().selectedItemProperty().addListener(
				(ChangeListener<String>) (observable, oldValue, newValue) -> {
					currentId.setText(newValue);
					ChatCon.current = newValue;
				});
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messageBox.addEventFilter(KeyEvent.KEY_PRESSED,ke ->{
			if (ke.getCode().equals(KeyCode.ENTER)){
				try {
					send();
				} catch (Exception e){
					e.printStackTrace();
				}
				ke.consume();
			}
		});
	}
}
