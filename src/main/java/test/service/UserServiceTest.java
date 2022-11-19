package test.service; 

import entity.UserDo;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import service.UserService;

/** 
* UserService Tester. 
* 
* @author <Authors name> 
* @since <pre>11�� 17, 2022</pre> 
* @version 1.0 
*/ 
public class UserServiceTest { 

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	*
	* Method: main(String[] args)
	*
	*/
	@Test
	public void testMain() throws Exception {
	//TODO: Test goes here...
	}

	/**
	*
	* Method: update(UserDo userDo)
	*
	*/
	@Test
	public void testUpdate() throws Exception {
		UserService service = new UserService();
		service.updatePasswdByWechatId("1234567","xiaoming");
	//TODO: Test goes here...
	}


} 
