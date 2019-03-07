package Server;


import java.util.ArrayList;

public class UserList {
	private static ArrayList<String> list;
	public static ArrayList<String> getList() {
		return list;
	}

	public static void setList(ArrayList<String> list) {
		UserList.list = list;
	}
}
