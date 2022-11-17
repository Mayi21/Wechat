package mapper;


import entity.UserDo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface UserMapper {
	public UserDo getUserByWechatId(String userName);

	public Set<String> getAllWechatId();

	public String getWechatNameByWechatId(String wechatId);

	public void updatePasswdByWechatId(@Param("passwd") String passwd, @Param("wechatId") String wechatId);
	public void updateWechatNameByWechatId(@Param("wecahtName") String wechatName, @Param("wechatId") String wechatId);
}
