package Server;

import Client.Message;
import Client.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
	private static ArrayList<User> list = new ArrayList<>();
	private static HashMap<String, ObjectOutputStream> map = new HashMap<>();
	public static HashSet<ObjectOutputStream> set = new HashSet<>();

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
		private static ObjectOutputStream objectOutputStream;
		private static InputStream inputStream;
		private static ObjectInputStream objectInputStream;
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
				objectInputStream = new ObjectInputStream(inputStream);
				outputStream = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(outputStream);
				Message message = (Message) objectInputStream.readObject();
				if (message.getMessageType().split(":")[0].equals("STATUS") & message.getMessageType().split(":")[1].equals("ONLINE")) {
					if (UserList.getList() == null) {
						UserList.setList(new ArrayList<>());
					}
					list = UserList.getList();
					//拿到发送人的id
					id = message.getSendId();
					User user = new User();
					//设置user的属性
					user.setId(id);
					user.setStatus("ONLINE");
					//添加这个用户在线
					list.add(user);
					//用于用户对用户发送信息时，进行寻址
					map.put(id, objectOutputStream);
					//把消息发给在线的所有人
					message.setList(list);
					message.setMessageType("NOTIFICATION");
					message.setMessage("你的好友" + message.getSendId() + "已经上线！");
					message.setSendId(null);
					set.add(objectOutputStream);
					System.out.println(set.size());
					notificationAll(message);
					//更新在线的用户
					UserList.setList(list);
				}
				while (socket.isConnected()) {
					System.out.println("isconnected");
					System.out.println(Thread.currentThread());
					Message inputMessage = (Message) objectInputStream.readObject();
					if (inputMessage != null & inputMessage.getToId() != null) {
						String type = inputMessage.getMessageType();
						System.out.println("send:" + inputMessage.getSendId() + "to:" + inputMessage.getToId());
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
						System.out.println(inputMessage.getToId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (objectInputStream != null){
					try {
						objectInputStream.close();
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
				if (objectOutputStream != null){
					try {
						objectOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		private void sendMessage (Message message) throws Exception {
			String toId = message.getToId();
			if (map.containsKey(toId)) {
				ObjectOutputStream o = map.get(toId);
				o.writeObject(message);
				o.reset();
			}
		}
		private void notificationAll (Message message) throws Exception {
			for (ObjectOutputStream o : set) {
				o.writeObject(message);
				o.reset();
			}
		}
	}
}
