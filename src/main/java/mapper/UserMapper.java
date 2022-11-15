package mapper;


import entity.UserDo;

import java.util.Set;

public interface UserMapper {
	public UserDo getUserByWechatId(String userName);

	public Set<String> getAllWechatId();
}
