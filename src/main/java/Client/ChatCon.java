package Client;

import enums.BubbleSpecEnum;
import Client.bubble.BubbledLabel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import mapper.UserFriendMapper;
import mapper.UserMapper;
import netty.Message;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import service.UserFriendService;
import service.UserService;
import util.ImageUtil;
import util.MyBatisUtil;
import util.MySqlDao;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

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

	//将聊天信息登记到聊天列表中
	public synchronized void addChat(Message message) throws Exception {
		Task<HBox> hBoxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				String id = message.getUserId();
				Image image = new Image(ImageUtil.getImageFilePath(id + ".jpg"));
				ImageView imageView = new ImageView(image);
				imageView.setFitHeight(30);
				imageView.setFitWidth(30);
				BubbledLabel bubbledLabel = new BubbledLabel();
				bubbledLabel.setText(message.getMessage());
				bubbledLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
				HBox x = new HBox();
				x.setAlignment(Pos.TOP_RIGHT);
				bubbledLabel.setBubbleSpec(BubbleSpecEnum.FACE_RIGHT_CENTER);
				x.getChildren().addAll(bubbledLabel,imageView);
				return x;
			}
		};
		hBoxTask.setOnSucceeded(event -> {
			chatList.getItems().add(hBoxTask.getValue());
		});
		Task<HBox> hboxTask = new Task<HBox>() {
			@Override
			protected HBox call() throws Exception {
				String id = message.getUserId();
				Image image = new Image(ImageUtil.getImageFilePath(id + ".jpg"));
				ImageView imageView = new ImageView(image);
				imageView.setFitHeight(30);
				imageView.setFitWidth(30);
				BubbledLabel bubbledLabel = new BubbledLabel();
				bubbledLabel.setText(message.getMessage());
				bubbledLabel.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));
				HBox x = new HBox();
				x.setAlignment(Pos.TOP_LEFT);
				bubbledLabel.setBubbleSpec(BubbleSpecEnum.FACE_LEFT_CENTER);
				x.getChildren().addAll(imageView,bubbledLabel);
				return x;
			}
		};
		hboxTask.setOnSucceeded(event -> {
			chatList.getItems().add(hboxTask.getValue());
		});

		if (message.getUserId().equals(UserInfo.getId(idLabel.getText()))) {
			Thread thread = new Thread(hBoxTask);
			thread.setDaemon(true);
			thread.start();
		} else if (message.getUserId().equals(UserInfo.getId(currentUserName.getText()))) {
			String selfId = message.getUserId();
			String anotherId = message.getToUserId();
			if (getFriendStatus(anotherId,selfId)){
				Thread thread = new Thread(hboxTask);
				thread.setDaemon(true);
				thread.start();
			}
		}
	}
	//得到用户的好友列表中anotherId的状态
	public boolean getFriendStatus(String selfId, String anotherId){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		int friendStatus = -1;
		try {
			friendStatus = mapper.getFriendStatus(selfId, anotherId);
			if (friendStatus == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("验证之后" + friendStatus);
		return false;
	}

	//只有当选中聊天用户时，才可以出现如下的菜单按钮
	public void opMenu(){
		MenuItem menuItem1 = new MenuItem("删除好友");
		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteFriendStage();
			}
		});
		MenuItem menuItem2 = new MenuItem();
		String selfId = UserInfo.getId(idLabel.getText());
		String anotherId = UserInfo.getId(currentUserName.getText());
		if (ObjectUtils.isEmpty(anotherId)) {

		} else {
			if (getFriendStatus(selfId, anotherId)){
				menuItem2 = new MenuItem("加入黑名单");
				menuItem2.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						//首先弹出框
						addFriendToBlackListStage();
					}
				});
			} else {
				menuItem2 = new MenuItem("移除黑名单");
				menuItem2.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						//首先弹出框
						removeFriendToBlackListStage();
					}
				});
			}
			opMenu.getItems().clear();
			opMenu.getItems().addAll(menuItem1,menuItem2);
		}
	}

	//从黑名单移除用户
	public void removeFriendToBlackListStage(){
		Stage stage = new Stage();
		Text text = new Text("确认移除");
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		vBox.getChildren().addAll(text,hBox);
		Button yes = new Button("移除");
		yes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//数据库
				removeFriendToBlackListForDataBase();
				stage.close();
			}
		});
		Button no = new Button("我再想想");
		no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		hBox.getChildren().addAll(yes,no);
		Scene scene = new Scene(vBox);
		stage.setHeight(100);
		stage.setWidth(100);
		stage.setScene(scene);
		stage.showAndWait();
	}

	//添加用户到和名单的stage
	public void addFriendToBlackListStage(){
		Stage stage = new Stage();
		Text text = new Text("请认真想想");
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		vBox.getChildren().addAll(text,hBox);
		Button yes = new Button("加入");
		yes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//数据库
				addFriendToBlackListForDataBase();
				stage.close();
			}
		});
		Button no = new Button("我再想想");
		no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		hBox.getChildren().addAll(yes,no);
		Scene scene = new Scene(vBox);
		stage.setHeight(100);
		stage.setWidth(100);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void removeFriendToBlackListForDataBase(){
		String selfId = UserInfo.getId(idLabel.getText());
		String anotherId = UserInfo.getId(currentUserName.getText());
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		try {
			mapper.updateStatus(selfId, anotherId, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//把好友加入到黑名单的数据库操作
	public void addFriendToBlackListForDataBase(){
		String selfId = UserInfo.getId(idLabel.getText());
		String anotherId = UserInfo.getId(currentUserName.getText());
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		try {
			mapper.updateStatus(selfId, anotherId, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//删除好友的stage
	public void deleteFriendStage(){
		Stage stage = new Stage();
		Text text = new Text("请认真想想");
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		vBox.getChildren().addAll(text,hBox);
		Button yes = new Button("Delete");
		yes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//数据库
				deleteFriend();
				//用户列表
				ChatListener.addFrinedForUserList(UserInfo.getId(currentUserName.getText()));
				stage.close();
			}
		});
		Button no = new Button("No");
		no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		hBox.getChildren().addAll(yes,no);
		Scene scene = new Scene(vBox);
		stage.setHeight(100);
		stage.setWidth(100);
		stage.setScene(scene);
		stage.showAndWait();
	}
	//删除好友从数据库
	public void deleteFriend(){
		//删除好友在两个人的数据库中
		String selfId = UserInfo.getId(idLabel.getText());
		String anotherId = UserInfo.getId(currentUserName.getText());
		UserFriendService userFriendService = new UserFriendService();
		userFriendService.deleteUserFriend(selfId, anotherId);
	}
	//设置当前用户的名字
	public void setUserLabel(String id){
		this.idLabel.setText(UserInfo.getUserName(id));
	}
	//设置menu里面的头像
	public void setImageView(String id){
		String url = ImageUtil.getImageFilePath(id + ".jpg");
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

		String imageFilePath = ImageUtil.getImageFilePath("头像.jpg");
		Image defaultImage = new Image(imageFilePath);
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
				String s = ImageUtil.getImageFilePath(UserInfo.getId(idLabel.getText()) + ".jpg").replace("file:", "");
				File file = new File(newPath[0]);
				File oldFile = new File(s);
				oldFile.delete();
				file.renameTo(new File(s));
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

	/**
	 * 功能：修改网名
	 * 实现：从输入框获取名字后，首先判断是否为空、是否过长、是否重复
	 * 建议：没必要了，可以更改为修改wechat id，name并不唯一，这里不能这样用，后期再改吧
	 * */
	public void display(){
		String oldPath = ImageUtil.getImageFilePath(idLabel.getText() + ".jpg").replace("file:", "");
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
						//向数据库更新用户的username
						update(afterChangeUserName);
						idLabel.setText(textArea.getText());
						//早先是因为用户名需要更改头像，因为头像是和用户id相连接的
						textArea.clear();
						window.close();
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
		UserService service = new UserService();
		service.updateWechatNameByWechatId(username, UserInfo.getId(idLabel.getText()));
	}
	//判断发送框是否为空，并调用Listener的发送消息的方法
	public void send() throws Exception{
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()){
			ChatListener.send(msg);
			messageBox.clear();
		}

	}
	//设置当前用户的用户列表
	public void setUserList(JSONObject message) throws Exception{
		Platform.runLater(() ->  {
			List<String> list = new LinkedList<>();
			try {
				JSONArray jsonArray = message.getJSONArray("List");
				for (int i = 0;i < jsonArray.length();i++){
					//if (!jsonArray.getString(i).equals(UserInfo.getId(idLabel.getText()))){
					list.add(UserInfo.getUserName(jsonArray.getString(i)));
					//}
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
	//聊天列表窗口最小化
	public void xiao(){
		LoginCon.stageL.setIconified(true);
	}
	//聊天列表窗口退出
	public void tui(){
		LoginCon.stageL.close();
	}
	//得到正在聊天的用户名称
	/**
	 * TODO 得到当前的用户
	 */
	public void getToUser(){
		opMenu();
		userList.getSelectionModel().selectedItemProperty().addListener(
				(ChangeListener<String>) (observable, oldValue, newValue) -> {
					currentUserName.setText(newValue);
					if (oldValue != null & oldValue != newValue){
						chatList.getItems().clear();
						try {
//							sqladd(UserInfo.getId(newValue));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
//							sqladd(UserInfo.getId(newValue));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					ChatCon.current = newValue;
				});
	}
	//搜索用户
	public String searchUser(String searchId,String userId){
		boolean isUser = false;
		boolean isFriend = false;
		String username = null;
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT id,userName from wechat");
			while (resultSet.next()){
				if (resultSet.getString("id").equals(searchId)){
					isUser = true;
					username = resultSet.getString("userName");
					break;
				}
			}
			if (isUser){
				String table = "u" + userId;
				ResultSet newResultSet = statement.executeQuery("select * from " + table);
				while (newResultSet.next()){
					//说明用户已经有这个好友了
					if (newResultSet.getString("user").equals(searchId)){
						isFriend = true;
						break;
					}
				}
			} else {
				return "-1";
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if (isFriend & isUser){
			return "1";
		}
		if (isUser){
			return "0";
		}
		return null;
	}
	//添加B到当前的用户好友列表中,添加当前用户到B的好友列表中
	public void addFriend(String id){
		Stage stage = new Stage();
		Text text = new Text(UserInfo.getUserName(id));
		Text text1 = new Text(id);
		Button button = new Button("加好友");
		button.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//向本用户的好友列表中添加此用户
				UserFriendService userFriendService = new UserFriendService();
				userFriendService.addUserFriend(UserInfo.getId(idLabel.getText()), id);
				stage.close();
			}
		});
		HBox hBox = new HBox();
		hBox.getChildren().addAll(text,text1,button);
		Scene scene = new Scene(hBox,200,75);
		stage.setScene(scene);
		stage.showAndWait();
	}
	//搜索好友已经是你好友的弹框
	public void searchThisUserIsYourFriend(){
		Stage stage = new Stage();
		HBox hBox = new HBox();
		Text text = new Text("此人已是你的好友");
		text.setFill(Color.RED);
		hBox.getChildren().add(text);
		Scene scene = new Scene(hBox);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setWidth(200);
		stage.setHeight(50);
		stage.setScene(scene);
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
	}
	//搜索好友时此人不存在的弹框
	public void searchNoThisUserStage(){
		Stage stage = new Stage();
		HBox hBox = new HBox();
		Text text = new Text("此人不在");
		text.setFill(Color.RED);
		hBox.getChildren().add(text);
		Scene scene = new Scene(hBox);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setWidth(200);
		stage.setHeight(50);
		stage.setScene(scene);
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
	}
	//用户聊天的历史记录
//	public void sqladd(String toid) throws Exception {
//		String sendid = UserInfo.getId(idLabel.getText());
//		Connection connection = MySqlDao.getConnection();
//		Connection connection1 = MySqlDao.getConnection();
//		Statement statement = connection.createStatement();
//		Statement statement1 = connection1.createStatement();
//		ResultSet resultSet = null;
//		ResultSet resultSet1 = null;
//		String sql = "SELECT log,time FROM log WHERE sendid=" + sendid + " AND toid=" + toid +" ORDER BY time DESC";
//		String sql1 = "SELECT log,time FROM log WHERE sendid=" + toid + " AND toid=" + sendid +" ORDER BY time DESC";
//		resultSet = statement.executeQuery(sql);
//		resultSet1 = statement1.executeQuery(sql1);
//		TreeMap<String ,JSONObject> recive = new TreeMap<>();
//		int i = 0;
//		while (resultSet.next() && i<6){
//			String time = resultSet.getString("time");
//			String log = resultSet.getString("log");
//			JSONObject jsonObject = new JSONObject();
//			try {
//				jsonObject.put("ToId",toid);
//				jsonObject.put("List",new JSONArray());
//				jsonObject.put("MessageType","CHAT");
//				jsonObject.put("Message",log);
//				jsonObject.put("SendId",sendid);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			recive.put(time,jsonObject);
//			i++;
//		}
//		int o = 0;
//		while (resultSet1.next() && o<6){
//			String time = resultSet1.getString("time");
//			String log = resultSet1.getString("log");
//			JSONObject jsonObject = new JSONObject();
//			try {
//				jsonObject.put("ToId",sendid);
//				jsonObject.put("List",new JSONArray());
//				jsonObject.put("MessageType","CHAT");
//				jsonObject.put("Message",log);
//				jsonObject.put("SendId",toid);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			recive.put(time,jsonObject);
//			o++;
//		}
//		recive.descendingMap();
//		int q = 0;
//		Iterator iterator= recive.keySet().iterator();
//		System.out.println(recive.size());
//		while (iterator.hasNext()&&q<6){
//			String key = (String) iterator.next();
//			addChat(recive.get(key));
//			q++;
//		}
//	}
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
					if (!searchCount.isEmpty()){
						String s = searchUser(searchCount,UserInfo.getId(idLabel.getText()));
						if (s.equals("0") & !searchCount.equals(UserInfo.getId(idLabel.getText()))){
							//表示此人不是你的好友,进行添加好友
							//数据库操作，将两人添加到对方的数据库
							addFriend(searchCount);
							//当前用户的userlist的操作
							ChatListener.addFrinedForUserList(searchCount);
							search.clear();
						} else if (s.equals("-1")){
							//表示此人不存在
							searchNoThisUserStage();
							search.clear();
						} else {
							//表示此人是你的好友
							searchThisUserIsYourFriend();
							search.clear();
						}
					}else {
						//不做反应
					}
				}
			}
		});
		messageSendButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					send();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}