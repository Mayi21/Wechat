package Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OneButton extends Application {
	public static void main(String[] args){
		launch(args);
	}
	@Override
	public void start(Stage primyStage){
		StackPane pane = new StackPane();
		pane.getChildren().add(new Button("OK"));
		Scene scene = new Scene(pane, 200, 50);
		primyStage.setTitle("Button");
		primyStage.setScene(scene);
		primyStage.show();
	}
}
