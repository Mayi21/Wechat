package test.service; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import service.UserFriendService;

/** 
* UserFriendService Tester. 
* 
* @author <Authors name> 
* @since <pre>11�� 15, 2022</pre> 
* @version 1.0 
*/ 
public class UserFriendServiceTest { 

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	*
	* Method: deleteUserFriend(String wechatId, String friendId)
	*
	*/
	@Test
	public void testDeleteUserFriend() throws Exception {
	//TODO: Test goes here...
	}

	/**
	*
	* Method: addUserFriend(String wechatId, String friendId)
	*
	*/
	@Test
	public void testAddUserFriend() throws Exception {
		UserFriendService userFriendService = new UserFriendService();
		userFriendService.addUserFriend("xiaoya", "xiaoming");
	//TODO: Test goes here...
	}


} 
