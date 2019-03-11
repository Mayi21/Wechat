package Client;

import Client.Buubble.BubbleSpec;
import Client.Buubble.BubbledLabel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatCon implements Initializable {
	@FXML private TextArea messageBox;
	@FXML private Button messageSendButton;
	@FXML private Label idLabel;
	@FXML private ListView userList;
	@FXML private ListView chatList;
	@FXML private Label currentId;
	@FXML private ImageView image;
	//public static Stage stage = new Stage();
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
	public void setImageView(String id){
		String url = "file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\" + id + ".jpg";
		Image image = new Image(url);
		this.image.setImage(image);
	}
	public void send() throws Exception{
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()){
			Listener.send(msg);
			messageBox.clear();
		}
	}
//	public void xiao(){
//		stage.setIconified(true);
//	}
//	public void tui(){
//		stage.close();
//	}
	public void setUserList(JSONObject message) throws Exception{
		Platform.runLater(() ->  {
			List<String> list = new LinkedList<>();
			try {
				JSONArray jsonArray = message.getJSONArray("List");
				for (int i = 0;i < jsonArray.length();i++){
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
			Text text = null;
			try {
				text = new Text(message.getString("Message"));
				text.setFont(new Font(15));
				text.setFill(Color.BLACK);
				VBox box = new VBox();
				box.getChildren().add(text);
				box.setStyle("-fx-background-color: white");

				final int width = 200;
				final int height = 50;
				final Scene scene = new Scene(box, width, height);
				scene.setFill(null);

				//stage = new Stage();
				final Stage stage = new Stage();
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.setScene(scene);
				Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
				stage.setX(primaryScreenBounds.getWidth() - width - 50);
				stage.setY(primaryScreenBounds.getHeight() - height - 50);
				stage.show();
				Task t = new Task() {
					@Override
					protected Object call() throws Exception {
						Thread.sleep(1000);
						Platform.runLater(stage::close);
						return "";
					}
				};
				new Thread(t).start();
			} catch (Exception e){
				e.printStackTrace();
			}
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
