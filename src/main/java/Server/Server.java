package Server;

import Client.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MySqlDao;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Server {
	private static LinkedList<String> list = new LinkedList<>();
	private static HashMap<String, OutputStream> map = new HashMap<>();
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(8888);
		try {
			while (true) {
				new Start(serverSocket.accept()).start();
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}
	private static class Start extends Thread {
		private static InputStream inputStream;
		private static OutputStream outputStream;
		private Socket socket;
		private String id;
		public Start(Socket socket) throws Exception {
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				JSONObject jsonObject = get(inputStream);
				id = jsonObject.getString("SendId");
				System.out.println(id);
				list = UserList.getFriendList(id);
				System.out.println(new JSONArray(list));
				map.put(id, outputStream);
				jsonObject.put("List",new JSONArray(list));
				jsonObject.put("MessageType","NOTIFICATION");
				jsonObject.put("Message","您的好友" + UserInfo.getUserName(id) + "已经上线!");
				jsonObject.put("SendId",id);
				jsonObject.put("ToId","server");
				notificationAll(jsonObject);
				while (socket.isConnected()) {
					System.out.println(Thread.currentThread());
					JSONObject inputMessage = get(socket.getInputStream());
					System.out.println(inputMessage);
					if (inputMessage != null) {
						String type = inputMessage.getString("MessageType");
						switch (type) {
							case "CHAT":
								sendMessage(inputMessage);
								sqllog(inputMessage);
								break;
							case "UPDATEUSERLIST":
								//要向自身发送更新消息
								notificationSelf(inputMessage);
								break;

							default:
						}
					} else {
						System.out.println(inputMessage.getString("ToId"));
					}
				}
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				if (inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null){
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public JSONArray getJsonArray(ArrayList<String> list){
			JSONArray jsonArray = new JSONArray(list);
			return jsonArray;
		}
		public JSONObject get(InputStream inputStream) throws Exception{
			byte[] bytes = new byte[4096];
			int len = inputStream.read(bytes);
			JSONObject jsonObject = new JSONObject(new String(bytes,0,len));
			return jsonObject;
		}
		private void sendMessage (JSONObject message) throws Exception {
			String toId = message.getString("ToId");
			String sendId = message.getString("SendId");
			if (map.containsKey(toId)) {
				System.out.println("sendMessage");
				OutputStream o = map.get(toId);
				o.write(message.toString().getBytes());
				o.flush();
			}
		}
		private void notificationAll (JSONObject message) throws Exception {
			String sendId = message.getString("SendId");
			if (map.containsKey(sendId)){
				OutputStream outputStream = map.get(sendId);
				outputStream.write(message.toString().getBytes());
				outputStream.flush();
			}
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
		public void notificationSelf(JSONObject message){
			try {
				String selfId = message.getString("SendId");
				String anoherId = message.getString("ToId");
				if (map.containsKey(anoherId)){
					LinkedList<String> linkedList1 = UserList.getFriendList(anoherId);
					JSONObject newMessage1 = new JSONObject();
					newMessage1.put("SendId","");
					newMessage1.put("ToId","");
					newMessage1.put("MessageType","NOTIFICATION");
					newMessage1.put("List",new JSONArray(linkedList1));
					newMessage1.put("Message","");
					OutputStream outputStream1 = map.get(anoherId);
					outputStream1.write(newMessage1.toString().getBytes());
					outputStream1.flush();
				}
				LinkedList<String> linkedList = UserList.getFriendList(selfId);
				JSONObject newMessage = new JSONObject();
				newMessage.put("SendId","");
				newMessage.put("ToId","");
				newMessage.put("MessageType","NOTIFICATION");
				newMessage.put("List",new JSONArray(linkedList));
				newMessage.put("Message","");
				OutputStream outputStream = map.get(selfId);
				outputStream.write(newMessage.toString().getBytes());
				outputStream.flush();
			} catch (Exception e){
				System.out.println("Class:Server,Method:notificationSelf,Message:\n" + e.getMessage());
			}
		}

	}
}
