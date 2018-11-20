package tests.p2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import actions.Anibis;
import datasets.Dataset;
import utilities.TestBase;
import utilities.TestRunner;

public class AU01_Login_Validation extends TestBase {
	/******************************************************
	 * VARIABLES
	 ******************************************************/

	/******************************************************
	 * SETUP & TEARDOWN
	 ******************************************************/

	@BeforeMethod
	public void beforeTest() {
		// actions need to be run at beginning of every test cases
	}

	/******************************************************
	 * TEST CASES
	 ******************************************************/
	@Test(dataProvider = "invalidAccounts", dataProviderClass = Dataset.class)
	public static void verify_user_cannot_login_with_invalid_accounts(String email, String password, String emailError, String passwordError) {
		
	}
}
