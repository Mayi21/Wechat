package client;

import service.UserService;

public class UserInfo {

	public static String getId(String username){
		UserService service = new UserService();
		return service.getWechatIdByWechatName(username);
	}

	public static String getUserName(String id){
		return new UserService().getWechatNameByWechatId(id);
	}
}
