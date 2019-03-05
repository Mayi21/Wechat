package Server;

import Client.Message;
import Client.User;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
		while (true) {
			new Start(serverSocket.accept()).start();
		}
	}
	private static class Start extends Thread {
		private static ObjectOutputStream objectOutputStream;
		private static InputStream inputStream;
		private static ObjectInputStream objectInputStream;
		private static OutputStream outputStream;
		private Socket socket;
		private String id;
		public Start(Socket socket) {
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
					try {
						list.add(user);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//用于用户对用户发送信息时，进行寻址
					System.out.println(message.getSendId());
					map.put(id, objectOutputStream);
					System.out.println(map.containsKey("123456"));
					//把消息发给在线的所有人
					message.setList(list);
					message.setMessageType("NOTIFICATION");
					message.setMessage("你的好友" + message.getSendId() + "已经上线！");
					message.setSendId(null);
					set.add(objectOutputStream);
					notificationAll(message);
					//更新在线的用户
					UserList.setList(list);
				}
				while (socket.isConnected()) {
					Message inputMessage = (Message) objectInputStream.readObject();
					if (inputMessage != null) {
						String type = inputMessage.getMessageType();
						System.out.println("send:" + inputMessage.getSendId() + "to:" + inputMessage.getToId());
						System.out.println(type);
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
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		private void sendMessage (Message message) throws Exception {
			String toId = message.getToId();
			if (map.containsKey(toId)) {
				map.get(toId).writeObject(message);
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
