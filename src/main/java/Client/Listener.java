package Client;

import io.netty.handler.codec.json.JsonObjectDecoder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Listener implements Runnable {
	private String server;
	private String port;
	private static String id;
	private static String currentId;
	private Socket socket;
	public static ChatCon control;
	private InputStream inputStream;
	private static OutputStream outputStream;
	private ObjectInputStream objectInputStream;
	private static ObjectOutputStream objectOutputStream;
	public Listener(String server,String port,String id,ChatCon control){
		Listener.id = id;
		this.port = port;
		this.server = server;
		Listener.control = control;
	}
	@Override
	public void run(){
		try {
			LoginCon.getLoginCon().showScene();
			socket = new Socket(server, Integer.parseInt(port));
			outputStream =socket.getOutputStream();
			inputStream = socket.getInputStream();
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			connect();
			while (socket.isConnected()){
				byte[] bytes = new byte[1024];
				int len = inputStream.read(bytes);
				JSONObject jsonObject = new JSONObject(new String(bytes,0,len));
				String message = jsonObject.getString("Message");
				 if (message != null){
				 	switch (jsonObject.getString("MessageType")){
						case "CHAT":
							control.addChat(jsonObject);
							break;
						case "NOTIFICATION":
							control.setUserList(jsonObject);
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
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ToId",ChatCon.current);
		jsonObject.put("List",new JSONArray());
		jsonObject.put("MessageType","CHAT");
		jsonObject.put("Message",msg);
		jsonObject.put("SendId",id);
		control.addChat(jsonObject);
		byte[] bytes = jsonObject.toString().getBytes();
		outputStream.write(bytes);
	}
	public static void connect() throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("SendId",id);
		jsonObject.put("Message","");
		jsonObject.put("MessageType","STATUS:ONLINE");
		jsonObject.put("ToId","");
		jsonObject.put("List",new JSONArray());
		byte[] bytes = jsonObject.toString().getBytes();
		outputStream.write(bytes);
	}
}