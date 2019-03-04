package Client;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.Socket;

public class Temp {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost",8888);
		if (socket.isConnected()) {
			JOptionPane.showMessageDialog(null, "有客户端连接到本地端口8888");

		}


	}
}