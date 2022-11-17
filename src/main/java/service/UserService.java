package service;

import entity.UserDo;
import entity.UserFriendDo;
import mapper.UserFriendMapper;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

public class UserService {
	public static void main(String[] args) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
//		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//		UserDo user = userMapper.getUserByUserName("xiaoli");
//		if (Objects.isNull(user)) {
//			System.out.println("查无此人");
//		} else {
//			System.out.println(user.toString());
//		}

		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		mapper.addFriend(new UserFriendDo("xiao", "xaoo"));
		sqlSession.commit();
	}

	public void update(UserDo userDo) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		mapper.updatePasswdByWechatId("12345", "xaohii");
		sqlSession.commit();
	}

}
