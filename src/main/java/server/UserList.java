package server;


import mapper.UserFriendMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 获取当前wechatId的好友id
 * */
public class UserList {
	public static LinkedList<String> getFriendList(String id){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		Map<String, String> userFriend = mapper.getUserFriend(id);
		return new LinkedList<>(userFriend.keySet());
	}
}
