package Server;
import Client.Message;
import Client.User;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTest {
	public static void main(String[] args) throws Exception{
		setConnect();
	}
	public static void setConnect() throws Exception{
		ServerSocket serverSocket = new ServerSocket(8888);
		Socket socket = serverSocket.accept();
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream  = new ObjectInputStream(inputStream);
		Message message = (Message)objectInputStream.readObject();
		if (message.getMessageType().equals("STATUS")){
			/**
			 * TODO 知道了message.getId()上线，并且通知所有人；更新userList表；
			 */
			/**
			 * TODO 通知信息有：用户登陆上后，通知服务器 STATUS ；服务接收到登陆消息，通知所有在线用户 NOTIFICATION ；
			 */
			ArrayList<User> list = message.getList();
			User user = new User();
			user.setId(message.getSendId());
			user.setStatus("ONLINE");
			list.add(user);
			message.setList(list);
		}
		ObjectOutputStream objectOutputStream = null;
		boolean t = false;
		while (true){
			message = (Message) objectInputStream.readObject();
			System.out.println("server 收到:" + message.getMessage() + message.getSendId());
			Thread.sleep(1000 * 1);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			Message message1 = new Message();
			message1.setSendId("1111");
			message1.setMessage(message.getMessage());
			objectOutputStream.writeObject(message1);
			objectOutputStream.flush();
		}
	}
}