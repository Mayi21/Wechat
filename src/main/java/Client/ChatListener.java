package Client;

import enums.MessageTypeEnum;
import io.netty.channel.Channel;
import netty.Client;
import netty.Message;
import util.LocalContext;

public class ChatListener implements Runnable {
	private static Channel channel;
	//这个就是登录者的ID
	private static String id;

	static {
		Client client = new Client();
		try {
			channel = client.call();
		} catch (Exception e) {
			channel = null;
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			LoginCon.getLoginCon().showScene();
		}catch (Exception e){
			e.printStackTrace();
		}
		sign();
	}

	// 发送普通消息
	public static void send(String msg) throws Exception{
		Message message = new Message();
		message.setMessage(msg);
		message.setToUserId(UserInfo.getId(ChatCon.current));
		message.setType(MessageTypeEnum.MESSAGE);
		message.setUserId(id);
		LocalContext.getChatCon().addChat(message);
		channel.writeAndFlush(message);
	}

	// 注册消息
	public void sign() {
		Message message = new Message();
		message.setType(MessageTypeEnum.SIGN);
		message.setUserId(id);
		channel.writeAndFlush(message);
	}

	// 添加好友的消息
	public static void addFrinedForUserList(String anotherId) {
		Message message = new Message();
		message.setType(MessageTypeEnum.FRIENDLIST);
		message.setUserId(id);
		message.setToUserId(anotherId);
		channel.writeAndFlush(message);
	}
}