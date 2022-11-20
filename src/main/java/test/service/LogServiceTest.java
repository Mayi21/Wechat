package test.service; 

import entity.LogPo;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import service.LogService;

/** 
* LogService Tester. 
* 
* @author <Authors name> 
* @since <pre>11�� 17, 2022</pre> 
* @version 1.0 
*/ 
public class LogServiceTest { 

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	*
	* Method: addLog(LogPo logPo)
	*
	*/
	@Test
	public void testAddLog() throws Exception {
		LogPo logPo = new LogPo("hello", "xiaoli", "xiaoming");
		LogService logService = new LogService();
		logService.addLog(logPo);
	//TODO: Test goes here...
	}


} 
