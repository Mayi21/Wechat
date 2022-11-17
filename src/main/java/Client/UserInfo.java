package Client;

import mapper.UserMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import util.MySqlDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class UserInfo {
	public static String getId(String username){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		String wechatIdByWechatName = mapper.getWechatIdByWechatName(username);
		if (ObjectUtils.isEmpty(wechatIdByWechatName)) {
			return "";
		} else {
			return wechatIdByWechatName;
		}
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
