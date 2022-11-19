package Client;

import mapper.UserMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import service.UserService;
import util.MyBatisUtil;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class UserInfo {

	public static String getId(String username){
		UserService service = new UserService();
		return service.getWechatIdByWechatName(username);
	}

	public static String getUserName(String id){
		return new UserService().getWechatNameByWechatId(id);
	}
}
