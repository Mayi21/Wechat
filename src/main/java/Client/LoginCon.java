package Client;

import auth.LoginAuth;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import util.LocalContext;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCon implements Initializable {
	@FXML private Button loginButton;
	@FXML private TextField userId;
	@FXML private PasswordField passwd;
	@FXML private TextField port;
	@FXML private TextField server;
	@FXML public static ImageView loginImage;
	public ChatCon control;
	private Scene scene;
	public static LoginCon loginCon;
	public static String userName;
	public static Stage stageL;

	public LoginCon(){
		loginCon = this;
	}
	public static LoginCon getLoginCon(){
		return loginCon;
	}
	public void login() throws Exception{
		String id = userId.getText();
		String pa = passwd.getText();
		if (LoginAuth.check(id,pa)) {
			userName = LoginAuth.userName;
			FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("fxml/ChatList.fxml"));
			Parent parent = (Pane) fxmlLoader.load();
			control = fxmlLoader.<ChatCon>getController();
			LocalContext.setChatCon(control);
			LocalContext.setWechatId(userId.getText());
			new Thread(new ChatListener()).start();
			this.scene = new Scene(parent);
		} else {
			dong();
			passwd.clear();
		}
	}
	public void xiao(){
		Login.getP().setIconified(true);
	}
	public void tui(){
		Login.getP().close();
	}
	public void showScene() throws IOException {
		Platform.runLater(() -> {
			stageL = (Stage) port.getScene().getWindow();
			Stage stage = stageL;
			stage.setResizable(true);
			stage.setOnCloseRequest((WindowEvent e) -> {
				Platform.exit();
				System.exit(0);
			});
			this.scene.getStylesheets().add(LoginCon.class.getResource("Chat.css").toExternalForm());
			stage.setScene(this.scene);
			stage.setMinWidth(960);
			stage.setMinHeight(540);
			ResizeHelper.addResizeListener(stage);
			stage.centerOnScreen();
			control.setUserLabel(userId.getText());
			control.setImageView(userId.getText());
		});
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	public void dong(){
		//创建一个路径对象
		Path path = new Path();
		double x=loginButton.getLayoutX() - 100;
		double y=loginButton.getLayoutY() - 87;
		//从哪个位置开始动画，一般来说给组件的默认位置就行
		path.getElements().add(new MoveTo(x, y));
		//添加一个向左移动的路径
		path.getElements().add(new LineTo(x-20, y));
		//添加一个向右移动的路径  这样就完成第一遍摇头
		path.getElements().add(new LineTo(x+20, y));
		//添加一个向左移动的路径 第二遍
		path.getElements().add(new LineTo(x-20, y));
		//添加一个向右移动的路径  这样就完成第二遍摇头
		path.getElements().add(new LineTo(x+20, y));
		//最后移动到原来的位置
		path.getElements().add(new LineTo(x, y));
		//创建一个动画对象
		PathTransition pathTransition = new PathTransition();
		//动画持续时间 0.5秒
		pathTransition.setDuration(Duration.seconds(0.5));
		//把我们设置好的动画路径放入里面
		pathTransition.setPath(path);
		//给动画添加组件，让某个组件来完成这个动画
		pathTransition.setNode(loginButton);
		//执行1遍
		pathTransition.setCycleCount(1);
		//执行动画
		pathTransition.play();
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
