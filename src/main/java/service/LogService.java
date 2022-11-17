package service;

import entity.LogPo;
import mapper.LogMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;

public class LogService {
	public void addLog(LogPo logPo) {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		LogMapper mapper = sqlSession.getMapper(LogMapper.class);
		mapper.addLog(logPo);
		sqlSession.commit();
	}
}
