package mapper;


import entity.UserDo;
import entity.UserFriendDo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface UserFriendMapper {
	public Set<String> getUserFriend(String wechatId);

	public int getFriendStatus(@Param("wechatId") String wechatId, @Param("friendId") String friendId);

	public void updateStatus(@Param("wechatId") String wechatId, @Param("friendId") String friendId, @Param("status") int status);

	public void deleteFriend(@Param("wechatId") String wechatId);

//	public void addFriend(@Param("wechatId") String wechatId, @Param("friendId") String friendId);
	public void addFriend(UserFriendDo userFriendDo);

	public void test();
}
