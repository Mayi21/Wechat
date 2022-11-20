package util;

import client.ChatCon;

public class LocalContext {

	private static ChatCon chatCon;

	private static String wechatId;

	private static String wechatName;

	public static ChatCon getChatCon() {
		return chatCon;
	}

	public static String getWechatId() {
		return wechatId;
	}

	public static String getWechatName() {
		return wechatName;
	}

	public static void setChatCon(ChatCon chatCon) {
		LocalContext.chatCon = chatCon;
	}

	public static void setWechatId(String wechatId) {
		LocalContext.wechatId = wechatId;
	}

	public static void setWechatName(String wechatName) {
		LocalContext.wechatName = wechatName;
	}
}
