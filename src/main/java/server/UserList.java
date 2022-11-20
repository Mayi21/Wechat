package server;


import entity.UserViewVo;
import mapper.UserFriendMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取当前wechatId的好友id
 * */
public class UserList {
	public static List<String> getFriendList(String id){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		List<UserViewVo> userFriend = mapper.getUserFriend(id);
		List<String> collect = userFriend.stream().map(u -> u.getId()).collect(Collectors.toList());
		return collect;
	}
}
