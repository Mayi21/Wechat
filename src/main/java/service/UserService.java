package service;


import entity.UserDo;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

public class UserService {
	public static void main(String[] args) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		UserDo user = userMapper.getUserByUserName("xiaoming");
		System.out.println(user.toString());
	}

}
