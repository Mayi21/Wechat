package Client;

import entity.UserDo;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

/**
 * 用户登录判断
 * */
public class Check {
	private static String id;
	private static String pa;
	public static String userName;

	/**
	 * 检查账户和密码是否正确
	 * */
	public static boolean check(String account, String passwd){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		UserDo userByUserName = mapper.getUserByWechatId(account);
		if (Objects.isNull(userByUserName)) {
			return false;
		} else {
			if (passwd.equals(userByUserName.getPasswd())) {
				id = account;
				pa = passwd;
				return true;
			} else {
				return false;
			}
		}
	}
}
