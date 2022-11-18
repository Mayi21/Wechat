package util;

import Client.ChatCon;

public class LocalContext {

	private static ChatCon chatCon;

	public static ChatCon getChatCon() {
		return chatCon;
	}

	public static void setChatCon(ChatCon chatCon) {
		LocalContext.chatCon = chatCon;
	}


}
