package Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Root extends Application {
	public static void main(String[] args){
		launch(args);
	}
	@Override
	public void start(Stage primyStage) throws Exception{
		Button button1 = new Button("Hello");
		button1.setOnAction(event -> {
			button1.setText("Hello i am fx");
		});
		Pane pane = new BorderPane();
		BorderPane borderPane = new BorderPane();

		borderPane.setCenter(button1);
		Scene scene = new Scene(borderPane, 500, 500);
		primyStage.setScene(scene);
		primyStage.setTitle("First");
		primyStage.show();
	}
}