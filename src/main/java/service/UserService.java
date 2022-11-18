package service;

import entity.UserDo;
import entity.UserFriendDo;
import mapper.UserFriendMapper;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

import java.lang.ref.PhantomReference;

public class UserService {

	private SqlSession sqlSession;

	private UserMapper mapper;

	public UserService() {
		sqlSession = MyBatisUtil.getSqlSession();
		mapper = sqlSession.getMapper(UserMapper.class);
	}
//	public static void main(String[] args) {
//		mapper.addFriend(new UserFriendDo("xiao", "xaoo"));
//		sqlSession.commit();
//	}

	public void updateWechatNameByWechatId(String wechatName, String wechatId) {
		try {
			mapper.updateWechatNameByWechatId(wechatName, wechatId);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePasswdByWechatId(String passwd, String wechatId) {
		try {
			mapper.updatePasswdByWechatId(passwd, wechatId);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
