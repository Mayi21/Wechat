package Server;

import Client.User;

import java.util.ArrayList;

public class UserList {
	private static ArrayList<User> list;
	public static ArrayList<User> getList() {
		return list;
	}

	public static void setList(ArrayList<User> list) {
		UserList.list = list;
	}
}
