package util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatisUtil {

	// 定义 SqlSessionFactory
	private static SqlSessionFactory factory;

	// 使用静态块只创建一次 SqlSessionFactory
	static {
		try {
			// 读取配置文件
			InputStream in = Resources.getResourceAsStream("config/mybatis-config.xml");
			// 创建 SqlSessionFactory 对象
			factory = new SqlSessionFactoryBuilder().build(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取 SqlSession 对象
	public static SqlSession getSqlSession() {
		SqlSession sqlSession = factory.openSession();
		return sqlSession;
	}

	// 提交事务
	public static void commit(SqlSession sqlSession) {
		if (null != sqlSession) {
			sqlSession.commit();
		}
		close();
	}

	// 回滚事务
	public static void rollBack(SqlSession sqlSession) {
		if (null != sqlSession) {
			sqlSession.rollback();
		}
		close();
	}

	// 关闭 SqlSession
	public static void close() {
		SqlSession sqlSession = getSqlSession();
		if (null != sqlSession) {
			sqlSession.close();
		}
	}
}