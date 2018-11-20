package utilities;

import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import actions.Assertion;

public class TestBase {
	
	  @BeforeClass
	  public void setUp() {
		  TestRunner.setUp(this.getClass());
		  Driver.initializeWeb("localhost", "Chrome");
		  Driver.setPageWait(60);
		  Driver.setWebElementWait(10);
		  Driver.instanceWeb.get(Constants.URL);
	  }
	  
	  @BeforeMethod
	  public void beforeTest(Method method) {
		  TestRunner.method = method;
		  TestRunner.startMethod();
	  }

	  @AfterMethod
	  public void afterTest(Method method) {
		  Assertion.verifyTest();
		  TestRunner.endMethod();
	  }

	  @AfterClass
	  public void tearDown() {
		  TestRunner.tearDown(this.getClass());
		  Driver.close();
	  }

}
