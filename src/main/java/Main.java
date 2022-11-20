import entity.UserViewVo;
import service.UserFriendService;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		UserFriendService service = new UserFriendService();
		List<UserViewVo> xaohii = service.getFriendIdAndName("xaohii");
		System.out.println(xaohii.toString());

	}
}
