package mapper;


import entity.LogPo;
import entity.UserFriendDo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface LogMapper {

	public void addLog(LogPo logPo);

}
