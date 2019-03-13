package Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MySqlDao;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Listener implements Runnable {
	private String server;
	private String port;
	//这个就是登录者的ID
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
				byte[] bytes = new byte[4096];
				int len = inputStream.read(bytes);
				JSONObject jsonObject = new JSONObject(new String(bytes,0,len));
				String message = jsonObject.getString("Message");
				 if (message != null & jsonObject.getString("ToId") != null ){
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
		jsonObject.put("ToId",UserInfo.getId(ChatCon.current));
		jsonObject.put("List",new JSONArray());
		jsonObject.put("MessageType","CHAT");
		jsonObject.put("Message",msg);
		jsonObject.put("SendId",id);
		control.addChat(jsonObject);
		sqllog(jsonObject);
		byte[] bytes = jsonObject.toString().getBytes();
		outputStream.write(bytes);
	}
	public static void sqllog(JSONObject message) throws JSONException {
		Connection connection = MySqlDao.getConnection();
		PreparedStatement statement = null;
		String sql = "INSERT INTO log (log,sendid,toid,time) values (?,?,?,?)";
		String log = message.getString("Message");
		String sendid = message.getString("SendId");
		String toid = message.getString("ToId");
		String time;
		time = String.valueOf(System.currentTimeMillis());
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, log);
			statement.setString(2, sendid);
			statement.setString(3, toid);
			statement.setString(4, time);
			statement.executeUpdate();
			System.out.println("sql success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void connect() throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("SendId",id);
		jsonObject.put("Message","");
		jsonObject.put("MessageType","STATUS:ONLINE");
		jsonObject.put("ToId","");
		jsonObject.put("List",new JSONArray());
		//byte[] bytes =
		outputStream.write(jsonObject.toString().getBytes());
	}
	public static void addFrinedForUserList(String anotherId){
		try {
			JSONObject message = new JSONObject();
			message.put("MessageType","UPDATEUSERLIST");
			message.put("SendId",id);
			message.put("ToId",anotherId);
			message.put("Message","");
			message.put("List",new JSONArray());
			outputStream.write(message.toString().getBytes());
		} catch (Exception e){

		}
	}
}