package Server;


import mapper.UserFriendMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class UserList {
	public static LinkedList<String> getFriendList(String id){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserFriendMapper mapper = sqlSession.getMapper(UserFriendMapper.class);
		Set<String> userFriend = mapper.getUserFriend(id);
		return new LinkedList<>(userFriend);
	}
}
