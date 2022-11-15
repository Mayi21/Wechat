package mapper;


import entity.UserDo;

public interface UserMapper {
	public UserDo getUserByUserName(String userName);
}
