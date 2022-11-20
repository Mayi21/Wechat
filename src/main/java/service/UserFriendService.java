package service;

import entity.UserFriendDo;
import entity.UserViewVo;
import mapper.UserFriendMapper;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.util.List;
import java.util.Map;

public class UserFriendService {
	private SqlSession sqlSession;

	private UserFriendMapper mapper;

	public UserFriendService() {
		sqlSession = MyBatisUtil.getSqlSession();
		mapper = sqlSession.getMapper(UserFriendMapper.class);
	}
	public void deleteUserFriend(String wechatId, String friendId) {
		mapper.deleteFriend(wechatId);
		mapper.deleteFriend(friendId);
		sqlSession.commit();
	}

	public void addUserFriend(String wechatId, String friendId) {
		mapper.addFriend(new UserFriendDo(wechatId, friendId));
		mapper.addFriend(new UserFriendDo(friendId, wechatId));
		sqlSession.commit();
	}

	public List<UserViewVo> getFriendIdAndName(String id) {
		return mapper.getUserFriend(id);
	}
}
