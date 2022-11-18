package server;


import mapper.UserFriendMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.util.LinkedList;
import java.util.Set;

/**
 * 获取当前wechatId的好友id
 * */
public class UserList {
	public static LinkedList<String> getFriendList(String id){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		Set<String> userFriend = mapper.getUserFriend(id);
		return new LinkedList<>(userFriend);
	}
}
