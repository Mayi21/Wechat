package client;

import enums.MessageTypeEnum;
import io.netty.channel.Channel;
import netty.Client;
import netty.Message;
import util.LocalContext;

public class ChatListener implements Runnable {
	private static Channel channel;

	static {
		Client client = new Client();
		try {
			channel = client.call();
			while (!channel.isActive()) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sign();
			LocalContext.getChatCon().setFriendList();
		} catch (Exception e) {
			channel = null;
			e.printStackTrace();
		}

	}

	@Override
	public void run(){
		try {
			LoginCon.getLoginCon().showScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送普通消息
	public static void send(String msg) throws Exception{
		Message message = new Message();
		message.setMessage(msg);
		message.setToUserId(ChatCon.current);
		message.setType(MessageTypeEnum.MESSAGE);
		message.setUserId(LocalContext.getWechatId());
		LocalContext.getChatCon().addChat(message);
		channel.writeAndFlush(message);
	}

	// 注册消息
	public static void sign() {
		Message message = new Message();
		message.setType(MessageTypeEnum.SIGN);
		message.setUserId(LocalContext.getWechatId());
		channel.writeAndFlush(message);
	}

	// 添加好友的消息
	public static void addFrinedForUserList(String anotherId) {
		Message message = new Message();
		message.setType(MessageTypeEnum.FRIENDLIST);
		message.setUserId(LocalContext.getWechatId());
		message.setToUserId(anotherId);
		channel.writeAndFlush(message);
	}
}