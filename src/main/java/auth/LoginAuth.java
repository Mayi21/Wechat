package auth;

import entity.UserDo;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import service.UserService;
import util.MyBatisUtil;

import java.util.Objects;

/**
 * 用户登录判断
 * */
public class LoginAuth {

	/**
	 * 检查账户和密码是否正确
	 * */
	public static boolean check(String account, String passwd){
		UserService service = new UserService();
		UserDo user = service.getUserById(account);
		if (Objects.isNull(user)) {
			return false;
		} else {
			return passwd.equals(user.getPasswd());
		}
	}
}
