package service;

import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

public class UserService {

	private SqlSession sqlSession;

	private UserMapper mapper;

	public UserService() {
		sqlSession = MyBatisUtil.getSqlSession();
		mapper = sqlSession.getMapper(UserMapper.class);
	}

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

	public String getWechatNameByWechatId(String wechatId) {
		try {
			return mapper.getWechatNameByWechatId(wechatId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getWechatIdByWechatName(String wechatName) {
		try {
			return mapper.getWechatNameByWechatId(wechatName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
