package test.Client; 

import client.UserInfo;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

/** 
* UserInfo Tester. 
* 
* @author <Authors name> 
* @since <pre>11�� 16, 2022</pre> 
* @version 1.0 
*/ 
public class UserInfoTest { 

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	*
	* Method: getId(String username)
	*
	*/
	@Test
	public void testGetId() throws Exception {
	//TODO: Test goes here...
	}

	/**
	*
	* Method: getUserName(String id)
	*
	*/
	@Test
	public void testGetUserName() throws Exception {
		System.out.println(UserInfo.getUserName("xaohii"));;
	//TODO: Test goes here...
	}


} 
