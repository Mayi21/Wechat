package Client;

public class Check {
	private static final String a = "123456";
	private static final String b = "123456";
	private static final String userName = "xi";
	public static boolean check(String account, String passwd){
		if (a.equals(account) && b.equals(passwd)){
			return true;
		} else {
			return false;
		}
	}

	public static String getA() {
		return a;
	}
	public static String getUserName() {
		return userName;
	}
}
