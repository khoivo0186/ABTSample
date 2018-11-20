package actions;

import org.testng.asserts.SoftAssert;

public class Assertion {
	public static SoftAssert softAssert = new SoftAssert();
	
	public static void checkEquals(String actual, String expected, String message) {
		softAssert.assertEquals(actual, expected, message);
	}
	
	public static void checkEquals(String actual, String expected) {
		String message = "check if \"" + expected + "\" equals \"" + actual + "\")";
		softAssert.assertEquals(actual, expected, message);
	}
	
	public static void checkEquals(Boolean actual, Boolean expected, String message) {
		softAssert.assertEquals(actual, expected, message);
	}
	
	public static void checkEquals(Boolean actual, Boolean expected) {
		String message = "check if " + expected + " equals " + actual + ")";
		softAssert.assertEquals(actual, expected, message);
	}
	
	public static void checkContains(String fullString, String subString, String message) {
		if(!fullString.contains(subString)) {
			softAssert.assertTrue(false, "check if \"" + fullString + "\" contains \"" + subString + "\")");
		}
	}
	
	public static void checkContains(String fullString, String subString) {
		String message = "check if \"" + fullString + "\" contains \"" + subString + "\")";
		if(!fullString.contains(subString)) {
			softAssert.assertTrue(false, message);
		}
	}
	
	public static void verifyTest() {
		softAssert.assertAll();
		softAssert = new SoftAssert();
	}
}
