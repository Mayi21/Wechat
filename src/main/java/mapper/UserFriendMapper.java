package mapper;


import entity.UserDo;
import entity.UserFriendDo;
import entity.UserViewVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserFriendMapper {
	public List<UserViewVo> getUserFriend(String wechatId);

	public int getFriendStatus(@Param("wechatId") String wechatId, @Param("friendId") String friendId);

	public void updateStatus(@Param("wechatId") String wechatId, @Param("friendId") String friendId, @Param("status") int status);

	public void deleteFriend(@Param("wechatId") String wechatId);

//	public void addFriend(@Param("wechatId") String wechatId, @Param("friendId") String friendId);
	public void addFriend(UserFriendDo userFriendDo);

	public void test();
}
