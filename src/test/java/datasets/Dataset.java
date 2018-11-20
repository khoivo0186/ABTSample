package datasets;

import org.testng.annotations.DataProvider;

import utilities.Excel;

public class Dataset {

	private static Excel excel = new Excel();
	private static String path = "./src/test/java/datasets/";
	
	@DataProvider
	public static Object[][] invalidAccounts(){
		return excel.getTableArray(path + "Account.xlsx");
	}
	
}
