package Client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Listener implements Runnable {
	private String server;
	private String port;
	private static String id;
	private static String currentId;
	private Socket socket;
	public ChatCon control;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectInputStream objectInputStream;
	private static ObjectOutputStream objectOutputStream;
	public Listener(String server,String port,String id,ChatCon control){
		Listener.id = id;
		this.port = port;
		this.server = server;
		this.control = control;
	}
	@Override
	public void run(){
		try {
			LoginCon.getLoginCon().showScene();
			socket = new Socket(server, Integer.parseInt(port));
			outputStream =socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
			inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			connect();
			while (socket.isConnected()){
				Message message = null;
				 message = (Message)objectInputStream.readObject();
				 if (message != null){
				 	switch (message.getMessageType()){
						case "CHAT":
							control.addChat(message);
							break;
						case "NOTIFICATION":
							control.setUserList(message);
							break;
						default:
					}
				 } else {
				 	System.out.println("message is null");
				 }
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void send(String msg) throws Exception{
		Message message = new Message();
		message.setToId(ChatCon.current);
		message.setList(null);
		message.setMessageType("CHAT");
		message.setMessage(msg);
		message.setSendId(id);
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
	}

	public static void connect() throws Exception{
		Message message = new Message();
		message.setSendId(id);
		message.setMessage(null);
		message.setMessageType("STATUS:ONLINE");
		message.setToId(null);
		message.setList(null);
		objectOutputStream.writeObject(message);
	}
}
