package test.Server; 

import server.UserList;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.LinkedList;

/** 
* UserList Tester. 
* 
* @author <Authors name> 
* @since <pre>11�� 15, 2022</pre> 
* @version 1.0 
*/ 
public class UserListTest { 

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	*
	* Method: getFriendList(String id)
	*
	*/
	@Test
	public void testGetFriendList() throws Exception {
		LinkedList<String> friendList = UserList.getFriendList("xiaoli");
		System.out.println(friendList.toString());
	}


} 
