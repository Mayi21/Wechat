package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener implements Runnable {
	private String server;
	private String port;
	private static String id;
	private Socket socket;
	public ChatCon control;
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
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			connect();
			while (socket.isConnected()){
				 Message message = (Message)objectInputStream.readObject();
				 if (message != null){
				 	control.addChat(message);
				 } else {
				 	System.out.println("message is null");
				 }
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void send(Message message) throws Exception{
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
	}
	public static void connect() throws Exception{
		Message message = new Message();
		message.setSendId(id);
		message.setMessage(null);
		message.setMessageType("STATUS");
		message.setToId(null);
		objectOutputStream.writeObject(message);
	}
}
