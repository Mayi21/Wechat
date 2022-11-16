package Client;

import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class UserInfo {
	public static String getId(String username){
		String id = null;
		Connection connection = MySqlDao.getConnection();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT id from wechat where userName='" + username +"'");
			while (resultSet.next()){
				id = resultSet.getString("id");
			}

		} catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}
	public static String getUserName(String id){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		String wechatNameByWechatId = mapper.getWechatNameByWechatId(id);
		if (Objects.isNull(wechatNameByWechatId)) {
			return "";
		} else {
			return wechatNameByWechatId;
		}
	}
}
