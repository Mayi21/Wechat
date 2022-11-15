package mapper;


import entity.UserDo;

import java.util.Set;

public interface UserFriendMapper {
	public Set<String> getUserFriend(String wechatId);
}
