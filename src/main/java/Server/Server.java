package Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
	private static ArrayList<String> list = new ArrayList<>();
	private static HashMap<String, OutputStream> map = new HashMap<>();
	public static HashSet<OutputStream> set = new HashSet<>();
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
				if (UserList.getList() == null) {
					UserList.setList(new ArrayList<>());
				}
				id = jsonObject.getString("SendId");
				list = UserList.getList();
				list.add(id);
				map.put(id, outputStream);
				set.add(outputStream);
				jsonObject.put("List",new JSONArray(list));
				jsonObject.put("MessageType","NOTIFICATION");
				jsonObject.put("Message","");
				jsonObject.put("SendId","");
				jsonObject.put("ToId","");
				notificationAll(jsonObject);
				UserList.setList(list);
				while (socket.isConnected()) {
					System.out.println(Thread.currentThread());
					JSONObject inputMessage = get(socket.getInputStream());
					System.out.println(inputMessage);
					if (inputMessage != null) {
						String type = inputMessage.getString("MessageType");
						switch (type) {
							case "CHAT":
								sendMessage(inputMessage);
								break;
							case "DISCONNECT":
								/**
								 * TODO 应该是注销用户
								 */
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
			if (map.containsKey(toId)) {
				OutputStream o = map.get(toId);
				o.write(message.toString().getBytes());
				o.flush();
			}
		}
		private void notificationAll (JSONObject message) throws Exception {
			for (OutputStream o : set) {
				byte[] bytes = message.toString().getBytes();
				o.write(bytes);
				o.flush();
			}
		}

	}
}
