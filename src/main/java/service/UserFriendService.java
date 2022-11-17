package service;

import entity.UserFriendDo;
import mapper.UserFriendMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

public class UserFriendService {
	public void deleteUserFriend(String wechatId, String friendId) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		mapper.deleteFriend(wechatId);
		mapper.deleteFriend(friendId);
		sqlSession.commit();
	}

	public void addUserFriend(String wechatId, String friendId) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		mapper.addFriend(new UserFriendDo(wechatId, friendId));
		mapper.addFriend(new UserFriendDo(friendId, wechatId));
		sqlSession.commit();
	}
}
