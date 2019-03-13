package Client;

import Client.Buubble.BubbleSpec;
import Client.Buubble.BubbledLabel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.json.JSONArray;
import org.json.JSONObject;
import util.MySqlDao;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatCon implements Initializable {
	@FXML private TextArea messageBox;
	@FXML private Button messageSendButton;
	@FXML private Label idLabel;
	@FXML private Menu imageMenu;
	@FXML private ListView userList;
	@FXML private ListView chatList;
	@FXML private Label currentUserName;
	@FXML private Menu opMenu;
	@FXML private TextField search;
	public static String current;
	public synchronized void addChat(JSONObject message) throws Exception{
		Task<HBox> hBoxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				BubbledLabel bubbledLabel =new BubbledLabel();
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
		if (message.getString("SendId").equals(UserInfo.getId(idLabel.getText()))){
			Thread thread = new Thread(hBoxTask);
			thread.setDaemon(true);
			thread.start();
		} else if (message.getString("ToId").equals(UserInfo.getId(currentUserName.getText()))){
			Thread thread = new Thread(hboxTask);
			thread.setDaemon(true);
			thread.start();
		}
//		else {
//			getOtherChat(message);
//
//		}
	}
	//不是正在聊天的用户发送消息
	public void getOtherChat(JSONObject message){
		Text text = null;
		try {
			Text send = new Text(message.getString("SendId"));
			text = new Text(message.getString("Message"));
			text.setFont(new Font(15));
			text.setFill(Color.GREEN);
			send.setFill(Color.WHITE);
			send.setFont(new Font(20));
			VBox box = new VBox();
			box.getChildren().addAll(send,text);
			box.setStyle("-fx-background-color: black");
			final int width = 200;
			final int height = 50;
			final Scene scene = new Scene(box, width, height);
			scene.setFill(null);
			Stage stage = new Stage();
			//stage.initStyle(StageStyle.TRANSPARENT);
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
	}
	public void opMenu(){
		MenuItem menuItem1 = new MenuItem("删除好友");
		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/**
				 * TODO 删除好友，对接数据库
				 */
			}
		});
		MenuItem menuItem2 = new MenuItem("加入黑名单");
		menuItem2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/**
				 * TODO 加入黑名单
				 */
			}
		});
		opMenu.getItems().clear();
		opMenu.getItems().addAll(menuItem1,menuItem2);
	}

	public void setUserLabel(String id){
		this.idLabel.setText(UserInfo.getUserName(id));
	}
	public void setImageView(String id){
		String url = "file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\" + id + ".jpg";
		Image image = new Image(url);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(60);
		imageView.setFitWidth(60);
		imageMenu.setGraphic(imageView);
		MenuItem menuItem = new MenuItem("修改网名");
		menuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				display();
			}
		});
		menuItem.setStyle("-fx-background-color: linear-gradient(to right,#a1c4fd,RGB(164,200,253))");
		MenuItem menuItem1 = new MenuItem("修改头像");
		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateIcon();
			}
		});
		imageMenu.getItems().addAll(menuItem,menuItem1);
	}
	//修改头像
	public void updateIcon(){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.TRANSPARENT);
		window.setMinWidth(300);
		window.setMinHeight(150);
		Image defaultImage = new Image("file:D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\头像.jpg");
		ImageView imageView = new ImageView();
		imageView.setFitHeight(60);
		imageView.setFitWidth(60);
		final String[] newPath = new String[1];
		imageView.setImage(defaultImage);
		Button button = new Button("选择图片");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("选择图片");
				fileChooser.setInitialDirectory(new File("D:\\Picture\\素材"));
				fileChooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("JPG", "*.jpg")
				);
				File file = fileChooser.showOpenDialog(window);
				newPath[0] = file.getAbsolutePath();
				if (file != null){
					Image newImage = new Image("file:" + file.getAbsolutePath());
					imageView.setImage(newImage);
				}
			}
		});
		Button subButton = new Button("提交");
		subButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				imageMenu.setGraphic(imageView);
				//设置新头像后，将新头像覆盖老头像
				String oldPath = "D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\" + idLabel.getText() + ".jpg";
				File file = new File(newPath[0]);
				File oldFile = new File(oldPath);
				oldFile.delete();

				file.renameTo(new File(oldPath));
				window.close();
			}
		});
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		hBox.getChildren().addAll(imageView,button);
		vBox.getChildren().addAll(hBox,subButton);

		Scene scene = new Scene(vBox);
		window.setScene(scene);
		//使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}

	//修改网名
	public void display(){
		String oldPath = "D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\" + idLabel.getText() + ".jpg";
		File file = new File(oldPath);
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.TRANSPARENT);
		window.setMinWidth(300);
		window.setMinHeight(150);
		TextField textArea = new TextField();
		textArea.setMaxHeight(50);
		textArea.setMaxWidth(200);
		Button button = new Button("修改");
		Text text = new Text();
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//判断输入是否为空
				if (!textArea.getText().isEmpty()){
					String afterChangeUserName = textArea.getText();
					//判断输入是否过长
					if (afterChangeUserName.length() <= 6 ){
						//判断数据库是否存在这个用户名
						if (select(afterChangeUserName)){
							System.out.println(textArea.getText());
							//向数据库更新用户的username
							update(afterChangeUserName);
							idLabel.setText(textArea.getText());
							String newPath = "D:\\Study\\JAVA\\idea\\Wechat\\src\\main\\resources\\" + idLabel.getText() + ".jpg";
							File newFile = new File(newPath);
							file.renameTo(newFile);
							textArea.clear();
							window.close();
						} else {
							text.setText("用户名已经存在，请更换名字");
							text.setFill(Color.RED);
							textArea.clear();
						}
					} else {
						text.setText("用户名过长，请更换名字");
						text.setFill(Color.RED);
						textArea.clear();
					}

				} else {
					text.setText("输入为空，请重新输入");
					text.setFill(Color.RED);
				}
			}
		});
		VBox layout = new VBox(10);
		layout.getChildren().addAll(text,textArea, button);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		//使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}
	//更新用户的username
	public void update(String username){
		Connection connection = MySqlDao.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("UPDATE SET username=" + username + "WHERE username=" + idLabel.getText());
			preparedStatement.executeUpdate();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 查询修改后的username是否已经在数据库存在了
	 */
	public boolean select(String username){
		Connection connection = MySqlDao.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		boolean status = true;
		try {
			statement = connection.createStatement();
			String sql = "select userName from wechat";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				if (resultSet.getString("userName").equals(username)){
					status = false;
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return status;
	}
	public void send() throws Exception{
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()){
			Listener.send(msg);
			messageBox.clear();
		}

	}
	public void setUserList(JSONObject message) throws Exception{
		Platform.runLater(() ->  {
			List<String> list = new LinkedList<>();
			try {
				JSONArray jsonArray = message.getJSONArray("List");
				for (int i = 0;i < jsonArray.length();i++){
					if (!jsonArray.getString(i).equals(UserInfo.getId(idLabel.getText()))){
						list.add(UserInfo.getUserName(jsonArray.getString(i)));
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
				box.setStyle("-fx-background-color: blue");
				final int width = 200;
				final int height = 50;
				final Scene scene = new Scene(box, width, height);
				scene.setFill(null);
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
	public void xiao(){
		LoginCon.stageL.setIconified(true);
	}
	public void tui(){
		LoginCon.stageL.close();
	}
	public void getToUser(){
		opMenu();
		userList.getSelectionModel().selectedItemProperty().addListener(
				(ChangeListener<String>) (observable, oldValue, newValue) -> {
					currentUserName.setText(newValue);
					if (oldValue != null & oldValue != newValue){
						System.out.println(oldValue + "  " + newValue);

						chatList.getItems().clear();
					}
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
		search.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
					search.setScaleX(1.5);
					//textField.setScaleY();
					search.setScaleZ(1.5);
				} else if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
					search.setScaleX(1);
					search.setScaleY(1);
					search.setScaleZ(1);
				}
			}
		});
		search.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)){
					String searchCount = search.getText();
					/**
					 * TODO 寻找这个用户名称在数据库中
					 */

				}
			}
		});

	}
}